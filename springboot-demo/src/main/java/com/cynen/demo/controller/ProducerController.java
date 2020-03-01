package com.cynen.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@RequestMapping("/send")
	public void send(String text) {
			
		// Destination destination = new ActiveMQQueue("test");
		
		/*jmsTemplate.send("test", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("测试一下。。。");
			}
		});*/
		jmsMessagingTemplate.convertAndSend("springboot.queue", text);
	}
	
	
}
