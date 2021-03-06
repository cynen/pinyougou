 //控制层 
app.controller('goodsController' ,function($scope,$controller ,$location,goodsService,loginService,uploadService,itemCatService,typeTemplateService){	
	
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
	
	//查询实体 ,返回的是一个组合实体.
	$scope.findOne=function(id){
		var id = $location.search()['id'];
		if(id == null){ 
			// 没有传输对应的goodsid,就表示不做反显.
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				// 1.组合实体.
				$scope.entity= response;
				// 2.商品描述,反显富文本.
				editor.html($scope.entity.tbGoodsDesc.introduction);
				// 3.商品图片,进行json转换即可.
				$scope.entity.tbGoodsDesc.itemImages = JSON.parse($scope.entity.tbGoodsDesc.itemImages);
				// 4.扩展属性反显.
				$scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
				// 5.规格型号列表.
				$scope.entity.tbGoodsDesc.specificationItems = JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
				// 6.SKU列表反显.
				for(var i=0;i<$scope.entity.itemList.length;i++){
					$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
				}
				
			}
		);				
	}
	
	// 判断CheckBox反显.返回true表示已选,false为未选.
	$scope.checkAttributeValue = function(specName,optionName){
		var items = $scope.entity.tbGoodsDesc.specificationItems;
		// 在列表中查询是否存在specName 的规格参数.
		var object = $scope.searchObjectByKey(items,'attributeName',specName);
		if(object == null){
			return false;
		}else{
			// 存在对应的规格型号,判断规格值是否已选.
			if(object.attributeValue.indexOf(optionName) >= 0){
				return true;
			}else{
				return false;
			}
		}
	}
		
	//保存 
	$scope.save=function(){		
		$scope.entity.tbGoodsDesc.introduction=editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.tbGoods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					// 新增成功,清空表单.
					alert("保存成功!");
					// $scope.entity = {}; // 新增成功后,清空entity实体.准备下一次新增.
					// editor.html("");// 清空editor
					location.href = "goods.html"; // 保存完就跳转到列表页面.
				}else{
					alert("保存失败,"+response.msg);
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
	$scope.status=['未审核','已审核','审核未通过','关闭'];//商品状态
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
	
	// =====================goods.html==================
	$scope.itemCatList=[];//商品分类列表
	// 查询所有分类名称,用于页面展示.
	$scope.findItemCatList = function(){
		itemCatService.findAll().success(function(response){
			// 返回的结果是 : List<TbItemCat>
			for(var i=0;i<response.length;i++){
				$scope.itemCatList[response[i].id]=response[i].name;
			}
		});
	}
	
	
	
	
	
	// =========goods_edit.html逻辑.============================
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
	$scope.entity = { tbGoods:{},tbGoodsDesc: {itemImages: [],specificationItems:[]}};
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
				if($location.search()['id'] == null){
					// 如果不是通过修改按钮进来,就进行数据初始化.否则会影响修改反显列表.
					$scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse(data.customAttributeItems);
				}
			});
	})
	
	
	// ==========规格型号待选列表.========================
		// 监听模板id
	$scope.$watch('entity.tbGoods.typeTemplateId',function(newValue,oldValue){
		// 根据模板id查询..
		typeTemplateService.findSpecList(newValue).success(function(response){
			$scope.specList = response;
		});
	})
	
	// == 动态生成选择的规格型号表
	// name是当前选择的规格名称,value是选择的值.
	$scope.updateSpecAttribute=function($event,name,value){
		// 查询当前规格名称是否存在于列表中.
		var object = $scope.searchObjectByKey($scope.entity.tbGoodsDesc.specificationItems,"attributeName",name);
		if(object !=null){
			// 规格名称不为空,表示之前已经添加过.
			//判断是移除还是添加
			if($event.target.checked){
				object.attributeValue.push(value);				
			}else{
				// 移除
				object.attributeValue.splice( object.attributeValue.indexOf(value ) ,1);//移除选项
				// 如果全部取消了,就清空该attributeValue
				if(object.attributeValue.length == 0){
					$scope.entity.tbGoodsDesc.specificationItems.splice($scope.entity.tbGoodsDesc.specificationItems.indexOf(object),1)
				}
			}
		}else{
			// 规格型号为空
			$scope.entity.tbGoodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
	}
	
	//=========== 创建SKU列表.
	$scope.createItemList=function(){
		// 初始化
		$scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' } ];
		var items = $scope.entity.tbGoodsDesc.specificationItems;
		// 遍历规格型号列表.添加表列
		for(var i=0;i<items.length;i++){
			$scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue)
		}
		
	}
	//给列表添加列.
	// 深克隆实现.
	addColumn=function(list,columnName,columnValue){
		var newList = []; //新集合.
		// 遍历旧集合
		for(var i=0;i<list.length;i++){
			var oldRow = list[i];
			// 遍历需要新增的列
			for(var j=0;j<columnValue.length;j++){
				var newRow = JSON.parse(JSON.stringify(oldRow));// 深克隆
				newRow.spec[columnName]=columnValue[j];
				newList.push(newRow);
			}
		}
		return newList;
	}
	
	
	//更新上下架信息
	$scope.updateMarketable = function(status){
		var list = $scope.list;
		var ids = $scope.selectIds;
		for(var i=0;i<list.length;i++){
			for(j = 0;j<ids.length;j++){
				if(list[i].id == ids[j] && list[i].auditStatus != "1"){
					alert("禁止上架未通过审核的商品!");
					return ;
				}
			}
		}
		goodsService.updateMarketable($scope.selectIds,status).success(function(response){
			if(response.success){
				alert("上架成功!");
				$scope.reloadList(); // 重新加载列表
				$scope.selectIds=[]; // 选项清空
			}else{
				alert(response.msg);
			}
		});
	}
	
});	
