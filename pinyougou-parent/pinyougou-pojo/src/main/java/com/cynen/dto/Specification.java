package com.cynen.dto;

import java.io.Serializable;
import java.util.List;

import com.cynen.pojo.TbSpecification;
import com.cynen.pojo.TbSpecificationOption;

/**
 * 规格和规格选项的组合类
 * 自定义的dto类.
 * 
 * @author myth
 *
 */
public class Specification implements Serializable{
	
	private TbSpecification tbSpecification;
	
	private List<TbSpecificationOption> specificationOptionList;

	public TbSpecification getTbSpecification() {
		return tbSpecification;
	}

	public void setTbSpecification(TbSpecification tbSpecification) {
		this.tbSpecification = tbSpecification;
	}

	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}

	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}
	
	
	
}
