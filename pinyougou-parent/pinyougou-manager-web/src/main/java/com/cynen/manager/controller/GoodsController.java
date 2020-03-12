package com.cynen.manager.controller;
import java.util.Arrays;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cynen.dto.Goods;
import com.cynen.page.service.ItemPageService;
import com.cynen.pojo.TbGoods;
import com.cynen.pojo.TbItem;
import com.cynen.sellersgoods.service.GoodsService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	// @Reference(timeout=10000)
	// private ItemSearchService itemSearchService;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination queueSolrDestination;
	
	@Autowired
	private Destination queueSolrDeleteDestination;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	

	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);
			// System.out.println("准备删除索引库中指定索引的数据。。。。");
			// itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
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
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids,String status  ){
		try {
			goodsService.updateStatus(ids, status);
			
			// 如果是审核通过，就需要更新索引库。
			if ("1".equals(status)) {
				System.out.println("1-------商品审核通过，准备更新索引库。。。。");
				// 1.查询列表。
				List<TbItem> list = goodsService.findItemListByGoodsIdsAndStatus(ids, status);
				if (list.size() > 0) {
					// 2. 保存列表
					// System.out.println("2--------准备更新索引库。。。。");
					// itemSearchService.importList(list);
					final String itemlist = JSON.toJSONString(list);
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							System.out.println("发送itemlist到 QUEUE ： queueSolrDestination 中,数据位: "+itemlist );
							return session.createTextMessage(itemlist);
						}
					});
					
					// 3.调用页面生成服务,生成静态页面.
					for (Long id:ids) {
						System.out.println("生成商品详情页..." + id);
						itemPageService.genItemHtml(id);
					}
					
				}else {
					System.out.println("没有审核通过的明细SKU！");
				}
				
			}
			return new Result(true, "操作成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result(false, "操作失败!");
		}		
	}
	
	@Reference(timeout=40000)
	private ItemPageService itemPageService;
	
	/**
	 * 留作备用调试接口.
	 * @param goodsId
	 */
	@RequestMapping("/genHtml")
	public void gen(Long goodsId) {
		itemPageService.genItemHtml(goodsId);
	}
	
}
