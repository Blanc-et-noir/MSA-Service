jQuery(function(){
	if(!isLoggedIn()){
		location.href=API_GATEWAY+"/api/v1/views/login";
	}
	
	var bookID = null;
	var bookImageCapacity = 4;
	var bookImageMaxSize = 10*1024*1024;
	
	function setNavigationButton(){
		if($(".register-container-box.focused").next(".register-container-box").length==0){
			$(".register-container-footer-button[name='next']").removeClass("focused");
		}else{
			$(".register-container-footer-button[name='next']").addClass("focused");
		}
		
		if($(".register-container-box.focused").prev(".register-container-box").length==0){
			$(".register-container-footer-button[name='prev']").removeClass("focused");
		}else{
			$(".register-container-footer-button[name='prev']").addClass("focused");
		}
		
		var phase = $(".register-container-box.focused").attr("value");
		$(".register-container-footer-phase").text(phase+" / 5");
	}
	
	setNavigationButton();
	
	$(document).on("click","#book-image-add-button",function(e){
		if(!isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
		
		if($(".book-image-wrapper").length>=bookImageCapacity){
			openToast({
				"toast-type":"fail",
				"toast-message":"도서 이미지는 최대 "+bookImageCapacity+"개까지 등록하실 수 있습니다."
			});
			return;
		}
		
		var body = $(".book-image-upload-container-body");
		var bookImageWrapper = $("<div class='book-image-wrapper book-image-not-added'></div>");
		var bookImageLabel = $("<label class='book-image-label'></label>");
		var bookImageThumbnail = $("<img src='/svg/이미지추가.svg' class='book-image-thumbnail'/>");
		var bookImageInput = $("<input class='book-image-input' type='file' accept='image/png, image/jpeg, image/jpg'/>");
		var bookImageDeleteButton = $("<div class='book-image-delete-button'></div>");
		
		bookImageDeleteButton.append("<img class='book-image-delete-button-svg' src='/svg/모퉁이엑스.svg' />")
		
		bookImageLabel.append(bookImageThumbnail);
		bookImageLabel.append(bookImageInput);
		
		bookImageWrapper.append(bookImageDeleteButton);
		bookImageWrapper.append(bookImageLabel);
		
		body.append(bookImageWrapper);
	});
	
	$(document).on("click",".book-image-delete-button",function(e){
		var wrapper = $(e.target).parents(".book-image-wrapper");
		
		if(wrapper.hasClass("book-image-added")){
			var bookImageID = wrapper.attr("value");
			
			if(!isLoggedIn()){
				location.href=API_GATEWAY+"/api/v1/views/login";
			}

			$.ajax({
				"url":API_GATEWAY+"/api/v1/books/"+bookID+"/book-images/"+bookImageID,
				"headers":{
					"member-access-token":loadMemberAccessToken()
				},
				"type":"delete"
			}).done(function(response){
				wrapper.remove();
			}).fail(function(xhr, status, error){
				var data = JSON.parse(xhr.responseText);
				const code = data.code;
				const message = data.message;
				openToast({
					"toast-type":"fail",
					"toast-message":message
				})
			})
			
		}else{
			wrapper.remove();
		}
	});
	
	$(document).on("change",".book-image-input",function(e) {
		if(!isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
		
		if(e.target.files[0].size>bookImageMaxSize){
			openToast({
				"toast-type":"fail",
				"toast-message":"도서 이미지는 개당 최대 "+(bookImageMaxSize/(1024*1024))+"MB 크기까지 등록하실 수 있습니다."
			})
			return;
		}
		
		$(e.target).siblings(".book-image-thumbnail").attr('src',URL.createObjectURL(e.target.files[0]));
		
		var formData = new FormData();
		formData.append("book-image",e.target.files[0]);
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/books/"+bookID+"/book-images",
			"headers":{
				"member-access-token":loadMemberAccessToken()
			},
			"type":"post",
			"contentType":false,
			"processData":false,
			"dataType":"json",
			"data":formData
		}).done(function(response){
			var bookImageID = response["data"]["book-image-id"];
			$(e.target).parents(".book-image-wrapper").addClass("book-image-added");
			$(e.target).parents(".book-image-wrapper").removeClass("book-image-not-added");
			$(e.target).parents(".book-image-wrapper").attr("value",bookImageID);
			$(e.target).parents(".book-image-wrapper").find("input").attr("disabled",true);
		}).fail(function(xhr, status, error){
			$(e.target).parents(".book-image-wrapper").remove();
			
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
				
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
		})
	});
	
	$(document).on("click","#register-button",function(e){
		if(!isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/login";
		}
				
		$.ajax({
			"url":API_GATEWAY+"/api/v1/books/"+bookID,
			"headers":{
				"member-access-token":loadMemberAccessToken()
			},
			"type":"patch",
			"contentType":"application/json",
			"dataType":"json",
			"data":JSON.stringify({
				"book-status":"NORMAL"
			})
		}).done(function(response){
			location.href=API_GATEWAY+"/api/v1/views/board";
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
	
	$(document).on("click","#close-address-modal-button",function(e){
		$("#wrap").css({"display":"none"});
		$("#open-address-modal-button").css({"display":"flex"});
		$("#close-address-modal-button").css({"display":"none"});
	});

	$(document).on("click","#open-address-modal-button",function(e){
		$("#wrap").css({"display":"block"});
		$("#open-address-modal-button").css({"display":"none"});
		$("#close-address-modal-button").css({"display":"flex"});
		
		var wrap = document.getElementById("wrap");

    	new daum.Postcode({
    		"theme":{
    			bgColor: "",
    			searchBgColor: "",
    			contentBgColor: "",
    			pageBgColor: "",
    			textColor: "",
    			queryTextColor: "",
    			postcodeTextColor: "",
    			emphTextColor: "",
    			outlineColor: ""
    		},
			"oncomplete": function(data) {
				var address = data.sido+" "+data.sigungu;
				
				if(data.bname1 != ""){
					address += " "+data.bname1;
				}else{
					address += " "+data.bname2;
				}
				
            	$("#wrap").css({"display":"none"});
				$("#open-address-modal-button").css({"display":"flex"});
				$("#close-address-modal-button").css({"display":"none"});
				$(".register-container-body-input-field-input[name='book-place']").val(address);
				$(".register-container-body-input-field-input[name='book-detailed-place']").val(data.address);
        	},
			"onsearch": function(data) {
				
        	},
        	"onresize" : function(size) {
				wrap.style.height = "300px";
			},
			"width" : '100%',
			"height" : '100%'
		}).embed(wrap);
	});
	
	$(document).on("click",".register-container-body-input-field-input",function(e){
		$(e.target).removeClass("selected");
	})
	
	$(document).on("click",".register-container-footer-button[name='prev']",function(e){
		var currentRegisterContainerBox = $(".register-container-box.focused");
		var prevRegisterContainerBox = $(".register-container-box.focused").prev(".register-container-box");
		
		if(prevRegisterContainerBox.length==0){
			return;
		}
		
		currentRegisterContainerBox.removeClass("focused");
		prevRegisterContainerBox.addClass("focused");
		setNavigationButton();
	});
	
	$(document).on("click",".register-container-footer-button[name='next']",function(e){
		var currentRegisterContainerBox = $(".register-container-box.focused");
		var nextRegisterContainerBox = $(".register-container-box.focused").next(".register-container-box");
		var phase = $(currentRegisterContainerBox).attr("value");
		
		if(nextRegisterContainerBox.length==0){
			return;
		}
		
		if(phase==1){
			var bookName = $(".register-container-body-input-field-input[name='book-name']").val();
			var bookPublisherName = $(".register-container-body-input-field-input[name='book-publisher-name']").val();
			var bookCategory = $(".register-container-body-input-field-input[name='book-category']").val();
			
			if(!checkBookName(bookName)){
				$(".register-container-body-input-field-input[name='book-name']").addClass("selected");
				openToast({
					"toast-type":"fail",
					"toast-time":10000,
					"toast-message":"도서명은 1자리 이상, 40자리 이하의 한글, 영어, 숫자 및 일부 특수문자로 구성되어야 합니다."
				})
				
				return;
			}
			
			if(!checkBookPublisherName(bookPublisherName)){
				$(".register-container-body-input-field-input[name='book-publisher-name']").addClass("selected");
				openToast({
					"toast-type":"fail",
					"toast-message":"출판사명은 1자리 이상, 40자리 이하의 한글, 영어, 숫자 및 일부 특수문자로 구성되어야 합니다."
				})
				return;
			}
			
			if(bookID==null){
				if(!isLoggedIn()){
					location.href=API_GATEWAY+"/api/v1/views/login";
				}
				
				$.ajax({
					"url":API_GATEWAY+"/api/v1/books",
					"headers":{
						"member-access-token":loadMemberAccessToken()
					},
					"type":"post",
					"contentType":"application/json",
					"dataType":"json",
					"data":JSON.stringify({
						"book-name":bookName,
						"book-publisher-name":bookPublisherName,
						"book-category":bookCategory
					})
				}).done(function(response){
					bookID = response["data"]["book-id"];
					openToast({
						"toast-type":"info",
						"toast-message":"해당 도서 정보를 임시로 저장했습니다."
					})
					
					currentRegisterContainerBox.removeClass("focused");
					nextRegisterContainerBox.addClass("focused");
					setNavigationButton();
				}).fail(function(xhr, status, error){
					var data = JSON.parse(xhr.responseText);
					const code = data.code;
					const message = data.message;
					
					openToast({
						"toast-type":"fail",
						"toast-message":message
					})
				})
			}else{
				if(!isLoggedIn()){
					location.href=API_GATEWAY+"/api/v1/views/login";
				}
				
				$.ajax({
					"url":API_GATEWAY+"/api/v1/books/"+bookID,
					"headers":{
						"member-access-token":loadMemberAccessToken()
					},
					"type":"patch",
					"contentType":"application/json",
					"dataType":"json",
					"data":JSON.stringify({
						"book-name":bookName,
						"book-publisher-name":bookPublisherName,
						"book-category":bookCategory
					})
				}).done(function(response){
					openToast({
						"toast-type":"info",
						"toast-message":"해당 도서 정보를 임시로 저장했습니다."
					})
					
					currentRegisterContainerBox.removeClass("focused");
					nextRegisterContainerBox.addClass("focused");
					setNavigationButton();
				}).fail(function(xhr, status, error){
					var data = JSON.parse(xhr.responseText);
					const code = data.code;
					const message = data.message;
					
					openToast({
						"toast-type":"fail",
						"toast-message":message
					})
				})
			}
			
		}else if(phase==2){
			var bookPrice = $(".register-container-body-input-field-input[name='book-price']").val();
			var bookQuality = $(".register-container-body-input-field-input[name='book-quality']").val();
			
			if(!isLoggedIn()){
				location.href=API_GATEWAY+"/api/v1/views/login";
			}
			
			if(!(bookPrice>=1&&bookPrice<=100000)){
				$(".register-container-body-input-field-input[name='book-price']").addClass("selected");
				openToast({
					"toast-type":"fail",
					"toast-message":"판매가격은 1원 - 100000원 범위내에 있어야합니다."
				});
				return;
			}
			
			$.ajax({
				"url":API_GATEWAY+"/api/v1/books/"+bookID,
				"headers":{
					"member-access-token":loadMemberAccessToken()
				},
				"type":"patch",
				"contentType":"application/json",
				"dataType":"json",
				"data":JSON.stringify({
					"book-quality":bookQuality,
					"book-price":bookPrice
				})
			}).done(function(response){
				openToast({
					"toast-type":"info",
					"toast-message":"해당 도서 정보를 임시로 저장했습니다."
				});
				currentRegisterContainerBox.removeClass("focused");
				nextRegisterContainerBox.addClass("focused");
				setNavigationButton();
			}).fail(function(xhr, status, error){
				var data = JSON.parse(xhr.responseText);
				const code = data.code;
				const message = data.message;
					
				openToast({
					"toast-type":"fail",
					"toast-message":message
				});
			})
		}else if(phase==3){
			var bookPlace = $(".register-container-body-input-field-input[name='book-place']").val();
			var bookDetailedPlace = $(".register-container-body-input-field-input[name='book-detailed-place']").val();
			
			if(!isLoggedIn()){
				location.href=API_GATEWAY+"/api/v1/views/login";
			}
			
			if(!checkBookPlace(bookPlace)){
				$(".register-container-body-input-field-input[name='book-place']").addClass("selected");
				openToast({
					"toast-type":"fail",
					"toast-message":"거래장소는 시/도, 시/군/구, 읍/면/동을 순서대로 입력해야합니다."
				});
				return;
			}
			
			if(!checkBookDetailedPlace(bookDetailedPlace)){
				$(".register-container-body-input-field-input[name='book-detailed-place']").addClass("selected");
				openToast({
					"toast-type":"fail",
					"toast-message":"거래장소는 1 - 100자리 이하의 숫자, 영어, 한글 및 일부 특수문자로만 구성되어야 합니다."
				});
				return;
			}
			
			$.ajax({
				"url":API_GATEWAY+"/api/v1/books/"+bookID,
				"headers":{
					"member-access-token":loadMemberAccessToken()
				},
				"type":"patch",
				"contentType":"application/json",
				"dataType":"json",
				"data":JSON.stringify({
					"book-place":bookPlace,
					"book-detailed-place":bookDetailedPlace
				})
			}).done(function(response){
				openToast({
					"toast-type":"info",
					"toast-message":"해당 도서 정보를 임시로 저장했습니다."
				});
				currentRegisterContainerBox.removeClass("focused");
				nextRegisterContainerBox.addClass("focused");
				setNavigationButton();
			}).fail(function(xhr, status, error){
				var data = JSON.parse(xhr.responseText);
				const code = data.code;
				const message = data.message;
				
				openToast({
					"toast-type":"fail",
					"toast-message":message
				});
			})
		}else if(phase==4){
			var bookDescription = $(".register-container-body-input-field-input[name='book-description']").val();
			
			if(!isLoggedIn()){
				location.href=API_GATEWAY+"/api/v1/views/login";
			}
			
			if(!checkBookDescription(bookDescription)){
				$(".register-container-body-input-field-input[name='book-description']").addClass("selected");
				openToast({
					"toast-type":"fail",
					"toast-message":"도서 설명은 1 - 1000자리로 구성되어야 합니다."
				});
				return;
			}
			
			$.ajax({
				"url":API_GATEWAY+"/api/v1/books/"+bookID,
				"headers":{
					"member-access-token":loadMemberAccessToken()
				},
				"type":"patch",
				"contentType":"application/json",
				"dataType":"json",
				"data":JSON.stringify({
					"book-description":bookDescription
				})
			}).done(function(response){
				openToast({
					"toast-type":"info",
					"toast-message":"해당 도서 정보를 임시로 저장했습니다."
				});
				currentRegisterContainerBox.removeClass("focused");
				nextRegisterContainerBox.addClass("focused");
				setNavigationButton();
			}).fail(function(xhr, status, error){
				var data = JSON.parse(xhr.responseText);
				const code = data.code;
				const message = data.message;
				
				openToast({
					"toast-type":"fail",
					"toast-message":message
				});
			})
		}
	});
});