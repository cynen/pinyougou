package com.cynen.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cynen.content.service.ContentService;
import com.cynen.pojo.TbContent;

@RestController
@RequestMapping("/contentController")
public class ContentController {
	
	@Reference
	private ContentService contentService;
	
	/**
	 * 根据广告分类id,查询对应的广告.
	 * @param id
	 * @return
	 */
	@RequestMapping("/findByCategoryId")
	public List<TbContent> findByCategoryId(Long id){
		return contentService.findByCategoryId(id);
	}

}
