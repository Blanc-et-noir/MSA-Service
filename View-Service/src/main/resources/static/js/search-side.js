jQuery(function(){
	const MAX_BOOK_ID = 9007199254740991;
	const LIMIT = 12;
	var bookID = MAX_BOOK_ID;
	var parameters = "book-statuses=NORMAL&book-image-statuses=NORMAL&";
	var wait = false;
	
	function resetParameters(){
		bookID = MAX_BOOK_ID;
		parameters = "book-statuses=NORMAL&book-image-statuses=NORMAL&";
	}
	
	function renderBooks(books){
		var flex = $(".board-container-body-flex");
		
		for(var i=0; i<books.length;i++){
			var book = books[i];
			
			var cover = $("<div class='board-container-body-wrapper-cover'></div>")
			var wrapper = $("<div class='board-container-body-wrapper' value='"+book["book-id"]+"'></div>");
			var imageWrapper = $("<div class='board-container-body-image-wrapper'></div>");
			var infoWrapper = $("<div class='board-container-body-info-wrapper'></div>");
			
			if(book["book-images"].length!=0){
				var image = $("<img class='board-container-body-image' decoding='async' loading='lazy' src='"+book["book-images"][0]["book-image-url"]+"?book-image-type=THUMBNAIL"+"'>");
				imageWrapper.append(image);
			}else{
				var image = $("<img class='board-container-body-image' decoding='async' loading='lazy' src='/svg/로딩.svg' style='width:10%; height:10%; top:50%; left:50%; transform:translate(-50%,-50%);'>");
				imageWrapper.append(image);
			}
			
			infoWrapper.append("<div class='book-name'>"+book["book-name"]+"</div><div class='book-quality'>"+book["book-quality"]+" 급</div><div class='book-price'>"+book["book-price"]+" 원"+"</div><div class='book-place'>"+book["book-place"]+"</div>")
			
			wrapper.append(imageWrapper);
			wrapper.append(infoWrapper);
			wrapper.append(cover);
			
			flex.append(wrapper);
		}
	}
	
	$("#book-price-slider").slider({
		"range": true,
		"min": 0,
		"max": 100000,
		"values": [0, 100000],
		"step":1000,
		"slide": function(e, v) {
			$("#book-price-slider").attr("min",v.values[0]);
			$("#book-price-slider").attr("max",v.values[1]);
			$("#book-price-value").text(v.values[0]+"원 - "+v.values[1]+"원");
		}
	});
	
	function readBooks(){
		if(wait){
			return;
		}
		
		wait=true;
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/books?"+"limit="+LIMIT+"&book-id="+bookID+"&"+parameters,
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
	
	$(document).on("mousewheel",".board-container-body",function(e){
		var delta = e.originalEvent.wheelDelta;
		var height1 = $(".board-container-body-flex").css("height").replaceAll("px","");
		var height2 = $(".board-container-body").css("height").replaceAll("px","");
		var scroll = $(".board-container-body-flex").scrollTop();
		
		console.log(scroll);
		
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
		
	resetParameters();
	readBooks();
	
	$(document).on("click","#search-side-container-footer-reset-button",function(e){
		$("#book-price-slider").slider({
			"values":[0,100000]
		});
		$("#book-price-value").text("0원 - 100000원");
		$(".book-category-label-input").prop("checked", false);
		$(".book-quality-label-input").prop("checked", false);
		$(".book-name-label-input").val("");
		$(".book-publisher-name-label-input").val("");
		$(".book-detailed-place-label-input").val("");
		$(".book-category-label").removeClass("checked");
		$(".book-quality-label").removeClass("checked");
		$("#book-price-slider").attr("min",0);
		$("#book-price-slider").attr("max",100000);
	})
	
	$(document).on("click","#search-side-container-footer-search-button",function(e){
		resetParameters();
		
		var checkbox = $(".book-category-label-input,.book-quality-label-input");
		
		for(var i=0; i<checkbox.length; i++){
			if(checkbox.eq(i).is(":checked")){
				parameters += checkbox.eq(i).attr("name")+"="+checkbox.eq(i).val()+"&";
			}
		}
		
		var bookName = $(".book-name-label-input").val();
		
		if(bookName.length!=0&&!checkBookName(bookName)){
			openToast({
				"toast-type":"fail",
				"toast-message":"도서명은 1 - 40자리의 숫자, 영어, 한글 및 일부 특수문자로만 구성되어야 합니다."
			})
			return;
		}
		
		var bookPublisherName = $(".book-publisher-name-label-input").val();
		
		if(bookPublisherName.length!=0&&!checkBookName(bookPublisherName)){
			openToast({
				"toast-type":"fail",
				"toast-message":"출판사명은 1 - 40자리의 숫자, 영어, 한글 및 일부 특수문자로만 구성되어야 합니다."
			})
			return;
		}
		
		var bookDetailedPlace = $(".book-detailed-place-label-input").val();
		
		if(bookDetailedPlace.length!=0&&!checkBookDetailedPlace(bookDetailedPlace)){
			openToast({
				"toast-type":"fail",
				"toast-message":"거래장소는 1 - 100자리의 숫자, 영어, 한글 및 일부 특수문자로만 구성되어야 합니다."
			})
			return;
		}
		
		parameters+="book-min-price="+$("#book-price-slider").attr("min")+"&book-max-price="+$("#book-price-slider").attr("max")+"&";
		
		if(bookName.length!=0){
			parameters += "book-name="+bookName+"&";
		}
		
		if(bookPublisherName.length!=0){
			parameters += "book-publisher-name="+bookPublisherName+"&";
		}
		
		if(bookDetailedPlace.length!=0){
			parameters += "book-detailed-place="+bookDetailedPlace+"&";
		}
		
		$(".board-container-body-wrapper").remove();
		switchSideBar("search-side");
		readBooks();
	})
	
	$(document).on("click",".book-category-label .book-category-label-input",function(e){
		var label = $(e.target).parents(".book-category-label");
		var input = $(e.target);
		
		if(input.is(":checked")){
			label.addClass("checked");
		}else{
			label.removeClass("checked");
		}
	});
	
	$(document).on("click",".book-quality-label .book-quality-label-input",function(e){
		var label = $(e.target).parents(".book-quality-label");
		var input = $(e.target);
		
		if(input.is(":checked")){
			label.addClass("checked");
		}else{
			label.removeClass("checked");
		}
	});
});
