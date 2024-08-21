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
		url : "./serviceTestList.ums?" + param,
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
function goInit() {
	$("#searchForm")[0].reset();
}

// 목록에서 메일명 클릭시
function goTestList(mid, sdate, tnm) {
	$("#txtSendDt").html( sdate );
	$("#txtTaskNm").html( tnm );
	
	$("#testSendForm input[name='mid']").val( mid );
	var param = $("#testSendForm").serialize();
	$.ajax({
		type : "GET",
		url : "./serviceTestResultList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#testResultList").html(pageHtml);
			
			fn.popupOpen('#popup_testsend_result');
		},
		error : function(){
			alert("Test List Data Error!!");
		}
	});
}

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}

// 페이징 테스트
function goPageNumTest(pageNum) {
	$("#testSendForm input[name='page']").val( pageNum );
	var param = $("#testSendForm").serialize();
	$.ajax({
		type : "GET",
		url : "./serviceTestResultList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#testResultList").html(pageHtml);
		},
		error : function(){
			alert("Test List Data Error!!");
		}
	});
}
