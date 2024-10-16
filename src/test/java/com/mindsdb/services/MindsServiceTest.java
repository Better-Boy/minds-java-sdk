package com.mindsdb.services;

import com.mindsdb.client.Client;
import com.mindsdb.models.Mind;
import kong.unirest.core.HttpMethod;
import kong.unirest.core.MockClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MindsServiceTest {

    private final MockClient mockClient = MockClient.register();
    private Client client;

    @BeforeEach
    void setUp() {
        mockClient.reset();
        client = new Client("api-key");
    }

    @Test
    void create() {
    }

    @Test
    void testCreate() {
    }

    @Test
    void list() throws Exception {
    }

    @Test
    void get() {
    }

    @Test
    void drop() {
    }
}