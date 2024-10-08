package mindsdb.models;

import com.google.gson.JsonObject;
import lombok.Data;

import java.util.List;

@Data
public class Datasource {
    private String name;
    private String engine;
    private String description;
    private JsonObject connection_data;
    private List<String> tables;
}
