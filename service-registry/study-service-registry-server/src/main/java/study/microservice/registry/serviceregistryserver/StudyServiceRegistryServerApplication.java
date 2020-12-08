package study.microservice.registry.serviceregistryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class StudyServiceRegistryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyServiceRegistryServerApplication.class, args);
	}

}
