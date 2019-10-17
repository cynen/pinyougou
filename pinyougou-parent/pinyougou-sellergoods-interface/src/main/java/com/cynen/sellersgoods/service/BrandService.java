package com.cynen.sellersgoods.service;

import java.util.List;

import com.cynen.pojo.TbBrand;

import entity.PageResult;
import entity.Result;

public interface BrandService {

	/**
	 * 查询全部品牌.
	 * @return
	 */
	public List<TbBrand> findAll();
	
	// 分页查询
	public PageResult findPage(int currPage,int pageSize);
	// 添加品牌
	public void addbrand(TbBrand tbBrand);
	
	public void updatebrand(TbBrand tbBrand);
	
	// 删除
	public void deleteBrand(Long[] ids);
	
	// 条件分页查询
	public PageResult findPage(TbBrand tbBrand,int currPage,int pageSize);
}
