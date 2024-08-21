/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.11.17
*	설명 : 실시간 이메일발송결재 결재처리 JavaScript
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
		$("#apprUserId").val( $("rnsAprForm input[name='tempUserId']").eq(apprStep-1).val() );
	} else {
		$("#apprUserId").val( $("rnsAprForm input[name='tempUserId']").eq(apprStep).val() );
	}
	
	if(confirm("결재를 승인합니다.")) {
		var param = $("#rnsAprForm").serialize();
		$.getJSON("./rnsAprStepUpdate.json?" + param, function(res) {
			if(res.result == "Success") {
				alert("승인되었습니다.");
				
				if(apprStep == totalCount) aprStepFinished = true;
				
				// 결재목록 다시 표시
				$.ajax({
					type : "POST",
					url : "./rnsAprStepListP.ums?" + param,
					dataType : "html",
					success : function(pageHtml){
						$("#divRnsApprStepList").html(pageHtml);
					},
					error : function(){
						alert("List Data Error!!");
					}
				});
				
				// 결재 Alert
				$("#rnsAprForm").attr("target","iFrmMail").attr("action","./APPROVAL_ALERT.jsp").submit();
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
	$("#apprUserId").val( $("#rnsAprForm input[name='tempUserId']").eq(rejectApprStep-1).val() );
	
	if(confirm("결재를 반려합니다.")) {
		var param = $("#rnsAprForm").serialize();
		$.getJSON("./rnsAprStepUpdate.json?" + param, function(res) {
			if(res.result == "Success") {
				alert("반려되었습니다.");
				aprStepRejected = true;
				
				fn.popupClose('#popup_reject_reason');

				// 결재목록 다시 표시
				$.ajax({
					type : "POST",
					url : "./rnsAprStepListP.ums?" + param,
					dataType : "html",
					success : function(pageHtml){
						$("#divRnsApprStepList").html(pageHtml);
					},
					error : function(){
						alert("List Data Error!!");
					}
				});
				
				// 결재 Alert
				$("#rnsAprForm").attr("target","iFrmMail").attr("action","./APPROVAL_ALERT.jsp").submit();
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

//테스트 센드 관련 처리

// 테스트발송 클릭시
function popRnsTestSend() {
	$("#tidTest").val( $("#tid").val() );
	
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
	$.getJSON("/rns/svc/serviceTestSend.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("테스트메일이 발송 되었습니다.");
			fn.popupClose('#popup_testsend_user');
		} else if(data.result == "Fail") {
			alert("테스트메일이 발송중 오류가 발생하였습니다.");
		}
	});
}
