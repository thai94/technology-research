package com.study.midservice;

import com.study.grpc.finalservice.FinalServiceGrpc;
import com.study.grpc.finalservice.Finalservice;
import com.study.grpc.midservice.HelloReply;
import com.study.grpc.midservice.HelloRequest;
import com.study.grpc.midservice.MidServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.concurrent.TimeUnit;

@GRpcService
public class MidGrpcServiceController extends MidServiceGrpc.MidServiceImplBase {


    Channel channel = ManagedChannelBuilder.forTarget("localhost:7002").usePlaintext().build();
    FinalServiceGrpc.FinalServiceBlockingStub stub = FinalServiceGrpc.newBlockingStub(channel);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {

        try {
            responseObserver.onNext(doBuz());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    HelloReply doBuz(){

        System.out.println("1. Step 1");

//        try {
//            Thread.sleep(5 * 1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("2. Step 2");

        com.study.grpc.finalservice.HelloRequest request = com.study.grpc.finalservice.HelloRequest.newBuilder().build();

        com.study.grpc.finalservice.HelloReply resp = null;
        try {
            resp = stub.withDeadlineAfter(5, TimeUnit.SECONDS).sayHello(request);
        } catch (StatusRuntimeException ex) {
            if(ex.getMessage().equalsIgnoreCase("CANCELLED: io.grpc.Context was cancelled without error")) {
                System.out.println("STOP");
            }
            throw ex;
        }

        System.out.println("3. Step 3");

        return HelloReply.newBuilder().setMessage(resp.getMessage()).build();
    }
}
