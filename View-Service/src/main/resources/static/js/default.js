const HTTP = "http";
const DOMAIN = "localhost";
const PORT = "3000";
const API_GATEWAY = HTTP+"://"+DOMAIN+":"+PORT;

function checkMemberEmailVerificationCode(memberEmailVerificationCode){
	const exp = /^[0-9]{6,6}$/;
	return exp.test(memberEmailVerificationCode);
}

function checkMemberID(memberID){
	const exp = /^[a-z]+[a-z0-9]{5,15}$/g;
	return exp.test(memberID);
}

function checkMemberPW(memberPW){
	const exp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
	return exp.test(memberPW);
}

function checkMemberName(memberName){
	const exp = /^[가-힣]{2,5}$/;
	return exp.test(memberName);
}

function checkMemberEmail(memberEmail){
	const exp = /^[a-z]+[a-z0-9]{5,15}$/g;
	return exp.test(memberEmail);
}

function checkBookName(bookName){
	const exp = /^[ㄱ-ㅎ가-힣a-zA-Z0-9\`\~\!\@\#\$\%\^\&\*\(\)\-\_\=\+\,\.\?\*\s\:\;]{1,40}$/g;
	return exp.test(bookName);
}

function checkBookPublisherName(bookPublisherName){
	const exp = /^[ㄱ-ㅎ가-힣a-zA-Z0-9\`\~\!\@\#\$\%\^\&\*\(\)\-\_\=\+\,\.\?\*\s\:\;]{1,40}$/g;
	return exp.test(bookPublisherName);
}

function checkBookPlace(bookPlace){
	const exp = /^[ㄱ-ㅎ가-힣a-zA-Z0-9\`\~\!\@\#\$\%\^\&\*\(\)\-\_\=\+\,\.\?\*\s\:\;]{1,40}$/g;
	return exp.test(bookPlace);
}

function checkBookDescription(bookDescription){
	const exp = /^[ㄱ-ㅎ가-힣a-zA-Z0-9\`\~\!\@\#\$\%\^\&\*\(\)\-\_\=\+\,\.\?\*\s\:\;]{1,1000}$/g;
	return exp.test(bookDescription);
}