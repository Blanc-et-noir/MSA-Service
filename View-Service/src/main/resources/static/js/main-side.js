jQuery(function(){
	if(isLoggedIn()){
		setLogin();
	}else{
		setLogout();
	}
	
	$(document).on("click",".main-side-container-footer-button[name='login']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/create/tokens";
	});
	
	$(document).on("click",".main-side-container-footer-button[name='join']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/create/members";
	});
	
	$(document).on("click",".side-container-body-container-box[name='home']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/home";
	});
	
	$(document).on("click",".side-container-body-container-box[name='board']",function(e){
		location.href=API_GATEWAY+"/api/v1/views/read/books";
	});
	
	$(document).on("click",".side-container-body-container-box[name='manage-books']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/manage/books";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='register']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/create/books";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='manage-members']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/manage/members";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='manage-reservations']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/manage/reservations";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='report']",function(e){
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/create/reports";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
		}
	});
	
	$(document).on("click",".side-container-body-container-box[name='withdraw']",function(e){		
		if(isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/delete/members";
		}else{
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
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
