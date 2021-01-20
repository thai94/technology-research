package com.study.finalservice;

import com.study.grpc.finalservice.FinalServiceGrpc;
import com.study.grpc.finalservice.HelloReply;
import com.study.grpc.finalservice.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class FinalGrpcServiceController extends FinalServiceGrpc.FinalServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {

        try {
            Thread.sleep(1000L);
            responseObserver.onNext(doBuz());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    HelloReply doBuz(){
        System.out.println("1. Step 1");
        System.out.println("2. Step 2");
        try {
            Thread.sleep(10 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("3. Step 3");

        return HelloReply.newBuilder().setMessage("Final Service").build();
    }
}
