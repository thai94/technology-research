package com.smile.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ConfigureTimeout {

    final static String SAMPLE_URL = "http://localhost:8080/hello-get?name=Thai";

    // connectTimeout is the timeout until a connection with the server is established.
    // connectionRequestTimeout is used when requesting a connection from the connection manager.
    // socketTimeou the time waiting for data
    @Test
    public void testTimeouts () throws IOException {

        int timeout = 5; // seconds
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpResponse response = httpClient.execute(new HttpGet(SAMPLE_URL));
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);
    }


    // Timeout and DNS Round Robin
    @Test
    public void testTimeouts2() throws IOException {
        int timeout = 3;
        RequestConfig config = RequestConfig.custom().
                setConnectTimeout(timeout * 1000).
                setConnectionRequestTimeout(timeout * 1000).
                setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config).build();

        HttpGet request = new HttpGet("http://www.google.com:81");
        HttpResponse response = client.execute(request);
    }
}
