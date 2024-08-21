$(document).ready(function() {		
	goSearch("1");
}); 

// 검색 버튼 클릭시
function goSearch(pageNo) {
  	console.log($("#orgKorNm").val());
	var orgCd = $("#orgCd").val();
	console.log(orgCd);
	$("#searchForm input[name='page']").val(pageNo); 
	$("#searchForm input[name='searchOrgCd']").val(orgCd);
	var param = $("#searchForm").serialize();	
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./userList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divUserList").html(pageHtml);

		},
		error : function(){
			alert("List Data Error!!");
		}
	});
 
} 

// 코드 또는 명 클릭시
function goUpdate(userId) {
	$("#userId").val(userId);
	$("#searchForm").attr("target","").attr("action","./userUpdateP.ums").submit();
}

// 신규등록 버튼 클릭시
function goAdd() {
	document.location.href = "./userAddP.ums";
}
 
//삭제 EVENT 구현
function goDelete() { 
	 
	const query = 'input[name="delUserId"]:checked';
  	const checkboxs = document.querySelectorAll(query);
  	
	var users="";
	if(checkboxs.length < 1 ){
		alert("삭제할 사용자를  선택해주세요");
		return;
	} else {
		for (var i = 0; i < checkboxs.length; i++) {
        	users += checkboxs[i].value + ',';	
    	}
	}
	  
  
	$.post("/sys/acc/userDelete.json?userIds=" + users, function(data) {
		if(data) {
		 		alert("삭제에 성공 하였습니다");
				$("#page").val("1");
				$("#searchForm").attr("target", "").attr("action", "/sys/acc/userListP.ums").submit();
			 
		} else {
			alert("삭제에 실패 하였습니다");
		}
	});  
} 
 
// 검색 코드 분류 변경시 
function change(){
	goSearch("1");
}

// 초기화 버튼 클릭시
function goReset() {
	$("#orgCd").val("");
	$("#searchForm")[0].reset();
}

function selectAll(selectAll)  {
	$("input[name='delUserId']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
} 

function goPageNum(page) {
	goSearch(page);
}