 
function goUpdate() {	
	
	if($("#userId").val() != "") {
	 
		if(checkForm()) {
			return;
		}
		
		if(checkData()){
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
		  
		$("#userInfoForm input[name='serviceGb']").val(services);
		
		var param = $("#userInfoForm").serialize();
		$.getJSON("./userUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정이 완료되었습니다");
				$("#page").val("1");
				$("#userInfoForm").attr("target","").attr("action","./userListP.ums").submit();
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("사용자 아이디가  없습니다!");	
	}
} 

// 입력 폼 검사
function checkForm() {

	var errstr = "";
	var errflag = false;
  
	if($("#userNm").val() == "") {
		errstr += "[사용자명]"; 
		errflag = true;
	} 
	
	if($("#userEm").val() == "") {
		errstr += "[이메일]"; 
		errflag = true;
	}
	
	if($("#userTel").val() == "") {
		errstr += "[연락처]"; 
		errflag = true;
	}

	if($("#mailFromNm").val() == "") {
		errstr += "[발송자명]"; 
		errflag = true;
	}
	
	if($("#mailFromEm").val() == "") {
		errstr += "[발송자이메일]"; 
		errflag = true;
	}	
		
	if($("#replyToEm").val() == "") {
		errstr += "[회신이메일]"; 
		errflag = true;
	}

	if($("#returnEm").val() == "") {
		errstr += "[RETURN이메일]"; 
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
		
	if($.byteString($("#userNm").val()) > 38 ) {
		alert("사용자명은 40byte를 넘을 수 없습니다.");
		$("#userNm").focus();
		$("#userNm").select();
		errflag = true;
	}
	if($.byteString($("#userDesc").val()) > 380 ) {
		alert("사용자설명은 400byte를 넘을 수 없습니다.");
		$("#userDesc").focus();
		$("#userDesc").select();
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

function setSysUse()
{ 	
	if ( $('input:checkbox[id="chkUseSys"]').is(":checked") == true ){
		if(confirm("관리자 기능입니다 설정하시겠습니까?")) {
			$('input:checkbox[id="chkUseSys"]').prop("checked", true);
		} else {
			$('input:checkbox[id="chkUseSys"]').prop("checked", false); 
		}
	} else {
		if(confirm("관리자 기능 미사용으로 설정하시겠습니까?")) {
			$('input:checkbox[id="chkUseSys"]').prop("checked", false);
		} else {
			$('input:checkbox[id="chkUseSys"]').prop("checked", true);
		}		
	}
}

function resetPassword(){
	
	$("#passwordMessage").text("");
	var param = $("#userInfoForm").serialize(); 
	$.getJSON("./userResetPassword.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("비밀번호가 [" + data.initWord + "]로 초기화되었습니다" ); 
			$("#passwordMessage").text(""); 
			$("#curPwd").val("");
			$("#userPwd").val("");
			$("#userPwdChk").val("");
			$("#divEditPw").css("display", "none"); 
		} else {
			alert("비밀번호 초기화에 실패했습니다");
		}
	});
}

function savePassword(){
	
	if( checkPassword()) {  
		$("#passwordMessage").text("");
		var param = $("#userInfoForm").serialize(); 
		$.getJSON("./userUpdatePassword.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("비밀번호가 변경되었습니다"); 
				$("#passwordMessage").text(""); 
				$("#curPwd").val("");
				$("#userPwd").val("");
				$("#userPwdChk").val("");
				$("#divEditPw").css("display", "none"); 
			} else {
				alert("비밀번호 변경에 실패했습니다");
			}
		}); 
	}
}

function checkPassword(){

	var pw = $("#userPwd").val();
	var chkPw = $("#userPwdChk").val()
	$("#passwordMessage").text("");
	
	if (pw != chkPw) {
		$("#passwordMessage").text("*비밀번호가 일치하지 않습니다"); 
		$("#userPwdChk").focus();
		$("#userPwdChk").select();
		return false;
	}
	
	var num = pw.search(/[0-9]/g);
	var eng = pw.search(/[a-z]/ig);
	var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	
	if(pw.length < 8 || pw.length > 20){
		$("#passwordMessage").text("*8자리 ~ 20자리 이내로 입력해주세요."); 
		$("#userPwd").focus();
		$("#userPwd").select();
		return false;
	}else if(pw.search(/\s/) != -1){	
		$("#passwordMessage").text("*비밀번호는 공백 없이 입력해주세요.");
		$("#userPwd").focus();
		$("#userPwd").select();
		
		return false;
	}else if( (num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0) ){		
		$("#passwordMessage").text("*영문,숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.");
		$("#userPwd").focus();
		$("#userPwd").select();
		
		return false;
	}else {
		return true;
	} 
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
	$("#userInfoForm").attr("target","").attr("action","./userListP.ums").submit();
}
