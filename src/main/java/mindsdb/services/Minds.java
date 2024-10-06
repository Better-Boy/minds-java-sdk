package mindsdb.services;

import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import mindsdb.models.Mind;
import mindsdb.utils.Constants;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Minds {

    public static List<Mind> list(){
        AtomicReference<List<Mind>> mindListAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.LIST_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .asObject(new GenericType<List<Mind>>(){})
                .ifFailure(listHttpResponse -> {
                    if (listHttpResponse.getParsingError().isPresent()) {
                        throw new RuntimeException("Not able to parse response. Error - " + listHttpResponse.getParsingError().get());
                    }
                })
                .ifSuccess(listHttpResponse -> {
                    mindListAtomicRef.set(listHttpResponse.getBody());
                });
        return mindListAtomicRef.get();
    }

    public static Mind get(String mindName) {
        AtomicReference<Mind> mindAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.GET_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .asObject(Mind.class)
                .ifFailure(mindHttpResponse -> {
                    if (mindHttpResponse.getParsingError().isPresent()) {
                        throw new RuntimeException("Not able to parse response. Error - " + mindHttpResponse.getParsingError().get());
                    }
                })
                .ifSuccess(mindHttpResponse -> {
                    mindAtomicRef.set(mindHttpResponse.getBody());
                });
        return mindAtomicRef.get();
    }

    // TODO: use logging, test it
    public static void delete(String mindName) {
        Unirest.get(Constants.DELETE_MIND_ENDPOINT)
                .routeParam(Constants.PROJECT_NAME_ROUTE_PARAM, Constants.MINDS_PROJECT)
                .routeParam(Constants.MIND_NAME_ROUTE_PARAM, mindName)
                .asString()
                .ifFailure(httpResponse -> {
                    throw new RuntimeException("Mind delete failed. Response code - " + httpResponse.getStatus());
                })
                .ifSuccess(httpResponse -> {
                    System.out.println("Mind deleted");
                });
    }
}
