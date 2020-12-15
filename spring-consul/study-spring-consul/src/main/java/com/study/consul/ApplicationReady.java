package com.study.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.kv.model.PutParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReady {

    @Autowired
    ConsulClient consulClient;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
//        consulClient.getKVValue("transfer/study/qc/stable", "43c2237e-6ded-9ab6-7f2f-b3bfc0b4ab34");
//        consulClient.setKVValue("transfer/study/qc/stable", "abc", "43c2237e-6ded-9ab6-7f2f-b3bfc0b4ab34", new PutParams(), QueryParams.DEFAULT);
        System.out.println("Ready...");
    }

}
