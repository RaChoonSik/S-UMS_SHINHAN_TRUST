$(document).ready(function() {
	goSearch();
  
}); 

// 검색 버튼 클릭시
function goSearch() {
 	getSenderKeyList() 
} 

// 코드 또는 명 클릭시
function getSenderKeyInfo(senderKeyId) {
	
	var param = "senderKeyId=" + senderKeyId;
	$.getJSON( "./senderKeyInfo.json?" + param, function(data) { 
		var senderKey = data.senderKey;
		$('#senderKeyId').val(senderKey.senderKeyId);
		
		$('#useYn').val(senderKey.useYn).prop("selected", true);
		$('#senderKey').val(senderKey.senderKey);
		$('#upNm').text(senderKey.upNm);
		
		if(senderKey.upDt !=""){
			var upDt = toDate(senderKey.upDt);
			$('#upDt').text(upDt);
		}
		
		if(senderKey.regDt != ""){
			var regDt = toDate(senderKey.regDt);
			$('#regDt').text(regDt);
		}
		
		$('#regNm').text(senderKey.regNm);
	})
}

// 수정 클릭시
function goUpdate() {

	if($("#senderKeyId").val() != "") {
		// 입력 폼 검사
		if(checkForm()) {
			return;
		}
		
		$('#senderKeyId').prop('disabled',false);
		
		var param = $("#senderKeyInfoForm").serialize();
		$('#senderKeyId').prop('disabled',true);
		console.log (param);
		$.getJSON("./senderKeyUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정 성공 하였습니다");
				getSenderKeyList();
			} else {
				alert("수정 실패하였습니다");
			}
		});
	} else {
		// 입력 폼 검사
		if(checkForm()) {
			return;
		}
		var param = $("#senderKeyInfoForm").serialize();
		$.getJSON("/sys/cod/senderKeyAdd.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("등록 성공 하였습니다");
				$("#senderKeyId").val(data.senderKeyId);
				getSenderKeyList();
			} else {
				alert("등록에 실패하였습니다");
			}
		});
	}	
	 
} 

// 신규등록 버튼 클릭시
function goAdd() {
	goReset();
} 

//삭제 EVENT 구현
function goDelete() {
	goReset();

	const query = 'input[name="delSenderKey"]:checked';
  	const checkboxs = document.querySelectorAll(query);

	var senderKeyIds="";
	if(checkboxs.length < 1 ){
		alert("삭제할 발송키를  선택해주세요");
		return;
	}  else {
		for (var i = 0; i < checkboxs.length; i++) {
        	senderKeyIds += checkboxs[i].value + ',';	
    	}
	} 
	

	$.getJSON("/sys/cod/senderKeyDelete.json?senderKeyIds=" + senderKeyIds , function(data) {
		if(data) {
		 		alert("삭제에 성공 하였습니다");
				$("#page").val("1");
				$("#senderKeyInfoForm").attr("target","").attr("action","/sys/cod/senderKeyListP.ums").submit();
			 
		} else {
			alert("Error!!");
		}
	});   
} 
   
function selectAll(selectAll)  {
	$("input[name='delSenderKey']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",selectAll.checked);
		}
	});
}

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	if($("#senderKey").val() == "") {
		errstr += "[발송키]"; 
		errflag = true;
	} 
 
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	if($.byteString($("#senderKey").val()) > 60 ) {
		alert("발송키는 100byte를 넘을 수 없습니다.");
		$("#senderKey").focus();
		$("#senderKey").select();
		errflag = true;
	}
 
	return errflag;
}

// 수정 폼 리셋
function goReset() {
	$("#senderKeyId").val("");
	$("#senderKey").val("");
	$("#upDt").text("");
	$("#upNm").text("");
	$("#regDt").text("");
	$("#regNm").text("");
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
 
//목록 불러오기
function getSenderKeyList(){
	
	var param = $("#senderKeyInfoForm").serialize();
	
	$.ajax({
		type : "GET",
		url : "./senderKeyList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divSenderKeyList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	}); 
}
  
  
 
function goSave(){
	
	goReset();
	
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