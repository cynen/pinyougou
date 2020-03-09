package com.cynen.cas.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	// 获取登录名
	@RequestMapping("/findLoginUser")
	public void getLoginName() {
		
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("登录名：" + name);
	}
	
	
}
