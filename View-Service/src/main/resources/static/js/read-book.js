jQuery(function(){
	var bookID = $(".books-container-body-input-field-input[name='book-id']").val();
	
	function setNavigationButton(){
		if($(".book-image-wrapper.focused").next(".book-image-wrapper").length==0){
			$(".books-container-body-images-wrapper-body-button[name='next']").removeClass("focused");
		}else{
			$(".books-container-body-images-wrapper-body-button[name='next']").addClass("focused");
		}
		
		if($(".book-image-wrapper.focused").prev(".book-image-wrapper").length==0){
			$(".books-container-body-images-wrapper-body-button[name='prev']").removeClass("focused");
		}else{
			$(".books-container-body-images-wrapper-body-button[name='prev']").addClass("focused");
		}
	}
	
	setNavigationButton();
	
	$(document).on("click",".books-container-body-images-wrapper-body-button[name='next']",function(e){
		var currentImageWrapper = $(".book-image-wrapper.focused");
		var nextImageWrapper = $(".book-image-wrapper.focused").next(".book-image-wrapper");
		
		if(nextImageWrapper.length==0){
			return;
		}
		
		currentImageWrapper.removeClass("focused");
		nextImageWrapper.addClass("focused");
		setNavigationButton();
	});
	
	$(document).on("click",".books-container-body-images-wrapper-body-button[name='prev']",function(e){
		var currentImageWrapper = $(".book-image-wrapper.focused");
		var prevImageWrapper = $(".book-image-wrapper.focused").prev(".book-image-wrapper");
		
		if(prevImageWrapper.length==0){
			return;
		}
		
		currentImageWrapper.removeClass("focused");
		prevImageWrapper.addClass("focused");
		setNavigationButton();
	});
	
	$(document).on("click",".books-container-body-images-wrapper-body-button[name='magnify']",function(e){
		var modalCover = $("<div class='modal-cover'></div>");
		var modalWrapper = $("<div class='modal-wrapper'></div>");
		
		if(modalCover.length==0){
			return;
		}
		var bookID =  $(".book-image-wrappers").attr("value");
		var bookImageID = $(".book-image-wrappers").find(".book-image-wrapper.focused").attr("value");
		var imageURL = API_GATEWAY+"/api/v1/books/"+bookID+"/book-images/"+bookImageID+"?book-image-type=ORIGINAL";
		
		var modalWrapperImage = $("<img class='modal-wrapper-image' src='"+imageURL+"' />");
		var modalWrapperTitle = $("<div class='modal-wrapper-title'>아무곳이나 클릭하여 화면 닫기</div>");
		
		modalWrapper.append(modalWrapperImage);
		modalWrapper.append(modalWrapperTitle);
		
		modalCover.append(modalWrapper);
		$("body").append(modalCover);
	});
	
	$(document).on("click",".modal-cover",function(e){
		$(".modal-cover").remove();
	});
	
	$(document).on("click","#close-button",function(e){
		if($(".create-reservations-cover").length>0){
			$(".create-reservations-cover").remove();
		}
	});
	
	$(document).on("click","#reset-button",function(e){
		if($(".create-reservations-cover").length>0){
			$(".create-reservations-cover").find("input").val("");
			$(".create-reservations-cover").find("textarea").val("");
		}
	});
	
	$(document).on("click",".books-container-footer-button[name='reserve']",function(e){
		if($(".create-reservations-cover").length>0){
			$(".create-reservations-cover").remove();
		}
		
		var cover = $("<div class='create-reservations-cover'></div>")
		var container = $("<div class='create-reservations-container'></div>");
		
		var header = $("<div class='create-reservations-container-header'></div>");
		var body = $("<div class='create-reservations-container-body'></div>");
		var footer = $("<div class='join-container-footer'></div>");
			
		body.append("<div class='create-reservations-container-body-input-field'><div class='create-reservations-container-body-input-field-placeholder'>판매자에게 제안할 거래일정</div><input type='datetime-local' name='reservation-wish-time' class='create-reservations-container-body-input-field-input' placeholder='판매자에게 제안할 거래시각, 거래날짜'></div>");
		body.append("<div class='create-reservations-container-body-input-field'><div class='create-reservations-container-body-input-field-placeholder'>판매자에게 제안할 내용</div><textarea name='reservation-description' class='create-reservations-container-body-input-field-textarea' placeholder='판매자에게 제안할 거래장소, 유의사항'></textarea></div>");	
		
		
		var flex = $("<div style='display:flex; margin-top:10px;'></div>");
		flex.append("<div id='close-button' class='create-reservations-container-button' name='close' style='flex:1; margin-rignt:10px;'><img class='create-reservations-container-body-input-field-placeholder-svg svg-green' src='/svg/엑스.svg'><div name='close' class='create-reservations-container-body-input-filed-placeholder-button'>닫기</div></div>");
		flex.append("<div id='reset-button' class='create-reservations-container-button' name='reset' style='flex:1; margin-left:10px;'><img class='create-reservations-container-body-input-field-placeholder-svg svg-orange' src='/svg/리셋.svg'><div name='reset' class='create-reservations-container-body-input-filed-placeholder-button'>초기화</div></div>");
		
		footer.append("<div id='reserve-button' class='create-reservations-container-button' name='reserve'><img class='create-reservations-container-body-input-field-placeholder-svg svg-blue' src='/svg/연필.svg'><div name='reserve' class='create-reservations-container-body-input-filed-placeholder-button'>도서예약</div></div>")
		footer.append(flex);
		
		container.append(header);
		container.append(body);
		container.append(footer);
		
		cover.append(container);
		
		$("body").append(cover);
	});
	
	$(document).on("click","#reserve-button",function(e){
		if(!isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
		}
		
		var reservationWishTime = $(".create-reservations-container-body-input-field-input[name='reservation-wish-time']").val();
		if(reservationWishTime.length==0){
			openToast({
				"toast-type":"fail",
				"toast-message":"판매자에게 제안할 거래시각은 필수적으로 입력해야합니다."
			})
			return;
		}
		
		var reservationDescription = $(".create-reservations-container-body-input-field-textarea[name='reservation-description']").val();
		if(!checkReservationDescription(reservationDescription)){
			openToast({
				"toast-type":"fail",
				"toast-message":"판매자에게 제안할 세부사항은 숫자, 영어, 한글 및 일부 특수문자만을 포함하여 1-300 자리여야 합니다."
			})
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/reservations",
			"headers":{
				"member-access-token":loadMemberAccessToken()
			},
			"type":"post",
			"dataType":"json",
			"contentType":"application/json",
			"dataType":"json",
			"data":JSON.stringify({
				"book-id":$(".books-container").attr("value"),
				"reservation-wish-time":reservationWishTime,
				"reservation-description":reservationDescription
			})
		}).done(function(response){
			openToast({
				"toast-type":"success",
				"toast-message":"해당 도서에 대한 직거래를 요청했습니다."
			})
			
			$(".create-reservations-cover").remove();
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
				
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
		})
	});
});