// 수정 클릭시
function goUpdate() {	
	if($("#domainId").val() != "") {
		// 입력 폼 검사
		if(checkForm()) {
			return;
		}
		
		var param = $("#domainInfoForm").serialize();
		$.getJSON("./domainUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정이 완료되었습니다");
				$("#page").val("1");
				$("#domainInfoForm").attr("target","").attr("action","./domainListP.ums").submit();
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("도메인 아이디가 없습니다!");	
	}
} 

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	
	if($("#domainName").val() == "") {
		errstr += "[도메인이름]"; 
		errflag = true;
	}
	 
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}	
		 
	return errflag;
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#domainInfoForm").attr("target","").attr("action","./domainListP.ums").submit();
}
