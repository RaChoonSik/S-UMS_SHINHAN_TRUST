/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.01.19
*	설명 : 통계분석 대용량메일(단/정) 통합 통계분석 화면 JavaScript
**********************************************************/

$(document).ready(function() {
	goStatTab(1);
});

// 탭 클릭시
var checkType = "";
var typeUrl = "";
var typeDiv = "";
function goStatTab(type) {
	if(type == 1) {						// 결과요약
		typeUrl = "./taskSummP.ums";
		typeDiv = "divTaskSummP";
	} else if(type == 2) {				// 세부에러
		typeUrl = "./taskErrorP.ums";
		typeDiv = "divTaskErrorP";
	} else if(type == 3) {				// 도메인별
		typeUrl = "./taskDomainP.ums";
		typeDiv = "divTaskDomainP";
	} else if(type == 4) {				// 발송시간별
		typeUrl = "./taskSendP.ums";
		typeDiv = "divTaskSendP";
	} else if(type == 5) {				// 응답시간별
		typeUrl = "./taskRespP.ums";
		typeDiv = "divTaskRespP";
	} else if(type == 6) {				// 고객별
		typeUrl = "./taskCustP.ums";
		typeDiv = "divTaskCustP";
	}	
	if(checkType != type) {
		$("#mailInfoForm input[name='page']").val("1");
	}
	var param = $("#mailInfoForm").serialize();
	$.ajax({
		type : "GET",
		url : typeUrl + "?" + param,
		dataType : "html",
		success : function(pageHtml){
			checkType = type;
			$("#" + typeDiv).html(pageHtml);
		},
		error : function(){
			alert("Page Data Error!!");
		}
	});
}

// 고객별탭 조회 클릭시
function goCustSearch() {
	goPageNumCust("1");
}

// 고객별탭 초기화 클릭시
function goInit() {
	$("#mailInfoForm")[0].reset();
}

// 고객별탭 페이징
function goPageNumCust(pageNum) {
	$("#mailInfoForm input[name='page']").val( pageNum );
	goStatTab(6);
}

// 발송시간별탭 페이징
function goPageNumSend(pageNum) {
	$("#mailInfoForm input[name='page']").val( pageNum );
	goStatTab(4);
}

// 응답시간별탭 페이징
function goPageNumResp(pageNum) {
	$("#mailInfoForm input[name='page']").val( pageNum );
	goStatTab(5);
}
 
function goMailDetil(){
	
	$("#popMailTitle").text($("#mailTitle").val());
	$("#popSendMailFromNm").text($("#mailFromNm").text());
	$("#popSendMailFromEm").text($("#mailFromEm").text());
	$("#popAttCnt").text('(파일 개수 : ' + $("#attCnt").val() + '개)');
	$("#popCampNm").text($("#campNm").text());
	
	if($("#webAgent").val() == "" ) {
		$("#popWebAgentDesc").text("첨부파일 형식이 지정되지 않았습니다");
	} else {
		$("#popWebAgent").val($("#webAgent").val());
		$("#popWebAgentDesc").text( "(" + $("#webAgentTyp").val() + ") 첨부파일 형식이 지정되었습니다");
	}
	
	$("#previewWebAgentUrl").html($("#webAgent").val());
	$("#popTaskNo").val($("#taskNo").val());
	$("#popSubTaskNo").val($("#subTaskNo").val()); 

	var contFlPath  = $("#contFlPath").val();
	var param = "/ems/cam/mailFileView.ums?contFlPath=" + contFlPath;
	
	$.getJSON(param, function(res) {
		if(res.result == 'Success') {
			$("#popContents").html( res.contVal );
		} else {
			$("#popContents").html( "<span>메일 미리보기에 실패했습니다</span>" ); 
		}
	});
	fn.popupOpen('#popup_mail_detail_ems_resend')	
}

function popWebAgentPreview(){
	if(	$("#popWebAgent").val() == ""){
		alert("등록된 보안메일이 없습니다");
		return;
	}
	$("#iFrmWebAgent").empty();	
	iFrmWebAgent.location.href = $("#popWebAgent").val();
	fn.popupOpen('#popup_preview_webagent');
}

// 캠페인별 > 메일발송분석 > 재발송 
function mailReSend() {
	
	if($("#reSendAuth").val() == "N"){
		alert("대용량 메일 재발송 권한이 없습니다");
		return;
	}

	var segRetry = 0;
	segRetry  = $("#segRetry").val();
	if(segRetry == 0){
		alert("EAI 연계 SQL이 존재 하지 않아 재발송 할 수 없습니다");
		return;
	}
	
	const query = 'input[name="popRcode"]:checked';
	const checkboxs = document.querySelectorAll(query);
 
	var rtyCodes =""; 
	if (checkboxs.length < 1 ) {
		alert("재발송 조건을 선택해주세요");
		return;
	}
	for (var i = 0; i < checkboxs.length; i++) {
		var checkRtyCode =  checkboxs[i].value; 
		rtyCodes +=checkRtyCode + ','; 
	}
	$("#popRtyCode").val(rtyCodes);
	
	var param = $("#mailDetailForm").serialize();
  
	param = "/ems/cam/mailReSend.json?" + param; 
	 
	var a = confirm("재발송 하시겠습니까?");
	if ( a ) {
		$.getJSON(param, function(data) {
			if(data.result == "Success") {
				alert("재발송 되었습니다");
				fn.popupClose('#popup_mail_detail_ems_resend');
			} else {
				alert("재발송에 실패하였습니다 실패하셨습니다");
			}
		});
	} else return;
}



// 목록 클릭시
function goList() {
	$("#searchForm").attr("target","").attr("action","./campMailEmsListP.ums").submit();
}

