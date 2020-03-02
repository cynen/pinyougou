app.service('userService',function($http){
	

	// 注册用户，新增
	this.useradd=function(entity,smscode){
		return $http.post("../user/add.do?smscode="+smscode,entity);
	}
	
	// 发送验证码。
	this.sendCode=function(phone){
		return $http.get("../user/sendCode.do?phone="+phone);
	}
	
});