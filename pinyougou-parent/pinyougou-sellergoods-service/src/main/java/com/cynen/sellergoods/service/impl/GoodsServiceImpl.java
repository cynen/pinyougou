package com.cynen.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cynen.dto.Goods;
import com.cynen.mapper.TbBrandMapper;
import com.cynen.mapper.TbGoodsDescMapper;
import com.cynen.mapper.TbGoodsMapper;
import com.cynen.mapper.TbItemCatMapper;
import com.cynen.mapper.TbItemMapper;
import com.cynen.mapper.TbSellerMapper;
import com.cynen.pojo.TbBrand;
import com.cynen.pojo.TbGoods;
import com.cynen.pojo.TbGoodsDesc;
import com.cynen.pojo.TbGoodsExample;
import com.cynen.pojo.TbGoodsExample.Criteria;
import com.cynen.pojo.TbItem;
import com.cynen.pojo.TbItemCat;
import com.cynen.pojo.TbItemExample;
import com.cynen.pojo.TbSeller;
import com.cynen.sellersgoods.service.GoodsService;

import entity.GlobalDict;
import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbBrandMapper tbBrandMapper;
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Autowired
	private TbSellerMapper tbSellerMapper;
	
	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		// 接受的是一个组合实体类
		// goods.getTbGoods().setAuditStatus("0");// 设置商品未审核.
		// 插入商品表
		goodsMapper.insert(goods.getTbGoods());		
		// 获取主键.
		goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());
		goodsDescMapper.insert(goods.getTbGoodsDesc());
		
		saveItem(goods,false);
	}
	
	private void saveItem(Goods goods,boolean isupdate) {
		if("1".equals(goods.getTbGoods().getIsEnableSpec())) {
			// 启用规格
			// 插入SKU信息.
			for(TbItem tbItem:goods.getItemList()) {
				
				// 1.标题: 名称+规格  例如: 手机 移动 4G 
				String title = goods.getTbGoods().getGoodsName();
				Map<String, Object> specMap = JSON.parseObject(tbItem.getSpec());
				for(String key: specMap.keySet()) {
					title += " " + specMap.get(key); 
				}
				tbItem.setTitle(title);
				setItemValue(goods,tbItem);
				tbItemMapper.insert(tbItem);
			}
		}else {
			// 不启用规格
			TbItem tbItem = new TbItem();
			tbItem.setTitle(goods.getTbGoods().getGoodsName());
			tbItem.setPrice(goods.getTbGoods().getPrice());
			tbItem.setStatus(GlobalDict.ITEM_ENABLE);
			tbItem.setIsDefault(GlobalDict.ITEM_DEFAULT_TRUE);
			tbItem.setNum(9999); // 默认库存量.
			tbItem.setSpec("{}");
			setItemValue(goods, tbItem);
			tbItemMapper.insert(tbItem);
		}
	}
	// 设置通用属性.
	private void setItemValue(Goods goods,TbItem tbItem) {
		
		// 2.图像.
		// tbItem.setImage(image);
		List<Map> imageList = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(), Map.class);
		if(imageList.size() > 0) {
			tbItem.setImage((String)imageList.get(0).get("url"));
		}
		
		//3.分类
		tbItem.setCategoryid(goods.getTbGoods().getCategory3Id());
		//4.是否启用.已绑定.
		//5.时间
		tbItem.setCreateTime(new Date());

		tbItem.setUpdateTime(new Date());
		// SPUid
		tbItem.setGoodsId(goods.getTbGoods().getId());
		tbItem.setSellerId(goods.getTbGoods().getSellerId());
		
		// 品牌名称
		TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
		tbItem.setBrand(tbBrand.getName());
		
		//分类名称
		TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(goods.getTbGoods().getCategory3Id());
		tbItem.setCategory(itemCat.getName());
		
		//商家名称
		TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(goods.getTbGoods().getSellerId());
		tbItem.setSeller(tbSeller.getNickName());
	}
	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		//1. 如果修改过,需要重新审核.
		goods.getTbGoods().setAuditStatus(GlobalDict.GOODS_UNCHECK);
		// 2. 更新tbgoods
		goodsMapper.updateByPrimaryKey(goods.getTbGoods());
		// 3. 更新tbgoodsDesc
		goodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());
		// 4.更新item
		// 删除原有的SKU列表数据.
		TbItemExample example = new TbItemExample();
		com.cynen.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getTbGoods().getId());
		tbItemMapper.deleteByExample(example);
		// 新增新的SKU列表数据.
		saveItem(goods,true);
	}	
	
	/**
	 * 根据ID获取组合实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		// 查询goods
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setTbGoods(tbGoods);
		// 查询描述
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setTbGoodsDesc(tbGoodsDesc);
		
		TbItemExample example = new TbItemExample();
		com.cynen.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		// 查询ItemList
		List<TbItem> list = tbItemMapper.selectByExample(example );
		goods.setItemList(list);
		
		return goods; 
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//仅做逻辑删除.
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsDelete(GlobalDict.GOODS_DELETE_TRUE);
			goodsMapper.updateByPrimaryKey(tbGoods);
			// goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		// criteria.andIsDeleteNotEqualTo("1"); // 当商品删除标志为 1 时,表示已删除.
		criteria.andIsDeleteIsNull(); // 初始时为空.
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				// criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId()); // 精确匹配.
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		// 更新商品审核状态.
		@Override
		public void updateStatus(Long[] ids, String status) {
			for(Long id : ids) {
				TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
				tbGoods.setAuditStatus(status);
				goodsMapper.updateByPrimaryKey(tbGoods);
			}
		}

		// 更新上下架信息.
		@Override
		public void updateMarketable(Long[] ids, String status) {
			for(Long id : ids) {
				TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
				tbGoods.setIsMarketable(status);
				goodsMapper.updateByPrimaryKey(tbGoods);
			}			
		}
	
}
