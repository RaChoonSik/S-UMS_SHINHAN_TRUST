/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.17
*	설명 : 템플릿 목록 JavaScript
**********************************************************/

$(document).ready(function() {
	// 페이지 로딩시 리스트 
	goSearch("1");
});

//사용자그룹 선택시 사용자 목록 조회 
function getUserList(formId, deptNo, userObjectId) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#" + formId + " select[name='" + userObjectId + "']").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#" + formId + " select[name='" + userObjectId + "']").append(option);
		});
	});
}

//검색 버튼 클릭
function goSearch(pageNum) {
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
	
	$("#searchForm input[name='page']").val(pageNum);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./tempList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divTempList").html(pageHtml);
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

// 신규등록 클릭시
function goAdd() {
	document.location.href = "./tempAddP.ums";
}

//템플릿명 클릭시
function goUpdate(tempNo) {
	$("#tempNo").val(tempNo);
	$("#searchForm").attr("target","").attr("action","./tempUpdateP.ums").submit();
}

//페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}
