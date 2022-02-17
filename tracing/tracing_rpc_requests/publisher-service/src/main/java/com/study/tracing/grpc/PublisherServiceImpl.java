package com.study.tracing.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class FormatterServiceImpl extends FormatterServiceGrpc.FormatterServiceImplBase {

    @Override
    public void format(FormatRequest request, StreamObserver<FormatResponse> responseObserver) {

        responseObserver.onNext(FormatResponse.newBuilder().setMsg(String.format("Hello, %s!", request.getHelloTo())).build());
        responseObserver.onCompleted();
    }
}
