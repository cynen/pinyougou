package com.cynen.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cynen.dto.Specification;
import com.cynen.mapper.TbSpecificationMapper;
import com.cynen.mapper.TbSpecificationOptionMapper;
import com.cynen.pojo.TbSpecification;
import com.cynen.pojo.TbSpecificationExample;
import com.cynen.pojo.TbSpecificationExample.Criteria;
import com.cynen.pojo.TbSpecificationOption;
import com.cynen.pojo.TbSpecificationOptionExample;
import com.cynen.sellersgoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	
	@Autowired
	private TbSpecificationOptionMapper specificationOptinMapper;
	
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		// 需要做2个表的插入.
		//插入规格表.
		specificationMapper.insert(specification.getTbSpecification());
		// 循环插入规格选项.
		for (TbSpecificationOption specificationOption : specification.getSpecificationOptionList()) {
			// 获取主键.
			specificationOption.setSpecId(specification.getTbSpecification().getId());
			specificationOptinMapper.insert(specificationOption);
			
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		// 更新.
		TbSpecification tbSpecification = specification.getTbSpecification();
		specificationMapper.updateByPrimaryKey(tbSpecification);
		// 删除原有的规格型号.
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.cynen.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(tbSpecification.getId());
		specificationOptinMapper.deleteByExample(example);
		
		// 更新规格选项.
		List<TbSpecificationOption> optionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption tbSpecificationOption : optionList) {
			tbSpecificationOption.setSpecId(tbSpecification.getId());
			specificationOptinMapper.insert(tbSpecificationOption);
			// specificationOptinMapper.updateByPrimaryKey(tbSpecificationOption);
			// specificationMapper.updateByPrimaryKey(specification);			
		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		// 查询组合类.
		TbSpecification tbSpecification =specificationMapper.selectByPrimaryKey(id);
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.cynen.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(tbSpecification.getId());
		List<TbSpecificationOption> list = specificationOptinMapper.selectByExample(example);
		// 拼接组合类.
		Specification specification = new Specification();
		specification.setSpecificationOptionList(list);
		specification.setTbSpecification(tbSpecification);
		
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		// 删除不仅仅删除规格,还需要删除规格选项.
		for(Long id:ids){
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			com.cynen.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);
			
			// 删除规格选项.
			specificationOptinMapper.deleteByExample(example);
			// 删除具体的规格.
			specificationMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public List<Map> selectOptionList() {
			return specificationMapper.selectOptionList();
		}
	
}
