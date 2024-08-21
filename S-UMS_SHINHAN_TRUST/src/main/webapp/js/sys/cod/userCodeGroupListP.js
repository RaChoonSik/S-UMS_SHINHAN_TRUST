$(document).ready(function() {
	$("#searchCdGrpNm").keydown(function(e){
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
		url : "./userCodeGroupList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divUserCodeGroupList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 코드그룹 또는 명 클릭시
function goUpdate(cdGrp) {
	$("#cdGrp").val(cdGrp);	
	$("#searchForm").attr("target","").attr("action","./userCodeGroupUpdateP.ums").submit();
}

// 초기화 버튼 클릭시
function goReset() {
	$("#searchForm")[0].reset();
} 

// 신규등록 버튼 클릭시
function goAdd() {
	document.location.href = "./userCodeGroupAddP.ums";
}
 
//삭제 EVENT 구현
function goDelete() {
	
	const query = 'input[name="delCdGrp"]:checked';
  	const checkboxs = document.querySelectorAll(query);
  	
	var cdGrps="";
	if(checkboxs.length < 1 ){
		alert("삭제할 분류코드를  선택해주세요");
		return;
	} else {
		for (var i = 0; i < checkboxs.length; i++) {
        		cdGrps += checkboxs[i].value+ ',';	
    	}
	} 
	
	$.getJSON("/sys/cod/userCodeGroupDelete.json?cdGrps=" + cdGrps, function(data) {
		if(data) {
	 		alert("삭제에 성공 하였습니다");
			goPageNum(1);
			//$("#page").val("1");
			//$("#searchForm").attr("target","").attr("action","/sys/cod/userCodeGroupListP.ums").submit();
		} else {
			alert("삭제에 실패 했습니다");
		}
	});  
} 
 
// 검색 코드 분류 변경시 
function change() {
	$("#searchCdGrpNm").val("");
	goPageNum(1);
}

function selectAll(selectAll)  {
	$("input[name='delCdGrp']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
}

function goPageNum(page) {
	//$("#searchForm input[name='page']").val(page);
	goSearch(page);
}