function progress(per) {
	var bar = $(".bar");
	var RADIUS = 54;
	var CIRCUMFERENCE = 2 * Math.PI * RADIUS;
	
	bar.css({
		"strokeDasharray":CIRCUMFERENCE
	});
	
	var progress = per / 100;
  	var dashoffset = CIRCUMFERENCE * (1 - progress);
  	
  	bar.css({
		"strokeDashoffset":dashoffset
	});
}

function closeAllToast(){
	var toastWrapper = $(".toast-wrapper");
	toastWrapper.remove();
}

function closeToast(){
	var toastWrapper = $(".toast-wrapper");
	toastWrapper.animate({
		"opacity":"0"
	},300,"linear",function(){
		toastWrapper.remove();
	});
}

function openToast(config){
	closeAllToast();
	
	var toastType = config["toast-type"];
	var toastMessage = config["toast-message"];
	var toastTime = config["toast-time"];
	var per = 0;
	
	toastType=toastType!=undefined?toastType:"info";
	toastTime=toastTime!=undefined?toastTime:5000;
	
	var toastWrapper = $("<div class='toast-wrapper "+toastType+"'></div>");
	var toastHeader = $("<div class='toast-header'></div>");
	var toastBody = $("<div class='toast-body'></div>");
	var toastFooter = $("<div class='toast-footer'></div>");
	
	var circleProgressWrap = $("<div class='circle-progress-wrap'><svg class='circle-progress' width='18' height='18' viewBox='0 0 120 120'><circle class='frame' cx='60' cy='60' r='54' stroke-width='12' /><circle class='bar' cx='60' cy='60' r='54' stroke-width='12' /></svg></div>");
	
	toastHeader.append(circleProgressWrap);
	
	var imageURL = null;
	
	if(toastType=="info"){
		imageURL = API_GATEWAY+"/svg/알림.svg";
	}else if(toastType=="success"){
		imageURL = API_GATEWAY+"/svg/성공.svg";
	}else if(toastType=="fail"){
		imageURL = API_GATEWAY+"/svg/경고.svg";
	}else{
		imageURL = API_GATEWAY+"/svg/경고.svg";
	}
	
	toastHeader.append("<img class='toast-image' src='"+imageURL+"'>");
	
	toastBody.append("<div class='toast-message'>"+toastMessage+"</div>");
	toastFooter.append("<div class='toast-close-button' onclick='closeToast()'><img class='toast-close-button-svg' src='"+API_GATEWAY+"/svg/엑스.svg'></div>");
	
	toastWrapper.append(toastHeader);
	toastWrapper.append(toastBody);
	toastWrapper.append(toastFooter);
	
	$("body").append(toastWrapper);
		
	progress(per);
	
	toastWrapper.css({
		"display":"flex"
	});
	
	toastWrapper.animate({
		"opacity":"1"
	},300,"linear",function(){
		progress(per)
		var interval = setInterval(function(){
			progress(per)
			per+=0.1;
		
			if(per>=100){
				clearInterval(interval);
				
				toastWrapper.animate({
					"opacity":"0"
				},300,"linear",function(){
					toastWrapper.remove();
					clearInterval(interval);
				});
			}
		}, toastTime/1000);
	});
}