app.service('searchService',function($http){
	
	//读取列表数据绑定到表单中
	this.itemsearch=function(searchMap){
		return $http.post('itemsearch/search.do',searchMap);		
	}
});