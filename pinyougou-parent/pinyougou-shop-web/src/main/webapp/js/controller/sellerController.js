 //控制层 
app.controller('sellerController' ,function($scope,$controller,sellerService,loginService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		sellerService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		sellerService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		sellerService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=sellerService.update( $scope.entity ); //修改  
		}else{
			serviceObject=sellerService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	//注册
	$scope.add=function(){				
		serviceObject=sellerService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
					//跳到登录页.
					location.href="shoplogin.html"
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		sellerService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		sellerService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	
	$scope.updatepwd=function(){
		if($scope.entity.pwd1 != $scope.entity.pwd2){
			alert("2次密码不一致!");
		}
		sellerService.updatePwd($scope.entity.oldPwd,$scope.entity.pwd1).success(function(response){
			if(response.success){
				alert("修改密码成功!,密码修改为: " + $scope.entity.pwd1 );
				location.href="index.html";
			}else{
				// alert("修改密码失败!");
				alert(response.msg)
			}
		});
	}
	
	//获取登录名.
	$scope.getLoginName=function(){
		loginService.loginName().success(function(response){
			$scope.loginName=response.loginName;
			$scope.loginDate=response.loginDate;
		});	
	}
	// 初始化商家
	$scope.sellerinit=function(){
		// 首先获取登录用户,在通过登录用户的id去查询详细信息.
		loginService.loginName().success(function(response){
			$scope.findOne(response.loginName);
		});	
	}
	
});	
