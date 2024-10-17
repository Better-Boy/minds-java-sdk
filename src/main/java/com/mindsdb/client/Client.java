package com.mindsdb.client;

import com.mindsdb.services.DatasourcesService;
import com.mindsdb.services.MindsService;
import lombok.Getter;

/**
 * The {@code Client} class provides an interface for interacting with the Minds and Datasources services.
 * It manages the creation and lifecycle of the {@link RestClient}, {@link MindsService},
 * and {@link DatasourcesService} instances.
 * <p>
 * This class is designed to facilitate communication with an API using the specified API key and optional
 * base URL.
 * </p>
 */
public class Client {

    private final RestClient restClient;
    public final MindsService mindsService;
    public final DatasourcesService datasourcesService;

    /**
     * Constructs a new {@code Client} with the specified API key.
     * This will create a default {@link RestClient} instance using the provided API key.
     * <p>
     * The {@code MindsService} and {@code DatasourcesService} will be initialized using the created
     * {@link RestClient}.
     * </p>
     *
     * @param apiKey the API key used for authentication
     */
    public Client(String apiKey) {
        this.restClient = new RestClient(apiKey);
        this.mindsService = new MindsService(restClient);
        this.datasourcesService = new DatasourcesService(restClient);
    }

    /**
     * Constructs a new {@code Client} with the specified API key and base URL.
     * This allows for communication with an API located at a custom base URL.
     * <p>
     * The {@code MindsService} and {@code DatasourcesService} will be initialized using the created
     * {@link RestClient}.
     * </p>
     *
     * @param apiKey  the API key used for authentication
     * @param baseUrl the base URL of the API
     */
    public Client(String apiKey, String baseUrl) {
        this.restClient = new RestClient(apiKey, baseUrl);
        this.mindsService = new MindsService(restClient);
        this.datasourcesService = new DatasourcesService(restClient);
    }

    /**
     * Shuts down the connection managed by the {@link RestClient}.
     * This method should be called when the client is no longer needed
     * to release resources associated with the connection.
     */
    public void shutDownConnection(){
        restClient.shutDown();
    }

}
