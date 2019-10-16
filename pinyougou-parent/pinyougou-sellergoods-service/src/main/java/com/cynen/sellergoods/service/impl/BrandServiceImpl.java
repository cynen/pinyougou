package com.cynen.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Service;
import com.cynen.mapper.TbBrandMapper;
import com.cynen.pojo.TbBrand;
import com.cynen.sellersgoods.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;
import entity.Result;

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


	/**
	 * 分页查询
	 */
	@Override
	public PageResult findPage(int currPage, int pageSize) {
		PageHelper.startPage(currPage, pageSize);
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 添加数据
	 */
	@Override
	public void addbrand(TbBrand tbBrand) {
		tbBrandMapper.insert(tbBrand);
	}

}
