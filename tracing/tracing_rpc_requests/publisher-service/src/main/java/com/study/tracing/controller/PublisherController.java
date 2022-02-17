package com.study.tracing.controller;

import com.study.tracing.config.RequestBuilderCarrier;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PublisherController {

    @Autowired
    JaegerTracer tracer;

    @RequestMapping(value = "/api/1.0/publish", method = RequestMethod.GET)
    public ResponseEntity format(@RequestParam String helloStr, @RequestHeader Map<String, String> headers) {

        SpanContext parentSpanCtx = tracer.extract(Format.Builtin.HTTP_HEADERS, new RequestBuilderCarrier(headers));
        Span span = tracer.buildSpan("publish").asChildOf(parentSpanCtx).start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            System.out.println(helloStr);
            return new ResponseEntity<String>("published", HttpStatus.OK);
        } finally {
            span.finish();
        }
    }
}
