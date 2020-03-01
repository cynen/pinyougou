package com.cynen.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@RequestMapping("/send")
	public void send() {
			
		// Destination destination = new ActiveMQQueue("test");
		
		/*jmsTemplate.send("test", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("测试一下。。。");
			}
		});*/
		// jmsMessagingTemplate.convertAndSend("springboot.queue", text);
		
		Map map = new HashMap();
		map.put("mobile", "18986521556");
		map.put("checkcode", "189865");
		
		jmsMessagingTemplate.convertAndSend("pinyougou.user.sms", map);
	}
	
	
}
