package com.cynen.demo.controller;

import java.util.Map;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

public class Consumer {
	
/*	@JmsListener(destination="springboot.queue")
	public void reciveMsg(String text) {
		System.out.println("接受到消息:" + text);
	}*/
	
	@JmsListener(destination="pinyougou.user.sms")    
	public void reciveCode(Map map) {
		System.out.println("接受到手机号:" + map.get("mobile"));
		System.out.println("接受到验证码:" + map.get("checkcode"));
	}
	
	
}
