/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.13
*	설명 : 캠페인별 메일 목록 JavaScript
**********************************************************/

$(document).ready(function() {
	// 메일 목록 화면 호출
	goSearch("1");
});

// 검색버튼 클릭시(캠페인별 메일 목록 조회)
function goSearch(pageNum) {
	$("#page").val(pageNum);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./campMailList.ums?" + param,
		dataType : "html",
		//async: false,
		success : function(pageHtml){
			$("#divCampMailList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 초기화 버튼 클릭시
function goInit() {
	$("#searchForm")[0].reset();
}

// 목록에서 전체 선택
function goAll() {
	if($("#searchForm input[name='isAll']").is(":checked") == true) {
		$("#searchForm input[name='taskNos']").prop("checked", true);
		$("#searchForm input[name='subTaskNos']").prop("checked", true);
		$("#searchForm input[name='workStatus']").prop("checked", true);
	} else {
		$("#searchForm input[name='taskNos']").prop("checked", false);
		$("#searchForm input[name='subTaskNos']").prop("checked", false);
		$("#searchForm input[name='workStatus']").prop("checked", false);
	}
}

// 목록에서 체크박스 클릭시 taskNo와 subTaskNo, workStatus를 같이 체크해 준다.
function goTaskNo(i) {
	var isChecked = $("#searchForm input[name='taskNos']").eq(i).is(":checked");
	$("#searchForm input[name='subTaskNos']").eq(i).prop("checked", isChecked);
	$("#searchForm input[name='workStatus']").eq(i).prop("checked", isChecked);
}

// 삭제된 목록의 체크박스 클릭시..
function goDeleteClick() {
	alert("삭제된 목록은 선택할 수 없습니다.");
	return false;
}

// 결재취소 클릭시
function goApprCancel() {
	//선택항목체크
	var isChecked = false;
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});
	if(!isChecked) {
		alert("결재취소 할 목록을 선택해 주세요!!");
		return;
	}

	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			if( !($("#searchForm input[name='workStatus']").eq(idx).val() == "202" && $("#searchForm input[name='apprProcYn']").eq(idx).val() == "N") ) {
				$("#searchForm input[name='taskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", false);
				invalidStatus = true;
			}
		}
	});
	if(invalidStatus) {
		alert("결재취소는 결재진행 상태에서 결재된 건이 없을 경우만 가능합니다.");
		return;
	}
	
	$("#status").val("000");
	var param = $("#searchForm").serialize();
	$.getJSON("./apprCancel.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("결재취소되었습니다.");
			
			// 메일 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("결재취소 처리중 오류가 발생하였습니다.");
		}
	});
}

// 테스트발송 클릭시(ums.common.js 에서 나머지 처리)
function popTestSend() {
	var checkedCount = 0;
	var statusError = false;

	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			if("000,201,202".indexOf($("#searchForm input[name='workStatus']").eq(idx).val()) < 0) {
				$("#searchForm input[name='taskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", false);
				statusError = true;
			}
			checkedCount++;
		}
	});
	if(statusError) {
		alert("테스트발송이 불가한 상태입니다.");
		return;
	}
	if(checkedCount == 0 || checkedCount > 1) {
		alert("테스트 발송할 목록을 하나만 선택해 주세요.!!");
		return;
	}
	
	$("#testTaskNos1").val( $("#searchForm input[name='taskNos']:checked").val() );
	$("#testSubTaskNo1").val( $("#searchForm input[name='subTaskNos']:checked").val() );
	$("#testTaskNos2").val( $("#searchForm input[name='taskNos']:checked").val() );
	$("#testSubTaskNo2").val( $("#searchForm input[name='subTaskNos']:checked").val() );
	
	getTestUserList();
	
	fn.popupOpen('#popup_testsend_user');
}

// 복사 클릭시
function goCopy() {
	var checkedCount = 0;

	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) checkedCount++;
	});
	if(checkedCount == 0 || checkedCount > 1) {
		alert("복사할 목록을 하나만 선택해 주세요.!!");
		return;
	}

	var param = $("#searchForm").serialize();
	$.getJSON("./mailCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
			
			// 메일 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
}

// 사용중지 클릭시
function goDisable() {
	var isChecked = false;
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});

	if(!isChecked) {
		alert("사용중지 할 목록을 선택해 주세요!!");
		return;
	}
	
	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			if("000,001,201".indexOf($("#searchForm input[name='workStatus']").eq(idx).val()) < 0) {
				$("#searchForm input[name='taskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", false);
				invalidStatus = true;
			}
		}
	});
	if(invalidStatus) {
		alert("결재대기, 발송대기, 발송승인 상태에서만 사용중지 가능합니다.");
		return;
	}

	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("사용중지되었습니다.");
			
			// 메일 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 클릭시
function goDelete() {
	var isChecked = false;
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});

	if(!isChecked) {
		alert("삭제할 목록을 선택해 주세요!!");
		return;
	}
	
	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			if("000,001,201".indexOf($("#searchForm input[name='workStatus']").eq(idx).val()) < 0) {
				$("#searchForm input[name='taskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", false);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", false);
				invalidStatus = true;
			}
		}
	});
	if(invalidStatus) {
		alert("결재대기, 발송대기, 발송승인 상태에서만 삭제 가능합니다.");
		return;
	}

	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			
			// 메일 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.")
		}
	});
}

// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동
function goUpdate(taskNo, subTaskNo, sendRepeat) {
	$("#taskNo").val( taskNo );
	$("#subTaskNo").val( subTaskNo );
	if(sendRepeat == "000") {
		//$("#searchForm").attr("target","").attr("action","./mailUpdateP.ums").submit();
		$("#searchForm").attr("target","").attr("action","./taskUpdateP.ums").submit();
	} else {
		$("#searchForm").attr("target","").attr("action","./taskUpdateP.ums").submit();
	}
}

// 목록에서 발송상태(발송대기) 클릭시 -> 발송승인
function goAdmit(i) {
	$("#taskNo").val( $("#searchForm input[name='taskNos']").eq(i).val() );
	$("#subTaskNo").val( $("#searchForm input[name='subTaskNos']").eq(i).val() );

	var a = confirm("발송 하시겠습니까?");
	if ( a ) {
		var param = $("#searchForm").serialize();
		$.getJSON("./mailAdmit.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("발송승인 되었습니다.");
				
				// 메일 목록 재조회;
				goSearch('1');
			} else if(data.result == "Fail") {
				alert("발송승인 처리중 오류가 발생하였습니다.");
			}
		});
	} else return;
}

// 목록에서 발송상태(결재대기) 클릭시(결재라인정보 팝업 오픈)
function popSubmitApproval(taskNo, subTaskNo, userId) {
	var param = "taskNo=" + taskNo + "&subTaskNo=" + subTaskNo + "&userId=" + userId;
	$.ajax({
		type : "GET",
		url : "./pop/popApprList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#popApprovalInfo").html(pageHtml);
		},
		error : function(){
		}
	});
	
	fn.popupOpen('#popup_mail_approval_info');
}

// 목록에서 발송상태(결재대기) 클릭시(상신) -> 결재대기
function goSubmitApproval(taskNo, subTaskNo) {
	if(confirm("보안결재를 상신 합니다.")) {
		var param = "taskNos=" + taskNo + "&subTaskNos=" + subTaskNo + "&status=202";
		$.getJSON("./mailSubmitApproval.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("상신 되었습니다.");
				fn.popupClose('#popup_mail_approval_info');
				// 결재 메시지 전달
				var apprUserId = data.apprUserId;
				var mailTitle = data.mailTitle;
				iFrmMail.location.href = "../apr/APPROVAL_ALERT.jsp?rsltCd=001&apprUserId=" + apprUserId + "&mailTitle=" + encodeURIComponent(mailTitle) + "&mailTypeNm=" + encodeURIComponent("대용량");
				
				goSearch($("#searchForm input[name='page']").val());
			} else if(data.result == "Fail") {
				alert("상신 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 목록에서 발송상태(발송실패) 클릭시
function goFail(str) {
	alert(str);
	return;
}


// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}

function goSubmit(taskNo, subTaskNo,workStatus,approvalProcAppYn) {
	$("#taskNo").val( taskNo );
	$("#subTaskNo").val( subTaskNo );
	$("#approvalProcAppYn").val(approvalProcAppYn);
	
	if(workStatus=="000" || workStatus=="201") {	
		$("#searchForm").attr("target","").attr("action","/ems/cam/taskUpdateP.ums").submit();
	} else {
		if(workStatus=="202" && approvalProcAppYn == "N") {
			$("#searchForm").attr("target","").attr("action","/ems/cam/taskUpdateP.ums").submit();	
		} else {
			$("#searchForm").attr("target","").attr("action","/ems/cam/taskUpdateDateP.ums").submit();
		}
	}
}

// 목록 클릭시
function goList() {
	$("#searchStartDt").val($("#orgSearchStartDt").val());
	$("#searchEndDt").val($("#orgSearchEndDt").val());
	$("#searchUserId").val($("#orgSearchUserId").val());
	$("#searchDeptNo").val($("#orgSearchDeptNo").val());
	$("#searchCampNm").val($("#orgSearchCampNm").val());
	$("#searchCampTy").val($("#orgSearchCampTy").val());
	$("#searchStatus").val($("#orgSearchStatus").val());
	$("#searchForm").attr("target","").attr("action","./campListP.ums").submit();
}

