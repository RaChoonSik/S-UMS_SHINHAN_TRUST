/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.09.02
*	설명 : 자동메일 서비스 정보수정 JavaScript
**********************************************************/

// 상태,발송상태,결재라인 변수
var rnsStatus = "";
var rnsWorkStatus = "";
var rnsApprLineYn = "";
var rnsApprProcYn = "";

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

// 템플릿 파일을 읽어 편집기에 값 설정
function setServiceContent(contentsTyp, contentsPath) {
	if($.trim(contentsTyp) == "0" || $.trim(contentsTyp) == "2")
	$.getJSON("./serviceFileView.json?contentsPath=" + contentsPath, function(res) {
		if(res.result == 'Success') {
			oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
			oEditors.getById["ir1"].exec("PASTE_HTML", [res.contVal]);	//스마트에디터 내용붙여넣기
		}
	});
}

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
	$("#txtWebAgentUrl").html("형식이 지정되지 않았습니다.");
	$("#webAgentAttachYn").prop("checked", false);
	alert("웹에이전트 등록이 취소되었습니다.");
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

// 테스트발송 클릭시
function popRnsTestSend() {
	$("#tidTest").val( $("#tids").val() );
	
	fn.popupOpen('#popup_testsend_user');
}

// 테스트발송 팝업창에서 추가 클릭시
function goRnsTestUserAdd() {
	if($("#rid").val() == "") {
		alert("ID를 입력해주세요.");
		$("#rid").focus();
		return ;
	}
	if($("#rname").val() == "") {
		alert("수신자를 입력해주세요.");
		$("#rname").focus();
		return ;
	}
	if($("#rmail").val() == "") {
		alert("이메일을 입력해주세요.");
		$("#rmail").focus();
		return ;
	}
	
	// 기존 등록된 없을 경우 초기화
	if($("#rnsTestUserList input").length == 0) {
		$("#rnsTestUserList").empty();
	}
	var userHtml = "";
	userHtml += '<tr>';
	userHtml += '<td><label><input type="checkbox" name="rids" value="'+ $("#rid").val() +'|'+ $("#rname").val() +'|'+ $("#rmail").val() +'"><span></span></label></td>';
	userHtml += '<td>'+ $("#rid").val() +'</td>';
	userHtml += '<td>'+ $("#rname").val() +'</td>';
	userHtml += '<td>'+ $("#rmail").val() +'</td>';
	userHtml += '</tr>';
	
	$("#rnsTestUserList").append(userHtml);
	
	$("#rid").val("");
	$("#rname").val("");
	$("#rmail").val("");
}

// 테스트발송 팝업창에서 삭제 클릭시
function goRnsTestUserDelete() {
	$("#listForm input[name='rids']").each(function(idx,item){
		if($(item).is(":checked")) {
			$(item).closest("tr").remove();
		}
	});
	
	// 등록된 대상자가 없을 경우
	if($("#rnsTestUserList input").length == 0) {
		$("#rnsTestUserList").append('<tr><td colspan="4" class="no_data">등록된 내용이 없습니다.</td></tr>');
	}
}

// 테스트발송 팝업창에서 전체선택 클릭시
function goRnsUserAll() {
	$("#listForm input[name='rids']").each(function(idx,item){
		$(item).prop("checked", $("#listForm input[name='isUserAll']").is(":checked"));
	});
}

// 테스트발송 팝업창에서 테스트발송 클릭시
function goRnsTestMailSend() {
	var userChecked = false;
	$("#listForm input[name='rids']").each(function(idx,item){
		if($(item).is(":checked")) {
			userChecked = true;
		};
	});
	
	if(!userChecked) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}
	
	var param = $("#listForm").serialize();
	$.getJSON("./serviceTestSend.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("테스트메일이 발송 되었습니다.");
			fn.popupClose('#popup_testsend_user');
		} else if(data.result == "Fail") {
			alert("테스트메일이 발송중 오류가 발생하였습니다.");
		}
	});
}

// 복사 클릭시
function goCopy() { 
	var param = $("#searchForm").serialize();
	$.getJSON("./serviceCopy.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복사되었습니다.");
		} else if(data.result == "Fail") {
			alert("복사 처리중 오류가 발생하였습니다.");
		}
	});
	
}

// 사용중지 클릭시
function goDisable() {
	if(rnsWorkStatus == "202") {
		alert("결재대기, 결재반려, 결재완료 상태에서만 사용중지 가능합니다.");
		return;
	}
	
	$("#status").val("001");
	
	var param = $("#searchForm").serialize();
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("사용중지 되었습니다.");
			rnsStatus = "001";
			
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
	
	var param = $("#searchForm").serialize();
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복구 되었습니다.");
			rnsStatus = "000";
			
			$("#btnEnable").hide();
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 클릭시
function goDelete() {
	if(rnsWorkStatus == "202") {
		alert("결재진행중인 항목은 삭제 하실 수 없습니다.");
		return;
	}
	$("#status").val("002");
	 
	var param = $("#searchForm").serialize(); 
	$.getJSON("./updateServiceStatus.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제 되었습니다.");
			rnsStatus = "002";
			
			$("#searchForm").attr("target","").attr("action","./serviceListP.ums").submit();
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.")
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
	
	var templateCd =  $("#eaiCampNo").val();
	var num = templateCd.search(/[0-9]/g);
	var eng = templateCd.search(/[a-z]/ig);  
	var tid= $("#tid").val();
	
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

// 수정 클릭시 (서비스 수정)
function goServiceUpdate(status) {
	if(rnsWorkStatus == "202") {
		alert("결재 진행중에는 수정이 불가합니다.");
		return;
	}
	if(status == "002") {
		alert("삭제된 서비스입니다.\n삭제된 서비스는 수정할 수 없습니다.");
		return;
	}
	
	if(!(rnsWorkStatus == "000" || rnsWorkStatus == "201")) {
		if(rnsApprLineYn == "Y") {
			alert("결재대기 상태만 수정이 가능합니다.");
		} else {
			alert("발송대기 상태만 수정이 가능합니다.");
		}
		return;
	}


	if($("#contentsTyp").val() == "") {
		alert("콘텐츠타입은 필수입력 항목입니다.");
		$("#contentsTyp").focus();
		return;
	}
	if($("#contentsTyp").val() == "") {
		alert("콘텐츠타입은 필수입력 항목입니다.");
		$("#contentsTyp").focus();
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
	
	if($("#webAgentUrl").val() == "" ){
		if($.trim($("#contentsTyp").val()) == "2") {	// 템플릿컨텐츠
			$("#webAgentAttachYn").val("");
		} else {										// DB컨텐츠
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
	
	//XssFilter 체크
	/*
	if (existXssFilter(oEditors.getById["ir1"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
	
	// 스마트에디터 편집기 내용 composerValue로 복사
	$("#contentsTyp").removeAttr("disabled");
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	var composerValue = $("#ir1").val();
	
	$("#serviceContent").val( composerValue );
	$("#serviceInfoForm").attr("target","iFrmService").attr("action","./serviceUpdate.ums").submit();
	
	$("#contentsTyp").attr("disabled",true);
	
	/*	
	if(checkXssFilter(composerValue)){
		composerValue = cleanXssFilter(composerValue);
		oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
		oEditors.getById["ir1"].exec("PASTE_HTML", [composerValue]);	//스마트에디터 내용붙여넣기
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
	} else{
		$("#serviceContent").val( composerValue );
		$("#serviceInfoForm").attr("target","iFrmService").attr("action","./serviceUpdate.ums").submit();
	}
	*/
}

// 취소 클릭시(목록으로 이동)
function goServiceCancel() {
	$("#searchForm").attr("target","").attr("action","./serviceListP.ums").submit();
}

// 첨부파일 다운로드
function goFileDown(fileNm, filePath) {
	var param = "downType=009&attachNm=" + encodeURIComponent(fileNm) + "&attachPath=" + encodeURIComponent(filePath);
	iFrmService.location.href = "../../com/down.ums?" + param;
}

// 발송결재라인 상신 클릭시
function goRnsSubmitApproval() {
	if(rnsApprLineYn == "N") {
		alert("보안결재에 해당되지 않습니다.");
		return;
	}
	if($("#approvalYn").val() == "N") {
		alert("발송 결재라인을 등록하세요");
		popMailApproval();
		return;
	}	
	
	if(rnsStatus == "000" && rnsWorkStatus == "201") {
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
		
		if($("#webAgentUrl").val() == "" ){
			 if($.trim($("#contentsTyp").val()) == "2") {	// 템플릿컨텐츠
				$("#webAgentAttachYn").val("");
			} else {											// DB컨텐츠
				$("#webAgentAttachYn").val("");
			}
		} else {
			if($.trim($("#contentsTyp").val()) == "1") {		// 웹컨텐츠
				$("#webAgentAttachYn").val("N");
			} else if($.trim($("#c  ontentsTyp").val()) == "2") {	// 템플릿컨텐츠
				$("#webAgentAttachYn").val("Y");
			} else {											// DB컨텐츠
				$("#webAgentAttachYn").val("Y");
			}
		}
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
			
		if(confirm("보안결재를 상신 합니다.")) {
			
			//XssFilter 체크
			/*
			if (existXssFilter(oEditors.getById["ir1"])){
				alert("사용 할수 없는 스크립트가 포함되어 있습니다");
				return;
			}
			*/
			
			// 스마트에디터 편집기 내용 composerValue로 복사
			oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
			var composerValue = $("#ir1").val();
			
			/*if(checkXssFilter(composerValue)){
				composerValue = cleanXssFilter(composerValue);
				oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);			//스마트에디터 초기화
				oEditors.getById["ir1"].exec("PASTE_HTML", [composerValue]);	//스마트에디터 내용붙여넣기
				alert("사용 할수 없는 스크립트가 포함되어 있습니다");
			} else{
			*/
			$("#serviceContent").val( composerValue );
			// 수정 처리(2021.10.25 추가)
			var frm = $("#serviceInfoForm")[0];
			var frmData = new FormData(frm);
			$.ajax({
				type: "POST",
				url: './rnsSubmitApprovalP.json',
				data: frmData,
				processData: false,
				contentType: false,
				success: function (data) {
					if(data.result == "Success") {
						alert("상신 되었습니다.");
						rnsWorkStatus = "202";
						
						// 결재 메시지 전달
						var apprUserId = data.apprUserId;
						var mailTitle = data.mailTitle;
						iFrmService.location.href = "/ems/apr/APPROVAL_ALERT.jsp?rsltCd=001&apprUserId=" + apprUserId + "&mailTitle=" + encodeURIComponent(mailTitle) + "&mailTypeNm=" + encodeURIComponent("실시간");
						
						setTimeout(function(){
							$("#searchForm").attr("target","").attr("action","./serviceUpdateP.ums").submit();
						},500);
					} else {
						alert("상신 처리중 오류가 발생하였습니다.");
					}
				},
				error: function () {
					alert("상신 처리중 오류가 발생하였습니다.");
				}
			});
		} 
	} else {
		alert("[정상] [결재대기] 상태에서만 상신가능 합니다.");
	}
}

// 결재취소 클릭시
function goApprCancel() {
	if(!(rnsWorkStatus == "202" && rnsApprProcYn == "N")) {
		alert("결재취소는 결재진행 상태에서 결재된 건이 없을 경우만 가능합니다.");
		return;
	}
	$("#status").val("000");
	var param = $("#searchForm").serialize();
	$.getJSON("./apprCancel.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("결재취소되었습니다.");
			
			// 수정화면 갱신
			$("#searchForm").attr("target","").attr("action","./serviceUpdateP.ums").submit();
		} else if(data.result == "Fail") {
			alert("결재취소 처리중 오류가 발생하였습니다.");
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