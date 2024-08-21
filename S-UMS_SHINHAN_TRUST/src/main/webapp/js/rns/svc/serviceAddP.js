/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.01
*	설명 : 자동메일 서비스 신규등록 JavaScript
**********************************************************/


$(document).ready(function(){
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

// 템플릿을 선택하였을 경우 에디터에 템플릿 넣기
function goTemplate() {
	if($("#tempFlPath").val() == "") {
		alert("템플릿을 선택하세요.");
		return;
	}
	
	$.getJSON("../tmp/tempFileView.json?tempFlPath=" + $("#tempFlPath").val(), function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    		//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.tempVal]);   //스마트에디터 내용붙여넣기
			
			$("#textContent").text(res.tempVal);
			$("#textContent").text($("#textContent").text());
		} else {
			alert("Template Read Error!!");
		}
	});
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
	$("#txtWebAgentUrl").html($("#webAgentUrl").val());
	
	if($("#webAgentAttachYn").val() == "Y") {
		$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
	} else {
		var contents = "^:" + $("#webAgentUrl").val() + ":^";
		oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
		oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);		//스마트에디터 내용붙여넣기
		$("#txtWebAgentUrl").html("본문삽입으로 지정되었습니다.");
	}
		
	/*
	if($("#webAgentAttachYn").is(":checked") == false) {
		var contents = "^:" + $("#webAgentUrl").val() + ":^";
		oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);   //스마트에디터 내용붙여넣기
		alert("웹에이전트가 등록되었습니다.");
	} else {
		alert("첨부파일로 지정되었습니다.");
	}
	*/
	fn.popupClose('#popup_web_agent');
}

// 웹에이전트 팝업창에서 취소 클릭시
function popWebAgentCancel() {
	$("#webAgentUrl").val("");
	$("#txtWebAgentUrl").html("URL(첨부파일) 형식이 지정되지 않았습니다.");
	//$("#webAgentAttachYn").prop("checked", false);
	//alert("웹에이전트 등록이 취소되었습니다.");
	fn.popupClose('#popup_web_agent');
}


// 웹에이전트 첨부파일/본문삽입 선택시
function changeAttachYn() {
	if($("#webAgentUrl").val() != "") {
		if($("#webAgentAttachYn").val() == "Y") {
			//$("#txtWebAgentUrl").html($("#webAgentUrl").val());
			$("#txtWebAgentUrl").html("첨부파일로 지정되었습니다.");
		} else {
			var contents = "^:" + $("#webAgentUrl").val() + ":^";
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [contents]);		//스마트에디터 내용붙여넣기
			$("#txtWebAgentUrl").html("본문삽입으로 지정되었습니다.");
		}
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
	
	$("#iFrmWebAgent").empty();
	
	$("#previewWebAgentUrl").html($("#webAgentUrl").val());
	
	var param = $("#serviceInfoForm").serialize();
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
	if($("#mergeKey").val() == "") {
		alert("머지입력을 선택하세요.");
		return;
	}
	if($("#emailSubject").val() == "") {
		$("#emailSubject").val( $("#mergeKey").val() );
	} else {
		var cursorPosition = $("#emailSubject")[0].selectionStart;
		$("#emailSubject").val( $("#emailSubject").val().substring(0,cursorPosition) + $("#mergeKey").val() + $("#emailSubject").val().substring(cursorPosition) );
	}
}

//본문 클릭시(메일 내용 추가)
function goContMerge() {
	if($("#mergeKey").val() == "") {
		alert("머지입력을 선택하세요.");
		return;
	}
	oEditors.getById["ir1"].exec("PASTE_HTML", [$("#mergeKey").val()]);   
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
			url: '../../com/uploadFileDir.json',
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
	$("#serviceInfoForm input[name='attachNm']").each(function(idx,item){
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
		url: '../../com/uploadFileDir.json',
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

// 템플릿 코드 체크(중복 확인)
function checkEaiCampNo() {	
 
	if($("#eaiCampNo").val() == "") {
		alert("검색할 템플릿 코드를 입력해주세요");
		return;
	} 
	
	var templateCd = $("#eaiCampNo").val();
	var num = templateCd.search(/[0-9]/g);
	var eng = templateCd.search(/[a-z]/ig);  
	var tid = 0;
	if(templateCd.length < 2 || templateCd.length > 12){
		alert("3자리 ~ 12자리 이내로 입력해주세요"); 
		$("#eaiCampNo").focus();
		$("#eaiCampNo").select();
		return;
	}else if(templateCd.search(/\s/) != -1){	
		alert("템플릿코드는 공백 없이 입력해주세요");
		$("#eaiCampNo").focus();
		$("#eaiCampNo").select();
		return; 
	}else if( (num < 0 || eng < 0) ){
		alert("영문,숫자를 혼합하여 입력해주세요");
		$("#eaiCampNo").focus();
		$("#eaiCampNo").select(); 
		return;
	}
	
	$.getJSON("/rns/svc/eaiCampNoCheck.json?eaiCampNo=" + templateCd + "&tid=" + tid , function(data) {	
		if(data.result == "Success") {
			alert("사용가능한 템플릿 코드입니다");	 
			$("#chkEaiCampNo").val("Y");
			$("#chkEaiCampNo").text("확인");
			$("#eaiCampNo").attr("disabled",true);
			$("#chkEaiCampNo").attr("disabled",true);	
		} else if(data.result == "Fail") {
			alert("이미 사용중인 템플릿 코드입니다.");
		}
	}); 
}

// 등록 클릭시 (서비스 등록)
function goServiceAdd() {
	if($("#contentsTyp").val() == "") {
		alert("콘텐츠타입은 필수입력 항목입니다.");
		$("#contentsTyp").focus();
		return;
	}
	
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
	
	if($("#sname").val() == "") {
		alert("발송자명은 필수입력 항목입니다.");
		$("#sname").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	
	if($("#smail").val() == "") {
		alert("발송자 이메일은 필수입력 항목입니다.");
		$("#smail").focus();
		fn.popupOpen('#popup_setting');
		return;
	}
	
	// 콘텐츠 타입에 따른 처리
	if($.trim($("#contentsTyp").val()) == "1") {
		if($("#webAgentUrl").val() == "") {
			alert("웹에이전트는 필수입력 항목입니다.");
			fn.popupOpen('#popup_web_agent');
			return;
		}
	}
	/*
	if($("#approvalYn").val() == "N") {
		alert("발송 결재라인을 등록하세요");
		popMailApproval();
		return;
	}
	*/
	/*
	if($.trim($("#contentsTyp").val()) == "1") {		// 웹컨텐츠
		$("#webAgentAttachYn").val("N");
	} else if($.trim($("#contentsTyp").val()) == "2") {	// 템플릿컨텐츠
		$("#webAgentAttachYn").val("Y");
	} else {											// DB컨텐츠
		$("#webAgentUrl").val("");
		$("#webAgentAttachYn").val("");
	}
	*/
	if($("#webAgentUrl").val() == "" ){
		 if($.trim($("#contentsTyp").val()) == "2") {	// 템플릿컨텐츠
			$("#webAgentAttachYn").val("");
		} else {											// DB컨텐츠
			$("#webAgentAttachYn").val("");
		}
	} else {
		if($.trim($("#contentsTyp").val()) == "1") {		// 웹컨텐츠
			$("#webAgentAttachYn").val("N");
		} else if($.trim($("#contentsTyp").val()) == "2") {	// 템플릿컨텐츠
			$("#webAgentAttachYn").val("Y");
		} else {											// DB컨텐츠
			$("#webAgentAttachYn").val("Y");
		}
	}
	
	if($("#tnm").val() == "") {
		alert("서비스명은 필수입력 항목입니다.");
		$("#tnm").focus();
		return;
	}
	
	if($("#emailSubject").val() == "") {
		alert("메일제목은 필수입력 항목입니다.");
		$("#emailSubject").focus();
		return;
	} 
	
	
	var templateCd= $("#eaiCampNo").val();
	if (templateCd != "") {
		/*var num = templateCd.search(/[0-9]/g);
		var eng = templateCd.search(/[a-z]/ig);  
		
		if(templateCd.length < 2 || pw.length > 12){
			alert("3자리 ~ 12자리 이내로 입력해주세요"); 
			$("#eaiCampNo").focus();
			$("#eaiCampNo").select();
			return;
		}else if(templateCd.search(/\s/) != -1){	
			alert("템플릿코드는 공백 없이 입력해주세요");
			$("#eaiCampNo").focus();
			$("#eaiCampNo").select();
			return; 
		}else if( (num < 0 && eng < 0) ){
			alert("영문,숫자를 혼합하여 입력해주세요");
			$("#eaiCampNo").focus();
			$("#eaiCampNo").select(); 
			return;
		}*/
		if ($("#chkEaiCampNo").val() != "Y"){
			alert("템플릿 코드 중복 여부를 체크하셔야합니다");
			$("#chkEaiCampNo").focus();
			return;
		}
		
		$("#eaiCampNo").removeAttr("disabled");
	}
	
	$("#contentsTyp").removeAttr("disabled");
	
	//XssFilter 체크
	/*
	if (existXssFilter(oEditors.getById["ir1"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
	
	// 스마트에디터 편집기 내용 serviceContent로 복사
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#serviceContent").val( $("#ir1").val() );

	$("#serviceInfoForm").attr("target","iFrmService").attr("action","./serviceAdd.ums").submit();
	$("#contentsTyp").attr("disabled",true);
}

// 취소 클릭시(목록으로 이동)
function goServiceCancel() {
	alert("등록이 취소되었습니다.");
	document.location.href = "./serviceListP.ums";
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