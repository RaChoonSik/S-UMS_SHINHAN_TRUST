/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.24
*	설명 : 메일발송 신규등록 JavaScript
**********************************************************/

var dateRegex = RegExp(/^\d{4}.(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[01])$/);
var checkDateInput = false;

$(document).ready(function() {
	//메일유형
	$(document).on("click", "input[name='isSendTerm']", function(){
		var index = $(this).closest("label").index();
		if(index == 0){ //단기메일
			//무기한 체크되어 있으면 해제시킴		
			if($("#noLimitedCheck").is(":checked")) {
				$(noLimitedCheck).prop("checked", false);
				checkNoLImited();
			}
			$(".attr_disabled").attr("disabled",true);
		}else{	//정기메일
			$(".attr_disabled").removeAttr("disabled");
		}
	});
	
	//예약일시 : datePicker
	//var date = new Date();
	//var firstday = new Date(date.getFullYear(), date.getMonth(), "01");
	$(".datepickerrange.fromDate input").datepicker('setDate', new Date());
	$(".datepickerrange.toDate input").datepicker('setDate', new Date());
	
	// 날짜 입력 체크
	$(".datepickerrange.fromDate input").on("keyup", function(){
		checkDateInput = true;
		$(this).val( $(this).val().replace(/[^0-9.]/gi,"") );
	});
	$(".datepickerrange.fromDate input").on("click", function(){
		checkDateInput = true;
	});
	$(".datepickerrange.fromDate input").on("blur", function(){
		var dateTxt = $(this).val();
		if(dateTxt.length == 8) {
			$(this).val( dateTxt.substring(0,4) + "." + dateTxt.substring(4,6) + "." + dateTxt.substring(6) );
		}
		if(!dateRegex.test($(this).val()) && checkDateInput == true) {
			alert("날짜형식이 올바르지 않습니다.");
			checkDateInput = false;
			$(this).select();
		}
	});
	
	$(".datepickerrange.toDate input").on("keyup", function(){
		checkDateInput = true;
		$(this).val( $(this).val().replace(/[^0-9.]/gi,"") );
	});
	$(".datepickerrange.toDate input").on("click", function(){
		checkDateInput = true;
	});
	$(".datepickerrange.toDate input").on("blur", function(){
		var dateTxt = $(this).val();
		if(dateTxt.length == 8) {
			$(this).val( dateTxt.substring(0,4) + "." + dateTxt.substring(4,6) + "." + dateTxt.substring(6) );
		}
		if(!dateRegex.test($(this).val()) && checkDateInput == true) {
			alert("날짜형식이 올바르지 않습니다.");
			checkDateInput = false;
			$(this).select();
		}
	});
	
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

// 템플릿을 선택하였을 경우 에디터에 템플릿 넣기
function goTemplate() {
	if($("#tempFlPath").val() == "") {
		alert("템플릿을 선택하세요.");
		return;
	}
	
	$.getJSON("../tmp/tempFileView.json?tempFlPath=" + $("#tempFlPath").val(), function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    //스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);   //스마트에디터 내용붙여넣기
		} else {
			alert("Template Read Error!!");
		}
	});
}

// 수신자그룹을 선택하였을 경우 머지부분 처리
function goMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	
	// 머지입력 초기화
	$("#mergeKeyMail").children().remove();
	
	var condHTML = "";
	var merge = $("#segNoc").val().substring($("#segNoc").val().indexOf("|")+1);
	var pos = merge.indexOf(",");
	while(pos != -1) {
		condHTML = "<option value='$:"+merge.substring(0,pos)+":$'>"+merge.substring(0,pos)+"</option>";
		$("#mergeKeyMail").append(condHTML);
		merge = merge.substring(pos+1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:"+merge+":$'>"+merge+"</option>";
	$("#mergeKeyMail").append(condHTML);
}

// 사용자그룹 선택시 사용자 목록 조회 
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
	if($("#webAgentAttachYn").val() == "Y") {
		//$("#txtWebAgentUrl").html($("#webAgentUrl").val());
		$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
	} else {
		var contents = "^:" + $("#webAgentUrl").val() + ":^";
		oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
		oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);		//스마트에디터 내용붙여넣기
		$("#txtWebAgentUrl").html("본문삽입으로 지정되었습니다.");
	}
	fn.popupClose('#popup_web_agent');
}

// 웹에이전트 팝업창에서 취소 클릭시
function popWebAgentCancel() {
	$("#webAgentUrl").val("");
	$("#txtWebAgentUrl").html("형식이 지정되지 않았습니다.");
	$("#webAgentAttachYn").prop("checked", false);
	alert("웹에이전트 등록이 취소되었습니다.");
	fn.popupClose('#popup_web_agent');
}

// 웹에이전트 첨부파일/본문삽입 선택시
function changeAttachYn() {
	if($("#webAgentUrl").val() != "") {
		if($("#secuAttTyp").val() == "NONE"){
			$("#webAgentAttachYn").val("N");
		} else {
			$("#webAgentAttachYn").val("Y");
		}
		
		if($("#webAgentAttachYn").val() == "Y") {
			//$("#txtWebAgentUrl").html($("#webAgentUrl").val());
			$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
		} else {
			var contents = "^:" + $("#webAgentUrl").val() + ":^";
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);		//스마트에디터 내용붙여넣기
			$("#txtWebAgentUrl").html("본문삽입으로 지정되었습니다.");
		}
	} else {
		alert("웹에이전트 정보를 먼저 등록해주세요");
		$("#secuAttTyp").val("NONE").prop("selected",true);
	}
}

// 웹에이전트 미리보기 클릭시
function popWebAgentPreview() {
	if($("#webAgentUrl").val() == "") {
		alert("URL을 입력하세요.");
		fn.popupOpen('#popup_web_agent');
		$("#webAgentUrl").focus();
		return;
	}
	if($("#secuAttTyp").val() == "EXCEL") {
		alert("EXCEL은 미리보기를 할 수 없습니다.");
		return;
	}
	if($("#secuAttTyp").val() == "PDF") {
		alert("PDF는 미리보기를 할 수 없습니다.");
		return;
	}
	
	$("#iFrmWebAgent").empty();
	
	$("#previewWebAgentUrl").html($("#webAgentUrl").val());
	
	var param = $("#mailInfoForm").serialize();
	$.getJSON("./webAgentPreview.json?" + param, function(res) {
		if(res.result == 'Success') {
			iFrmWebAgent.location.href = res.webAgentUrl;
			fn.popupOpen('#popup_preview_webagent');
		} else {
			alert("웹에이전트 미리보기 처리중 오류가 발생하였습니다.");			
		}
	});
}

// 제목 클릭시(메일제목 내용 추가)
function goTitleMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	if($("#mailTitle").val() == "") {
		$("#mailTitle").val( $("#mergeKeyMail").val() );
	} else {
		var cursorPosition = $("#mailTitle")[0].selectionStart;
		$("#mailTitle").val( $("#mailTitle").val().substring(0,cursorPosition) + $("#mergeKeyMail").val() + $("#mailTitle").val().substring(cursorPosition) );
	}
}

//본문 클릭시(메일 내용 추가)
function goContMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	oEditors.getById["ir1"].exec("PASTE_HTML", [$("#mergeKeyMail").val()]);   
}


// 발송결재라인 등록 클릭시(ums.common.js 파일에 정의)
// popMailApproval()

// HTML등록 클릭시
var uploadHtmlType = "";
function popUploadHtml(upType) {
	uploadHtmlType = upType;
	$("#fileUpload")[0].reset();
	$(".fileupload .inputfile ~ button.btn").click();
	fn.popupOpen('#popup_html');
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
						if(uploadHtmlType == "mail") {
							oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
							oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
						} else if(uploadHtmlType == "temp") {
							oEditors2.getById["ir2"].exec("SET_CONTENTS", [""]);		//스마트에디터 초기화
							oEditors2.getById["ir2"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
						}
						fn.fileReset('#popup_html');
						fn.popupClose('#popup_html');
					}
				});
			},
			error: function () {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다.");
		return;
	} 
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

// 등록 클릭시 (메일 등록)
function goMailAdd() {
	if($("input[name='isSendTerm']").eq(1).is(":checked")) {
		if($("#sendTermLoop").val() == "") {
			alert("정기발송 주기는 필수입력 항목입니다.");
			$("#sendTermLoop").focus();
			return;
		}
	}
	if($("#campNo").val() == "0") {
		alert("캠페인명을 선택하세요.");
		popCampSelect();
		return;
	}
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		popSegSelect();
		return;
	}
	
	/*
	if($("#approvalYn").val() == "N") {
		alert("발송 결재라인을 등록하세요");
		popMailApproval();
		return;
	}
	*/
	
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "") {
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
	if($("#tempMktGb").is(":checked")) {
		if($("#mailMktGb").val() == "") {
			alert("마케팅 동의여부 선택은 필수 입력 항목입니다.");
			$("#mailMktGb").focus();
			return;
		}
	}
	
	//XssFilter 체크 
	/*
	if (existXssFilter(oEditors.getById["ir1"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
		
	// 스마트에디터 편집기 내용 composerValue로 복사
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#composerValue").val( $("#ir1").val() );	
	$("#sendTermEndDt").attr("disabled", false);
	$("#mailInfoForm").attr("target","iFrmMail").attr("action","./mailAdd.ums").submit();
	$("#sendTermEndDt").attr("disabled",true);
}

// 취소 클릭시(목록으로 이동)
function goMailCancel() {
	//alert("등록이 취소되었습니다.");
	document.location.href = "./taskListP.ums";
}














/***************************************************************************************************
* 2021.10.13 추가
* 캠페인/템플릿/수신자그룹 팝업작업 처리
***************************************************************************************************/

// 캠페인명 선택 클릭시(캠페인선택 팝업)
function popCampSelect() {
	$("#popCampSearchForm input[name='searchStartDt']").val("");
	$("#popCampSearchForm input[name='searchEndDt']").val("");
	$("#popCampSearchForm input[name='searchCampNm']").val("");
	goPopCampSearch("1");
	fn.popupOpen("#popup_campaign");
}

// 팝업 캠페인 목록 검색
function goPopCampSearch(pageNum) {
	$("#popCampSearchForm input[name='page']").val(pageNum);
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popCampList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 신규등록 클릭시
function goPopCampAdd() {
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popCampAdd.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록에서 캠페인명 클릭시
function goPopCampUpdate(campNo) {
	$("#popCampSearchForm input[name='campNo']").val(campNo);
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popCampUpdate.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록에서 선택 클릭시
function setPopCampInfo(campNo, campNm, campTy) {
	$("#campNo").val(campNo);
	$("#campTy").val(campTy);
	$("#txtCampNm").html(campNm);
	$("#mailTitle").val(campNm);
	
	fn.popupClose("#popup_campaign");
}

// 팝업 캠페인 목록 페이징
function goPageNumPopCamp(pageNum) {
	goPopCampSearch(pageNum);
}

// 팝업 캠페인 등록 클릭시
function goPopCampInfoAdd() {
	if($("#popCampInfoForm select[name='campTy']").val() == "") {
		alert("캠페인목적은 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='campTy']").focus();
		return;
	}
	if($("#popCampInfoForm input[name='campNm']").val() == "") {
		alert("캠페인명은 필수입력 항목입니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popCampInfoForm input[name='campNm']").val()) > 100) {
		alert("캠페인명은 100byte를 넘을 수 없습니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		$("#popCampInfoForm input[name='campNm']").select();
		return;
	}
	if($.byteString($("#popCampInfoForm textarea[name='campDesc']").val()) > 4000) {
		alert("캠페인 설명은 4000byte를 넘을 수 없습니다.");
		$("#popCampInfoForm textarea[name='campDesc']").focus();
		$("#popCampInfoForm textarea[name='campDesc']").select();
		return;
	}
	// 등록 처리
	var frm = $("#popCampInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: './campAdd.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("등록되었습니다.");
				goPopCampList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 캠페인 신규등록화면/수정화면 목록 클릭시
function goPopCampList() {
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popCampList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContCamp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 수정 클릭시
function goPopCampInfoUpdate() {
	if($("#popCampInfoForm select[name='campTy']").val() == "") {
		alert("캠페인목적은 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='campTy']").focus();
		return;
	}
	if($("#popCampInfoForm select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='status']").focus();
		return;
	}
	if($("#popCampInfoForm input[name='campNm']").val() == "") {
		alert("캠페인명은 필수입력 항목입니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popCampInfoForm input[name='campNm']").val()) > 100) {
		alert("캠페인명은 100byte를 넘을 수 없습니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		$("#popCampInfoForm input[name='campNm']").select();
		return;
	}
	if($.byteString($("#popCampInfoForm textarea[name='campDesc']").val()) > 4000) {
		alert("캠페인 설명은 4000byte를 넘을 수 없습니다.");
		$("#popCampInfoForm textarea[name='campDesc']").focus();
		$("#popCampInfoForm textarea[name='campDesc']").select();
		return;
	}
	// 수정 처리
	var frm = $("#popCampInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: './campUpdate.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("수정되었습니다.");
				goPopCampList();
			} else {
				alert("수정 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}





// 템플릿명 선택 클릭시(템플릿선택 팝업)
function popTempSelect() {
	$("#popTempSearchForm input[name='searchStartDt']").val("");
	$("#popTempSearchForm input[name='searchEndDt']").val("");
	$("#popTempSearchForm input[name='searchTempNm']").val("");
	goPopTempSearch("1");
	fn.popupOpen("#popup_template");
}

// 팝업 템플릿 목록 검색
function goPopTempSearch(pageNum) {
	$("#popTempSearchForm input[name='page']").val(pageNum);
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popTempList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 템플릿 신규등록 클릭시
function goPopTempAdd() {
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popTempAdd.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 템플릿 목록에서 템플릿명 클릭시
function goPopTempUpdate(tempNo) {
	$("#popTempSearchForm input[name='tempNo']").val(tempNo);
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popTempUpdate.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 템플릿 목록에서 선택 클릭시
function setPopTempInfo(tempNo, tempNm, tempFlPath) {
	$("#txtTempNm").html(tempNm);
	$.getJSON("../tmp/tempFileView.json?tempFlPath=" + tempFlPath, function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    //스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);   //스마트에디터 내용붙여넣기
	
			fn.popupClose("#popup_template");
		} else {
			alert("Template Read Error!!");
		}
	});
}

// 팝업 템플릿 목록 페이징
function goPageNumPopTemp(pageNum) {
	goPopTempSearch(pageNum);
}

// 팝업 템플릿 등록 클릭시
function goPopTempInfoAdd() {
	if($("#popTempInfoForm input[name='tempNm']").val() == "") {
		alert("템플릿명은 필수입력 항목입니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popTempInfoForm input[name='tempNm']").val()) > 100) {
		alert("템플릿명은 100byte를 넘을 수 없습니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		$("#popTempInfoForm input[name='tempNm']").select();
		return;
	}
	if($.byteString($("#popTempInfoForm textarea[name='tempDesc']").val()) > 200) {
		alert("템플릿 설명은 4000byte를 넘을 수 없습니다.");
		$("#popTempInfoForm textarea[name='tempDesc']").focus();
		$("#popTempInfoForm textarea[name='tempDesc']").select();
		return;
	}
	
	//XssFilter 체크
	/*
	if (existXssFilter(oEditors2.getById["ir2"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
		
	// 등록 처리
	oEditors2.getById["ir2"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#popTempInfoForm input[name='tempVal']").val( $("#ir2").val() );
	var frm = $("#popTempInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: '../tmp/tempAdd.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("등록되었습니다.");
				goPopTempList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 템플릿 신규등록화면/수정화면 목록 클릭시
function goPopTempList() {
	var param = $("#popTempSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popTempList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContTemp").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 템플릿 파일을 읽어 편집기에 값 설정
function setPopTempContent(tempFlPath) {
	$.getJSON("../tmp/tempFileView.json?tempFlPath=" + tempFlPath, function(res) {
		if(res.result == 'Success') {
			oEditors2.getById["ir2"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors2.getById["ir2"].exec("PASTE_HTML", [res.tempVal]);	//스마트에디터 내용붙여넣기
		}
	});
}

// 팝업 템플릿 수정 클릭시
function goPopTempInfoUpdate() {
	if($("#popTempInfoForm select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popTempInfoForm select[name='status']").focus();
		return;
	}
	if($("#popTempInfoForm input[name='tempNm']").val() == "") {
		alert("템플릿명은 필수입력 항목입니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popTempInfoForm input[name='tempNm']").val()) > 100) {
		alert("템플릿명은 100byte를 넘을 수 없습니다.");
		$("#popTempInfoForm input[name='tempNm']").focus();
		$("#popTempInfoForm input[name='tempNm']").select();
		return;
	}
	if($.byteString($("#popTempInfoForm textarea[name='tempDesc']").val()) > 200) {
		alert("템플릿 설명은 4000byte를 넘을 수 없습니다.");
		$("#popTempInfoForm textarea[name='tempDesc']").focus();
		$("#popTempInfoForm textarea[name='tempDesc']").select();
		return;
	}
	
	//XssFilter 체크
	/*
	if (existXssFilter(oEditors2.getById["ir2"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
	
	// 수정 처리
	oEditors2.getById["ir2"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#popTempInfoForm input[name='tempVal']").val( $("#ir2").val() );
	var frm = $("#popTempInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		url: '../tmp/tempUpdate.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("수정되었습니다.");
				goPopTempList();
			} else {
				alert("수정 처리중 오류가 발생하였습니다.");
			}
		},
		error: function () {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}




// 수신자그룹 선택 클릭시(수신자그룹선택 팝업)
function popSegSelect() {
	$("#fileUploadSeg")[0].reset();
	$(".fileupload .inputfile ~ button.btn").click();

	$("#popSegSearchForm input[name='searchStartDt']").val("");
	$("#popSegSearchForm input[name='searchEndDt']").val("");
	$("#popSegSearchForm input[name='searchSegNm']").val("");
	goPopSegSearch("1");
	fn.popupOpen("#popup_segment");
}

// 팝업 수신자그룹 목록 검색
function goPopSegSearch(pageNum) {
	$("#popSegSearchForm input[name='page']").val(pageNum);
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popSegList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 신규등록 클릭시
function goPopSegAdd() {
	var segAddUrl = "";
	if($("#popSegSearchForm select[name='addCreateTy']").val() == "") {
		alert("신규등록 할 유형을 선택해주세요.");
		$("#popSegSearchForm select[name='addCreateTy']").focus();
		return;
	}
	if($("#popSegSearchForm select[name='addCreateTy']").val() == "003") {
		segAddUrl = "./pop/popSegAddFile.ums";
	} else {
		segAddUrl = "./pop/popSegAddSql.ums";
	}
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : segAddUrl + "?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 목록에서 수신자그룹명 클릭시
function goPopSegUpdate(segNo, createTy) {
	var segUpdateUrl = "";
	if(createTy == "003") {
		segUpdateUrl = "./pop/popSegUpdateFile.ums";
	} else if(createTy == "002") {
		segUpdateUrl = "./pop/popSegUpdateSql.ums";
	}
	$("#popSegSearchForm input[name='segNo']").val(segNo);
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : segUpdateUrl + "?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 목록에서 선택 클릭시
function setPopSegInfo(segNoc, segNm) {
	$("#segNoc").val(segNoc);
	$("#txtSegNm").html(segNm);
	
	// 머지입력 초기화
	$("#mergeKeyMail").children().remove();
	
	var condHTML = "";
	var merge = segNoc.substring(segNoc.indexOf("|")+1);
	var pos = merge.indexOf(",");
	while(pos != -1) {
		condHTML = "<option value='$:"+merge.substring(0,pos)+":$'>"+merge.substring(0,pos)+"</option>";
		$("#mergeKeyMail").append(condHTML);
		merge = merge.substring(pos+1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:"+merge+":$'>"+merge+"</option>";
	$("#mergeKeyMail").append(condHTML);
	
	fn.popupClose("#popup_segment");
}

// 팝업 수신자그룹 목록 페이징
function goPageNumPopSeg(pageNum) {
	goPopSegSearch(pageNum);
}

// 팝업 수신자그룹 파일 업로드
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
				$("#popSegInfoFormMail input[name='tempFlPath']").val(result.oldFileName);
				$("#popSegInfoFormMail input[name='segFlPath']").val(result.newFileName);
				$("#headerInfo").val(result.headerInfo);
				$("#popSegInfoFormMail input[name='separatorChar']").val("");
				fn.fileReset('#popup_file_seg');
				fn.popupClose('#popup_file_seg');
			},
			error: function () {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다. csv, txt 만 가능합니다.");
	}
}

// 샘플 클릭시(샘플파일 다운로드)
function downloadSample() {
	iFrmMail.location.href = "../../com/down.ums?downType=003";
}

// 다운로드 버튼 클릭(수신자그룹파일 다운로드)
function goSegDownload() {
	$("#popSegInfoFormMail").attr("target","iFrmMail").attr("action","../../com/down.ums").submit();
}

// 구분자 등록(확인)
function fncSep(userId) {
	if($("#popSegInfoFormMail input[name='segFlPath']").val() == "") {
		$("#popSegInfoFormMail input[name='separatorChar']").val("");
		alert("파일이 입력되어 있지 않습니다.\n파일을 입력하신 후 구분자를 입력해 주세요.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if($("#popSegInfoFormMail input[name='separatorChar']").val() == "") {
		alert("구분자를 입력하세요.");
		$("#popSegInfoFormMail input[name='separatorChar']").focus();
		return;
	}
	var tmp = $("#popSegInfoFormMail input[name='segFlPath']").val().substring($("#popSegInfoFormMail input[name='segFlPath']").val().lastIndexOf("/")+1);
	$("#popSegInfoFormMail input[name='segFlPath']").val("addressfile/" + userId + "/" + tmp);
	var param = $("#popSegInfoFormMail").serialize();
	$.ajax({
		type : "POST",
		url : "/ems/seg/segFileMemberListP.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#iFrmMail").contents().find("body").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

// 팝업 수신자그룹(파일) 등록 클릭시
function goPopSegInfoAddFile(userId) {
	if($("#popSegInfoFormMail input[name='segFlPath']").val() == "") {
		alert("파일등록은 필수입력 항목입니다.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if($("#popSegInfoFormMail input[name='separatorChar']").val() == "") {
		alert("구분자는 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='separatorChar']").focus();
		return;
	}
	if($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}
	
	//수신자 그룹 체크 항목
	var targetService = "EMS";
	var headerInfo = $("#headerInfo").val();
	var dataInfo = "";
	var separatorChar = $("#popSegInfoFormMail input[name='separatorChar']").val();
	var headerInfoArr = new Array();
	if(headerInfo != "N"){
		if(headerInfo != null && headerInfo != ""){
			headerInfoArr = headerInfo.split(separatorChar);
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
	if($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail textarea[name='segDesc']").focus();
		$("#popSegInfoFormMail textarea[name='segDesc']").select();
		return;
	}
	
	// 등록시 경로 추가  : addressfile/userId (2022.03.22)
	var tmp = $("#segFlPath").val().substring($("#segFlPath").val().lastIndexOf("/")+1);
	$("#segFlPath").val("addressfile/" + userId + "/" + tmp);
	
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("등록되었습니다.");
			goPopSegList();
		} else if(data.result == "Fail") {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

function goPopSegQueryTest(type) {
	if($("#popSegInfoFormMail textarea[name='query']").val() == "") {
		alert("QUERY를 입력하세요.");
		$("#popSegInfoFormMail textarea[name='query']").focus();
		return;
	}
	
	if (invalidQuery()) {
		return;
	}
	
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segDirectSQLTest.json?" + param, function(data) {
		if(data.result == 'Success') {
			$("#mergeKey").val(data.mergeKey);
			$("#mergeCol").val(data.mergeKey);
			if(type == "000") {				// QUERY TEST
				alert("성공.\n[MERGE_KEY : " + data.mergeKey + "]");
				goSegCnt();
			} else if(type == "001") {		// 등록
				goPopSegInfoAddSql();
			} else if(type == "002") {		// 수정
				goPopSegInfoUpdateSql();
			}
		} else if(data.result == 'Fail') {
			alert("실패\n" + data.message);
		}
	});
}

// 수신자그룹 재실행 쿼리 검증 
function invalidQuery(){
	var invalid = false;
	var validStr = "";
	var query = "";
	var alertMsg = "";
	//재실행 쿼리 체크 
	if($("#popRetryQuery").val() != "") {
		validStr = "NEO_SENDLOG";
		query = $("#popRetryQuery").val();
		query = query.replace(/(\r\n|\n|\r)/gm, " ");
		query = query.toUpperCase(); 
		if(query.indexOf(validStr) == -1){
			alertMsg ="[재실행쿼리] NEO_SEND_LOG 테이블은 필수이니다";
			invalid = true; 
		}
	}
	
	if(invalid){
		alert(alertMsg);
	}

	return invalid;
}

// 대상수 구하기
function goSegCnt() {
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segCount.json?" + param, function(data) {
		$("#txtTotCnt").html(data.totCnt + "명");
		$("#totCnt").val(data.totCnt);
	});
}

// 팝업 수신자그룹(SQL) 등록 클릭시
function goPopSegInfoAddSql() {
	if($("#popSegInfoFormMail select[name='dbConnNo']").val() == "") {
		alert("Connection은 필수입력 항목입니다.");
		$("#popSegInfoFormMail select[name='dbConnNo']").focus();
		return;
	}
	if($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}
	if($("#popSegInfoFormMail textarea[name='query']").val() == "") {
		alert("QUERY는 필수입력 항목입니다.");
		$("#popSegInfoFormMail textarea[name='query']").focus();
		return;
	}
	//수신자 그룹 체크 항목
	var targetService = "EMS";
	var headerInfo = $("#mergeKey").val();
	var dataInfo = "";
	var headerInfoArr = new Array();
	if(headerInfo != null && headerInfo != ""){
		
		headerInfoArr = headerInfo.split(",");
		var errstr = checkSegmentFileInfo(headerInfoArr, dataInfo, targetService);
	
		if(errstr != null && errstr != "" ){
			alert("다음 정보를 확인하세요.\n" + errstr);
			return;
		}
	}else{
		alert("등록한 파일을 확인해 주세요.");
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail textarea[name='segDesc']").focus();
		$("#popSegInfoFormMail textarea[name='segDesc']").select();
		return;
	}
	
	// 등록 처리
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("등록되었습니다.");
			goPopSegList();
		} else if(data.result == "Fail") {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 템플릿 신규등록화면/수정화면 목록 클릭시
function goPopSegList() {
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./pop/popSegList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divPopContSeg").html(pageHtml);
		},
		error : function(){
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹(파일) 수정 클릭시
function goPopSegInfoUpdateFile(userId) {
	if($("#popSegInfoFormMail input[name='segFlPath']").val() == "") {
		alert("파일등록은 필수입력 항목입니다.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if($("#popSegInfoFormMail input[name='separatorChar']").val() == "") {
		alert("구분자는 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='separatorChar']").focus();
		return;
	}
	if($("#popSegInfoFormMail select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popSegInfoFormMail select[name='status']").focus();
		return;
	}
	if($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}
	
	//수신자 그룹 체크 항목
	var targetService = "EMS";
	var headerInfo = $("#headerInfo").val();
	var dataInfo = "";
	var separatorChar = $("#popSegInfoFormMail input[name='separatorChar']").val();
	var headerInfoArr = new Array();
	if(headerInfo != "N"){
		if(headerInfo != null && headerInfo != ""){
			headerInfoArr = headerInfo.split(separatorChar);
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
	if($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail textarea[name='segDesc']").focus();
		$("#popSegInfoFormMail textarea[name='segDesc']").select();
		return;
	}
	
	// 수정시 경로 추가 : addressfile/userId (2022.03.22)
	var tmp = $("#segFlPath").val().substring($("#segFlPath").val().lastIndexOf("/")+1);
	$("#segFlPath").val("addressfile/" + userId + "/" + tmp);
	
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segUpdate.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수정되었습니다.");
			goPopSegList();
		} else if(data.result == "Fail") {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 수신자그룹(SQL) 수정 클릭시
function goPopSegInfoUpdateSql() {
	if($("#popSegInfoFormMail select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popSegInfoFormMail select[name='status']").focus();
		return;
	}
	if($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}
	if($("#popSegInfoFormMail textarea[name='query']").val() == "") {
		alert("QUERY 필수입력 항목입니다.");
		$("#popSegInfoFormMail textarea[name='query']").focus();
		return;
	}
	//수신자 그룹 체크 항목
	var targetService = "EMS";
	var headerInfo = $("#mergeKey").val();
	var dataInfo = "";
	var headerInfoArr = new Array();
	if(headerInfo != null && headerInfo != ""){
		
		headerInfoArr = headerInfo.split(",");
		var errstr = checkSegmentFileInfo(headerInfoArr, dataInfo, targetService);
	
		if(errstr != null && errstr != "" ){
			alert("다음 정보를 확인하세요.\n" + errstr);
			return;
		}
	}else{
		alert("등록한 파일을 확인해 주세요.");
		return;
	}
	// 입력값 Byte 체크
	if($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail textarea[name='segDesc']").focus();
		$("#popSegInfoFormMail textarea[name='segDesc']").select();
		return;
	}
	
	// 수정 처리
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segUpdate.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수정되었습니다.");
			goPopSegList();
		} else if(data.result == "Fail") {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}

// 무기한 체크
function checkNoLImited() {
	if($("#noLimitedCheck").is(":checked")) {
		$("#sendTermEndDt").datepicker('setDate','9999.12.31');
		$("#sendTermEndDt").prop("readonly", true);
		$("#sendTermEndDt").attr("disabled",true);		
	} else {
		$("#sendTermEndDt").datepicker('setDate',new Date());
		$("#sendTermEndDt").prop("readonly", false);
		$("#sendTermEndDt").attr("disabled",false);
	}
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
