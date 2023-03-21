package com.study.tracing.config;

import com.study.tracing.entity.Employee;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GlobalStore {

    @Autowired
    JaegerTracer tracer;

    private HashMap<String, Employee> employees = new HashMap<>();

    public void insertEmployee(Employee employee) {

        this.employees.put(employee.getId(), employee);
    }

    public Employee getEmployee(String id, Span pSpan) {
        Span span = tracer.buildSpan("get employee from store").asChildOf(pSpan).start();
        span.finish();
        return this.employees.get(id);
    }

    public void deleteEmployee(String id) {

        Span span = tracer.buildSpan("deleteEmployee").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            this.employees.remove(id);
        } finally {
            span.finish();
        }
    }
}
