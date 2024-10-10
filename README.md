# Minds Java SDK

### Installation

Requires atleast java 11

TODO - publish jar to maven central

#### TODO

- Chat completion - OpenAI has no official java sdk
- CI/CD using github actions

### Getting Started

1. Initialize the MindsDb client

To get started, you'll need to initialize the Client with your API key. If you're using a different server, you can also specify a custom base URL.

```java
String apiKey = "";

// Default connection to Minds Cloud that uses 'https://mdb.ai' as the base URL
MindsDb.init(apiKey);

// If you have self-hosted Minds Cloud instance, use your custom base URL
String baseUrl = "https://staging.mdb.ai";
MindsDb.init(apiKey, baseUrl);
```


2. Creating a Data Source

You can connect to various databases, such as PostgreSQL, by configuring your data source. Build the Datasource object with the credentials in a json as follows.

```java
String credentialString = "{\"user\":\"demo_user\",\"password\":\"demo_password\",\"host\":\"samples.mindsdb.com\",\"port\":5432,\"database\":\"demo\",\"schema\":\"demo_data\"}";
JsonObject connObject  = Constants.gson.fromJson(credentialString, JsonObject.class);
String dsName = "testds";
String engine = "postgres";
Datasource datasource = Datasource.builder().description("Postgres database")
        .name(dsName)
        .engine(engine)
        .tables(List.of("car_info", "jobs"))
        .connection_data(connObject).build();
boolean isCreated = datasource.create();
System.out.println(isCreated);
```

3. Creating a Mind

You can create a `mind` and associate it with a data source.

```java

// Create a mind with a data source
String mindName = "test";
Mind mind = Mind.builder().name(mindName).datasources(List.of("testds")).build();
boolean isCreated = mind.create();
if(isCreated) System.out.println(mindName + " created");

// Alternatively, add a datasource to a mind later
String mindName = "test";
String newDsName = "testdsnew";
boolean isAdded = Mind.addDatasource(mindName, newDsName, true);
if(isAdded) System.out.println(newDsName + " added to " + mindName);
```

You can also add a data source to an existing mind:

```java
String mindName = "testMind";
String newDsName = "testdsnew";
Mind mindObj = Mind.get(mindName).get();
boolean isAdded = mindObj.addDatasource(newDsName);
if(isAdded) System.out.println(newDsName + " added to " + mindName);
```

### Managing Minds

To update a mind, specify the new name and data sources. The provided data sources will replace the existing ones.

```java
String mindName = "test1";
Mind mindObj = Mind.get(mindName).get();
mindObj.setModel_name("gpt-3.5");
mindObj.setProvider("openai");
mindObj.update();
```

#### List Minds

You can list all the minds you’ve created.

```java
Optional<List<Mind>> optionalMinds = Mind.list();
optionalMinds.ifPresent(System.out::println);
```

#### Get a Mind by Name

You can fetch details of a mind by its name.

```java
String mindName = "t54";
Optional<Mind> optionalMind = Mind.get(mindName);
optionalMind.ifPresent(System.out::println);
```

#### Remove a Mind

To delete a mind, use the following command:

```java
String mindName = "newmind";
boolean isDeleted = Mind.delete(mindName);
if(isDeleted) System.out.println(mindName + " deleted");
```

### Managing Data Sources

To view all data sources:

```java
Optional<List<Datasource>> list = Datasource.list();
list.ifPresent(System.out::println);
```

#### Get a Data Source by Name

You can fetch details of a specific data source by its name.

```java
String dsName = "testds";
Optional<Datasource> optionalDatasource = Datasource.get(dsName);
optionalDatasource.ifPresent(System.out::println);
```

#### Remove a Data Source

To delete a data source, use the following command:

```java
String dsName = "testds";
boolean isDeleted = Datasource.delete(dsName);
if (isDeleted) System.out.println(dsName + " deleted");
```

> Note: The SDK currently does not support automatically removing a data source if it is no longer connected to any mind.

