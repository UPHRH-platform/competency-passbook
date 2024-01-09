package com.sphere.compentency;

import com.sphere.compentency.kafka.consumer.api.kafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sphere.compentency.kafka.consumer.api")
public class CompentencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompentencyApplication.class, args);
		kafkaConsumer.startKafkaConsumer();
	}
}