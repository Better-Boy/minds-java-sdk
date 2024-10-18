package com.mindsdb.services;

import com.mindsdb.client.Client;
import com.mindsdb.models.Mind;
import com.mindsdb.utils.Constants;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

class DatasourcesServiceTest {

    private static MockWebServer server;

    private static Client client;

    private static Dispatcher createDispatcher(){
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                assert request.getPath() != null;
                String dsName = "test";
                String api = "/api";
                String listDsUrl = api + Constants.LIST_DATASOURCE_ENDPOINT;
                String getDsUrl = api + String.format(Constants.GET_DATASOURCE_ENDPOINT, dsName);
                String dropDsUrl = api + String.format(Constants.DELETE_DATASOURCE_ENDPOINT, dsName);
                String createDsUrl = api + Constants.CREATE_DATASOURCE_ENDPOINT;
                MockResponse mockResponse = new MockResponse();
                mockResponse.setResponseCode(200);
                if (request.getPath().equals(listDsUrl)) mockResponse.setBody(listDsResponse().toString());
                else if (request.getPath().equals(getDsUrl)) mockResponse.setBody(getDsResponse().toString());
                else if (request.getPath().equals(createDsUrl)) mockResponse.setBody(createDsResponse().toString());
                else if (request.getPath().equals(dropDsUrl)) return mockResponse;
                else mockResponse.setResponseCode(404);
                return mockResponse;
            }
        };
    }

    public static List<Mind> listDsResponse(){
        return List.of(Mind.builder().name("test").datasources(List.of("testds")).build());
    }

    public static Mind getDsResponse(){
        return Mind.builder().name("test").datasources(List.of("testds")).build();
    }

    public static Mind createDsResponse(){
        return Mind.builder().name("test").datasources(List.of("testds")).build();
    }

    @Test
    void create() {
    }

    @Test
    void testCreate() {
    }

    @Test
    void list() {
    }

    @Test
    void get() {
    }

    @Test
    void drop() {
    }
}