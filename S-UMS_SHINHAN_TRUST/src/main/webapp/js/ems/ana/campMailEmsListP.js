/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.13
	*	설명 :캠페인별 발송 - 메일별 발송목록 JavaScript
**********************************************************/

$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

// 목록 전체 체크
function goAll() {
	$("#searchForm input[name='taskNos']").each(function(idx,item){
		$(item).prop("checked", $("#searchForm input[name='isAll']").is(":checked"));
		$("#searchForm input[name='subTaskNos']").eq(idx).prop("checked", $("#searchForm input[name='isAll']").is(":checked"));
	});
}

function goChagePerPageRows(rows){
	$("#searchForm input[name='rows']").val(rows);
	goSearch("1");
}

// 검색 버튼 클릭
function goSearch(pageNo) {
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
		alert("검색 기간은 1년을 넘길수 없습니다");
		return;
	}
	

	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./campMailEmsList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divCampMailEmsList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
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

function goCampMailAnalList(taskNo, subTaskNo){
	$("#taskNo").val(taskNo);	
	$("#subTaskNo").val(subTaskNo);
	$("#searchForm").attr("target","").attr("action","./campMailAnalListP.ums").submit();
}

function goFail(str) {
	alert(str);
	return;
}
 
// 초기화 버튼 클릭
function goInit() {
	$("#searchForm")[0].reset();
	/*hidden reest*/
	$("#searchTaskNo").val("0");	
}

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}

// 캠페인명 선택 클릭시(캠페인선택 팝업)
function popMailHist(taskNo, subTaskNo){

	$("#popMailSendHistForm input[name='taskNo']").val(taskNo);
	$("#popMailSendHistForm input[name='subTaskNo']").val(subTaskNo);
	
	goPopMailHistSearch("1", taskNo, subTaskNo);
	fn.popupOpen("#popup_mail_send_hist");
}

// 발송 이력 조회 
function goPopMailHistSearch(pageNum, taskNo, subTaskNo) {
	$("#popMailSendHistForm input[name='page']").val(pageNum);
	
	var param = $("#popMailSendHistForm").serialize();
	
	if(param  == "" ){
		param ="taskNo=" + taskNo + "&subTaskNo=" + subTaskNo;
	}
	
	console.log(param);
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popMailSendHist.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#popMailSendHist").html(pageHtml);
		},
		error : function(){
			alert("Pop Mail Send Hist Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록 페이징
function goPageNumPopMailHist(pageNum) {
	goPopMailHistSearch(pageNum, $("popHistTaskNo").val(), $("popHistSubTaskNo").val());
}

// 목록 클릭시
function goList() {
	$("#searchStartDt").val($("#orgSearchStartDt").val());
	$("#searchEndDt").val($("#orgSearchEndDt").val());
	$("#searchUserId").val($("#orgSearchUserId").val());
	$("#searchDeptNo").val($("#orgSearchDeptNo").val());
	$("#searchCampNo").val("0");
	$("#searchCampNm").val("");
	$("#searchForm").attr("target","").attr("action","./campMailSendListP.ums").submit();
}
