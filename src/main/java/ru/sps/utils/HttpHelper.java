package ru.sps.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpHelper {

    private final static Logger log = LoggerFactory.getLogger(HttpHelper.class);

    public static void sendPost(String url, Map<String, String> headers, String serializedBody) {
        HttpPost post = new HttpPost(url);
        try {
            headers.forEach(post::setHeader);
            post.setEntity(new StringEntity(serializedBody, StandardCharsets.UTF_8));
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
               if (response.getStatusLine().getStatusCode() != 200) {
                   log.error("Response status: " + response.getStatusLine().getStatusCode());
               }
            }
        } catch (Exception e) {
            log.error("Error while sending POST request", e);
        }
    }
}
