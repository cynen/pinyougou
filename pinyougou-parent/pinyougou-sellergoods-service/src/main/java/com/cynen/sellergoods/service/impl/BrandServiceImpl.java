package com.cynen.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.cynen.mapper.TbBrandMapper;
import com.cynen.pojo.TbBrand;
import com.cynen.sellersgoods.service.BrandService;

/**
 * 引入dubbo的依赖. interface
 * @author myth
 *
 */

@Service
public class BrandServiceImpl implements BrandService{

	
	@Autowired
	private TbBrandMapper tbBrandMapper;
	
	
	@Override
	public List<TbBrand> findAll() {
		System.out.println("pinyougou-sellergoods-service 服务被调用了  BrandService.findAll 方法.... ");
		return tbBrandMapper.selectByExample(null);
	}

}
