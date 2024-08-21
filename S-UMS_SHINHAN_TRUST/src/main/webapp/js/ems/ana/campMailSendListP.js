/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.13
*	설명 : 캠페인별 메일 발송  JavaScript
**********************************************************/

$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
});

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
	
	if($("input[name='isSendTerm']").eq(1).is(":checked")) {
		$("#searchServiceGb").val("20");
	} else {
		$("#searchServiceGb").val("10");
	}

	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./campMailSendList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divCampMailSendList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

function goCampMailList(campNo, campNm){

	if($("input[name='isSendTerm']").eq(1).is(":checked")) {
		$("#searchServiceGb").val("20");
	} else {
		$("#searchServiceGb").val("10");
	}
	
	$("#searchCampNo").val( campNo );
	$("#searchCampNm").val( campNm );
	
	if ($("#searchServiceGb").val() == "10") {
		$("#searchForm").attr("target","").attr("action","./campMailEmsListP.ums").submit();
	} else {
		$("#searchForm").attr("target","").attr("action","./campMailRnsListP.ums").submit();
	}
}
 
// 초기화 버튼 클릭
function goInit(adminYn, deptNo) {
	if(adminYn == "Y") {
		$("#searchDeptNo").val("0");
	} else {
		$("#searchDeptNo").val(deptNo);
	}
	$("#searchUserId").val("");
	$("#searchCampNo").val("0");
	$("#searchServiceGb").val("10");
	$("#txtCampNm").html("선택된 캠페인이 없습니다.");

	$("input[name='isSendTerm']").eq(0).attr('checked', true);
	$("input[name='isSendTerm']").eq(1).attr('checked', false);
	
	var date = new Date();
	var searchEndDt = date.getFullYear() + "." + ("0"+(date.getMonth()+1)).slice(-2) + "." + ("0"+(date.getDay()-1)).slice(-2) 
	
	date.setMonth(date.getMonth() - 1);
	var searchStartDt = date.getFullYear() + "." + ("0"+(date.getMonth()+1)).slice(-2) + "." + ("0"+(date.getDay()+2)).slice(-2)
	
	$("#searchStartDt").val(searchStartDt);
	$("#searchEndDt").val(searchEndDt);
}

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}

// 캠페인명 선택 클릭시(캠페인+RNS 서비스 선택 팝업)
function popCampRnsSelect() {
	var serviceGb = ""; 
	if($("input[name='isSendTerm']").eq(1).is(":checked")) {
		serviceGb = "20";		
	} else {
		serviceGb = "10";
	}
	
	$("#popCampRnsSearchForm input[name='searchStartDt']").val("");
	$("#popCampRnsSearchForm input[name='searchEndDt']").val("");
	$("#popCampRnsSearchForm input[name='searchCampNm']").val("");	
	$("#popCampRnsSearchForm input[name='searchServiceGb']").val(serviceGb);
	
	goPopCampRnsSearch("1", serviceGb);
	fn.popupOpen("#popup_campaign_rns");
}

// 팝업 캠페인 + RNS 서비스 목록 검색
function goPopCampRnsSearch(pageNum, serviceGb) {
	$("#popCampRnsSearchForm input[name='page']").val(pageNum);
	var param = $("#popCampRnsSearchForm").serialize();
	
	if(param  == "" ){
		param = "searchServiceGb=" + serviceGb;
	}
	console.log(param);
	$.ajax({
		type : "GET",
		url : "/ems/cam/pop/popCampRnsList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCampRns").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록에서 선택 클릭시
function setPopCampRnsInfo(campNo, campNm, serviceGb) {
	$("#searchCampNo").val(campNo);
	$("#searchServiceGb").val(serviceGb);
	$("#txtCampNm").html(campNm);
	
	fn.popupClose("#popup_campaign_rns");
}


// 팝업 캠페인 목록 페이징
function goPageNumPopCampRns(pageNum) {
	goPopCampRnsSearch(pageNum);
}
