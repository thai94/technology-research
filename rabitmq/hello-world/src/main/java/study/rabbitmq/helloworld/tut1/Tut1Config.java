package study.rabbitmq.helloworld.tut1;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;


@Profile({"tut1","hello-world"})
@Configuration
@EnableScheduling
public class Tut1Config {

    @Bean
    public Queue hello() {
        return new Queue("hello");
    }

    @Bean
    public Queue hello2() {
        return new Queue("hello2");
    }

    @Profile("receiver")
    @Bean
    public Tut1Receiver receiver() {
        return new Tut1Receiver();
    }

    @Profile("sender")
    @Bean
    public Tut1Sender sender() {
        return new Tut1Sender();
    }

    @Profile("receiver")
    @Bean
    public Tut1Receiver2 receiver2() {
        return new Tut1Receiver2();
    }

    @Profile("sender")
    @Bean
    public Tut1Sender2 sender2() {
        return new Tut1Sender2();
    }
}
