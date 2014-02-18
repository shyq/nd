function loginout(){
	bootbox.confirm("您确定要退出系统吗?", function(result) {
		if(result){
			window.location.href = ctx + "/login!logout.action";
		}
	}); 
}