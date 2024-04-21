function loadMemberAccessToken(){
	var memberAccessToken = localStorage.getItem("member-access-token");
	
	if(memberAccessToken!=null&&memberAccessToken!=undefined){
		return memberAccessToken;
	}else{
		return null;
	}
}

function loadMemberRefreshToken(){
	var memberRefreshToken = localStorage.getItem("member-refresh-token");
	
	if(memberRefreshToken!=null&&memberRefreshToken!=undefined){
		return memberRefreshToken;
	}else{
		return null;
	}
}

function saveMemberAccessToken(token){
	localStorage.setItem("member-access-token",token);
}

function saveMemberRefreshToken(token){
	localStorage.setItem("member-refresh-token",token);
}

function deleteMemberAccessToken(token){
	localStorage.removeItem("member-access-token",token);
}

function deleteMemberRefreshToken(token){
	localStorage.removeItem("member-refresh-token",token);
}

function hasMemberAccessToken(){
	var memberAccessToken = localStorage.getItem("member-access-token");
	
	if(memberAccessToken==null||memberAccessToken==undefined){
		return false;
	}else{
		return true;
	}
}

function hasMemberRefreshToken(){
	var memberRefreshToken = localStorage.getItem("member-access-token");
	
	if(memberRefreshToken==null||memberRefreshToken==undefined){
		return false;
	}else{
		return true;
	}
}

function readTokenHeader(token){
	return KJUR.jws.JWS.readSafeJSONString(b64utoutf8(token.split(".")[0]));
}

function readTokenPayload(token){	
	return KJUR.jws.JWS.readSafeJSONString(b64utoutf8(token.split(".")[1]));
}

function isTokenExpired(token){	
	var expirationDelay = 30;
	var now = Date.now() / 1000;
	var payload = readTokenPayload(token);
	var tokenExpirationTime = payload["exp"];
	
	if(now>tokenExpirationTime-expirationDelay){
		return true;
	}else{
		return false;
	}
}

function readMemberID(token){
	var payload = readTokenPayload(token);
	var memberID = payload["member-id"];
	return memberID;
}

function readMemberRole(token){	
	var payload = readTokenPayload(token);
	var memberRole = payload["member-role"];
	return memberRole;
}

function isMemberAccessToken(token){
	var payload = readTokenPayload(token);
	var tokenType = payload["token-type"];
	
	if(tokenType=="member-access-token"){
		return true;
	}else{
		return false;
	}
}

function isMemberRefreshToken(token){
	var payload = readTokenPayload(token);
	var tokenType = payload["token-type"];
	
	if(tokenType=="member-refresh-token"){
		return true;
	}else{
		return false;
	}
}

function isLoggedIn(){
	if(!hasMemberAccessToken()||!hasMemberRefreshToken()){
		deleteMemberAccessToken();
		deleteMemberRefreshToken();
		return false;
	}
	
	var memberAccessToken = loadMemberAccessToken();
	var memberRefreshToken = loadMemberRefreshToken();
	
	if(isTokenExpired(memberRefreshToken)){
		deleteMemberAccessToken();
		deleteMemberRefreshToken();
		return false;
	}
	
	if(!isTokenExpired(memberAccessToken)){
		return true;
	}
	
	var flag = false;
	
	$.ajax({
		"async":false,
		"url":API_GATEWAY+"/api/v1/tokens",
		"type":"put",
		"contentType":"application/json",
		"dataType":"json",
		"data":JSON.stringify({
			"member-access-token":memberAccessToken,
			"member-refresh-token":memberRefreshToken
		})
	}).done(function(response){
		flag = true;
		
		deleteMemberAccessToken();
		deleteMemberRefreshToken();
		
		saveMemberAccessToken(response["data"]["member-access-token"]);
		saveMemberRefreshToken(response["data"]["member-refresh-token"]);
	}).fail(function(xhr, status, error){
		deleteMemberAccessToken();
		deleteMemberRefreshToken();
	});
	
	return flag;
}
