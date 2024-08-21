/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22  
*	설명 : 상세로그 JavaScript
**********************************************************/

$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

// 검색 버튼 클릭
function goSearch(pageNo) {
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
	if ( parseInt(diff/currDay) > 31 ){
		alert("검색 기간은 1개월을 넘길수 없습니다");
		return;
	}
		
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./detailLogList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divDetailLogList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

function goMailDetil(mid, deptNm, attchCnt, obj ){

	var sname="";
	var tnm="";
	var smail ="";
	var subject ="";
	var cdate ="";
	var sdate = ""; 
	var contents =""; 
	
	var selTr = $(obj.parentNode.parentNode);
	console.log(selTr); 
	var sell = $(selTr.children("td"));	
	
	console.log(sell);
	
	for(var j= 0; j < sell.length; j ++){
		var idx = $(sell[j]).index();
		var val = $(sell[j]).text();
		
		if( idx == 6 ){ //발송자명
			sname = val;
		}else if( idx == 2 ){ //서비스명
			tnm  = val;
		}else if( idx == 3 ){ //메일제목
			subject  = val;
		}else if( idx == 10 ){ //등록일시
			cdate  = val; 
		}else if( idx == 1){ //발송일시
			sdate  = val;
		}else if( idx == 11 ){ //발송템플릿
			contents  = val;
		}else if( idx == 7 ){ //발송자이메일주소
			smail  = val;
		}
	} 
	$("#popSnm").text(sname + '(' + smail +')');
	$("#popTnm").text(tnm);
	$("#popSubject").text(subject);
	$("#popCdate").text(cdate);
	$("#popSdate").text(sdate);
	$("#popDeptNm").text(deptNm);
	$("#popContents").html( contents );
	$("#popAttchCnt").text('(파일 개수 : ' + attchCnt + '개)');
	
	var param = "searchMid=" + mid;
	$.ajax({
		type : "GET",
		url : "./mailSendResultList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#mailSendResult").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
	
	fn.popupOpen('#popup_mail_detail_rns');
}
// 초기화 버튼 클릭
function goInit() {
	$("#searchForm")[0].reset();
} 

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}
 
function goExcel(){
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
	if ( parseInt(diff/currDay) > 365 ){
		alert("검색 기간은 1년을 넘길수 없습니다");
		return;
	}	
	$("#searchForm").attr("target","iFrmExcel").attr("action","./detailLogExcelList.ums").submit();
}