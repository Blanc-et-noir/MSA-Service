function switchSideBar(name){
	var sideBar = $(".side[name='"+name+"']");
	var sideBarButton = sideBar.find(".side-container-button");
	
	if(sideBar.hasClass("close")){
		sideBar.removeClass("close");
		sideBar.addClass("open");
		
		$(".side-container-button").css({
			"display":"none"
		})
			
		sideBarButton.css({
			"display":"flex"
		})
		
		sideBar.animate({
			"left":"0px"
		},650,"easeInOutExpo",function(){

		});
	}else{
		sideBar.removeClass("open");
		sideBar.addClass("close");
		
		sideBar.animate({
			"left":"-280px"
		},650,"easeInOutExpo",function(){
			$(".side-container-button").css({
				"display":"flex"
			})
		});
	}
}

function closeAllSideBar(){
	var sideBars = $(".side");
	
	sideBars.removeClass("open");
	sideBars.addClass("close");
		
	sideBars.animate({
		"left":"-280px"
	},650,"easeInOutExpo",function(){
			
	});
}

function setLogin(){
	var loginButton = $(".main-side-container-footer-button[name='login']");
	var joinButton = $(".main-side-container-footer-button[name='join']");
	var logoutButton = $(".main-side-container-footer-button[name='logout']");
	logoutButton.css({"display":"flex"});
	loginButton.css({"display":"none"});
	joinButton.css({"display":"none"});
}

function setLogout(){
	var loginButton = $(".main-side-container-footer-button[name='login']");
	var joinButton = $(".main-side-container-footer-button[name='join']");
	var logoutButton = $(".main-side-container-footer-button[name='logout']");
	logoutButton.css({"display":"none"});
	loginButton.css({"display":"flex"});
	joinButton.css({"display":"flex"});
}