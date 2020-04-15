package com.smile.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.Assert.assertThat;
//https://www.baeldung.com/httpclient-guide
// https://www.baeldung.com/httpclient-connection-management
@SpringBootTest
public class CheckResponse {

    final static String SAMPLE_URL = "http://localhost:8080/hello-get?name=Thai";

    @Test
    public void givenGetRequestExecuted_whenAnalyzingTheResponse_thenCorrectStatusCode()
            throws ClientProtocolException, IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet(SAMPLE_URL));
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);
    }

    // Wrong use because the pool is allway created each request
    @Test
    public void givenGetRequestExecuted_whenAnalyzingTheResponse_thenCorrectStatusCode1()
            throws ClientProtocolException, IOException {
        for (int i = 0; i < 5; i++) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(SAMPLE_URL);
            HttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(statusCode, HttpStatus.SC_OK);
            //httpGet.releaseConnection();
        }
    }

    @Test
    public void givenGetRequestExecuted_whenAnalyzingTheResponse_thenCorrectStatusCode2()
            throws ClientProtocolException, IOException {

        HttpClient client = HttpClientBuilder.create().build();
        for (int i = 0; i < 1; i++) {
            HttpGet httpGet = new HttpGet(SAMPLE_URL);
            HttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("================:" + i);
            Assert.assertEquals(statusCode, HttpStatus.SC_OK);
            httpGet.releaseConnection();
        }

    }
}
