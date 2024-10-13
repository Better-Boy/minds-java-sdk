package com.mindsdb.services;

import com.mindsdb.models.Mind;
import kong.unirest.core.HttpMethod;
import kong.unirest.core.MockClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class MindsServiceTest {

    private final MockClient mockClient = MockClient.register();

    @BeforeEach
    void setUp() {
        mockClient.reset();
    }

    @Test
    void create() {
        Mind expectedMind = new Mind("testmind");
        mockClient.expect(HttpMethod.POST, "/projects/mindsdb/minds")
                .thenReturn(expectedMind);
        AtomicReference<Mind> actualMind = new AtomicReference<>();
        assertDoesNotThrow(() -> actualMind.set(MindsService.create(expectedMind)));
        assertEquals(expectedMind, actualMind.get());
        mockClient.verifyAll();
    }

    @Test
    void list() throws Exception {
        List<Mind> expectedMinds = List.of(Mind.builder().name("hi").datasources(List.of("testds")).build());
        mockClient.expect(HttpMethod.GET, "/projects/mindsdb/minds")
                .thenReturn(expectedMinds);
        Optional<List<Mind>> actualMinds = MindsService.list();
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
        Optional<Mind> actualMind = MindsService.get(mindName);
        assertTrue(actualMind.isPresent());
        assertEquals(actualMind.get(), expectedMind);
        mockClient.verifyAll();
    }

    @Test
    void get_error() {
        String mindName = "";
        Exception exception = assertThrows(Exception.class, () -> MindsService.get(mindName));
        String expectedMessage = "mind name cannot be empty string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void drop() throws Exception {
        String mindName = "testmind";
        mockClient.expect(HttpMethod.DELETE, "/projects/mindsdb/minds/" + mindName)
                .thenReturn(true);
        assertDoesNotThrow(() -> MindsService.drop(mindName));
        mockClient.verifyAll();
    }
}