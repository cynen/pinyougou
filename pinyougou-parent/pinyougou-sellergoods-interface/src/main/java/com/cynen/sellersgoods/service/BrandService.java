package com.cynen.sellersgoods.service;

import java.util.List;

import com.cynen.pojo.TbBrand;

public interface BrandService {

	/**
	 * 查询全部品牌.
	 * @return
	 */
	public List<TbBrand> findAll();
	
}
