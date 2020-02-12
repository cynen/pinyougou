package com.cynen.search.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.cynen.pojo.TbItem;
import com.cynen.search.service.ItemSearchService;

/**
 * 搜索业务的服务实现。
 * @author myth
 *
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private SolrTemplate solrTemplate;
	
	/**
	 * 直接搜索solr中的数据，不和mysql数据库进行交互。
	 */
	@Override
	public Map<String, Object> search(Map searchMap) {
		System.out.println("搜索Service收到请求。。。");
		// 创建一个返回对象。
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Query query = new SimpleQuery();
		// 添加条件查询。
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria );
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query , TbItem.class);
		System.out.println("总记录数： "+page.getTotalElements());
		//将查询到的结果集保存到返回map中。
		resultMap.put("rows", page.getContent());
		return resultMap;
	}

}
