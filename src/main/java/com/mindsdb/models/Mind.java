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

    private void update(Mind newMind) throws Exception {
        Utils.validateMindName(newMind.getName());
        String patchBody = Constants.gson.toJson(newMind);
        String endPoint = String.format(Constants.UPDATE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name);
        HttpResponse<String> httpResponse = restClient.sendPatchRequest(endPoint, patchBody);
        log.debug("Response code - {}, {} updated", httpResponse.getStatus(), name);
    }

    public void update(String newName, List<String> newDatasources, String newModelName, JsonObject newParameters, String newProvider, String newPromptTemplate) throws Exception {
        Utils.validateMindName(newName);
        Mind toBeUpdatedMind = Utils.createMindFromParams(newName, newDatasources, newModelName, newParameters, newProvider, newPromptTemplate);
        update(toBeUpdatedMind);
    }

    public void addDatasource(String newDatasourceName) throws Exception {
        Utils.validateMindName(name);
        String postBody = Utils.createRequestBodyForAddDs(newDatasourceName, true);
        String endPoint = String.format(Constants.ADD_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name);
        HttpResponse<String> httpResponse = restClient.sendPostRequest(endPoint, postBody);
        log.debug("Response code - {}. New {} datasource added", httpResponse.getStatus(), newDatasourceName);
    }

    public void dropDatasource(String datasourceName) throws Exception {
        Utils.validateDatasourceName(datasourceName);
        String endPoint = String.format(Constants.DEL_DATASOURCE_MIND_ENDPOINT, Constants.MINDS_PROJECT, name, datasourceName);
        HttpResponse<String> httpResponse = restClient.sendDeleteRequest(endPoint);
        log.debug("Response code - {}. {} datasource deleted from {}", httpResponse.getStatus(), datasourceName, name);
    }

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

    public Stream<ChatCompletionChunk> streamCompletion(String message) throws URISyntaxException {
        configureOpenAIClient();
        ChatClient chatClient = openAI.chatClient();
        CreateChatCompletionRequest request = CreateChatCompletionRequest.newBuilder()
                .message(ChatMessage.userMessage(message))
                .stream(true)
                .build();
        return chatClient.streamChatCompletion(request);
    }

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
