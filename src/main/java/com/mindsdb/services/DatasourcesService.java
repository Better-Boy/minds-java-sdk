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

@Slf4j
public class DatasourcesService {


    /**
     * Creates a new data source.
     *
     * @throws Exception if validation fails or an error occurs during the request.
     */
    public static Datasource create(DatabaseConfig databaseConfig) throws Exception {
        Utils.validateDatabaseConfig(databaseConfig);
        String postBody = databaseConfig.toString();
        HttpResponse<String> httpResponse = RestClient.sendPostRequest(Constants.CREATE_DATASOURCE_ENDPOINT, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), databaseConfig.getName());
        return Constants.gson.fromJson(databaseConfig.toString(), Datasource.class);
    }

    public static Datasource create(DatabaseConfig databaseConfig, boolean replace) throws Exception {
        Utils.validateDatabaseConfig(databaseConfig);
        if(replace){
            get(databaseConfig.getName());
            drop(databaseConfig.getName());
        }
        String postBody = databaseConfig.toString();
        HttpResponse<String> httpResponse = RestClient.sendPostRequest(Constants.CREATE_DATASOURCE_ENDPOINT, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), databaseConfig.getName());
        return Constants.gson.fromJson(databaseConfig.toString(), Datasource.class);
    }

    /**
     * Lists all available data sources.
     *
     * @return an Optional containing a list of data sources if the request was successful;
     *         otherwise, an empty Optional.
     */
    public static Optional<List<Datasource>> list() throws Exception {
        HttpResponse<String> listHttpResponse = RestClient.sendGetRequest(Constants.LIST_DATASOURCE_ENDPOINT);
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
    public static Optional<Datasource> get(String datasourceName) throws Exception {
        String endPoint = String.format(Constants.GET_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = RestClient.sendGetRequest(endPoint);
        log.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, httpResponse.getStatus());
        Datasource datasource = Utils.parseStringToDatasource(httpResponse.getBody());
        return Optional.of(datasource);
    }

    /**
     * Deletes a data source by name.
     *
     * @param datasourceName the name of the data source to delete.
     */
    public static void drop(String datasourceName) throws Exception {
        String endPoint = String.format(Constants.DELETE_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = RestClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), datasourceName);
    }


}
