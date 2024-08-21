/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.13
*	설명 : 캠페인 목록 JavaScript
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
		url : "./campList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divCampList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 초기화 버튼 클릭
function goReset() {
	$("#searchForm")[0].reset();
}

// 캠페인명 클릭시
function goUpdate(campNo) {
	$("#campNo").val(campNo);
	$("#searchForm").attr("target","").attr("action","./campUpdateP.ums").submit();
}

// 발송메일 클릭시
function goMail(campNo) {
	if(campNo) {
		$("#searchForm input[name='campNo']").val(campNo);
		$("#searchForm").attr("target","").attr("action","./campMailListP.ums").submit();
	} else {
		alert("캠페인 선택");		// CAMTBLLB007
	}
}

// 신규등록 클릭시
function goAdd() {
	$("#searchForm").attr("target","").attr("action","./campAddP.ums").submit();
}

//페이징
function goPageNum(page) {
	goSearch(page);
}
