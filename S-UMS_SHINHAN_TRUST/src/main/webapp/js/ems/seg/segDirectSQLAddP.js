/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.08.18
*	설명 : 수신자그룹 신규등록(직접SQL) JavaScript
**********************************************************/

// 페이지 로딩 후 바로 실행
$(document).ready(function() {
	getMetaFrameContent();
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

// 사용자그룹 선택시 사용자 목록 설정
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

// Connection 변경시 재조회
function goReload() {
	$("#dbConnNo").val($("#dbConnNo").val());
	$("#segInfoForm").attr("action","./segDirectSQLAddP.ums").submit();
}

//메타 테이블 컨텐츠 생성
function getMetaFrameContent() {
	if ($("#dbConnNo").val() == null || $("#dbConnNo").val() == "") {
		alert("사용가능한 DB Connection 연결이 없습니다. 권한을 먼저 설정해주세요");
		return;
	}
	var dbConnNo = $("#dbConnNo").val();
	
	$.ajax({
		type : "GET",
		url : "./metatableListP.ums?dbConnNo=" + dbConnNo,
		dataType : "html",
		success : function(pageHtml){
			$("#divMetaTableInfo").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

// 메타 컬럼 컨텐츠 생성
/*
function goColumnInfo(tblNm) {
	var dbConnNo = $("#dbConnNo").val();
	$.ajax({
		type : "GET",
		url : "./metacolumnListP.ums?dbConnNo=" + dbConnNo + "&tblNm=" + tblNm,
		dataType : "html",
		success : function(pageHtml){
			$("#divMetaColumnInfo").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}
*/
function goColumnInfo(tblNo) {
	var dbConnNo = $("#dbConnNo").val();
	$.ajax({
		type : "GET",
		url : "./metacolumnListP.ums?dbConnNo=" + dbConnNo + "&tblNo=" + tblNo,
		dataType : "html",
		success : function(pageHtml){
			$("#divMetaColumnInfo").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

// 대상수 구하기
function goSegCnt() {
	if($("#dbConnNo").val() == "") {
		alert("Connection을 선택하세요.");
		return;
	}
	if($("#query").val() == "") {
		alert("QUERY를 입력하세요.");
		return;
	}
	
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segCount.json?" + param, function(data) {
		$("#txtTotCnt").html(data.totCnt + "명");
		$("#totCnt").val(data.totCnt);
	});
}

function invalidQuery(){
	var invalid = false;
	var validStr = "";
	var query = "";
	var alertMsg = "";
	//재실행 쿼리 체크 
	
	if($("#retryQuery").val() != "") {
		validStr = "NEO_SENDLOG";
		query = $("#retryQuery").val();
		query = query.replace(/(\r\n|\n|\r)/gm, " ");
		query = query.toUpperCase(); 
		if(query.indexOf(validStr) == -1){
			alertMsg ="[재실행쿼리] NEO_SENDLOG 테이블은 필수입니다";
			invalid = true; 
		}
	}
	
	if(invalid){
		alert(alertMsg);
	}
	
	return invalid;
}
// QUERY TEST 클릭시
// SQL 문 유효성 체크및 필수 머지키 (ID, NAME, EMAIL) 확인
// MERGY_KEY 정보 설정
function goQueryTest(type) {
	if($("#query").val() == "") {
		alert("QUERY를 입력하세요.");
		return;
	}
	
	if (invalidQuery()) {
		return;
	}
	
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segDirectSQLTest.json?" + param, function(data) {
		if(data.result == 'Success') {
			$("#mergeKey").val(data.mergeKey);
			$("#mergeCol").val(data.mergeKey);
			if(type == "000") {		// QUERY TEST
				alert("성공.\n[MERGE_KEY : " + data.mergeKey + "]");
			} else if(type == "001") {
				goDirectSQLAdd();	// 등록
			} else if(type == "002") {
				goSegInfo();		// 미리보기
			}
		} else if(data.result == 'Fail') {
			alert("실패\n" + data.message);
		}
	});
}

//   직접 SQL 이용 발송대상(세그먼트) 등록
function goDirectSQLAdd() {
	if($("#dbConnNo").val() == "") {
		alert("Connection은 필수입력 항목입니다.");
		$("#dbConnNo").focus();
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
	if($("#query").val() == "") {
		alert("QUERY는 필수입력 항목입니다.");
		$("#query").focus();
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
					document.location.href = "./segMainP.ums";
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
				document.location.href = "./segMainP.ums";
				//$("#searchForm").attr("action","./segMainP.ums").submit();
			} else if(data.result == "Fail") {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 미리보기 클릭시
function goSegInfo() {
	if($("#dbConnNo").val() == "") {
		alert("Connection을 선택하세요.");
		return;
	}
	if($("#query").val() == "") {
		alert("QUERY를 입력하세요.");
		return;
	}
	
	$("#previewSegNm").html( $("#segNm").val() );
	$("#previewSql").html( $("#query").val() );
	
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
	document.location.href = "./segMainP.ums";
}
