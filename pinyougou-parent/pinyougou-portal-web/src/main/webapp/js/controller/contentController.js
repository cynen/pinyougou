app.controller('contentController',function($scope,contentService){
	
	$scope.contentList=[];//广告集合
	
	$scope.findCategoryById = function(id){
		contentService.findCategoryById(id).success(function(response){
			$scope.contentList[id] = response;
		});
	}
	
});