package com.mindsdb.client;

import com.mindsdb.utils.Constants;
import com.mindsdb.exception.ForbiddenException;
import com.mindsdb.exception.ObjectNotFoundException;
import com.mindsdb.exception.UnauthorizedException;
import kong.unirest.core.Cache;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for sending HTTP requests using the Unirest library.
 *
 * This class provides methods to simplify making HTTP requests, including
 * POST, PATCH, GET, and DELETE methods. Each method allows for the inclusion of
 * a request body where applicable.
 *
 * <p>
 * Note: Ensure that the Unirest library is included in your project dependencies
 * to use this class effectively. Proper error handling should be implemented
 * when processing the responses from the HTTP requests.
 * </p>
 */
@Getter
@Slf4j
public class RestClient {

    private final String apiKey;
    private final String baseUrl;

    public RestClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        configureUnirest(apiKey, baseUrl);
    }

    public RestClient(String apiKey) {
        this.apiKey = apiKey;
        this.baseUrl = Constants.MINDS_CLOUD_ENDPOINT;
        configureUnirest(apiKey, baseUrl);
    }

    /**
     * Sends an HTTP POST request to the specified endpoint with the given request body.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @param body       The body of the POST request.
     * @return           An HttpResponse containing the response from the server.
     */
    public HttpResponse<String> sendPostRequest(String endPoint, String body) throws Exception {
        HttpResponse<String> httpResponse = Unirest.post(endPoint).body(body).asString();
        checkForFailedResponse(httpResponse);
        return httpResponse;
    }

    /**
     * Sends an HTTP PATCH request to the specified endpoint with the given request body.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @param body       The body of the PATCH request.
     * @return           An HttpResponse containing the response from the server.
     */
    public HttpResponse<String> sendPatchRequest(String endPoint, String body) throws Exception {
        HttpResponse<String> httpResponse = Unirest.patch(endPoint).body(body).asString();
        checkForFailedResponse(httpResponse);
        return httpResponse;
    }

    /**
     * Sends an HTTP GET request to the specified endpoint.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @return           An HttpResponse containing the response from the server.
     */
    public HttpResponse<String> sendGetRequest(String endPoint) throws Exception {
        HttpResponse<String> httpResponse =  Unirest.get(endPoint).asString();
        checkForFailedResponse(httpResponse);
        return httpResponse;
    }

    /**
     * Sends an HTTP DELETE request to the specified endpoint.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @return           An HttpResponse containing the response from the server.
     */
    public HttpResponse<String> sendDeleteRequest(String endPoint) throws Exception {
        HttpResponse<String> httpResponse = Unirest.delete(endPoint).asString();
        checkForFailedResponse(httpResponse);
        return httpResponse;
    }

    /**
     * Checks the HTTP response for errors and throws appropriate exceptions
     * based on the status code.
     *
     * @param httpResponse the HTTP response to check
     * @throws ObjectNotFoundException if the response status is 404 (Not Found)
     * @throws ForbiddenException if the response status is 403 (Forbidden)
     * @throws UnauthorizedException if the response status is 401 (Unauthorized)
     * @throws UnknownError if the response status indicates an error (status code 400-599)
     * @throws Exception if an unexpected error occurs while processing the response
     */
    private void checkForFailedResponse(HttpResponse<String> httpResponse) throws Exception{
        switch (httpResponse.getStatus()){
            case 404: {
                log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
                throw new ObjectNotFoundException(httpResponse.getBody());
            }
            case 403: {
                log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
                throw new ForbiddenException(httpResponse.getBody());
            }
            case 401: {
                log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
                throw new UnauthorizedException(httpResponse.getBody());
            }
        }

        if(httpResponse.getStatus() >= 400 && httpResponse.getStatus() < 600){
            log.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
            throw new UnknownError(httpResponse.getBody());
        }
    }

    /**
     * Configures the Unirest HTTP client with the provided API key and base URL.
     * This method sets up cookie management, default headers, and response caching.
     *
     * @param apiKey the API key used for authorization
     * @param baseUrl the base URL for API requests; it is stripped of leading/trailing spaces
     *                and appended with the Minds API endpoint if not already present
     */
    private synchronized void configureUnirest(String apiKey, String baseUrl){
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

    public void shutDown() {
        Unirest.shutDown();
    }
}
