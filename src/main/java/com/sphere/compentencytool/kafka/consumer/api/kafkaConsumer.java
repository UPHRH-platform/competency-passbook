package com.sphere.compentencytool.kafka.consumer.api;

import com.sphere.compentencytool.common.utils.AppProperties;
import com.sphere.compentencytool.common.utils.propertiesCache;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;

@SpringBootApplication
public class kafkaConsumer {


    public static void main(String[] args) {
        SpringApplication.run(kafkaConsumer.class, args);

        propertiesCache env=new propertiesCache();

        Api_services api_services= new Api_services();
        Logger logger= LoggerFactory.getLogger(kafkaConsumer.class.getName());

        String bootstrapServers=env.getProperty("kafka.bootstrapServers");
        String grp_id=env.getProperty("kafka.groupID");
        String topic= env.getProperty("kafka.topic");;
         System.out.println("topic  --- > "+topic);
        System.out.println("bootstrapServers  --- > "+bootstrapServers);
        System.out.println("grp_id  --- > "+grp_id);

        //Creating consumer properties
        Properties properties=new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        //creating consumer
        KafkaConsumer<String,String> consumer= new KafkaConsumer<String,String>(properties);
        //Subscribing
        consumer.subscribe(Arrays.asList(topic));
        System.out.println("kafka consumer subscribe & running");
        //polling
        while(true){
            ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record: records) {
//                logger.info("Key: " + record.key() + ", Value:" + record.value());
                String msg = record.value();
                if (msg.length() != 0 & msg != null && !msg.isEmpty() && !msg.trim().isEmpty()) {
                    JSONObject json = new JSONObject(record.value());
                    JSONObject edata = json.getJSONObject("edata");
                    JSONArray userId = (JSONArray) edata.get("userIds");
                    String courseId = (String) edata.get("courseId");
                    System.out.println(edata.get("userIds"));
                    System.out.println(edata.get("courseId"));
                    api_services.get_hierarchy(courseId,userId);
                } else {
                    System.out.println("null check condition : "+record.value());
//                logger.info("Partition:" + record.partition()+",Offset:"+record.offset());
                }

            }
        }
    }
}
