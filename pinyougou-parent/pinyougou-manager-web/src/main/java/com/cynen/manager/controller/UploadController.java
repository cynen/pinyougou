package com.cynen.manager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cynen.utils.FastDFSClient;

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
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
		try {
			// 2.创建一个FastClient客户端.
			FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
			// 3.上传文件,返回文件的id
			String fileId = client.uploadFile(file.getBytes(), extName);
			
			//4.获得图片的全路径. 
			String url = FILE_SERVER_URL + fileId;
			
			return new Result(true, url);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "上传失败!");
		}
	}
	
}
