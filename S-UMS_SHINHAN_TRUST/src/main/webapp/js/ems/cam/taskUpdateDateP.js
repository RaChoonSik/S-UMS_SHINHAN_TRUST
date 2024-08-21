/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.28
*	설명 : 메일발송 정보수정 JavaScript
**********************************************************/

// 상태,발송상태,결재라인 변수
var mailStatus = "";
var mailWorkStatus = "";
var mailApprLineYn = "";
var mailApprProcYn = "";

var dateRegex = RegExp(/^\d{4}.(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[01])$/);
var checkDateInput = false;

$(document).ready(function() {

	// 날짜 입력 체크
	$(".datepickerrange.fromDate input").on("keyup", function(){
		checkDateInput = true;
		$(this).val( $(this).val().replace(/[^0-9.]/gi,"") );
	});
	$(".datepickerrange.fromDate input").on("click", function(){
		checkDateInput = true;
	});
	$(".datepickerrange.fromDate input").on("blur", function(){
		var dateTxt = $(this).val();
		if(dateTxt.length == 8) {
			$(this).val( dateTxt.substring(0,4) + "." + dateTxt.substring(4,6) + "." + dateTxt.substring(6) );
		}
		if(!dateRegex.test($(this).val()) && checkDateInput == true) {
			alert("날짜형식이 올바르지 않습니다.");
			checkDateInput = false;
			$(this).select();
		}
	});
	
	$(".datepickerrange.toDate input").on("keyup", function(){
		checkDateInput = true;
		$(this).val( $(this).val().replace(/[^0-9.]/gi,"") );
	});
	$(".datepickerrange.toDate input").on("click", function(){
		checkDateInput = true;
	});
	$(".datepickerrange.toDate input").on("blur", function(){
		var dateTxt = $(this).val();
		if(dateTxt.length == 8) {
			$(this).val( dateTxt.substring(0,4) + "." + dateTxt.substring(4,6) + "." + dateTxt.substring(6) );
		}
		if(!dateRegex.test($(this).val()) && checkDateInput == true) {
			alert("날짜형식이 올바르지 않습니다.");
			checkDateInput = false;
			$(this).select();
		}
	});
   
	checkSendRepeat();
	
	// 예약일시,정기발송종료일 설정
	setTimeout(function(){
		$("#sendYmd").datepicker("setDate",sendYmd);
		$("#sendTermEndDt").datepicker("setDate",sendTermEndDt);
	},5);
});

// 메일 유형 값 변경시 
function checkSendRepeat() {
	var sendRepeat =  $("#txtSendRepeat").val();
	if(sendRepeat == "000") {
		$(".attr_disabled").attr("disabled",true);
	} else {
		$(".attr_disabled").removeAttr("disabled");
		//무기한 정기 메일일 경우 처리 
		if (sendTermEndDt == "9999.12.31"){
			$("#noLimitedCheck").prop("checked",true);
			checkNoLImited();
		} 

	}
}
 
// 환경설정 클릭시
function popEnvSetting() {
	if($("#envSetAuth").val() == "N"){
		alert("환경 설정 조회 권한이 없습니다");
		return;
	}	
	fn.popupOpen('#popup_setting');
}

// 환경설정 팝업창에서 등록 클릭시
function popSettingSave() {
	if($("#mailFromNm").val() == "") {
		alert("발송자명을 입력하세요.");
		$("#mailFromNm").focus();
		return;
	}
	if($("#mailFromEm").val() == "") {
		alert("발송자 이메일을 입력하세요.");
		$("#mailFromEm").focus();
		return;
	}
	if($("#replyToEm").val() == "") {
		alert("반송 이메일을 입력하세요.");
		$("#replyToEm").focus();
		return;
	}
	if($("#returnEm").val() == "") {
		alert("리턴 이메일을 입력하세요.");
		$("#returnEm").focus();
		return;
	}
	$("#txtMailFromNm").html( $("#mailFromNm").val() );
	
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

	alert("환경설정이 완료되었습니다.");
	fn.popupClose('#popup_setting');
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
	
	var param = $("#mailInfoForm").serialize();
	$.getJSON("./webAgentPreview.json?" + param, function(res) {
		if(res.result == 'Success') {
			iFrmWebAgent.location.href = res.webAgentUrl;
			fn.popupOpen('#popup_preview_webagent');
		} else {
			alert("보안메일 미리보기 처리중 오류가 발생하였습니다.");
		}
	});
}

// 웹에이전트 팝업창에서 취소 클릭시
function popWebAgentCancel() {
	$("#webAgentUrl").val("");
	$("#txtWebAgentUrl").html("형식이 지정되지 않았습니다.");
	$("#webAgentAttachYn").prop("checked", false);
	alert("보안메일 등록이 취소되었습니다.");
	fn.popupClose('#popup_web_agent');
}

// 테스트발송 클릭시(ums.common.js 에서 나머지 처리)
function popTestSend() {
	
	if($("#testSendAuth").val() == "N"){
		alert("테스트발송 할 수 있는 권한이 없습니다");
		return;
	}
	
	$("#testTaskNos1").val( $("#taskNo").val() );
	$("#testSubTaskNo1").val( $("#subTaskNo").val() );
	$("#testTaskNos2").val( $("#taskNo").val() );
	$("#testSubTaskNo2").val( $("#subTaskNo").val() );
	
	$("#testEaiTaskNos1").val(  $("#taskNo").val() );
	$("#testEaiSubTaskNo1").val(  $("#subTaskNo").val() )
	$("#testEaiTaskNos2").val(  $("#taskNo").val() );
	$("#testEaiSubTaskNo2").val(  $("#subTaskNo").val() )
		
	if ( $("#segCreateTy").val() == "002"){
		var campNo = $("#campNo").val();
		var campNm = $("#txtCampNm").text();
		var taskNm = $("#taskNm").val();

		$("#searchIfCampId").val(campNo);
		$("#searchIfCampNm").text(campNm);
		
		$("#popSendTestEaiUserCampNm").text(campNm);
		$("#popSendTestEaiUserTaskNm").text(taskNm);
		
		//고객정보 초기화 
		$("#popSendTestEaiUserSendResCd").text(""); 
		$("#popSendTestEaiUserIfId").text("");
		$("#popSendTestEaiUserIfEmail").text("");
		$("#popSendTestEaiUserIfName").text("");
		$("#popSendTestEaiUserIfMakeDate").text("");
		
		getTestEaiUserList();
		
		fn.popupOpen('#popup_testsend_user_eai');
	} else {
		getTestUserList();
	
		fn.popupOpen('#popup_testsend_user');
	}
}
 
// 사용중지 클릭시
function goDisable() {
	if(!(mailWorkStatus == "000" || mailWorkStatus == "001" || mailWorkStatus == "201")) {
		alert("결재대기, 발송대기, 발송승인 상태에서만 사용중지 가능합니다.");
		return;
	}
	
	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("사용중지 되었습니다.");
			mailStatus = "001";
			
			$("#btnDisable").hide();
			$("#btnEnable").removeClass("hide");
			$("#btnEnable").show();
			// 메일 목록 페이지로 이동
			//$("#searchForm").attr("target","").attr("action","./taskListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 복구 클릭시
function goEnable() {
	$("#status").val("000");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복구 되었습니다.");
			mailStatus = "000";
			
			$("#btnEnable").hide();
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();
			// 메일 목록 페이지로 이동
			//$("#searchForm").attr("target","").attr("action","./taskListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");	// 복구실패
		}
	});
}

// 삭제 클릭시
function goDelete() {
	if(!(mailWorkStatus == "000" || mailWorkStatus == "001" || mailWorkStatus == "201")) {
		alert("결재대기, 발송대기, 발송승인 상태에서만 삭제 가능합니다.");
		return;
	}
	
	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			mailStatus = "002";
			
			// 메일 목록 페이지로 이동
			$("#searchForm").attr("target","").attr("action","./taskListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("삭제중 오류가 발생하였습니다.");
		}
	});
}

// 복사 클릭시
function goCopy() {
	var param = $("#searchForm").serialize();
	$.getJSON("./mailCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
			
			// 메일 목록 페이지로 이동
			$("#searchForm").attr("target","").attr("action","./taskListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
}

// 수정 클릭시
function goUpdate() {

	if(mailStatus == "002") {
		alert("삭제된 메일입니다.\n삭제된 메일은 수정할 수 없습니다.");
		return;
	}
	 
	$("#sendTermEndDt").attr("disabled", false);
	$("#mailInfoForm").attr("target","iFrmMail").attr("action","./mailUpdateDate.ums").submit();
	$("#sendTermEndDt").attr("disabled",true);
}

// 취소 클릭시
function goCancel() {
	//alert("수정이 취소되었습니다.");
	if($("#searchForm input[name='campNo']").val() == "0") {
		$("#searchForm").attr("target","").attr("action","./taskListP.ums").submit();
	} else {
		$("#searchForm").attr("target","").attr("action","./campMailListP.ums").submit();
	}
}
 
// 수신자그룹 선택 클릭시(수신자그룹선택 팝업)
function popSegSelect() {
	$("#fileUploadSeg")[0].reset();
	$(".fileupload .inputfile ~ button.btn").click();

	$("#popSegSearchForm input[name='searchStartDt']").val("");
	$("#popSegSearchForm input[name='searchEndDt']").val("");
	$("#popSegSearchForm input[name='searchSegNm']").val("");
	goPopSegSearch("1");
	fn.popupOpen("#popup_segment");
}

// 팝업 수신자그룹 목록 검색
function goPopSegSearch(pageNum) {
	$("#popSegSearchForm input[name='page']").val(pageNum);
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popSegList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}
/*
// 팝업 수신자그룹 목록 페이징
function goPageNumPopSeg(pageNum) {
	goPopSegSearch(pageNum);
}
*/
 
// 무기한 체크
function checkNoLImited() {
	if($("#noLimitedCheck").is(":checked")) {
		$("#sendTermEndDt").datepicker('setDate','9999.12.31');
		$("#sendTermEndDt").prop("readonly", true);
		$("#sendTermEndDt").attr("disabled",true);		
	} else {
		$("#sendTermEndDt").datepicker('setDate',new Date());
		$("#sendTermEndDt").prop("readonly", false);
		$("#sendTermEndDt").attr("disabled",false);
	}
}

// 메일 파일을 읽어 편집기에 값 설정
function setMailContent(contFlPath) {
	$.getJSON("./mailFileView.json?contFlPath=" + contFlPath, function(res) {
		if(res.result == 'Success') {
			$("#mailContents").html( res.contVal );
		} else{
			$("#mailContents").html("<span>메일 미리보기에 실패했습니다</span>");
		}
	});
}
  
//준법심의 취소
function popProhibitCancel() {	
	fn.popupClose('#popup_prohibit');
}

//준법심의 결과 보기
function goPopProbibitInfo(taskNo, subTaskNo) {
	
	var param = "taskNo=" + taskNo + "&subTaskNo=" + subTaskNo;
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./pop/popProhibitInfo.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopProhibitInfo").html(pageHtml);
		},
		error : function(){
			alert("준법심의 결과 조회에 실패했습니다!");
		}
	}); 
	fn.popupOpen("#popup_prohibit_info");
}

// 목록에서 발송상태 보기 
function popApprovalState(taskNo, subTaskNo) {
	
	var param = "taskNo=" + taskNo + "&subTaskNo=" + subTaskNo;
	$.ajax({
		type : "GET",
		url : "./pop/popApprStateList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#popApprovalState").html(pageHtml);
		},
		error : function(){
		}
	});
	
	fn.popupOpen('#popup_mail_approval_state');
}