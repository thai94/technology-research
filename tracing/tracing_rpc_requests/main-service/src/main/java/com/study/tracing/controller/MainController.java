package com.study.tracing.controller;

import com.study.tracing.config.RequestBuilderCarrier;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.propagation.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    JaegerTracer tracer;

    @RequestMapping(value = "/api/1.0/main", method = RequestMethod.GET)
    public ResponseEntity main(@RequestParam String name, @RequestHeader Map<String, String> headers) {

        Span span = tracer.buildSpan("main").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            publish(format(name));
            return new ResponseEntity<String>("finished", HttpStatus.OK);
        } finally {
            span.finish();
        }
    }

    public String format(String name) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> header = new HashMap<>();

        Span activeSpan = tracer.activeSpan();
        tracer.inject(activeSpan.context(), Format.Builtin.HTTP_HEADERS, new RequestBuilderCarrier(header));

        for (String key : header.keySet()) {
            httpHeaders.add(key, header.get(key));
        }

        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> respEnt = restTemplate.exchange("http://localhost:8081/api/1.0/format?helloTo=" + name,
                HttpMethod.GET, entity, String.class);
        return respEnt.getBody();
    }

    public void publish(String helloStr) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> header = new HashMap<>();

        Span activeSpan = tracer.activeSpan();
        tracer.inject(activeSpan.context(), Format.Builtin.HTTP_HEADERS, new RequestBuilderCarrier(header));

        for (String key : header.keySet()) {
            httpHeaders.add(key, header.get(key));
        }

        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);

        ResponseEntity<String> respEnt = restTemplate.exchange("http://localhost:8082/api/1.0/publish?helloStr=" + helloStr, HttpMethod.GET,
                entity, String.class);
    }
}
