/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2022.02.18
*	설명 : 자동메일 서비스 부분수정 JavaScript
**********************************************************/

// 상태,발송상태,결재라인 변수
var rnsStatus = "";
var rnsWorkStatus = "";
var rnsApprLineYn = "";
var rnsApprProcYn = "";

$(document).ready(function(){
	//발송결재라인 등록 팝업 내 사용자
	$(document).on("click", ".content-area .user button", function(){
		if($(this).closest("li").hasClass("active")){
			$(this).closest("li").removeClass("active");
		}else{
			$(this).closest("li").addClass("active").siblings().removeClass("active");
		}
	});
	
	// 발송결재라인등록 팝업창에서 사용자명 입력 엔터키 처리
	$("#searchUserNm").keypress(function(e) {
		if(e.which == 13) {
			popSearchUser();
		}
	});
	
	//드래그앤드롭
	$("#apprUserList").sortable();
});

// 템플릿 파일을 읽어 편집기에 값 설정
function setServiceContent(contentsTyp, contentsPath) {
	if($.trim(contentsTyp) == "0" || $.trim(contentsTyp) == "2")
	$.getJSON("./serviceFileView.json?contentsPath=" + contentsPath, function(res) {
		if(res.result == 'Success') {
			$("#serviceContents").html( res.contVal );
		} else{
			$("#serviceContents").html("<span>메일 미리보기에 실패했습니다</span>");
		}
	});
}

// 웹에이전트 미리보기 클릭시
function popWebAgentPreview() {
	if($("#webAgentUrl").val() == "") {
		alert("등록된 보안메일정보가 없습니다.");
		return;
	}
	if($("#secuAttTyp").val() == "EXCEL") {
		alert("EXCEL은 미리보기를 할 수 없습니다.");
		return;
	}
	
	$("#iFrmWebAgent").empty();
	
	$("#previewWebAgentUrl").html($("#webAgentUrl").val());
	
	var param = $("#serviceInfoForm").serialize();
	$.getJSON("./webAgentPreview.json?" + param, function(res) {
		if(res.result == 'Success') {
			iFrmWebAgent.location.href = res.webAgentUrl;
			fn.popupOpen('#popup_preview_webagent');
		} else {
			alert("보안메일 미리보기 처리중 오류가 발생하였습니다.");
		}
	});
}

// 테스트발송 클릭시
function popRnsTestSend() {
	if($("#testSendAuth").val() == "N"){
		alert("테스트발송 권한이 없습니다");
		return;
	}
		
	$("#tidTest").val( $("#tids").val() );
	
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
	var param = $("#searchForm").serialize();
	$.getJSON("./serviceCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
}

// 사용중지 클릭시
function goDisable() {
	if(rnsWorkStatus == "202") {
		alert("결재대기, 결재반려, 결재완료 상태에서만 사용중지 가능합니다.");
		return;
	}
	
	$("#status").val("001");
	
	var param = $("#searchForm").serialize();
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("사용중지 되었습니다.");
			rnsStatus = "001";
			
			$("#btnDisable").hide();
			$("#btnEnable").removeClass("hide");
			$("#btnEnable").show();
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 복구 클릭시
function goEnable() {
	$("#status").val("000");
	
	var param = $("#searchForm").serialize();
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복구 되었습니다.");
			rnsStatus = "000";
			
			$("#btnEnable").hide();
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 클릭시
function goDelete() {
	if(rnsWorkStatus == "202") {
		alert("결재진행중인 항목은 삭제 하실 수 없습니다.");
		return;
	}
	$("#status").val("002");

	var param = $("#searchForm").serialize();
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제 되었습니다.");
			rnsStatus = "002";
			
			$("#searchForm").attr("target","").attr("action","./serviceListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.")
		}
	});
}
 
// 수정 클릭시 (서비스 수정)
function goServiceUpdate(status) {
	if(status == "002") {
		alert("삭제된 서비스입니다.\n삭제된 서비스는 수정할 수 없습니다.");
		return;
	}

	if($("#sname").val() == "") {
		alert("발송자명은 필수입력 항목입니다.");
		$("#sname").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("#smail").val() == "") {
		alert("발송자 이메일은 필수입력 항목입니다.");
		$("#smail").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("input[name='infoCheckYn']").eq(0).is(":checked")) {
		$("#titleChkYn").val("Y");
	} else {
		$("#titleChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(1).is(":checked")) {
		$("#bodyChkYn").val("Y");
	} else {
		$("#bodyChkYn").val("N");
	}
	
	if($("input[name='infoCheckYn']").eq(2).is(":checked")) {
		$("#attachFileChkYn").val("Y");
	} else {
		$("#attachFileChkYn").val("N");
	}	
		
	$("#serviceInfoForm").attr("target","iFrmService").attr("action","./serviceUpdateDate.ums").submit();
}

// 취소 클릭시(목록으로 이동)
function goServiceCancel() {
	$("#searchForm").attr("target","").attr("action","./serviceListP.ums").submit();
}

// 첨부파일 다운로드
function goFileDown(fileNm, filePath) {
	var param = "downType=009&attachNm=" + encodeURIComponent(fileNm) + "&attachPath=" + encodeURIComponent(filePath);
	iFrmService.location.href = "../../com/down.ums?" + param;
}

//준법심의 결과 보기
function goPopProbibitInfo(tid) {
	
	var param = "tid=" + tid;
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./pop/popRnsProhibitInfo.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopRnsProhibitInfo").html(pageHtml);
			fn.popupOpen("#popup_rns_prohibit_info");
		},
		error : function(){
			alert("준법심의 결과 조회에 실패했습니다!");
		}
	});
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
