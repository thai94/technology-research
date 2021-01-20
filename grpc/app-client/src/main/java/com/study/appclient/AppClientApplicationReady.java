package com.study.appclient;

import com.google.common.util.concurrent.ListenableFuture;
import com.study.grpc.midservice.HelloReply;
import com.study.grpc.midservice.HelloRequest;
import com.study.grpc.midservice.MidServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AppClientApplicationReady {

    Channel channel = ManagedChannelBuilder.forTarget("localhost:7001").usePlaintext().build();
    MidServiceGrpc.MidServiceFutureStub stub = MidServiceGrpc.newFutureStub(channel);

    @EventListener(ApplicationReadyEvent.class)
    public void ready() throws InterruptedException {

        ListenableFuture<HelloReply> resp = stub.withDeadlineAfter(5, TimeUnit.SECONDS).sayHello(HelloRequest.newBuilder().build());
        Thread.sleep(2*1000);
        resp.cancel(true);
    }
}
