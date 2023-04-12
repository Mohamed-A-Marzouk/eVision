package com.evision.readcsv.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.evision.readcsv.dto.Person;
import com.evision.readcsv.kafka.KafkaManager;
import com.evision.readcsv.kafka.PersonDeserializer;
import com.evision.readcsv.kafka.PersonKafkaConsumer;
import com.evision.readcsv.kafka.PersonKafkaProducer;
import com.evision.readcsv.kafka.PersonSerializer;
import com.evision.readcsv.tasks.PersonDequeuerTask;

@Service
public class CSVService {

	@Value("${custom.prop.kafkaUrl}")
	private String kafkaUrl;
	@Value("${custom.prop.fileFolder}")
	private String filesPath;

	private final ThreadPoolExecutor personDequeuerTPE;
	private final ThreadPoolExecutor personWriterTPE;
	LineIterator readerIterator = null;
	String line = null;
	Person person = null;
	private PersonKafkaProducer<Person> personKafkaProducerLess1000;
	private PersonKafkaProducer<Person> personKafkaProducerApove1000;
	
	@Autowired
	public CSVService(ThreadPoolExecutor personDequeuerTPE, ThreadPoolExecutor personWriterTPE) {
		this.personDequeuerTPE = personDequeuerTPE;
		this.personWriterTPE = personWriterTPE;
	}

	public void processFile(File file) throws InterruptedException, ExecutionException, IOException {
		try {
			createTopics();
			readerIterator = FileUtils.lineIterator(file, "UTF-8");
			while (readerIterator.hasNext()) {
				line = readerIterator.nextLine();
				String s[] = line.split(",");
				person = new Person(s[0], s[1], Integer.parseInt(s[2]));
				if (person.getAmount() <= 1000) {
					person.setAmount(person.getAmount() + (person.getAmount() * 10 / 100));
					System.out.println(person.toString());
					personKafkaProducerLess1000.produce(person);
				} else {
					person.setAmount((person.getAmount() + (person.getAmount() * 20 / 100)) / 20);
					System.out.println(person.toString());
					personKafkaProducerApove1000.produce(person);
				}

			}
			readerIterator.close();
			personKafkaProducerLess1000.close();
			personKafkaProducerApove1000.close();
			consumeAndWriteFiles();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (readerIterator != null) {
				readerIterator.close();
			}
		}
	}

	private void createTopics() {
		Set<String> kafkaTopics = KafkaManager.getTopics(kafkaUrl);
		if (!kafkaTopics.contains("less_1000")) {
			if (KafkaManager.createTopic(kafkaUrl, "less_1000")) {
				System.out.println("topic less_1000 is created");
				this.personKafkaProducerLess1000 = new PersonKafkaProducer<>(PersonSerializer.class.getCanonicalName(),
						kafkaUrl, "less_1000");
			}
		} else {
			this.personKafkaProducerLess1000 = new PersonKafkaProducer<>(PersonSerializer.class.getCanonicalName(),
					kafkaUrl, "less_1000");
		}
		if (!kafkaTopics.contains("apove_1000")) {
			if (KafkaManager.createTopic(kafkaUrl, "apove_1000")) {
				System.out.println("topic apove_1000 is created");
				this.personKafkaProducerApove1000 = new PersonKafkaProducer<>(PersonSerializer.class.getCanonicalName(),
						kafkaUrl, "apove_1000");
			}
		} else {
			this.personKafkaProducerApove1000 = new PersonKafkaProducer<>(PersonSerializer.class.getCanonicalName(),
					kafkaUrl, "apove_1000");
		}

	}

	private void consumeAndWriteFiles() throws IOException, InterruptedException {
		PersonKafkaConsumer<Person> topicConsumer = null;
		Set<String> kafkaTopics = KafkaManager.getTopics(kafkaUrl);
		ArrayList<Person> result = new ArrayList<>();
		for(String topic : kafkaTopics) {
			PersonDequeuerTask personDequeuerTask = new PersonDequeuerTask(personWriterTPE, kafkaUrl, topic,filesPath);
			personDequeuerTPE.execute(personDequeuerTask);
			personDequeuerTPE.awaitTermination(10, TimeUnit.SECONDS);

		}

	}

}
