/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.10.18
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
function goSmsList(campNo) {
	if(campNo != "0") {
		$("#searchCampNo").val(campNo);
		console.log($("#searchCampNo").val());
		$("#searchForm").attr("target","").attr("action","./smsListP.ums").submit();
	} else {
		alert("캠페인이 선택되지 않았습니다");
	}
}

// 신규등록 클릭시
function goAdd() {
	document.location.href = "./campAddP.ums";
}

//페이징
function goPageNum(page) {
	goSearch(page);
}
