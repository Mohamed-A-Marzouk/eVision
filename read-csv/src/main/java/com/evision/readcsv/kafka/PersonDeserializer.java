package com.evision.readcsv.kafka;
import com.evision.readcsv.dto.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;


public class PersonDeserializer implements Deserializer<Person> {

	private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public Person deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
            	System.out.println("Null received at deserializing person");
                return null;
            }
            return objectMapper.readValue(new String(data, "UTF-8"), Person.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to Person");
        }
	}

}
