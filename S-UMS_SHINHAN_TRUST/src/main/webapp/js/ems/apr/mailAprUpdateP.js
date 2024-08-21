/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.30
*	설명 : 메일발송결재 결재처리 JavaScript
**********************************************************/

// 결재완료/반려 체크
var aprStepFinished = false;
var aprStepRejected = false;

// 결재 클릭시
function goApprStepConfirm(apprStep, totalCount) {
	$("#apprStep").val(apprStep);
	$("#totalCount").val(totalCount);
	$("#rsltCd").val("002");
	
	if(apprStep == totalCount) {
		$("#apprUserId").val( $("#mailAprForm input[name='tempUserId']").eq(apprStep-1).val() );
	} else {
		$("#apprUserId").val( $("#mailAprForm input[name='tempUserId']").eq(apprStep).val() );
	}
	
	if(confirm("결재를 승인합니다.")) {
		var param = $("#mailAprForm").serialize();
		$.getJSON("./mailAprStepUpdate.json?" + param, function(res) {
			if(res.result == "Success") {
				alert("승인되었습니다.");
				
				if(apprStep == totalCount) aprStepFinished = true;
				
				// 결재목록 다시 표시
				$.ajax({
					type : "POST",
					url : "./mailAprStepListP.ums?" + param,
					dataType : "html",
					success : function(pageHtml){
						$("#divMailApprStepList").html(pageHtml);
					},
					error : function(){
						alert("List Data Error!!");
					}
				});
				
				// 결재 Alert
				$("#mailAprForm").attr("target","iFrmMail").attr("action","./APPROVAL_ALERT.jsp").submit();
			} else {
				alert("승인 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 반려 클릭시
var rejectApprStep;
function popApprStepReject(apprStep) {
	rejectApprStep = apprStep;
	fn.popupOpen('#popup_reject_reason');
}

// 반려 팝업창에서 등록 클릭시
function goApprStepReject() {
	if($("#rejectCd").val() == "") {
		alert("반려사유를 선택해주세요.");
		return;
	}
	
	$("#apprStep").val(rejectApprStep);
	$("#rsltCd").val("003");
	$("#apprUserId").val( $("#mailAprForm input[name='tempUserId']").eq(rejectApprStep-1).val() );
	
	if(confirm("결재를 반려합니다.")) {
		var param = $("#mailAprForm").serialize();
		$.getJSON("./mailAprStepUpdate.json?" + param, function(res) {
			if(res.result == "Success") {
				alert("반려되었습니다.");
				aprStepRejected = true;
				
				fn.popupClose('#popup_reject_reason');

				// 결재목록 다시 표시
				$.ajax({
					type : "POST",
					url : "./mailAprStepListP.ums?" + param,
					dataType : "html",
					success : function(pageHtml){
						$("#divMailApprStepList").html(pageHtml);
					},
					error : function(){
						alert("List Data Error!!");
					}
				});
				
				// 결재 Alert
				$("#mailAprForm").attr("target","iFrmMail").attr("action","./APPROVAL_ALERT.jsp").submit();
			} else {
				alert("반려 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 반려 내용 확인
function  popRejectDisplay(rejectDt, rejectNm) {
	$("#txtRejectDt").html( rejectDt );
	$("#txtRejectNm").html( rejectNm );
	
	fn.popupOpen('#popup_reject_display');
}

// 테스트발송 클릭시(ums.common.js 에서 나머지 처리)
function popTestSend() {
	if(aprStepFinished) {
		alert("결재가 이미 완료되어 테스트가 불가합니다.");
		return;
	}
	if(aprStepRejected) {
		alert("결재가 이미 반려되어 테스트가 불가합니다.");
		return;
	}
	$("#testTaskNos1").val( $("#taskNo").val() );
	$("#testSubTaskNo1").val( $("#subTaskNo").val() );
	$("#testTaskNos2").val( $("#taskNo").val() );
	$("#testSubTaskNo2").val( $("#subTaskNo").val() );
	
	getTestUserList();
	
	fn.popupOpen('#popup_testsend_user');
}

// 목록 클릭시
function goList() {
	$("#searchForm").attr("target","").attr("action","./mailAprListP.ums").submit();
}

