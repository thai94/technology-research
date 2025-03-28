package com.study.tracing.config;

import com.study.tracing.grpc.PublisherServiceImpl;
import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.opentracing.contrib.grpc.TracingServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author thainq
 */
@Configuration
public class GrpcServerConfiguration {

    @Autowired
    private PublisherServiceImpl formatterService;

    @Autowired
    private TracingServerInterceptor tracingServerInterceptor;

    @Bean
    public ServerBuilder<?> nettyServerBuilder() {
        return NettyServerBuilder.forPort(9092);
    }

    @Bean
    public GRpcServerRunner grpcServerRunner(@Qualifier("nettyServerBuilder") ServerBuilder<?> serverBuilder) {
        return new GRpcServerRunner(serverBuilder, formatterService, tracingServerInterceptor);
    }
}
