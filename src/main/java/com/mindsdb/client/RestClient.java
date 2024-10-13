package com.mindsdb.client;

import com.mindsdb.utils.Constants;
import com.mindsdb.exception.ForbiddenException;
import com.mindsdb.exception.ObjectNotFoundException;
import com.mindsdb.exception.UnauthorizedException;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for sending HTTP requests using the Unirest library.
 *
 * This class provides methods to simplify making HTTP requests, including
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
@Slf4j
public class RestClient {


    /**
     * Sends an HTTP POST request to the specified endpoint with the given request body.
     *
     * @param endPoint    The URL endpoint to which the request is sent.
     * @param body       The body of the POST request.
     * @return           An HttpResponse containing the response from the server.
     */
    public static HttpResponse<String> sendPostRequest(String endPoint, String body) throws Exception {
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
    public static HttpResponse<String> sendPatchRequest(String endPoint, String body) throws Exception {
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
    public static HttpResponse<String> sendGetRequest(String endPoint) throws Exception {
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
    public static HttpResponse<String> sendDeleteRequest(String endPoint) throws Exception {
        HttpResponse<String> httpResponse = Unirest.delete(endPoint).asString();
        checkForFailedResponse(httpResponse);
        return httpResponse;
    }

    private static void checkForFailedResponse(HttpResponse<String> httpResponse) throws Exception{
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

}
