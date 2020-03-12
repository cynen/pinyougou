package com.cynen.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cynen.cart.service.CartService;
import com.cynen.dto.Cart;
import com.cynen.utils.CookieUtil;

import entity.Result;


@RestController
@RequestMapping("/cart")
public class CartController {

	
	@Reference(timeout=40000)
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping("/findCartList")
	public List<Cart> findCartList() {
		// 从cookie中查询.
		String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		// 空指针判断.
		if (cartListString==null || "".equals(cartListString)) {
			cartListString = "[]";
		}
		
		List<Cart> cartList = JSON.parseArray(cartListString, Cart.class);
		return cartList;
	}
	
	/**
	 * 向购物车列表中添加数据
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/addGoodsToCart")
	public Result addGoodsToCart(Long itemId,int num ) {
		try {
			// 获取购物车列表.
			List<Cart> cartList = findCartList();
			// 添加都购物车中.
			cartList= cartService.addGoodsToCartList(cartList, itemId, num);
			
			String jsonString = JSON.toJSONString(cartList);
			// 保存到cookie中..
			CookieUtil.setCookie(request, response, "cartList", jsonString, 3600*24, "UTF-8");
			return new Result(true, "添加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败!");
		}
	}
	
}
