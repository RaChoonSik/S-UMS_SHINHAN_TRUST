/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.18
*	설명 : 수신자그룹 신규등록(파일연동) JavaScript
**********************************************************/

// 탭 클릭시 페이지 이동
function goCreateTy(no) {
    var actionUrl;
    
    if(no == '000') actionUrl = "./segToolAddP.ums";      	// 추출도구이용
    if(no == '002') actionUrl = "./segDirectSQLAddP.ums";	// SQL 직접 입력
    if(no == '003') actionUrl = "./segFileAddP.ums";   		// 파일그룹
    if(no == '004') actionUrl = "./segRemarketAddP.ums";    // 연계서비스(리타게팅) 지정
    
    document.location.href = actionUrl;
}

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
				alert("파일 업로드가 완료되었습니다.");
				$("#upTempFlPath").val(result.oldFileName);
				$("#upSegFlPath").val(result.newFileName);
				$("#tempFlPath").val(result.oldFileName);
				$("#segFlPath").val(result.newFileName);
				$("#headerInfo").val(result.headerInfo);
				$("#separatorChar").val("");
				
				fn.fileReset('#popup_file_seg');
				fn.popupClose('#popup_file_seg');
			},
			error: function (e) {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다. csv, txt 만 가능합니다.");
	}
}

// 샘플 클릭시()샘플파일 다운로드)
function downloadSample() {
	iFrmDown.location.href = "../../com/down.ums?downType=011";
}

// 구분자 등록(확인)
function fncSep(userId) {
	if($("#segFlPath").val() == "") {
		$("#separatorChar").val("");
		alert("파일이 입력되어 있지 않습니다.\n파일을 입력하신 후 구분자를 입력해 주세요.");
		fn.popupOpen('#popup_file');
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
			alert("Error!!");
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
		fn.popupOpen('#popup_file');
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
			alert("Error!!");
		}
	});
}

// 등록 버튼 클릭시 : 수신자그룹정보(파일연동) 등록
function goSegFileAdd() {
	if($("#segFlPath").val() == "") {
		alert("파일등록은 필수입력 항목입니다.");
		fn.popupOpen('#popup_file');
		return;
	}
	if($("#separatorChar").val() == "") {
		alert("구분자는 필수입력 항목입니다.");
		$("#separatorChar").focus();
		return;
	}
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "") {
			alert("사용자그룹을 선택하세요.");
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
	if(!($("#emsuseYn").is(":checked") || $("#smsuseYn").is(":checked") || $("#pushuseYn").is(":checked"))) {
		alert("서비스구분을 하나 이상 선택하세요.");
		return;
	}
	
	//수신자 그룹 체크 항목
	var targetService = getTargetService();
	var headerInfo = $("#headerInfo").val();
	var dataInfo = "";
	var headerInfoArr = new Array();
	if(headerInfo !="N"){
		if(headerInfo != null && headerInfo != ""){
			headerInfoArr = headerInfo.split($("#separatorChar").val());
			var errstr = checkSegmentFileInfo(headerInfoArr, dataInfo, targetService);
		
			if(errstr != null && errstr != "" ){
				alert("다음 정보를 확인하세요.\n" + errstr);
				return;
			}
		}
	}else{
		alert("등록한 파일을 확인해 주세요.");
		return;
	}
	
	// 입력값 Byte 체크
	if($.byteString($("#segNm").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#segNm").focus();
		$("#segNm").select();
		return true;
	}
	if($.byteString($("#segDesc").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#segDesc").focus();
		$("#segDesc").select();
		return true;
	}
	
	// 등록 처리
	if($("#totCnt").val() == 0) {
		var a = confirm("쿼리테스트를 하지 않았습니다.\n계속 실행을 하겠습니까?");
		if ( a ) {
			var param = $("#segInfoForm").serialize();
			$.getJSON("./segAdd.json?" + param, function(data) {
				if(data.result == "Success") {
					alert("등록되었습니다.");
					document.location.href = "./segMainP.ums";
					//$("#searchForm").attr("action","./segMainP.ums").submit();
				} else if(data.result == "Fail") {
					alert("등록 처리중 오류가 발생하였습니다.");
				}
			});
			
		} else {
			return;
		}
	} else {
		var param = $("#segInfoForm").serialize();
		$.getJSON("./segAdd.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("등록되었습니다.");
				document.location.href = "./segMainP.ums";
				//$("#searchForm").attr("action","./segMainP.ums").submit();
			} else if(data.result == "Fail") {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 취소 클릭시
function goCancel() {
	alert("등록이 취소되었습니다.");
	document.location.href = "./segMainP.ums";
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
			alert("List Data Error!!");
		}
	});
}

//targetService
function getTargetService(){
	var targetService = new Array();
	
	if($("#emsuseYn").is(":checked")){
		targetService.push("EMS");
	}
	if($("#smsuseYn").is(":checked")){
		targetService.push("SMS");
	}
	if($("#pushuseYn").is(":checked")){
		targetService.push("PUSH");
	}
	return targetService;
}

