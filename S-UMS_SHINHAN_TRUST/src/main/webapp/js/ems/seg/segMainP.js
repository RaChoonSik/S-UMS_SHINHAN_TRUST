$(document).ready(function() {
	goSearch("1");
});

function goChagePerPageRows(rows){
	$("#searchForm input[name='rows']").val(rows);
	goSearch("1");
}

// 사용자그룹 선택시 사용자 목록 조회 
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}

// 조회
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
		url : "./segList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divSegList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 실패하였습니다");
		}
	});
}

// 검색조건을 초기화 함
function goInit() {
	$("#searchForm")[0].reset();
}

// 등록
function goAdd() {
	document.location.href = "./segDirectSQLAddP.ums";
	//$("#searchForm").attr("target","").attr("action","./segFileAddP.ums").submit();
}

// 사용중지 EVENT 구현
function goDisable() {
	var checked = false;
	if($("input[name='segNos']").val() != "undefined") {
		$("input[name='segNos']").each(function(idx,item) {
			if($(item).is(":checked") == true) {
				checked = true;
			}
		});
	}
	if(!checked) {
		alert("사용중지 할 목록을 선택해 주세요.");
		return;
	}

	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == 'Success') {
			alert("사용중지되었습니다.");
			goSearch("1");
			//$("#searchForm").attr("target","").attr("action","./segMainP.ums").submit();
		} else if(data.result == 'Fail') {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 EVENT 구현
function goDelete() {
	var checked = false;
	if($("input[name='segNos']").val() != "undefined") {
		$("input[name='segNos']").each(function(idx,item) {
			if($(item).is(":checked") == true) {
				checked = true;
			}
		});
	}
	if(!checked) {
		alert("삭제할 목록을 선택해 주세요!!");
		return;
	}

	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == 'Success') {
			alert("삭제되었습니다.");
			goSearch("1");
			//$("#searchForm").attr("target","").attr("action","./segMainP.ums").submit();
		} else if(data.result == 'Fail') {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	});
}

// 목록에서 전체 체크박스 클릭시
function goSelectAll() {
	if($("#searchForm input[name='isAll']").is(":checked")) {
		$("#searchForm input[name='segNos']").each(function(idx,item){
			$(item).prop("checked", true);
		});
	} else {
		$("#searchForm input[name='segNos']").each(function(idx,item){
			$(item).prop("checked", false);
		});
	}
}

// 삭제된 항목의 체크박스 클릭시
function goDeleteClick() {
	alert("삭제된 목록은 선택할 수 없습니다.");
	return false;
}

// 목록에서 발송대상(세그먼트) 클릭시 수정화면 이동
function goUpdate(segNo,createTy,filePath) {
	$("#searchForm input[name='segNo']").val(segNo);
	$("#searchForm input[name='createTy']").val(createTy);
	
	var actionUrl = "";
	if(createTy == '000') actionUrl = "./segToolUpdateP.ums";
	if(createTy == '001') actionUrl = "./segOneClickUpdateP.ums";
	if(createTy == '002') actionUrl = "./segDirectSQLUpdateP.ums";
	if(createTy == '003') actionUrl = "./segFileUpdateP.ums";
	if(createTy == '004') actionUrl = "./segRemarketUpdateP.ums";
	
	$("#searchForm").attr("target","").attr("action",actionUrl).submit();
}

// 페이지 이동
function goPageNum(page) {
	goSearch(page);
	//$("#searchForm").attr("target","").attr("action","./segMainP.ums").submit();
}
