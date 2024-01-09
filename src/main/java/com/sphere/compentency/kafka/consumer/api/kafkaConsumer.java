package com.sphere.compentency.kafka.consumer.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sphere.compentency.common.utils.propertiesCache;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class kafkaConsumer {


    private static final Logger logger = LoggerFactory.getLogger(kafkaConsumer.class);
    private static ApiService api_services = new ApiService();

    public static void startKafkaConsumer() {
        propertiesCache env = new propertiesCache();


        String bootstrapServers = "localhost:9092";
        String groupId = "dev-activity-aggregate-updater-group";
        String topic = "dev.issue.certificate.request";
        System.out.println("Bootstrap Servers: " + bootstrapServers);

        // Creating consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // Creating consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribing to the topic
        consumer.subscribe(Arrays.asList(topic));
        logger.info("Kafka consumer subscribed and running");

        // Polling for messages
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    processKafkaMessage(record);
                }
            }
        } catch (Exception e) {
            logger.error("Error in Kafka consumer: {}", e.getMessage(), e);
        } finally {
            // Close the consumer when done
            consumer.close();
        }
    }

    private static void processKafkaMessage(ConsumerRecord<String, String> record) throws IOException, InterruptedException {
        logger.info("Inside processKafkaMessage with record " + record);
        String msg = record.value();
        if (msg != null && !msg.isEmpty() && !msg.trim().isEmpty()) {
            JSONObject json = new JSONObject(record.value());
            JSONObject edata = json.getJSONObject("edata");
            String userIds = edata.getString("userIds");
            // Now, you can pass userIds to your method
            JSONObject relatedObject = json.getJSONObject("edata").getJSONObject("related");
            String courseId = relatedObject.getString("courseId");//do_1139628834519941121286,do_11394806141846323211
            logger.info("Processing Kafka message - userId: {}, courseId: {}", userIds, courseId);
            api_services.get_hierarchy(courseId, userIds);
        } else {
            logger.warn("Received empty or null message from Kafka");
        }
    }
}
