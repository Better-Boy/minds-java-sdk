# Minds Java SDK

### Installation

Requires atleast java 17.

Run the following commands:

```bash
git clone https://github.com/Better-Boy/minds-java-sdk
cd minds-java-sdk
mvn clean install
```

Once this is done, the jar is created in the `target` folder. To add this as a dependency to maven project, use the following command:

```bash
mvn install:install-file -Dfile=minds-java-sdk-1.0.0.jar -DgroupId=org.mindsdb.sdk -DartifactId=minds-java-sdk -Dversion=1.0.0 -Dpackaging=jar
```


### Getting Started

1. Initialize the MindsDb client

To get started, you'll need to initialize the Client with your API key. If you're using a different server, you can also specify a custom base URL.

```java
String apiKey = "";

// Default connection to Minds Cloud that uses 'https://mdb.ai' as the base URL
Client client = new Client(apiKey);

// If you have self-hosted Minds Cloud instance, use your custom base URL
String baseUrl = "https://staging.mdb.ai";
Client client = new Client(apiKey, baseUrl);
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
Datasource datasource = client.datasourcesService.create(databaseConfig);
```

3. Creating a Mind

You can create a `mind` and associate it with a data source.

```java
// Create a mind with a data source
String mindName = "testMind";
List<String> ds = List.of("testds");
Mind mind = client.mindsService.create(mindName, ds);
System.out.println(mind);

// Alternatively, add a datasource to a mind later
String mindName = "testMind";
String newDsName = "testdsnew";
Mind mind = client.mindsService.get(mindName).get();
mind.addDatasource(newDsName);

// If datasource is not there, then create the datasource using DatasourcesService.create
```

4. Chatting with a mind

```java
String mindName = "testMind";
String message = "How are you today?!"
Mind mind = client.mindsService.get(mindName).get();
String response = mind.completion(message);

// Stream the responses
Mind mind = client.mindsService.get(mindName).get();
Stream<ChatCompletionChunk> streamResponse = mind.streamCompletion(message);
streamResponse.forEach(System.out::println);
```

### Managing Minds

To update a mind, create a new Mind object with the changes required. Then pass the new Mind object to the existing mind object that needs updation.

```java
String mindName = "test34";
String newMindName = "latestMind";
Mind mind = client.mindsService.get(mindName).get();
mind.update(newMindName, null, null, null, null, null);
```

`null` above is for other properties like - datasources, provider etc.. Refer the javadoc for the method signature.

#### List Minds

You can list all the minds you’ve created.

```java
Client client = new Client(apiKey, baseUrl);
Optional<List<Mind>> optionalMinds = client.mindsService.list();
optionalMinds.ifPresent(System.out::println);
```

#### Get a Mind by Name

You can fetch details of a mind by its name.

```java
String mindName = "newMind";
Optional<Mind> optionalMind = client.mindsService.get(mindName);
optionalMind.ifPresent(System.out::println);
```

#### Remove a Mind

To delete a mind, use the following code:

```java
String mindName = "newMind";
client.mindsService.drop(mindName);
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
Optional<List<Datasource>> list = client.datasourcesService.list();
list.ifPresent(System.out::println);
```

#### Get a Data Source by Name

You can fetch details of a specific data source by its name.

```java
String dsName = "testds";
Optional<Datasource> optionalDatasource = client.datasourcesService.get(dsName);
optionalDatasource.ifPresent(System.out::println);
```

#### Remove a Data Source

To delete a data source, use the following code:

```java
String dsName = "testds";
client.datasourcesService.drop(dsName);
```

#### TODO

- CI/CD using github actions - need org account from mindsdb for [sonatype](https://central.sonatype.com/)
