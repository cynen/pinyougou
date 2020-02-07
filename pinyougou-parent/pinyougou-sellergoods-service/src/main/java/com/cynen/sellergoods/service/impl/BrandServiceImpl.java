package com.cynen.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Service;
import com.cynen.mapper.TbBrandMapper;
import com.cynen.pojo.TbBrand;
import com.cynen.pojo.TbBrandExample;
import com.cynen.pojo.TbBrandExample.Criteria;
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
@Transactional
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
	public PageResult findPage(TbBrand tbBrand,int currPage, int pageSize) {
		PageHelper.startPage(currPage, pageSize);
		
		TbBrandExample example = new TbBrandExample();
		Criteria criteria = example.createCriteria();
		// 条件分页查询.
		if (tbBrand != null) {
			if (tbBrand.getName() != null && tbBrand.getName().length()>0) {
				criteria.andNameLike("%"+tbBrand.getName()+"%");
			}
			if (tbBrand.getFirstChar() !=null && tbBrand.getFirstChar().length() > 0) {
				criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
			}
		}

		Page<TbBrand> page =(Page<TbBrand>) tbBrandMapper.selectByExample(example);				
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 添加品牌
	 */
	@Override
	public void addbrand(TbBrand tbBrand) {
		tbBrandMapper.insert(tbBrand);
	}


	// 更新品牌信息
	@Override
	public void updatebrand(TbBrand tbBrand) {
		tbBrandMapper.updateByPrimaryKey(tbBrand);
	}

	
	/**
	 * 删除品牌,入参: 品牌的id数组.
	 */
	@Override
	public void deleteBrand(Long[] ids) {
		for (Long id : ids) {
			tbBrandMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 分页查询.
	 */
	@Override
	public PageResult findPage(int currPage, int pageSize) {
		PageHelper.startPage(currPage, pageSize);
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 根据主键查询一个
	 */
	@Override
	public TbBrand findOne(Long id) {
		// TODO Auto-generated method stub
		return tbBrandMapper.selectByPrimaryKey(id);
	}


	// 查询所有的品牌列表.
	@Override
	public List<Map> selectOptionList() {
		return tbBrandMapper.selectOptionList();
	}

}
