package com.zte.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zte.dubbo.service.UserService;


/**
 * Dubbo���ṩ��.
 * @author myth
 *
 */

@Service
public class UserServiceImpl implements UserService {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Hello...Dubbo...";
	}

	
	
}
