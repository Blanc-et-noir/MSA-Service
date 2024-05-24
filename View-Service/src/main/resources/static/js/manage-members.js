jQuery(function(){
	if(!isLoggedIn()){
		location.href=API_GATEWAY+"/api/v1/views/create/tokens";
	}
	
	$(document).on("click",".account-container-body-input-field-input",function(e){
		$(e.target).removeClass("selected");
	})
	
	$(document).on("click",".account-container-header-button",function(e){
		var name = $(e.target).attr("name");
		var updateNameButton = $(".account-container-header-button[name='update-name']");
		var updatePWButton = $(".account-container-header-button[name='update-pw']");
		var updateEmailButton = $(".account-container-header-button[name='update-email']");
		
		if(name=="update-name"){
			$(updateNameButton).addClass("focused");
			$(updatePWButton).removeClass("focused");
			$(updateEmailButton).removeClass("focused");
			$("#update-name-box").css({"display":"block"});
			$("#update-pw-box").css({"display":"none"});
			$("#update-email-box").css({"display":"none"});
		}else if(name=="update-pw"){
			$(updateNameButton).removeClass("focused");
			$(updatePWButton).addClass("focused");
			$(updateEmailButton).removeClass("focused");
			$("#update-name-box").css({"display":"none"});
			$("#update-pw-box").css({"display":"block"});
			$("#update-email-box").css({"display":"none"});
		}else{
			$(updateNameButton).removeClass("focused");
			$(updatePWButton).removeClass("focused");
			$(updateEmailButton).addClass("focused");
			$("#update-name-box").css({"display":"none"});
			$("#update-pw-box").css({"display":"none"});
			$("#update-email-box").css({"display":"block"});
		}
	})
	
	$(document).on("click",".account-container-body-input-field-input-button[name='send']",function(e){
		var memberEmail = $(".account-container-body-input-field-input[name='member-email']").val();
		
		if(!checkMemberEmail(memberEmail)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/verifications/member-emails",
			"type":"post",
			"dataType" : "json",
			"contentType":"application/json",
			"data":JSON.stringify({
				"member-email":memberEmail+"@"+$("select[name='member-email-domain']").val()
			})			
		}).done(function(response){
			openToast({
				"toast-type":"success",
				"toast-message":"해당 이메일에 대한 인증코드를 발송했습니다. 이메일에 대한 인증코드는 5분간 유효합니다."
			})
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
	
	$(document).on("click",".account-container-body-input-field-input-button[name='verify']",function(){
		var memberEmail = $(".account-container-body-input-field-input[name='member-email']").val();
		var memberEmailVerificationCode = $(".account-container-body-input-field-input[name='member-email-verification-code']").val();
		
		if(!checkMemberEmail(memberEmail)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$(".account-container-body-input-field-input[name='member-email']").addClass("selected");
			return;
		}
		
		if(!checkMemberEmailVerificationCode(memberEmailVerificationCode)){
			openToast({
				"toast-type":"fail",
				"toast-message":"인증코드는 6자리 숫자로 구성되어야 합니다."
			})
			$(".account-container-body-input-field-input[name='member-email-verification-code']").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/verifications/member-emails",
			"type":"delete",
			"dataType" : "json",
			"contentType":"application/json",
			"data":JSON.stringify({
				"member-email":memberEmail+"@"+$("select[name='member-email-domain']").val(),
				"member-email-verification-code":memberEmailVerificationCode
			})
		}).done(function(response){
			openToast({
				"toast-type":"fail",
				"toast-message":"해당 이메일에 대한 소유권 인증에 성공했습니다. 이메일에 대한 인증정보는 30분간 유효합니다."
			})
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			
			if(code=="MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND"){
				$(".account-container-body-input-field-input[name='member-email']").addClass("selected");
			}else if(code=="MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG"){
				$(".account-container-body-input-field-input[name='member-email-verification-code']").addClass("selected");
			}
			
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
		})
	});
	
	$(document).on("click","#reset-button",function(e){
		$(".account-container-body-input-field-input").val("");
		$(".account-container-body-input-field-input").removeClass("selected");
	});
	
	$(document).on("click","#update-button",function(e){
		var name = $(".account-container-header-button.focused").attr("name");
		
		if(!isLoggedIn()){
			location.href=API_GATEWAY+"/api/v1/views/create/tokens";
		}
		
		var memberID = readMemberID(loadMemberAccessToken());
		
		if(name=="update-name"){
			var memberName = $(".account-container-body-input-field-input[name='member-name']").val();
			
			if(!checkMemberName(memberName)){
				openToast({
					"toast-type":"fail",
					"toast-message":"이름은 2자리 이상, 5자리 이하의 한글로 구성되어야 합니다."
				})
				$(".account-container-body-input-field-input[name='member-name']").addClass("selected");
				return;
			}
			
			$.ajax({
				"url":API_GATEWAY+"/api/v1/members/"+memberID,
				"type":"patch",
				"headers":{
					"member-access-token":loadMemberAccessToken()
				},
				"contentType":"application/json",
				"dataType":"json",
				"data":JSON.stringify({
					"member-name":memberName
				})
			}).done(function(response){
				openToast({
					"toast-type":"success",
					"toast-message":"회원 이름이 성공적으로 변경되었습니다."
				})
			}).fail(function(xhr, status, error){
				var data = JSON.parse(xhr.responseText);
				const code = data.code;
				const message = data.message;
				openToast({
					"toast-type":"fail",
					"toast-message":message
				})
			})
		}else if(name=="update-pw"){
			var memberPW = $(".account-container-body-input-field-input[name='member-pw']").val();
			var memberPWCheck = $(".account-container-body-input-field-input[name='member-pw-check']").val();
			
			if(!checkMemberPW(memberPW)){
				openToast({
					"toast-type":"fail",
					"toast-message":"비밀번호는 8자리 이상, 16자리 이하의 영어, 숫자 및 특수문자를 모두 포함하여 구성되어야 합니다."
				})
				$(".account-container-body-input-field-input[name='member-pw']").addClass("selected");
				return;
			}
			
			if(!checkMemberPW(memberPWCheck)){
				openToast({
					"toast-type":"fail",
					"toast-message":"비밀번호 확인은 8자리 이상, 16자리 이하의 영어, 숫자 및 특수문자를 모두 포함하여 구성되어야 합니다."
				})
				$(".account-container-body-input-field-input[name='member-pw-check']").addClass("selected");
				return;
			}
			
			if(memberPW!=memberPWCheck){
				openToast({
					"toast-type":"fail",
					"toast-message":"비밀번호가 서로 일치되어야 합니다."
				})
				$(".account-container-body-input-field-input[name='member-pw']").addClass("selected");
				$(".account-container-body-input-field-input[name='member-pw-check']").addClass("selected");
				return;
			}
			
			$.ajax({
				"url":API_GATEWAY+"/api/v1/members/"+memberID,
				"headers":{
					"member-access-token":loadMemberAccessToken()
				},
				"type":"patch",
				"contentType":"application/json",
				"dataType":"json",
				"data":JSON.stringify({
					"member-pw":memberPW,
					"member-pw-check":memberPWCheck
				})
			}).done(function(response){
				openToast({
					"toast-type":"success",
					"toast-message":"회원 비밀번호가 성공적으로 변경되었습니다."
				})
			}).fail(function(xhr, status, error){
				var data = JSON.parse(xhr.responseText);
				const code = data.code;
				const message = data.message;
			})
		}else{
			var memberEmail = $(".account-container-body-input-field-input[name='member-email']").val();
			
			if(!checkMemberEmail(memberEmail)){
				openToast({
					"toast-type":"fail",
					"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
				})
				$(".account-container-body-input-field-input[name='member-email']").addClass("selected");
				return;
			}
			
			$.ajax({
				"url":API_GATEWAY+"/api/v1/members/"+memberID,
				"headers":{
					"member-access-token":loadMemberAccessToken()
				},
				"type":"patch",
				"contentType":"application/json",
				"dataType":"json",
				"data":JSON.stringify({
					"member-email":memberEmail+"@"+$("select[name='member-email-domain']").val()
				})
			}).done(function(response){
				openToast({
					"toast-type":"success",
					"toast-message":"회원 이메일이 성공적으로 변경되었습니다."
				})
			}).fail(function(xhr, status, error){
				var data = JSON.parse(xhr.responseText);
				const code = data.code;
				const message = data.message;
				if(code=="MEMBER_EMAIL_NOT_VERIFIED"){
					$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
				}else if(code=="MEMBER_EMAIL_ALREADY_OCCUPIED"){
					$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
				}
				
				openToast({
					"toast-type":"fail",
					"toast-message":message
				})
			})
		}
	});
})