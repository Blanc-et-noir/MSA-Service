function switchSideBar(name){
	var sideBar = $(".side[name='"+name+"']");
	
	if(sideBar.hasClass("close")){
		sideBar.removeClass("close");
		sideBar.addClass("open");
		
		sideBar.animate({
			"left":"0px"
		},1000,"easeInOutExpo",function(){
			
		});
	}else{
		sideBar.removeClass("open");
		sideBar.addClass("close");
		
		sideBar.animate({
			"left":"-250px"
		},1000,"easeInOutExpo",function(){
			
		});
	}
}

function closeAllSideBar(){
	var sideBars = $(".side");
	
	sideBars.removeClass("open");
	sideBars.addClass("close");
		
	sideBars.animate({
		"left":"-250px"
	},1000,"easeInOutExpo",function(){
			
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