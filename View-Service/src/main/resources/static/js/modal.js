function closeModal(modalCloseButton){
	var modal = $(modalCloseButton).parents(".modal-container");
	modal.animate({"opacity":"0"},150,"linear",function(){modal.remove();})
}

function openFailModal(message){
	const delay = 5000;
	var modal = $("<div name='fail' class='modal-container'></div>");
	
	var modalUpperContainer = $("<div class='modal-upper-container'></div>");
	var modalLowerContainer = $("<div class='modal-lower-container'></div>");
	
	var modalHeader = $("<div class='modal-upper-container-header'></div>");
	modalHeader.append("<img class='modal-upper-container-header-svg svg-red' src='"+API_GATEWAY+"/svg/경고.svg'>")
	
	var modalBody = $("<div class='modal-upper-container-body'></div>");
	modalBody.append("<div class='modal-upper-container-body-title'>ERROR</div>");
	modalBody.append("<div class='modal-upper-container-body-subtitle'>"+message+"</div>");
	
	var modalFooter = $("<div class='modal-upper-container-footer'></div>");
	modalFooter.append("<img onclick='closeModal(this)' class='svg-red modal-upper-container-footer-svg' src='"+API_GATEWAY+"/svg/취소.svg'>")
	
	modalUpperContainer.append(modalHeader);
	modalUpperContainer.append(modalBody);
	modalUpperContainer.append(modalFooter);
	
	var modalRemainingTimeCounter = $("<div class='fail-modal-remaining-time-counter'></div>");
	
	modalLowerContainer.append(modalRemainingTimeCounter);
	
	modal.append(modalUpperContainer);
	modal.append(modalLowerContainer);
	
	$(".modal").append(modal);
	
	modal.animate({
		"opacity":"1",
	},200,"linear",function(){
		modalRemainingTimeCounter.animate({
			"width":"100%"
		},delay,"linear",function(){
			modal.animate({
				"opacity":"0",
				"margin-top":"-60px"
			},200,"linear",function(){
				modal.remove();
			})
		})
	});
}

function openSuccessModal(message){
	const delay = 5000;
	var modal = $("<div name='success' class='modal-container'></div>");
	
	var modalUpperContainer = $("<div class='modal-upper-container'></div>");
	var modalLowerContainer = $("<div class='modal-lower-container'></div>");
	
	var modalHeader = $("<div class='modal-upper-container-header'></div>");
	modalHeader.append("<img class='modal-upper-container-header-svg svg-lime' src='"+API_GATEWAY+"/svg/성공.svg'>")
	
	var modalBody = $("<div class='modal-upper-container-body'></div>");
	modalBody.append("<div class='modal-upper-container-body-title'>SUCCESS</div>");
	modalBody.append("<div class='modal-upper-container-body-subtitle'>"+message+"</div>");
	
	var modalFooter = $("<div class='modal-upper-container-footer'></div>");
	modalFooter.append("<img onclick='closeModal(this)' class='svg-lime modal-upper-container-footer-svg' src='"+API_GATEWAY+"/svg/취소.svg'>")
	
	modalUpperContainer.append(modalHeader);
	modalUpperContainer.append(modalBody);
	modalUpperContainer.append(modalFooter);
	
	var modalRemainingTimeCounter = $("<div class='success-modal-remaining-time-counter'></div>");
	
	modalLowerContainer.append(modalRemainingTimeCounter);
	
	modal.append(modalUpperContainer);
	modal.append(modalLowerContainer);
	
	$(".modal").append(modal);
	
	modal.animate({
		"opacity":"1",
	},200,"linear",function(){
		modalRemainingTimeCounter.animate({
			"width":"100%"
		},delay,"linear",function(){
			modal.animate({
				"opacity":"0",
				"margin-top":"-60px"
			},200,"linear",function(){
				modal.remove();
			})
		})
	});
}

function openInfoModal(message){
	const delay = 5000;
	var modal = $("<div name='info' class='modal-container'></div>");
	
	var modalUpperContainer = $("<div class='modal-upper-container'></div>");
	var modalLowerContainer = $("<div class='modal-lower-container'></div>");
	
	var modalHeader = $("<div class='modal-upper-container-header'></div>");
	modalHeader.append("<img class='modal-upper-container-header-svg svg-light-blue' src='"+API_GATEWAY+"/svg/알림.svg'>")
	
	var modalBody = $("<div class='modal-upper-container-body'></div>");
	modalBody.append("<div class='modal-upper-container-body-title'>INFO</div>");
	modalBody.append("<div class='modal-upper-container-body-subtitle'>"+message+"</div>");
	
	var modalFooter = $("<div class='modal-upper-container-footer'></div>");
	modalFooter.append("<img onclick='closeModal(this)' class='svg-light-blue modal-upper-container-footer-svg' src='"+API_GATEWAY+"/svg/취소.svg'>")
	
	modalUpperContainer.append(modalHeader);
	modalUpperContainer.append(modalBody);
	modalUpperContainer.append(modalFooter);
	
	var modalRemainingTimeCounter = $("<div class='info-modal-remaining-time-counter'></div>");
	
	modalLowerContainer.append(modalRemainingTimeCounter);
	
	modal.append(modalUpperContainer);
	modal.append(modalLowerContainer);
	
	$(".modal").append(modal);
	
	modal.animate({
		"opacity":"1",
	},200,"linear",function(){
		modalRemainingTimeCounter.animate({
			"width":"100%"
		},delay,"linear",function(){
			modal.animate({
				"opacity":"0",
				"margin-top":"-60px"
			},200,"linear",function(){
				modal.remove();
			})
		})
	});
}