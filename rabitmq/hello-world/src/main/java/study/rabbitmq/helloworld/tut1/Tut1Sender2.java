package study.rabbitmq.helloworld.tut1;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

public class Tut1Sender2 {

    @Autowired
    private RabbitTemplate template;

    @Qualifier("hello2")
    @Autowired
    private Queue queue;

    @Autowired
    AmqpTemplate amqpTemplate;

//    @Scheduled(fixedDelay = 1, initialDelay = 50)
    public void send() {
        String message = "Hello World!";
        this.amqpTemplate.convertAndSend(queue.getName(), message);
        System.out.println(" [x][hello2] Sent '" + message + "'");
    }
}
