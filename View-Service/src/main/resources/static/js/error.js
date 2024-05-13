jQuery(function(){
	var time = 10000;
	var second = time/1000;
	
	$(".second").text(second+"초 후에 자동으로 홈 화면으로 이동합니다.");
	
	var interval = setInterval(function(){
		if(second==0){
			clearInterval(interval);
		}
		
		$(".second").text(second+"초 후에 자동으로 홈 화면으로 이동합니다.");
		second--;
	},1000);
	
	setTimeout(function(){
		location.href=API_GATEWAY+"/api/v1/views/home";
	},time+1000);
})