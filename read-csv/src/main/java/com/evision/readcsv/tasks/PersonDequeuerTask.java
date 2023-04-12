package com.evision.readcsv.tasks;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.evision.readcsv.dto.Person;
import com.evision.readcsv.kafka.PersonDeserializer;
import com.evision.readcsv.kafka.PersonKafkaConsumer;

public class PersonDequeuerTask implements Runnable{

	private PersonKafkaConsumer<Person> topicConsumer;
	private final ThreadPoolExecutor personWriterTPE;
	private String topicName;
	private String filesPath;
	public PersonDequeuerTask(ThreadPoolExecutor personWriterTPE,String kafkaUrl,String topicName,String filesPath) {
		this.personWriterTPE = personWriterTPE;
		topicConsumer = new PersonKafkaConsumer<>(PersonDeserializer.class.getCanonicalName(), kafkaUrl,
				topicName);
		this.topicName = topicName;
		this.filesPath =filesPath;
		
	}

	@Override
	public void run() {
		ArrayList<Person> result = new ArrayList<>();
		result.addAll(topicConsumer.consume());
		if (result != null && !result.isEmpty()) {
			System.out.println("consume from "+ topicName+ " : "+result.size());
			personWriterTPE.execute(new CsvWriterTask(result, filesPath, topicName));
			
		}
		
	}

	

}
