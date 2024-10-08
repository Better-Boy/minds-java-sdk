package com.mindsdb;

import com.google.gson.JsonObject;
import com.mindsdb.Mind;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String apiKey = "";
        String baseUrl = "https://staging.mdb.ai";
        MindsDb.init(apiKey, baseUrl);

        // Done
//        Mind mind = new Mind("newmind", Arrays.asList("testds"));
//        mind.create();

        // Done
//        Mind mind = Mind.builder().name("newmindone").datasources(Arrays.asList("testds")).build();
//        mind.create();

        // Done
//        boolean isCreated = Mind.create("newmindtwo", Arrays.asList("testds"));

        // Done
//        Mind mind = Mind.builder().name("newmindthre")
//                .datasources(Arrays.asList("testds"))
//                .model_name("gpt")
//                .provider("opsfdv")
//                .build();
//        mind.create();

        // Done
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("hi", "jello");
//        Mind mind = Mind.builder().name("t4")
//                .datasources(Arrays.asList("testds"))
//                .model_name("llama-3.2")
//                .provider("meta")
//                .parameters(jsonObject)
//                .build();
//        mind.create();

        // Done
//        boolean isCreated = Mind.create("mindsdb", "newmindthreee", Arrays.asList("testds"));

//        Optional<List<Mind>> mindList = Mind.list();
//        System.out.println(mindList.get());

//        Optional<Mind> mind = Mind.get("t54");
//        System.out.println(mind.get());
//
//        mind = new Mind("t54", Collections.emptyList()).get();
//        System.out.println(mind.get());


//        boolean isDeleted = Mind.delete("t32");
//        System.out.println(isDeleted);
//
//        isDeleted = new Mind("t382", Collections.emptyList()).delete();
//        System.out.println(isDeleted);

//        boolean isAdded = Mind.addDatasource("t54", "newtestds", true);
//        System.out.println(isAdded);

//        boolean isAdded = new Mind("t54", Collections.emptyList()).addDatasource("newtestds");
//        System.out.println(isAdded);

//        Datasource datasource = Datasource.
//        Mind mind = Mind.builder().datasources(Collections.emptyList()).name("sfd").build();
//        Mind mind = new Mind("hi", Collections.emptyList());

//        Datasources.delete("testds");
//        Datasources.delete("testds1");
//        List<Datasource> datasources = Datasources.list();
//        System.out.println(datasources);
//        boolean isCreated = Minds.create("newtestt", Arrays.asList("testds"));
//        System.out.println(isCreated);
//        String s = "[{\"connection_data\":{\"database\":\"demo\",\"host\":\"samples.mindsdb.com\",\"integrations_name\":\"testds\",\"password\":\"demo_password\",\"port\":\"5432\",\"publish\":true,\"sampledb\":true,\"schema\":\"demo_data\",\"test\":true,\"type\":\"postgres\",\"user\":\"demo_user\",\"new-data\":[\"hi\",\"hee\",\"asf\"]},\"created_at\":\"Tue, 08 Oct 2024 01:12:37 GMT\",\"description\":\"Demo database\",\"engine\":\"postgres\",\"name\":\"testds\",\"tables\":[\"car_info\",\"car_sales\",\"financial_headlines\",\"home_rentals\",\"iris\",\"cta\",\"amazon_reviews\",\"review_sentiment\",\"chat_llm_mindsdb_docs\",\"house_sales\",\"used_car_price\",\"fraud_detection\",\"telecom_customer_churn\",\"crm_demo\",\"customer_churn\",\"bank_customer_transactions\",\"user_comments\",\"customer_support_chat\",\"jobs\"]}]";
//        Type empTypeList = ;
//        Gson gson = new Gson();
//        List<Datasource> datasources = gson.fromJson(s, empTypeList);
//        System.out.println(datasources.get(0).getConnection_data().get("new-data"));
    }
}
