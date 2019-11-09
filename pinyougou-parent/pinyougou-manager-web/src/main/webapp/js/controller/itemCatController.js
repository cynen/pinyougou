 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			$scope.entity.typeId=$scope.typeId.id;
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			// 保存当前的父节点.
			$scope.entity.parentId = $scope.parentId;
			$scope.entity.typeId=$scope.typeId.id;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	// $scope.reloadList();//重新加载
					$scope.findByParentId($scope.parentId);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					// $scope.reloadList();//刷新列表
					$scope.findByParentId($scope.parentId);
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			});
	}
	
	$scope.findByParentId=function(parentId){
		itemCatService.findByParentId(parentId).success(function(response){
			$scope.list=response;
		});
	}
	
	// 面包屑导航
	$scope.grade = 1;
	$scope.setGrade=function(value){
		$scope.grade = value;
	}
	// 点击对应的层级,返回.
	// entity   代表1级的面包屑  
	// entity_1 代表2级的面包屑. 
	// entity_2 代表3级的面包屑
	$scope.selectList=function(p_entity){
		if($scope.grade == 1){
			$scope.entity_1 =null;
			$scope.entity_2 =null;
		}else if($scope.grade == 2){ 
			//当点击了2级的时候,先将当前的实体赋值给2级的面包屑,用于存储分类名称.
			// 此时列表实际是展示的是p_entity所在层级的下一级.
			$scope.entity_1 =p_entity;
			$scope.entity_2 =null;
		}else{
			$scope.entity_2 =p_entity;
		}
		$scope.parentId = p_entity.id;
		$scope.findByParentId(p_entity.id);
	}
	// 默认父节点为0
	$scope.parentId = 0;
	$scope.entity={};
	
	// 类型列表查询.
	$scope.typeList={data:[]}
	//品牌列表查询
	$scope.findTypeList=function(){			
		typeTemplateService.findTypeList().success(
				function(response){
					$scope.typeList={data:response};
				}			
		);
	}
    
});	
