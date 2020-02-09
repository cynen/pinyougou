package com.cynen.content.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.cynen.content.service.ContentService;
import com.cynen.mapper.TbContentMapper;
import com.cynen.pojo.TbContent;
import com.cynen.pojo.TbContentExample;
import com.cynen.pojo.TbContentExample.Criteria;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		// 先获取id
		contentMapper.insert(content);	
		// 将当前content对象存入缓存,查询的时候再放入到缓存.
		// redisTemplate.boundHashOps("content").put(content.getId(), content);
		// 更新缓存中的广告列表,全部清空
		redisTemplate.delete("content_category");
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		// 删除之前的缓存
		redisTemplate.boundHashOps("content").delete(content.getId());
		contentMapper.updateByPrimaryKey(content);
		// 重新放入缓存.update不建议重新放入缓存.
		// redisTemplate.boundHashOps("content").put(content.getId(),content );
		// 更新缓存中的广告列表,清空指定广告类型列表
		redisTemplate.boundHashOps("content_category").delete(content.getCategoryId());
		
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		// 先去缓存中查询指定的数据是否存在.
		TbContent content = (TbContent) redisTemplate.boundHashOps("content").get(id);
		if (content == null) {
			// 不存在的时候,从数据库中查询,然后按照自定义规格插入到缓存中.
			System.out.println("content没有缓存,从数据库查询: " + id);
			content = contentMapper.selectByPrimaryKey(id);
			redisTemplate.boundHashOps("content").put(id, content);
		}
		return content;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			contentMapper.deleteByPrimaryKey(id);
			redisTemplate.boundHashOps("content").delete(id);
		}		
	}
	
	@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		/**
		 * 根据广告分类id,查询对应的广告.
		 */
		@Override
		public List<TbContent> findByCategoryId(Long id) {
			// 先从缓存中查,缓存中没有,再查询数据库.
			
			List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content_category").get(id);
			if (contentList == null) {
				System.out.println("广告类型"+id+"的广告列表查询  -- 查数据库,并存入缓存中...");
				
				TbContentExample example =new TbContentExample();
				Criteria criteria = example.createCriteria();
				criteria.andCategoryIdEqualTo(id); // 分类id查询
				criteria.andStatusEqualTo("1"); // 启用状态.
				example.setOrderByClause("sort_order"); // 排序
				contentList = contentMapper.selectByExample(example );
				// 将查询的结果,保存到缓存中.
				redisTemplate.boundHashOps("content_category").put(id, contentList);
			}else {
				System.out.println("广告类型"+id+"的广告列表查询  -- 查缓存 !");
			}
			return contentList;
		}
	
}
