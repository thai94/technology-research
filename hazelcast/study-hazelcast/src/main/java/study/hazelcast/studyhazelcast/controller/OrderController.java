package study.hazelcast.studyhazelcast.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.hazelcast.studyhazelcast.OrderEntity;
import study.hazelcast.studyhazelcast.OrderEntity2;

@RestController
public class OrderController {

    @Autowired
    HazelcastInstance hzc;

    @GetMapping("/demo1/set")
    public String demo1Set() {
        IMap map = hzc.getMap("ORDER_MAP");
        OrderEntity entity1 = new OrderEntity();
        entity1.setOrderId(1L);
        entity1.setAmount(1000L);
        entity1.setDescription("Test");
        map.put(entity1.getOrderId(), entity1);
        return "success";
    }

    @GetMapping("/demo1/get")
    public String demo1Get() {
        IMap map = hzc.getMap("ORDER_MAP");
        OrderEntity entity1 = (OrderEntity)map.get(1L);
        return entity1.toString();
    }

    @GetMapping("/demo2/set")
    public String demo2Set() {
        IMap map = hzc.getMap("ORDER_MAP");
        OrderEntity2 entity2 = new OrderEntity2();
        entity2.setOrderId(1L);
        entity2.setAmount(1000L);
        entity2.setDescription("Test");
        map.put(entity2.getOrderId(), entity2);
        return "success";
    }

    @GetMapping("/demo2/get")
    public String demo2Get() {
        IMap map = hzc.getMap("ORDER_MAP");
        OrderEntity2 entity2 = (OrderEntity2)map.get(1L);
        return entity2.toString();
    }
}
