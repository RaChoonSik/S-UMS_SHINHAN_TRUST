/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.26
*	설명 : 단기메일발송 정보수정 JavaScript
**********************************************************/

// 상태,발송상태,결재라인 변수
var mailStatus = "";
var mailWorkStatus = "";
var mailApprLineYn = "";

$(document).ready(function() {
	//발송결재라인 등록 팝업 내 사용자
	$(document).on("click", ".content-area .user button", function(){
		if($(this).closest("li").hasClass("active")){
			$(this).closest("li").removeClass("active");
		}else{
			$(this).closest("li").addClass("active").siblings().removeClass("active");
		}
	});
	
	// 발송결재라인등록 팝업창에서 사용자명 입력 엔터키 처리
	$("#searchUserNm").keypress(function(e) {
		if(e.which == 13) {
			popSearchUser();
		}
	});
	
	//드래그앤드롭
	$("#apprUserList").sortable();
	
	// 캠페인 목적 설정
	goCamp();
	
	// 머지 설정
	goMerge();
	
	// 예약일시 설정
	setTimeout(function(){
		$(".datepicker input").datepicker("setDate",sendYmd);
	},5);
});

// 캠페인을 선택하였을 때 발생하는 이벤트
function goCamp() {
	if($("#campInfo").val() == "") {
		$("#campNo").val("");
		$("#campTy").val("");
	} else {
		var tmp = $("#campInfo").val();
		var campNo = tmp.substring(0, tmp.indexOf("|"));
		tmp = tmp.substring(tmp.indexOf("|") + 1);
		var campTy = tmp.substring(0, tmp.indexOf("|"));

		$("#campNo").val( campNo );
		$("#campTy").val( campTy );
	}
}

//템플릿을 선택하였을 경우 에디터에 템플릿 넣기
function goTemplate() {
	if($("#tempFlPath").val() == "") {
		alert("템플릿을 선택하세요.");
		return;
	}
	
	$.getJSON("../tmp/tempFileView.json?tempFlPath=" + $("#tempFlPath").val(), function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    		//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
		} else {
			alert("Template Read Error!!");
		}
	});
}

// 수신자그룹을 선택하였을 경우 머지부분 처리
function goMerge() {
	// 머지입력 초기화
	$("#mergeKey").children().remove();
	
	if($("#segNoc").val() == "") {
		//alert("수신자그룹을 선택하세요.");
		return;
	}
	
	var condHTML = "";
	var merge = $("#segNoc").val().substring($("#segNoc").val().indexOf("|")+1);
	var pos = merge.indexOf(",");
	while(pos != -1) {
		condHTML = "<option value='$:"+merge.substring(0,pos)+":$'>"+merge.substring(0,pos)+"</option>";
		$("#mergeKey").append(condHTML);
		merge = merge.substring(pos+1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:"+merge+":$'>"+merge+"</option>";
	$("#mergeKey").append(condHTML);
}

//사용자그룹 선택시 사용자 목록 조회 
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

// 환경설정 클릭시
function popEnvSetting() {
	fn.popupOpen('#popup_setting');
}

// 환경설정 팝업창에서 등록 클릭시
function popSettingSave() {
	if($("#mailFromNm").val() == "") {
		alert("발송자명을 입력하세요.");
		$("#mailFromNm").focus();
		return;
	}
	if($("#mailFromEm").val() == "") {
		alert("발송자 이메일을 입력하세요.");
		$("#mailFromEm").focus();
		return;
	}
	if($("#replyToEm").val() == "") {
		alert("반송 이메일을 입력하세요.");
		$("#replyToEm").focus();
		return;
	}
	if($("#returnEm").val() == "") {
		alert("리턴 이메일을 입력하세요.");
		$("#returnEm").focus();
		return;
	}
	$("#txtMailFromNm").html( $("#mailFromNm").val() );
	alert("환경설정이 완료되었습니다.");
	fn.popupClose('#popup_setting');
}

// 마케팅수신 동의유형
function checkMktGb() {
	if($("#tempMktGb").is(":checked") == false) {
		$("#mailMktGb").val("");
	} 
}
function changeMktGb() {
	if($("#mailMktGb").val() == "") {
		$("#tempMktGb").prop("checked", false);
	} else {
		$("#tempMktGb").prop("checked", true);
	}
}

// 웹에이전트 클릭시
function popWebAgent() {
	fn.popupOpen('#popup_web_agent');
}

// 웹에이전트 팝업창에서 등록 클릭시
function popWebAgentSave() {
	if($("#webAgentUrl").val() == "") {
		alert("URL을 입력하세요.");
		$("#webAgentUrl").focus();
		return;
	}
	if($("#webAgentAttachYn").is(":checked") == false) {
		var contents = "^:" + $("#webAgentUrl").val() + ":^";
		oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);   //스마트에디터 내용붙여넣기
		$("#txtWebAgentUrl").html($("#webAgentUrl").val());
		//alert("웹에이전트가 등록되었습니다.");
	} else {
		$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
	}
	fn.popupClose('#popup_web_agent');
}

// 웹에이전트 팝업창에서 취소 클릭시
function popWebAgentCancel() {
	$("#webAgentUrl").val("");
	$("#txtWebAgentUrl").html("URL(첨부파일) 형식이 지정되지 않았습니다.");
	$("#webAgentAttachYn").prop("checked", false);
	alert("웹에이전트 등록이 취소되었습니다.");
	fn.popupClose('#popup_web_agent');
}

// 제목 클릭시(메일제목 내용 추가)
function goTitleMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	$("#mailTitle").val( $("#mailTitle").val() + $("#mergeKey").val() );
}

//본문 클릭시(메일 내용 추가)
function goContMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	oEditors.getById["ir1"].exec("PASTE_HTML", [$("#mergeKey").val()]);   
}

// 발송결재라인 등록 클릭시(ums.common.js 파일에 정의)
// popMailApproval()

// 발송결재라인 상신 클릭시
function goSubmitApproval() {
	if(mailApprLineYn == "N") {
		alert("보안결재에 해당되지 않습니다.");
		return;
	}
	if(mailStatus == "000" && mailWorkStatus == "201") {
		if(confirm("보안결재를 상신 합니다.")) {
			$("#status").val("202");
			var param = $("#searchForm").serialize();
			$.getJSON("./mailSubmitApproval.json?" + param, function(data) {
				if(data.result == "Success") {
					alert("단기메일이 상신 되었습니다.");
					mailWorkStatus = "202";
					
					// 결재 메시지 전달
					var apprUserId = data.apprUserId;
					var mailTitle = data.mailTitle;
					iFrmMail.location.href = "../apr/APPROVAL_ALERT.jsp?rsltCd=001&apprUserId=" + apprUserId + "&mailTitle=" + encodeURIComponent(mailTitle) + "&mailTypeNm=" + encodeURIComponent("대용량");
				} else if(data.result == "Fail") {
					alert("상신 처리중 오류가 발생하였습니다.");
				}
			});
		}
	} else {
		alert("[정상] [결재대기] 상태에서만 상신가능 합니다.");
	}
}


// 발송승인 클릭시
function goAdmit() {
	if($("#apprUserList input[name='apprUserId']").length > 0) {
		alert("발송결재라인이 등록된 경우는 발송 할 수 없습니다.");
		return;
	}
	if($("#campInfo").val() == "") {
		alert("캠페인명을 선택하세요.");
		$("#campInfo").focus();
		return;
	}
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		$("#segNoc").focus();
		return;
	}
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "0"){
			alert("사용자그룹을 선택하세요.");
			$("#deptNo").focus();
			return;
		}
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			alert("사용자명을 선택하세요.");
			$("#userId").focus();
			return;
		}
	}
	if($("#taskNm").val() == "") {
		alert("메일명은 필수입력 항목입니다.");
		$("#taskNm").focus();
		return;
	}
	if($("#mailTitle").val() == "") {
		alert("메일제목은 필수입력 항목입니다.");
		$("#mailTitle").focus();
		return;
	}
	if($("#mailFromNm").val() == "") {
		alert("발송자명은 필수입력 항목입니다.");
		$("#mailFromNm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("#mailFromEm").val() == "") {
		alert("발송자 이메일은 필수입력 항목입니다.");
		$("#mailFromEm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("#replyToEm").val() == "") {
		alert("반송 이메일은 필수입력 항목입니다.");
		$("#replyToEm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("#returnEm").val() == "") {
		alert("리턴 이메일은 필수입력 항목입니다.");
		$("#returnEm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	
	if(confirm("발송 하시겠습니까?")) {
		
		//XssFilter 체크 
		/*
		if (existXssFilter(oEditors.getById["ir1"])){
			alert("사용 할수 없는 스크립트가 포함되어 있습니다");
			return;
		}
		*/
		oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
		$("#composerValue").val( $("#ir1").val() );
	
		$("#mailInfoForm").attr("target","iFrmMail").attr("action","./mailUpdateAdmit.ums").submit();
	}
}


// 테스트발송 클릭시(ums.common.js 에서 나머지 처리)
function popTestSend() {
	$("#testTaskNos1").val( $("#taskNo").val() );
	$("#testSubTaskNo1").val( $("#subTaskNo").val() );
	$("#testTaskNos2").val( $("#taskNo").val() );
	$("#testSubTaskNo2").val( $("#subTaskNo").val() );
	
	getTestUserList();
	
	fn.popupOpen('#popup_testsend_user');
}

// HTML등록 등록버튼 클릭시
function goHtmlUpload(userId) {
	if($("#vFileName").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}
	
	var extCheck = "htm,html";
	var fileName = $("#vFileName").val();
	var fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
	
	// 첨부파일 용량 체크
	var fileByte = $("#fileUrl")[0].files[0].size;
	if( fileByte > 1048500) {
		alert("HTML파일의 용량은 최대 1MB까지 입니다.");
		return;
	}
		
	if(extCheck.toLowerCase().indexOf(fileExt.toLowerCase()) >= 0) {
		var frm = $("#fileUpload")[0];
		var frmData = new FormData(frm);
		$.ajax({
			type: "POST",
			enctype: 'multipart/form-data',
			url: '../../com/uploadFile.json',
			data: frmData,
			processData: false,
			contentType: false,
			success: function (result) {
				alert("HTML 업로드가 완료되었습니다.");
				$("#vFileName").val(result.oldFileName);
				$("#rFileName").val(result.newFileName);
				
				// 편집기에 파일내용 표시
				$.getJSON("../tmp/tempFileView.json?tempFlPath=" + encodeURIComponent("html/" + userId + "/" + result.newFileName), function(res) {
					if(res.result == 'Success') {
						oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
						oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
						fn.fileReset('#popup_html');
						fn.popupClose('#popup_html');
					}
				});
			},
			error: function (e) {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다.");
		return;
	} 
}

// 사용중지 클릭시
function goDisable() {
	if(!(mailWorkStatus == "000" || mailWorkStatus == "001" || mailWorkStatus == "201")) {
		alert("결재대기, 발송대기, 발송승인 상태에서만 사용중지 가능합니다.");
		return;
	}
	
	$("#status").val("001");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("단기메일이 사용중지 되었습니다.");
			mailStatus = "001";
			
			$("#btnDisable").hide();
			$("#btnEnable").removeClass("hide");
			$("#btnEnable").show();
			// 메일 목록 페이지로 이동
			//$("#searchForm").attr("target","").attr("action","./mailListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 복구 클릭시
function goEnable() {
	$("#status").val("000");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("단기메일이 복구 되었습니다.");
			mailStatus = "000";
			
			$("#btnEnable").hide();
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();
			// 메일 목록 페이지로 이동
			//$("#searchForm").attr("target","").attr("action","./mailListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");	// 복구실패
		}
	});
}

// 삭제 클릭시
function goDelete() {
	if(!(mailWorkStatus == "000" || mailWorkStatus == "001" || mailWorkStatus == "201")) {
		alert("결재대기, 발송대기, 발송승인 상태에서만 삭제 가능합니다.");
		return;
	}
	
	$("#status").val("002");
	var param = $("#searchForm").serialize();
	$.getJSON("./mailDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("단기메일이 삭제되었습니다.");
			mailStatus = "002";
			
			// 메일 목록 페이지로 이동
			$("#searchForm").attr("target","").attr("action","./mailListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("단기메일 삭제중 오류가 발생하였습니다.");
		}
	});
}

// 복사 클릭시
function goCopy() {
	var param = $("#searchForm").serialize();
	$.getJSON("./mailCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
			
			// 메일 목록 페이지로 이동
			$("#searchForm").attr("target","").attr("action","./mailListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
}

// 파일첨부 팝업창에서 등록 클릭시
var totalFileCnt = 0;
var totalFileByte = 0;
function attachFileUpload() {
	if($("#upTempAttachPath").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}
	
	// 파일 중복 체크
	var fileDupCheck = false;
	$("#mailInfoForm input[name='attachNm']").each(function(idx,item){
		if($(item).val() == $("#upTempAttachPath").val()) {
			fileDupCheck = true;
		}
	});
	if(fileDupCheck) {
		alert("이미 등록된 파일입니다.");
		return;
	}
	
	// 첨부파일 갯수 체크
	if(totalFileCnt >= 5) {
		alert("파일 첨부는 최대 5개까지 등록 가능합니다.");
		return;
	}
	
	// 첨부파일 용량 체크
	var fileByte = $("#fileUrl2")[0].files[0].size;
	if(totalFileByte + fileByte > 10485760) {
		alert("첨부파일의 용량은 최대 10MB까지 입니다.");
		return;
	}
	
	var frm = $("#attachUpload")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		enctype: 'multipart/form-data',
		url: '../../com/uploadFile.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (result) {
			alert("파일 업로드가 완료되었습니다.");
			
			var s = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'],
				e = Math.floor(Math.log(fileByte) / Math.log(1024));
			var fileSizeTxt = (fileByte / Math.pow(1024, e)).toFixed(2) + " " + s[e];
			
			var attachFileHtml = "";
			attachFileHtml += '<li>';
			attachFileHtml += '<input type="hidden" name="attachNm" value="'+ result.oldFileName +'">';
			attachFileHtml += '<input type="hidden" name="attachPath" value="'+ result.newFileName +'">';
			attachFileHtml += '<input type="hidden" name="attachSize" value="'+ fileByte +'">';
			attachFileHtml += '<span>'+ result.oldFileName +'</span>';
			attachFileHtml += '<em>'+ fileSizeTxt +'</em>';
			attachFileHtml += '<button type="button" class="btn-del" onclick="deleteAttachFile(this)"><span class="hide">삭제</span></button>';
			attachFileHtml += '</li>';
			
			totalFileCnt++;
			totalFileByte += fileByte;
			
			$("#mailAttachFileList").append(attachFileHtml);
			
			$("#popFileDelete").click();
			frm.reset();
			
			fn.fileReset('#popup_file');
			fn.popupClose('#popup_file');
		},
		error: function (e) {
			alert("File Upload Error!!");
		}
	});
}

// 첨부파일 삭제
function deleteAttachFile(obj) {
	totalFileCnt--;
	var attachSize = $(obj).closest("li").children("input[name='attachSize']").val();
	totalFileByte = totalFileByte - attachSize;
	$(obj).closest("li").remove();
}

// 수정 클릭시
function goUpdate() {
	if(mailWorkStatus == "202") {
		alert("승인결재 진행중에는 수정이 불가합니다.");
		return;
	}
	if(mailStatus == "002") {
		alert("삭제된 메일입니다.\n삭제된 메일은 수정할 수 없습니다.");
		return;
	}
	if(!(mailWorkStatus == "000" || mailWorkStatus == "201")) {
		if(mailApprLineYn == "Y") {
			alert("결재대기 상태만 수정이 가능합니다.");
		} else {
			alert("발송대기 상태만 수정이 가능합니다.");
		}
		return;
	}

	if($("#campInfo").val() == "") {
		alert("캠페인명을 선택하세요.");
		$("#campInfo").focus();
		return;
	}
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		$("#segNoc").focus();
		return;
	}
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "0"){
			alert("사용자그룹을 선택하세요.");
			$("#deptNo").focus();
			return;
		}
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			alert("사용자명을 선택하세요.");
			$("#userId").focus();
			return;
		}
	}
	if($("#taskNm").val() == "") {
		alert("메일명은 필수입력 항목입니다.");
		$("#taskNm").focus();
		return;
	}
	if($("#mailTitle").val() == "") {
		alert("메일제목은 필수입력 항목입니다.");
		$("#mailTitle").focus();
		return;
	}
	if($("#mailFromNm").val() == "") {
		alert("발송자명은 필수입력 항목입니다.");
		$("#mailFromNm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("#mailFromEm").val() == "") {
		alert("발송자 이메일은 필수입력 항목입니다.");
		$("#mailFromEm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("#replyToEm").val() == "") {
		alert("반송 이메일은 필수입력 항목입니다.");
		$("#replyToEm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	if($("#returnEm").val() == "") {
		alert("리턴 이메일은 필수입력 항목입니다.");
		$("#returnEm").focus();
		fn.popupOpen('#popup_setting');
		return;
	}

	//XssFilter 체크
	/*
	if (existXssFilter(oEditors.getById["ir1"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
	
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#composerValue").val( $("#ir1").val() );

	$("#mailInfoForm").attr("target","iFrmMail").attr("action","./mailUpdate.ums").submit();
}

// 취소 클릭시
function goCancel() {
	//alert("수정이 취소되었습니다.");
	if($("#searchForm input[name='campNo']").val() == "0") {
		$("#searchForm").attr("target","").attr("action","./mailListP.ums").submit();
	} else {
		$("#searchForm").attr("target","").attr("action","./campMailListP.ums").submit();
	} 
}

// 메일 파일을 읽어 편집기에 값 설정
function setMailContent(contFlPath) {
	$.getJSON("./mailFileView.json?contFlPath=" + contFlPath, function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.contVal]);	//스마트에디터 내용붙여넣기
		}
	});
}

function existXssFilter(obj){
	var checkXss = false;
	
	var composerValue =obj.getIR();
	if(checkXssFilter(composerValue)){
		composerValue = cleanXssFilter(composerValue);
		obj.exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
		obj.exec("PASTE_HTML", [composerValue]);	//스마트에디터 내용붙여넣기
		checkXss = true;
	}
	return checkXss; 
}