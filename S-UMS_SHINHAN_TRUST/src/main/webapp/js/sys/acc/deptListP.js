$(document).ready(function() {
	$("#searchDeptNm").keydown(function(e){
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
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./deptList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divDeptList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
} 

// 코드 또는 명 클릭시
function goUpdate(deptNo) {
	$("#deptNo").val(deptNo);
	$("#searchForm").attr("target","").attr("action","./deptUpdateP.ums").submit();
}

// 초기화 버튼 클릭시
function goReset() {
	$("#searchForm")[0].reset();
	//사용자그룹상태 default 값 정상 으로 세팅		
	$("#searchStatus").val("000");
}

// 신규등록 버튼 클릭시
function goAdd() {
	document.location.href = "./deptAddP.ums";
}
 
//삭제 EVENT 구현
function goDelete() { 
	 
	const query = 'input[name="delDeptNo"]:checked';
  	const checkboxs = document.querySelectorAll(query);
  	
	var deptNos="";
	if(checkboxs.length < 1 ){
		alert("삭제할 사용자그룹  정보를  선택해주세요");
		return;
	} else {
		for (var i = 0; i < checkboxs.length; i++) {
        deptNos += checkboxs[i].value + ',';	
    	}
	}
  
	$.getJSON("/sys/acc/deptDelete.json?deptNos=" + deptNos, function(data) {
		if(data) {
		 		alert("삭제에 성공 하였습니다");
				$("#page").val("1");
				$("#searchForm").attr("target", "").attr("action", "/sys/acc/deptListP.ums").submit();
			 
		} else {
			alert("삭제에 실패 하였습니다");
		}
	});  
} 
   
function selectAll(selectAll)  {
	$("input[name='delDeptNo']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
} 

function goPageNum(page) {
	goSearch(page);
}