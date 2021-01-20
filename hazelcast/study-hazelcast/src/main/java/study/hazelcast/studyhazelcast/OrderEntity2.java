package study.hazelcast.studyhazelcast;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;

public class OrderEntity2 implements Portable {

    private final static int classVersion = 2;

    private Long orderId;
    private Long amount;
    private String description;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getFactoryId() {
        return 1;
    }

    @Override
    public int getClassId() {
        return classVersion;
    }

    @Override
    public void writePortable(PortableWriter portableWriter) throws IOException {
        portableWriter.writeLong("orderId", orderId);
        portableWriter.writeLong("amount", amount);
        portableWriter.writeUTF("description", description);
    }

    @Override
    public void readPortable(PortableReader portableReader) throws IOException {
        this.orderId = portableReader.readLong("orderId");
        this.amount = portableReader.readLong("amount");
        this.description = portableReader.readUTF("description");
    }

    @Override
    public String toString() {
        return "{" + this.orderId + "-" + this.amount + "-" + this.description +"}";
    }
}
