// 등록 클릭시
function goAdd() {
 
	if(checkForm()) {
		return;
	}
		
	var param = $("#domainInfoForm").serialize();
	console.log(param);
	$.getJSON("./domainAdd.json?" + param, function(data) {
		if(data.result == "Success") { 
			$("#domainInfoForm").attr("target","").attr("action","./domainListP.ums").submit();
		} else {
			alert("등록에 실패하였습니다");
		}
	});
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
