
// 사용자 아이디 체크(중복 확인)
function checkUserId() {	
 
	if($("#userId").val() == "") {
		alert("검색할 사용자 아이디를 입력해주세요");
		return;
	} 
	
	var reg_id =/^[A-za-z0-9]{5,12}$/;
	var userId= $("#userId").val();

	if(!reg_id.test(userId)) {
		alert("사용자ID는 영문 또는 숫자로 5~12자로 입력하셔야합니다");
		$("#userId").focus();
		$("#userId").select();		
		return; 
	}
	
	$.getJSON("/sys/acc/userIdCheck.json?userId=" + userId, function(data) {	
		if(data.result == "Success") {
			alert("사용가능한 아이디입니다.");	 
			$("#checkId").val("Y");
			$("#checkId").text("확인");
			$("#userId").attr("disabled",true);
			$("#checkId").attr("disabled",true);			
		} else if(data.result == "Fail") {
			alert("이미 사용중인 아이디입니다.");
		}
	}); 
}

function setSysUse()
{ 	
	if ( $('input:checkbox[id="chkUseSys"]').is(":checked") == true ){
		if(confirm("관리자 기능입니다 설정하시겠습니까?")) {
			$('input:checkbox[id="chkUseSys"]').prop("checked", true);
		} else {
			$('input:checkbox[id="chkUseSys"]').prop("checked", false); 
		}
	}
}

// 등록 클릭시
function goAdd() {  
	if(checkForm()) {
		return;
	} 
	
	if(checkData()) {
		return;
	}
	
	const query = 'input[name="service"]:checked';
  	const checkboxs = document.querySelectorAll(query);
  	
	var services="";

	if(checkboxs.length > 0 ){
		for (var i = 0; i < checkboxs.length; i++) {
        	services += checkboxs[i].value + ',';
    	}
	}  
    $("#userId").removeAttr("disabled");
	$("#userInfoForm input[name='serviceGb']").val(services);
	var param = $("#userInfoForm").serialize();
	 
	$.getJSON("/sys/acc/userAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			document.location.href= "./userListP.ums";
			$("#userInfoForm").attr("target","").attr("action","./userListP.ums").submit();
		} else {
			alert("등록에 실패하였습니다");
		}
	});
}


// 입력 폼 검사
function checkForm() {

	var errstr = "";
	var errflag = false;

	if($("#userId").val() == "") {
		errstr += "[사용자ID]"; 
		errflag = true;
	} 
		
	if($("#userNm").val() == "") {
		errstr += "[사용자명]"; 
		errflag = true;
	}
	
	if($("#userPwd").val() == "") {
		errstr += "[비밀번호]"; 
		errflag = true;
	}
	
	if($("#userPwdChk").val() == "") {
		errstr += "[비밀번호확인]"; 
		errflag = true;
	}	
	
	if($("#usertEm").val() == "") {
		errstr += "[사용자이메일주소]"; 
		errflag = true;
	}
	
	if($("#replyToEm").val() == "") {
		errstr += "[회신이메일주소]"; 
		errflag = true;
	}
	
	if($("#mailFromEm").val() == "") {
		errstr += "[발송자이메일]"; 
		errflag = true;
	}

	if($("#mailFromNm").val() == "") {
		errstr += "[발송자명]"; 
		errflag = true;
	}
 
	if($('#charset').val() == "0") {
		errstr += "[메일문자셋]";
		errflag = true;
	}
	
	if($('#tzCd').val() == "0") {
		errstr += "[타임존]";
		errflag = true;
	}
	
	if($('#uilang').val() == "0") {
		errstr += "[UI언어권]";
		errflag = true;
	}
 
	if($('#deptNo').val() == "0") {
		errstr += "[사용자그룹]";
		errflag = true;
	}
	
	if($("#orgKorNm").val() == "") {
		errstr += "[부서명]"; 
		errflag = true;
	}
	
	if($('#orgCd').val() == "0") {
		errstr += "[부서]";
		errflag = true;
	}	

	
	if($('#positionGb').val() == "0") {
		errstr += "[직급]";
		errflag = true;
	}
	
	if($('#jobGb').val() == "0") {
		errstr += "[직책]";
		errflag = true;
	} 

	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	
	return errflag;
}

function checkData(){ 
	var errflag = false;
	
	if($("#checkId").val() == "N") {
		alert("사용자ID 중복검사를 해야합니다 사용자ID옆의 검색 버튼을 클릭하여 ID중복검사를 해주세요\n");	
		errflag = true;
	}
		
	if($.byteString($("#userNm").val()) > 38 ) {
		alert("사용자명은 40byte를 넘을 수 없습니다.");
		$("#userNm").focus();
		$("#userNm").select();
		errflag = true;
	}
	if($.byteString($("#userDesc").val()) > 380 ) {
		alert("사용자 설명은 400byte를 넘을 수 없습니다.");
		$("#userDesc").focus();
		$("#userDesc").select();
		errflag = true;
	}
	
	if (checkPassword()){
		errflag = true;
	}
	
	if (checkEmail()){
		errflag = true;
	}
	
	if (checkTelNo()){
		errflag = true;
	}	
		
	return errflag;
}

function checkPassword(){

	var pw = $("#userPwd").val();
	var chkPw = $("#userPwdChk").val() 
	
	var num = pw.search(/[0-9]/g);
	var eng = pw.search(/[a-z]/ig);
	var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	var errflag = false;	
		
	if (pw != chkPw) {
		alert("입력하신 비빌먼호와 비밀번호 확인 내용이 일치하지 않습니다");
		$("#userPwdChk").focus();
		$("#userPwdChk").select();
		errflag = true;
	} else {
		if(pw.length < 8 || pw.length > 20){		
			alert("8자리 ~ 20자리 이내로 입력해주세요"); 
			$("#userPwd").focus();
			$("#userPwd").select();
			errflag = true;
		}else if(pw.search(/\s/) != -1){			
			alert("비밀번호는 공백 없이 입력해주세요");
			$("#userPwd").focus();
			$("#userPwd").select();
			
			errflag = true;
		}else if( (num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0) ){
			alert("영문,숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요");
			$("#userPwd").focus();
			$("#userPwd").select(); 
			errflag = true;
		}
	}
	return errflag;
}
 
function checkEmail(){
	
	var email ="";
	var errstr = "";
	var errflag = false;	
	var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
	
	email = $("#userEm").val()
	if(!reg_email.test(email)) {
		errstr += "[이메일]";
		errflag = true; 
	} 
	
	email = $("#mailFromEm").val()
	if(!reg_email.test(email)) {
		errstr += "[발송자이메일]";
		errflag = true; 
	} 
		 
	email = $("#replyToEm").val()
	if(!reg_email.test(email)) {
		errstr += "[회신이메일]";
		errflag = true; 
	}  
	
	email = $("#returnEm").val()
	if(!reg_email.test(email)) {
		errstr += "[RETURN이메일]";
		errflag = true; 
	}
	
	if(errflag) {
		alert("입력하신 이메일 주소는 유효하지 않습니다 이메일주소를 확인해주세요(예:gildong@sictglobal.com).\n" + errstr);
	}	
	 
	return errflag;
} 
 
function checkTelNo(){
	
	var telNo ="";
	var errstr = "";
	var errflag = false;	
	var reg_telNo =/^[0-9]{6,20}$/; 
	telNo = $("#userTel").val()
	if(!reg_telNo.test(telNo)) {
		errstr = "[연락처]";
		errflag = true; 
	} 
	 
	if(errflag) {
		alert("연락처는 6~20자리 이하 숫자만 가능합니다\n" + errstr);
	}	
	 
	return errflag;
}
 

//  취소 클릭시(리스트로 이동)
function goCancel() {	
	document.location.href= "./userListP.ums";
}
