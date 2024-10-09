package com.mindsdb;

import com.google.gson.JsonObject;
import com.mindsdb.Mind;

import javax.xml.crypto.Data;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String apiKey = "";
        String baseUrl = "https://staging.mdb.ai";
        MindsDb.init(apiKey, baseUrl);

//         Done
//        Mind mind = new Mind("nd-", Arrays.asList("testds"));
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

//        Mind mind = new Mind("t54", Arrays.asList("testds"));
//        boolean isUpdated = Mind.update("t54", mind);
//        System.out.println(isUpdated);

//        Optional<Mind> mind = Mind.get("t54");
//        Mind t54Mind = mind.get();
//        System.out.println(t54Mind);
//        t54Mind.getParameters().addProperty("indi","soon");
//        t54Mind.update();

//        Optional<Datasource> ds = Datasource.get("t67");
//        System.out.println(ds.get());

//        Optional<List<Datasource>> list = Datasource.list();
//        System.out.println(list.get());

//        Datasource.delete("newestds");


        String con = "{\"database\":\"demo\",\"host\":\"samples.mindsdb.com\",\"integrations_name\":\"newds\",\"password\":\"demo_password\",\"port\":\"5432\",\"publish\":true,\"test\":true,\"type\":\"postgres\",\"user\":\"demo_user\",\"schema\":\"demo_data\",\"sampledb\":true}";
        JsonObject conn  = Constants.gson.fromJson(con, JsonObject.class);
        Datasource datasource = Datasource.builder().description("newest ds")
                .name("t167")
                .engine("postgres")
                .connection_data(conn).build();
        datasource.create();



    }
}
