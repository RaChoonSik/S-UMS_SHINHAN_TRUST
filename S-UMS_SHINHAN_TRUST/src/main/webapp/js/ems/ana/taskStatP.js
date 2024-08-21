/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.06
*	설명 : 통계분석 정기메일 통합 통계분석 화면 JavaScript
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

// Excel Download 클릭시
function goExcelDown(excelType) {
	var excelDownUrl = "";
	if(excelType == "Summ") {
		excelDownUrl = "./taskSummExcel.ums";
	} else if(excelType == "Error") {
		excelDownUrl = "./taskErrorExcel.ums";
	} else if(excelType == "Domain") {
		excelDownUrl = "./taskDomainExcel.ums";
	} else if(excelType == "Send") {
		excelDownUrl = "./taskSendExcel.ums";
	} else if(excelType == "Resp") {
		excelDownUrl = "./taskRespExcel.ums";
	} else if(excelType == "Cust") {
		excelDownUrl = "./taskCustExcel.ums";
	}
	$("#mailInfoForm").attr("target","iFrmExcel").attr("action",excelDownUrl).submit();
}

// 목록 클릭시
function goList() {
	$("#searchForm").attr("target","").attr("action","./taskListP.ums").submit();
}

