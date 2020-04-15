package com.smile.http.client;

import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class CustomHttpHeader {

    final static String SAMPLE_URL = "http://localhost:8080/hello-get?name=Thai";

    @Test
    public void test() throws IOException {
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri(SAMPLE_URL)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
        client.execute(request);
    }
}
