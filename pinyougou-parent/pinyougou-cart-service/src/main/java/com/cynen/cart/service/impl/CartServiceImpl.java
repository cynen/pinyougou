package com.cynen.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.cynen.cart.service.CartService;
import com.cynen.dto.Cart;
import com.cynen.mapper.TbItemMapper;
import com.cynen.pojo.TbItem;
import com.cynen.pojo.TbOrderItem;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private TbItemMapper tbItemMapper;
	/**
	 * itemId是添加到购物车中的商品SKUID,num是数量.
	 */
	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		//1.根据商品SKU ID查询SKU商品信息
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		// 容错判断.
		if (tbItem == null) {
			throw new RuntimeException("无此itemid的商品!");
		}
		if (!"1".equals(tbItem.getStatus())) {
			throw new RuntimeException("itemid为 "+itemId+"  的商品状态不正确!");
		}
		//2.获取商家ID		
		String sellerId = tbItem.getSellerId();
		//3.根据商家ID判断购物车列表中是否存在该商家的购物车	
		Cart cart = searchCartBySellerId(cartList,sellerId);
		// 判断cart对象是否为空
		if (cart == null) {//4.如果购物车列表中不存在该商家的购物车
			//4.1 新建购物车对象
			//4.2 将新建的购物车对象添加到购物车列表
			cart = new Cart();
			cart.setSellerId(sellerId);
			cart.setSellerName(tbItem.getSeller());
			// 构建购物车订单明细
			TbOrderItem orderItem = createOrderItem(tbItem, num);
			List<TbOrderItem> orderItems = new ArrayList<>();
			orderItems.add(orderItem);
			// 设置订单列表
			cart.setOrderItemList(orderItems);
			// 购物车添加对应商家购物对象.
			cartList.add(cart);
		}else {//5.如果购物车列表中存在该商家的购物车	
			// 查询购物车明细列表中是否存在该商品
			TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(),tbItem);
			if (orderItem == null) {
				//5.1. 如果没有，新增购物车明细	
				orderItem = createOrderItem(tbItem, num);
				cart.getOrderItemList().add(orderItem);
			}else {
				//5.2. 如果有，在原购物车明细上添加数量，更改金额
				orderItem.setNum(orderItem.getNum() + num);
				orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
				// 如果数量少于1,移除对应的明细.
				if (orderItem.getNum() < 1) {
					// 移除购物车明细.
					cart.getOrderItemList().remove(orderItem);
				}
				// 如果cart中的orderitem数量为0 ,也可以移除.
				if (cart.getOrderItemList().size() < 1) {
					cartList.remove(cart);
				}
			}
		}
		// 返回的是处理后的购物车列表.
		return cartList;
	}
	
	/**
	 * 从购物车中查询订单明细是否存在.
	 * @param orderItemList
	 * @param tbItem
	 * @return
	 */
	private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, TbItem tbItem) {
		for (TbOrderItem tbOrderItem : orderItemList) {
			if(tbOrderItem.getItemId().longValue() == tbItem.getId().longValue()) {
				return tbOrderItem;
			}
		}
		return null;
	}

	/**
	 * 根据sellerId查询购物车列表中是否有指定的商家.
	 * 有就返回当前商家的购物车对象.
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart searchCartBySellerId(List<Cart> cartList,String sellerId) {
		for (Cart cart : cartList) {
			if (sellerId.equals(cart.getSellerId())) {
				return cart;
			}
		}
		return null;
	}
	
	/**
	 * 构建购物车对象中的基本组成单元.
	 * @param item
	 * @param num
	 * @return
	 */
	private TbOrderItem createOrderItem(TbItem item,int num) {
		if(num<=0){
			throw new RuntimeException("数量非法");
		}
		TbOrderItem orderItem=new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setSellerId(item.getSellerId());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
		return orderItem;
	}
	

}
