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

    private final RestClient restClient;

    public DatasourcesService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Datasource create(DatabaseConfig databaseConfig) throws Exception {
        Utils.validateDatabaseConfig(databaseConfig);
        String postBody = databaseConfig.toString();
        HttpResponse<String> httpResponse = restClient.sendPostRequest(Constants.CREATE_DATASOURCE_ENDPOINT, postBody);
        log.debug("Response code - {}, {} created", httpResponse.getStatus(), databaseConfig.getName());
        return Constants.gson.fromJson(databaseConfig.toString(), Datasource.class);
    }

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

    public Optional<List<Datasource>> list() throws Exception {
        HttpResponse<String> listHttpResponse = restClient.sendGetRequest(Constants.LIST_DATASOURCE_ENDPOINT);
        List<Datasource> datasources = Utils.parseStringToDatasourceList(listHttpResponse.getBody());
        return Optional.of(datasources);
    }

    public Optional<Datasource> get(String datasourceName) throws Exception {
        String endPoint = String.format(Constants.GET_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = restClient.sendGetRequest(endPoint);
        log.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, httpResponse.getStatus());
        Datasource datasource = Utils.parseStringToDatasource(httpResponse.getBody());
        return Optional.of(datasource);
    }

    public void drop(String datasourceName) throws Exception {
        String endPoint = String.format(Constants.DELETE_DATASOURCE_ENDPOINT, datasourceName);
        HttpResponse<String> httpResponse = restClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}, {} deleted", httpResponse.getStatus(), datasourceName);
    }

}
