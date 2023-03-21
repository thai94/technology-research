package com.study.tracing.api;

import com.study.tracing.config.GlobalStore;
import com.study.tracing.entity.Employee;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EmployeeController {

    @Autowired
    GlobalStore globalStore;

    @Autowired
    JaegerTracer tracer;

    @RequestMapping(value = "/api/1.0/employees", method = RequestMethod.POST)
    public ResponseEntity createEmployee(@RequestBody Employee employee) {

        Span span = tracer.buildSpan("create employee").start();

        globalStore.insertEmployee(employee);

        span.setTag("http.status_code", HttpStatus.CREATED.value());
        span.finish();
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/1.0/employees/{id}", method = RequestMethod.GET)
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") String idString) {

        Span span = tracer.buildSpan("get employee").start();
        Employee employee = globalStore.getEmployee(idString, span);
        if (employee != null) {
            printLog(span);
            span.setTag("http.status_code", HttpStatus.OK.value());
            span.finish();
            return new ResponseEntity<Employee>(employee, HttpStatus.OK);
        }
        printLog(span);
        span.setTag("http.status_code", HttpStatus.NOT_FOUND.value());
        span.finish();
        return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
    }

    private void printLog(Span rootSpan) {
        Span span = tracer.buildSpan("printLog").asChildOf(rootSpan).start();
        span.finish();
    }

    private void printLog() {
        Span span = tracer.buildSpan("printLog").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            System.out.println("printLog");
        } finally {
            span.finish();
        }
    }

    @RequestMapping(value = "/api/1.0/employees/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSpan(@PathVariable("id") String idString) {

        Span span = tracer.buildSpan("deleteSpan").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            // delete from store
            globalStore.deleteEmployee(idString);
            // pint log
            printLog();
        } finally {
            span.finish();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
