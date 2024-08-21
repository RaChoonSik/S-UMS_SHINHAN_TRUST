/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.10.26
	*	설명 : 캠페인별 문자발송 분석
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
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./kakaoSendList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divKakaoSendList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

function goKakaoAnalList(msgid, keygen){
	$("#msgid").val( msgid );
	$("#keygen").val( keygen );
	$("#searchForm").attr("target","").attr("action","./kakaoAnalListP.ums").submit();
} 
// 초기화 버튼 클릭시
function goInit() {
	$("#searchForm")[0].reset();
}

function goPageNum(page) {
	goSearch(page);
}

// 목록 클릭시
function goList() {
	$("#searchStartDt").val($("#orgSearchStartDt").val());
	$("#searchEndDt").val($("#orgSearchEndDt").val());
	$("#searchUserId").val($("#orgSearchUserId").val());
	$("#searchDeptNo").val($("#orgSearchDeptNo").val());
	$("#searchForm").attr("target","").attr("action","./campKakaoListP.ums").submit();
}

// 목록에서 성공/실패의 건수 클릭시 팝업창
function goKakaoSendList(msgid, keygen, rsltCd,smsSendYn) { 
	$("#popSmsSendResultSearchForm input[name='sendTelNo']").val("");
	$("#popSmsSendResultSearchForm input[name='rsltCd']").val(rsltCd);
	$("#popSmsSendResultSearchForm input[name='smsSendYn']").val(smsSendYn);
	$("#popSmsSendResultSearchForm input[name='msgid']").val(msgid);
	$("#popSmsSendResultSearchForm input[name='keygen']").val(keygen);
	$("#popSmsSendResultSearchForm input[name='gubun']").val("004");
	
	goSmsSendReultList();
	fn.popupOpen('#popup_smsSendResultList');
}