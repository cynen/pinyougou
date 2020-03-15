package com.cynen.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
		
		// 从cookie中查询.是否执行合并另说.
		String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		// 空指针判断.
		if (cartListString==null || "".equals(cartListString)) {
			cartListString = "[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		
		// 判断是否登录,没有登录的用户,用户名固定: anonymousUser 
		// 这个是SpringSecurity提供的.
		if("anonymousUser".equals(name)) {
			// 如果没有登录,就从cookie中取值.
			return cartList_cookie;	
		}else {
			// 如果登录了,就从redis中取值.
			List<Cart> cartList_redis = cartService.findCartListFromRedis(name);
			// 判断本地购物车是否为空
			if (cartList_cookie.size() > 0) {
				// 1.合并购物车
				cartList_redis = cartService.mergeCartList(cartList_cookie, cartList_redis);
				// 2.清空本地购物车.
				CookieUtil.deleteCookie(request, response, "cartList");
				// 3.将合并后的购物车存入Redis
				cartService.saveCartListToRedis(name, cartList_redis);
			}
			return cartList_redis;
		}
	}
	
	/**
	 * 向购物车列表中添加数据
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/addGoodsToCart")
	@CrossOrigin(origins="http://localhost:9105",allowCredentials="true")
	public Result addGoodsToCart(Long itemId,int num ) {
		// CROS跨域解决,以注解方式替代.[4.2之后的版本才支持]
		// response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
		// 使用cookie信息就需要添加此响应头.
		// 并且对应的CROS域不可以设置成 * 必须配置指定域.
		// response.setHeader("Access-Control-Allow-Credentials", "true");
		
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
