package com.cynen.text;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestValue {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void setValue() {
		redisTemplate.boundValueOps("name").set("hello");
	}
	@Test
	public void getValue() {
		String string = redisTemplate.boundValueOps("name").get();
		System.out.println("从redis中获取值: "+string);
		
	}
	@Test
	public void delateValue() {
		redisTemplate.delete("name");
	}
	
	
	@Test
	public void setValueSet(){
		redisTemplate.boundSetOps("nameset").add("曹操");		
		redisTemplate.boundSetOps("nameset").add("刘备");	
		redisTemplate.boundSetOps("nameset").add("孙权");
	}
	
	@Test
	public void getValueSet(){
		Set members = redisTemplate.boundSetOps("nameset").members();
		System.out.println(members);
	}
	
	
}
