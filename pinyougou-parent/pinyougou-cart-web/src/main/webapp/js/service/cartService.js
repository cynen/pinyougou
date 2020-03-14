app.service('cartService',function($http){
	

	// 查询列表.
	this.findCartList=function(){
		return $http.get("../cart/findCartList.do");
	}
	
	// 添加购物车.
	this.addGoodsToCart=function(itemId,num){
		return $http.get("../cart/addGoodsToCart.do?itemId="+itemId+"&num="+num);
	}
	
	// 求合计数和总金额.
	this.sum=function(cartList){
		var totalValue={totalNum:0,totalMoney:0}
		// 遍历购物车
		for(var i=0;i<cartList.length;i++){
			// 遍历购物车对象
			for(var j=0;j<cartList[i].orderItemList.length;j++){
				totalValue.totalNum+=cartList[i].orderItemList[j].num;
				totalValue.totalMoney+=cartList[i].orderItemList[j].totalFee;
			}
		}
		return totalValue;
	}
	
});