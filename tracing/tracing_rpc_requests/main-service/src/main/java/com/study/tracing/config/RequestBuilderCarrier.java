package com.study.tracing.config;

import java.util.Iterator;
import java.util.Map;

public class RequestBuilderCarrier implements io.opentracing.propagation.TextMap {

    private final Map<String, String> headers;

    public RequestBuilderCarrier(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }

    @Override
    public void put(String key, String value) {
        headers.put(key, value);
    }
}
