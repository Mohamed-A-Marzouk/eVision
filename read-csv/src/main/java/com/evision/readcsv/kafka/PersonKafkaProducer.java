package com.evision.readcsv.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class PersonKafkaProducer<Person> {
	private final KafkaProducer<String, Person> producer;
    private String topic;
    
    public PersonKafkaProducer(String valueSerializer, String bootstrapServer, String topic) {
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        producer = new KafkaProducer<>(prop);
        this.topic = topic;
    }
    public void produce(Person person) throws InterruptedException, ExecutionException {
        ProducerRecord<String, Person> record = new ProducerRecord<>(topic, person);
        
        producer.send(record, new Callback() {

           @Override
           public void onCompletion(RecordMetadata metadata, Exception ex) {
              if (ex != null) {
            	  System.out.println("Error while inserting in kafka: "+ ex.getMessage());
              }
           }
        });
     }

    public void close() {
        producer.close();
    }
}
