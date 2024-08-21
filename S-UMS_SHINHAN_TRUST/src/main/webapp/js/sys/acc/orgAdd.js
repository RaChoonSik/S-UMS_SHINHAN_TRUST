// 등록 클릭시
function goAdd() {
 
	if(checkForm()) {
		return;
	}
	  
	var upOrgCd= $("#orgCd").val(); 
	$("#upOrgCd").val(upOrgCd) ;
	
	var param = $("#orgInfoForm").serialize();
	
	$.getJSON("/sys/acc/orgAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			document.location.href= "./orgListP.ums";
			$("#orgInfoForm").attr("target","").attr("action","./orgListP.ums").submit();
		} else {
			alert("등록에 실패하였습니다");
		}
	});
}  

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	
	if($("#orgNm").val() == "") {
		errstr += "[부서명]"; 
		errflag = true;
	} 
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
 
	return errflag;
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#orgInfoForm").attr("target","").attr("action","./orgListP.ums").submit();
}
