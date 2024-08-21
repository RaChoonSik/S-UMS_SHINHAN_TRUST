$(document).ready(function() {
	$("#searchDbConnNm").keydown(function(e){
		if(e.keyCode == 13) {
			e.preventDefault();
		}
	});	
	goSearch("1");
}); 

// 검색 버튼 클릭시
function goSearch(pageNo) {
  
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	
	$.ajax({
		type : "GET",
		url : "./dbConnList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divDbConnList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
} 

// 코드 또는 명 클릭시
function goUpdate(dbConnNo) {
	$("#dbConnNo").val(dbConnNo);
	$("#searchForm").attr("target","").attr("action","./dbConnUpdateP.ums").submit();
}
 
// 초기화 버튼 클릭시
function goReset() {
	$("#searchForm")[0].reset();
} 

// 신규등록 버튼 클릭시
function goAdd() {
	document.location.href = "./dbConnAddP.ums";
}

//삭제 EVENT 구현
function goDelete() { 
	 
	var query = 'input[name="dbConnNo"]:checked';
  	var checkboxs = document.querySelectorAll(query);
  	var dbConnNos ="";
	if(checkboxs.length < 1 ){
		alert("삭제할 DB 연결 정보를  선택해주세요");
		return;
	} else {
		for (var i = 0; i < checkboxs.length; i++) {
        	dbConnNos += checkboxs[i].value + ',';	
    	}
	}
	 
	$.getJSON("/sys/dbc/dbConnDelete.json?dbConnNos=" + dbConnNos, function(data) {
		if(data) {
		 		alert("삭제에 성공 하였습니다");
				$("#page").val("1");
				$("#searchForm").attr("target", "").attr("action", "/sys/dbc/dbConnListP.ums").submit();
			 
		} else {
			alert("Error!!");
		}
	});  
} 
 
// 권한관리
function setGrant(dbConnNo) { 
	$("#dbConnNo").val(dbConnNo);
	$("#searchForm").attr("target","").attr("action","./dbConnAuthListP.ums").submit();
}

// 메타정보관리
function setMeta(dbConnNo) {	
	$("#dbConnNo").val(dbConnNo);
	$("#searchForm").attr("target","").attr("action","./dbConnMetaP.ums").submit();
}

// 조인정보관리
function setJoin(dbConnNo) {
	$("#dbConnNo").val(dbConnNo);
	$("#searchForm").attr("target","").attr("action","./dbConnMetaJoinP.ums").submit();
}

// 검색 코드 분류 변경시 
function change() {
	goSearch("1");
}
 
function selectAll(selectAll)  {
	$("input[name='dbConnNo']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
}
 
function goPageNum(page) {
	goSearch(page);
}