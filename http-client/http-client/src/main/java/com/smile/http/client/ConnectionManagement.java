package com.smile.http.client;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ConnectionManagement {

    final static String SAMPLE_URL = "http://localhost:8080/hello-get?name=Thai";

    @Test
    public void test_success() throws IOException {
        PoolingHttpClientConnectionManager poolingConnManager
                = new PoolingHttpClientConnectionManager();
        CloseableHttpClient client
                = HttpClients.custom().setConnectionManager(poolingConnManager)
                .build();
        client.execute(new HttpGet(SAMPLE_URL));
    }

    @Test
    public void test_success_multiple_host_the_same_pool() throws InterruptedException {
        HttpGet get1 = new HttpGet(SAMPLE_URL);
        HttpGet get2 = new HttpGet("http://google.com");
        PoolingHttpClientConnectionManager connManager
                = new PoolingHttpClientConnectionManager();
        CloseableHttpClient client1
                = HttpClients.custom().setConnectionManager(connManager).build();
        CloseableHttpClient client2
                = HttpClients.custom().setConnectionManager(connManager).build();

        MultiHttpClientConnThread thread1
                = new MultiHttpClientConnThread(client1, get1, 0);
        MultiHttpClientConnThread thread2
                = new MultiHttpClientConnThread(client2, get2, 0);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    @Test
    public void testKeepAliveNotExpire() throws InterruptedException {
        HttpGet get = new HttpGet(SAMPLE_URL);
        PoolingHttpClientConnectionManager connManager
                = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(5);

        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();

        MultiHttpClientConnThread[] threads
                = new  MultiHttpClientConnThread[10];
        for(int i = 0; i < threads.length; i++){

            // Executing first 5 parallel requesting to server.
            if(i < 5) {
                threads[i] = new MultiHttpClientConnThread(client, get, 0);
            } else {
                // Waiting for 70 seconds and then executing next 5 parallel requesting to server.
                threads[i] = new MultiHttpClientConnThread(client, get, 70 * 1000);
            }
        }

        for(int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (MultiHttpClientConnThread thread: threads) {
            thread.join(80 * 1000);
        }
    }
}
