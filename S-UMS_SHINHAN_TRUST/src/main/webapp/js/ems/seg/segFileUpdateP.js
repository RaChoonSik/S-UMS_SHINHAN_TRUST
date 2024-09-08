/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.08.20
*	설명 : 수신자그룹 수정(파일연동) JavaScript
**********************************************************/


// 파일 업로드
function addressUpload() {
	if($("#upTempFlPath").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}
	
	var extCheck = "csv,txt";
	var fileName = $("#upTempFlPath").val();
	var fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
	
	if(extCheck.toLowerCase().indexOf(fileExt.toLowerCase()) >= 0) {
		var frm = $("#fileUploadSeg")[0];
		var frmData = new FormData(frm);
		$.ajax({
			type: "POST",
			enctype: 'multipart/form-data',
			url: '../../com/uploadSegFile.json',
			data: frmData,
			processData: false,
			contentType: false,
			success: function (result) {
				if (result.result == "Success"){
					alert("파일 업로드가 완료되었습니다.");
					$("#upTempFlPath").val(result.oldFileName);
					$("#upSegFlPath").val(result.newFileName);
					$("#tempFlPath").val(result.oldFileName);
					$("#segFlPath").val(result.newFileName);
				} else {
					alert("파일 업로드가 실패 했습니다 다시 시도해주세요");
				}
				fn.fileReset('#popup_file_seg');
				fn.popupClose('#popup_file_seg');
			},
			error: function (e) {
				alert("파일 업로드에 실패했습니다  다시 시도해주세요.");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다. csv, txt 만 가능합니다.");
	}
}

// 다운로드 버튼 클릭
function goDownload() {
	$("#segInfoForm").attr("target","iFrmDown").attr("action","../../com/down.ums").submit();
}

// 구분자 등록(확인)
function fncSep(userId) {
	if($("#segFlPath").val() == "") {
		$("#separatorChar").val("");
		alert("파일이 입력되어 있지 않습니다.\n파일을 입력하신 후 구분자를 입력해 주세요.");
		fn.popupOpen('#popup_file_seg');
		return;
	}

	if($("#separatorChar").val() == "") {
		alert("구분자를 입력하세요.");
		$("#separatorChar").focus();
		return;
	}

	var tmp = $("#segFlPath").val().substring($("#segFlPath").val().lastIndexOf("/")+1);
	$("#segFlPath").val("addressfile/" + userId + "/" + tmp);
	var param = $("#segInfoForm").serialize();
	$.ajax({
		type : "POST",
		url : "./segFileMemberListP.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#iFrmDown").contents().find("body").html(pageHtml);
		},
		error : function(){
			alert("구분자 등록에 실패했습니다");
		}
	});
}

//사용자그룹 선택시 사용자 목록 설정
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

// 미리보기 클릭시
function goSegInfo(userId) {
	if($("#segFlPath").val() == "") {
		$("#separatorChar").val("");
		alert("파일이 입력되어 있지 않습니다.\n파일을 입력하신 후 구분자를 입력해 주세요.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if($("#separatorChar").val() == "") {
		alert("구분자를 입력하세요.");
		$("#separatorChar").focus();
		return;
	}
	
	var tmp = $("#segFlPath").val().substring($("#segFlPath").val().lastIndexOf("/")+1);
    $("#segFlPath").val("addressfile/" + userId + "/" + tmp);

	$("#previewSegNm").html( $("#segNm").val() );
	$("#previewSql").html( $("#segFlPath").val() );

	var param = $("#segInfoForm").serialize();
	$.ajax({
		type : "POST",
		url : "./segFileMemberListP.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#iFrmDown").contents().find("body").html(pageHtml);
			fn.popupOpen('#popup_preview_seg');
		},
		error : function(){
			alert("수신자그룹 미리보기에 실패하였습니다");
		}
	});
}

// 수정버튼 클릭시(발송대상(세그먼트) 정보 수정)
function goSegFileUpdate(status) {
	if(status == "002") {
		alert("삭제된 발송대상그룹입니다\n삭제된 발송대상그룹은 수정을 할 수 없습니다");
		return;
	}
	if($("#segFlPath").val() == "") {
		alert("파일등록은 필수입력 항목입니다.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if($("#separatorChar").val() == "") {
		alert("구분자는 필수입력 항목입니다.");
		$("#separatorChar").focus();
		return;
	}
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "0"){
			alert("사용자그룹은 필수입력 항목입니다.");
			$("#deptNo").focus();
			return;	
		}		
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			alert("사용자는 필수입력 항목입니다.");
			$("#userId").focus();
			return;
		}
	}
	if($("#segNm").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#segNm").focus();
		return;
	}

	if($("#totCnt").val() == 0) {
		var a = confirm("쿼리테스트를 하지 않았습니다.\n계속 실행을 하겠습니까?");
		if ( a ) {
			var param = $("#segInfoForm").serialize();
			$.getJSON("./segUpdate.json?" + param, function(data) {
				if(data.result == "Success") {
					alert("수정되었습니다.");
					
					$("#searchForm").attr("action","./segMainP.ums").submit();
				} else if(data.result == "Fail") {
					alert("수정 처리중 오류가 발생하였습니다.");
				}
			});
		} else return;
	} else {
		var param = $("#segInfoForm").serialize();
		$.getJSON("./segUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정되었습니다.");
				
				$("#searchForm").attr("action","./segMainP.ums").submit();
			} else if(data.result == "Fail") {
				alert("수정 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 사용중지 클릭시
function goDisable() {
	$("#status").val("001");
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수신자그룹이 사용중지 되었습니다.");
			$("#btnDisable").hide();
			$("#btnEnable").removeClass("hide");
			$("#btnEnable").show();
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 복구 클릭시
function goEnable() {
	$("#status").val("000");
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수신자그룹이 복구 되었습니다.");
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();
			$("#btnEnable").hide();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 클릭시
function goDelete() {
	$("#status").val("002");
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수신자그룹이 삭제 되었습니다.");
			
			$("#searchForm").attr("target","").attr("action","./segMainP.ums").submit();
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	});
}

//목록으로 이동
function goSegList() {
	$("#searchForm").attr("action","./segMainP.ums").submit();
}

// 미리보기 페이징(ums.common.js 변수&함수 포함)
function goPageNumSeg(pageNum) {
	$("#page").val(pageNum);
	var param = $("#segInfoForm").serialize();
	$.ajax({
		type : "POST",
		url : "./segFileMemberListP.ums?" + param + "&checkSearchReason=" + checkSearchReason + "&searchReasonCd=" + $("#searchReasonCd").val() + "&contentPath=" + window.location.pathname,
		dataType : "html",
		success : function(pageHtml){
			checkSearchReason = true;
			$("#previewMemberList").html(pageHtml);
		},
		error : function(){
			alert("목록 조회에 오류가 발생하였습니다");
		}
	});
}
