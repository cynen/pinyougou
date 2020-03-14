package com.cynen.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
		
		// 获取登录人信息
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人:" + name);
		// 判断是否登录,没有登录的用户,用户名固定: anonymousUser 
		// 这个是SpringSecurity提供的.
		if("anonymousUser".equals(name)) {
			// 如果没有登录,就从cookie中取值.
			// 从cookie中查询.
			String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
			// 空指针判断.
			if (cartListString==null || "".equals(cartListString)) {
				cartListString = "[]";
			}
			List<Cart> cartList = JSON.parseArray(cartListString, Cart.class);
			return cartList;	
		}else {
			// 如果登录了,就从redis中取值.
			List<Cart> list = cartService.findCartListFromRedis(name);
			return list;
		}
	}
	
	/**
	 * 向购物车列表中添加数据
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/addGoodsToCart")
	public Result addGoodsToCart(Long itemId,int num ) {
		// 获取登录人信息
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人:" + name);
		try {
			// 获取购物车列表.
			List<Cart> cartList = findCartList();
			// 添加都购物车中.
			cartList= cartService.addGoodsToCartList(cartList, itemId, num);
			// 这个是SpringSecurity提供的.
			if("anonymousUser".equals(name)) {
				String jsonString = JSON.toJSONString(cartList);
				// 保存到cookie中..
				CookieUtil.setCookie(request, response, "cartList", jsonString, 3600*24, "UTF-8");
			}else {
				// 已登录,就保存到redis中.
				cartService.saveCartListToRedis(name, cartList);
			}
			return new Result(true, "添加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败!");
		}
	}
	
}
