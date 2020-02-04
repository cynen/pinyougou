//服务层
app.service('uploadService',function($http){
	    	
	//上传文件.
	this.uploadFile=function(){
		// h5新增的文件上传组件.
		var formData=new FormData();
		// 需要和页面的上传文件的id匹配.
		formData.append("file",file.files[0]);
		
		return $http({
			url: "../upload.do",
			method: "POST",
			data: formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
		});		
	}
  	
});
