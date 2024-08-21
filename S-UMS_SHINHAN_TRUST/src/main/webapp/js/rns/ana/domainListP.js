/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
*	설명 : 도메인통계 JavaScript
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
		url : "./domainList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divDomainList").html(pageHtml);
			setFoot();
		},
		error : function(){
			alert("List Data Error!!");
		}
	}); 
}
 
// 초기화 버튼 클릭
function goInit() {
	$("#searchForm")[0].reset();
}

function setFoot(){ 
    	
	var colTotal = 11;            //총발송건수  col index 
	var colSuccess = 12;          //성공수    col index 
	var colFail = 13;             //실패수    col index 
	var colSyntax = 14;           //문법오류   col index 
	var colDns	= 15;             //DNS에러  col index 
	var colTransact = 16;         //트랜잭션에러 col index 
	var colReceiver = 17;         //수신자오류  col index 
	var colNetwork = 18 ;         //네트워크   col index 
	var colService = 19;          //서비스장애  col index 

	var colTotalSum = 0;            //총발송건수  col sum
	var colSuccessSum = 0;          //성공수    col sum
	var colFailSum = 0;             //실패수    col sum
	var colSyntaxSum = 0;           //문법오류   col sum
	var colDnsSum = 0;             //DNS에러  col isum
	var colTransactSum = 0;         //트랜잭션에러 col sum
	var colReceiverSum = 0;         //수신자오류  col sum
	var colNetworkSum = 0 ;         //네트워크   col sum
	var colServiceSum = 0;          //서비스장애  col sum
		
	// loop row
	var trList = $("#tblDomainData tr");
	if (trList.length > 1) {
		console.log(trList);
		for (var i = 1 ; i < trList.length ; i ++) {
			var sell = $(trList[i]).children("td");
			for(var j= 0; j < sell.length; j ++){
				var idx = $(sell[j]).index();
				var val = $(sell[j]).text();
				
				if( idx == colTotal ){
					colTotalSum += parseInt(val); 
				}else if( idx == colSuccess ){
					colSuccessSum += parseInt(val);
				}else if( idx == colFail ){
					colFailSum += parseInt(val);
				}else if( idx == colSyntax ){
					colSyntaxSum += parseInt(val);
				}else if( idx == colDns ){
					colDnsSum += parseInt(val);
				}else if( idx == colTransact ){
					colTransactSum += parseInt(val);
				}else if( idx == colReceiver ){
					colReceiverSum += parseInt(val);
				}else if( idx == colNetwork ){
					colNetworkSum += parseInt(val);
				}else if( idx == colService ){
					colServiceSum += parseInt(val);
				}
			}
		}
0		
	} 
	
	// 합계를 출력
	$("#tot_total").text(colTotalSum);
	$("#tot_success").text(colSuccessSum);
	$("#tot_fail").text(colFailSum);
	$("#tot_syntax").text(colSyntaxSum);	
	$("#tot_dns").text(colDnsSum);
	$("#tot_transact").text(colTransactSum);
	$("#tot_receiver").text(colReceiverSum);
	$("#tot_network").text(colNetworkSum);		
	$("#tot_service").text(colServiceSum);
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
	if ( parseInt(diff/currDay) > 91 ){
		alert("엑셀 다운로드 기간은 1년을 넘길수 없습니다");
		return;
	}
		
	$("#searchForm").attr("target","iFrmExcel").attr("action","./domainExcelList.ums").submit();
}