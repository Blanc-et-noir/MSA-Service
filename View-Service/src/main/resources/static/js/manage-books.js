jQuery(function(){
	if(!isLoggedIn()){
		location.href=API_GATEWAY+"/api/v1/views/login";
	}
	
	const MAX_BOOK_ID = 9007199254740991;
	const LIMIT = 10;
	var bookID = MAX_BOOK_ID;
	var parameters = "book-statuses=UNREGISTERED&book-statuses=TRANSACTING&book-statuses=TRANSACTED&book-statuses=NORMAL&book-statuses=NORMAL&book-image-statuses=NORMAL&";
	var wait = false;
	
	function resetParameters(){
		bookID = MAX_BOOK_ID;
		parameters = "book-statuses=UNREGISTERED&book-statuses=TRANSACTING&book-statuses=TRANSACTED&book-statuses=NORMAL&book-statuses=NORMAL&book-image-statuses=NORMAL&";
	}
	
	function parseBookStatus(bookStatus){
		if(bookStatus=="UNREGISTERED"){
			return "미등록";
		}else if(bookStatus=="NORMAL"){
			return "등록";
		}else if(bookStatus=="TRANSACTING"){
			return "거래중";
		}else if(bookStatus=="TRANSACTED"){
			return "거래완료";
		}
		
		return null;
	}
	
	function parseBookStatusIntoClass(bookStatus){
		if(bookStatus=="UNREGISTERED"){
			return "unregistered";
		}else if(bookStatus=="NORMAL"){
			return "normal";
		}else if(bookStatus=="TRANSACTING"){
			return "transacting";
		}else if(bookStatus=="TRANSACTED"){
			return "transacted";
		}
		
		return null;
	}
	
	function parseBookPrice(bookPrice){
		if(bookPrice==null){
			return "-";
		}else{
			return bookPrice+" 원";
		}
	}
	
	function parseBookQuality(bookQuality){
		if(bookQuality==null){
			return "-";
		}else{
			return bookQuality+" 급";
		}
	}
	
	function parseBookCreateTime(bookCreateTime){
		var now = new Date();
		var time = new Date(bookCreateTime);
		var diff = now.getTime() - time.getTime()
		
		if(diff<1000 * 60 * 60 * 24){
			return bookCreateTime.substring(11);
		}else{
			return bookCreateTime.substring(0,10);
		}
	}
	
	function renderBooks(books){
		var flex = $(".book-wrappers-body");
		
		for(var i=0; i<books.length;i++){
			var book = books[i];
			
			var cover = $("<div class='manage-container-body-wrapper-cover'></div>")
			var wrapper = $("<div class='manage-container-body-wrapper' value='"+book["book-id"]+"'></div>");
			var imageWrapper = $("<div class='manage-container-body-image-wrapper'></div>");
			var infoWrapper = $("<div class='manage-container-body-info-wrapper'></div>");
			
			infoWrapper.append("<div class='book-id'>"+book["book-id"]+"</div><div class='book-status "+(parseBookStatusIntoClass(book["book-status"]))+"'>"+parseBookStatus(book["book-status"])+"</div>"+"<div class='book-name'>"+book["book-name"]+"</div><div class='book-quality'>"+parseBookQuality(book["book-quality"])+"</div><div class='book-price'>"+parseBookPrice(book["book-price"])+"</div>"+"<div class='book-create-time'>"+parseBookCreateTime(book["book-create-time"])+"</div>")
			
			wrapper.append(imageWrapper);
			wrapper.append(infoWrapper);
			wrapper.append(cover);
			
			flex.append(wrapper);
		}
	}
	
	function readBooks(){
		if(!isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
		
		if(wait){
			return;
		}
		
		wait=true;
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/books?"+"limit="+LIMIT+"&book-id="+bookID+"&"+parameters+"&member-id="+readMemberID(loadMemberAccessToken()),
			"type":"get",
			"dataType":"json"
		}).done(function(response){
			var data = response.data;
			var books = data["books"];
			
			if(books.length==0||books==null||books==undefined){
				bookID = null;
			}else{
				bookID = data["book-id"];
			}
			
			renderBooks(books);
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			
		}).always(function(){
			wait=false;
		})
	}
	
	resetParameters();
	readBooks();
	
	$(document).on("click",".manage-container-body-wrapper",function(e){
		
		if($(e.target).hasClass("menu-wrapper-body-button")){
			return;
		}
		
		$(".manage-container-body-info-wrapper").removeClass("hide");
		$(".menu-wrapper").remove();
		
		var wrapper = $(e.target).hasClass("manage-container-body-wrapper")?$(e.target):$(e.target).parents(".manage-container-body-wrapper");
		var bookID = wrapper.attr("value");
		
		var menuWrapper = $("<div class='menu-wrapper' value='"+bookID+"'></div>");
		var menuWrapperHeader = $("<div class='menu-wrapper-header'></div>");
		var menuWrapperBody = $("<div class='menu-wrapper-body'></div>");
		var menuWrapperFooter = $("<div class='menu-wrapper-footer'></div>");
		
		menuWrapperBody.append("<div class='menu-wrapper-body-button' name='read'>도서 조회하기</div>");
		menuWrapperBody.append("<div class='menu-wrapper-body-button' name='update'>도서 수정하기</div>");
		menuWrapperBody.append("<div class='menu-wrapper-body-button' name='delete'>도서 삭제하기</div>");
		menuWrapperBody.append("<div class='menu-wrapper-body-button' name='reservation'>예약 조회하기</div>");
		
		menuWrapper.append(menuWrapperHeader);
		menuWrapper.append(menuWrapperBody);
		menuWrapper.append(menuWrapperFooter);
		
		wrapper.find(".manage-container-body-info-wrapper").addClass("hide");
		wrapper.append(menuWrapper);
	});
	
	$(document).on("mousewheel",".manage-container-body",function(e){		
		if(bookID==null){
			return;
		}
		
		if(wait){
			return;
		}
		
		if((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
			readBooks();
    	}
		
	});
	
	$(document).on("click",".menu-wrapper-body-button[name='delete']",function(e){
		if(!isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
		
		var wrapper = $(e.target).parents(".manage-container-body-wrapper");
		var bookID = wrapper.attr("value");
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/books/"+bookID,
			"type":"delete",
			"headers":{
				"member-access-token":loadMemberAccessToken()
			}
		}).done(function(response){			
			openToast({
				"toast-type":"success",
				"toast-message":response["message"]
			})
			
			wrapper.remove();
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
		});
	})
	
	$(document).on("click",".menu-wrapper-body-button[name='read']",function(e){
		var wrapper = $(e.target).parents(".manage-container-body-wrapper");
		var bookID = wrapper.attr("value");
		
		location.href=API_GATEWAY+"/api/v1/views/books/"+bookID+"?book-statuses=NORMAL&book-statuses=TRANSACTING&book-statuses=TRANSACTED&book-image-statuses=NORMAL";
	})
});