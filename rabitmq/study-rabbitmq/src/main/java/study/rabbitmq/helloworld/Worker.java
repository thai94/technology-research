package study.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Worker {
    private static final String TASK_QUEUE_NAME = "task_queue";
    public static void main(String[] argv) throws Exception {

        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String jvmName = bean.getName();
        System.out.println("Name = " + jvmName);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.103");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        WorkerThread worker1 = new WorkerThread();
        worker1.name = "A";
        worker1.sleepTime = 1000;
        worker1.connection = connection;

        WorkerThread worker2 = new WorkerThread();
        worker2.name = "B";
        worker2.connection = connection;

        worker1.run();
        worker2.run();
    }

}
