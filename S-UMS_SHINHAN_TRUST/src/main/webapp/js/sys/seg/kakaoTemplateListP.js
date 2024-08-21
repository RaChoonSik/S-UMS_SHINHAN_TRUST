/**********************************************************
*	작성자 : 김재환
*	작성일시 : 2021.12.09
*	설명 : 카카오 알림톡 템플릿 목록
**********************************************************/

$(document).ready(function() {
	goSearch("1");
});

// 사용자그룹 선택시 사용자 목록 조회 
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}

// 조회
function goSearch(pageNo) {
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./kakaoTemplateList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divSegList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 검색조건을 초기화 함
function goInit() {
	$("#searchForm")[0].reset();
	$("#searchDeptNo").val("0");
}

// 등록
function goAdd() {
	$("#searchForm").attr("target","").attr("action","./kakaoTemplateAddP.ums").submit();
}

// 목록에서 템플릿 이름 클릭시 수정화면 이동
function goUpdate(tempCd) {
	if( tempCd == "" || tempCd == null || tempCd == undefined ) {
		alert("수정할 템플릿의 템플릿코드가 없습니다 확인해주세요");
		return;
	}
	$("#tempCd").val(tempCd);
	$("#searchForm").attr("target","").attr("action","./kakaoTemplateUpdateP.ums").submit();
}

// 페이지 이동
function goPageNum(page) {
	goSearch(page);
}
