package com.evision.readcsv.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.evision.readcsv.dto.Person;

public class PersonSerializer implements Serializer<Person> {

	private final ObjectMapper objectMapper = new ObjectMapper(); 
	

    public byte[] serialize(String topic, Person person) {
        try {
            if (person == null) {
            	System.out.println("Null person for serializing");
                return null;
            }
            System.out.println("Serializing person...");
            return objectMapper.writeValueAsBytes(person);
        } catch (Exception e) {
        	System.out.println("Error when serializing person to byte[]");
            throw new SerializationException("Error when serializing person to byte[]");
        }
    }

}
