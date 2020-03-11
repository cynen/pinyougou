package com.cynen.page.service.impl;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.dubbo.config.annotation.Service;
import com.cynen.mapper.TbGoodsDescMapper;
import com.cynen.mapper.TbGoodsMapper;
import com.cynen.page.service.ItemPageService;
import com.cynen.pojo.TbGoods;
import com.cynen.pojo.TbGoodsDesc;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
@Transactional
public class ItemPageServiceImpl implements ItemPageService{

	@Autowired
	private FreeMarkerConfigurer freemarkerConfig; 
	
	@Value("${pageDir}")
	private String pagedir;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper descMapper;
	
	
	/**
	 * 生成商品详情页
	 */
	@Override
	public boolean genItemHtml(Long goodsId) {
		
		Configuration configuration = freemarkerConfig.getConfiguration();
		try {
			// 获得模板文件
			Template template = configuration.getTemplate("item.ftl");
			// 构建数据模型
			Map dataModel = new HashMap();
			// 查询数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			
			TbGoodsDesc goodsDesc = descMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);
			
			// 设置输出文件名称
			Writer out = new FileWriter(pagedir+goodsId+".html");
			// 执行模板引擎。
			template.process(dataModel, out);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
