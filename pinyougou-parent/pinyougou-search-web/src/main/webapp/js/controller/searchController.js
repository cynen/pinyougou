app.controller('searchController',function($scope,searchService){
	
	$scope.searchMap = {};
	$scope.search = function(){
		searchService.itemsearch($scope.searchMap).success(function(response){
			$scope.resultMap = response;
		});
	}
	
});