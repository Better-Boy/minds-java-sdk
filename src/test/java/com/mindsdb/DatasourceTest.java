package com.mindsdb;

import com.google.gson.JsonObject;
import kong.unirest.core.HttpMethod;
import kong.unirest.core.MockClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DatasourceTest {

    private final MockClient mockClient = MockClient.register();

    private Datasource datasource;

    @BeforeEach
    void setUp() {
        mockClient.reset();
        JsonObject connData = new JsonObject();
        connData.addProperty("username", "mindsdb");
        datasource = Datasource.builder()
                .name("testds")
                .engine("postgres")
                .description("pg connection")
                .connection_data(connData).build();
    }

    @Test
    void create() throws Exception {
        mockClient.expect(HttpMethod.POST, Constants.CREATE_DATASOURCE_ENDPOINT)
                .thenReturn(true);
        assertTrue(datasource.create());
        mockClient.verifyAll();
    }

    @Test
    void list() {
        List<Datasource> expectedDs = List.of(datasource);
        mockClient.expect(HttpMethod.GET, Constants.LIST_DATASOURCE_ENDPOINT)
                .thenReturn(expectedDs);
        Optional<List<Datasource>> actualDs = Datasource.list();
        assertTrue(actualDs.isPresent());
        assertEquals(actualDs.get(), expectedDs);
        mockClient.verifyAll();
    }

    @Test
    void get() {
        mockClient.expect(HttpMethod.GET, Constants.LIST_DATASOURCE_ENDPOINT + "/" + datasource.getName())
                .thenReturn(datasource);
        Optional<Datasource> actualDs = Datasource.get(datasource.getName());
        assertTrue(actualDs.isPresent());
        assertEquals(actualDs.get(), datasource);
        mockClient.verifyAll();
    }

    @Test
    void update() throws Exception {
        mockClient.expect(HttpMethod.PATCH, Constants.LIST_DATASOURCE_ENDPOINT + "/" + datasource.getName())
                .thenReturn(true);
        assertTrue(datasource.update());
        mockClient.verifyAll();
    }

    @Test
    void delete() {
        mockClient.expect(HttpMethod.DELETE, Constants.LIST_DATASOURCE_ENDPOINT + "/" + datasource.getName())
                .thenReturn(true);
        boolean isDeleted = Datasource.delete(datasource.getName());
        assertTrue(isDeleted);
        mockClient.verifyAll();
    }
}