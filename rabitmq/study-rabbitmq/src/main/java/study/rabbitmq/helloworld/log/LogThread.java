package study.rabbitmq.helloworld.log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LogThread implements Runnable {

    public Channel channel;
    public String queueName;
    public String name;

    @Override
    public void run() {

        System.out.printf("[%s] [*] Waiting for messages. To exit press CTRL+C \n", this.name);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.printf(" [%s][x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'\n", this.name);
        };

        try {
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
