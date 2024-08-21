/**********************************************************
*	작성자 : 이문용
*	작성일시 : 2022.03.16
*	설명 : 문자 발송목록 JavaScript
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
		url : "./campKakaoList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divCampSmsList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

function goCampKakaoList(tempCd, tempNm){
	$("#searchTempCd").val( tempCd );
	$("#searchTempNm").val( tempNm );
	
	$("#searchForm").attr("target","").attr("action","./kakaoSendListP.ums").submit();
}
 
 
// 목록에서 성공/실패의 건수 클릭시 팝업창
function goSmsSendList(msgid, keygen, rsltCd) { 
	$("#popSmsSendResultSearchForm input[name='sendTelNo']").val("");
	$("#popSmsSendResultSearchForm input[name='rsltCd']").val(rsltCd);
	$("#popSmsSendResultSearchForm input[name='msgid']").val(msgid);
	$("#popSmsSendResultSearchForm input[name='keygen']").val(keygen);

	goSmsSendReultList();
	fn.popupOpen('#popup_smsSendResultList');
}
 
// 초기화 버튼 클릭시
function goInit(adminYn, deptNo) {
	$("#searchForm")[0].reset();
	if(adminYn == "Y") {
		$("#searchDeptNo").val("0");
	} else {
		$("#searchDeptNo").val(deptNo);
	}
	$("#searchUserId").val("");
	$("#searchUserId").val("");
	$("#searchCampNo").val("0");
	$("#txtCampNm").html("선택된 캠페인이 없습니다.");
	
	var date = new Date();
	var searchEndDt = date.getFullYear() + "." + ("0"+(date.getMonth()+1)).slice(-2) + "." + ("0"+(date.getDay()-1)).slice(-2) 
	
	date.setMonth(date.getMonth() - 1);
	var searchStartDt = date.getFullYear() + "." + ("0"+(date.getMonth()+1)).slice(-2) + "." + ("0"+(date.getDay()+2)).slice(-2)
	
	$("#searchStartDt").val(searchStartDt);
	$("#searchEndDt").val(searchEndDt);
}

function goPageNum(page) {
	goSearch(page);
}
