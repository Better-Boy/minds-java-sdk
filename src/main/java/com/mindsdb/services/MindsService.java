package com.mindsdb.services;

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

    /**
     * Creates a new mind using the provided {@code Mind} object.
     *
     * @param mind the mind to be created
     * @return an {@code Optional<Mind>} containing the created mind if successful, or empty if the creation failed
     * @throws Exception if validation fails or an error occurs during the request
     */
    public static Mind create(Mind mind) throws Exception {
        Utils.validateMind(mind);
        String postBody = Constants.gson.toJson(mind);
        String endPoint = String.format(Constants.CREATE_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> httpResponse = RestClient.sendPostRequest(endPoint, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), mind.getName());
        return mind;
    }


    /**
     * Retrieves a list of all minds associated with the 'mindsdb' project.
     *
     * @return an {@code Optional<List<Mind>>} containing the list of minds if successful, or empty if the request failed
     */
    public static Optional<List<Mind>> list() throws Exception {
        String endPoint = String.format(Constants.LIST_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> listHttpResponse = RestClient.sendGetRequest(endPoint);
        List<Mind> mindList = Utils.parseStringToMindList(listHttpResponse.getBody());
        return Optional.of(mindList);
    }

    /**
     * Retrieves a specific mind by its name.
     *
     * @param mindName the name of the mind to retrieve
     * @return an {@code Optional<Mind>} containing the retrieved mind if successful, or empty if not found
     * @throws Exception if validation fails or an error occurs during the request
     */
    public static Optional<Mind> get(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        String endPoint = String.format(Constants.GET_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
        HttpResponse<String> httpResponse = RestClient.sendGetRequest(endPoint);
        Mind resMind = Utils.parseStringToMind(httpResponse.getBody());
        return Optional.of(resMind);
    }

    /**
     * Deletes a specific mind by its name.
     *
     * @param mindName the name of the mind to delete
     * @return {@code true} if the mind was deleted successfully, {@code false} otherwise
     * @throws Exception if validation fails or an error occurs during the request
     */
    public static boolean delete(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        String endPoint = String.format(Constants.DELETE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
        HttpResponse<String> httpResponse = RestClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), mindName);
        return true;
    }
}
