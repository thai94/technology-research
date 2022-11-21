package com.study.tracing.config;

import com.study.tracing.grpc.FormatterServiceImpl;
import io.grpc.*;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.protobuf.services.HealthStatusManager;
import io.opentracing.contrib.grpc.TracingServerInterceptor;
import org.springframework.context.SmartLifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author thainq
 */
public class GRpcServerRunner implements SmartLifecycle {

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final ServerBuilder<?> serverBuilder;
    private final HealthStatusManager healthStatusManager = new HealthStatusManager();
    private final HealthGrpc.HealthImplBase healthService = (HealthGrpc.HealthImplBase) healthStatusManager.getHealthService();
    private CountDownLatch latch;
    private Server server;
    private final FormatterServiceImpl formatterService;
    private final TracingServerInterceptor tracingServerInterceptor;

    public GRpcServerRunner(ServerBuilder<?> serverBuilder, FormatterServiceImpl formatterService, TracingServerInterceptor tracingServerInterceptor) {

        this.serverBuilder = serverBuilder;
        this.formatterService = formatterService;
        this.tracingServerInterceptor = tracingServerInterceptor;
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }

        System.out.println("Starting gRPC Server ...");
        latch = new CountDownLatch(1);
        try {

            ServerServiceDefinition formatterServiceDn = formatterService.bindService();
            List<ServerInterceptor> formatterInterceptors = new ArrayList<>();
            formatterInterceptors.add(tracingServerInterceptor);
            formatterServiceDn = ServerInterceptors.intercept(formatterServiceDn, formatterInterceptors);

            server = serverBuilder
                    .addService(formatterServiceDn)
                    .addService(healthService)
                    .build()
                    .start();
            isRunning.set(true);
            startDaemonAwaitThread();
            System.out.printf("gRPC Server started, listening on port %s.%n", server.getPort());

        } catch (Exception e) {
            throw new RuntimeException("Failed to start GRPC server", e);
        }
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> {
            try {

                latch.await();
            } catch (InterruptedException e) {
            } finally {
                isRunning.set(false);
            }
        });
        awaitThread.setName("grpc-server-awaiter");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void stop() {
        Optional.ofNullable(server).ifPresent(s -> {
            System.out.println("Shutting down gRPC server ...");

            healthStatusManager.enterTerminalState();
            s.shutdown();
            try {
                s.awaitTermination();
            } catch (InterruptedException e) {
                System.out.println(e);
            } finally {
                latch.countDown();
            }
            System.out.println("gRPC server stopped.");
        });
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }
}
