// 无需分页时使用,和 base_pagination二选一即可.
var app=angular.module('pinyougou',[]);

/*$sce服务写成过滤器*/
app.filter('trustHtml',['$sce',function($sce){
    return function(data){
        return $sce.trustAsHtml(data);
    }
}]);