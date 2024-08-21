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
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./domainList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divDomainList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}   

// 도메인 추가
function goAdd(domainId) {
	$("#searchForm").attr("target","").attr("action","./domainAddP.ums").submit();
}

// 도메인수정
function goUpdate(domainId) {
	$("#domainId").val(domainId);
	$("#searchForm").attr("target","").attr("action","./domainUpdateP.ums").submit();
}

//삭제 EVENT 구현
function goDelete() { 
	 
	const query = 'input[name="delDomainId"]:checked';
  	const checkboxs = document.querySelectorAll(query);
  	
	var domainIds="";
	if(checkboxs.length < 1 ){
		alert("삭제할 도메인  정보를  선택해주세요");
		return;
	} else {
		for (var i = 0; i < checkboxs.length; i++) {
        	domainIds += checkboxs[i].value + ',';	
    	}
	}
  
	$.getJSON("/sys/rns/domainDelete.json?domainIds=" + domainIds, function(data) {
		if(data) {
		 	alert("삭제에 성공 하였습니다");
			$("#page").val("1");
			$("#searchForm").attr("target", "").attr("action", "/sys/rns/domainListP.ums").submit();			
		} else {
			alert("삭제에 실패 하였습니다");
		}
	});  
} 

function goPageNum(page) {
	goSearch(page);
}

function selectAll(selectAll)  {
	$("input[name='delDomainId']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
} 