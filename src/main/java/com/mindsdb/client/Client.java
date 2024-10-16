package com.mindsdb.client;

import com.mindsdb.services.DatasourcesService;
import com.mindsdb.services.MindsService;
import lombok.Getter;

@Getter
public class Client {

    private final RestClient restClient;
    public final MindsService mindsService;
    public final DatasourcesService datasourcesService;

    public Client(String apiKey) {
        this.restClient = new RestClient(apiKey);
        this.mindsService = new MindsService(restClient);
        this.datasourcesService = new DatasourcesService(restClient);
    }

    public Client(String apiKey, String baseUrl) {
        this.restClient = new RestClient(apiKey, baseUrl);
        this.mindsService = new MindsService(restClient);
        this.datasourcesService = new DatasourcesService(restClient);
    }

    public void shutDownConnection(){
        restClient.shutDown();
    }

}
