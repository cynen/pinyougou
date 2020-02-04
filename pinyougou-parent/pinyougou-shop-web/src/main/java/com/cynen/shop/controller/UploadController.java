package com.cynen.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;

/**
 * 上传文件后,给前端回传对应图片的访问路径.
 * @author myth
 *
 */


@RestController
public class UploadController {

	//获得配置文件中的fileServer地址.
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL; //文件服务器地址
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file) {
		
		//1.获取上传文件的扩展名.
		String originalFilename = file.getOriginalFilename();

		
	}
	
}
