jQuery(function(){
	$(document).on("click",".board-container-footer-container-button[name='register']",function(){
		location.href=API_GATEWAY+"/api/v1/views/register";
	});
})

