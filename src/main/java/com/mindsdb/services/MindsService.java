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

@Slf4j
public class MindsService {

    private final RestClient restClient;

    public MindsService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Mind create(String name) throws Exception {
        return create(name, null, null, null, null, null);
    }

    public Mind create(String name, List<String> datasources) throws Exception {
        return create(name, datasources, null, null, null, null);
    }

    private Mind create(String name, List<String> datasources, String modelName, JsonObject parameters, String provider, String promptTemplate) throws Exception {
        Mind toBeCreatedMind = Utils.createMindFromParams(name, datasources, modelName, parameters, provider, promptTemplate);
        return create(toBeCreatedMind);
    }

    private Mind create(Mind mind) throws Exception {
        Utils.validateMind(mind);
        if(mind.getPrompt_template() != null) mind.getParameters().addProperty(Constants.PROMPT_TEMPLATE, mind.getPrompt_template());
        if(!mind.getParameters().has(Constants.PROMPT_TEMPLATE)) mind.getParameters().addProperty(Constants.PROMPT_TEMPLATE, Constants.DEFAULT_PROMPT_TEMPLATE);
        String postBody = Constants.gson.toJson(mind);
        String endPoint = String.format(Constants.CREATE_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> httpResponse = restClient.sendPostRequest(endPoint, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), mind.getName());
        return get(mind.getName()).get();
    }

    public Optional<List<Mind>> list() throws Exception {
        String endPoint = String.format(Constants.LIST_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> listHttpResponse = restClient.sendGetRequest(endPoint);
        List<Mind> mindList = Utils.parseStringToMindList(listHttpResponse.getBody());
        mindList.forEach(mind -> mind.setRestClient(restClient));
        return Optional.of(mindList);
    }

    public Optional<Mind> get(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        String endPoint = String.format(Constants.GET_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
        HttpResponse<String> httpResponse = restClient.sendGetRequest(endPoint);
        Mind resMind = Utils.parseStringToMind(httpResponse.getBody());
        resMind.setRestClient(restClient);
        return Optional.of(resMind);
    }

    public void drop(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        String endPoint = String.format(Constants.DELETE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
        HttpResponse<String> httpResponse = restClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), mindName);
    }
}
