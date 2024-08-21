/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.12.27
*	설명 : PUSH 발송목록 JavaScript
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

	$.ajax({
		type : "GET",
		url : "./pushList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPushList").html(pageHtml);
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

// 목록 전체 체크
function goAll() {
	if($("#searchForm input[name='isAll']").is(":checked") == true) {
		$("#searchForm input[name='pushmessageIds']").each(function(idx,item){
			if($(item).is(":disabled") == false) {
				$("#searchForm input[name='pushmessageIds']").eq(idx).prop("checked", true); 
				$("#searchForm input[name='selStatus']").eq(idx).prop("checked", true);
			}
		});
	} else {
		$("#searchForm input[name='pushmessageIds']").prop("checked", false); 
		$("#searchForm input[name='selStatus']").prop("checked", false);
	}
} 

// 목록에서 체크박스 클릭시 status와 workStatus 같이 체크해 준다.
function goPushmessageId(i) {
	var isChecked = $("#searchForm input[name='pushmessageIds']").eq(i).is(":checked");	
	$("#searchForm input[name='selStatus']").eq(i).prop("checked", isChecked);
	$("#searchForm input[name='selWorkStatus']").eq(i).prop("checked", isChecked);
}

// 목록에서 발송상태(발송대기) 클릭시 -> 발송승인
function goAdmit(i) {
	var pushmessageIds =  $("#searchForm input[name='pushmessageIds']").eq(i).val() ;
	var selStatus =  $("#searchForm input[name='selStatus']").eq(i).val() ;
	if ( selStatus == "001") {
		alert("삭제된 항목은 발송승인 할수 없습니다");
		return;
	}
	
	$("#pushmessageId").val(pushmessageIds);

	var a = confirm("발송승인 하시겠습니까?");
	if ( a ) {
		var param = $("#searchForm").serialize();
		
		$.getJSON("./pushAdmit.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("발송승인 되었습니다.");
				goSearch($("#searchForm input[name='page']").val());
			} else if(data.result == "Fail") {
				alert("발송승인 처리중 오류가 발생하였습니다.");
			}
		});
	} else return;
}

// 복사 클릭시
function goPushCopy() {
	
	var checkedCount = 0;

	$("#searchForm input[name='pushmessageIds']").each(function(idx,item){
		if($(item).is(":checked")) checkedCount++;
	});
	if(checkedCount == 0 ) {
		alert("복사할 목록을 선택해 주세요.!!");
		return;
	}

	var param = $("#searchForm").serialize(); 

	$.getJSON("./pushCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
			
			// PUSH 목록 재조회;
			goSearch('1');			
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});	
}

// 삭제 클릭시
function goPushDelete() { 
 
	// 선택항목체크
	var isChecked = false;
	$("#searchForm input[name='pushmessageIds']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});
	if(!isChecked) {
		alert("삭제할 목록을 선택해 주세요!!");
		return;
	} 
	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='pushmessageIds']").each(function(idx,item){
		if($(item).is(":checked")) {
			if("000,001".indexOf($("#searchForm input[name='selWorkStatus']").eq(idx).val()) < 0) {
				$("#searchForm input[name='pushmessageIds']").eq(idx).prop("checked", false);
				$("#searchForm input[name='selStatus']").eq(idx).prop("checked", false);
				$("#searchForm input[name='selWorkStatus']").eq(idx).prop("checked", false);
				invalidStatus = true;
			}
		}
	});
	if(invalidStatus) {
		alert("발송대기, 발송승인 만 삭제 가능합니다.");
		return;
	}
	
	$("#status").val("002");
	var param = $("#searchForm").serialize();
	
	$.getJSON("./pushDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다."); 
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	}); 
}

// PUSH 신규등록 클릭시
function goPushAdd() {
	document.location.href = "./pushAddP.ums";	
}

// PUSH 업데이트 화면으로 이동
function goUpdate(pushmessageId) {
	$("#pushmessageId").val(pushmessageId);
	$("#searchForm").attr("target","").attr("action","./pushUpdateP.ums").submit();
} 

// 삭제된 목록의 체크박스 클릭시
function goDeleteClick() {
	alert("삭제된 목록은 선택할 수 없습니다.");
	return false;
}

function goPageNum(page) {
	goSearch(page);
}