 //控制层 
app.controller('goodsController' ,function($scope,$location,$controller ,goodsService,loginService,itemCatService,typeTemplateService){	
	
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

	// ==========品牌列表和扩展属性 ===============
	// ========品牌选择,和具体的模板有关联关系,通过模板id查询对应的关联的品牌.========================
	// 监听模板id
	$scope.$watch('entity.tbGoods.typeTemplateId',function(newValue,oldValue){
		if(newValue == null){
			return ;
		}
			typeTemplateService.findOne(newValue).success(function(data){
				// 获取的是模板数据.
				// 品牌列表,获取到的是json字符串,需要进行解析成json对象.
				$scope.itemBrandList = JSON.parse(data.brandIds);
			});
	})
	
	
	// ==========规格型号待选列表.========================
		// 监听模板id
	$scope.$watch('entity.tbGoods.typeTemplateId',function(newValue,oldValue){
		if(newValue == null){
			return ;
		}
		// 根据模板id查询..
		typeTemplateService.findSpecList(newValue).success(function(response){
			$scope.specList = response;
		});
	})

	 
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
	
	// $scope.searchEntity={auditStatus:"0"};//定义搜索对象 
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
	
	
	// 更新商品的审核状态.
	
	$scope.updateStatus=function(status){
		goodsService.updateStatus($scope.selectIds,status).success(function(response){
			if(response.success){
				$scope.reloadList(); // 重新加载列表
				$scope.selectIds=[]; // 选项清空
			}else{
				alert(response.msg);
			}
		});
	}
	
});	
