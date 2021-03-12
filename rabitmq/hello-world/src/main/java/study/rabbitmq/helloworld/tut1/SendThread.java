package study.rabbitmq.helloworld.tut1;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class SendThread implements Runnable{
    private RabbitTemplate rabbitTemplate;
    private String queueName = "";

    public SendThread(RabbitTemplate rabbitTemplate, String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public void run() {
        String message = "Hello World!";
        this.rabbitTemplate.convertAndSend(queueName, message);
        System.out.println(" [x][hello] Sent '" + message + "'");
    }
}
