package com.cynen.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cynen.pojo.TbSeller;
import com.cynen.sellersgoods.service.SellerService;

/**
 * 自定义实现的登录认证类.
 * @author myth
 *
 */

public class UserDetailsServiceImpl implements UserDetailsService{

	// 不使用dubbo的引用注解,采取较为原始的,xml注入方式.
	private SellerService sellerService;
	
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(username + ", 经过了自定义认证...");
		
		// SecurityContextHolder.getContext().getAuthentication();
		// 设定权限
		List<GrantedAuthority> grantAuths = new ArrayList<GrantedAuthority>();
		// 必须在SpringSecurity的配置文件中配置对应的用户ROLE.
		grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		
		//获取用户.
		TbSeller seller = sellerService.findOne(username);		
		// 商家必须存在.
		if (seller != null) {
			// 商家必须处于正常状态.才可以登录.
			if ("1".equals(seller.getStatus())) {
				// 密码不匹配,此处会返回null.
				return new User(username, seller.getPassword(), grantAuths);							
			}else {
				System.out.println(username + "商家状态不正确...");
				return null;
			}
		}else {
			System.out.println(username + "商家不存在...");
			return null;
		}
	}

}
