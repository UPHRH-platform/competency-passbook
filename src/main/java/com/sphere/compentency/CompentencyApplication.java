package com.sphere.compentency;

import com.sphere.compentency.kafka.kafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CompentencyApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CompentencyApplication.class, args);

		kafkaConsumer consumer = context.getBean(kafkaConsumer.class);
		consumer.startKafkaConsumer();
	}
}