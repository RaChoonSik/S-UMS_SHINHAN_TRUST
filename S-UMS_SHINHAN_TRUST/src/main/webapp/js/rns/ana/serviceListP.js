/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 월별통계 화면
*	설명 : 월별통계 JavaScript
**********************************************************/


$(document).ready(function() {
	 goSearch();
});

// 검색 버튼 클릭
function goSearch() {
	if($("#searchStartDt").val() > $("#searchEndDt")) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");
		return;
	}
	
	var endDt = $("#searchEndDt").val().split('.');
	var startDt = $("#searchStdDt").val().split('.');
	var endDate = new Date(endDt[0], endDt[1], endDt[2]);
	var startDate = new Date(startDt[0], startDt[1], startDt[2]);
	
	var diff = endDate - startDate;
	var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
	if ( parseInt(diff/currDay) > 91 ){
		alert("검색 기간은 3개월을 넘길수 없습니다");
		return;
	}
	
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./serviceList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divServiceList").html(pageHtml);
			setFoot();
		},
		error : function(){
			alert("List Data Error!!");
		}
	}); 
 
}

function setFoot(){
	var rowCnt = 0;
    	
	var colSend = 2;		// 발송 col index
	var colSuccess = 6;		// 성공 col index
	var colFailed = 7;		// 실패 col index
	var colReply = 8;		// 응답 col index
	
	var colSendSum = 0;		// 발송 col sum
	var colSuccessSum = 0;	// 성공 col sum
	var colFailedSum = 0;	// 실패 col sum
	var colReplySum = 0;	// 응답 col sum
	
	// loop row
	var trList = $("#tblServiceData tr");
	if (trList.length > 1) {
		console.log(trList);
		for (var i = 1 ; i < trList.length ; i ++) {
			var sell = $(trList[i]).children("td");
			for(var j= 0; j < sell.length; j ++){
				var idx = $(sell[j]).index();
				var val = $(sell[j]).text();
				if( idx == colSend ){
					colSendSum += parseInt(val); 
				}else if( idx == colSuccess ){
					colSuccessSum += parseInt(val);
				}else if( idx == colFailed ){
					colFailedSum += parseInt(val);
				}else if( idx == colReply ){
					colReplySum += parseInt(val);
				}
			}
		}
0		
	} 
	
	// 합계를 출력
	$("#tot_send").text(colSendSum);
	$("#tot_success").text(colSuccessSum);
	$("#tot_failed").text(colFailedSum);
	$("#tot_reply").text(colReplySum);	
}

// 초기화 버튼 클릭
function goInit() {
	$("#searchForm")[0].reset();
}
 
 function goExcel(){
	if($("#searchStdDt").val() > $("#searchEndDt")) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");
		return;
	}
	
	var endDt = $("#searchEndDt").val().split('.');
	var startDt = $("#searchStdDt").val().split('.');
	var endDate = new Date(endDt[0], endDt[1], endDt[2]);
	var startDate = new Date(startDt[0], startDt[1], startDt[2]);
	
	var diff = endDate - startDate;
	var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
	if ( parseInt(diff/currDay) > 365 ){
		alert("엑셀 다운로드 기간은 1년을 넘길수 없습니다");
		return;
	}	
	$("#searchForm").attr("target","iFrmExcel").attr("action","./serviceExcelList.ums").submit();
}