package com.cynen.shop.controller;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cynen.pojo.TbSeller;
import com.cynen.sellersgoods.service.SellerService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeller> findAll(){			
		return sellerService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return sellerService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeller seller){
		// 对密码进行Bcrypt加密.
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = encoder.encode(seller.getPassword());
		seller.setPassword(password);
		try {
			sellerService.add(seller);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbSeller findOne(String id){
		return sellerService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(String [] ids){
		try {
			sellerService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeller seller, int page, int rows  ){
		return sellerService.findPage(seller, page, rows);		
	}
	
	
	/**
	 * Bcrypt 加密,好像修改密码不能这么使用....
	 * 如何判断输入的密码是旧密码?
	 * @param oldpwd
	 * @param pwd
	 * @return
	 */

	@RequestMapping("/updatepwd")
	public Result updatepwd(String oldpwd, String pwd){
		System.out.println("旧密码:" + oldpwd + ",新密码" + pwd);
		// 获取当前登录用户id
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前准备修改的用户 : "+name);
		// 获取原密码
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		TbSeller seller = sellerService.findOne(name);
		String originpwd = seller.getPassword();
		// 判断旧密码是否匹配.
		if(encoder.matches(oldpwd,originpwd)) {
			// 只有原密码正确,才更新密码
			System.out.println("旧密码匹配...");
			seller.setPassword(encoder.encode(pwd));
			try {
				sellerService.update(seller);
				return new Result(true, "更新成功!");
			} catch (Exception e) {
				// TODO: handle exception
				return new Result(true, "更新失败!" + e.getLocalizedMessage());
			}
			
		}else {
			return new Result(false, "原密码不正确!");
		}
	}
	
}
