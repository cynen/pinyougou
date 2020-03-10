package com.cynen.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cynen.page.service.impl.ItemPageServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-service.xml")
public class FreemarkerTest {
	
	@Autowired
	private ItemPageServiceImpl itemPageServiceImpl;
	
	@Test
	public void test() {
		
		boolean flag = itemPageServiceImpl.genItemHtml(149187842867934L);
		System.out.println(flag);
	}
	
}
