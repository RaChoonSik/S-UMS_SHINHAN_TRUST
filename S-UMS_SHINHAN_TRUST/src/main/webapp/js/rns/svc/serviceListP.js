/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.01
*	설명 : 자동메일 서비스목록 JavaScript
**********************************************************/


$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

// 검색 버튼 클릭
function goSearch(pageNo) {
	if($("#searchStartDt").val() > $("#searchEndDt")) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");
		return;
	}
	

	var endDt = $("#searchEndDt").val().split('.');
	var startDt = $("#searchStartDt").val().split('.');
	var endDate = new Date(endDt[0], endDt[1], endDt[2]);
	var startDate = new Date(startDt[0], startDt[1], startDt[2]);
	/*
	var diff = endDate - startDate;
	var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
	if ( parseInt(diff/currDay) > 365 ){
		alert("검색 기간은 1년을 넘길수 없습니다");
		return;
	}
	*/
	
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./serviceList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divServiceList").html(pageHtml);
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
	$("#searchTnm").val("");
	if(adminYn == "Y") {
		$("#searchDeptNo").val("0");
	} else {
		$("#searchDeptNo").val(deptNo);
	}
	$("#searchUserId").val("");
	$("#searchContentsTyp").val("");
	$("#searchStatus").val("");
}

// 목록 전체 체크
function goAll() {
	if($("#searchForm input[name='isAll']").is(":checked") == true) {
		$("#searchForm input[name='tids']").each(function(idx,item){
			if($(item).is(":disabled") == false) {
				$("#searchForm input[name='tids']").eq(idx).prop("checked", true);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", true);
			}
		});
	} else {
		$("#searchForm input[name='tids']").prop("checked", false);
		$("#searchForm input[name='workStatus']").prop("checked", false);
	}
}

// 목록에서 체크박스 클릭시 tid와 subTaskNo, workStatus를 같이 체크해 준다.
function goTid(i) {
	var isChecked = $("#searchForm input[name='tids']").eq(i).is(":checked");
	$("#searchForm input[name='workStatus']").eq(i).prop("checked", isChecked);
}

// 삭제된 목록의 체크박스 클릭시
function goDeleteClick() {
	alert("삭제된 목록은 선택할 수 없습니다.");
	return false;
}

// 신규등록 클릭시
function goServiceAdd() {
	document.location.href = "./serviceAddP.ums";
}

// 결재취소 클릭시
function goApprCancel() {
	//선택항목체크
	var isChecked = false;
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});
	if(!isChecked) {
		alert("결재취소 할 목록을 선택해 주세요!!");
		return;
	}

	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) {
			if( !($("#searchForm input[name='workStatus']").eq(idx).val() == "202" && $("#searchForm input[name='apprProcYn']").eq(idx).val() == "N") ) {
				$("#searchForm input[name='tids']").eq(idx).prop("checked", false);
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


// 테스트발송 클릭시
function popRnsTestSend() {
	var checkedCount = 0;
	var statusError = false;

	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) {
			/*if("000,201,202".indexOf($("#searchForm input[name='workStatus']").eq(idx).val()) < 0) {
				$("#searchForm input[name='tids']").eq(idx).prop("checked", false);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", false);
				statusError = true;
			}
			*/
			checkedCount++;
		}
	});
	/*
	if(statusError) {
		alert("테스트발송이 불가한 상태입니다.");
		return;
	}
	*/
	if(checkedCount == 0 || checkedCount > 1) {
		alert("테스트 발송할 목록을 하나만 선택해 주세요.!!");
		return;
	}
		
	$("#tidTest").val( $("#searchForm input[name='tids']:checked").val() );
	
	fn.popupOpen('#popup_testsend_user');
}

// 테스트발송 팝업창에서 추가 클릭시
function goRnsTestUserAdd() {
	if($("#rid").val() == "") {
		alert("ID를 입력해주세요.");
		$("#rid").focus();
		return ;
	}
	if($("#rname").val() == "") {
		alert("수신자를 입력해주세요.");
		$("#rname").focus();
		return ;
	}
	if($("#rmail").val() == "") {
		alert("이메일을 입력해주세요.");
		$("#rmail").focus();
		return ;
	}
	
	// 기존 등록된 없을 경우 초기화
	if($("#rnsTestUserList input").length == 0) {
		$("#rnsTestUserList").empty();
	}
	var userHtml = "";
	userHtml += '<tr>';
	userHtml += '<td><label><input type="checkbox" name="rids" value="'+ $("#rid").val() +'|'+ $("#rname").val() +'|'+ $("#rmail").val() +'"><span></span></label></td>';
	userHtml += '<td>'+ $("#rid").val() +'</td>';
	userHtml += '<td>'+ $("#rname").val() +'</td>';
	userHtml += '<td>'+ $("#rmail").val() +'</td>';
	userHtml += '</tr>';
	
	$("#rnsTestUserList").append(userHtml);
	
	$("#rid").val("");
	$("#rname").val("");
	$("#rmail").val("");
}

// 테스트발송 팝업창에서 삭제 클릭시
function goRnsTestUserDelete() {
	$("#listForm input[name='rids']").each(function(idx,item){
		if($(item).is(":checked")) {
			$(item).closest("tr").remove();
		}
	});
	
	// 등록된 대상자가 없을 경우
	if($("#rnsTestUserList input").length == 0) {
		$("#rnsTestUserList").append('<tr><td colspan="4" class="no_data">등록된 내용이 없습니다.</td></tr>');
	}
}

// 테스트발송 팝업창에서 전체선택 클릭시
function goRnsUserAll() {
	$("#listForm input[name='rids']").each(function(idx,item){
		$(item).prop("checked", $("#listForm input[name='isUserAll']").is(":checked"));
	});
}

// 테스트발송 팝업창에서 테스트발송 클릭시
function goRnsTestMailSend() {
	var userChecked = false;
	$("#listForm input[name='rids']").each(function(idx,item){
		if($(item).is(":checked")) {
			userChecked = true;
		};
	});
	
	if(!userChecked) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}
	
	var param = $("#listForm").serialize();
	$.getJSON("./serviceTestSend.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("테스트메일이 발송 되었습니다.");
			fn.popupClose('#popup_testsend_user');
		} else if(data.result == "Fail") {
			alert("테스트메일이 발송중 오류가 발생하였습니다.");
		}
	});
}

// 복사 클릭시
function goCopy() {
	var checkedCount = 0;

	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) checkedCount++;
	});
	if(checkedCount == 0 || checkedCount > 1) {
		alert("복사할 목록을 하나만 선택해 주세요.!!");
		return;
	}

	var param = $("#searchForm").serialize();
	$.getJSON("./serviceCopy.json?" + param, function(data) {
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
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});

	if(!isChecked) {
		alert("사용중지 할 목록을 선택해 주세요!!");
		return;
	}

	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) {
			if("202" == ($("#searchForm input[name='workStatus']").eq(idx).val())) {
				$("#searchForm input[name='tids']").eq(idx).prop("checked", false);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", false);
				invalidStatus = true;
			}
		}
	});
	if(invalidStatus) {
		alert("결재진행중인 항목은 사용중지 하실 수 없습니다.");
		return;
	}
	
	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("사용중지되었습니다.");
			
			// 서비스 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 클릭시
function goDelete() {
	var isChecked = false;
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});

	if(!isChecked) {
		alert("삭제할 목록을 선택해 주세요!!");
		return;
	}

	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='tids']").each(function(idx,item){
		if($(item).is(":checked")) {
			if("202" == ($("#searchForm input[name='workStatus']").eq(idx).val())) {
				$("#searchForm input[name='tids']").eq(idx).prop("checked", false);
				$("#searchForm input[name='workStatus']").eq(idx).prop("checked", false);
				invalidStatus = true;
			}
		}
	});
	if(invalidStatus) {
		alert("결재진행중인 항목은 삭제하실수 없습니다.");
		return;
	}
	
	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			
			// 서비스 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.")
		}
	});
}

// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동
function goUpdate(tid) {
	$("#tid").val( tid );
	$("#searchForm").attr("target","").attr("action","./serviceUpdateP.ums").submit();
}


// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동 - 결재완료, 발송승인인 경우 
function goUpdateDate(tid, approvalProcAppYn) {
	$("#tid").val( tid );
	$("#approvalProcAppYn").val( approvalProcAppYn );
	$("#searchForm").attr("target","").attr("action","./serviceUpdateDateP.ums").submit();
}


// 목록에서 발송상태(결재대기) 클릭시(결재라인정보 팝업 오픈)
function popSubmitApproval(tid, userId) {
	var param = "tid=" + tid + "&userId=" + userId;
	$.ajax({
		type : "GET",
		url : "./pop/popRnsApprList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#popRnsApprovalInfo").html(pageHtml);
		},
		error : function(){
		}
	});
	
	fn.popupOpen('#popup_rns_approval_info');
}

// 목록에서 발송상태(발송대기) 클릭시 -> 발송승인
function goRnsAdmit(tid) {
	$("#tid").val( tid );
	$("#workStatus").val( "001" );
	
	var a = confirm("발송 하시겠습니까?");
	if ( a ) {
		var param = $("#searchForm").serialize();
		$.getJSON("./rnsAdmit.json?" + param, function(data) {
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

// 결재라인정보 팝업에서 결재상신 클릭시(상신) -> 결재대기
function goRnsSubmitApproval(tid) {
	if(confirm("보안결재를 상신 합니다.")) {
		var param = "tids=" + tid + "&status=202";
		$.getJSON("./rnsSubmitApproval.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("상신 되었습니다.");
				fn.popupClose('#popup_rns_approval_info');

				// 결재 메시지 전달
				var apprUserId = data.apprUserId;
				var mailTitle = data.mailTitle;
				iFrmService.location.href = "/ems/apr/APPROVAL_ALERT.jsp?rsltCd=001&apprUserId=" + apprUserId + "&mailTitle=" + encodeURIComponent(mailTitle) + "&mailTypeNm=" + encodeURIComponent("실시간");
				
				goSearch($("#searchForm input[name='page']").val());
			} else if(data.result == "Fail") {
				alert("상신 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 목록에서 발송상태 보기 
function popRnsApprovalState(tid) {
	
	var param = "tid=" + tid;
	$.ajax({
		type : "GET",
		url : "./pop/popRnsApprStateList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#popRnsApprovalState").html(pageHtml);
			fn.popupOpen('#popup_rns_approval_state');
		},
		error : function(){
			alert("결재상태 조회에 실패했습니다!");
		}
	});
}

// 페이징
function goPageNum(page) {
	goSearch(page);
}

