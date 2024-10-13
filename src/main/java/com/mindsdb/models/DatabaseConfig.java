package com.mindsdb.models;

import com.google.gson.JsonObject;
import com.mindsdb.utils.Constants;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@Slf4j
public class DatabaseConfig {
    /** The name of the data source. */
    @NonNull
    private String name;

    /** The type of engine used for the data source. */
    @NonNull
    private String engine;

    /** A description of the data source. */
    @NonNull
    private String description;

    /** Connection data in JSON format. */
    @NonNull
    private JsonObject connection_data;

    /** A list of tables associated with the data source. */
    @Builder.Default private List<String> tables = new ArrayList<>();

    @Override
    public String toString(){
        return Constants.gson.toJson(this);
    }
}
