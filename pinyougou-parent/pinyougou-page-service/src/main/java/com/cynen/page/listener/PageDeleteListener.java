package com.cynen.page.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cynen.page.service.ItemPageService;

@Component
public class PageDeleteListener implements MessageListener{

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] ids = (Long[]) objectMessage.getObject();
			System.out.println("监听到消息:" + ids);
			boolean b = itemPageService.deletePage(ids);
			System.out.println("网页删除结果:" + b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
