app.controller('brandController',function($scope,$controller,brandService){
	
	// 控制器伪继承
	$controller('baseController',{$scope:$scope});
	
	// 列表查询全部.
	// $scope.findAll=function(){
		// $http.get("../brand/findAll.do").success(function(response){
			// $scope.list=response;
		// });
	// }
	$scope.findAll=function(){
		brandService.findAll().success(function(response){
			$scope.list=response;
		})
	}
	
	// 分页查询.
	$scope.findPage=function(currPage,pageSize){
		/* $http.post("../brand/findPage.do?currPage="+currPage+"&pageSize="+pageSize).success(function(response){
			$scope.list=response.rows;
			$scope.paginationConf.totalItems=response.total;
		}); */
		brandService.findPage(currPage,pageSize).success(function(response){
			$scope.list=response.rows;
			$scope.paginationConf.totalItems=response.total;
		});
	}
	
	
	// 增加品牌.
	$scope.save=function(){
		// 判断是 添加还是修改.通过entity的id属性来判断.
		// 还有一种办法: 在后端判断是否存在id,进行判断.选择不同的执行方法.
		var object=null;
		if($scope.entity.id != null){
			object = brandService.update($scope.entity); //执行新增.
		}else{
			object = brandService.add($scope.entity);
		}
		
		// $http.post("../brand/"+method+".do",$scope.entity).success(function(response){
		object.success(function(response){
			if(response.success){
				// 添加成功,就刷新列表
				$scope.reloadList();
			}else{
				// 失败就报错...
				alert(response.msg);
			}
		});	
	}
	
	// 修改品牌.
	$scope.edit=function(tbbrand){
		// 传递当前点中的品牌,将对象传递进来.
		// 赋给 $scope域中的entity对象.
		$scope.entity=tbbrand;// 用于反显.
		
		// 方法二: 通过点击修改按钮,传递当前的id过来.
		//$scope.entity.id=tbbrand.id
		//$scope.entity.name=tbbrand.name; 
		//$scope.entity.firstChar=tbbrand.firstChar; 			
	}
	
	// 删除选中的ids
	$scope.delte=function(){
		// $http.get("../brand/delte.do?ids="+$scope.selectIds).success(function(response){
		brandService.delte($scope.selectIds).success(function(response){
			if(response.success){
				// 删除成功,刷新列表
				$scope.reloadList();
			}else{
				// 删除失败就报错...
				alert(response.msg);
			}
		});
	}
	
	
	$scope.searchEntity={}; // 必须提供一个默认值,否则,POST提交会报错.
	// 关键字查询.
	$scope.search=function(currPage,pageSize){
		// $http.post("../brand/search.do?currPage="+currPage+"&pageSize="+pageSize,$scope.searchEntity).success(function(response){
		brandService.search(currPage,pageSize,$scope.searchEntity).success(function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;	
		});
	}
})