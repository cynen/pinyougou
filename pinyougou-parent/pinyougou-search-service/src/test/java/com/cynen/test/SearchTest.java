package com.cynen.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cynen.search.service.ItemSearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:spring/applicationContext*.xml")
public class SearchTest {
	
	@Autowired 
	private ItemSearchService service;
	
	@Test
	public void test() {
		Map searchMap = new HashMap<String, Object>();
		searchMap.put("keywords", "三星");
		Map<String, Object> map = service.search(searchMap );
		System.out.println(map.get("rows"));
	}
}
