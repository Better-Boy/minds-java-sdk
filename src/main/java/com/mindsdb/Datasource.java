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
public class Datasource {

    private static final Logger logger = LoggerFactory.getLogger(Datasource.class);

    @NonNull
    private String name;

    @NonNull
    private String engine;

    @NonNull
    private String description;

    @NonNull
    private JsonObject connection_data;

    @NonNull
    private List<String> tables;

    public boolean create() throws Exception {
        Utils.validateDatasource(this);
        String postBody = toString();
        AtomicBoolean isCreated = new AtomicBoolean(false);
        Unirest.post(Constants.CREATE_DATASOURCE_ENDPOINT)
                .body(postBody)
                .asString()
                .ifFailure(stringHttpResponse -> {
                    if(!stringHttpResponse.isSuccess()){
                        logger.error(Constants.FAILED_REQUEST_ERROR_LOG, stringHttpResponse.getStatus(), stringHttpResponse.getBody());
                    }
                })
                .ifSuccess(stringHttpResponse -> {
                    logger.debug("Response code - {}, {} created", stringHttpResponse.getStatus(), name);
                    isCreated.set(true);
                });
        return isCreated.get();
    }

    public static Optional<List<Datasource>> list(){
        AtomicReference<Optional<List<Datasource>>> listAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.LIST_DATASOURCE_ENDPOINT)
                .asObject(new GenericType<List<Datasource>>(){})
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
                    listAtomicRef.set(Optional.of(listHttpResponse.getBody()));
                });

        return listAtomicRef.get();
    }

    public static Optional<Datasource> get(String datasourceName) {
        AtomicReference<Optional<Datasource>> optionalDatasource = new AtomicReference<>(Optional.empty());
        Unirest.get(Constants.GET_DATASOURCE_ENDPOINT)
                .routeParam(Constants.DATASOURCE_NAME_ROUTE_PARAM, datasourceName)
                .asObject(Datasource.class)
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
                    optionalDatasource.set(Optional.of(mindHttpResponse.getBody()));
                });
        return optionalDatasource.get();
    }

    public boolean update() throws Exception {
        String patchBody = Constants.gson.toJson(this);
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        Unirest.patch(Constants.UPDATE_DATASOURCE_ENDPOINT)
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

    public static boolean delete(String datasourceName) {
        AtomicBoolean isDeleted = new AtomicBoolean(false);
        Unirest.delete(Constants.DELETE_DATASOURCE_ENDPOINT)
                .routeParam(Constants.DATASOURCE_NAME_ROUTE_PARAM, datasourceName)
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
                    logger.debug("Response code - {}, {} deleted", httpResponse.getStatus(), datasourceName);
                    isDeleted.set(true);
                });
        return isDeleted.get();
    }

    public boolean delete(){
        return delete(name);
    }

    @Override
    public String toString(){
        return Constants.gson.toJson(this);
    }
}
