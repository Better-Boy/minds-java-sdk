package mindsdb.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constants {

    public static final String MINDS_API_ENDPOINT = "/api";
    public static final String MINDS_CLOUD_ENDPOINT = "https://mdb.ai" + MINDS_API_ENDPOINT;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String MINDS_PROJECT = "mindsdb";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String QUERY = "query";
    public static final String PROJECT_NAME_ROUTE_PARAM = "projectName";
    public static final String DATASOURCE_NAME_ROUTE_PARAM = "datasourceName";
    public static final String DATABASE_NAME_ROUTE_PARAM = "databaseName";
    public static final String MODEL_NAME_ROUTE_PARAM = "modelName";
    public static final String MIND_NAME_ROUTE_PARAM = "mindName";
    public static final String DB = "db";
    public static final String CONTEXT = "context";
    public static final Gson gson = new Gson();
    public static final String SERVER_STATUS_ENDPOINT = "/api/status";
    public static final String SQL_QUERY_ENDPOINT = "/api/sql/query";
    public static final String LIST_DATASOURCE_ENDPOINT = "/datasources";
    public static final String GET_DATASOURCE_ENDPOINT = LIST_DATASOURCE_ENDPOINT + "/{" + DATASOURCE_NAME_ROUTE_PARAM + "}";
    public static final String DELETE_DATASOURCE_ENDPOINT = LIST_DATASOURCE_ENDPOINT + "/{" + DATASOURCE_NAME_ROUTE_PARAM + "}";
    public static final String LIST_DATABASE_ENDPOINT = "/api/databases";
    public static final String GET_DATABASE_ENDPOINT = "/api/databases/{" + DATABASE_NAME_ROUTE_PARAM + "}";
    public static final String LIST_MODEL_ENDPOINT = "/api/projects/{" + PROJECT_NAME_ROUTE_PARAM + "}/models" ;
    public static final String GET_MODEL_ENDPOINT = "/api/projects/{" + PROJECT_NAME_ROUTE_PARAM + "}/models/{" + MODEL_NAME_ROUTE_PARAM + "}" ;
    public static final String LIST_TABLE_ENDPOINT = "/api/databases/{" + DATABASE_NAME_ROUTE_PARAM + "}/tables";
    public static final String LIST_MIND_ENDPOINT = "/projects/{" + PROJECT_NAME_ROUTE_PARAM + "}/minds";

    public static final String GET_MIND_ENDPOINT = LIST_MIND_ENDPOINT + "/{" + MIND_NAME_ROUTE_PARAM + "}";
}
