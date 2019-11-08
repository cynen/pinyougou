app.controller('indexController',function($scope,loginService){
	
	// 控制器伪继承
	// $controller('baseController',{$scope:$scope});
	
	//获取登录名.
	$scope.getLoginName=function(){
		loginService.loginName().success(function(response){
			$scope.loginName=response.loginName;
		});	
	}
	
})