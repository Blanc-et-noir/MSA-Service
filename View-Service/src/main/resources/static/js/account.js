jQuery(function(){
	if(!isLoggedIn()){
		location.href=API_GATEWAY+"/api/v1/views/login";
	}
})