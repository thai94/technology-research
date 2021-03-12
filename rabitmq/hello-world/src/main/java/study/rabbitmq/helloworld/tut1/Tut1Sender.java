package study.rabbitmq.helloworld.tut1;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

public class Tut1Sender {

    @Autowired
    private RabbitTemplate template;

    @Qualifier("hello")
    @Autowired
    private Queue queue;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 1, initialDelay = 50)
    public void send() {

        List<SendThread> listThread = new ArrayList<>();
        for (int i = 0; i < 500; i ++) {
            SendThread sendThread = new SendThread(rabbitTemplate, queue.getName());
            listThread.add(sendThread);
        }

        for (int i = 0; i < 500; i ++) {
            listThread.get(i).run();
        }

//        String message = "Hello World!";
//        this.rabbitTemplate.convertAndSend(queue.getName(), message);
//        System.out.println(" [x][hello] Sent '" + message + "'");
    }
}
