/**********************************************************
*	작성자 : 이혜민
*	작성일시 : 2022.03.23
*	설명 : 통계분석 카카오통계분석 분석 목록 화면 JavaScript
**********************************************************/

$(document).ready(function() {
	var timeGap = getTimeGap();
	$("#timeGap").text(timeGap);
});

function getTimeGap(){
	
	var time1 = new Date($("#startTime").text());
	console.log("startTime: " + time1 );
	var time2 = new Date($("#endTime").text());
	console.log("endTime: " + time2 );
	var timeGap = time2 - time1;
	
	var hours =  Math.floor((timeGap % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
	var min = Math.floor((timeGap % (1000 * 60 * 60)) / (1000 * 60));
	var sec = Math.floor((timeGap % (1000 * 60)) / 1000);

	if (hours   < 10) {hours   = "0" + hours;}
	if (min < 10) {min = "0" + min;}
	if (sec < 10) {sec = "0" + sec;}	

	return hours + ":" + min+ ":" + sec;
}

// 목록에서 메일명 클릭시 팝업창
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

function goList() {
	if ($("#searchCallGubun").val() == "C" ){
		$("#searchForm").attr("target","").attr("action","./kakaoSendListP.ums").submit();
	} else {
		$("#searchForm").attr("target","").attr("action","./kakaoListP.ums").submit();	
	}
	
}

/*

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
	var timeGap = getTimeGap();
	$("#timeGap").text(timeGap);
}


// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}
*/
