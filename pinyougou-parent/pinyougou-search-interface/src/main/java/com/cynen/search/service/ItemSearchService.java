package com.cynen.search.service;


import java.util.List;
import java.util.Map;

public interface ItemSearchService {
	
	public Map<String, Object> search(Map searchMap);
	
	
	// 导入列表。
	public void importList(List list);
	
	// 删除列表
	public  void deleteByGoodsIds(List goodsIdList);
}
