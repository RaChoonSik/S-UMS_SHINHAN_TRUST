$(document).ready(function() {
	goSearch();
  
}); 

// 검색 버튼 클릭시
function goSearch() {
 	//goReset();
 	getUserCodeList();
 	checkText();
} 

function goSave(){
	
	goReset();
	
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요!");
		return;
	}
	var cdGrp = $("#searchCdGrp").val();
	var arrList = $("tr"); 
	var aaa = 0; 
	for (i = 1 ; i < arrList.length  ; i++){
		var sortNo = i ;
		var cd =$('tr:eq('+ i +')>td:eq(3)>a:eq(0)').html();
		var param = "cdGrp=" + cdGrp + "&cd=" + cd + "&sortNo=" + sortNo;
		$.post("/sys/cod/updateUserCodeSortNo.json?", param , function(data) {
			if(data) {
			 		aaa =  aaa + 1;  
			} else {
				alert("저장에 실패하였습니다");
			}
		}); 
	}
	alert("저장에 성공하였습니다");
 
}
// 코드 또는 명 클릭시
function getUserCodeInfo(cdGrp, cd) {
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요!");
		return;
	}
	
	$("#cd").val(cd);	
	var param = "cdGrp=" + cdGrp + "&cd=" + cd;
	$.getJSON( "./userCodeInfo.json?" + param, function(data) { 
		var userCode = data.userCode;
		$('#cdNm').val(userCode.cdNm);
		
		$('input[name=cd]').attr('value',cd);
		$('input[name=cdGrp]').attr('value',cdGrp);
		
		if(userCode.upCd != null){
			console.log("cdGrp : " + cdGrp);
			console.log("upCd : " + userCode.upCd);
			$('#upCd').val(userCode.upCd).prop("selected", true);
		}else{
			console.log("null 일때 cdGrp : " + cdGrp);
			console.log("null 일때 upCd : " + userCode.upCd);
			searchUpCdSelect(cdGrp)
			$('#upCd').val("").prop("selected", true);
		}
		$('#useYn').val(userCode.useYn).prop("selected", true);
		$('#cdDtl').val(userCode.cdDtl);
		$('#upNm').text(userCode.upNm);
		
		if(userCode.upDt !=""){
			var upDt = toDate(userCode.upDt);
			$('#upDt').text(upDt);
		}
		
		if(userCode.regDt != ""){
			var regDt = toDate(userCode.regDt);
			$('#regDt').text(regDt);
		}
		
		$('#regNm').text(userCode.regNm);
	})
}

// 수정 클릭시
function goUpdate() {
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요!");
		return;
	}
	var a = $("#viewGrp").text();
	var b = $("#searchCdGrp option:selected").text();
	
	//텍스트가 같지 않을 떄 
	if(a == b){
		
		$("#cdGrp").val($("#searchCdGrp").val());
		if($("#cd").val() != "") {
			// 입력 폼 검사
			if(checkForm()) {
				return;
			}
			var param = $("#userCodeInfoForm").serialize();
			console.log (param);
			$.getJSON("./userCodeUpdate.json?" + param, function(data) {
				if(data.result == "Success") {
					alert("수정 성공 하였습니다");
					getUserCodeList();
				} else {
					alert("수정 실패하였습니다");
				}
			});
		} else {
			// 입력 폼 검사
			if(checkForm()) {
				return;
			}
			var param = $("#userCodeInfoForm").serialize();
			$.getJSON("/sys/cod/userCodeAdd.json?" + param, function(data) {
				if(data.result == "Success") {
					alert("등록 성공 하였습니다");
					$("#cd").val(data.cd);
					$('input[name=cd]').attr('value',data.cd);
					//코드명, 상위 코드, 사용여부, 설명
					getUserCodeList();
				} else {
					alert("등록에 실패하였습니다");
				}
			});
		}	
	}else{
		alert("현재분류코드를 확인해 주세요.");
	}
} 

// 신규등록 버튼 클릭시
function goAdd() {
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요!");
		return;
	} else {
		goReset();
	} 
} 

//삭제 EVENT 구현
function goDelete() {
	goReset();
 
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index < 1 ) {
		alert("분류크드를 먼저 선택해주세요!");
		return;
	}	

	const query = 'input[name="delCd"]:checked';
  	const checkboxs = document.querySelectorAll(query);

	var cds="";
	if(checkboxs.length < 1 ){
		alert("삭제할 코드를  선택해주세요");
		return;
	}  else {
		for (var i = 0; i < checkboxs.length; i++) {
        	cds += checkboxs[i].value + ',';	
    	}
	} 
	
	var cdGrp = $("#searchCdGrp").val();	
	$.getJSON("/sys/cod/userCodeDelete.json?cdGrp=" + cdGrp + "&cds=" + cds, function(data) {
		if(data) {
		 		alert("삭제에 성공 하였습니다");
				$("#page").val("1");
				$("#userCodeInfoForm").attr("target","").attr("action","/sys/cod/userCodeListP.ums").submit();
			 
		} else {
			alert("Error!!");
		}
	});   
} 
   
function selectAll(selectAll)  {
	$("input[name='delCd']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
}

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	if($("#cdNm").val() == "") {
		errstr += "[코드명]"; 
		errflag = true;
	} 
 
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	if($.byteString($("#cdNm").val()) > 60 ) {
		alert("코드명은 60byte를 넘을 수 없습니다.");
		$("#cdNm").focus();
		$("#cdNm").select();
		errflag = true;
	}
	if($.byteString($("#cdDtl").val()) > 100 ) {
		alert("코드 설명은 100byte를 넘을 수 없습니다.");
		$("#cdDtl").focus();
		$("#cdDtl").select();
		errflag = true;
	} 
		
	return errflag;
}

// 수정 폼 리셋
function goReset() {
	$("#cd").val("");
	$("#cdNm").val("");
	$("#upNm").val("");
	$("#cdDtl").val("");
	$("#upDt").text("");
	$("#upNm").text("");
	$("#regDt").text("");
	$("#regNm").text("");
	$("#upCd option:eq(0)").prop("selected", true);
	$("#useYn option:eq(0)").prop("selected", true);
}

// 날짜 형식
function toDate(date){
	var date = String(date);
	
	var sYear 	= date.substring(0,4);
	var sMonth 	= date.substring(4,6);
	var sDay 	= date.substring(6,8);
	var sHh 	= date.substring(8,10);
	var sMm	 	= date.substring(10,12);
	var sSs 	= date.substring(12,14);
	
	return new Date(Number(sYear), Number(sMonth)-1, Number(sDay), Number(sHh), Number(sMm), Number(sSs)).
		toISOString().
		replace("T"," ").split(".")[0];
}

// 코드상세정보의 분류 코드 변경시 연결된 상위코드 목록 조회
function searchUpCdSelect(upCd) { 	

	$.getJSON("/sys/cod/userCodeListByCodeGroup.json?cdGrp=" + upCd, function(data) {
		$("#upCd").children("option:not(:first)").remove();
		$.each(data.upCdGrpList, function(idx,item){
			var option = new Option(item.cdNm,item.cd);
			$("#upCd").append(option);
		});
	});
}  
//목록 불러오기
function getUserCodeList(){
	
	var codeGrpSysYn = checkSysYn();
	
	var param = $("#userCodeInfoForm").serialize();
	
	$.ajax({
		type : "GET",
		url : "./userCodeList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divUserCodeList").html(pageHtml);
			if (codeGrpSysYn ==  "Y"){
				document.getElementById("btnAddCode").disabled = true;
				document.getElementById("btnUpdateCode").disabled = true;
			} else {
				document.getElementById("btnAddCode").disabled = false; 
				document.getElementById("btnUpdateCode").disabled = false; 
			}
		},
		error : function(){
			alert("List Data Error!!");
		}
	}); 
}

function checkSysYn(){
	
	var codeGrpSysYn ="N";
	var index = $("#searchCdGrp option").index($("#searchCdGrp option:selected"));
	if (index > 0 ) {
		var txtSearchCdGrp= $("#searchCdGrp option:selected").text();
		
		if (txtSearchCdGrp.indexOf("수정불가") > - 1){
			codeGrpSysYn ="Y"; 
		}  else {
			codeGrpSysYn ="N"; 
		} 
	}	
	return codeGrpSysYn;
}

function checkText(){
	var viewGrp = $("#searchCdGrp option:selected").text();
		
 	if(viewGrp == "필수"){
		$("#viewGrp").text("");	
	}else{
	 	$("#viewGrp").text(viewGrp);
	}
	return viewGrp;
}