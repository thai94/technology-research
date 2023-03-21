package com.study.tracing.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImpl extends PublisherServiceGrpc.PublisherServiceImplBase {

    @Override
    public void publish(PublishRequest request, StreamObserver<PublishResponse> responseObserver) {

        System.out.println(request.getHelloStr());
        responseObserver.onNext(PublishResponse.newBuilder().setMsg("published").build());
        responseObserver.onCompleted();
    }
}
