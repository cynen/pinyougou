app.controller('userController',function($scope,userService){
	
	// 在首页的搜索框输入关键字后，点击搜索，跳转到搜索服务。
	$scope.search=function(){
		location.href="http://localhost:9104/search.html#?keywords=" + $scope.keywords;
	}
	
	$scope.entity={};
	
	// 新增注册。
	$scope.useradd=function(){
		//2次密码校验。
		if($scope.entity.password != $scope.password){
			alert("两次密码不一致！");
			return;
		}
		if($scope.smscode == null ){
			alert("请输入短信验证码！");
			return;
		}
		userService.useradd($scope.entity,$scope.smscode).success(function(response){
			alert(response.msg);
		});
	}
	
	// 发送验证码。
	$scope.sendCode=function(){
		if($scope.entity.phone == null || $scope.entity.phone == ""){
			alert("请输入手机号！");
			return;
		}
		userService.sendCode($scope.entity.phone).success(function(response){			
				alert(response.msg)
		});
	}
	
});