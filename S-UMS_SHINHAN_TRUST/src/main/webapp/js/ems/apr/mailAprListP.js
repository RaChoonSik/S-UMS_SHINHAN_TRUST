/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.23
*	설명 : 메일발송결재 목록 JavaScript
**********************************************************/

$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

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

//승인예정그룹 선택시 사용자 목록 조회 
function getUserListApr(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchAprUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchAprUserId").append(option);
		});
	});
}

// 검색 클릭시
function goSearch(pageNum) {
	$("#page").val(pageNum);
	
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
	
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./mailAprList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 초기화 클릭시
function goInit(adminYn, deptNo) {
	$("#searchForm")[0].reset();
	$("#searchTaskNm").val("");
	$("#searchCampNo").val("0");
	if(adminYn == "Y") {
		$("#searchDeptNo").val("0");
	} else {
		$("#searchDeptNo").val(deptNo);
	}
	$("#searchUserId").val("");
	$("#searchWorkStatus").val("");
	$("#searchAprDeptNo").val("0");
	$("#searchAprUserId").val("");
}

// 목록에서 메일명 클릭시
function goAprUpdate(taskNo, subTaskNo, svcType) {
	if (svcType == "10") {
		$("#taskNo").val( taskNo );
		$("#subTaskNo").val( subTaskNo );
		
		
		$("#searchForm").attr("target","").attr("action","./mailAprUpdateP.ums").submit();
	} else {
		$("#taskNo").val( taskNo ); 
		
		
		$("#searchForm").attr("target","").attr("action","./rnsAprUpdateP.ums").submit();
	}

}

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}