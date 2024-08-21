$(document).ready(function() {		
	getJoinList();
}); 


//사용자 그룹 선택
function getJoinList() {
    var dbConnNo = $("#dbConnNo").val(); 
	$.ajax({
		type : "GET",
		url : "./dbConnMetaJoinList.ums?dbConnNo=" + dbConnNo,
		dataType : "html",
		success : function(pageHtml){
			$("#divJoinList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
} 

function changeMasterTable(){
	var idx = $("#masterTable option").index( $("#masterTable option:selected") );
	if(idx == 0) {
		return;
	}
	
	var tblNo = $('#masterTable > option:selected').val();
	var dbConnNo = $('#dbConnNo').val(); 
	
	$.ajax({
		type : "GET",
		url : "./getMetaJoinColumnList.ums?dbConnNo=" + dbConnNo + "&tblNo=" + tblNo,
		dataType: 'json',
		success:function(data){
			if(data.metaColumnList.length > 0){
				$('#masterColumn').find ('option').remove (); 
				$('#masterColumn').append("<option>선택</option>");
				for ( var i = 0 ; i < data.metaColumnList.length ; i++) {
					$('#masterColumn').append("<option value='"+ data.metaColumnList[i].colNo +"'>" + data.metaColumnList[i].colNm + '</option>');
				}
			}else{
				$('#masterColumn').find ('option').remove (); 
				$('#masterColumn').append("<option>선택</option>");
			}
		} 
	});
}

function changeForeignTable(){ 

	var idx = $("#foreignTable option").index( $("#foreignTable option:selected") );
	if(idx == 0) {
		return;
	}
	var tblNo = $('#foreignTable > option:selected').val();
	var dbConnNo = $('#dbConnNo').val();
	$.ajax({
		type : "GET",
		url : "./getMetaJoinColumnList.ums?dbConnNo=" + dbConnNo + "&tblNo=" + tblNo,
		dataType: 'json',
		success:function(data){
			if(data.metaColumnList.length > 0){
				$('#foreignColumn').find ('option').remove (); 
				$('#foreignColumn').append("<option>선택</option>");
				for ( var i = 0 ; i < data.metaColumnList.length ; i++) {
					$('#foreignColumn').append("<option value='"+ data.metaColumnList[i].colNo +"'>" + data.metaColumnList[i].colNm + '</option>');
				}
			}else{
				$('#foreignColumn').find ('option').remove (); 
				$('#foreignColumn').append("<option>선택</option>");
			}
		} 
	});
}
 
function goList(){ 
	$("#page").val("1");
	$("#dbJoinInfoForm").attr("target","").attr("action","./dbConnListP.ums").submit();
}

function goReset(){ 
	$('#masterTable').val("");
	$('#masterColumn').find('option').remove(); 
	$('#masterColumn').append("<option>선택</option>");
	$('#foreignTable').val("");
	$('#foreignColumn').find('option').remove(); 
	$('#foreignColumn').append("<option>선택</option>");
	$('#joinTypeCodeList').val("");
	$('#relTypeCodeList').val(""); 
}

function goAdd(){
	if(checkForm()) {
		return;
	}
	
	var dbConnNo =$('#dbConnNo').val();
	var mstTblNm =$("#masterTable option:selected").text();
	var mstColNm =$("#masterColumn option:selected").text();
	var joinTy =$("#joinTypeCodeList option:selected").val();
	var relTy =$("#relTypeCodeList option:selected").val();
	var forTblNm =$("#foreignTable option:selected").text();
	var forColNm =$("#foreignColumn option:selected").text();
 
	var param = "dbConnNo=" + dbConnNo + "&mstTblNm=" + mstTblNm + "&mstColNm=" + mstColNm ;
	param += "&forTblNm=" + forTblNm + "&forColNm=" + forColNm + "&joinTy=" + joinTy + "&relTy=" + relTy;
	
	console.log(param);
	
	$.getJSON("/sys/dbc/metajoinAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("등록에 성공하였습니다");
			goReset();
			getJoinList();
		} else {
			alert("등록에 실패하였습니다");
		}
	});	  
}

// 수정 클릭시
function goUpdate(obj, joinNo){
 	
	var updatePr =$(obj).parent().parent().parent();
	var joinTy = $(updatePr).find('select:eq(0)').val();
	var relTy = $(updatePr).find('select:eq(1)').val();
  
	var param = "joinNo=" + joinNo + "&joinTy=" + joinTy + "&relTy=" + relTy ;
	console.log(param);
	$.getJSON("/sys/dbc/metajoinUpdate.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("저장에 성공 하였습니다");
		} else {
			alert("저장에 실패하였습니다");
		}
	});	
	 
}
// 수정 클릭시
function goDelete(obj, joinNo ) {
	if(joinNo == "") {
		alert("삭제할 조인번호가 없습니다");
		return;
	}		
	$.getJSON("/sys/dbc/metajoinDelete.json?joinNo=" + joinNo, function(data) {
		if(data.result == "Success") {
			getJoinList();
		} else {
			alert("등록에 실패하였습니다");
		}
	});	
}


// 입력 폼 검사
function checkForm() {

	var errstr = "";
	var errflag = false; 
 
	var idx = $("#masterTable option").index( $("#masterTable option:selected") );
	if(idx == 0) {
		errstr += "[마스터테이블]";
		errflag = true;
	}
	
	idx = $("#masterColumn option").index( $("#masterColumn option:selected") );
	if(idx == 0) {
		errstr += "[마스터컬럼]";
		errflag = true;
	}
	
	idx = $("#joinTypeCodeList option").index( $("#joinTypeCodeList option:selected") );
	if(idx == 0) {
		errstr += "[조인유형]";
		errflag = true;
	}
	
	idx = $("#relTypeCodeList option").index( $("#relTypeCodeList option:selected") );
	if(idx == 0) {
		errstr += "[관계유형]";
		errflag = true;
	} 

	idx = $("#foreignTable option").index( $("#foreignTable option:selected") );
	if(idx == 0) {
		errstr += "[포린테이블]";
		errflag = true;
	}
	
	idx = $("#foreignColumn option").index( $("#foreignColumn option:selected") );
	if(idx == 0) {
		errstr += "[포린컬럼]";
		errflag = true;
	}
	
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	 
	return errflag;
}
 