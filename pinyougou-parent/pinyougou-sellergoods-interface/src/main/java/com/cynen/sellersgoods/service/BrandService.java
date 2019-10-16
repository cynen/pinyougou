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
	
	public PageResult findPage(int currPage,int pageSize);
	
	public void addbrand(TbBrand tbBrand);
	
}
