# Minds Java SDK

### Installation

Requires atleast java 11. Yet to be published to maven central.


### Getting Started

1. Initialize the MindsDb client

To get started, you'll need to initialize the Client with your API key. If you're using a different server, you can also specify a custom base URL.

```java
String apiKey = "";

// Default connection to Minds Cloud that uses 'https://mdb.ai' as the base URL
Client.init(apiKey);

// If you have self-hosted Minds Cloud instance, use your custom base URL
String baseUrl = "https://staging.mdb.ai";
Client.init(apiKey, baseUrl);
```


2. Creating a Data Source

You can connect to various databases, such as PostgreSQL, by configuring your data source. Build the Datasource object with the credentials in a json as follows.

```java
String credentialString = "{\"user\":\"demo_user\",\"password\":\"demo_password\",\"host\":\"samples.mindsdb.com\",\"port\":5432,\"database\":\"demo\",\"schema\":\"demo_data\"}";
JsonObject connObject  = Constants.gson.fromJson(credentialString, JsonObject.class);
String dsName = "testds12";
String engine = "postgres";
DatabaseConfig databaseConfig = DatabaseConfig.builder().description("Postgres database")
        .name(dsName)
        .engine(engine)
        .tables(List.of("car_info", "jobs"))
        .connection_data(connObject).build();
Datasource datasource = DatasourcesService.create(databaseConfig);
```

3. Creating a Mind

You can create a `mind` and associate it with a data source.

```java
// Create a mind with a data source
String mindName = "test34";
Mind mind = Mind.builder().name(mindName).datasources(List.of("testds")).build();
MindsService.create(mind);

// Alternatively, add a datasource to a mind later
String mindName = "test34";
String newDsName = "testdsnew";
Mind mind = MindsService.get(mindName).get();
mind.addDatasource(newDsName);

// If datasource is not there, then create the datasource using DatasourcesService.create
```

### Managing Minds

To update a mind, create a new Mind object with the changes required. Then pass the new Mind object to the existing mind object that needs updation.

```java
String mindName = "test34";
String newMindName = "latestMind";
Mind updatemind = Mind.builder().name(newMindName).build();
Mind mind = MindsService.get(mindName).get();
mind.update(updatemind);
```

#### List Minds

You can list all the minds you’ve created.

```java
Optional<List<Mind>> optionalMinds = MindsService.list();
optionalMinds.ifPresent(System.out::println);
```

#### Get a Mind by Name

You can fetch details of a mind by its name.

```java
String mindName = "newMind";
Optional<Mind> optionalMind = MindsService.get(mindName);
optionalMind.ifPresent(System.out::println);
```

#### Remove a Mind

To delete a mind, use the following code:

```java
String mindName = "newMind";
MindsService.drop(mindName);
```

#### Remove a datasource from mind

To delete a datasource from a mind, use the following code:

```java
String mindName = "newMind";
String dsName = "oldDs";
Mind mind = MindsService.get(mindName).get();
mind.dropDatasource(dsName);
```

### Managing Data Sources

To view all data sources:

```java
Optional<List<Datasource>> list = DatasourcesService.list();
list.ifPresent(System.out::println);
```

#### Get a Data Source by Name

You can fetch details of a specific data source by its name.

```java
String dsName = "testds";
Optional<Datasource> optionalDatasource = DatasourcesService.get(dsName);
optionalDatasource.ifPresent(System.out::println);
```

#### Remove a Data Source

To delete a data source, use the following code:

```java
String dsName = "testds";
DatasourcesService.drop(dsName);
```

#### TODO

- Chat completion - OpenAI has no official java sdk and the 3rd part sdk is outdated.
- CI/CD using github actions