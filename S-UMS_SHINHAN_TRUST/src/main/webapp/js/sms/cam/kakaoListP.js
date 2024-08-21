/**********************************************************
*	작성자 : 김재환
*	작성일시 : 2021.12.08
*	설명 : 카카오톡 알림톡 발송목록 JavaScript
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
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./kakaoList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divKakaoList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 초기화 버튼 클릭
function goInit(adminYn, deptNo) {
	var date = new Date();
	var firstday = new Date(date.getFullYear(), date.getMonth(), "01");
	$(".datepickerrange.fromDate input").datepicker('setDate', firstday);
	$(".datepickerrange.toDate input").datepicker('setDate', new Date());
	$("#searchTaskNm").val("");
	$("#searchGubun").val("");
	$("#searchCampNo").val("0");
	if(adminYn == "Y") {
		$("#searchDeptNo").val("0");
	} else {
		$("#searchDeptNo").val(deptNo);
	}
	$("#searchUserId").val("");
	$("#searchStatus").val("");
	$("#searchSmsStatus").val("");
}

// 목록 전체 체크
function goAll() {
	if($("#searchForm input[name='isAll']").is(":checked") == true) {
		$("#searchForm input[name='selKeygens']").each(function(idx,item){
			if($(item).is(":disabled") == false) {
				$("#searchForm input[name='selKeygens']").eq(idx).prop("checked", true); 
				$("#searchForm input[name='selStatus']").eq(idx).prop("checked", true);
			}
		});
	} else {
		$("#searchForm input[name='selKeygens']").prop("checked", false); 
		$("#searchForm input[name='selStatus']").prop("checked", false);
	}
} 
 
 
// 목록에서 체크박스 클릭시 taskNo와 subTaskNo, workStatus를 같이 체크해 준다.
function goKeygen(i) {
	var isChecked = $("#searchForm input[name='selKeygens']").eq(i).is(":checked");	
	$("#searchForm input[name='selStatus']").eq(i).prop("checked", isChecked);
	$("#searchForm input[name='selSmsStatus']").eq(i).prop("checked", isChecked);
} 


// 목록에서 발송상태(발송대기) 클릭시 -> 발송승인
function goAdmit(i) {
	var selKeygens =  $("#searchForm input[name='selKeygens']").eq(i).val() ;
	var selSmsStatus =  $("#searchForm input[name='selSmsStatus']").eq(i).val() ;
	if ( selSmsStatus == "001") {
		alert("삭제된 항목은 발송승인 할수 없습니다");
		return;
	}
	
	var msgid = selKeygens.substring(0, selKeygens.indexOf(":"));
	var keygen = selKeygens.substring(selKeygens.indexOf(":") + 1 , selKeygens.length );
	$("#msgid").val(msgid);
	$("#keygen").val(keygen);

	var a = confirm("발송승인 하시겠습니까?");
	if ( a ) {
		var param = $("#searchForm").serialize();
		
		$.getJSON("./smsAdmit.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("발송승인 되었습니다.");
				goSearch($("#searchForm input[name='page']").val());
			} else if(data.result == "Fail") {
				alert("발송승인 처리중 오류가 발생하였습니다.");
			}
		});
	} else return;

}

// 신규등록 클릭시
function goKakaoAdd() {
	$("#searchForm").attr("target","").attr("action","./kakaoAddP.ums").submit();
}

// 목록에서 제목 클릭시 : 메일 업데이트 화면으로 이동
function goUpdate(msgid, keygen) {
	$("#msgid").val( msgid );
	$("#keygen").val( keygen );
	$("#searchForm").attr("target","").attr("action","./kakaoUpdateP.ums").submit();
} 
 
function goPageNum(page) {
	goSearch(page);
}

// 복사 클릭시
function goKakaoCopy() {
	
	var checkedCount = 0;

	$("#searchForm input[name='selKeygens']").each(function(idx,item){
		if($(item).is(":checked")) checkedCount++;
	});
	if(checkedCount == 0 ) {
		alert("복사할 목록을 선택해 주세요.!!");
		return;
	}
	
	if(checkedCount > 1){
		alert("복사는 한개만 선택 가능합니다.");
		return;	
	}

	var param = $("#searchForm").serialize(); 
	$.getJSON("/sms/cam/kakaoCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
			
			// 문자 목록 재조회;
			goSearch('1');			
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});	
  
}
  
// 삭제 클릭시
function goKakaoDelete() { 
 
	// 선택항목체크
	var isChecked = false;
	$("#searchForm input[name='selKeygens']").each(function(idx,item){
		if($(item).is(":checked")) isChecked = true;
	});
	if(!isChecked) {
		alert("삭제할 목록을 선택해 주세요!!");
		return;
	} 
	// 상태체크
	var invalidStatus = false;
	$("#searchForm input[name='selKeygens']").each(function(idx,item){
		if($(item).is(":checked")) {
			if("000,001".indexOf($("#searchForm input[name='selStatus']").eq(idx).val()) < 0) {
				$("#searchForm input[name='selKeygens']").eq(idx).prop("checked", false);
				$("#searchForm input[name='selStatus']").eq(idx).prop("checked", false);
				$("#searchForm input[name='selSmsStatus']").eq(idx).prop("checked", false);
				invalidStatus = true;
			}
		}
	});
	if(invalidStatus) {
		alert("발송대기, 발송승인 만 삭제 가능합니다.");
		return;
	}
	$("#smsStatus").val("001");
	var param = $("#searchForm").serialize();
	
	$.getJSON("/sms/cam/smsDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다."); 
			// 문자 목록 재조회;
			goSearch('1');
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	}); 
}