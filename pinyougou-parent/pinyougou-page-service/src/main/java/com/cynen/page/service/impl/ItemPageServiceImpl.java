package com.cynen.page.service.impl;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.cynen.mapper.TbGoodsDescMapper;
import com.cynen.mapper.TbGoodsMapper;
import com.cynen.mapper.TbItemCatMapper;
import com.cynen.mapper.TbItemMapper;
import com.cynen.page.service.ItemPageService;
import com.cynen.pojo.TbGoods;
import com.cynen.pojo.TbGoodsDesc;
import com.cynen.pojo.TbItem;
import com.cynen.pojo.TbItemExample;
import com.cynen.pojo.TbItemExample.Criteria;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageService{

	@Autowired
	private FreeMarkerConfigurer freemarkerConfig; 
	
	@Value("${pageDir}")
	private String pagedir;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper descMapper;
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Autowired
	private TbItemMapper tbitemMapper;
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
			// 1.加载商品表数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			// 2.加载扩展表数据
			TbGoodsDesc goodsDesc = descMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);
			
			// 3.商品分类.
			String itemCat1 = tbItemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat2 = tbItemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat3 = tbItemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat1", itemCat1);
			dataModel.put("itemCat2", itemCat2);
			dataModel.put("itemCat3", itemCat3);
			
			//4.读取SKU列表.
			TbItemExample example = new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andGoodsIdEqualTo(goodsId);
			criteria.andStatusEqualTo("1");
			example.setOrderByClause("is_default desc");
			
			List<TbItem> itemList = tbitemMapper.selectByExample(example);
			dataModel.put("itemList", itemList);
			
			
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
