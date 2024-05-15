jQuery(function(){
	if(isLoggedIn()){
		location.href=API_GATEWAY+"/api/v1/views/home";
	}
	
	$(document).on("click","#login-button",function(e){
		location.href=API_GATEWAY+"/api/v1/views/login";
	});
	
	$(document).on("click","#find-button",function(e){
		location.href=API_GATEWAY+"/api/v1/views/find";
	});
	
	$(document).on("click",".join-container-body-input-field-input",function(e){
		$(e.target).removeClass("selected");
	})
	
	$(document).on("click",".join-container-body-input-field-input-button[name='send']",function(e){
		const memberEmail = $(".join-container-body-input-field-input[name='member-email']").val();
		
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
			"data":JSON.stringify({
				"member-email":memberEmail+"@"+$("select[name='member-email-domain']").val()
			}),
			"contentType":"application/json"
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
	
	$(document).on("click",".join-container-body-input-field-input-button[name='verify']",function(e){
		const memberEmail = $(".join-container-body-input-field-input[name='member-email']").val();
		const memberEmailVerificationCode = $(".join-container-body-input-field-input[name='member-email-verification-code']").val();
		
		if(!checkMemberEmail(memberEmail)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
			return;
		}
		
		if(!checkMemberEmailVerificationCode(memberEmailVerificationCode)){
			openToast({
				"toast-type":"fail",
				"toast-message":"인증코드는 6자리 숫자로 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-email-verification-code']").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/verifications/member-emails",
			"type":"delete",
			"dataType" : "json",
			"data":JSON.stringify({
				"member-email":memberEmail+"@"+$("select[name='member-email-domain']").val(),
				"member-email-verification-code":memberEmailVerificationCode
			}),
			"contentType":"application/json"
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
				$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
			}else if(code=="MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG"){
				$(".join-container-body-input-field-input[name='member-email-verification-code']").addClass("selected");
			}
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
		})
	});
	
	$(document).on("click","#join-button",function(e){
		const memberId = $(".join-container-body-input-field-input[name='member-id']").val();
		const memberPW = $(".join-container-body-input-field-input[name='member-pw']").val();
		const memberPWCheck = $(".join-container-body-input-field-input[name='member-pw-check']").val();
		const memberEmail = $(".join-container-body-input-field-input[name='member-email']").val();
		const memberName = $(".join-container-body-input-field-input[name='member-name']").val();
		
		if(!checkMemberID(memberId)){
			openToast({
				"toast-type":"fail",
				"toast-message":"아이디는 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-id']").addClass("selected");
			return;
		}
		
		if(!checkMemberPW(memberPW)){
			openToast({
				"toast-type":"fail",
				"toast-message":"비밀번호는 8자리 이상, 16자리 이하의 영어, 숫자 및 특수문자를 모두 포함하여 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-pw']").addClass("selected");
			return;
		}
		
		if(!checkMemberPW(memberPWCheck)){
			openToast({
				"toast-type":"fail",
				"toast-message":"비밀번호 확인은 8자리 이상, 16자리 이하의 영어, 숫자 및 특수문자를 모두 포함하여 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-pw-check']").addClass("selected");
			return;
		}
		
		if(memberPW!=memberPWCheck){
			openToast({
				"toast-type":"fail",
				"toast-message":"비밀번호가 서로 일치되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-pw']").addClass("selected");
			$(".join-container-body-input-field-input[name='member-pw-check']").addClass("selected");
			return;
		}
		
		if(!checkMemberName(memberName)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이름은 2자리 이상, 5자리 이하의 한글로 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-name']").addClass("selected");
			return;
		}
		
		if(!checkMemberEmail(memberEmail)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/members",
			"type":"post",
			"dataType" : "json",
			"data":JSON.stringify({
				"member-id":memberId,
				"member-pw":memberPW,
				"member-pw-check":memberPWCheck,
				"member-name":memberName,
				"member-email":memberEmail+"@"+$("select[name='member-email-domain']").val()
			}),
			"contentType":"application/json"
		}).done(function(response){
			location.href="http://localhost:3000/api/v1/views/home";
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			
			if(code=="MEMBER_ID_ALREADY_OCCUPIED"){
				$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
			}else if(code=="MEMBER_PW_NOT_MATCHED_TO_EACH_OTHER"){
				$(".join-container-body-input-field-input[name='member-pw']").addClass("selected");
				$(".join-container-body-input-field-input[name='member-pw-check']").addClass("selected");
			}else if(code=="MEMBER_EMAIL_NOT_VERIFIED"){
				$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
			}else if(code=="MEMBER_EMAIL_ALREADY_OCCUPIED"){
				$(".join-container-body-input-field-input[name='member-email']").addClass("selected");
			}
			
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
		})
	});
});