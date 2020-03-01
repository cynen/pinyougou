package com.cynen.search.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cynen.pojo.TbItem;
import com.cynen.search.service.ItemSearchService;

/**
 * 消息队列的监听器。替代dubbo的相互依赖。
 * 用于监听mq上的  配置队列，在配置文件中进行配置
 * @author myth
 *
 */
@Component
public class ItemSearchDeleteListener implements MessageListener{

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		System.out.println("ItemSearchDeleteListener监听到消息");
		try {
			
			ObjectMessage objectMessage =  (ObjectMessage) message;
			Long[] ids = (Long[]) objectMessage.getObject();
			System.out.println("监听到的消息内容： "+ Arrays.asList(ids));
			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			System.out.println("成功删除索引库数据。。。");
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("ItemSearchDeleteListener 删除索引库异常。");
		}
	}
}
