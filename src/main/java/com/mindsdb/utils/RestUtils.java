package com.mindsdb.utils;

import com.mindsdb.Constants;
import kong.unirest.core.*;

import java.util.Map;

/**
 * Utility class for sending HTTP requests using the Unirest library.
 *
 * This class provides static methods to simplify making HTTP requests, including
 * POST, PATCH, GET, and DELETE methods. Each method allows for the inclusion of
 * route parameters and a request body where applicable.
 *
 * Example usage:
 * <pre>
 *     Map<String, String> routeParams = new HashMap<>();
 *     routeParams.put("id", "123");
 *     String body = "{\"name\":\"John Doe\"}";
 *
 *     // Sending a POST request
 *     HttpResponse<String> response = RestUtils.sendPostRequest("http://example.com/api/users", routeParams, body);
 *
 *     // Sending a GET request
 *     HttpResponse<String> getResponse = RestUtils.sendGetRequest("http://example.com/api/users/{id}", routeParams);
 * </pre>
 *
 * <p>
 * Note: Ensure that the Unirest library is included in your project dependencies
 * to use this class effectively. Proper error handling should be implemented
 * when processing the responses from the HTTP requests.
 * </p>
 */
public class RestUtils {

    /**
     * Sends an HTTP POST request to the specified endpoint with the given request body.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @param body       The body of the POST request.
     * @return           An HttpResponse containing the response from the server.
     */
    public static HttpResponse<String> sendPostRequest(String endPoint, String body){
        return Unirest.post(endPoint).body(body).asString();
    }

    /**
     * Sends an HTTP PATCH request to the specified endpoint with the given request body.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @param body       The body of the PATCH request.
     * @return           An HttpResponse containing the response from the server.
     */
    public static HttpResponse<String> sendPatchRequest(String endPoint, String body){
        return Unirest.patch(endPoint).body(body).asString();
    }

    /**
     * Sends an HTTP GET request to the specified endpoint.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @return           An HttpResponse containing the response from the server.
     */
    public static HttpResponse<String> sendGetRequest(String endPoint){
        return Unirest.get(endPoint).asString();
    }

    /**
     * Sends an HTTP DELETE request to the specified endpoint.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @return           An HttpResponse containing the response from the server.
     */
    public static HttpResponse<String> sendDeleteRequest(String endPoint){
        return Unirest.delete(endPoint).asString();
    }
}
