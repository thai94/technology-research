package com.smile.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class StopFollowRedirect {

    @Test
    public void givenRedirectsAreDisabled_whenConsumingUrlWhichRedirects_thenNotRedirected()
            throws ClientProtocolException, IOException {
        HttpClient instance = HttpClientBuilder.create().disableRedirectHandling().build();
        HttpResponse response = instance.execute(new HttpGet("http://t.co/I5YYd9tddw"));

        Assert.assertEquals(response.getStatusLine().getStatusCode(), 301);
    }
}
