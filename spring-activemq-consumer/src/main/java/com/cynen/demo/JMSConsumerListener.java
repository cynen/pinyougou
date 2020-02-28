package com.cynen.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


public class JMSConsumerListener implements MessageListener{

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("接受到消息：" + textMessage.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
