package study.microservice.registry.serviceregistryclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StudyServiceRegistryClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyServiceRegistryClientApplication.class, args);
	}

}
