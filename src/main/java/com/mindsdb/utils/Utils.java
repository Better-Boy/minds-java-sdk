package com.mindsdb.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mindsdb.models.DatabaseConfig;
import com.mindsdb.models.Datasource;
import com.mindsdb.models.Mind;

import java.net.URI;
import java.net.URISyntaxException;
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

    public static void validateDatabaseConfig(DatabaseConfig databaseConfig) throws Exception {
        validateDatasourceName(databaseConfig.getName());
        if(databaseConfig.getDescription().isEmpty()) throw new Exception("datasource description cannot be empty string");
        if(databaseConfig.getEngine().isEmpty()) throw new Exception("datasource engine cannot be empty string");
        if(databaseConfig.getConnection_data().isEmpty()) throw new Exception("datasource connection json object cannot be empty string");
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

    /**
     * Parses a JSON string into a Mind object.
     *
     * @param mindJsonString The JSON string representing a Mind object.
     * @return              A Mind object parsed from the provided JSON string.
     */
    public static Mind parseStringToMind(String mindJsonString){
        return Constants.gson.fromJson(mindJsonString, Mind.class);
    }

    /**
     * Parses a JSON string into a Datasource object.
     *
     * @param dsJsonString The JSON string representing a Datasource object.
     * @return            A Datasource object parsed from the provided JSON string.
     */
    public static Datasource parseStringToDatasource(String dsJsonString){
        return Constants.gson.fromJson(dsJsonString, Datasource.class);
    }

    /**
     * Parses a JSON string into a list of Mind objects.
     *
     * @param mindListJsonString The JSON string representing a list of Mind objects.
     * @return                  A List of Mind objects parsed from the provided JSON string.
     */
    public static List<Mind> parseStringToMindList(String mindListJsonString){
        TypeToken<List<Mind>> typeToken = new TypeToken<List<Mind>>(){};
        return Constants.gson.fromJson(mindListJsonString, typeToken.getType());
    }

    /**
     * Parses a JSON string into a list of Datasource objects.
     *
     * @param dsListJsonString The JSON string representing a list of Datasource objects.
     * @return                A List of Datasource objects parsed from the provided JSON string.
     */
    public static List<Datasource> parseStringToDatasourceList(String dsListJsonString){
        TypeToken<List<Datasource>> typeToken = new TypeToken<List<Datasource>>(){};
        return Constants.gson.fromJson(dsListJsonString, typeToken.getType());
    }

    /**
     * Returns the base URL for OpenAI services based on the provided URL.
     * The method modifies the host based on specific conditions and constructs a new URI.
     *
     * @param url the original URL to be modified
     * @return the new base URL for OpenAI services as a String
     * @throws URISyntaxException if the given URL is not a valid URI
     */
    public static String getBaseUrlForOpenAI(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String netloc = uri.getHost();
        String llmHost;

        if ("mdb.ai".equals(netloc)) {
            llmHost = "llm.mdb.ai";
        } else {
            llmHost = "ai." + netloc;
        }
        URI newUri = new URI(uri.getScheme(), llmHost, "", uri.getQuery(), uri.getFragment());
        return newUri.toString();
    }

    /**
     * Creates a Mind object from the provided parameters.
     * This method utilizes the Builder pattern to construct a Mind object based on the specified attributes.
     *
     * @param name          the name of the Mind
     * @param datasources   a list of data sources associated with the Mind
     * @param modelName     the name of the model to be used
     * @param parameters    a JSON object containing additional parameters for the Mind
     * @param provider      the provider to be used
     * @param promptTemplate the template for the prompt
     * @return a Mind object created from the provided parameters
     */
    public static Mind createMindFromParams(String name, List<String> datasources, String modelName, JsonObject parameters, String provider, String promptTemplate){
        Mind.MindBuilder mindBuilder = Mind.builder().name(name);
        if(!(datasources == null || datasources.isEmpty())) mindBuilder.datasources(datasources);
        if(!(modelName == null || modelName.isEmpty())) mindBuilder.model_name(modelName);
        if(!(parameters == null || parameters.isEmpty())) mindBuilder.parameters(parameters);
        if(!(provider == null || provider.isEmpty())) mindBuilder.provider(provider);
        if(!(promptTemplate == null || promptTemplate.isEmpty())) mindBuilder.prompt_template(promptTemplate);
        return mindBuilder.build();
    }
}
