// 등록 클릭시
function goAdd() {
 
	if(checkForm()) {
		return;
	}
		
	var param = $("#userCodeGroupInfoForm").serialize();
	
	$.getJSON("/sys/cod/userCodeGroupAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("등록에 성공 하였습니다");
			document.location.href= "./userCodeGroupListP.ums";
			$("#userCodeGroupInfoForm").attr("target","").attr("action","./userCodeGroupListP.ums").submit();
		} else {
			alert("등록에 실패 하였습니다");
		}
	});
} 

// 등록 폼 리셋
function goReset() {
	$(cdGrpNm).val("");
	$(cdGrpDtl).val("");
	$(upCdGrp).val("");
	$(uiLang).val("000");
	$(sysYn).val("Y");
	$(useY).val("Y");
}

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	if($("#cdGrpNm").val() == "") {
		errstr += "[분류명]"; 
		errflag = true;
	}
  
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	if($.byteString($("#cdGrpNm").val()) > 60 ) {
		alert("분류명은 60byte를 넘을 수 없습니다.");
		$("#cdGrpNm").focus();
		$("#cdGrpNm").select();
		errflag = true;
	}
	if($.byteString($("#cdGrpDtl").val()) > 100 ) {
		alert("분류설명은 100byte를 넘을 수 없습니다.");
		$("#cdGrpDtl").focus();
		$("#cdGrpDtl").select();
		errflag = true;
	} 
		
	return errflag;
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#userCodeGroupInfoForm").attr("target","").attr("action","./userCodeGroupListP.ums").submit();
}
