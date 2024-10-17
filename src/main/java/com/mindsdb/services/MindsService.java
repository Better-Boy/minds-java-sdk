package com.mindsdb.services;

import com.google.gson.JsonObject;
import com.mindsdb.models.Mind;
import com.mindsdb.utils.Constants;
import com.mindsdb.client.RestClient;
import com.mindsdb.utils.Utils;
import kong.unirest.core.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Minds.
 * Provides methods to create, retrieve, list, and delete Mind objects using a REST client.
 */
@Slf4j
public class MindsService {

    private final RestClient restClient;

    public MindsService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Creates a new Mind with the specified name.
     *
     * @param name the name of the Mind to be created
     * @return the created Mind object
     * @throws Exception if an error occurs during the creation process
     */
    public Mind create(String name) throws Exception {
        return create(name, null, null, null, null, null);
    }

    /**
     * Creates a new Mind with the specified name and associated data sources.
     *
     * @param name        the name of the Mind to be created
     * @param datasources a list of data sources to associate with the Mind
     * @return the created Mind object
     * @throws Exception if an error occurs during the creation process
     */
    public Mind create(String name, List<String> datasources) throws Exception {
        return create(name, datasources, null, null, null, null);
    }

    /**
     * Creates a new Mind with the specified parameters.
     *
     * @param name          the name of the Mind to be created
     * @param datasources   a list of data sources to associate with the Mind
     * @param modelName     the name of the model to be used
     * @param parameters    JSON object containing additional parameters for the Mind
     * @param provider      the provider to be used
     * @param promptTemplate the template for the prompt
     * @return the created Mind object
     * @throws Exception if an error occurs during the creation process
     */
    public Mind create(String name, List<String> datasources, String modelName, JsonObject parameters, String provider, String promptTemplate) throws Exception {
        Mind toBeCreatedMind = Utils.createMindFromParams(name, datasources, modelName, parameters, provider, promptTemplate);
        return create(toBeCreatedMind);
    }

    /**
     * Creates a new Mind based on the provided Mind object.
     *
     * @param mind the Mind object to be created
     * @return the created Mind object
     * @throws Exception if an error occurs during the creation process
     */
    private Mind create(Mind mind) throws Exception {
        Utils.validateMind(mind);
        if(mind.getPrompt_template() != null) mind.getParameters().addProperty(Constants.PROMPT_TEMPLATE, mind.getPrompt_template());
        if(mind.getParameters() != null && !mind.getParameters().has(Constants.PROMPT_TEMPLATE)) mind.getParameters().addProperty(Constants.PROMPT_TEMPLATE, Constants.DEFAULT_PROMPT_TEMPLATE);
        String postBody = Constants.gson.toJson(mind);
        String endPoint = String.format(Constants.CREATE_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> httpResponse = restClient.sendPostRequest(endPoint, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), mind.getName());
        return get(mind.getName()).get();
    }

    /**
     * Retrieves a list of all Minds.
     *
     * @return an Optional containing a list of Mind objects, or an empty Optional if no Minds are found
     * @throws Exception if an error occurs during the retrieval process
     */
    public Optional<List<Mind>> list() throws Exception {
        String endPoint = String.format(Constants.LIST_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> listHttpResponse = restClient.sendGetRequest(endPoint);
        List<Mind> mindList = Utils.parseStringToMindList(listHttpResponse.getBody());
        mindList.forEach(mind -> mind.setRestClient(restClient));
        return Optional.of(mindList);
    }

    /**
     * Retrieves a specific Mind by name.
     *
     * @param mindName the name of the Mind to retrieve
     * @return an Optional containing the Mind object, or an empty Optional if not found
     * @throws Exception if an error occurs during the retrieval process
     */
    public Optional<Mind> get(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        String endPoint = String.format(Constants.GET_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
        HttpResponse<String> httpResponse = restClient.sendGetRequest(endPoint);
        Mind resMind = Utils.parseStringToMind(httpResponse.getBody());
        resMind.setRestClient(restClient);
        return Optional.of(resMind);
    }

    /**
     * Deletes a specific Mind by name.
     *
     * @param mindName the name of the Mind to delete
     * @throws Exception if an error occurs during the deletion process
     */
    public void drop(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        String endPoint = String.format(Constants.DELETE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
        HttpResponse<String> httpResponse = restClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), mindName);
    }
}
