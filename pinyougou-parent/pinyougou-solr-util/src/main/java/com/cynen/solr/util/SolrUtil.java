package com.cynen.solr.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cynen.mapper.TbItemMapper;
import com.cynen.pojo.TbItem;
import com.cynen.pojo.TbItemExample;
import com.cynen.pojo.TbItemExample.Criteria;

/**
 * 此项目一般只初始化一次。
 * 
 * @author myth
 */



@Component
public class SolrUtil {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void importData() {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");
		List<TbItem> list = itemMapper.selectByExample(example );
		System.out.println("开始导入数据。。。");
		for (TbItem tbItem : list) {
			// 将spec字段中的json字符串转换为map
			// Map specMap = JSON.parseObject(tbItem.getSpec(), Map.class);
			Map specMap = JSON.parseObject(tbItem.getSpec());
			tbItem.setSpecMap(specMap);
		}
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
		System.out.println("结束。。。");
	}
	
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
		solrUtil.importData();
	}
}
