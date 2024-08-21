$(document).ready(function() {		
	goSearch("1");
}); 

// 검색 버튼 클릭시
function goSearch(pageNo) { 
	$("#searchForm input[name='page']").val(pageNo);  
	var param = $("#searchForm").serialize();	
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./userAuthList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divUserAuthList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
} 

// 코드 또는 명 클릭시
function goUpdate(userId) { 	
	$("#userId").val(userId);
	$("#searchForm").attr("target","").attr("action","./userAuthUpdateP.ums").submit();
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
	 
	$.getJSON("/sys/aut/userAuthDelete.json?userIds=" + users, function(data) {
		if(data) {
	 		alert("삭제에 성공 하였습니다");
			$("#page").val("1");
			$("#searchForm").attr("target", "").attr("action", "/sys/aut/userAuthListP.ums").submit();
		} else {
			alert("삭제에 실패 하였습니다");
		}
	});  
}  

// 초기화 버튼 클릭시
function goReset() {
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
 
// 사용자 권한 조회 클릭시
function popUserAuthSearch(userId) {
 
	$.getJSON("/sys/aut/getUserAuthInfo.json?userId=" + userId, function(data) {
		var userAuthInfo = ""; 
		$.each(data.userAuthList, function(idx,item){ 	 
			userAuthInfo += '<li class="clear"> <span class="fl"><strong>' + item.lv1Nm + '</strong><span>' + ' > '  +  item.lv2Nm + '</span><span>'  + ' > '  +  item.lv3Nm + '</span></span></li>'		 
		});	 
		$("#ulUserAuthList").html(userAuthInfo );	
	}); 
		
 	fn.popupOpen("#popup_user_auth_search");
}

