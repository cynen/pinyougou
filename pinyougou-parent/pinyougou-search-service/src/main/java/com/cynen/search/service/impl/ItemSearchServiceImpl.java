package com.cynen.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
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
		
/*		Query query = new SimpleQuery();
		// 添加条件查询。
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria );
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query , TbItem.class);
		System.out.println("总记录数： "+page.getTotalElements());*/
		//将查询到的结果集保存到返回map中。
		// resultMap.put("rows", page.getContent());
		
		// 1.高亮搜索
		resultMap.putAll(searchList(searchMap));
		// 2.分类搜索。
		List categoryList = searchCategoryList(searchMap);
		resultMap.put("categoryList", categoryList);
		
		return resultMap;
	}
	
	// 2.商品分类列表。
	private List searchCategoryList(Map searchMap) {
		List<String> list = new ArrayList<String>();
		// 构建查询对象
		Query query = new SimpleQuery();
		// 按照关键字查询。
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// 设置分组查询。
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions );
		// 得到分组页。
		GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query , TbItem.class);
		// 根据分组列，得到分组结果集。
		GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");
		// 入口页。
		Page<GroupEntry<TbItem>> entries = groupResult.getGroupEntries();
		// 得到分组集合
		List<GroupEntry<TbItem>> content = entries.getContent();
		for (GroupEntry<TbItem> entry : content) {
			list.add(entry.getGroupValue());
		}
		return list;
	} 
	
	// 1.高亮列表查询。
	private Map searchList(Map searchMap) {
		Map map = new HashMap();
		// 构建一个高亮查询。
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");// 设置查询的高亮域
		highlightOptions.setSimplePrefix("<em style='color:red'>");//高亮后缀
		highlightOptions.setSimplePostfix("</em>");//高亮前缀
		query.setHighlightOptions(highlightOptions);
		// 按照关键字查询。
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria );
		// 查询出高亮结果的列表。
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);
		// 循环高亮结果集入口,就是为了将高亮结果集中的内容做高亮展示。
		for (HighlightEntry<TbItem> h : highlightPage.getHighlighted()) {
			TbItem item = h.getEntity(); // 获取实体
			if (h.getHighlights().size() >0 && h.getHighlights().get(0).getSnipplets().size() > 0 ) {
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0)); // 设置高亮结果。 
			}
		}
		map.put("rows", highlightPage.getContent());
		System.out.println("高亮总记录数： "+highlightPage.getTotalElements());
		return map;
	}

}
