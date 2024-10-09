package com.mindsdb;

import kong.unirest.core.Cache;
import kong.unirest.core.Unirest;

import java.util.concurrent.TimeUnit;

/**
 * A utility class for initializing the MindsDB API configuration.
 * This class provides methods to set up the API key and base URL for
 * making requests to the MindsDB service.
 */
public class MindsDb {

    // Private constructor to prevent instantiation
    private MindsDb() { }

    /**
     * Initializes the MindsDB API with the provided API key and
     * the default base URL.
     *
     * This method configures the Unirest HTTP client to manage cookies,
     * set the default base URL, and add necessary headers for authorization
     * and content type. It also configures response caching and retry behavior.
     *
     * @param apiKey the API key for authenticating requests to the MindsDB service.
     */
    public static synchronized void init(String apiKey) {
        Unirest.config()
                .enableCookieManagement(true)
                .defaultBaseUrl(Constants.MINDS_CLOUD_ENDPOINT)
                .addDefaultHeader(Constants.AUTHORIZATION_HEADER,"Bearer " + apiKey)
                .addDefaultHeader(Constants.CONTEXT_TYPE_HEADER, Constants.APPLICATION_JSON)
                .cacheResponses(new Cache.Builder().maxAge(1, TimeUnit.MINUTES))
                .retryAfter(true, 2);
    }

    /**
     * Initializes the MindsDB API with the provided API key and a custom base URL.
     *
     * This method configures the Unirest HTTP client similarly to the other
     * init method, but allows for a custom base URL. It ensures that the
     * base URL ends with the Minds API endpoint.
     *
     * @param apiKey  the API key for authenticating requests to the MindsDB service.
     * @param baseUrl the custom base URL for the MindsDB API.
     */
    public static synchronized void init(String apiKey, String baseUrl) {
        baseUrl = baseUrl.strip();
        if(!baseUrl.endsWith(Constants.MINDS_API_ENDPOINT)) baseUrl+=Constants.MINDS_API_ENDPOINT;
        Unirest.config()
                .enableCookieManagement(true)
                .defaultBaseUrl(baseUrl)
                .addDefaultHeader(Constants.AUTHORIZATION_HEADER,"Bearer " + apiKey)
                .addDefaultHeader(Constants.CONTEXT_TYPE_HEADER, Constants.APPLICATION_JSON)
                .cacheResponses(new Cache.Builder().maxAge(1, TimeUnit.MINUTES))
                .retryAfter(true, 2);
    }
}
