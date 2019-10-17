package com.cynen.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	
	// 全部查询
	@RequestMapping("/findAll")
	public List<TbBrand> findAll() {
		return brandService.findAll();
		
	}
	
	// 分页查询
	@RequestMapping("/findPage")
	public PageResult findPage(int currPage,int pageSize) {
		return brandService.findPage(null,currPage, pageSize);
	}
	
	// 添加品牌.
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
	
	// 添加品牌.
	@RequestMapping("/update")
	public Result updateBrand(@RequestBody TbBrand tbBrand) {
		try {
			brandService.updatebrand(tbBrand);
			return new Result(true, "更新成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "更新失败!");
		}
	}
	
	
	// 删除
	@RequestMapping("/delte")
	public Result deleteBrands(Long[] ids ) {
		try {
			brandService.deleteBrand(ids);
			return new Result(true, "删除成功!");
		} catch (Exception e) {
			return new Result(false, "删除失败!");
		}
	}
	
	/**
	 * 条件分页查询.
	 * 必须是POST请求.
	 * @param tbBrand
	 * @param currPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand tbBrand,int currPage,int pageSize) {
		return brandService.findPage(tbBrand, currPage, pageSize);
	}
}
