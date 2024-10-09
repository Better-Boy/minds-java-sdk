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
 * Represents a data source class for connecting to external data stores.
 * This class provides methods to create, update, retrieve, and delete data sources.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Datasource {

    private static final Logger logger = LoggerFactory.getLogger(Datasource.class);

    /** The name of the data source. */
    @NonNull
    private String name;

    /** The type of engine used for the data source. */
    @NonNull
    private String engine;

    /** A description of the data source. */
    @NonNull
    private String description;

    /** Connection data in JSON format. */
    @NonNull
    private JsonObject connection_data;

    /** A list of tables associated with the data source. */
    private List<String> tables;

    /**
     * Creates a new data source.
     *
     * @return true if the data source was created successfully; false otherwise.
     * @throws Exception if validation fails or an error occurs during the request.
     */
    public boolean create() throws Exception {
        Utils.validateDatasource(this);
        String postBody = toString();
        AtomicBoolean isCreated = new AtomicBoolean(false);
        Unirest.post(Constants.CREATE_DATASOURCE_ENDPOINT)
                .body(postBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} created", stringHttpResponse.getStatus(), name);
                    isCreated.set(true);
                });
        return isCreated.get();
    }

    /**
     * Lists all available data sources.
     *
     * @return an Optional containing a list of data sources if the request was successful;
     *         otherwise, an empty Optional.
     */
    public static Optional<List<Datasource>> list(){
        AtomicReference<Optional<List<Datasource>>> listAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.LIST_DATASOURCE_ENDPOINT)
                .asObject(new GenericType<List<Datasource>>(){})
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
                    listAtomicRef.set(Optional.of(listHttpResponse.getBody()));
                });

        return listAtomicRef.get();
    }

    /**
     * Retrieves a specific data source by name.
     *
     * @param datasourceName the name of the data source to retrieve.
     * @return an Optional containing the data source if found;
     *         otherwise, an empty Optional.
     */
    public static Optional<Datasource> get(String datasourceName) {
        AtomicReference<Optional<Datasource>> optionalDatasource = new AtomicReference<>(Optional.empty());
        Unirest.get(Constants.GET_DATASOURCE_ENDPOINT)
                .routeParam(Constants.DATASOURCE_NAME_ROUTE_PARAM, datasourceName)
                .asString()
                .ifFailure(httpResponse -> {
                    if(!httpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
                    }
                })
                .ifSuccess(httpResponse -> {
                    logger.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, httpResponse.getStatus());
                    Datasource resDatasource = Constants.gson.fromJson(httpResponse.getBody(), Datasource.class);
                    optionalDatasource.set(Optional.of(resDatasource));
                });
        return optionalDatasource.get();
    }

    /**
     * Updates the existing data source.
     *
     * @return true if the data source was updated successfully; false otherwise.
     * @throws Exception if an error occurs during the request.
     */
    public boolean update() throws Exception {
        String patchBody = Utils.generateDatasourceUpdateBody(this);
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        Unirest.patch(Constants.UPDATE_DATASOURCE_ENDPOINT)
                .routeParam(Constants.DATASOURCE_NAME_ROUTE_PARAM, name)
                .body(patchBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    System.out.println(stringHttpResponse.getRequestSummary().getUrl());
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
     * Deletes a data source by name.
     *
     * @param datasourceName the name of the data source to delete.
     * @return true if the data source was deleted successfully; false otherwise.
     */
    public static boolean delete(String datasourceName) {
        AtomicBoolean isDeleted = new AtomicBoolean(false);
        Unirest.delete(Constants.DELETE_DATASOURCE_ENDPOINT)
                .routeParam(Constants.DATASOURCE_NAME_ROUTE_PARAM, datasourceName)
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
                    logger.debug("Response code - {}, {} deleted", httpResponse.getStatus(), datasourceName);
                    isDeleted.set(true);
                });
        return isDeleted.get();
    }

    /**
     * Deletes the current instance of the data source.
     *
     * @return true if the data source was deleted successfully; false otherwise.
     */
    public boolean delete(){
        return delete(name);
    }

    /**
     * Converts the data source object to its JSON representation.
     *
     * @return a JSON string representation of the data source.
     */
    @Override
    public String toString(){
        return Constants.gson.toJson(this);
    }
}
