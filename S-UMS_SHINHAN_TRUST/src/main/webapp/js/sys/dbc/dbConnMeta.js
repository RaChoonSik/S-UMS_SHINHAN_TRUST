//사용자 그룹 선택
function getMetaTableInfo(tblNm) { 	
	$("#tblNm").val(tblNm); 
	var param = $("#dbConnInfoForm").serialize(); 
	console.log(param);
	$.getJSON( "./metaTableInfo.json?" + param, function(data) { 
		if (data.result == "1" ){
			tblNo = data.metaTable.tblNo; 
			$('#viewTblNm').text(data.metaTable.tblNm);
			$('#tblAlias').val(data.metaTable.tblAlias);
			$('#tblDesc').val(data.metaTable.tblDesc);
		} else {
			tblNo = 0 ;
		}
		setView(tblNo, tblNm);
	});
}
 
//사용자 검색 선택
function getMetaColumInfo() {
	 
	var param = $("#dbConnInfoForm").serialize(); 
	console.log("getMetaColumInfo param :" + param);
	$.ajax({
		type : "GET",
		url : "./metacolumnList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divMetaColumnist").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});	
}

function goReset(){
	$('#tblAlias').val("");
	$('#tblDesc').val("");
} 

// 수정 클릭시
function goUpdate() {		
	if($("#dbConnNo").val() != "") {
		var url ="";		
		
		if ($("#tblNo").val() != "0"){
			url = "./metatableUpdate.json?";
		} else {
			url = "./metatableAdd.json?";
		}

		var param = $("#dbConnInfoForm").serialize();
		console.log ( "param : " + param);
		$.getJSON(url + param, function(data) {
			if(data.result == "Success") {
				alert("수정에 성공하였습니다");
				setView(data.tblNo, data.tblNm);
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("DB Connection 번호가 없습니다!");	
	}
}

// 삭제 클릭시
function goDelete() {	 
	if($("#dbConnNo").val() != "") {
 		 
		var param = $("#dbConnInfoForm").serialize();
		console.log ( "param : " + param);
		$.getJSON("./metatableDelete.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("삭제 되었습니다");
				setView(data.tblNo, data.tblNm); 
			} else {
				alert("삭제에 실패하셨습니다");
			}
		});
	} else {
		alert("DB Connection 번호가 없습니다!");	
	}
}  

function setView(tblNo, tblNm) {
	console.log("setView : tblNo =" + tblNo + " / tblNm =" + tblNm );
	$('#tblNo').val(tblNo);
	$('#tblNm').val(tblNm);
	$('#viewTblNm').text(tblNm);
	
	if($('#tblAlias').val() == ""){
		$('#tblAlias').val(tblNm);
	}
	if (tblNo == "0") {
		$('#tblAlias').val("");
		$('#tblDesc').val("");
		$("#metaInfoDel").hide();
		$("#metaInfoUpd").html("등록"); 
		$("#divMetaColumnist").attr('style', "visibility:hidden");		
	} else { 
		$("#metaInfoDel").show();
		$("#metaInfoUpd").html("수정"); 
		$("#divMetaColumnist").attr('style', "visibility:visible");
		getMetaColumInfo();
	} 
}

// 수정 클릭시
function goUpdateColumn(obj) {
	   
	var colInfoList =$(obj).parent().siblings(); 
 
	$("#colNo").val(colInfoList.get(0).innerText);
	//console.log (colInfoList.get(1).innerText);
	$("#colNm").val(colInfoList.get(1).innerText);
	var alias = $(colInfoList.get(2)).children().get(0).value;
	if (alias.length == 0 ){
		$(colInfoList.get(2)).children().get(0).value = colInfoList.get(1).innerText;
	}
	$("#colAlias").val($(colInfoList.get(2)).children().get(0).value);
	$("#colDataTy").val(colInfoList.get(3).innerText); 
	$("#colDesc").val($(colInfoList.get(4)).children().get(0).value);
	  
	var checkHiddenYn = $($(colInfoList.get(5)).children().get(0)).children().get(0);
	var checkEncrDecrYn = $($(colInfoList.get(5)).children().get(1)).children().get(0);
	 
	if ($(checkHiddenYn).is(":checked"))
	{
		$("#colHiddenYn").val("Y");
	} else {
		$("#colHiddenYn").val("N");
	}
	  
	if ($(checkEncrDecrYn).is(":checked"))
	{
		$("#colEncrDecrYn").val("Y");
	} else {
		$("#colEncrDecrYn").val("N");
	}
	 	
 	if($("#dbConnNo").val() != "") { 
		 
		var param = $("#dbConnInfoForm").serialize();
		console.log ( "param : " + param);
		$.getJSON("./metacolumnUpdate.json?" + param, function(data) {
			if(data.result == "Success") {				
				//$("#colNo").val(data.colNo);
				colInfoList.get(0).innerText = data.colNo;
				setViewColumn(obj, data.colNo);
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	} else {
		alert("DB Connection 번호가 없습니다!");	
	} 
}

function goExtractColumn(obj) {  
	 var colInfoList =$(obj).parent().siblings();
	 $("#colNo").val(colInfoList.get(0).innerText);

	var colNo = $("#colNo").val();
	var tblNo = $("#tblNo").val(); 
	popMetaExtract(tblNo,colNo );
	 
} 

function goDeleteColumn(obj) {
	
	var colInfoList =$(obj).parent().siblings();
	 $("#colNo").val(colInfoList.get(0).innerText);
  
     console.log($("#colNo").val);

	if($("#dbConnNo").val() != "" && $("#tblNo").val() != "" ) { 
	 
		var param = $("#dbConnInfoForm").serialize();
		console.log ( "param : " + param);
		$.getJSON("./metacolumnDelete.json?" + param, function(data) {
			if(data.result == "Success") {				
				$("#colNo").val(data.colNo);
				setViewColumn(obj, data.colNo); 
			} else {
				alert("삭제에 실패하셨습니다");
			}
		});
	} else {
		alert("DB Connection 번호가 없습니다!");	
	} 
}

function setViewColumn(obj, colNo) {
 
	var colInfoList =$(obj).parent().siblings();
	
	if (colNo == "0") {	
		 
		colInfoList.get(0).innerText ="0";			 
		$(colInfoList.get(2)).children().get(0).value ="";
		$(colInfoList.get(4)).children().get(0).value =""; 
		  
		var checkHiddenYn = $($(colInfoList.get(5)).children().get(0)).children().get(0);
		var checkEncrDecrYn = $($(colInfoList.get(5)).children().get(1)).children().get(0);
		 
		$(checkHiddenYn).prop("checked", false);
		$(checkEncrDecrYn).prop("checked", false);
				  
		
		colInfoList  =$(obj).siblings();
		
		$(obj).hide();
		$(colInfoList.get(0)).hide();
		$(colInfoList.get(1)).hide();
		$(colInfoList.get(2)).show();
		
	} else {
		colInfoList.get(0).innerText =colNo;		  
		colInfoList =$(obj).siblings();
		var upBtnText="수정";
		var delBtnText="삭제";
		var addBtnText="등록";
		
		if ($(obj).text() == upBtnText) {
			$(colInfoList.get(0)).show();
			$(colInfoList.get(1)).show();
			$(colInfoList.get(2)).hide();
		} else if ($(obj).text() == delBtnText) {
			$(obj).hide();
			$(colInfoList.get(0)).hide();
			$(colInfoList.get(1)).hide();
			$(colInfoList.get(2)).show();
		} else if ($(obj).text() == addBtnText) {
			$(obj).hide();
			$(colInfoList.get(0)).show();
			$(colInfoList.get(1)).show();
			$(colInfoList.get(2)).show();
		}
	 
	} 
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#dbConnInfoForm").attr("target","").attr("action","./dbConnListP.ums").submit();
}
 
/********************************************************
 * 메타 데이터 추출처리
 ********************************************************/

// 메타 데이터 추출 화면 팝업   
function popMetaExtract(tblNo,colNo) {	

	$.getJSON("/sys/dbc/metaOperList.json?tblNo=" + tblNo + "&colNo=" + colNo, function(data) {
		 
		console.log ( data.metaColumn.tblNm + "[" + data.metaColumn.colNm +"]");
		$("#metaTableTitle").text(data.metaColumn.tblNm + "[" + data.metaColumn.colNm +"]")
		$("#metaColumnTitle").text("[" + data.metaColumn.colNm +"] VALUE" )
		$("#tblNo").val(data.metaColumn.tblNo);
		$("#colNo").val(data.metaColumn.colNo);
		$("#popTblNo").val(data.metaColumn.tblNo);
		$("#popColNo").val(data.metaColumn.colNo);
		
		var opperCodeHtml  = "";
		$.each(data.metaOperatorList, function(idx,item){
			console.log("idx" + idx );
			if (idx % 3 == 0) {
				opperCodeHtml += '<li class="col-full"><div class="list-item">';
				opperCodeHtml += '<label><input type="checkbox" name="chkOper"';
				if (item.operNo != 0 ) {
					opperCodeHtml += 'checked ';
				}
				opperCodeHtml += 'value=' + item.operCd + ' ><span>' + item.cdDtl + '</span></label>';
			} else if ( idx % 3 == 1) {
				opperCodeHtml += '<label><input type="checkbox" name="chkOper"';
				if (item.operNo != 0 ) {
					opperCodeHtml += 'checked ';
				} 
				opperCodeHtml += 'value=' + item.operCd + '><span>' + item.cdDtl + '</span></label>';
			} else {
				opperCodeHtml += '<label><input type="checkbox" name="chkOper" ';
				
				if (item.operNo != 0 ) {
					opperCodeHtml += 'checked ';
				} 
				opperCodeHtml += 'value=' + item.operCd + '><span>' + item.cdDtl + '</span></label>';
				opperCodeHtml += '</div></li>';
			} 
		});
		$("#extractOperList").html(opperCodeHtml);
	}); 
	
	$.ajax({
		type : "GET",
		url : "./metaValueList.ums?colNo=" + colNo,
		dataType : "html",
		success : function(pageHtml){
			$("#divExtractValueInfo").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
		
	fn.popupOpen("#popup_meta_extract");
}

//operation 저장
function goUpdateOper(){
	
	if($("#colNo").val() != "") { 
		const query = 'input[name="chkOper"]:checked'; 
		const checkboxs = document.querySelectorAll(query);
		
		var operCd ="";
		var colNo = "";
		
		for (var i = 0; i < checkboxs.length; i++) {
			 operCd += checkboxs[i].value + ',' ;  
		}

		colNo = $("#colNo").val(); 
	 	$.getJSON("./metaoperUpdate.json?colNo=" + colNo + "&operCd=" + operCd , function(data) {
			if(data.result == "Success") {
				alert("수정이 완료되었습니다"); 
			} else {
				alert("수정에 실패하셨습니다");
			}
		});
	}
}

//operation 재입력 
function goResetOper(){
	$("input[name='chkOper']").each(function(idx,item){
		if( $(item).is(":disabled") == false) {
			$(item).prop("checked",false);
		}
	});
}
 
//value 신규등록  
function goCancelValue(){
	$("#addValueNm").val("");
	$("#addValueAlias").val("");
}
//value 신규등록  
function goAddValue(){ 
	
	//var param = "colNo=" + $("#colNo").val() +  "&valueNm=" + $("#valueNm").val() +  "&valueAlias=" + $("#valueAlias").val() ;
	//console.log(param);
		
	$("#valueNm").val($("#addValueNm").val());
	$("#valueAlias").val($("#addValueAlias").val());
	console.log(param);
	
	var param = $("#dbConnColValueInfoForm").serialize();
	
	$.getJSON("./metavalAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("등록 되었습니다");
			$("#valueNm").val(""); 
			$("#valueAlias").val(""); 
			$('.valueenroll').hide();
			$("#divValueInfo").show();
			
			$("#addValueNm").val("");
			$("#addValueAlias").val("");
			$.ajax({
				type : "GET",
				url : "./metaValueList.ums?colNo=" + $("#colNo").val(),
				dataType : "html",
				success : function(pageHtml){
					$("#divExtractValueInfo").html(pageHtml);
				},
				error : function(){
					alert("Error!!");
				}
			});			
			
		} else {
			alert("등록에 실패하셨습니다");
		}
	});
}

//value 수정
function goUpdateValue(obj, valueNo){
	var valInfoList =$(obj).parent().parent().siblings();
	var valueNm = $(valInfoList.get(1)).children().get(0).value;
	var valueAlias = $(valInfoList.get(2)).children().get(0).value;

	$("#valueNo").val(valueNo);	
	$("#valueNm").val(valueNm);
	$("#valueAlias").val(valueAlias);
	
	var param = $("#dbConnColValueInfoForm").serialize();
	
	//var param = "valueNo=" + valueNo + "&valueNm=" + valueNm + "&valueAlias=" + valueAlias;
	//var param = $("#dbConnInfoForm").serialize();
	console.log(param);
	$.getJSON("./metavalUpdate.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수정 되었습니다"); 
			  
		} else {
			alert("수정에 실패하셨습니다");
		}
	}); 
}
 
//value 수정
function goDeleteValue(obj, valueNo){
	 
	var removeRow =$(obj).parent().parent().parent();
	console.log ("target :" + removeRow) ;
	 
	$.getJSON("./metavalDelete.json?valueNo=" + valueNo, function(data) {
		if(data.result == "Success") {
			alert("삭제 되었습니다"); 			
			removeRow.remove();
			  
		} else {
			alert("수정에 실패하셨습니다");
		}
	}); 
	 
}