package com.cynen.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}
}
