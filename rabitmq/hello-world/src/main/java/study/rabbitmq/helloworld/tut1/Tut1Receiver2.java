package study.rabbitmq.helloworld.tut1;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "hello2")
public class Tut1Receiver2 {
    @RabbitHandler
    public void receive(String in) {
        System.out.println(" [x][hello2] Received '" + in + "'");
    }
}