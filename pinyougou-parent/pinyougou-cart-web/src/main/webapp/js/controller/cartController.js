app.controller('cartController',function($scope,cartService){
	
	
	// 获得列表
	$scope.findCartList=function(){
		cartService.findCartList().success(function(response){
			$scope.cartList=response;
			$scope.totalValue = cartService.sum(response); // 求合计数.
		});
	}
	
	// 获得列表
	$scope.addGoodsToCart=function(itemId,num){
		cartService.addGoodsToCart(itemId,num).success(function(response){
			if(response.success){
				//添加成功
				$scope.findCartList();
			}else{
				// 添加失败.
				alert(response.msg)
			}
		});
	}
	
	
});