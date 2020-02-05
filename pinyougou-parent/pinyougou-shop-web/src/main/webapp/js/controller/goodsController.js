 //控制层 
app.controller('goodsController' ,function($scope,$controller ,goodsService,loginService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	
	//新增商品
	$scope.addGoods=function(){
		// 获取富文本内容.
		$scope.entity.tbGoodsDesc.introduction=editor.html();
		goodsService.add( $scope.entity ).success(
			function(response){
				if(response.success){
					// 新增成功,清空表单.
					alert("添加成功!");
					$scope.entity = {}; // 新增成功后,清空entity实体.准备下一次新增.
					editor.html("");// 清空editor
				}else{
					alert("添加失败,"+response.msg);
				}
			}		
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//获取登录名.
	$scope.getLoginName=function(){
		loginService.loginName().success(function(response){
			$scope.loginName=response.loginName;
		});	
	}
    
	$scope.initSearch=function(){
		loginService.loginName().success(function(response){
			// response.loginName;
			$scope.searchEntity.sellerId = response.loginName;
			$scope.reloadList();
		});	
	}
	
	// ====================商品图片=====================================
	// 商品图片文件上传.
	$scope.uploadFile=function(){
		uploadService.uploadFile().success(function(response){
			if(response.success){
				// 上传成功.
				$scope.image_entity.url =  response.msg;
			}else{
				alert("上传异常:" + response.msg);
			}
		}).error(function(){
			alert("上传出错!");
		});
	}
	
	//商品图片列表添加图片.
	// 向列表entity.tbGoodsDesc.itemImages中push元素.
	$scope.entity = { tbGoods:{},tbGoodsDesc: {itemImages: []}};
	$scope.image_list_add=function(){
		$scope.entity.tbGoodsDesc.itemImages.push( $scope.image_entity );
	}
	// 从商品图片列表中删除图片元素.
	$scope.image_list_delete=function(index){
		$scope.entity.tbGoodsDesc.itemImages.splice( index,1 );
	}
	
	// ========商品分类,逐级选择.==================================================
	// 1级菜单,初始化查询.
	$scope.selectItemCat1List=function(){
		itemCatService.findByParentId(0).success(function(response){
			$scope.itemCat1List = response;
		});
	}
	// 2级菜单,监听1级菜单的model变量.
	$scope.$watch('entity.tbGoods.category1Id',function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(function(response){
			$scope.itemCat2List = response;
		});
	})
	// 3级菜单,监听1级菜单的model变量.
	$scope.$watch('entity.tbGoods.category2Id',function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(function(response){
			$scope.itemCat3List = response;
		});
	})
	//	模板id的查询.监听变量entity.tbGoods.category3Id.
		$scope.$watch('entity.tbGoods.category3Id',function(newValue,oldValue){
		itemCatService.findOne(newValue).success(function(response){
			// 变更模板id
			$scope.entity.tbGoods.typeTemplateId = response.typeId;
			/*挪到下面也是可行的.
			 * // ========品牌选择,和具体的模板有关联关系,通过模板id查询对应的关联的品牌.========================
			typeTemplateService.findOne(response.typeId).success(function(data){
				// 品牌列表
				$scope.itemBrandList = JSON.parse(data.brandIds);
				// 扩展属性列表.
				$scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse(data.customAttributeItems);
				
			});*/
		});
	})
	
	// ==========品牌列表和扩展属性 ===============
	// ========品牌选择,和具体的模板有关联关系,通过模板id查询对应的关联的品牌.========================
	// 监听模板id
	$scope.$watch('entity.tbGoods.typeTemplateId',function(newValue,oldValue){
			typeTemplateService.findOne(newValue).success(function(data){
				// 获取的是模板数据.
				// 品牌列表,获取到的是json字符串,需要进行解析成json对象.
				$scope.itemBrandList = JSON.parse(data.brandIds);
				// 扩展属性列表.
				$scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse(data.customAttributeItems);
			});
	})
	
});	
