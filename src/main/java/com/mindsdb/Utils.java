package com.mindsdb;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class Utils {

    public static String createMindBody(String mindName, List<String> datasources){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.CREATE_MIND_BODY_PARAM_NAME, mindName);
        JsonArray jsonElements = new JsonArray();
        for(String datasource: datasources) jsonElements.add(datasource);
        jsonObject.add(Constants.CREATE_MIND_BODY_PARAM_DATASOURCES, jsonElements);
        return jsonObject.toString();
    }

    public static boolean validateMind(Mind mind) throws Exception {
        if(mind.getName().isEmpty()) throw new Exception("Mind name cannot be empty");
        if(mind.getDatasources().isEmpty()) throw new Exception("Datasources list cannot be empty. A mind must atleast have one datasource.");
        return true;
    }

    public static void validateMindName(String mindName) throws Exception {
        if(mindName == null) throw new Exception("mind name cannot be null");
        if(mindName.isEmpty()) throw new Exception("mind name cannot be empty string");
    }

    public static void validateDatasourceList(List<String> datasources) throws Exception {
        if(datasources == null) throw new Exception("datasources list cannot be null");
        if(datasources.isEmpty()) throw new Exception("datasources cannot be empty list. A mind needs atleast one datasource");
    }

    public static void validateDatasource(Datasource datasource) throws Exception {
        validateDatasourceName(datasource.getName());
        if(datasource.getDescription().isEmpty()) throw new Exception("datasource description cannot be empty string");
        if(datasource.getEngine().isEmpty()) throw new Exception("datasource engine cannot be empty string");
        if(datasource.getConnection_data().isEmpty()) throw new Exception("datasource connection json object cannot be empty string");
    }

    public static void validateDatasourceName(String datasourceName) throws Exception {
        if(datasourceName.isEmpty()) throw new Exception("datasource name cannot be empty string");
    }

    public static String createRequestBodyForAddDs(String datasourceName, boolean checkConnection) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.CREATE_MIND_BODY_PARAM_NAME, datasourceName);
        jsonObject.addProperty(Constants.ADD_DATASOURCE_CHECK_CONN_BODY_PARAM, checkConnection);
        return jsonObject.toString();
    }
}
