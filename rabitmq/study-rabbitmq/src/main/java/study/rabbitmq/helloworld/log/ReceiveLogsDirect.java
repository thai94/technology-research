package study.rabbitmq.helloworld.log;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.103");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String infoQueueName = channel.queueDeclare().getQueue();
        String errorQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(infoQueueName, EXCHANGE_NAME, "info");
        channel.queueBind(errorQueueName, EXCHANGE_NAME, "error");

        LogThread info = new LogThread();
        info.queueName = infoQueueName;
        info.channel = connection.createChannel();
        info.name = "info";

        LogThread error = new LogThread();
        error.queueName = errorQueueName;
        error.channel = connection.createChannel();
        error.name = "error";

        info.run();
        error.run();
    }
}
