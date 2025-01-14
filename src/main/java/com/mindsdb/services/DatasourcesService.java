package com.mindsdb.services;

import com.mindsdb.models.Datasource;
import com.mindsdb.utils.Constants;
import com.mindsdb.client.RestClient;
import com.mindsdb.models.DatabaseConfig;
import com.mindsdb.utils.Utils;
import kong.unirest.core.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing data sources.
 * Provides methods to create, retrieve, list, and delete data sources using a REST client.
 */
@Slf4j
public class DatasourcesService {

    private final RestClient restClient;

    public DatasourcesService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Creates a new data source with the given database configuration.
     *
     * @param databaseConfig the configuration of the database to create a data source for
     * @return the created Datasource object
     * @throws Exception if an error occurs during the creation process
     */
    public Datasource create(DatabaseConfig databaseConfig) throws Exception {
        Utils.validateDatabaseConfig(databaseConfig);
        String postBody = databaseConfig.toString();
        HttpResponse<String> httpResponse = restClient.sendPostRequest(Constants.CREATE_DATASOURCE_ENDPOINT, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), databaseConfig.getName());
        return Constants.gson.fromJson(databaseConfig.toString(), Datasource.class);
    }

    /**
     * Creates a new data source with the given database configuration, with an option to replace an existing data source.
     *
     * @param databaseConfig the configuration of the database to create a data source for
     * @param replace       if true, replaces the existing data source with the same name
     * @return the created Datasource object
     * @throws Exception if an error occurs during the creation process
     */
    public Datasource create(DatabaseConfig databaseConfig, boolean replace) throws Exception {
        Utils.validateDatabaseConfig(databaseConfig);
        if(replace){
            get(databaseConfig.getName());
            drop(databaseConfig.getName());
        }
        String postBody = databaseConfig.toString();
        HttpResponse<String> httpResponse = restClient.sendPostRequest(Constants.CREATE_DATASOURCE_ENDPOINT, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), databaseConfig.getName());
        return Constants.gson.fromJson(databaseConfig.toString(), Datasource.class);
    }

    /**
     * Retrieves a list of all data sources.
     *
     * @return an Optional containing a list of Datasource objects, or an empty Optional if no data sources are found
     * @throws Exception if an error occurs during the retrieval process
     */
    public Optional<List<Datasource>> list() throws Exception {
        HttpResponse<String> listHttpResponse = restClient.sendGetRequest(Constants.LIST_DATASOURCE_ENDPOINT);
        List<Datasource> datasources = Utils.parseStringToDatasourceList(listHttpResponse.getBody());
        return Optional.of(datasources);
    }

    /**
     * Retrieves a specific data source by name.
     *
     * @param datasourceName the name of the data source to retrieve
     * @return an Optional containing the Datasource object, or an empty Optional if not found
     * @throws Exception if an error occurs during the retrieval process
     */
    public Optional<Datasource> get(String datasourceName) throws Exception {
        String endPoint = String.format(Constants.GET_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = restClient.sendGetRequest(endPoint);
        log.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, httpResponse.getStatus());
        Datasource datasource = Utils.parseStringToDatasource(httpResponse.getBody());
        return Optional.of(datasource);
    }

    /**
     * Deletes a specific data source by name.
     *
     * @param datasourceName the name of the data source to delete
     * @throws Exception if an error occurs during the deletion process
     */
    public void drop(String datasourceName) throws Exception {
        String endPoint = String.format(Constants.DELETE_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = restClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), datasourceName);
    }

}
