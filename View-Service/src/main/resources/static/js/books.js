jQuery(function(){	
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
		
		var imageURL = $(".book-image-wrappers").find(".book-image-wrapper.focused .book-image").attr("src");
		
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
});