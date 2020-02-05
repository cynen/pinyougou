app.controller('baseController',function($scope){

	//分页控件配置 
	$scope.paginationConf = {
			 currentPage: 1,
			 totalItems: 10,
			 itemsPerPage: 10,
			 perPageOptions: [10, 20, 30, 40, 50],
			 onChange: function(){
			     $scope.reloadList();//重新加载
			 }
	}; 
	// 公共的加载数据.
	$scope.reloadList=function(){
		 $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	}
	
	// 对选中的所有的id进行保存的数组.
	$scope.selectIds=[];
	// 列表的check框选中事件.
	$scope.updateSelection=function($event,id){
		if($event.currentTarget.checked){
		// 点击是选中
			$scope.selectIds.push(id)
		}else{
		// 点击是不选中.
			var index=$scope.selectIds.indexOf(id);
			$scope.selectIds.splice(index,1);
		}
	}
	
	
	// 抽取一个公共的方法.
	// 提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
	$scope.jsonToString=function(jsonString,key){
		var json = JSON.parse(jsonString);
		var result = "";
		for(var i=0;i < json.length;i++){
			// 从第二个数据开始,添加,隔开.
			if(i > 0){
				result+=",";
			}
			result+=json[i][key];
		}
		return result;
	}
	
})