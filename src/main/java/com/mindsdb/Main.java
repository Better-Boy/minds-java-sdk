package com.mindsdb;

import com.mindsdb.client.Client;
import com.mindsdb.models.Datasource;

public class Main {
    public static void main(String[] args) throws Exception {
        String apiKey = "mdb_0fOwVx7J0PJeP1VWYeMxzcz2krv5ASjkeBrVlGIcCnLU";
        String baseUrl = "https://staging.mdb.ai";
        Client client = new Client(apiKey, baseUrl);
        Datasource datasource = client.datasourcesService.get("testds").get();
        System.out.println(datasource);

    }
}
