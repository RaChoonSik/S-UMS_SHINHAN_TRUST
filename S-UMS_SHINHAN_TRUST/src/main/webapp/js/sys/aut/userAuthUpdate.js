$(document).ready(function() {
	
	var userId = $('input[name=userId]').val();
	$("#userMenuList").empty();
		 
	$.getJSON("/sys/aut/getUserMenuInfo.json?userId=" + userId, function(data) {
		$.each(data.userMenuList, function(idx,item){
			if (item.menuLvlVal == 0 ){ 
				var li= $('<li><button type="button" class="btn-toggle">' + item.menuNm + '</button><label><input type="checkbox" name="chkM01" id=chk' + 'M'+ item.menuId + '><span></span></label><ul id=' + "M"+ item.menuId +' class="depth2"></ul></li>');
				 $(li).appendTo($("#userMenuList")); 
			} else if (item.menuLvlVal == 1 ){
				var parentUl = document.getElementById('M' +item.parentMenuId); 
				var li= $('<li><button type="button" class="btn-toggle">' + item.menuNm  + '</button><label><input type="checkbox" name="chkM02" id=chk' + 'M'+ item.menuId + ' class=' + 'M' +item.parentMenuId  + '><span></span></label><ul id='+ 'M'+ item.menuId +' class="depth3"></ul></li>');
				$(li).appendTo(parentUl); 
			} else {
				var parentUl = document.getElementById('M' + item.parentMenuId);
				if (item.mappYn == "Y" ){
					var li= $('<li id=' + 'lM' + item.menuId   +' style="display:none"><button type="button" id=' + item.menuId + '>' + item.menuNm + '</button><label><input type="checkbox" name="chkM03" class=' + 'M' +item.parentMenuId  + ' value=' + item.menuId + '|' + item.menuNm + '|' + item.lv1Nm + '|' + item.lv2Nm + '|Y|' + '><span></span></label><li>');
				} else {
					var li= $('<li id=' + 'lM' + item.menuId   +' ><button type="button" id=' + item.menuId + '>' + item.menuNm + '</button><label><input type="checkbox" name="chkM03" class=' + 'M' +item.parentMenuId  + ' value=' + item.menuId + '|' + item.menuNm + '|' + item.lv1Nm + '|' + item.lv2Nm + '|N|' + '><span></span></label><li>');
				}
				console.log(li);
				$(li).appendTo(parentUl);
			}
		});
	});
 
	$("#userAuthList").empty();
		
	$.getJSON("/sys/aut/getUserAuthInfo.json?userId=" + userId, function(data) {
		var userAuthHtml = "";		
		$.each(data.userAuthList, function(idx,item){
			userAuthHtml += '<li id=' + 'lA' + item.menuId   +'><label class="clear"><span class="fl">';
		 	userAuthHtml += '<strong>' + item.lv1Nm + '</strong><span>' + item.lv2Nm + '</span> > <span>' + item.lv3Nm + '</span></span>';
			userAuthHtml += '<span class="fr"><input type="checkbox" name="userAuth" value="' + item.menuId + '"><span></span></span></label></li>';
			
		});	
		$("#userAuthList").html(userAuthHtml);	
	}); 
}); 

$(document).on('click','input[type="checkbox"]', function(){ 
	select(this);
});

// 접근대상 메뉴 클릭시 상위 클릭 하면 하위도 모드 선택
function select(obj) {
	var objName = obj.getAttribute("name");
	var objId = obj.getAttribute("id");
	var checked = true;
	var type = objName.charAt(3); 
	if (type !="M"){ 
		return;
	}
	
	if ($(obj).is(":checked") == true ){checked = true;
	} else {checked = false;}
 
	if( objId != "" && objId != null && objId != undefined ){
		selectMenu(objName, objId, checked);	 
	}
}

function selectMenu(name, id, checked){
	if (name.length >= 6 && id.length >= 3 ) { 
		var lv = name.substr(4, 2);  
		var targetcheck = document.querySelectorAll('.' + id.replace("chk", ""));		
		if (lv =="01") {
			for (var i = 0; i < targetcheck.length; i++) {
				targetcheck[i].checked = checked;
				var subTargetcheck = document.querySelectorAll('.' + targetcheck[i].getAttribute("id").replace("chk", ""));
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
}

function goUpdate() {	
	 
	if($("#userId").val() != "") {
	  
	    var arrMenuIds = document.getElementsByName("userAuth");	  	
		var menuIds="";
		if(arrMenuIds.length > 0 ){ 
		 for (var i = 0; i < arrMenuIds.length; i++) {
				menuIds += arrMenuIds[i].value + ",";
			}
		}
		
		console.log("대상:" + menuIds);
		$('#menuIds').val(menuIds);

		var param = $("#userAuthInfoForm").serialize();
		console.log(param);
		
		$.getJSON("./userAuthUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정이 완료되었습니다");
				$("#page").val("1");
				$("#userAuthInfoForm").attr("target","").attr("action","./userAuthListP.ums").submit();
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("사용자 아이디가  없습니다!");	
	}
} 
 
//  취소 클릭시(리스트로 이동)
function goCancel() { 
	$("#userAuthInfoForm").attr("target","").attr("action","./userAuthListP.ums").submit();
}

function goAdd(){
	var targetcheck = document.getElementsByName('chkM03');	
	 
	var menuId = "";
	var menuNm = "";
	var lv1Nm = "";
	var lv2Nm = "";
	var mappYn = ""; 
	
	var parentUl = document.getElementById('userAuthList');
	if(targetcheck.length > 0 ){
		for (var i = 0; i < targetcheck.length; i++){ 
 			if (targetcheck[i].checked == true ){
				
				var result = targetcheck[i].value.split("|");
				menuId= result[0]; //메뉴id
				menuNm= result[1];
				lv1Nm= result[2];
				lv2Nm= result[3];
				mappYn= result[4];
				
				if(mappYn == "N"){
					//접근 메뉴에 추가	 
					var li=$('<li id=' + 'lA' + menuId +'><label class="clear"><span class="fl"><strong>' + lv1Nm + '</strong><span>' + lv2Nm + '</span> > <span>' + menuNm + '</span></span><span class="fr"><input type="checkbox" name="userAuth" value="' + menuId + '"><span></span></span></label></li>');		 			
					var menuLi = document.getElementById('lM' +menuId );
					if (menuLi != undefined){
						$(menuLi).css('display', 'none');
						targetcheck[i].value =  menuId + '|' + menuNm + '|' + lv1Nm + '|' + lv2Nm + '|Y|';
					}
					$(li).appendTo(parentUl);
 				}
			}
		}
	}
	
	for (var i = 0; i < targetcheck.length; i++){
		 if (targetcheck[i].checked == true ){
			targetcheck[i].checked = false;
		}
	}
	targetcheck = document.getElementsByName('chkM02');
	for (var i = 0; i < targetcheck.length; i++){
		 if (targetcheck[i].checked == true ){
			targetcheck[i].checked = false;
		}
	}
		
	targetcheck = document.getElementsByName('chkM01');
	for (var i = 0; i < targetcheck.length; i++){
		 if (targetcheck[i].checked == true ){
			targetcheck[i].checked = false;
		}
	}	
}

function goRemove(){
	var targetcheck = document.getElementsByName('userAuth');
	var arrRemove= [];
		
	if(targetcheck.length > 0 ){
		for (var i = 0; i < targetcheck.length; i++) {
 			if (targetcheck[i].checked == true ){ 
				arrRemove.push( targetcheck[i].value);  
			}
		} 
	} 
	
	//arrRemove를 가지고 처리함
	for (var i = 0 ; i < arrRemove.length ; i++){	
		var menuLi = document.getElementById('lM' +arrRemove[i] );
		if (menuLi != undefined){
			$(menuLi).css('display', 'block');
			var menuCheck = $(menuLi).find('input');
			if(menuCheck != undefined){
				var curVal = menuCheck.val();
				var result = curVal.split("|"); 
				menuCheck.val( result[0] + '|' + result[1] + '|' + result[2] + '|' + result[3] + '|N|');
			}
		}
		var parentLiIdNode = document.getElementById('lA' + arrRemove[i]);
		parentLiIdNode.parentNode.removeChild(parentLiIdNode);
	}
	
 
}

