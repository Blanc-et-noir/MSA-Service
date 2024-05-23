jQuery(function(){
	if(isLoggedIn()){
		setLogin();
	}else{
		setLogout();
	}
	
	$(document).on("click",".main-side-container-footer-button[name='login']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/login";
	});
	
	$(document).on("click",".main-side-container-footer-button[name='join']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/join";
	});
	
	$(document).on("click",".side-container-body-container-box[name='home']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/home";
	});
	
	$(document).on("click",".side-container-body-container-box[name='intro']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/intro";
	});
	
	$(document).on("click",".side-container-body-container-box[name='board']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/board";
	});
	
	$(document).on("click",".side-container-body-container-box[name='manage-books']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/manage/books";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='register']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/register";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='manage-accounts']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/manage/accounts";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='manage-reservations']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/manage/reservations";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='report']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/report";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='withdraw']",function(e){		
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/withdraw";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
	});
	
	$(document).on("click",".main-side-container-footer-button[name='logout']",function(e){
		if(isLoggedIn()){
			var memberAccessToken = loadMemberAccessToken();
			var memberRefreshToken = loadMemberRefreshToken();
			
			$.ajax({
				"url":API_GATEWAY+"/api/v1/tokens",
				"headers":{
					"member-access-token":memberAccessToken,
					"member-refresh-token":memberRefreshToken
				},
				"type":"delete",
				"contentType":"application/json",
				"dataType":"json",
				"data":JSON.stringify({
					"member-access-token":memberAccessToken,
					"member-refresh-token":memberRefreshToken
				})
			}).always(function(){
				deleteMemberAccessToken();
				deleteMemberRefreshToken();
				location.href=API_GATEWAY+"/api/v1/views/home";
			});
		}else{
			location.href=API_GATEWAY+"/api/v1/views/home";
		}
	});
});
