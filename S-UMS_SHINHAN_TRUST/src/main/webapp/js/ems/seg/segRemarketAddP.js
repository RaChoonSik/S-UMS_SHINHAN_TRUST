/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.08.19
*	설명 : 수신자그룹 신규등록(리타게팅) JavaScript
**********************************************************/

// 페이지 로딩 후 바로 실행
$(document).ready(function(){
	//등록일시 : datePicker
	var date = new Date();
	var firstday = new Date(date.getFullYear(), date.getMonth(), "01");
	$(".datepickerrange.fromDate input").datepicker('setDate', firstday);
	$(".datepickerrange.toDate input").datepicker('setDate', new Date());
});

// 탭 클릭시 페이지 이동
function goCreateTy(no) {
    var actionUrl;
    
    if(no == '000') actionUrl = "./segToolAddP.ums";      	// 추출도구이용
    if(no == '002') actionUrl = "./segDirectSQLAddP.ums";	// SQL 직접 입력
    if(no == '003') actionUrl = "./segFileAddP.ums";   		// 파일그룹
    if(no == '004') actionUrl = "./segRemarketAddP.ums";    // 연계서비스(리타게팅) 지정
    
    document.location.href = actionUrl;
}

//사용자그룹 선택시 사용자 목록 설정
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

//사용자그룹 선택시 사용자 목록 설정(메일검색 팝업)
function getUserListMail(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}

// 검색 클릭시(메일 리스트 팝업 : 메일 찾기)
function openInfo() {
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./segRemarketMailListP.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
			fn.popupOpen('#popup_mailsearch');
		},
		error : function(){
			alert("Error!!");
		}
	});
}

// 메일검색에서 메일 선택시 발생
function goMailSelect(taskNo, subTaskNo, taskNm) {
	$("#taskNo").val(taskNo);
	$("#subTaskNo").val(subTaskNo);
	$("#srcWhere").val(taskNo + "|" + subTaskNo + "|" + taskNm);
	$("#taskNm").val(taskNm);
	
	fn.popupClose('#popup_mailsearch');
}

// 메일검색 페이징
function goPageNumMail(pageNum) {
	$("#searchForm input[name='page']").val( pageNum );
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./segRemarketMailListP.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

// 메일검색 초기화
function goResetMail() {
	$("#searchForm")[0].reset();
}

// 모든 조건 선택 해제
function fAllClose() {
	$("#segInfoForm input[name='firstWhere']").each(function(idx,item){
		$(item).prop("checked", false);
	});
	fSecondClose();
}

// 2차 조건 모두 선택 해제
function fSecondClose() {
	$("#segInfoForm input[name='secondWhere']").each(function(idx,item){
		$(item).prop("checked", false);
	});
}

// 1차 조건 클릭시(Select 절 구하기)
function goSelect() {
	var fromSql = "";
	var whereSql = "";
	var firstWhere = "";

	if($("#taskNm").val() == "") {
		fAllClose();
		alert("먼저 메일명을 선택해 주세요");
		return;
	}

	firstWhere = $("#segInfoForm input[name='firstWhere']:checked").val();
	if(firstWhere == '001') {   // 성공
		fSecondClose();
		fromSql = "NEO_SENDLOG";
		whereSql  = "NEO_SENDLOG.SEND_RCODE = '000' AND NEO_SENDLOG.TASK_NO =  " + $("#taskNo").val();
		$("#segFlPath").val("1|0");
	} else if(firstWhere == '002') { // 오픈
		fSecondClose();
		fromSql = "NEO_SENDLOG, NEO_RESPLOG";
		whereSql  = "NEO_RESPLOG.TASK_NO =  " + $("#taskNo").val();
		whereSql += " AND NEO_SENDLOG.CUST_ID = NEO_RESPLOG.CUST_ID";
		$("#segFlPath").val("2|0");
	}

	$("#fromSql").val( fromSql );
	$("#whereSql").val( whereSql );
}

// 2차 조건 클릭시(실패 구하기)
function fFail() {
	var fromSql = "NEO_SENDLOG";
	var whereSql = "";
	var firstWhere = "";
	var segFlPath = "";

	if($("#taskNm").val() == "") {
		fAllClose();
		alert("먼저 메일명을 선택해 주세요");
		return;
	}

	firstWhere = $("#segInfoForm input[name='firstWhere']:checked").val();
	if(firstWhere != "003") {
		fSecondClose();
		alert("2차 조건은 1차 조건의 실패를 선택해야만 가능합니다.");
		return;
	}

	segFlPath = "3|";

	var j = 0;
	$("#segInfoForm input[name='secondWhere']").each(function(idx,item){
		if($(item).is(":checked") == true) {
			if(j != 0) {
				whereSql += ", ";
			} else {
				whereSql += "NEO_SENDLOG.SEND_RCODE IN (";
			}
			whereSql += "'"+ $(item).val() +"'";
			segFlPath += $(item).val() + "|";
			j++;
		}
	});

	if(j > 0) whereSql += ") AND NEO_SENDLOG.TASK_NO =  " + $("#taskNo").val();

	$("#fromSql").val( fromSql );
	$("#whereSql").val( whereSql );
	$("#segFlPath").val( segFlPath );
}

// 대상수 구하기
function goSegCnt() {
	if($("#dbConnNo").val() == "") {
		alert("Connection 을 선택해 주세요.");
		return;
	}
	if($("#selectSql").val() == "" || $("#fromSql").val() == "") {
		alert("쿼리문을 잘못 입력하셨습니다.");
		return;
	}

	var param = $("#segInfoForm").serialize();
	$.getJSON("./segCount.json?" + param, function(data) {
		$("#txtTotCnt").html(data.totCnt + "명");
		$("#totCnt").val(data.totCnt);
	});
}

// 등록
function goRemarketAdd() {
	if($("#taskNm").val() == "") {
		alert("메일명을 선택해주세요.");
		openInfo();
		return;
	}
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "0"){
			alert("사용자그룹은 필수입력 항목입니다.");
			$("#deptNo").focus();
			return;	
		}		
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			alert("사용자는 필수입력 항목입니다.");
			$("#userId").focus();
			return;
		}
	}
	if($("#segNm").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#segNm").focus();
		return;
	}
	if($("#selectSql").val() == "" || $("#fromSql").val() == "") {
		alert("질의문이 올바르지 않습니다.");
		return;
	}
	
	// 입력값 Byte 체크
	if($.byteString($("#segNm").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#segNm").focus();
		$("#segNm").select();
		return true;
	}
	if($.byteString($("#segDesc").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#segDesc").focus();
		$("#segDesc").select();
		return true;
	}
	
	if($("#totCnt").val() == "0") {
		var a = confirm("대상자수 추출을 하지 않았습니다.\n계속 실행을 하겠습니까?");
		if ( a ) {
			var param = $("#segInfoForm").serialize();
			$.getJSON("./segAdd.json?" + param, function(data) {
				if(data.result == "Success") {
					alert("등록되었습니다.");
					document.location.href ="./segMainP.ums";
					//$("#searchForm").attr("action","./segMainP.ums").submit();
				} else if(data.result == "Fail") {
					alert("등록 처리중 오류가 발생하였습니다.");
				}
			});
		} else return;
	} else {
		var param = $("#segInfoForm").serialize();
		$.getJSON("./segAdd.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("등록되었습니다.");
				document.location.href ="./segMainP.ums";
				$("#searchForm").attr("action","./segMainP.ums").submit();
			} else if(data.result == "Fail") {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 미리보기 클릭시
function goSegInfo() {
	if($("#dbConnNo").val() == "") {
		alert("Connection 을 선택해 주세요.");
		return;
	}
	if($("#selectSql").val() == "" || $("#fromSql").val() == "") {
		alert("쿼리문을 잘못 입력하셨습니다.");
		return;
	}
	
	$("#previewSegNm").html( $("#segNm").val() );
	var sql = "SELECT " + $("#selectSql").val() + " FROM " + $("#fromSql").val();
	if($("#whereSql").val() != "") {
		sql += " WHERE " + $("#whereSql").val();
	}
	if($("#orderbySql").val() != "") {
		sql += " ORDER BY " + $("#orderbySql").val();
	}
	$("#previewSql").html( sql );
	
	var mergeKey = $("#mergeKey").val().split(",");
	var mergeCol = $("#mergeCol").val().split(",");
	for(var i=0;i<mergeKey.length;i++) {
		var option = new Option(mergeKey[i], mergeCol[i]);
		$("#previewSearch").append(option);
	}
	
	// 대상수 구하기
	goSegCnt();
	
	if(checkSearchReason) {
		goPageNumSeg("1");
	}
	fn.popupOpen('#popup_preview_seg');
}

// 미리보기 페이징(ums.common.js 변수&함수 포함)
function goPageNumSeg(pageNum) {
	$("#page").val( pageNum );
	var param = $("#segInfoForm").serialize();
	$.ajax({
		type : "GET",
		url : "./segDbMemberListP.ums?" + param + "&checkSearchReason=" + checkSearchReason + "&searchReasonCd=" + $("#searchReasonCd").val() + "&contentPath=" + window.location.pathname,
		dataType : "html",
		success : function(pageHtml){
			checkSearchReason = true;
			$("#previewMemberList").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

// 취소 클릭시
function goCancel() {
	alert("등록이 취소되었습니다.");
	document.location.href = "./segMainP.ums";
}
