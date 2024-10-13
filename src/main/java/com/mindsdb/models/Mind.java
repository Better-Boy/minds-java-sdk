package com.mindsdb.models;

import com.google.gson.JsonObject;
import com.mindsdb.utils.Constants;
import com.mindsdb.client.RestClient;
import com.mindsdb.utils.Utils;
import kong.unirest.core.HttpResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Slf4j
public class Mind {

    /**
     * The name of the mind.
     */
    @NonNull
    private String name;

    /**
     * A list of datasource names associated with the mind.
     */
    @Builder.Default private List<String> datasources = new ArrayList<>();

    /**
     * The creation timestamp of the mind.
     */
    private String created_at;

    /**
     * The name of the model associated with the mind.
     */
    private String model_name;

    /**
     * Additional parameters for the mind, represented as a JSON object.
     */
    private JsonObject parameters;

    /**
     * The provider of the model_name.
     */
    private String provider;

    /**
     * The last updated timestamp of the mind.
     */
    private String updated_at;


    /**
     * Updates an existing mind with the provided new mind data.
     *
     * @param newMind the new mind data to update
     * @return {@code true} if the mind was updated successfully, {@code false} otherwise
     */
    public void update(Mind newMind) throws Exception {
        Utils.validateMindName(newMind.getName());
        String patchBody = Constants.gson.toJson(newMind);
        String endPoint = String.format(Constants.UPDATE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name);
        HttpResponse<String> httpResponse = RestClient.sendPatchRequest(endPoint, patchBody);
        log.debug("Response code - {}, {} updated", httpResponse.getStatus(), name);
    }

    /**
     * Adds a new datasource to the specified mind.
     *
     * @param newDatasourceName the name of the new datasource
     * @return {@code true} if the datasource was added successfully, {@code false} otherwise
     * @throws Exception if validation fails or an error occurs during the request
     */
    public void addDatasource(String newDatasourceName) throws Exception {
        Utils.validateMindName(name);
        String postBody = Utils.createRequestBodyForAddDs(newDatasourceName, true);
        String endPoint = String.format(Constants.ADD_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name);
        HttpResponse<String> httpResponse = RestClient.sendPostRequest(endPoint, postBody);
        log.debug("Response code - {}. New {} datasource added", httpResponse.getStatus(), newDatasourceName);
    }

    @Override
    public String toString(){
        return Constants.gson.toJson(this);
    }
}
