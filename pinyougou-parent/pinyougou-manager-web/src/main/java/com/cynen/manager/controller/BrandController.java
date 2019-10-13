package com.cynen.manager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cynen.pojo.TbBrand;
import com.cynen.sellersgoods.service.BrandService;


@Controller
@RequestMapping("/brand")
public class BrandController {

	
	@Reference
	private BrandService brandService;
	
	
	@RequestMapping("/findAll")
	@ResponseBody
	public List<TbBrand> findAll() {
		System.out.println("监听到了  /brand/findAll 请求....");
		return brandService.findAll();
		
	}
}
