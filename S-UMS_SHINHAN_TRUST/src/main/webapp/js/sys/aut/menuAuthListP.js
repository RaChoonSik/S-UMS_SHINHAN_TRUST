$(document).ready(function() { 
	
	getMenuList();

 	$("#userNm").keypress(function(e) {
		if(e.which == 13) {
			getUserList('');
		}
	}); 
}); 
 
$(document).on('click','input[type="checkbox"]', function(){
	var objName = this.getAttribute("name");
	var objId = this.getAttribute("id");
	var checked = true;
	var type = objName.charAt(3); 
	
	if (type !="M"){ 
		return;
	}
	
	if ($(this).is(":checked") == true ){checked = true;
	} else {checked = false;}
 
	if( objId != "" && objId != null && objId != undefined ){
		selectMenu(objName, objId, checked); 
	}
});

function selectMenu(objName, objId, checked) {
	if (objName.length >= 6 && objId.length >= 3 ) { 
		var lv = objName.substr(4, 2);  
		var targetcheck = document.querySelectorAll('.' + objId.replace("chkM", ""));		
		if (lv =="01") {
			for (var i = 0; i < targetcheck.length; i++) {
        		targetcheck[i].checked = checked;
				var subTargetcheck = document.querySelectorAll('.' + targetcheck[i].getAttribute("id").replace("chkM", ""));
				for (var j = 0; j < subTargetcheck.length; j++) {
					subTargetcheck[j].checked = checked;
				}
    		}	 
		} else if(lv =="02") {  
			for (var i = 0; i < targetcheck.length; i++) {
        		targetcheck[i].checked = checked;
			}
		} 
	} 
	getMenuUserList(); 
}

function getMenuList(){
	$("#menuList").empty();  

	$.getJSON("/sys/aut/getMenuList.json?", function(data) {
		$.each(data.userMenuList, function(idx,item){ 
			
			if(item.functionYn == "Y") {
				menuType ="기능항목";
			}   
			if (item.menuLvlVal == 0 ){ 
				var li=$('<li class="depth1"><ul><li class="col-box"><span class="col service"><button type="button" class="btn-toggle">' + item.menuNm + '</button></span><span class="col checkbox"><label><input type="checkbox" name="chkM01" id=chkM' + item.menuId + '><span></span></label></span></li></ul><ul class="depth2"><li id=' + item.menuId + '></li></ul></li>');
				 $(li).appendTo($("#menuList"));  
			} else if (item.menuLvlVal == 1 ){ 
				var parentUl = document.getElementById(item.parentMenuId); 
				var li= $('<ul><li class="col-box"><span class="col menu"><button type="button" class="btn-toggle">' + item.menuNm + '</button></span><span class="col checkbox"><label><input type="checkbox" name="chkM02" id=chkM' + item.menuId + ' class=' +item.parentMenuId + '><span></span></label></span></li></ul><ul class="depth3" id=' +item.menuId + '></ul>');
				$(li).appendTo(parentUl); 
			} else if (item.menuLvlVal = 2 ){ 
				var parentUl = document.getElementById(item.parentMenuId); 
				var li= $('<li class="col-box"><span class="col menu"><button type="button">' +item.menuNm + '</button></span><span class="col checkbox"><label><input type="checkbox" name="chkM03" id=chkM' + item.menuId + ' class=' +item.parentMenuId  + ' value=' + item.menuId + '|' +  item.menuNm.replace(' ','') + '><span></span></label></span></li>');
				$(li).appendTo(parentUl);
			} 
		});
	}); 
} 
 
// 부서 팝업 하위 조직목록 조회
function getViewOrgList(upOrgCd) {

	var addLevel = 1;
	
	$.getJSON("/sys/acc/getOrgListView.json?upOrgCd=" + upOrgCd, function(data) {
		// 조직 트리 설정
		var orgHtml = "";
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			if(item.childCnt > 0) {  
				orgHtml += '<button type="button" class="btn-toggle" onclick="getViewOrgList(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (parseInt(item.lvlVal) + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="getUserList(\'' + item.orgCd + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
	});
	//해당 부서 사용자를 조회한다 
	getUserList(upOrgCd);
	
}
function getUserList(orgCd){ 
	
	const query = 'input[name="chkM03"]:checked';
  	const checkboxs = document.querySelectorAll(query);
	
	if(checkboxs.length < 1 )
	{
		alert("권한을 설정할 메뉴를 먼저 선택해주세요!");
		return; 
	}

	$("#userList").empty(); 
	
	var evtType = "ser";	
	var param = "" ;	
	if (orgCd ==""){
		param = "userNm=" + $('#userNm').val() ; 
	} else {
		param = "orgCd=" + orgCd ;
	}
	$('#userNm').val("");
	$.post("/sys/aut/userList.json?", param,  function(data) { 
		$.each(data.userList, function(idx,item){
			var userListHtml = ""; 
			userListHtml += '<li id=' + 'lu_' + item.userId +'><label class="clear">';
			userListHtml += '<span class="fl"><strong>' + item.userNm + '</strong><span>' + item.orgKorNm + ' | </span><span>' + item.jobNm + '</span></span>';
			userListHtml += '<span class="fr"><input type="checkbox" name="chkUser" id=' + 'cu_' + item.userId +' value="'+ item.userId + '|' + 'add|' + '"><span>';
			userListHtml += '</label></li>'; 
			$("#userList").append(userListHtml);
		});
	}); 
}

// 메뉴 사용자 리스트 
function getMenuUserList() { 
	$("#hTitle").text("권한 매핑 사용자" );
	const query = 'input[name="chkM03"]:checked';
  	const checkboxs = document.querySelectorAll(query);
   
	$("#authUserList").empty();	
	$("#checkedMenuList").text("선택 : ");
	
	if (checkboxs.length == 0) {
		$("#userList").empty();		
		return;
	}
	 
	var menuIds ="";
	var menuNms =""; 
	for (var i = 0; i < checkboxs.length; i++) {
		var checkMenuVal =  checkboxs[i].value;
		var checkMenuInfo = checkMenuVal.split("|");
		var checkMenuId = checkMenuInfo[0];
		var checkMenuNm = checkMenuInfo[1];
    	menuIds +=checkMenuId + ',';
		menuNms +=checkMenuNm + ',';
	} 
		 
	$.getJSON("/sys/aut/getMenuUserList.json?menuIds=" + menuIds, function(data) {
		$.each(data.menuUserList, function(idx,item){
			var userListHtml = "";			
			userListHtml += '<li id=' + 'la_' + item.userId +'><label class="clear">';
			userListHtml += '<span class="fl"><strong>' + item.userNm + '</strong><span>' + item.orgNm + ' | </span><span>' + item.jobNm + '</span></span>';
			if(data.enable == "N") {
				userListHtml += '<span class="fr"><input type="checkbox" name="chkAuth" id=' + 'ca_' + item.userId +' value="'+ item.userId + '|' + 'auth|' + '" disabled="disabled" ><span>';
			 } else {
				userListHtml += '<span class="fr"><input type="checkbox" name="chkAuth" id=' + 'ca_' + item.userId +' value="'+ item.userId + '|' + 'auth|' + '" ><span>';
			}
			userListHtml += '</label></li>'; 
			$("#authUserList").append(userListHtml); 
		});  
			
		if(data.enable == "N") {
			//모든 컨트롤을 막는다 
			$('#btnRemove').attr('disabled','disabled');
			$('#btnAdd').attr('disabled','disabled');
			$("#enaleYn").val("N");
			$("#userList").empty();		
			$("#hTitle").text("권한 매핑 사용자(수정불가)" );
		} else {
			$('#btnRemove').removeAttr('disabled');
			$('#btnAdd').removeAttr('disabled');
			$("#enaleYn").val("Y"); 
		}
	});
	var menuNm = menuNms.replace(/,\s*$/, "");
 	$("#checkedMenuList").text( "선택 : " + menuNm); 
}
  
function goUpdate() {
	
	if ($("#enaleYn").val() != "Y"){
		alert("선택하신 메뉴에대한 권한 사용자가 동일하지 않습니다 저장하실수 없습니다");
		return;
		
	} 
	var mappUserList = document.getElementsByName("chkAuth");
	var updUserIds="";	
	var menuIds ="";
	if(mappUserList.length > 0 ){  
		for (var i = 0; i < mappUserList.length; i++) {
			var exInfo = mappUserList[i].value; 
			var exUser = exInfo.split("|"); 
			updUserIds += exUser[0] + ',';
		}
	} 
	
	const query = 'input[name="chkM03"]:checked';
  	const checkboxs = document.querySelectorAll(query);

	if (checkboxs.length > 0) {   
		for (var i = 0; i < checkboxs.length; i++) {
			var menuInfo =  checkboxs[i].value.split("|");
			menuIds += menuInfo[0] + ',';
    	}
	  
		$.getJSON("/sys/aut/menuAuthUpdate.json?menuIds=" + menuIds + "&userIds=" + updUserIds, function(data) {
			if(data) { 
				alert("저장 성공 하였습니다");
				document.location.href = "./menuAuthListP.ums";
			} else {
				alert("저장 실패 하였습니다 ");
				return;
			}
		});  
	}
} 
 
function goAdd(){ 
  
	var chkUser = document.getElementsByName('chkUser');
	var chkAuth = document.getElementsByName('chkAuth');
		 
	var authParentUl = document.getElementById('authUserList');
	var arrMove = new Array(); 
	var arrRemMove = new Array(); 
	var arrRemIndex = 0; 
	
	if(chkUser.length > 0 ){
		for (var i = 0; i < chkUser.length; i++){ 
 			if (chkUser[i].checked == true ){				 
				var chkUserVal = chkUser[i].value.split("|");
				var userId = chkUserVal[0];	
 
				for (var j = 0 ; j < chkAuth.length ; j++){
					var chkAuthVal = chkAuth[j].value.split("|");
					var chkAuthUserId =  chkAuthVal[0]
					if( userId == chkAuthUserId){
						arrRemMove[arrRemIndex] = chkAuthUserId;
						arrRemIndex = arrRemIndex + 1;
					} 
				}	 
				arrMove[i] = userId; 
				
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
				var chkAuthVal = chkAuth[i].value.split("|");
				var userId = chkAuthVal[0];	
				//같은 id가 있는지 찾는다 
				
				for (var j = 0 ; j < chkUser.length ; j++){
					var chkUserVal = chkUser[j].value.split("|");
					var chkUserId = chkUserVal[0]; 
					if( userId == chkUserId){
						arrRemMove[arrRemIndex] = chkUserId;
						arrRemIndex = arrRemIndex + 1;
					}
				}
				arrMove[i] = userId; 
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
 