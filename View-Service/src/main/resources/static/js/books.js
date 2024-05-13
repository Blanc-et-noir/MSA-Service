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
	
	
});