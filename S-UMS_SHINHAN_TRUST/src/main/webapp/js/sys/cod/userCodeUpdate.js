// 수정 클릭시
function goUpdate() {
	if($("#cd").val() != "") {
		// 입력 폼 검사
		if(checkForm()) {
			return;
		}
		var page = $("#userCodeInfoForm input[name='page']").val();
		console.log ("page : " + page);
		var param = $("#userCodeInfoForm").serialize();
		console.log (param);
		$.getJSON("./userCodeUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정 성공 하였습니다");
				$("#page").val(page);
				$("#userCodeInfoForm").attr("target","").attr("action","./userCodeListP.ums").submit();
			} else {
				alert("수정 실패하였습니다");
			}
		});
	} else {
		alert("코드 선택");	
	}
} 

// 수정 폼 리셋
function goReset() {
	$(cdNm).val("");
	$(cdDtl).val("");
	$(upCd).val("");
	$(uiLang).val("000");
	$(sysYn).val("Y");
	$(useYn).val("Y");
}

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	if($("#cdNm").val() == "") {
		errstr += "[코드명]"; 
		errflag = true;
	} 
 
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	if($.byteString($("#cdNm").val()) > 60 ) {
		alert("코드명은 60byte를 넘을 수 없습니다.");
		$("#cdNm").focus();
		$("#cdNm").select();
		errflag = true;
	}
	if($.byteString($("#cdDtl").val()) > 100 ) {
		alert("코드 설명은 100byte를 넘을 수 없습니다.");
		$("#cdDtl").focus();
		$("#cdDtl").select();
		errflag = true;
	} 
		
	return errflag;
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#userCodeInfoForm").attr("target","").attr("action","./userCodeListP.ums").submit();
}
