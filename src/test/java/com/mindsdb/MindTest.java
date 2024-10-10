package com.mindsdb;

import kong.unirest.core.HttpMethod;
import kong.unirest.core.MockClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MindTest {

    private final MockClient mockClient = MockClient.register();

    @BeforeEach
    void setUp() {
        mockClient.reset();
    }

    @Test
    void create() throws Exception {
        Mind expectedMind = new Mind("testmind");
        mockClient.expect(HttpMethod.POST, "/projects/mindsdb/minds")
                .thenReturn(expectedMind);
        boolean isCreated = expectedMind.create();
        assertTrue(isCreated);
        mockClient.verifyAll();
    }

    @Test
    void list() {
        List<Mind> expectedMinds = List.of(Mind.builder().name("hi").datasources(List.of("testds")).build());
        mockClient.expect(HttpMethod.GET, "/projects/mindsdb/minds")
                .thenReturn(expectedMinds);
        Optional<List<Mind>> actualMinds = Mind.list();
        assertTrue(actualMinds.isPresent());
        assertEquals(actualMinds.get(), expectedMinds);
        mockClient.verifyAll();
    }

    @Test
    void get() throws Exception {
        String mindName = "testmind";
        Mind expectedMind = new Mind(mindName);
        mockClient.expect(HttpMethod.GET, "/projects/mindsdb/minds/" + mindName)
                .thenReturn(expectedMind);
        Optional<Mind> actualMind = Mind.get(mindName);
        assertTrue(actualMind.isPresent());
        assertEquals(actualMind.get(), expectedMind);
        mockClient.verifyAll();
    }

    @Test
    void testGet() {
        String mindName = "";
        Exception exception = assertThrows(Exception.class, () -> Mind.get(mindName));
        String expectedMessage = "mind name cannot be empty string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() throws Exception {
        String mindName = "testmind";
        mockClient.expect(HttpMethod.DELETE, "/projects/mindsdb/minds/" + mindName)
                .thenReturn(true);
        boolean isDeleted = Mind.delete(mindName);
        assertTrue(isDeleted);
        mockClient.verifyAll();
    }

    @Test
    void update() throws Exception {
        String mindName = "testmind";
        String dsName = "testds";
        Mind mind = Mind.builder().name(mindName).datasources(List.of(dsName)).build();
        mockClient.expect(HttpMethod.PATCH, "/projects/mindsdb/minds/" + mindName)
                .thenReturn(true);
        boolean actualValue = Mind.update(mindName, mind);
        assertTrue(actualValue);
        mockClient.verifyAll();
    }

    @Test
    void testUpdate() {
        String mindName = "";
        String dsName = "testds";
        Mind mind = Mind.builder().name(mindName).datasources(List.of(dsName)).build();
        Exception exception = assertThrows(Exception.class, () -> Mind.update(mindName, mind));
        String expectedMessage = "mind name cannot be empty string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addDatasource() throws Exception {
        String mindName = "testmind";
        String dsName = "testds";
        mockClient.expect(HttpMethod.POST, "/projects/mindsdb/minds/" + mindName + "/datasources")
                .thenReturn(true);
        assertTrue(Mind.addDatasource(mindName, dsName, true));
        mockClient.verifyAll();
    }

    @Test
    void testAddDatasource() {
        String mindName = "";
        String dsName = "testds";
        Exception exception = assertThrows(Exception.class, () -> Mind.addDatasource(mindName, dsName, false));
        String expectedMessage = "mind name cannot be empty string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        String mindName = "";
        Exception exception = assertThrows(Exception.class, () -> Mind.delete(mindName));
        String expectedMessage = "mind name cannot be empty string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}