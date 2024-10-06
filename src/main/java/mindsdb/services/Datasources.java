package mindsdb.services;

import kong.unirest.core.GenericType;
import kong.unirest.core.Unirest;
import mindsdb.models.Datasource;
import mindsdb.models.Mind;
import mindsdb.utils.Constants;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Datasources {

    public static List<Datasource> list(){
        AtomicReference<List<Datasource>> listAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.LIST_DATASOURCE_ENDPOINT)
                .asObject(new GenericType<List<Datasource>>(){})
                .ifFailure(listHttpResponse -> {
                    if (listHttpResponse.getParsingError().isPresent()) {
                        throw new RuntimeException("Not able to parse response. Error - " + listHttpResponse.getParsingError().get());
                    }
                })
                .ifSuccess(listHttpResponse -> {
                    listAtomicRef.set(listHttpResponse.getBody());
                });
        return listAtomicRef.get();
    }

    public static Datasource get(String datasourceName) {
        AtomicReference<Datasource> datasourceAtomicRef = new AtomicReference<>();
        Unirest.get(Constants.GET_DATASOURCE_ENDPOINT)
                .routeParam(Constants.DATASOURCE_NAME_ROUTE_PARAM, datasourceName)
                .asObject(Datasource.class)
                .ifFailure(datasourceHttpResponse -> {
                    if (datasourceHttpResponse.getParsingError().isPresent()) {
                        throw new RuntimeException("Not able to parse response. Error - " + datasourceHttpResponse.getParsingError().get());
                    }
                })
                .ifSuccess(datasourceHttpResponse -> {
                    datasourceAtomicRef.set(datasourceHttpResponse.getBody());
                });
        return datasourceAtomicRef.get();
    }

    // TODO: use logging, test it
    public static void delete(String datasourceName) {
        Unirest.get(Constants.DELETE_DATASOURCE_ENDPOINT)
                .routeParam(Constants.DATASOURCE_NAME_ROUTE_PARAM, datasourceName)
                .asString()
                .ifFailure(datasourceHttpResponse -> {
                    throw new RuntimeException("Datasource delete failed. Response code - " + datasourceHttpResponse.getStatus());
                })
                .ifSuccess(datasourceHttpResponse -> {
                    System.out.println("Datasource deleted");
                });
    }
}
