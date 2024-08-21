/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.23
*	설명 : 단기메일 발송목록 JavaScript
**********************************************************/


$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

// 검색 버튼 클릭
function goSearch(pageNo) {
	if($("#searchStartDt").val() > $("#searchEndDt")) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");	// COMJSALT017
		return;
	}

	var endDt = $("#searchEndDt").val().split('.');
	var startDt = $("#searchStartDt").val().split('.');
	var endDate = new Date(endDt[0], endDt[1], endDt[2]);
	var startDate = new Date(startDt[0], startDt[1], startDt[2]);
	
	var diff = endDate - startDate;
	var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
	if ( parseInt(diff/currDay) > 365 ){
		alert("검색 기간은 1년을 넘길수 없습니다");
		return;
	}
		
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./mailList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 초기화 버튼 클릭
function goInit(adminYn, deptNo) {
	var date = new Date();
	var firstday = new Date(date.getFullYear(), date.getMonth(), "01");
	$(".datepickerrange.fromDate input").datepicker('setDate', firstday);
	$(".datepickerrange.toDate input").datepicker('setDate', new Date());
	$("#searchTaskNm").val("");
	$("#searchCampNo").val("0");
	if(adminYn == "Y") {
		$("#searchDeptNo").val("0");
	} else {
		$("#searchDeptNo").val(deptNo);
	}
	$("#searchUserId").val("");
	$("#searchStatus").val("");
	$("#searchWorkStatus").val("");
}

// 목록 전체 체크
function goAll() {
	if($("#searchForm input[name='isAll']").is(":checked") == true) {
		$("#searchForm input[name='taskNos']").each(function(idx,item){
			if($(item).is(":disabled") == false) {
				$("#searchForm input[name='taskNos']").eq(idx).prop("checked", true);
				$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", true);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", true);
			}
		});
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

// 삭제된 목록의 체크박스 클릭시
function goDeleteClick() {
	alert("삭제된 목록은 선택할 수 없습니다.");
	return false;
}

// 신규등록 클릭시
function goMailAdd() {
	document.location.href = "./mailAddP.ums";
	//$("#searchForm").attr("target","").attr("action","<c:url value='/ems/cam/mailAddP.ums'/>").submit();
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
	if(checkedCount == 0 || checkedCount > 1) {
		alert("테스트 발송할 목록을 하나만 선택해 주세요.!!");
		return;
	}
	if(statusError) {
		alert("테스트발송이 불가한 상태입니다.");
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
	// 선택항목체크
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
	// 선택항목체크
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
function goUpdate(taskNo, subTaskNo) {
	$("#taskNo").val( taskNo );
	$("#subTaskNo").val( subTaskNo );
	$("#searchForm").attr("target","").attr("action","./mailUpdateP.ums").submit();
}

// 목록에서 발송상태(발송실패) 클릭시
function goFail(str) {
	alert(str);
	return;
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

// 목록에서 발송상태(결재대기) 클릭시(상신) -> 결재대기
function goSubmitApproval(taskNo, subTaskNo) {
	if(confirm("보안결재를 상신 합니다.")) {
		var param = "taskNos=" + taskNo + "&subTaskNos=" + subTaskNo + "&status=202";
		$.getJSON("./mailSubmitApproval.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("상신 되었습니다.");
				
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

// 페이징
function goPageNumMail(page) {
	goSearch(page);
}

