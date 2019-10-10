package com.zte.dubbo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zte.dubbo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Reference
	private UserService userService;
	
	
	@RequestMapping("/get")
	@ResponseBody
	public String getName() {
		// return "123";
		return userService.getName();
	}
	
	
}
