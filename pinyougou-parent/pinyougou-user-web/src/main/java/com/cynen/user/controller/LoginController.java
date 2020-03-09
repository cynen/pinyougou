package com.cynen.user.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@RequestMapping("/name")
	public Map getName() {
		// 获取当前登录用户的名称.
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Map map = new HashMap<>();
		map.put("loginDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		map.put("loginName", name);
		return map;
	}

}
