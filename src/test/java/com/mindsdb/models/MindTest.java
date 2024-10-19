package com.mindsdb.models;

import com.mindsdb.client.RestClient;
import com.mindsdb.utils.Constants;
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

class MindTest {

    private static MockWebServer server;
    private static RestClient restClient;

    private static Dispatcher createDispatcher(){
        return new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                assert request.getPath() != null;
                String mindName = "test";
                String api = "/api";
                String dsName = "testds";
                String updateMindUrl = api + String.format(Constants.UPDATE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
                String addDsMindUrl = api + String.format(Constants.ADD_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
                String dropDsMindUrl = api + String.format(Constants.DEL_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName, dsName);
                MockResponse mockResponse = new MockResponse();
                mockResponse.setResponseCode(200);
                if (request.getPath().equals(updateMindUrl) || request.getPath().equals(dropDsMindUrl) || request.getPath().equals(addDsMindUrl)) return mockResponse;
                mockResponse.setResponseCode(404);
                return mockResponse;
            }
        };
    }

    public static Mind createMind(){
        String dsName = "testds";
        String mindName = "test";
        return Mind.builder().name(mindName).datasources(List.of(dsName)).restClient(restClient).build();
    }

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.setDispatcher(createDispatcher());
        server.start(8080);
        String baseUrl = String.format("http://%s:8080", server.getHostName());
        restClient = new RestClient("api-key", baseUrl);
    }

    @Test
    void update() {
        String newMindName = "newtest";
        Mind mind = createMind();
        Assertions.assertDoesNotThrow(() -> mind.update(newMindName, null, null, null, null, null));
    }

    @Test
    void addDatasource() {
        String dsDropName = "testds";
        Mind mind = createMind();
        Assertions.assertDoesNotThrow(() -> mind.addDatasource(dsDropName));
    }

    @Test
    void dropDatasource() {
        String dsDropName = "testds";
        Mind mind = createMind();
        Assertions.assertDoesNotThrow(() -> mind.dropDatasource(dsDropName));
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
        restClient.shutDown();
    }
}