app.controller('indexController',function($scope,loginService){
	
	// 获得登录用户名。
	
	$scope.showName=function(){
		loginService.loginName().success(function(response){
			$scope.loginName=response.loginName;
		});
	}
	
});