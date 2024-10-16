package com.mindsdb.utils;

import com.google.gson.Gson;

public class Constants {

    public static final String MINDS_API_ENDPOINT = "/api";
    public static final String MINDS_CLOUD_ENDPOINT = "https://mdb.ai" + MINDS_API_ENDPOINT;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String CONTEXT_TYPE_HEADER = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String MINDS_PROJECT = "mindsdb";
    public static final String PROJECT_NAME_ROUTE_PARAM = "projectName";
    public static final String DATASOURCE_NAME_ROUTE_PARAM = "datasourceName";
    public static final String MIND_NAME_ROUTE_PARAM = "mindName";
    public static final String CREATE_MIND_BODY_PARAM_NAME = "name";
    public static final String CREATE_MIND_BODY_PARAM_DATASOURCES = "datasources";
    public static final String ADD_DATASOURCE_CHECK_CONN_BODY_PARAM = "check_connection";
    public static final String PROMPT_TEMPLATE = "prompt_template";
    public static final String DEFAULT_PROMPT_TEMPLATE = "Use your database tools to answer the user's question: {{question}}";
    public static final Gson gson = new Gson();

    public static final String LIST_DATASOURCE_ENDPOINT = "/datasources";
    public static final String CREATE_DATASOURCE_ENDPOINT = "/datasources";
    public static final String GET_DATASOURCE_ENDPOINT = LIST_DATASOURCE_ENDPOINT + "/%s";
    public static final String UPDATE_DATASOURCE_ENDPOINT = LIST_DATASOURCE_ENDPOINT + "/%s";
    public static final String DELETE_DATASOURCE_ENDPOINT = LIST_DATASOURCE_ENDPOINT + "/%s";


    public static final String LIST_MIND_ENDPOINT = "/projects/%s/minds";
    public static final String CREATE_MIND_ENDPOINT = "/projects/%s/minds";
    public static final String GET_MIND_ENDPOINT = LIST_MIND_ENDPOINT + "/%s";
    public static final String DELETE_MIND_ENDPOINT = LIST_MIND_ENDPOINT + "/%s";
    public static final String UPDATE_MIND_ENDPOINT = LIST_MIND_ENDPOINT + "/%s";
    public static final String ADD_DATASOURCE_MIND_ENDPOINT = LIST_MIND_ENDPOINT + "/%s" + CREATE_DATASOURCE_ENDPOINT;
    public static final String DEL_DATASOURCE_MIND_ENDPOINT = LIST_MIND_ENDPOINT + "/%s" + CREATE_DATASOURCE_ENDPOINT + "/%s";

    public static final String FAILED_REQUEST_ERROR_LOG = "Oh No! Status - {}. Response body - {}";
    public static final String FAILED_REQUEST_RESPONSE_BODY_LOG = "Response body: {}";
    public static final String SUCCESS_REQUEST_RESPONSE_STATUS_LOG = "Response status: {}, success";
}
