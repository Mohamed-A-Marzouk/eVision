package com.evision.readcsv.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class PersonKafkaConsumer<Person> {
	private final KafkaConsumer<String, Person> consumer;
	
    public PersonKafkaConsumer(String valueDeserializer, String bootstrapServer, String topic) {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.put(ConsumerConfig.GROUP_ID_CONFIG,"Person");
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        prop.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);

        consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(topic));
    }
    
    public ArrayList<Person> consume() {
        ArrayList<Person> result = new ArrayList<>();
        ConsumerRecords<String, Person> records = consumer.poll(Duration.ofSeconds(5));
        if (records != null) {
            for (ConsumerRecord<String, Person> record : records) {
                result.add(record.value());
            }
            consumer.commitAsync();
        }
        System.out.println("Consumer  get from kafka batch size:" + result.size());
        return result;
    }
}
