package com.mindsdb.services;

import com.google.gson.JsonObject;
import com.mindsdb.models.DatabaseConfig;
import com.mindsdb.models.Datasource;
import com.mindsdb.utils.Constants;
import kong.unirest.core.HttpMethod;
import kong.unirest.core.MockClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DatasourcesServiceTest {

    private final MockClient mockClient = MockClient.register();

    private Datasource datasource;

    @BeforeEach
    void setUp() {
        mockClient.reset();
        JsonObject connData = new JsonObject();
        connData.addProperty("username", "mindsdb");
        datasource = new Datasource("testds", "postgres", "pg connection", connData, List.of());
    }

    @Test
    void create() throws Exception {
        DatabaseConfig databaseConfig = datasource;
        mockClient.expect(HttpMethod.POST, Constants.CREATE_DATASOURCE_ENDPOINT)
                .thenReturn(true);
        Datasource actualDs = DatasourcesService.create(databaseConfig);
        assertEquals(datasource, actualDs);
        mockClient.verifyAll();
    }

    @Test
    void list() throws Exception {
        List<Datasource> expectedDs = List.of(datasource);
        mockClient.expect(HttpMethod.GET, Constants.LIST_DATASOURCE_ENDPOINT)
                .thenReturn(expectedDs);
        Optional<List<Datasource>> actualDs = DatasourcesService.list();
        assertTrue(actualDs.isPresent());
        assertEquals(actualDs.get(), expectedDs);
        mockClient.verifyAll();
    }

    @Test
    void get() throws Exception {
        mockClient.expect(HttpMethod.GET, Constants.LIST_DATASOURCE_ENDPOINT + "/" + datasource.getName())
                .thenReturn(datasource);
        Optional<Datasource> actualDs = DatasourcesService.get(datasource.getName());
        assertTrue(actualDs.isPresent());
        assertEquals(actualDs.get(), datasource);
        mockClient.verifyAll();
    }

    @Test
    void drop() {
        mockClient.expect(HttpMethod.DELETE, Constants.LIST_DATASOURCE_ENDPOINT + "/" + datasource.getName())
                .thenReturn(true);
        assertDoesNotThrow(() -> DatasourcesService.drop(datasource.getName()));
        mockClient.verifyAll();
    }
}