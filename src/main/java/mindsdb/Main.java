package mindsdb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.core.GenericType;
import mindsdb.models.Datasource;
import mindsdb.models.Mind;
import mindsdb.services.Datasources;
import mindsdb.services.Minds;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        String apiKey = "";
        String baseUrl = "https://staging.mdb.ai";
        MindsDb.init(apiKey, baseUrl);
//        List<Mind> mindList = Minds.list();
//        System.out.println(mindList);
//        Mind mind = Minds.get("test");
//        System.out.println(mind);
        Optional<Datasource> datasource = Datasources.get("testds");
        System.out.println(datasource.isEmpty());
//        Minds.delete("test");
//        Datasources.delete("testds");
//        List<Datasource> datasources = Datasources.list();
//        System.out.println(datasources);
//        String s = "[{\"connection_data\":{\"database\":\"demo\",\"host\":\"samples.mindsdb.com\",\"integrations_name\":\"testds\",\"password\":\"demo_password\",\"port\":\"5432\",\"publish\":true,\"sampledb\":true,\"schema\":\"demo_data\",\"test\":true,\"type\":\"postgres\",\"user\":\"demo_user\",\"new-data\":[\"hi\",\"hee\",\"asf\"]},\"created_at\":\"Tue, 08 Oct 2024 01:12:37 GMT\",\"description\":\"Demo database\",\"engine\":\"postgres\",\"name\":\"testds\",\"tables\":[\"car_info\",\"car_sales\",\"financial_headlines\",\"home_rentals\",\"iris\",\"cta\",\"amazon_reviews\",\"review_sentiment\",\"chat_llm_mindsdb_docs\",\"house_sales\",\"used_car_price\",\"fraud_detection\",\"telecom_customer_churn\",\"crm_demo\",\"customer_churn\",\"bank_customer_transactions\",\"user_comments\",\"customer_support_chat\",\"jobs\"]}]";
//        Type empTypeList = ;
//        Gson gson = new Gson();
//        List<Datasource> datasources = gson.fromJson(s, empTypeList);
//        System.out.println(datasources.get(0).getConnection_data().get("new-data"));
    }
}
