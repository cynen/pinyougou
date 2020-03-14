package com.cynen.cart.service;

import java.util.List;

import com.cynen.dto.Cart;

public interface CartService {
	
	
	/**
	 * 添加商品到购物车
	 * @param cartList
	 * @param itemId
	 * @param num
	 * @return
	 */
	public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);
	
	/**
	 * 从redis中读取指定用户的购物车
	 * @param username
	 * @return
	 */
	public List<Cart> findCartListFromRedis(String username);
	/**
	 * 将当前用户的购物车,保存到Redis中.
	 * @param username
	 * @param cartList
	 */
	public void saveCartListToRedis(String username,List<Cart> cartList);
}
