package com.cynen.dto;

import java.io.Serializable;
import java.util.List;

import com.cynen.pojo.TbGoods;
import com.cynen.pojo.TbGoodsDesc;
import com.cynen.pojo.TbItem;

/**
 * 
 * @author myth 组合商品实体类. 包含 SPU goods表 goodsspecial表 item表
 *
 */
public class Goods implements Serializable {

	private TbGoods tbGoods; // 商品表
	private TbGoodsDesc tbGoodsDesc; // 商品扩展表
	private List<TbItem> itemList; // item集合.SKU
	public TbGoods getTbGoods() {
		return tbGoods;
	}
	public void setTbGoods(TbGoods tbGoods) {
		this.tbGoods = tbGoods;
	}
	public TbGoodsDesc getTbGoodsDesc() {
		return tbGoodsDesc;
	}
	public void setTbGoodsDesc(TbGoodsDesc tbGoodsDesc) {
		this.tbGoodsDesc = tbGoodsDesc;
	}
	public List<TbItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}

	
	
}