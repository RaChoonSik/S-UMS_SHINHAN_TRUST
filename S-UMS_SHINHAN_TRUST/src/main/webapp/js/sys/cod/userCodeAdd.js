// 코드상세정보의 분류 코드 변경시 연결된 상위코드 목록 조회
function searchUpCdSelect(upCd) { 	

	$.getJSON("/sys/cod/userCodeListByCodeGroup.json?cdGrp=" + upCd, function(data) {
		$("#upCd").children("option:not(:first)").remove();
		$.each(data.upCdGrpList, function(idx,item){
			var option = new Option(item.cdNm,item.cd);
			$("#upCd").append(option);
		});
	});
}  
// 등록 클릭시
function goAdd() {
 
	if(checkForm()) {
		return;
	}
		
	var param = $("#userCodeInfoForm").serialize();
	
	$.getJSON("/sys/cod/userCodeAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("등록 성공 하였습니다");
			//코드명, 상위 코드, 사용여부, 설명  
			$("#upCd option:eq(0)").prop("selected", true);
			$("#useYn option:eq(0)").prop("selected", true); 
			$("#cdDtl").val("");
			$("#cdNm").val("");
					
		} else {
			alert("등록에 실패하였습니다");
		}
	});
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
		alert("코드설명은 100byte를 넘을 수 없습니다.");
		$("#cdDtl").focus();
		$("#cdDtl").select();
		errflag = true;
	} 
		
	return errflag;
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#searchCdGrp").val($("#cdGrp").val());
	
	$("#userCodeInfoForm").attr("target","").attr("action","./userCodeListP.ums").submit();
}
