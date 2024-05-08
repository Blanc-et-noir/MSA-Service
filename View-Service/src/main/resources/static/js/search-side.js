jQuery(function(){
	const MAX_BOOK_ID = 9007199254740991;
	const LIMIT = 9;
	
	function resetParameters(){
		bookID = MAX_BOOK_ID;
		parameters = "limit="+LIMIT+"&book-id="+bookID+"&book-statuses=NORMAL&book-image-statuses=NORMAL&";
	}
	
	function renderBooks(books){
		
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
		$.ajax({
			"url":API_GATEWAY+"/api/v1/books?"+parameters,
			"type":"get",
			"dataType":"json"
		}).done(function(response){
			var data = response.data;
			var books = data["books"];
			
			if(books.length==0||books==null||books==undefined){
				
			}else{
				bookID = data["book-id"];
			}
			
			renderBooks(books);
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			
		});
	}
		
	resetParameters();
	
	$(document).on("click","#search-side-container-footer-reset-button",function(e){
		resetParameters();
		$("#book-price-slider").slider({
			"values":[0,100000]
		})
		$("#book-price-value").text("0원 - 100000원");
		$(".book-category-label-input").prop("checked", false);
		$(".book-quality-label-input").prop("checked", false);
		$(".book-name-label-input").val("");
		$(".book-publisher-name-label-input").val("");
		$(".book-detailed-place-label-input").val("");
		$(".book-category-label").removeClass("checked");
		$(".book-quality-label").removeClass("checked");
	})
	
	$(document).on("click","#search-side-container-footer-search-button",function(e){
		resetParameters();
		
		//체크박스
		var checkbox = $(".book-category-label-input,.book-quality-label-input");
		
		for(var i=0; i<checkbox.length; i++){
			if(checkbox.eq(i).is(":checked")){
				parameters += checkbox.eq(i).attr("name")+"="+checkbox.eq(i).val()+"&";
			}
		}
		
		//도서명체크
		var bookName = $(".book-name-label-input").val();
		
		if(bookName.length!=0&&!checkBookName(bookName)){
			openFailModal("도서명은 1 - 40자리의 숫자, 영어, 한글 및 일부 특수문자로만 구성되어야 합니다.");
			return;
		}
		
		//출판사명체크
		var bookPublisherName = $(".book-publisher-name-label-input").val();
		
		if(bookPublisherName.length!=0&&!checkBookName(bookPublisherName)){
			openFailModal("출판사명은 1 - 40자리의 숫자, 영어, 한글 및 일부 특수문자로만 구성되어야 합니다.");
			return;
		}
		
		//판매지역체크
		var bookDetailedPlace = $(".book-detailed-place-label-input").val();
		
		if(bookDetailedPlace.length!=0&&!checkBookDetailedPlace(bookDetailedPlace)){
			openFailModal("거래장소는 1 - 100자리의 숫자, 영어, 한글 및 일부 특수문자로만 구성되어야 합니다.");
			return;
		}
		
		//파라미터 설정
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
