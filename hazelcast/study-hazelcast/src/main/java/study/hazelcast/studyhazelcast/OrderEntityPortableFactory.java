package study.hazelcast.studyhazelcast;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;

import java.util.HashMap;
import java.util.Map;

public class OrderEntityPortableFactory implements PortableFactory {

    private static final Map<Integer, Portable> DATA = new HashMap<>();

    static {
        DATA.put(1, new OrderEntity());
        DATA.put(2, new OrderEntity2());
    }

    @Override
    public Portable create(int version) {
        return DATA.get(version);
    }
}
