/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.31
*	설명 : 정기메일 차수별 발송목록 JavaScript
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
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./subTaskList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 목록에서 발송상태(발송실패) 클릭시
function goFail(str) {
	alert(str);
	return;
}

// 초기화 버튼 클릭
function goInit() {
	/*
	var date = new Date();
	var firstday = new Date(date.getFullYear(), date.getMonth(), "01");
	var lastday = new Date(date.getFullYear(), "11", "01");
	$(".datepickerrange.fromDate input").datepicker('setDate', firstday);
	$(".datepickerrange.toDate input").datepicker('setDate', lastday);
	*/
	$("#searchForm")[0].reset();
}

// 뒤로가기 클릭시
function goMain() {
	$("#prevSearchForm").attr("target","").attr("action","./taskListP.ums").submit();
}

// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동
function goUpdate(taskNo, subTaskNo) {
	$("#taskNo").val( taskNo );
	$("#subTaskNo").val( subTaskNo );
	$("#prevSearchForm").attr("target","").attr("action","./taskUpdateP.ums").submit();
}

// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동 - 결재완료, 발송승인인 경우 
function goUpdateDate(taskNo, subTaskNo, approvalProcAppYn) {
	$("#taskNo").val( taskNo );
	$("#subTaskNo").val( subTaskNo );
	$("#approvalProcAppYn").val( approvalProcAppYn );
	console.log(approvalProcAppYn);
	$("#prevSearchForm").attr("target","").attr("action","./taskUpdateDateP.ums").submit();
}

// 페이징
function goPageNum(page) {
	goSearch(page);
}
