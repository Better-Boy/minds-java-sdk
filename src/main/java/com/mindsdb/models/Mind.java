package com.mindsdb.models;

import com.google.gson.JsonObject;
import com.mindsdb.utils.Constants;
import com.mindsdb.client.RestClient;
import com.mindsdb.utils.Utils;
import io.github.stefanbratanov.jvm.openai.*;
import kong.unirest.core.HttpResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Slf4j
public class Mind {

    @NonNull private String name;
    @Builder.Default
    private List<String> datasources = new ArrayList<>();
    private String created_at;
    private String model_name;
    private String prompt_template;
    private JsonObject parameters;
    private String provider;
    private String updated_at;

    private transient OpenAI openAI;

    private transient RestClient restClient;

    /**
     * Updates the specified Mind object by sending a PATCH request to the server.
     * Validates the Mind's name before performing the update.
     *
     * @param newMind the Mind object containing updated information
     * @throws Exception if validation fails or if the HTTP request encounters an error
     */
    private void update(Mind newMind) throws Exception {
        Utils.validateMindName(newMind.getName());
        String patchBody = Constants.gson.toJson(newMind);
        String endPoint = String.format(Constants.UPDATE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name);
        HttpResponse<String> httpResponse = restClient.sendPatchRequest(endPoint, patchBody);
        log.debug("Response code - {}, {} updated", httpResponse.getStatus(), name);
    }

    /**
     * Updates the Mind with new parameters by creating a Mind object
     * from the provided parameters and then calling the update method.
     *
     * @param newName the new name for the Mind
     * @param newDatasources the new list of data sources associated with the Mind
     * @param newModelName the new model name for the Mind
     * @param newParameters the new parameters for the Mind as a JsonObject
     * @param newProvider the new provider for the Mind
     * @param newPromptTemplate the new prompt template for the Mind
     * @throws Exception if validation fails or if the update operation encounters an error
     */
    public void update(String newName, List<String> newDatasources, String newModelName, JsonObject newParameters, String newProvider, String newPromptTemplate) throws Exception {
        Utils.validateMindName(newName);
        Mind toBeUpdatedMind = Utils.createMindFromParams(newName, newDatasources, newModelName, newParameters, newProvider, newPromptTemplate);
        update(toBeUpdatedMind);
    }

    /**
     * Adds a new data source to the Mind by sending a POST request to the server.
     * Validates the Mind's name before adding the new data source.
     *
     * @param newDatasourceName the name of the new data source to be added
     * @throws Exception if the Mind's name is invalid or if the HTTP request encounters an error
     */
    public void addDatasource(String newDatasourceName) throws Exception {
        Utils.validateMindName(name);
        String postBody = Utils.createRequestBodyForAddDs(newDatasourceName, true);
        String endPoint = String.format(Constants.ADD_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name);
        HttpResponse<String> httpResponse = restClient.sendPostRequest(endPoint, postBody);
        log.debug("Response code - {}. New {} datasource added", httpResponse.getStatus(), newDatasourceName);
    }

    /**
     * Removes a data source from the Mind by sending a DELETE request to the server.
     * Validates the data source's name before performing the deletion.
     *
     * @param datasourceName the name of the data source to be removed
     * @throws Exception if the data source name is invalid or if the HTTP request encounters an error
     */
    public void dropDatasource(String datasourceName) throws Exception {
        Utils.validateDatasourceName(datasourceName);
        String endPoint = String.format(Constants.DEL_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name, datasourceName);
        HttpResponse<String> httpResponse = restClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}. {} datasource deleted from {}", httpResponse.getStatus(), datasourceName, name);
    }

    /**
     * Generates a completion response based on the provided message
     * by interacting with the OpenAI client.
     *
     * @param message the input message for which a completion is requested
     * @return the content of the completion response
     * @throws URISyntaxException if the base URL for OpenAI is invalid
     */
    public String completion(String message) throws URISyntaxException {
        configureOpenAIClient();
        ChatClient chatClient = openAI.chatClient();
        CreateChatCompletionRequest createChatCompletionRequest = CreateChatCompletionRequest.newBuilder()
                .model(name)
                .message(ChatMessage.userMessage(message))
                .build();
        ChatCompletion chatCompletion = chatClient.createChatCompletion(createChatCompletionRequest);
        return chatCompletion.choices().get(0).message().content();
    }

    /**
     * Streams completion responses based on the provided message
     * by interacting with the OpenAI client.
     *
     * @param message the input message for which a completion is requested
     * @return a stream of ChatCompletionChunk objects representing the streamed responses
     * @throws URISyntaxException if the base URL for OpenAI is invalid
     */
    public Stream<ChatCompletionChunk> streamCompletion(String message) throws URISyntaxException {
        configureOpenAIClient();
        ChatClient chatClient = openAI.chatClient();
        CreateChatCompletionRequest request = CreateChatCompletionRequest.newBuilder()
                .message(ChatMessage.userMessage(message))
                .stream(true)
                .build();
        return chatClient.streamChatCompletion(request);
    }

    /**
     * Configures the OpenAI client with the appropriate API key and base URL.
     * This method initializes the OpenAI client only if it has not been configured yet.
     *
     * @throws URISyntaxException if the base URL for OpenAI is invalid
     */
    public synchronized void configureOpenAIClient() throws URISyntaxException {
        String openAIBaseUrl = Utils.getBaseUrlForOpenAI(restClient.getBaseUrl());
        if(this.openAI == null){
            this.openAI = OpenAI
                    .newBuilder(restClient.getApiKey())
                    .requestTimeout(Duration.ofSeconds(10))
                    .baseUrl(openAIBaseUrl).build();
        }
    }

    @Override
    public String toString(){
        return Constants.gson.toJson(this);
    }
}
