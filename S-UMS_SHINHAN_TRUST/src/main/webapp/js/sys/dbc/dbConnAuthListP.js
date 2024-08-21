
$(document).ready(function() { 
 	$("#userNm").keypress(function(e) {
		if(e.which == 13) {
			getDbConnOrgUserList('');
		}
	}); 
});

// 부서 팝업 하위 조직목록 조회
function getDbConnOrgListView(upOrgCd) {
 
	$.getJSON("/sys/acc/getOrgListView.json?upOrgCd=" + upOrgCd, function(data) {		
		var orgHtml = "";
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			if(item.childCnt > 0) {  
				orgHtml += '<button type="button" class="btn-toggle" onclick="getDbConnOrgListView(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (parseInt(item.lvlVal) + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="getDbConnOrgUserList(\'' + item.orgCd + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
	});
	getDbConnOrgUserList(upOrgCd);
}

//사용자 검색
function getDbConnOrgUserList(orgCd){ 
	var param = "" ;
	
	if (orgCd ==""){
		param = "dbConnNo=" + $('#dbConnNo').val() + "&userNm=" + $('#userNm').val() ;  
	} else {
		param = "dbConnNo=" + $('#dbConnNo').val() + "&orgCd=" + orgCd ;
	} 
	
	var userListHtml = ""; 
	$("#userList").empty(); 
	$('#userNm').val("");
	
	var url = "/sys/dbc/dbConnUserList.json?";
	$.post(url, param, function(data) { 
	 
		$.each(data.dbConnUserList, function(idx,item){	
			userListHtml += '<li class="clear"id=' + 'lu_' + item.userId +'><label class="clear">';	
			userListHtml += '<span class="fl"><strong>' + item.userNm + '</strong><span>' + item.orgKorNm + ' | </span><span>' + item.jobNm + '</span></span>';
			userListHtml += '<span class="fr"><input type="checkbox" name="chkUser" id=' + 'cu_' + item.userId +' value="'+ item.userId + '" ><span></span></span>';			
			userListHtml += '</label></li>'; 
		});
		$("#userList").html(userListHtml);
	});
} 

//그룹 검색
function getDbConnDeptList(){ 
	var param = "" ;

	param = "dbConnNo=" + $('#dbConnNo').val() + "&deptNm=" + $('#deptNm').val() ;  

	var deptListHtml = ""; 
	$("#deptList").empty(); 
	$('#deptNm').val("");
	
	var url = "/sys/dbc/dbConnDeptList.json?";
	$.post(url, param, function(data) { 
	 
		$.each(data.dbConnDeptList, function(idx,item){	
			deptListHtml += '<li id=' + 'ld_' + item.deptNo +' class="clear"><label class="clear">';
			deptListHtml += '<span class="fl_last">'+ item.deptNm + '</span>';
			deptListHtml += '<span class="fr"><input type="checkbox" name="chkDept" id=' + 'cd_' + item.deptNo +' value="'+ item.deptNm + '" ><span></span></span>';
			deptListHtml += '</label></li>'; 
		});
		$("#deptList").html(deptListHtml);
	});
}

// 수정 클릭시
function goUpdate() {	
	if($("#dbConnNo").val() != "") {
  		
		var dbAuthUserList = document.getElementsByName("chkAuth");
		var dbAuthDeptList = document.getElementsByName("chkAuthDept");
		var userIds="";
		var deptNos="";
		
		if(dbAuthUserList.length > 0 ){
			for (var i = 0; i < dbAuthUserList.length; i++) {
				userIds += dbAuthUserList[i].value + ',';
			}
		}

		if(dbAuthDeptList.length > 0 ){
			for (var i = 0; i < dbAuthDeptList.length; i++) {
				deptNos += dbAuthDeptList[i].value + ',';
			}
		} 
		
		console.log ("goUpdate userIds : " + userIds);
		console.log ("goUpdate deptNos : " + deptNos);
		var param = "dbConnNo=" + $("#dbConnNo").val()+ "&userIds=" + userIds  + "&deptNos=" + deptNos;
		$.getJSON("./dbConnAuthUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정이 완료되었습니다");
				$("#page").val("1");
				$("#dbConnInfoForm").attr("target","").attr("action","./dbConnListP.ums").submit();
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("DB Connection 번호가 없습니다!");	
	}
} 

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#dbConnInfoForm").attr("target","").attr("action","./dbConnListP.ums").submit();
}
 
function goAdd(){ 
	var chkUser = document.getElementsByName('chkUser');
	var chkAuth = document.getElementsByName('chkAuth');
  	
	var authParentUl = document.getElementById('dbAuthUserList');
	var arrMove = new Array();  
		
	var arrRemMove = new Array(); 
	var arrRemIndex = 0; 
			
	if(chkUser.length > 0 ){
		for (var i = 0; i < chkUser.length; i++){ 
 			if (chkUser[i].checked == true ){				 
				var chkUserId = chkUser[i].value;   
				for (var j = 0 ; j < chkAuth.length ; j++){
					var chkAuthUserId = chkAuth[j].value; 
					if( chkUserId == chkAuthUserId ){ 
						arrRemMove[arrRemIndex] = chkUserId;
						arrRemIndex = arrRemIndex + 1;
					}
				}				  
				arrMove[i] = chkUserId; 
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remUserId = arrRemMove[i]; 
			var userLi = document.getElementById('la_' + remUserId); 
			if (userLi != undefined ){
			 	userLi.parentNode.removeChild(userLi);
			}
		}
	}	
		
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveUserId = arrMove[i]; 
			var userLi = document.getElementById('lu_' + moveUserId);
			var userCheck = document.getElementById('cu_' + moveUserId);
			if (userLi != undefined ){
				$(userLi).appendTo($(authParentUl));
				$(userLi).prop('id', 'la_' + moveUserId);
				
				if (userCheck != undefined ){					
					$(userCheck).prop("checked", false);
					$(userCheck).prop('name', 'chkAuth');
					$(userCheck).prop('id', 'ca_' + moveUserId);
				}
			}
		}
	}	 
}

function goRemove(){
	
	var chkUser = document.getElementsByName('chkUser');
	var chkAuth = document.getElementsByName('chkAuth');
  	 		
	var userParentUl = document.getElementById('userList');
	var arrMove = new Array(); 
	
 	var arrRemMove = new Array(); 
	var arrRemIndex = 0; 
	
	if(chkAuth.length > 0 ){
		for (var i = 0; i < chkAuth.length; i++){ 
 			if (chkAuth[i].checked == true ){				 
				var chkAuthUserId = chkAuth[i].value;  				
				for (var j = 0 ; j < chkUser.length ; j++){
					var chkUserId = chkUser[j].value ;
					if( chkAuthUserId == chkUserId ){
						arrRemMove[arrRemIndex] = chkUserId;
						arrRemIndex = arrRemIndex + 1;
					}
				}
				arrMove[i] = chkAuthUserId;  
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remUserId = arrRemMove[i]; 
			var userLi = document.getElementById('lu_' + remUserId); 
			if (userLi != undefined ){
			 	userLi.parentNode.removeChild(userLi);
			}
		}
	}	
		
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveUserId = arrMove[i]; 
			var mappLi = document.getElementById('la_' + moveUserId);
			var mappCheck = document.getElementById('ca_' + moveUserId);
			
			if (mappLi != undefined ){
				$(mappLi).appendTo($(userParentUl));
				$(mappLi).prop('id', 'lu_' + moveUserId);
				
				if (mappCheck != undefined ){					
					$(mappCheck).prop("checked", false);
					$(mappCheck).prop('name', 'chkUser');
					$(mappCheck).prop('id', 'cu_' + moveUserId);
				}				
			}
		}
	}		
}

function goAddDept(){
	var chkDept = document.getElementsByName('chkDept');
	var chkAuthDept = document.getElementsByName('chkAuthDept');
	
	var authParentUl = document.getElementById('dbAuthDeptList');
	var arrMove = new Array();
	
	var arrRemMove = new Array();
	var arrRemIndex = 0; 
	
	if(chkDept.length > 0 ){
		for (var i = 0; i < chkDept.length; i++){ 
 			if (chkDept[i].checked == true ){
				var chkDeptNo = chkDept[i].value;   
				for (var j = 0 ; j < chkAuthDept.length ; j++){
					var chkAuthDeptNo = chkAuthDept[j].value; 
					if( chkDeptNo == chkAuthDeptNo ){ 
						arrRemMove[arrRemIndex] = chkDeptNo;
						arrRemIndex = arrRemIndex + 1;
					}
				}
				arrMove[i] = chkDeptNo; 
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remDeptNo = arrRemMove[i]; 
			var deptLi = document.getElementById('ld_' + remDeptNo); 
			if (deptLi != undefined ){
				deptLi.parentNode.removeChild(deptLi);
			}
		}
	}	
	
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveDeptNo = arrMove[i]; 
			var deptLi = document.getElementById('ld_' + moveDeptNo);
			var deptCheck = document.getElementById('cd_' + moveDeptNo);
			if (deptLi != undefined ){
				$(deptLi).appendTo($(authParentUl));
				$(deptLi).prop('id', 'lad_' + moveDeptNo);
				
				if (deptCheck != undefined ){
					$(deptCheck).prop("checked", false);
					$(deptCheck).prop('name', 'chkAuthDept');
					$(deptCheck).prop('id', 'cad_' + moveDeptNo);
				}
			}
		}
	}
}

function goRemoveDept(){
	
	var chkDept = document.getElementsByName('chkDept');
	var chkAuthDept = document.getElementsByName('chkAuthDept');
	
	var deptParentUl = document.getElementById('deptList');
	var arrMove = new Array(); 
	
	var arrRemMove = new Array();
	var arrRemIndex = 0; 
	
	if(chkAuthDept.length > 0 ){
		for (var i = 0; i < chkAuthDept.length; i++){ 
 			if (chkAuthDept[i].checked == true ){
				var chkAuthDeptNo = chkAuthDept[i].value;
				for (var j = 0 ; j < chkDept.length ; j++){
					var chkDeptNo = chkDept[j].value ;
					if( chkAuthDeptNo == chkDeptNo ){
						arrRemMove[arrRemIndex] = chkDeptNo;
						arrRemIndex = arrRemIndex + 1;
					}
				}
				arrMove[i] = chkAuthDeptNo;
			}
		}
	}
	
	if (arrRemMove.length > 0 ){
		for(var i = 0 ; i < arrRemMove.length ; i++){
			var remDeptNo = arrRemMove[i]; 
			var deptLi = document.getElementById('ld_' + remDeptNo); 
			if (deptLi != undefined ){
			 	deptLi.parentNode.removeChild(deptLi);
			}
		}
	}
	
	if (arrMove.length > 0 ){
		for(var i = 0 ; i < arrMove.length ; i++){
			var moveDeptNo = arrMove[i]; 
			var mappLi = document.getElementById('lad_' + moveDeptNo);
			var mappCheck = document.getElementById('cad_' + moveDeptNo);
			
			if (mappLi != undefined ){
				$(mappLi).appendTo($(deptParentUl));
				$(mappLi).prop('id', 'ld_' + moveDeptNo);
				
				if (mappCheck != undefined ){
					$(mappCheck).prop("checked", false);
					$(mappCheck).prop('name', 'chkDept');
					$(mappCheck).prop('id', 'cd_' + moveDeptNo);
				}
			}
		}
	}
}