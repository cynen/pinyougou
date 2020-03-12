package com.cynen.page.service;

/**
 * 商品详细页接口
 * @author myth
 *
 */
public interface ItemPageService {
	/**
	 * 根据商品的SPUID生成item的详细页面。
	 * @param goodsId
	 * @return
	 */
	public boolean genItemHtml(Long goodsId);
	
	public boolean deletePage(Long[] ids);
}
