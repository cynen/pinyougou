app.controller('searchController',function($scope,$location,searchService){
	
	$scope.searchMap = {'keywords':'','category':'','brand':'','spec':{},'pageNo':1,'pageSize':20};
	$scope.search = function(){
		searchService.itemsearch($scope.searchMap).success(function(response){
			$scope.resultMap = response;
		});
	}
	
	$scope.addSearchItem=function(key,value){
		if(key == 'category' || key == 'brand'){
			// 如果点击的是分类或者品牌，就给searchMap添加参数
			$scope.searchMap[key]=value;
		}else{
			// 否则就添加规格型号参数。
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();
	}
	// 移除查询的条件参数。
	$scope.reomveSearchItem=function(key){
		if(key == 'category' || key == 'brand'){
			$scope.searchMap[key]="";
		}else{
			delete $scope.searchMap.spec[key]; // 移除属性
		}
		$scope.search();
	}
	
	//分页查询
	$scope.queryByPage=function(pageNo){
		if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
			return ;
		}		
		$scope.searchMap.pageNo=pageNo;
		$scope.search();//查询
	}
	
	
	//判断当前页是否为第一页
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}		
	}
	
	//判断当前页是否为最后一页
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}	
	}
	
	
	
	// 接受首页转交过来的关键字，并执行插叙。
	$scope.loadkeywords = function(){
		$scope.searchMap.keywords = $location.search()['keywords'];
		$scope.search();
	}
});