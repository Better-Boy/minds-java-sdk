package mindsdb.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Datasource {
    private String name;
    private String engine;
    private String description;
    private Map<String, String> connection_data;
    private List<String> tables;
}
