/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.06
*	설명 : 통계분석 정기메일 등록현황 화면 JavaScript
**********************************************************/

$(document).ready(function() {
	goSearch("1");
});

//검색 버튼 클릭시
function goSearch(pageNum) {
	if( $("#searchStartDt").val() > $("#searchEndDt").val() ) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");		// <spring:message code='COMJSALT017'/>
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
	
	$("#searchForm input[name='page']").val(pageNum);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./taskList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divTaskList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 초기화 버튼 클릭시
function goInit() {
	$("#searchForm")[0].reset();
}

// 목록에서 전체선택 클릭시
function goAll() {
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		$(item).prop("checked", $("#searchForm input[name='isAll']").is(":checked"));
	});
}

// 목록에서 메일명 클릭시
function goTaskStat(taskNo, sendRepeat) {
	$("#taskNo").val(taskNo);
	$("#sendRepeat").val(sendRepeat);
	
	if(sendRepeat == "000" ) {
		$("#searchForm").attr("target","").attr("action","./mailStatP.ums").submit();
	} else {
		$("#searchForm").attr("target","").attr("action","./taskStatP.ums").submit();	
	}
	
}

// 메일별분석 클릭시
function goTaskStep(taskNo) {
	$("#page").val("1");
	$("#searchCampNo").val("0");
	$("#searchTaskNm").val("");
	$("#searchDeptNo").val("0");
	$("#searchUserId").val("");
	$("#taskNo").val(taskNo);
	
	$("#searchForm").attr("target","").attr("action","./taskStepListP.ums").submit();
}

// 병합분석 클릭시
function goJoin() {
	var checkCnt = 0;
	
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			checkCnt++;
		}
	});
	
	if(checkCnt < 2) {
		alert("병합할 대상을 2개이상 선택하세요.");
	} else {
		$("#searchForm").attr("target","").attr("action","./taskJoinP.ums").submit();
	}
}



// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}