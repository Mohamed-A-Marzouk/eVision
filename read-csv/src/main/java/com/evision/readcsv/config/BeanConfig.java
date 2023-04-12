package com.evision.readcsv.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
	@Bean
	@Autowired
	public ThreadPoolExecutor personDequeuerTPE() {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 3, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

		return threadPool;
	}

	@Bean
	@Autowired
	public ThreadPoolExecutor personWriterTPE() {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 3, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

		return threadPool;
	}
}
