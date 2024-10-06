package mindsdb.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Mind {
    private String created_at;
    private List<String> datasources;
    private String model_name;
    private String name;
    private JsonObject parameters;
    private String provider;
    private String updated_at;
}
