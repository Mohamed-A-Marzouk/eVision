package com.evision.readcsv.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.evision.readcsv.dto.Person;

public class CsvWriterTask implements Runnable{
	private ArrayList<Person> records ;
	private String filePath;
	private String topicName;
	
	
	public CsvWriterTask(ArrayList<Person> records, String filePath,String topicName) {
		super();
		this.records = records;
		this.filePath = filePath;
		this.topicName = topicName;
	}


	@Override
	public void run() {
		BufferedWriter bw;
		String fileName = topicName + ".csv";
		System.out.println(fileName);
		try {
			bw = new BufferedWriter(new FileWriter(new File(filePath, fileName), true));
			String columnNames = "name," + "nationalId," + "amount";
			bw.write(columnNames);
			bw.newLine();
			for (Person person :records) {
				bw.write(person.print());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

}
