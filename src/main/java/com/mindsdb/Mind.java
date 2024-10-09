package com.mindsdb;

import com.google.gson.JsonObject;
import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
public class Mind {

    private static final Logger logger = LoggerFactory.getLogger(Mind.class);

    /**
     * The name of the mind.
     */
    @NonNull private String name;

    /**
     * A list of datasource names associated with the mind.
     */
    @NonNull private List<String> datasources;

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
    private static Optional<Mind> create(Mind mind) throws Exception {
        Utils.validateMind(mind);
        String postBody = Constants.gson.toJson(mind);
        AtomicReference<Optional<Mind>> mindAtomicRef = new AtomicReference<>();
        Unirest.post(Constants.CREATE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .body(postBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} created", stringHttpResponse.getStatus(), mind.name);
                    mindAtomicRef.set(Optional.of(mind));
                });
        return mindAtomicRef.get();
    }

    /**
     * Creates a new mind with the specified name and datasources.
     *
     * @param mindName the name of the new mind
     * @param datasources the list of datasource names
     * @return an {@code Optional<Mind>} containing the created mind if successful, or empty if the creation failed
     * @throws Exception if validation fails or an error occurs during the request
     */
    public static Optional<Mind> create(String mindName, List<String> datasources) throws Exception {
        return create(new Mind(mindName, datasources));
    }

    /**
     * Creates the current mind instance.
     *
     * @return an {@code Optional<Mind>} containing the created mind if successful, or empty if the creation failed
     * @throws Exception if validation fails or an error occurs during the request
     */
    public Optional<Mind> create() throws Exception {
        return create(this);
    }

    /**
     * Retrieves a list of all minds associated with the 'mindsdb' project.
     *
     * @return an {@code Optional<List<Mind>>} containing the list of minds if successful, or empty if the request failed
     */
    public static Optional<List<Mind>> list(){
        AtomicReference<Optional<List<Mind>>> mindListAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.LIST_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .asObject(new GenericType<List<Mind>>(){})
                .ifFailure(listHttpResponse -> {

                    if(!listHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, listHttpResponse.getStatus(), listHttpResponse.getBody());
                    }

                    listHttpResponse.getParsingError().ifPresent(parsingException -> {
                        logger.error(Constants.FAILED_REQUEST_PARSE_LOG, parsingException);
                        logger.error(Constants.FAILED_REQUEST_RESPONSE_BODY_LOG, parsingException.getOriginalBody());
                    });
                })
                .ifSuccess(listHttpResponse -> {
                    logger.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, listHttpResponse.getStatus());
                    mindListAtomicRef.set(Optional.of(listHttpResponse.getBody()));
                });
        return mindListAtomicRef.get();
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
        AtomicReference<Optional<Mind>> mindAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.GET_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .asString()
                .ifFailure(mindHttpResponse -> {
                    if(!mindHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, mindHttpResponse.getStatus(), mindHttpResponse.getBody());
                    }
                })
                .ifSuccess(mindHttpResponse -> {
                    logger.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, mindHttpResponse.getStatus());
                    Mind resMind = Constants.gson.fromJson(mindHttpResponse.getBody(), Mind.class);
                    mindAtomicRef.set(Optional.of(resMind));
                });
        return mindAtomicRef.get();
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
        AtomicBoolean isDeleted = new AtomicBoolean(false);
        Unirest.delete(Constants.DELETE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .asString()
                .ifFailure(httpResponse -> {
                    if(!httpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
                    }
                    httpResponse.getParsingError().ifPresent(parsingException -> {
                        logger.error(Constants.FAILED_REQUEST_PARSE_LOG, parsingException);
                        logger.error(Constants.FAILED_REQUEST_RESPONSE_BODY_LOG, parsingException.getOriginalBody());
                    });
                })
                .ifSuccess(httpResponse -> {
                    logger.debug("Response code - {}, {} deleted", httpResponse.getStatus(), mindName);
                    isDeleted.set(true);
                });
        return isDeleted.get();
    }

    /**
     * Updates an existing mind with the provided new mind data.
     *
     * @param existingMindName the name of the existing mind
     * @param newMind the new mind data to update
     * @return {@code true} if the mind was updated successfully, {@code false} otherwise
     */
    public static boolean update(String existingMindName, Mind newMind) {
        String patchBody = Constants.gson.toJson(newMind);
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        Unirest.patch(Constants.UPDATE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, existingMindName)
                .body(patchBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} updated", stringHttpResponse.getStatus(), existingMindName);
                    isUpdated.set(true);
                });
        return isUpdated.get();
    }

    /**
     * Updates the current mind instance.
     *
     * @return {@code true} if the mind was updated successfully, {@code false} otherwise
     */
    public boolean update() {
        String patchBody = Constants.gson.toJson(this);
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        Unirest.patch(Constants.UPDATE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, name)
                .body(patchBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} updated", stringHttpResponse.getStatus(), name);
                    isUpdated.set(true);
                });
        return isUpdated.get();
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
        AtomicBoolean isDatasourceAdded = new AtomicBoolean(false);
        String postBody = Utils.createRequestBodyForAddDs(newDatasourceName, checkConnection);
        System.out.println(postBody);
        Unirest.post(Constants.ADD_DATASOURCE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .body(postBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    System.out.println(stringHttpResponse.getRequestSummary().getUrl());
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}. New {} datasource added", stringHttpResponse.getStatus(), newDatasourceName);
                    isDatasourceAdded.set(true);
                });
        return isDatasourceAdded.get();
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
