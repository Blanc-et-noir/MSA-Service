jQuery(function(){
	if(isLoggedIn()){
		location.href=API_GATEWAY+"/api/v1/views/home";
	}
	
	$(document).on("click",".login-container-body-input-field-input",function(e){
		$(e.target).removeClass("selected");
	})
	
	$(document).on("click","#find-button",function(e){
		location.href="http://localhost:3000/api/v1/views/find/members";
	});
	
	$(document).on("click","#join-button",function(e){
		location.href="http://localhost:3000/api/v1/views/create/members";
	});
	
	$(document).on("click","#login-button",function(e){
		var memberID = $(".login-container-body-input-field-input[name='member-id']").val();
		var memberPW = $(".login-container-body-input-field-input[name='member-pw']").val();
		
		if(!checkMemberID(memberID)){
			openToast({
				"toast-type":"fail",
				"toast-message":"ID는 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$(".login-container-body-input-field-input[name='member-id']").addClass("selected");
			return;
		}
		
		if(!checkMemberPW(memberPW)){
			openToast({
				"toast-type":"fail",
				"toast-message":"PW는 8자리 이상, 16자리 이하의 영어, 숫자 및 특수문자를 모두 포함하여 구성되어야 합니다."
			})
			$(".login-container-body-input-field-input[name='member-pw']").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/tokens",
			"type":"post",
			"dataType" : "json",
			"data":JSON.stringify({
				"member-id":memberID,
				"member-pw":memberPW
			}),
			"contentType":"application/json"
		}).done(function(response){
			var data = response.data;
			var memberAccessToken = data["member-access-token"];
			var memberRefreshToken = data["member-refresh-token"];
			
			saveMemberAccessToken(memberAccessToken);
			saveMemberRefreshToken(memberRefreshToken);
			
			location.href=API_GATEWAY+"/api/v1/views/home";
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
		});
	});
});