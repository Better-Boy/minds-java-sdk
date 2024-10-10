package com.mindsdb;

import com.google.gson.JsonObject;
import com.mindsdb.utils.RestUtils;
import com.mindsdb.utils.Utils;
import kong.unirest.core.HttpResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code Mind} class represents a model for handling minds in the system.
 * It encapsulates the properties and operations related to a mind, including
 * creation, retrieval, updating, deletion, and datasource management.
 * <p>
 * Each {@code Mind} instance contains a name, a list of associated datasources,
 * and additional metadata. The class provides static methods for performing
 * operations with minds via an API, utilizing the Unirest library for HTTP requests.
 * </p>
 * <p>
 * The class uses Gson for JSON serialization and deserialization.
 * </p>
 *
 * @see Utils
 * @see Constants
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Slf4j
public class Mind {

    /**
     * The name of the mind.
     */
    @NonNull private String name;

    /**
     * A list of datasource names associated with the mind.
     */
    @Builder.Default private List<String> datasources = new ArrayList<>();

    /**
     * The creation timestamp of the mind.
     */
    private String created_at;

    /**
     * The name of the model associated with the mind.
     */
    private String model_name;

    /**
     * Additional parameters for the mind, represented as a JSON object.
     */
    private JsonObject parameters;

    /**
     * The provider of the model_name.
     */
    private String provider;

    /**
     * The last updated timestamp of the mind.
     */
    private String updated_at;

    /**
     * Creates a new mind using the provided {@code Mind} object.
     *
     * @param mind the mind to be created
     * @return an {@code Optional<Mind>} containing the created mind if successful, or empty if the creation failed
     * @throws Exception if validation fails or an error occurs during the request
     */
    private boolean create(Mind mind) throws Exception {
        Utils.validateMind(mind);
        String postBody = Constants.gson.toJson(mind);
        String endPoint = String.format(Constants.CREATE_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> httpResponse = RestUtils.sendPostRequest(endPoint, postBody);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return false;
        }

        log.debug("Response code - {}, {} created", httpResponse.getStatus(), mind.name);
        return true;
    }

    /**
     * Creates the current mind instance.
     *
     * @return an {@code Optional<Mind>} containing the created mind if successful, or empty if the creation failed
     * @throws Exception if validation fails or an error occurs during the request
     */
    public boolean create() throws Exception {
        return create(this);
    }

    /**
     * Retrieves a list of all minds associated with the 'mindsdb' project.
     *
     * @return an {@code Optional<List<Mind>>} containing the list of minds if successful, or empty if the request failed
     */
    public static Optional<List<Mind>> list(){
        String endPoint = String.format(Constants.LIST_MIND_ENDPOINT, Constants.MINDS_PROJECT);
        HttpResponse<String> listHttpResponse = RestUtils.sendGetRequest(endPoint);

        if(!listHttpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, listHttpResponse.getStatus(), listHttpResponse.getBody());
            return Optional.empty();
        }

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
        HttpResponse<String> httpResponse = RestUtils.sendGetRequest(endPoint);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return Optional.empty();
        }

        Mind resMind = Utils.parseStringToMind(httpResponse.getBody());
        return Optional.of(resMind);
    }

    public Optional<Mind> get() throws Exception {
        return get(name);
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
        HttpResponse<String> httpResponse = RestUtils.sendDeleteRequest(endPoint);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return false;
        }
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), mindName);
        return true;
    }

    /**
     * Updates an existing mind with the provided new mind data.
     *
     * @param existingMindName the name of the existing mind
     * @param newMind the new mind data to update
     * @return {@code true} if the mind was updated successfully, {@code false} otherwise
     */
    public static boolean update(String existingMindName, Mind newMind) throws Exception {
        Utils.validateMindName(existingMindName);

        Utils.validateMindName(newMind.getName());

        String patchBody = Constants.gson.toJson(newMind);

        String endPoint = String.format(Constants.UPDATE_MIND_ENDPOINT, Constants.MINDS_PROJECT, existingMindName);

        HttpResponse<String> httpResponse = RestUtils.sendPatchRequest(endPoint, patchBody);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return false;
        }

        log.debug("Response code - {}, {} updated", httpResponse.getStatus(), existingMindName);
        return true;
    }

    /**
     * Updates the current mind instance.
     *
     * @return {@code true} if the mind was updated successfully, {@code false} otherwise
     */
    public boolean update() throws Exception {
        return update(name, this);
    }

    /**
     * Adds a new datasource to the specified mind.
     *
     * @param mindName the name of the mind to which the datasource will be added
     * @param newDatasourceName the name of the new datasource
     * @param checkConnection whether to check the connection of the datasource
     * @return {@code true} if the datasource was added successfully, {@code false} otherwise
     * @throws Exception if validation fails or an error occurs during the request
     */
    public static boolean addDatasource(String mindName, String newDatasourceName, boolean checkConnection) throws Exception {
        Utils.validateMindName(mindName);
        String postBody = Utils.createRequestBodyForAddDs(newDatasourceName, checkConnection);

        String endPoint = String.format(Constants.ADD_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);

        HttpResponse<String> httpResponse = RestUtils.sendPostRequest(endPoint, postBody);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return false;
        }

        log.debug("Response code - {}. New {} datasource added", httpResponse.getStatus(), newDatasourceName);
        return true;
    }

    /**
     * Adds a new datasource to the current mind.
     *
     * @param newDatasourceName the name of the new datasource
     * @return {@code true} if the datasource was added successfully, {@code false} otherwise
     * @throws Exception if validation fails or an error occurs during the request
     */
    public boolean addDatasource(String newDatasourceName) throws Exception {
        Utils.validateDatasourceName(newDatasourceName);
        return addDatasource(name, newDatasourceName, true);
    }

    /**
     * Deletes the current mind instance.
     *
     * @return {@code true} if the mind was deleted successfully, {@code false} otherwise
     * @throws Exception if an error occurs during the request
     */
    public boolean delete() throws Exception {
        return delete(name);
    }

    /**
     * Returns a JSON representation of the current mind instance.
     *
     * @return a JSON string representing the mind
     */
    @Override
    public String toString(){
        return Constants.gson.toJson(this);
    }
}
