package com.cynen.demo.controller;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	
	@JmsListener(destination="springboot.queue")
	public void reciveMsg(String text) {
		System.out.println("接受到消息:" + text);
	}
}
