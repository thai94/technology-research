package com.smile.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MultiHttpClientConnThread extends Thread {


    private CloseableHttpClient client;
    private HttpGet get;
    private int spleepTime = 0;

    public MultiHttpClientConnThread(CloseableHttpClient client, HttpGet get, int sleepTime) {
        this.client  = client;
        this.get = get;
        this.spleepTime = sleepTime;
    }

    // standard constructors
    public void run(){
        try {
            Thread.sleep(spleepTime);
            HttpResponse response = client.execute(get);
            EntityUtils.consume(response.getEntity());
            System.out.println("========== Finshed Thread ==========");
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
