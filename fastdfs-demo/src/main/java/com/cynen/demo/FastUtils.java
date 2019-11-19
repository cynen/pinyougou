package com.cynen.demo;

import java.io.File;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FastUtils {
	public static String CONF_FILENAME = FastUtils.class.getResource("/fastdfs.conf").getFile();
	public static void uploadFile(File file) {
		try {
			// 全局初始化
			System.out.println(CONF_FILENAME);
			ClientGlobal.init(CONF_FILENAME);
			// 创建Client
			TrackerClient trackerClient = new TrackerClient();
			// 通过Client获得Server
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			// 构建Storage客户端.
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			// 获得上传文件的文件名
			String fileName = file.getName();
			// 获得上传文件的后缀[文件格式]
			String file_ext_name  = fileName.substring(fileName.lastIndexOf("."));
			System.out.println("文件的绝对路径: "+ file.getAbsolutePath());
			// 执行上传动作.
			String[] fileStrings = storageClient.upload_file(file.getAbsolutePath(), file_ext_name, null);
			for (String string : fileStrings) {
				System.out.println(string); 
			}
			// 输出: group1 
			// 输出: M00/00/00/rBAIM13Tg5iAXYiaAAG-d-3QfL44..jpeg
		} catch (Exception e) {
			
		}
	}
	
	// 下载文件,实际是获得文件的字节数组.
	public static byte[] downloadFile(String groupName,String fileName) {
		try {
			// 全局初始化
			ClientGlobal.init(CONF_FILENAME);
			// 创建Client
			TrackerClient trackerClient = new TrackerClient();
			// 通过Client获得Server
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			// 构建Storage客户端.
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			byte[] bs = storageClient.download_file(groupName, fileName);
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
