package com.mindsdb;

import com.google.gson.*;

import java.util.List;

/**
 * Utility class providing static methods for validation and JSON creation
 * related to Minds and DataSources.
 */
public class Utils {

    /**
     * Creates a JSON representation of a mind body for API requests.
     *
     * @param mindName    the name of the mind.
     * @param datasources a list of data source names associated with the mind.
     * @return a JSON string representing the mind body.
     */
    public static String createMindBody(String mindName, List<String> datasources){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.CREATE_MIND_BODY_PARAM_NAME, mindName);
        JsonArray jsonElements = new JsonArray();
        for(String datasource: datasources) jsonElements.add(datasource);
        jsonObject.add(Constants.CREATE_MIND_BODY_PARAM_DATASOURCES, jsonElements);
        return jsonObject.toString();
    }

    /**
     * Validates the given Mind object.
     *
     * @param mind the Mind object to validate.
     * @throws Exception if the mind's name is invalid.
     */
    public static void validateMind(Mind mind) throws Exception {
        validateMindName(mind.getName());
    }

    /**
     * Validates the name of a mind.
     *
     * @param mindName the name of the mind to validate.
     * @throws Exception if the mind name is null or empty.
     */
    public static void validateMindName(String mindName) throws Exception {
        if(mindName == null) throw new Exception("mind name cannot be null");
        if(mindName.isEmpty()) throw new Exception("mind name cannot be empty string");
    }

    /**
     * Validates a list of data sources.
     *
     * @param datasources the list of data sources to validate.
     * @throws Exception if the list is null or empty.
     */
    public static void validateDatasourceList(List<String> datasources) throws Exception {
        if(datasources == null) throw new Exception("datasources list cannot be null");
        if(datasources.isEmpty()) throw new Exception("datasources cannot be empty list. A mind needs atleast one datasource");
    }

    /**
     * Validates the properties of a Datasource object.
     *
     * @param datasource the Datasource object to validate.
     * @throws Exception if the datasource's name, description, engine, or connection data is invalid.
     */
    public static void validateDatasource(Datasource datasource) throws Exception {
        validateDatasourceName(datasource.getName());
        if(datasource.getDescription().isEmpty()) throw new Exception("datasource description cannot be empty string");
        if(datasource.getEngine().isEmpty()) throw new Exception("datasource engine cannot be empty string");
        if(datasource.getConnection_data().isEmpty()) throw new Exception("datasource connection json object cannot be empty string");
    }

    /**
     * Validates the name of a data source.
     *
     * @param datasourceName the name of the data source to validate.
     * @throws Exception if the datasource name is empty.
     */
    public static void validateDatasourceName(String datasourceName) throws Exception {
        if(datasourceName.isEmpty()) throw new Exception("datasource name cannot be empty string");
    }

    /**
     * Creates a JSON request body for adding a data source.
     *
     * @param datasourceName the name of the data source to add.
     * @param checkConnection a boolean indicating whether to check the connection.
     * @return a JSON string representing the request body for adding a data source.
     */
    public static String createRequestBodyForAddDs(String datasourceName, boolean checkConnection) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.CREATE_MIND_BODY_PARAM_NAME, datasourceName);
        jsonObject.addProperty(Constants.ADD_DATASOURCE_CHECK_CONN_BODY_PARAM, checkConnection);
        return jsonObject.toString();
    }

    /**
     * Generates a JSON body for updating a data source.
     *
     * @param datasource the Datasource object to convert to JSON.
     * @return a JSON string representing the update body for the data source.
     */
    public static String generateDatasourceUpdateBody(Datasource datasource) {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getName().equals("name") || f.getName().equals("engine");
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                }).create();
        return gson.toJson(datasource);
    }
}
