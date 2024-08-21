/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.08
*	설명 : 통계분석 누적통계 화면 JavaScript
**********************************************************/

$(document).ready(function() {
	goSearch();
});

// 검색 클릭시
function goSearch() {
	
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
	
	$("#searchCampNm").val($("#searchCampNo option:selected").text());
	$("#searchDeptNm").val($("#searchDeptNo option:selected").text());
	$("#searchUserNm").val($("#searchUserId option:selected").text());
	
	$("#txtStartEndDt").html($("#searchStartDt").val() + " ~ " + $("#searchEndDt").val());
	var campNm = $("#searchCampNo").val() == "0"?"모든 캠페인":$("#searchCampNo option:selected").text();
	$("#txtCampNm").html(campNm);
	
	goStatTab(1);
	$("#tabMenuChange a").eq(0).click();
}

// 초기화 클릭시
function goInit() {
	$("#searchForm")[0].reset();
}

// 탭 클릭시
var typeUrl = "";
var typeDiv = "";
function goStatTab(type) {
	if(type == 1) {						// 월별
		typeUrl = "./summMonthP.ums";
		typeDiv = "divSummMonthP";
	} else if(type == 2) {				// 요일별
		typeUrl = "./summWeekP.ums";
		typeDiv = "divSummWeekP";
	} else if(type == 3) {				// 일자별
		typeUrl = "./summDateP.ums";
		typeDiv = "divSummDateP";
	} else if(type == 4) {				// 도메인별
		typeUrl = "./summDomainP.ums";
		typeDiv = "divSummDomainP";
	} else if(type == 5) {				// 사용자그룹별
		typeUrl = "./summDeptP.ums";
		typeDiv = "divSummDeptP";
	} else if(type == 6) {				// 고객별
		typeUrl = "./summUserP.ums";
		typeDiv = "divSummUserP";
	} else if(type == 7) {				// 캠페인별
		typeUrl = "./summCampP.ums";
		typeDiv = "divSummCampP";
	}
	
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : typeUrl + "?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#" + typeDiv).html(pageHtml);
		},
		error : function(){
			alert("Page Data Error!!");
		}
	});
}

// Excel Download 클릭시
function goExcelDown(excelType) {
	if($("#searchStartDt").val() > $("#searchEndDt").val()) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");
		return;
	}
		
	var endDt = $("#searchEndDt").val().split('.');
	var startDt = $("#searchStartDt").val().split('.');
	var endDate = new Date(endDt[0], endDt[1], endDt[2]);
	var startDate = new Date(startDt[0], startDt[1], startDt[2]);
	
	var diff = endDate - startDate;
	var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
	if ( parseInt(diff/currDay) > 365 ){
		alert("엑셀 다운로드 기간은 1년을 넘길수 없습니다");
		return;
	}
	
	var excelDownUrl = "";
	if(excelType == "Month") {					// 월별
		excelDownUrl = "./summMonthExcel.ums";
	} else if(excelType == "Week") {			// 요일별
		excelDownUrl = "./summWeekExcel.ums";
	} else if(excelType == "Date") {			// 일자별
		excelDownUrl = "./summDateExcel.ums";
	} else if(excelType == "Domain") {			// 도메인별
		excelDownUrl = "./summDomainExcel.ums";
	} else if(excelType == "Dept") {			// 사용자그룹별
		excelDownUrl = "./summDeptExcel.ums";
	} else if(excelType == "User") {			// 사용자별
		excelDownUrl = "./summUserExcel.ums";
	} else if(excelType == "Camp") {			// 캠페인별
		excelDownUrl = "./summCampExcel.ums";
	}
	$("#searchForm").attr("target","iFrmExcel").attr("action",excelDownUrl).submit();
}
