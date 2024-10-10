package com.mindsdb;

import com.google.gson.JsonObject;
import com.mindsdb.utils.RestUtils;
import com.mindsdb.utils.Utils;
import kong.unirest.core.HttpResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a data source class for connecting to external data stores.
 * This class provides methods to create, update, retrieve, and delete data sources.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Slf4j
public class Datasource {

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
    @Builder.Default private List<String> tables = new ArrayList<>();

    /**
     * Creates a new data source.
     *
     * @return true if the data source was created successfully; false otherwise.
     * @throws Exception if validation fails or an error occurs during the request.
     */
    public boolean create() throws Exception {
        Utils.validateDatasource(this);
        String postBody = toString();
        HttpResponse<String> httpResponse = RestUtils.sendPostRequest(Constants.CREATE_DATASOURCE_ENDPOINT, postBody);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return false;
        }

        log.debug("Response code - {}, {} created", httpResponse.getStatus(), name);
        return true;
    }

    /**
     * Lists all available data sources.
     *
     * @return an Optional containing a list of data sources if the request was successful;
     *         otherwise, an empty Optional.
     */
    public static Optional<List<Datasource>> list(){
        HttpResponse<String> listHttpResponse = RestUtils.sendGetRequest(Constants.LIST_DATASOURCE_ENDPOINT);

        if(!listHttpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, listHttpResponse.getStatus(), listHttpResponse.getBody());
            return Optional.empty();
        }

        List<Datasource> datasources = Utils.parseStringToDatasourceList(listHttpResponse.getBody());
        return Optional.of(datasources);
    }

    /**
     * Retrieves a specific data source by name.
     *
     * @param datasourceName the name of the data source to retrieve.
     * @return an Optional containing the data source if found;
     *         otherwise, an empty Optional.
     */
    public static Optional<Datasource> get(String datasourceName) {
        String endPoint = String.format(Constants.GET_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = RestUtils.sendGetRequest(endPoint);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return Optional.empty();
        }

        log.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, httpResponse.getStatus());
        Datasource datasource = Utils.parseStringToDatasource(httpResponse.getBody());
        return Optional.of(datasource);
    }

    /**
     * Updates the existing data source.
     *
     * @return true if the data source was updated successfully; false otherwise.
     */
    public boolean update() {
        String patchBody = Utils.generateDatasourceUpdateBody(this);
        String endPoint = String.format(Constants.GET_DATASOURCE_ENDPOINT, name);
        HttpResponse<String> httpResponse = RestUtils.sendPatchRequest(endPoint, patchBody);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return false;
        }

        log.debug("Response code - {}, {} updated", httpResponse.getStatus(), name);
        return true;
    }

    /**
     * Deletes a data source by name.
     *
     * @param datasourceName the name of the data source to delete.
     * @return true if the data source was deleted successfully; false otherwise.
     */
    public static boolean delete(String datasourceName) {
        String endPoint = String.format(Constants.GET_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = RestUtils.sendDeleteRequest(endPoint);

        if(!httpResponse.isSuccess()){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            return false;
        }
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), datasourceName);
        return true;
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
