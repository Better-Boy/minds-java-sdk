package com.mindsdb;

import com.google.gson.JsonObject;
import com.mindsdb.client.Client;
import com.mindsdb.models.DatabaseConfig;
import com.mindsdb.models.Datasource;
import com.mindsdb.models.Mind;
import com.mindsdb.services.DatasourcesService;
import com.mindsdb.services.MindsService;
import com.mindsdb.utils.Constants;

import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws Exception {
        String apiKey = "";
        String baseUrl = "https://staging.mdb.ai";
        Client.init(apiKey, baseUrl);

//        String mindName = "test34";
//        String newMindName = "latestMind";
//        Mind updatemind = Mind.builder().name(newMindName).build();
//        Mind mind = MindsService.get(mindName).get();
//        mind.update(updatemind);

        String dsName = "testds";
        Optional<Datasource> optionalDatasource = DatasourcesService.get(dsName);
        optionalDatasource.ifPresent(System.out::println);
    }
}
