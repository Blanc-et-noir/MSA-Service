jQuery(function(){
	if(isLoggedIn()){
		location.href=API_GATEWAY+"/api/v1/views/home";
	}
	
	$(document).on("click","#join-button",function(e){
		location.href=API_GATEWAY+"/api/v1/views/join";
	});
	
	$(document).on("click","#login-button",function(e){
		location.href=API_GATEWAY+"/api/v1/views/login";
	});
	
	$(document).on("click",".find-container-header-button",function(e){
		const NAME = $(e.target).attr("name");
		var findIDButton = $(".find-container-header-button[name='find-id']");
		var findPWButton = $(".find-container-header-button[name='find-pw']");
		
		if(NAME=="find-id"){
			$(findIDButton).addClass("focused");
			$(findPWButton).removeClass("focused");
			$("#find-id-box").css({"display":"block"});
			$("#find-pw-box").css({"display":"none"});
		}else{
			$(findIDButton).removeClass("focused");
			$(findPWButton).addClass("focused");
			$("#find-pw-box").css({"display":"block"});
			$("#find-id-box").css({"display":"none"});
		}
	})
	
	$(document).on("click",".find-container-body-input-field-input",function(e){
		$(e.target).removeClass("selected");
	})
	
	$(document).on("click",".find-container-body-input-field-input-button[name='find']",function(e){
		const memberEmail = $("#member-email-input-for-find-member-id").val();
		
		if(!checkMemberEmail(memberEmail)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$("#member-email-input-for-find-member-id").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/members/member-emails/"+memberEmail+"@"+$("#member-email-domain-input-for-find-member-id").val(),
			"type":"get",
			"dataType" : "json"
		}).done(function(response){
			const memberID = response["data"]["member-id"];
			openToast({
				"toast-type":"success",
				"toast-message":"해당 이메일이 연동된 회원 아이디 정보를 조회하였습니다."
			})
			$("#member-id-input-for-find-member-id").val(memberID);
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
	
	$(document).on("click",".find-container-body-input-field-input-button[name='send']",function(e){
		const memberID = $("#member-id-input-for-find-member-pw").val();
		const memberEmail = $("#member-email-input-for-find-member-pw").val();
		
		if(!checkMemberID(memberID)){
			openToast({
				"toast-type":"fail",
				"toast-message":"아이디는 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$("#member-id-input-for-find-member-pw").addClass("selected");
			return;
		}
		
		if(!checkMemberEmail(memberEmail)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$("#member-email-input-for-find-member-pw").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/verifications/member-pws",
			"type":"post",
			"dataType" : "json",
			"data":JSON.stringify({
				"member-id":memberID,
				"member-email":memberEmail+"@"+$("#member-email-domain-input-for-find-member-pw").val()
			}),
			"contentType":"application/json"
		}).done(function(response){
			openToast({
				"toast-type":"success",
				"toast-message":"해당 이메일에 대한 인증코드를 발송했습니다. 이메일에 대한 인증코드는 5분간 유효합니다."
			})
		}).fail(function(xhr, status, error){
			const data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
			
		});
	});
	
	$(document).on("click",".find-container-body-input-field-input-button[name='verify']",function(e){
		const memberID = $("#member-id-input-for-find-member-pw").val();
		const memberEmail = $("#member-email-input-for-find-member-pw").val();
		const memberEmailVerificationCode = $(".find-container-body-input-field-input[name='member-email-verification-code']").val();
		
		if(!checkMemberID(memberID)){
			openToast({
				"toast-type":"fail",
				"toast-message":"아이디는 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$(".join-container-body-input-field-input[name='member-id']").addClass("selected");
			return;
		}
		
		if(!checkMemberEmail(memberEmail)){
			openToast({
				"toast-type":"fail",
				"toast-message":"이메일은 6자리 이상, 16자리 이하의 영어 및 숫자로 구성되어야 합니다."
			})
			$("#member-email-input-for-find-member-pw").addClass("selected");
			return;
		}
		
		if(!checkMemberEmailVerificationCode(memberEmailVerificationCode)){
			openToast({
				"toast-type":"fail",
				"toast-message":"인증코드는 6자리 숫자로 구성되어야 합니다."
			})
			$(".find-container-body-input-field-input[name='member-verification-code']").addClass("selected");
			return;
		}
		
		$.ajax({
			"url":API_GATEWAY+"/api/v1/verifications/member-pws",
			"type":"delete",
			"dataType" : "json",
			"data":JSON.stringify({
				"member-id":memberID,
				"member-email":memberEmail+"@"+$("#member-email-domain-input-for-find-member-pw").val(),
				"member-email-verification-code":memberEmailVerificationCode,
			}),
			"contentType":"application/json"
		}).done(function(response){
			openToast({
				"toast-type":"success",
				"toast-message":"해당 이메일에 대한 소유권 인증에 성공했습니다. "+memberID+" 에 대한 임시 PW가 "+memberEmail+"로 전송되었습니다."
			})
		}).fail(function(xhr, status, error){
			var data = JSON.parse(xhr.responseText);
			const code = data.code;
			const message = data.message;
			
			if(code=="MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND"){
				$("#member-email-input-for-find-member-pw").addClass("selected");
			}else if(code=="MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG"){
				$("#member-email-input-for-find-member-pw").addClass("selected");
			}
			
			openToast({
				"toast-type":"fail",
				"toast-message":message
			})
			
		});
	});
});