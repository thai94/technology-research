package com.study.tracing.config;

import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.opentracing.contrib.grpc.TracingClientInterceptor;
import io.opentracing.contrib.grpc.TracingServerInterceptor;
import io.opentracing.propagation.Format;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaegerConfiguration {

    @Bean
    public JaegerTracer jaegerTracer() {

        SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv()
                .withType(ProbabilisticSampler.TYPE)
                .withParam(0.1);

        ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv()
                .withLogSpans(true);

        io.jaegertracing.Configuration config = new io.jaegertracing.Configuration("Publisher service")
                .withCodec(io.jaegertracing.Configuration.CodecConfiguration.fromString("b3"))
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        return config.getTracer();
    }

    @Bean
    public TracingServerInterceptor tracingServerInterceptor(@Qualifier("jaegerTracer") JaegerTracer tracer) {
        return TracingServerInterceptor
                .newBuilder()
                .withTracer(tracer)
                .withStreaming()
                .withVerbosity()
                .withTracedAttributes(TracingServerInterceptor.ServerRequestAttribute.CALL_ATTRIBUTES)
                .build();
    }
}
