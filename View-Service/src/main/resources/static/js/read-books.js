jQuery(function(){
	$(document).on("click",".board-container-footer-container-button[name='register']",function(){
		location.href=API_GATEWAY+"/api/v1/views/create/books";
	});
	
	$(document).on("click",".board-container-body-wrapper", function(e){
		var bookID = null;
		
		if($(e.target).hasClass(".board-container-body-wrapper")){
			bookID = $(e.target).attr("value");
		}else{
			bookID = $(e.target).parents(".board-container-body-wrapper").attr("value");
		}
		
		location.href=API_GATEWAY+"/api/v1/views/read/books/"+bookID+"?book-statuses=NORMAL&book-image-statuses=NORMAL";
	});
})

