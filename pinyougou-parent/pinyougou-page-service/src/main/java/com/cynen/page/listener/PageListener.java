package com.cynen.page.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cynen.page.service.ItemPageService;

@Component
public class PageListener implements MessageListener{
	
	@Autowired
	private ItemPageService itemPageService;
	

	@Override
	public void onMessage(Message message) {
		// 生成页面.
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			System.out.println("接受到消息: " + text+",准备生成详情页");
			itemPageService.genItemHtml(Long.parseLong(text));
			System.out.println("商品详情页生成成功:"+ text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
