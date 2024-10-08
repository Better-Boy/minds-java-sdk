package com.mindsdb;

import com.google.gson.JsonObject;
import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Mind {

    private static final Logger logger = LoggerFactory.getLogger(Mind.class);

    @NonNull private String name;
    @NonNull private List<String> datasources;
    private String created_at;
    private String model_name;
    private JsonObject parameters;
    private String provider;
    private String updated_at;

    // Uncomment only if projectName is needed explicitly. For now, it's hard-coded to "mindsdb"
//    public static boolean create(@NonNull String projectName, String mindName, List<String> datasources){
//        String postBody = Utils.createMindBody(mindName, datasources);
//        AtomicBoolean isCreated = new AtomicBoolean(false);
//        Unirest.post(Constants.CREATE_MIND_ENDPOINT)
//                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, projectName)
//                .body(postBody)
//                .asString()
//                .ifFailure(stringHttpResponse -> {
//                    if(!stringHttpResponse.isSuccess()){
//                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
//                    }
//                })
//                .ifSuccess(stringHttpResponse -> {
//                    logger.debug("Response code - {}, {} created", stringHttpResponse.getStatus(), mindName);
//                    isCreated.set(true);
//                });
//        return isCreated.get();
//    }

    private static Optional<Mind> create(Mind mind) throws Exception {
        Utils.validateMind(mind);
        String postBody = Constants.gson.toJson(mind);
        AtomicReference<Optional<Mind>> mindAtomicRef = new AtomicReference<>();
        Unirest.post(Constants.CREATE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .body(postBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} created", stringHttpResponse.getStatus(), mind.name);
                    mindAtomicRef.set(Optional.of(mind));
                });
        return mindAtomicRef.get();
    }

    public static Optional<Mind> create(String mindName, List<String> datasources) throws Exception {
        return create(new Mind(mindName, datasources));
    }

    public Optional<Mind> create() throws Exception {
        return create(this);
    }

    public static Optional<List<Mind>> list(){
        AtomicReference<Optional<List<Mind>>> mindListAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.LIST_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .asObject(new GenericType<List<Mind>>(){})
                .ifFailure(listHttpResponse -> {

                    if(!listHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, listHttpResponse.getStatus(), listHttpResponse.getBody());
                    }

                    listHttpResponse.getParsingError().ifPresent(parsingException -> {
                        logger.error(Constants.FAILED_REQUEST_PARSE_LOG, parsingException);
                        logger.error(Constants.FAILED_REQUEST_RESPONSE_BODY_LOG, parsingException.getOriginalBody());
                    });
                })
                .ifSuccess(listHttpResponse -> {
                    logger.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, listHttpResponse.getStatus());
                    mindListAtomicRef.set(Optional.of(listHttpResponse.getBody()));
                });
        return mindListAtomicRef.get();
    }

    public static Optional<Mind> get(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        AtomicReference<Optional<Mind>> mindAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.GET_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .asObject(Mind.class)
                .ifFailure(mindHttpResponse -> {
                    if(!mindHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, mindHttpResponse.getStatus(), mindHttpResponse.getBody());
                    }
                    mindHttpResponse.getParsingError().ifPresent(parsingException -> {
                        logger.error(Constants.FAILED_REQUEST_PARSE_LOG, parsingException);
                        logger.error(Constants.FAILED_REQUEST_RESPONSE_BODY_LOG, parsingException.getOriginalBody());
                    });
                })
                .ifSuccess(mindHttpResponse -> {
                    logger.debug(Constants.SUCCESS_REQUEST_RESPONSE_STATUS_LOG, mindHttpResponse.getStatus());
                    mindAtomicRef.set(Optional.of(mindHttpResponse.getBody()));
                });
        return mindAtomicRef.get();
    }

    public Optional<Mind> get() throws Exception {
        return get(name);
    }

    public static boolean delete(String mindName) throws Exception {
        Utils.validateMindName(mindName);
        AtomicBoolean isDeleted = new AtomicBoolean(false);
        Unirest.delete(Constants.DELETE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .asString()
                .ifFailure(httpResponse -> {
                    if(!httpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, httpResponse.getStatus(), httpResponse.getBody());
                    }
                    httpResponse.getParsingError().ifPresent(parsingException -> {
                        logger.error(Constants.FAILED_REQUEST_PARSE_LOG, parsingException);
                        logger.error(Constants.FAILED_REQUEST_RESPONSE_BODY_LOG, parsingException.getOriginalBody());
                    });
                })
                .ifSuccess(httpResponse -> {
                    logger.debug("Response code - {}, {} deleted", httpResponse.getStatus(), mindName);
                    isDeleted.set(true);
                });
        return isDeleted.get();
    }

    public boolean update(String existingMindName, Mind newMind) {
        String patchBody = Constants.gson.toJson(newMind);
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        Unirest.patch(Constants.UPDATE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, existingMindName)
                .body(patchBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} updated", stringHttpResponse.getStatus(), existingMindName);
                    isUpdated.set(true);
                });
        return isUpdated.get();
    }

    public boolean update() {
        String patchBody = Constants.gson.toJson(this);
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        Unirest.patch(Constants.UPDATE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, name)
                .body(patchBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} updated", stringHttpResponse.getStatus(), name);
                    isUpdated.set(true);
                });
        return isUpdated.get();
    }

    public static boolean addDatasource(String mindName, String newDatasourceName, boolean checkConnection) throws Exception {
        Utils.validateMindName(mindName);
        Utils.validateDatasourceName(newDatasourceName);
        AtomicBoolean isDatasourceAdded = new AtomicBoolean(false);
        String postBody = Utils.createRequestBodyForAddDs(newDatasourceName, checkConnection);
        System.out.println(postBody);
        Unirest.post(Constants.ADD_DATASOURCE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .body(postBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    System.out.println(stringHttpResponse.getRequestSummary().getUrl());
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}. New {} datasource added", stringHttpResponse.getStatus(), newDatasourceName);
                    isDatasourceAdded.set(true);
                });
        return isDatasourceAdded.get();
    }

    public boolean addDatasource(String newDatasourceName) throws Exception {
        Utils.validateDatasourceName(newDatasourceName);
        return addDatasource(name, newDatasourceName, true);
    }

    public boolean delete() throws Exception {
        return delete(name);
    }

    @Override
    public String toString(){
        return Constants.gson.toJson(this);
    }
}
