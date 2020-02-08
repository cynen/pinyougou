app.service('contentService',function($http){
	
	//读取列表数据绑定到表单中
	this.findCategoryById=function(categoryId){
		return $http.get('contentController/findByCategoryId.do?id='+categoryId);		
	}
});