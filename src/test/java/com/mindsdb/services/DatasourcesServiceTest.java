package com.mindsdb.services;

import com.mindsdb.client.Client;
import com.mindsdb.models.DatabaseConfig;
import com.mindsdb.models.Datasource;
import com.mindsdb.utils.Constants;
import com.mindsdb.utils.Utils;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
                else if (request.getPath().equals(createDsUrl)) mockResponse.setBody(generateDsResponse().toString());
                else if (request.getPath().equals(dropDsUrl)) return mockResponse;
                else mockResponse.setResponseCode(404);
                return mockResponse;
            }
        };
    }

    public static List<Datasource> listDsResponse(){
        String jsonResponse = "{\"name\":\"testds\",\"engine\":\"postgres\",\"description\":\"Postgres database\",\"connection_data\":{\"database\":\"demo\",\"host\":\"samples.mindsdb.com\",\"password\":\"demo_password\",\"port\":5432,\"schema\":\"demo_data\",\"user\":\"demo_user\"},\"tables\":[\"car_info\",\"jobs\"]}";
        return List.of(Utils.parseStringToDatasource(jsonResponse));
    }

    public static Datasource getDsResponse(){
        String jsonResponse = "{\"name\":\"testds\",\"engine\":\"postgres\",\"description\":\"Postgres database\",\"connection_data\":{\"database\":\"demo\",\"host\":\"samples.mindsdb.com\",\"password\":\"demo_password\",\"port\":5432,\"schema\":\"demo_data\",\"user\":\"demo_user\"},\"tables\":[\"car_info\",\"jobs\"]}";
        return Utils.parseStringToDatasource(jsonResponse);
    }

    public static DatabaseConfig createDsRequest(){
        return generateDsResponse();
    }

    public static Datasource generateDsResponse(){
        String jsonResponse = "{\"name\":\"testds\",\"engine\":\"postgres\",\"description\":\"Postgres database\",\"connection_data\":{\"database\":\"demo\",\"host\":\"samples.mindsdb.com\",\"password\":\"demo_password\",\"port\":5432,\"schema\":\"demo_data\",\"user\":\"demo_user\"},\"tables\":[\"car_info\",\"jobs\"]}";
        return Utils.parseStringToDatasource(jsonResponse);
    }

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.setDispatcher(createDispatcher());
        server.start(8080);
        String baseUrl = String.format("http://%s:8080", server.getHostName());
        client = new Client("api-key", baseUrl);
    }

    @Test
    void create() throws Exception {
        DatabaseConfig dsConfig = createDsRequest();
        Datasource datasource = client.datasourcesService.create(dsConfig);
        Assertions.assertNotNull(datasource);
        assert generateDsResponse().equals(datasource);
    }

    @Test
    void list() throws Exception {
        Optional<List<Datasource>> actualList = client.datasourcesService.list();
        assert actualList.isPresent();
        assert actualList.get().equals(listDsResponse());
    }

    @Test
    void get() throws Exception {
        String dsName = "test";
        Optional<Datasource> actualDs = client.datasourcesService.get(dsName);
        assert actualDs.isPresent();
        assert actualDs.get().equals(getDsResponse());
    }

    @Test
    void drop() {
        String dsName = "test";
        Assertions.assertDoesNotThrow(() -> client.datasourcesService.drop(dsName));
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
        client.shutDownConnection();
    }
}