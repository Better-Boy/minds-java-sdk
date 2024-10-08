package mindsdb;

import kong.unirest.core.Cache;
import kong.unirest.core.Unirest;
import mindsdb.utils.Constants;
import java.util.concurrent.TimeUnit;

public class MindsDb {

    private MindsDb() { }

    public static synchronized void init(String apiKey) {
        Unirest.config()
                .enableCookieManagement(true)
                .defaultBaseUrl(Constants.MINDS_CLOUD_ENDPOINT)
                .addDefaultHeader(Constants.AUTHORIZATION_HEADER,"Bearer " + apiKey)
                .cacheResponses(new Cache.Builder().maxAge(1, TimeUnit.MINUTES))
                .retryAfter(true, 2);
    }

    public static synchronized void init(String apiKey, String baseUrl) {
        baseUrl = baseUrl.strip();
        if(!baseUrl.endsWith(Constants.MINDS_API_ENDPOINT)) baseUrl+=Constants.MINDS_API_ENDPOINT;
        Unirest.config()
                .enableCookieManagement(true)
                .defaultBaseUrl(baseUrl)
                .addDefaultHeader(Constants.AUTHORIZATION_HEADER,"Bearer " + apiKey)
                .cacheResponses(new Cache.Builder().maxAge(1, TimeUnit.MINUTES))
                .retryAfter(true, 2);
    }
}
