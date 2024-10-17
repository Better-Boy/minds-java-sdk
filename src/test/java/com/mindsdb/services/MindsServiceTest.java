package com.mindsdb.services;

import com.mindsdb.client.Client;
import com.mindsdb.models.Mind;
import com.mindsdb.utils.Constants;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

class MindsServiceTest {

    private static MockWebServer server;

    private static Client client;

    private static Dispatcher createDispatcher(){
        return new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                assert request.getPath() != null;
                String mindName = "test";
                String api = "/api";
                String listMindUrl = api + String.format(Constants.LIST_MIND_ENDPOINT, Constants.MINDS_PROJECT);
                String getMindUrl = api + String.format(Constants.GET_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
                String dropMindUrl = api + String.format(Constants.DELETE_MIND_ENDPOINT, Constants.MINDS_PROJECT, mindName);
                String createMindUrl = api + String.format(Constants.CREATE_MIND_ENDPOINT, Constants.MINDS_PROJECT);
                String dsName = "testds";
                MockResponse mockResponse = new MockResponse();
                mockResponse.setResponseCode(200);
                if(request.getPath().equals(listMindUrl)) mockResponse.setBody(listMindResponse().toString());
                else if(request.getPath().equals(getMindUrl)) mockResponse.setBody(getMindResponse().toString());
                else if(request.getPath().equals(createMindUrl)) mockResponse.setBody(createMindResponse().toString());
                else if(request.getPath().equals(dropMindUrl)) return mockResponse;
                else mockResponse.setResponseCode(404);
                return mockResponse;
            }
        };
    }

    public static List<Mind> listMindResponse(){
        return List.of(Mind.builder().name("test").datasources(List.of("testds")).build());
    }

    public static Mind getMindResponse(){
        return Mind.builder().name("test").datasources(List.of("testds")).build();
    }

    public static Mind createMindResponse(){
        return Mind.builder().name("test").datasources(List.of("testds")).build();
    }

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.setDispatcher(createDispatcher());
        server.start(8080);
        String baseUrl = String.format("http://%s:8080", server.getHostName());
        client = new Client("api-key", baseUrl);
    }

    @Test
    void create() throws Exception {
        String mindName = "test";
        Mind mind = client.mindsService.create(mindName, List.of("testds"));
        Assertions.assertNotNull(mind);
        assert createMindResponse().equals(mind);
    }

    @Test
    void list() throws Exception {
        Optional<List<Mind>> actualList = client.mindsService.list();
        assert actualList.isPresent();
        assert actualList.get().equals(listMindResponse());
    }

    @Test
    void get() throws Exception {
        String mindName = "test";
        Optional<Mind> actualList = client.mindsService.get(mindName);
        assert actualList.isPresent();
        assert actualList.get().equals(getMindResponse());

    }

    @Test
    void drop() {
        String mindName = "test";
        Assertions.assertDoesNotThrow(() -> client.mindsService.drop(mindName));
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }
}