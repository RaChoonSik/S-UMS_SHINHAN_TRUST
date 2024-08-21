// 수정 클릭시
function goUpdate() {	
	if($("#updOrgCd").val() != "") {
		// 입력 폼 검사
		if(checkForm()) {
			return;
		}
		
		var orgCd, orgNm, upOrgCd; 
		
		upOrgCd =  $("#orgCd").val();  //상위부서 
		orgNm =  $("#updOrgNm").val(); //변경할 부서이름
		orgCd =  $("#updOrgCd").val(); //변경할 부서 코드 
		  
		$("#upOrgCd").val(upOrgCd) ; 
		$("#orgCd").val(orgCd) ;
		$("#orgEngNm").val(orgNm) ;
				   
		var param = $("#orgInfoForm").serialize();
		$.getJSON("./orgUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정이 완료되었습니다");
				$("#orgInfoForm").attr("target","").attr("action","./orgListP.ums").submit();
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("부서 코드번호가 없습니다!");	
	}
} 

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	if($("#updOrgNm").val() == "") {
		errstr += "[부서명]"; 
		errflag = true;
	} 
	
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	if($.byteString($("#updOrgNm").val()) > 38 ) {
		alert("부서명 40byte를 넘을 수 없습니다.");
		$("#updOrgNm").focus();
		$("#updOrgNm").select();
		errflag = true;
	} 
		
	return errflag;
}


//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#orgInfoForm").attr("target","").attr("action","./orgListP.ums").submit();
}
