	// 品牌服务.
	app.service('brandService',function($http){
		this.findAll=function(){
			return $http.get("../brand/findAll.do");
		}
		
		this.findPage=function(currPage,pageSize){
			return $http.post("../brand/findPage.do?currPage="+currPage+"&pageSize="+pageSize);
		}
		
		this.delte=function(selectIds){
			return $http.get("../brand/delte.do?ids="+selectIds);
		}
		
		this.search = function(currPage,pageSize,searchEntity){
			return $http.post("../brand/search.do?currPage="+currPage+"&pageSize="+pageSize,searchEntity);
		}
		
		this.add=function(entity){
			return $http.post("../brand/add.do",entity);
		}
		this.update=function(entity){
			return $http.post("../brand/update.do",entity);
		}
		
		this.findOne=function(id){
			return $http.get("../brand/findOne.do?id="+id);
		}
	})