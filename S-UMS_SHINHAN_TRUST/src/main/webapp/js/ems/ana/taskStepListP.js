/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.08
*	설명 : 통계분석 정기메일 차수별 통계분석 목록 화면 JavaScript
**********************************************************/

//사용자그룹 선택시 사용자 목록 조회 
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}

// 검색 버튼 클릭시
function goSearch(pageNum) {
	if( $("#searchStartDt").val() > $("#searchEndDt").val() ) {
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
	
	$("#searchForm input[name='page']").val(pageNum);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./taskStepList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divTaskStepList").html(pageHtml);
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
		$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", $("#searchForm input[name='isAll']").is(":checked"));
	});
}

// 목록에서 메일명 클릭시
function goTaskStepStat(taskNo, subTaskNo) {
	$("#taskNo").val(taskNo);
	$("#subTaskNo").val(subTaskNo);
	
	$("#searchForm").attr("target","").attr("action","./taskStepStatP.ums").submit();
}

// 정기메일분석 클릭시
function goTaskList() {
	$("#page").val("1");
	$("#searchCampNo").val("0");
	$("#searchTaskNm").val("");
	$("#searchDeptNo").val("0");
	$("#searchUserId").val("");
	
	$("#searchForm").attr("target","").attr("action","./taskListP.ums").submit();
}

// 병합분석 클릭시
function goJoin() {
	var checkCnt = 0;
	
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", true);
			checkCnt++;
		} else {
			$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", false);
		}
	});
	
	if(checkCnt < 2) {
        alert("병합할 대상을 2개이상 선택하세요.");
    } else {
		$("#searchForm").attr("target","").attr("action","./taskStepJoinP.ums").submit();
	}
}

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}
