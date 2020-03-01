package com.cynen.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
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
	
	@Autowired
	private RedisTemplate redisTemplate;
	
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
		List<String> categoryList = searchCategoryList(searchMap);
		resultMap.put("categoryList", categoryList);
		if (categoryList.size() > 0) {
			resultMap.putAll(searchBrandAndSpecList(categoryList.get(0)));
		}
		
		return resultMap;
	}
	
	
	
	
	// 3.查询分类和品牌列表。
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap();
		// 获取模板id
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
		if(typeId != null ) {
			// 根据模板id查询品牌列表。
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			map.put("brandList", brandList);
			// 根据模板ID查询规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
			map.put("specList", specList);
		}
		return map;
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
		
		
		// 1.按照关键字查询。
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria );
		//1.2 商品分类过滤
		if (!"".equals(searchMap.get("category"))) {
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//1.3 品牌过滤
		if (!"".equals(searchMap.get("brand"))) {
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//1.4 按规格过滤
		if(searchMap.get("spec")!=null){			
			Map<String,String> specMap= (Map<String, String>) searchMap.get("spec");
			for(String key :specMap.keySet()){
				
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key)  );
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);					
			}		
		}
		
		//1.6 分页
		Integer pageNo= (Integer) searchMap.get("pageNo");//获取页码
		if(pageNo==null){
			pageNo=1;
		}
		Integer pageSize= (Integer) searchMap.get("pageSize");//获取页大小
		if(pageSize==null){
			pageSize=20;
		}
		
		query.setOffset( (pageNo-1)*pageSize  );//起始索引
		query.setRows(pageSize);//每页记录数
		
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
		map.put("total", highlightPage.getTotalElements());
		map.put("totalPages", highlightPage.getTotalPages());
		System.out.println("高亮总记录数： "+highlightPage.getTotalElements());
		return map;
	}




	
	
	@Override
	public void importList(List list) {
		// TODO Auto-generated method stub
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}


	// 根据goodids删除索引库中的数据。
	@Override
	public void deleteByGoodsIds(List goodsIdList) {
		Query query = new SimpleQuery();
		Criteria criteria = new Criteria("item_goodsid").in(goodsIdList);
		query.addCriteria(criteria );
		solrTemplate.delete(query);
		solrTemplate.commit();
	}

}
