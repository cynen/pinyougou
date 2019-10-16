package com.cynen.manager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cynen.pojo.TbBrand;
import com.cynen.sellersgoods.service.BrandService;

import entity.PageResult;
import entity.Result;


@RestController
@RequestMapping("/brand")
public class BrandController {

	
	@Reference
	private BrandService brandService;
	
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll() {
		return brandService.findAll();
		
	}
	
	@RequestMapping("/findPage")
	public PageResult findPage(int currPage,int pageSize) {
		// 分页查询.
		return brandService.findPage(currPage, pageSize);
	}
	
	@RequestMapping("/add")
	public Result addBrand(@RequestBody TbBrand tbBrand) {
		try {
			brandService.addbrand(tbBrand);
			return new Result(true, "添加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败!");
		}
		
	}
	
}
