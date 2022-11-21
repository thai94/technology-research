package com.study.tracing.controller;

import com.study.tracing.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.contrib.grpc.TracingClientInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainGrpcController {

    @Autowired
    JaegerTracer tracer;
    @Autowired
    TracingClientInterceptor tracingClientInterceptor;

    @RequestMapping(value = "/api/1.0/main-grpc", method = RequestMethod.GET)
    public ResponseEntity main(@RequestParam String name, @RequestHeader Map<String, String> headers) {

        Span span = tracer.buildSpan("main-grpc").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            publish(format(name));
            return new ResponseEntity<String>("finished", HttpStatus.OK);
        } finally {
            span.finish();
        }
    }

    public String format(String name) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091).usePlaintext().build();
        FormatterServiceGrpc.FormatterServiceBlockingStub stub = FormatterServiceGrpc.newBlockingStub(channel)
                .withInterceptors(tracingClientInterceptor);

        FormatResponse response = stub.format(FormatRequest.newBuilder().setHelloTo(name).build());
        return response.getMsg();
    }

    public void publish(String helloStr) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9092).usePlaintext().build();
        PublisherServiceGrpc.PublisherServiceBlockingStub stub = PublisherServiceGrpc.newBlockingStub(channel).withInterceptors(tracingClientInterceptor);
        stub.publish(PublishRequest.newBuilder().setHelloStr(helloStr).build());
    }
}
