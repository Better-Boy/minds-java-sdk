package com.mindsdb.models;

import com.google.gson.JsonObject;
import lombok.NonNull;

import java.util.List;

public class Datasource extends DatabaseConfig{

    public Datasource(@NonNull String name, @NonNull String engine, @NonNull String description, @NonNull JsonObject connection_data, List<String> tables) {
        super(name, engine, description, connection_data, tables);
    }

}
