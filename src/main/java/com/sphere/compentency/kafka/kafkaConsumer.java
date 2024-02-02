package com.sphere.compentency.kafka;

import com.sphere.compentency.utils.AppProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
@Component
public class kafkaConsumer {
    @Autowired
    private AppProperties props;

    @Autowired
    private ApiService api_services;

    private final Logger logger = LoggerFactory.getLogger(kafkaConsumer.class);
    public kafkaConsumer(AppProperties props, ApiService api_services) {
        this.props = props;
        this.api_services = api_services;
    }
    public void startKafkaConsumer() {

        // Creating consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getKafkaBootstrapServers());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, props.getKafkaGroupID());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // Creating consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribing to the topic
        consumer.subscribe(Arrays.asList(props.getKafkaTopic()));
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

    private void processKafkaMessage(ConsumerRecord<String, String> record) throws IOException, InterruptedException {
        logger.info("Inside processKafkaMessage with record " + record);
        String msg = record.value();
        if (msg != null && !msg.isEmpty() && !msg.trim().isEmpty()) {
            JSONObject json = new JSONObject(record.value());
            JSONObject edata = json.getJSONObject("edata");
            JSONArray userIdsArray = edata.getJSONArray("userIds");
            String userId = "";

            if (userIdsArray.length() > 0) {
                // Extract the first userId from the array
                userId = userIdsArray.getString(0);
            } else {
                // Handle the case where the array is empty
                logger.info("No userIds found in the array");
            }
            // Now, you can pass userIds to your method
            JSONObject relatedObject = json.getJSONObject("edata").getJSONObject("related");
            String courseId = relatedObject.getString("courseId");//do_1139628834519941121286,do_11394806141846323211
            logger.info("Processing Kafka message - userId: {}, courseId: {}", userId, courseId);
            api_services.get_hierarchy(courseId, userId);
        } else {
            logger.warn("Received empty or null message from Kafka");
        }
    }
}