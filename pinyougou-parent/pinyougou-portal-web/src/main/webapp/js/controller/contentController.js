app.controller('contentController',function($scope,contentService){
	
	$scope.contentList=[];//广告集合
	
	$scope.findCategoryById = function(id){
		contentService.findCategoryById(id).success(function(response){
			$scope.contentList[id] = response;
		});
	}
	$scope.keywords="";
	// 在首页的搜索框输入关键字后，点击搜索，跳转到搜索服务。
	$scope.search=function(){
		location.href="http://localhost:9104/search.html#?keywords=" + $scope.keywords;
	}
	
});