$(document).ready(function() {		
	goSearch();
}); 
 
$(document).on('click','input[type="checkbox"]', function(){
	select(this);
});

// 검색 버튼 클릭시
function goSearch() {
	var param = $("#searchForm").serialize();
	console.log(param);
	
	$("#orgList").empty();
	var li="";
	var ul="";
	var upOrgCd = "";
	$.getJSON("/sys/acc/orgList.json?"+ param, function(data) { 
		var total = data.orgList.length;
		$('#orgTotal').text(total );
		$.each(data.orgList, function(idx,item){  
			if (item.lvlVal == "1" ){ 
				li =" <li id= li" + item.orgCd +" class='depth1'><ul><li class='col-box'><span class='col checkbox'>";
				li +="<label><input type='checkbox' name='delOrgCd' value=" + item.orgCd + "/" + item.childCnt + "><span></span></label></span><span class='col departmentcode'>";
				li +="<button type='button' id=chk"+ item.orgCd + " class='btn-toggle'> " + item.orgCd + "</button></span><span class='col departmentname'>";
				li +="<a href='#' onclick='goUpdate(this)' class='bold' name=" +item.orgCd +">" + item.orgNm +"</a></span>";
				li +="<span class='col registrant'>" + item.regNm +"</span>";
				li +="<span class='col enrolldate'>" + item.regDt +"</span></li></ul></li>";
				var lis = $(li); 
				 $(lis).appendTo($("#orgList")); 
			} else if (item.lvlVal == "2" ){
				var targetUl = document.getElementById("ul" + item.upOrgCd) ; 
				if (targetUl == null) { 
					targetUl = $("<ul id=ul" + item.upOrgCd +" class=" + "depth" + item.lvlVal + "></ul>"); 
					var upli = document.getElementById('li' +  item.upOrgCd); 
					$(targetUl).appendTo(upli);
				}  
				li =" <li id= li" + item.orgCd +"><ul><li class='col-box'><span class='col checkbox'>";
				li +="<label><input type='checkbox' name='delOrgCd' value=" + item.orgCd + "/" + item.childCnt +"><span></span></label></span>";
				if (item.childCnt > 0 ) {
					li +="<span class='col departmentcode'><button type='button' class='btn-toggle'> " + item.orgCd + "</button></span>";
				} else {
					li +="<span class='col departmentcode'><span class='last'>" + item.orgCd + "</span></span>";
				}
				li +="<span class='col departmentname'><a href='#' onclick='goUpdate(this)' class='bold' name=" +item.orgCd +">" + item.orgNm +"</a></span>";
				li +="<span class='col registrant'>" + item.regNm +"</span>";
				li +="<span class='col enrolldate'>" + item.regDt +"</span></li></ul></li>";
				var lis = $(li);
				$(lis).appendTo(targetUl); 
			} else if (item.lvlVal == "3" ){
				var targetUl = document.getElementById("ul" + item.upOrgCd) ; 
				if (targetUl == null) { 
					targetUl = $("<ul id=ul" + item.upOrgCd +" class=" + "depth" + item.lvlVal + "></ul>"); 
					var upli = document.getElementById('li' +  item.upOrgCd); 
					$(targetUl).appendTo(upli);
				}  
				li =" <li id= li" + item.orgCd +"><ul><li class='col-box'><span class='col checkbox'>";
				li +="<label><input type='checkbox' name='delOrgCd' value=" + item.orgCd + "/" + item.childCnt +"><span></span></label></span>";
				if (item.childCnt > 0 ) {
					li +="<span class='col departmentcode'><button type='button' class='btn-toggle'> " + item.orgCd + "</button></span>";
				} else {
					li +="<span class='col departmentcode'><span class='last'>" + item.orgCd + "</span></span>";
				}
				li +="<span class='col departmentname'><a href='#' onclick='goUpdate(this)' class='bold' name=" +item.orgCd +">" + item.orgNm +"</a></span>";
				li +="<span class='col registrant'>" + item.regNm +"</span>";
				li +="<span class='col enrolldate'>" + item.regDt +"</span></li></ul></li>";
				var lis = $(li);
				$(lis).appendTo(targetUl); 
			} else {
				var targetUl = document.getElementById("ul" + item.upOrgCd) ; 
				if (targetUl == null) { 
					targetUl = $("<ul id=ul" + item.upOrgCd +" class=" + "depth" + item.lvlVal + "></ul>"); 
					var upli = document.getElementById('li' +  item.upOrgCd); 
					$(targetUl).appendTo(upli);
				}  
				li =" <li id= li" + item.orgCd +"><ul><li class='col-box'><span class='col checkbox'>";
				li +="<label><input type='checkbox' name='delOrgCd' value=" + item.orgCd + "/" + item.childCnt +"><span></span></label></span>";
				if (item.childCnt > 0 ) {
					li +="<span class='col departmentcode'><button type='button' class='btn-toggle'> " + item.orgCd + "</button></span>";
				} else {
					li +="<span class='col departmentcode'><span class='last'>" + item.orgCd + "</span></span>";
				}
				li +="<span class='col departmentname'><a href='#' onclick='goUpdate(this)' class='bold' name=" +item.orgCd +">" + item.orgNm +"</a></span>";
				li +="<span class='col registrant'>" + item.regNm +"</span>";
				li +="<span class='col enrolldate'>" + item.regDt +"</span></li></ul></li>";
				var lis = $(li);
				$(lis).appendTo(targetUl); 				
			}				
		});
	}); 
}

// 코드 또는 명 클릭시
function goUpdate( obj){
	$("#orgCd").val(obj.name); 	
	$("#searchForm").attr("target","").attr("action","./orgUpdateP.ums").submit();
}

// 초기화 버튼 클릭시
function goReset() {
	$("#searchForm")[0].reset();
}

// 신규등록 버튼 클릭시
function goAdd() {
	document.location.href = "./orgAddP.ums";
}
 
//삭제 EVENT 구현
function goDelete() { 
	 
	const query = 'input[name="delOrgCd"]:checked';
  	const checkboxs = document.querySelectorAll(query);
  	
	var orgCds="";
	if(checkboxs.length < 1 ){
		alert("삭제할 부서 정보를 선택해주세요");
		return;
	} else {
		
		for (var i = 0; i < checkboxs.length; i++) {
			var pos=checkboxs[i].value.indexOf("/");
			orgCds +=  checkboxs[i].value.substring(0, pos) + ',';	
			console.log(orgCds);
    	}
	} 
	
	console.log(orgCds);
	$.getJSON("/sys/acc/orgDelete.json?orgCds=" + orgCds, function(data) {
		if(data.result == "Success") {
		 		alert("삭제에 성공 하였습니다");
				$("#page").val("1");
				$("#searchForm").attr("target", "").attr("action", "/sys/acc/orgListP.ums").submit();
			 
		} else {
			alert(data.errMsg);
		}
	});
} 
 
// 검색 코드 분류 변경시 
function change(){
	goSearch();
}

function selectAll(selectAll)  {
	$("input[name='delOrgCd']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
}

function select(obj) { 
	if(obj.getAttribute("value") == null){
		return;
	} 
	var objVal = obj.getAttribute("value");
	var pos = objVal.indexOf("/");
	var id = objVal.substring(0, pos);
	var childcnt = objVal.substr(pos + 1, objVal.length-pos);
	  
	if (childcnt > 0 ) {
		var lis = document.querySelectorAll("#li" + id + " input");  
		for (var i = 0; i < lis.length; i++) {
			$(lis.item(i)).prop("checked",obj.checked);  
		}
	} 
} 
