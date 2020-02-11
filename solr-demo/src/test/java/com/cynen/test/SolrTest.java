package com.cynen.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.AdvisedSupportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cynen.pojo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")
public class SolrTest {
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	/**
	 * 新增单个数据
	 */
	@Test
	public void add() {
		TbItem tbItem = new TbItem();
		tbItem.setId(1L);
		tbItem.setBrand("华为");
		tbItem.setCategory("手机");
		tbItem.setGoodsId(1L);
		tbItem.setImage("图片");
		tbItem.setPrice(new BigDecimal(1000));
		tbItem.setSeller("珠海");
		tbItem.setTitle("华为Mate");
		solrTemplate.saveBean(tbItem);
		solrTemplate.commit();
	}
	
	// 插入多笔数据
	@Test
	public void addList() {
		List<TbItem> list = new ArrayList<TbItem>();
		for (int i = 0; i < 50; i++) {
			TbItem tbItem = new TbItem();
			tbItem.setId(i+1L);
			tbItem.setBrand("华为");
			tbItem.setCategory("手机");
			tbItem.setGoodsId(i+1L);
			tbItem.setImage("图片");
			tbItem.setPrice(new BigDecimal(1000+i));
			tbItem.setSeller("珠海");
			tbItem.setTitle("华为Mate" + i);
			list.add(tbItem);
		}
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	@Test
	public void deleteOne() {
		solrTemplate.deleteById("1");
		solrTemplate.commit();
	}
	
	//分页查询
	@Test
	public void pageQuery() {
		
		Query query = new SimpleQuery("*:*");
		query.setOffset(0);
		query.setRows(20);
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query , TbItem.class);
		System.out.println(page.getTotalPages());
		System.out.println(page.getTotalElements());
	}
	
	@Test
	public void deleteAll() {
		Query query = new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
	
}
