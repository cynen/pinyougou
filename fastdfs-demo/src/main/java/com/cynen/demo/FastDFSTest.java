package com.cynen.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FastDFSTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String filename = ClassLoader.getSystemResource("637.jpg").getFile();
		System.out.println(filename);
		// 文件上传.
		// FastUtils.uploadFile(new File(filename));
		// 下载文件,输出到指定路径.
		byte[] bs = FastUtils.downloadFile("group1", "M00/00/00/rBJ4Cl3T902Aes5QAAK3h53ub_A69..jpg");
		// 输出到该文件对象中.
		File file = new File("D://123.jpg");
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(bs);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
