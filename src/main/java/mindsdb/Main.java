package mindsdb;

import mindsdb.models.Mind;
import mindsdb.services.Minds;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String apiKey = "";
        MindsDb.init(apiKey);
        List<Mind> mindList = Minds.list();
        System.out.println(mindList);
    }
}
