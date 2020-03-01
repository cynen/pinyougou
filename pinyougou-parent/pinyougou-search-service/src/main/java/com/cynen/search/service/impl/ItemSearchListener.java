package com.cynen.search.service.impl;

import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cynen.pojo.TbItem;
import com.cynen.search.service.ItemSearchService;

/**
 * 消息队列的监听器。替代dubbo的相互依赖。
 * @author myth
 *
 */
@Component
public class ItemSearchListener implements MessageListener{

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		System.out.println("ItemSearchListener监听到消息");
		try {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			System.out.println("监听到的消息内容：" + text);
			List<TbItem> list = JSON.parseArray(text, TbItem.class);
			// 还需要将spec字段的string转换成对象。
			for (TbItem tbItem : list) {
				Map specMap = JSON.parseObject(tbItem.getSpec());
				tbItem.setSpecMap(specMap);
			}
			itemSearchService.importList(list);
			System.out.println("导入索引库成功！");
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("ItemSearchListener 导入索引库异常。");
		}
	}
}
