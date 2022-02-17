package com.study.tracing.config;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;

@Configuration
public class JaegerConfiguration {

    @Bean
    public JaegerTracer jaegerTracer() {

        SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv()
                .withLogSpans(true);

        io.jaegertracing.Configuration config = new io.jaegertracing.Configuration("Sample service")
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        return config.getTracer();
    }
}
