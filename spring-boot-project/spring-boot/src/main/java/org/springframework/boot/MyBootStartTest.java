package org.springframework.boot;

import org.springframework.context.ApplicationContext;


public class MyBootStartTest {
	public static void main(String[] args) {
		SpringApplication.run(MyBootStartTest.class);
		SpringApplication application=new SpringApplication(MyBootStartTest.class);
		ApplicationContext applicationContext=application.run(args);
		//applicationContext.publishEvent();
	}
}
