/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.10.30
*	설명 : 문자발송 수정  JavaScript
**********************************************************/
$(document).ready(function() {
	goMerge();
	setAttatchFileCount();
	setPhonePreview("");
	setButton();
	$('#addPhoneCount').val(addPhoneNumberCount);

	$("#smsName").keyup(function() {
		setPhonePreview();
	});
	$("#smsMessage").keyup(function() {
		setPhonePreview();
	});
	
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
	
	// 예약일시,정기발송종료일 설정
	setTimeout(function(){
		$("#sendYmd").datepicker("setDate",sendYmd);
	},5);
	
});

// 캠페인을 선택하였을 때 발생하는 이벤트
function goCamp() {
	if ($("#campInfo").val() == "") {
		$("#campNo").val("");
		$("#campTy").val("");
	} else {
		var tmp = $("#campInfo").val();
		var campNo = tmp.substring(0, tmp.indexOf("|"));
		tmp = tmp.substring(tmp.indexOf("|") + 1);
		var campTy = tmp.substring(0, tmp.indexOf("|"));

		$("#campNo").val(campNo);
		$("#campTy").val(campTy);
	}
}

// 수신자그룹을 선택하였을 경우 머지부분 처리
function goMerge() {
	if ($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}

	// 머지입력 초기화
	$("#mergeKeySms").children().remove();

	var condHTML = "";
	var merge = $("#segNoc").val().substring($("#segNoc").val().indexOf("|") + 1);
	var pos = merge.indexOf(",");
	while (pos != -1) {
		condHTML = "<option value='$:" + merge.substring(0, pos) + ":$'>" + merge.substring(0, pos) + "</option>";
		$("#mergeKeySms").append(condHTML);
		merge = merge.substring(pos + 1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:" + merge + ":$'>" + merge + "</option>";
	$("#mergeKeySms").append(condHTML);
}

function getTextLength(input) {
	var len = 0;

	for (var i = 0; i < input.length; i++) {
		if (escape(input.charAt(i)).length == 6) {
			len++;
		}
		len++;
	}
	return len;
}
//  사용자그룹 선택시 사용자 목록 조회   
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx, item) {
			var option = new Option(item.userNm, item.userId);
			$("#userId").append(option);
		});
	});
}


// 제목 클릭시(메일제목 내용 추가)
function goTitleMerge() {
	if ($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	if ($("#smsName").val() == "") {
		$("#smsName").val($("#mergeKeySms").val());
	} else {
		var cursorPosition = $("#smsName")[0].selectionStart;
		$("#smsName").val($("#smsName").val().substring(0, cursorPosition) + $("#mergeKeySms").val() + $("#smsName").val().substring(cursorPosition));
	}
	setPhonePreview();
}

//본문 클릭시(메일 내용 추가)
function goContMerge() {
	if ($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}

	addSmsMessage($("#mergeKeySms").val(), 2);

	setPhonePreview();
}

function addSmsMessage(addMessage, pos) {
	var element = document.getElementById("smsMessage");
	var strOriginal = element.value;
	var iStartPos = element.selectionStart;
	var iEndPos = element.selectionEnd;
	if (pos == 0) { //(광고) 클릭시 넣기 
		iStartPos = pos;
		iEndPos = pos;
	} else if (pos == 1) { //(광고) 클릭시 수신거부 내역 
		iStartPos = element.value.length;
		iEndPos = iStartPos;
	}
	var strFront = "";
	var strEnd = "";

	if (iStartPos == iEndPos) {
		strFront = strOriginal.substring(0, iStartPos);
		strEnd = strOriginal.substring(iStartPos, strOriginal.length);
	} else return;

	element.value = strFront + addMessage + strEnd;
}

// 파일첨부 팝업창에서 등록 클릭시
var totalFileCnt = 0;
var totalFileByte = 0;
var limitFileCnt = 1;
function setAttatchFileCount() {
	totalFileCnt = $("#attachCnt").val();
}

function attachFileUpload() {
	if ($("#upTempAttachPath").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}

	// 파일 중복 체크
	var fileDupCheck = false;
	$("#smsInfoForm input[name='attachNm']").each(function(idx, item) {
		if ($(item).val() == $("#upTempAttachPath").val()) {
			fileDupCheck = true;
		}
	});
	if (fileDupCheck) {
		alert("이미 등록된 파일입니다.");
		return;
	}

	// 첨부파일 갯수 체크
	if (totalFileCnt >= 4) {
		alert("파일 첨부는 최대 3개까지 등록 가능합니다.");
		return;
	}

	// 첨부파일 용량 체크
	var fileByte = $("#fileUrl2")[0].files[0].size;
	if (totalFileByte + fileByte > 61440) {
		alert("첨부파일의 용량은 최대 60kb까지 입니다.");
		return;
	}

	//var extCheck = "jpg,png,jpeg,bmp,gif";
	var extCheck = "jpg";
	var fileName = $("#upTempAttachPath").val();
	var fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);

	if (extCheck.toLowerCase().indexOf(fileExt.toLowerCase()) >= 0) {
		var frm = $("#attachUpload")[0];
		var frmData = new FormData(frm);
		$.ajax({
			type: "POST",
			enctype: 'multipart/form-data',
			url: '../../com/uploadImgFile.json',
			data: frmData,
			processData: false,
			contentType: false,
			success: function(result) {
				alert("파일 업로드가 완료되었습니다.");

				var s = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'],
					e = Math.floor(Math.log(fileByte) / Math.log(1024));
				var fileSizeTxt = (fileByte / Math.pow(1024, e)).toFixed(2) + " " + s[e];

				var attachFileHtml = "";
				attachFileHtml += '<li>';
				attachFileHtml += '<input type="hidden" name="attachNm" value="' + result.oldFileName + '">';
				attachFileHtml += '<input type="hidden" name="attachPath" value="' + result.newFileName + '">';
				attachFileHtml += '<span>' + result.oldFileName + '</span>';
				attachFileHtml += '<em>' + fileSizeTxt + '</em>';
				attachFileHtml += '<button type="button" class="btn-del" onclick="deleteAttachFile(this)"><span class="hide">삭제</span></button>';
				attachFileHtml += '</li>';

				totalFileCnt++;
				totalFileByte += fileByte;

				$("#smsAttachFileList").append(attachFileHtml);

				$("#popFileDelete").click();
				frm.reset();

				fn.popupClose('#popup_file');

				if (totalFileCnt == limitFileCnt) {
					$('#btnAttachFile').attr('disabled', 'disabled');
				}
				setPhonePreview();
			},
			error: function(e) {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다. jpg,png,jpeg,bmp,gif만 가능합니다.");
	}
}

// 첨부파일 삭제
function deleteAttachFile(obj) {
	totalFileCnt--;
	var attachSize = $(obj).closest("li").children("input[name='attachSize']").val();
	totalFileByte = totalFileByte - attachSize;
	$(obj).closest("li").remove();

	if (totalFileCnt < 3) {
		$('#btnAttachFile').removeAttr('disabled');
	}
	setPhonePreview();
}

var addPhoneNumberCount = 0;
function addPhoneNumber() {
	var addPhone = $("#addPhone").val();

	if (addPhone.length == 10 || addPhone.length == 11) {
		var regPhone = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
		if (regPhone.test(addPhone) === true) {
			if (addPhone.length == 10) {
				addPhone = addPhone.substr(0, 3) + "-" + addPhone.substr(3, 3) + "-" + addPhone.substr(6, 4);
			} else {
				addPhone = addPhone.substr(0, 3) + "-" + addPhone.substr(3, 4) + "-" + addPhone.substr(7, 4);
			}

			var validPhone = true;
			$("#smsInfoForm input[name='phoneNumber']").each(function(idx, item) {
				if ($(item).val() == addPhone) {
					validPhone = false;
				}
			});

			if (!validPhone) {
				alert("이미 추가된 번호입니다.");
				return;
			}

			var addPhoneHtml = "";
			addPhoneHtml += '<li><span>' + addPhone + '</span>';
			addPhoneHtml += '<input type="hidden" name="phoneNumber" value="' + addPhone + '">';
			addPhoneHtml += '<button type="button" class="btn-del" onclick="deletePhoneNumber(this)" name="phoneNumber" ><span class="hide">삭제</span></button>';
			addPhoneHtml += '</li>';

			$("#addPhoneNumberList").append(addPhoneHtml);
			$("#addPhone").val("");
			addPhoneNumberCount++;
			$("#addPhoneCount").val(addPhoneNumberCount);
			$("#addPhoneNone").hide();
		}
	} else {
		alert("입력된 핸드폰 번호는 핸드폰 번호 양식과 맞지 않습니다 다시 입력해주세요");
		$("#addPhone").focus();
	}
}

// 추가 번호 삭제
function deletePhoneNumber(obj) {
	$(obj).closest("li").remove();
	addPhoneNumberCount--;
	$("#addPhoneCount").val(addPhoneNumberCount);
	if (addPhoneNumberCount == 0) {
		$("#addPhoneNone").show();
	}
}

//추가수신번호 새로고침 
function addPhoneNumberClear() {
	$('#addPhoneNumberList').empty();
	$('#addPhoneCount').val('0');
	addPhoneNumberCount = 0;
}

// 등록 클릭시 (SMS 등록)
function goSmsUpdate() {

	if (checkForm()) {
		return;
	}

	var smsPhones = "";

	$("#smsInfoForm input[name='phoneNumber']").each(function(idx, item) {
		smsPhones += $(item).val() + ",";
	});

	if ($('#chklegalYn').is(':checked')) {
		$("#smsInfoForm input[name='legalYn']").val("Y");
	}

	$("#smsInfoForm input[name='sendTyp']").val($('input[name="chkSendCond"]:checked').val());
	$("#smsInfoForm input[name='smsPhones']").val(smsPhones);

	$("#smsInfoForm").attr("target", "iFrmMail").attr("action", "./smsUpdate.ums").submit();
}

// 발송승인 클릭시
function goSmsAdmit() {
	var param = $("#smsInfoForm").serialize();

	$.getJSON("./smsAdmit.json?" + param, function(data) {
		if (data.result == "Success") {
			alert("승인에 성공하였습니다");
			$("#status").val("001");
			setButton();
		} else {
			alert("승인에 실패하였습니다");
		}
	});
}

// 복사 클릭시
function goSmsCopy() {
	var param = $("#smsInfoForm").serialize();

	$.getJSON("./smsCopy.json?" + param, function(data) {
		if (data.result == "Success") {
			alert("복사에 성공하였습니다");
		} else {
			alert("복사에 실패하였습니다");
		}
	});
}

// 삭제 클릭시
function goSmsDelete() {
	$("#smsStatus").val("001");
	var param = $("#smsInfoForm").serialize();
	$.getJSON("./smsDelete.json?" + param, function(data) {
		if (data.result == "Success") {
			alert("삭제에 성공하였습니다");
			setButton();
		} else {
			alert("삭제에 실패하였습니다");
		}
	});
}

// 복구 클릭시
function goSmsEnable() {
	$("#smsStatus").val("000");
	var param = $("#smsInfoForm").serialize();
	$.getJSON("./smsDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("복구 되었습니다.");
			setButton();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");	// 복구실패
		}
	});
}

function setPhonePreview() {
	var previewHtml = "";
	var attatchList = $('input[name="attachPath"]');
	var filePath = "";
	var filePath2 = "";
	var filePath3 = "";

	var addStartMessage = "(광고)";
	var addEndMessage = "무료수신거부 " + $("#legalCf").val();

	var imgDomainPath = document.location.origin + $('#imgUploadPath').val();

	var imgDomainPath = document.location.origin + $('#imgUploadPath').val();
	if (attatchList.length > 0) {

		for (var i = 0; i < attatchList.length; i++) {
			if (i == 1) {
				filePath2 = imgDomainPath + "/" + attatchList[i].value;
			} else if (i == 2) {
				filePath3 = imgDomainPath + "/" + attatchList[i].value;
			} else {
				filePath = imgDomainPath + "/" + attatchList[i].value;
			}
		}
	}

	previewHtml += '<div class="preview-txt">';
	if (filePath.length > 0) {
		previewHtml += '<img src="' + filePath + '" alt="첨부파일1">';
	}
	if (filePath2.length > 0) {
		previewHtml += '<img src="' + filePath2 + '" alt="첨부파일2">';
	}
	if (filePath3.length > 0) {
		previewHtml += '<img src="' + filePath3 + '" alt="첨부파일3">';
	}
	previewHtml += '<p>';

	var smsMessage = ""
	if ($("#chklegalYn").prop("checked")) {
		smsMessage = addStartMessage + " " + $("#smsName").val() + "<br>" + $("#smsMessage").val() + "<br><br>" + addEndMessage;
	} else {
		smsMessage = $("#smsName").val() + "<br>" + $("#smsMessage").val();
	}
	previewHtml += smsMessage + '</p>';

	var messageByte = byteCheck(smsMessage);
	previewHtml += '<span class="byte">' + messageByte + '/2000byte</span>';
	previewHtml += '</div>';

	$("#divPhonePreview").empty();
	$("#divPhonePreview").append(previewHtml);
	setSmsGubun(messageByte);
}

// 파일 첨부 또는 글자수 입력에 따른 SMS gubun값 변경 
function setSmsGubun(messageByte) {
	/*
	var legalMsgPre = "(광고)";
	var smsName = $("#smsName").val();
	var smsMessage = $("#smsMessage").val();
	var legalMsgTail = "문자수신거부 080-0000-0000";
	var strText = "";
	if ($('#chklegalYn').is(':checked')) {
		strText = legalMsgPre + " " + smsName + "\n" + smsMessage + "\n" + legalMsgTail;
	} else {
		strText =smsName + "\n" + smsMessage + "\n";
	}
	
	var byteText = 0;

	byteText = getTextLength(strText);
	$("#messageByte").val(byteText + "/2000byte");
	if (byteText > 2000) {
		alert("문자 길이는 2000Byte를 넘을수 없습니다");
		return;
	} else {
		if (byteText > 80) {
			$("#smsTitle").html("LMS (장문 메시지)");
			$("#gubun").val("001");
		} else {
			$("#smsTitle").html("SMS (단문 메시지)");
			$("#gubun").val("000");
		}
	}

	if (totalFileCnt > 0) {
		$("#smsTitle").html("MMS (멀티미디어 메시지)");
		$("#gubun").val("002");
	}*/ 
	if (messageByte > 2000) {
		alert("문자 길이는 2000Byte를 넘을수 없습니다");
		return;
	} else {
		if (messageByte > 80) {
			$("#smsTitle").html("LMS (장문 메시지)");
			$("#gubun").val("001");
		} else {
			$("#smsTitle").html("SMS (단문 메시지)");
			$("#gubun").val("000");
		}
	}

	if (totalFileCnt > 0) {
		$("#smsTitle").html("MMS (멀티미디어 메시지)");
		$("#gubun").val("002");
	}
}

function setButton(){
	var status = $("#status").val();
	var smsStatus = $("#smsStatus").val();
	
	$('#btnSmsAdmit').hide();
	$('#btnSmsDelete').hide();
	$('#btnSmsUpdate').hide();
	$('#btnSmsEnable').hide();
		
	if(smsStatus == "000"){
		if(status == "000"){
			$('#btnSmsAdmit').show();
		}
		$('#btnSmsDelete').show();
		$('#btnSmsUpdate').show();
	}
	
	if(smsStatus == "001"){
		$('#btnSmsEnable').show();
	}
}

function byteCheck(smsMessage) {
	var codeByte = 0;
	for (var idx = 0; idx < smsMessage.length; idx++) {
		var oneChar = escape(smsMessage.charAt(idx));
		if (oneChar.length == 1) {
			codeByte++;
		} else if (oneChar.indexOf("%u") != -1) {
			codeByte += 2;
		} else if (oneChar.indexOf("%") != -1) {
			codeByte++;
		}
	}
	return codeByte;
}

// 입력 폼 검사
function checkForm() {

	var errstr = "";
	var errflag = false;

	if ($("#campNo").val() == "0") {
		errstr += "[캠페인]";
		errflag = true;
	}
	if ($("#segNoc").val() == "") {
		errstr += "[수신자그룹]";
		errflag = true;
	}
	if (typeof $("#deptNo").val() != "undefined") {
		if ($("#deptNo").val() == "") {
			alert("사용자그룹을 선택하세요.");
			$("#deptNo").focus();
			return;
		}
		if ($("#deptNo").val() != "0" && $("#userId").val() == "") {
			errstr += "[사용자명]";
			errflag = true;
		}
	}
	if ($("#taskNm").val() == "") {
		errstr += "[문자명]";
		errflag = true;
	}
	if ($("#smsName").val() == "") {
		errstr += "[문자제목]";
		errflag = true;
	}
	if ($("#sendNm").val() == "") {
		errstr += "[발송자명]";
		errflag = true;
		errflag = true;
	}
	if ($("#sendTelno").val() == "") {
		errstr += "[발송자연락처]";
		errflag = true;
	}

	if (errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}

	return errflag;
}

// 취소 클릭시(목록으로 이동)
function goSmsCancel() {
	$("#searchForm").attr("target","").attr("action","./smsListP.ums").submit();
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
	fn.popupOpen("#popup_sms_campaign");
}

// 팝업 캠페인 목록 검색
function goPopCampSearch(pageNum) {
	$("#popCampSearchForm input[name='page']").val(pageNum);
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: "./pop/popCampList.ums?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContCamp").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 신규등록 클릭시
function goPopCampAdd() {
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: "./pop/popCampAdd.ums?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContCamp").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록에서 캠페인명 클릭시
function goPopCampUpdate(campNo) {
	$("#popCampSearchForm input[name='campNo']").val(campNo);
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: "./pop/popCampUpdate.ums?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContCamp").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 목록에서 선택 클릭시
function setPopCampInfo(campNo, campNm, campTy) {
	$("#campNo").val(campNo);
	$("#campTy").val(campTy);
	$("#txtCampNm").html(campNm);

	fn.popupClose("#popup_sms_campaign");
}

// 팝업 캠페인 목록 페이징
function goPageNumPopCamp(pageNum) {
	goPopCampSearch(pageNum);
}

// 팝업 캠페인 등록 클릭시
function goPopCampInfoAdd() {
	if ($("#popCampInfoForm select[name='campTy']").val() == "") {
		alert("캠페인목적은 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='campTy']").focus();
		return;
	}
	if ($("#popCampInfoForm input[name='campNm']").val() == "") {
		alert("캠페인명은 필수입력 항목입니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if ($.byteString($("#popCampInfoForm input[name='campNm']").val()) > 100) {
		alert("캠페인명은 100byte를 넘을 수 없습니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		$("#popCampInfoForm input[name='campNm']").select();
		return;
	}
	if ($.byteString($("#popCampInfoForm textarea[name='campDesc']").val()) > 4000) {
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
		success: function(res) {
			if (res.result == "Success") {
				alert("등록되었습니다.");
				goPopCampList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function() {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 캠페인 신규등록화면/수정화면 목록 클릭시
function goPopCampList() {
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: "./pop/popCampList.ums?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContCamp").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 캠페인 수정 클릭시
function goPopCampInfoUpdate() {
	if ($("#popCampInfoForm select[name='campTy']").val() == "") {
		alert("캠페인목적은 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='campTy']").focus();
		return;
	}
	if ($("#popCampInfoForm select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popCampInfoForm select[name='status']").focus();
		return;
	}
	if ($("#popCampInfoForm input[name='campNm']").val() == "") {
		alert("캠페인명은 필수입력 항목입니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		return;
	}
	// 입력값 Byte 체크
	if ($.byteString($("#popCampInfoForm input[name='campNm']").val()) > 100) {
		alert("캠페인명은 100byte를 넘을 수 없습니다.");
		$("#popCampInfoForm input[name='campNm']").focus();
		$("#popCampInfoForm input[name='campNm']").select();
		return;
	}
	if ($.byteString($("#popCampInfoForm textarea[name='campDesc']").val()) > 4000) {
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
		success: function(res) {
			if (res.result == "Success") {
				alert("수정되었습니다.");
				goPopCampList();
			} else {
				alert("수정 처리중 오류가 발생하였습니다.");
			}
		},
		error: function() {
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
	fn.popupOpen("#popup_sms_segment");
}

// 팝업 수신자그룹 목록 검색
function goPopSegSearch(pageNum) {
	$("#popSegSearchForm input[name='page']").val(pageNum);
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: "./pop/popSegList.ums?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContSeg").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 신규등록 클릭시
function goPopSegAdd() {
	var segAddUrl = "";
	if ($("#popSegSearchForm select[name='addCreateTy']").val() == "") {
		alert("신규등록 할 유형을 선택해주세요.");
		$("#popSegSearchForm select[name='addCreateTy']").focus();
		return;
	}
	if ($("#popSegSearchForm select[name='addCreateTy']").val() == "003") {
		segAddUrl = "./pop/popSegAddFile.ums";
	} else {
		segAddUrl = "./pop/popSegAddSql.ums";
	}
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: segAddUrl + "?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContSeg").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 목록에서 수신자그룹명 클릭시
function goPopSegUpdate(segNo, createTy) {
	var segUpdateUrl = "";
	if (createTy == "003") {
		segUpdateUrl = "./pop/popSegUpdateFile.ums";
	} else if (createTy == "002") {
		segUpdateUrl = "./pop/popSegUpdateSql.ums";
	}
	$("#popSegSearchForm input[name='segNo']").val(segNo);
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: segUpdateUrl + "?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContSeg").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹 목록에서 선택 클릭시
function setPopSegInfo(segNoc, segNm) {
	$("#segNoc").val(segNoc);
	$("#txtSegNm").html(segNm);

	// 머지입력 초기화
	$("#mergeKeySms").children().remove();

	var condHTML = "";
	var merge = segNoc.substring(segNoc.indexOf("|") + 1);
	var pos = merge.indexOf(",");
	while (pos != -1) {
		condHTML = "<option value='$:" + merge.substring(0, pos) + ":$'>" + merge.substring(0, pos) + "</option>";
		$("#mergeKeySms").append(condHTML);
		merge = merge.substring(pos + 1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:" + merge + ":$'>" + merge + "</option>";
	$("#mergeKeySms").append(condHTML);

	fn.popupClose("#popup_sms_segment");
}

// 팝업 수신자그룹 목록 페이징
function goPageNumPopSeg(pageNum) {
	goPopSegSearch(pageNum);
}

// 팝업 수신자그룹 파일 업로드
function addressUpload() {
	if ($("#upTempFlPath").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}

	var extCheck = "csv,txt";
	var fileName = $("#upTempFlPath").val();
	var fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);

	if (extCheck.toLowerCase().indexOf(fileExt.toLowerCase()) >= 0) {
		var frm = $("#fileUploadSeg")[0];
		var frmData = new FormData(frm);
		$.ajax({
			type: "POST",
			enctype: 'multipart/form-data',
			url: '../../com/uploadSegFile.json',
			data: frmData,
			processData: false,
			contentType: false,
			success: function(result) {
				alert("파일 업로드가 완료되었습니다.");

				$("#upTempFlPath").val(result.oldFileName);
				$("#upSegFlPath").val(result.newFileName);
				$("#popSegInfoFormMail input[name='tempFlPath']").val(result.oldFileName);
				$("#popSegInfoFormMail input[name='segFlPath']").val(result.newFileName);
				$("#headerInfo").val(result.headerInfo);
				$("#popSegInfoFormMail input[name='separatorChar']").val("");
				fn.popupClose('#popup_file_seg');
			},
			error: function() {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다. csv, txt 만 가능합니다.");
	}
}

// 샘플 클릭시(샘플파일 다운로드)
function downloadSample() {
	iFrmMail.location.href = "../../com/down.ums?downType=005";
}

// 다운로드 버튼 클릭(수신자그룹파일 다운로드)
function goSegDownload() {
	$("#popSegInfoFormMail").attr("target", "iFrmMail").attr("action", "../../com/down.ums").submit();
}

// 구분자 등록(확인)
function fncSep(userId) {
	if ($("#popSegInfoFormMail input[name='segFlPath']").val() == "") {
		$("#popSegInfoFormMail input[name='separatorChar']").val("");
		alert("파일이 입력되어 있지 않습니다.\n파일을 입력하신 후 구분자를 입력해 주세요.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if ($("#popSegInfoFormMail input[name='separatorChar']").val() == "") {
		alert("구분자를 입력하세요.");
		$("#popSegInfoFormMail input[name='separatorChar']").focus();
		return;
	}
	var tmp = $("#popSegInfoFormMail input[name='segFlPath']").val().substring($("#popSegInfoFormMail input[name='segFlPath']").val().lastIndexOf("/") + 1);
	$("#popSegInfoFormMail input[name='segFlPath']").val("addressfile/" + userId + "/" + tmp);
	var param = $("#popSegInfoFormMail").serialize();
	$.ajax({
		type: "POST",
		url: "/ems/seg/segFileMemberListP.ums?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#iFrmMail").contents().find("body").html(pageHtml);
		},
		error: function() {
			alert("Error!!");
		}
	});
}

// 팝업 수신자그룹(파일) 등록 클릭시
function goPopSegInfoAddFile(userId) {
	if ($("#popSegInfoFormMail input[name='segFlPath']").val() == "") {
		alert("파일등록은 필수입력 항목입니다.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if ($("#popSegInfoFormMail input[name='separatorChar']").val() == "") {
		alert("구분자는 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='separatorChar']").focus();
		return;
	}
	if ($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}

	// 입력값 Byte 체크
	if ($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if ($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail textarea[name='segDesc']").focus();
		$("#popSegInfoFormMail textarea[name='segDesc']").select();
		return;
	}

	// 등록시 경로 추가  : addressfile/userId (2022.03.22)
	var tmp = $("#segFlPath").val().substring($("#segFlPath").val().lastIndexOf("/") + 1);
	$("#segFlPath").val("addressfile/" + userId + "/" + tmp);

	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segAdd.json?" + param, function(data) {
		if (data.result == "Success") {
			alert("등록되었습니다.");
			goPopSegList();
		} else if (data.result == "Fail") {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

function goPopSegQueryTest(type) {
	if ($("#popSegInfoFormMail textarea[name='query']").val() == "") {
		alert("QUERY를 입력하세요.");
		$("#popSegInfoFormMail textarea[name='query']").focus();
		return;
	}

	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segDirectSQLTest.json?" + param, function(data) {
		if (data.result == 'Success') {
			$("#mergeKey").val(data.mergeKey);
			$("#mergeCol").val(data.mergeKey);
			if (type == "000") {				// QUERY TEST
				alert("성공.\n[MERGE_KEY : " + data.mergeKey + "]");
				goSegCnt();
			} else if (type == "001") {		// 등록
				goPopSegInfoAddSql();
			} else if (type == "002") {		// 수정
				goPopSegInfoUpdateSql();
			}
		} else if (data.result == 'Fail') {
			alert("실패\n" + data.message);
		}
	});
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
	if ($("#popSegInfoFormMail select[name='dbConnNo']").val() == "") {
		alert("Connection은 필수입력 항목입니다.");
		$("#popSegInfoFormMail select[name='dbConnNo']").focus();
		return;
	}
	if ($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}
	if ($("#popSegInfoFormMail textarea[name='query']").val() == "") {
		alert("QUERY는 필수입력 항목입니다.");
		$("#popSegInfoFormMail textarea[name='query']").focus();
		return;
	}

	// 입력값 Byte 체크
	if ($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if ($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail textarea[name='segDesc']").focus();
		$("#popSegInfoFormMail textarea[name='segDesc']").select();
		return;
	}

	// 등록 처리
	var param = $("#popSegInfoFormMail").serialize();
	console.log(param);
	$.getJSON("/ems/seg/segAdd.json?" + param, function(data) {
		if (data.result == "Success") {
			alert("등록되었습니다.");
			goPopSegList();
		} else if (data.result == "Fail") {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 템플릿 신규등록화면/수정화면 목록 클릭시
function goPopSegList() {
	var param = $("#popSegSearchForm").serialize();
	$.ajax({
		type: "GET",
		url: "./pop/popSegList.ums?" + param,
		dataType: "html",
		success: function(pageHtml) {
			$("#divPopContSeg").html(pageHtml);
		},
		error: function() {
			alert("Page Loading Error!!");
		}
	});
}

// 팝업 수신자그룹(파일) 수정 클릭시
function goPopSegInfoUpdateFile(userId) {
	if ($("#popSegInfoFormMail input[name='segFlPath']").val() == "") {
		alert("파일등록은 필수입력 항목입니다.");
		fn.popupOpen('#popup_file_seg');
		return;
	}
	if ($("#popSegInfoFormMail input[name='separatorChar']").val() == "") {
		alert("구분자는 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='separatorChar']").focus();
		return;
	}
	if ($("#popSegInfoFormMail select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popSegInfoFormMail select[name='status']").focus();
		return;
	}
	if ($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}
	//수신자 그룹 체크 항목
	var targetService = "SMS";
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
	if ($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if ($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
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
		if (data.result == "Success") {
			alert("수정되었습니다.");
			goPopSegList();
		} else if (data.result == "Fail") {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}

// 팝업 수신자그룹(SQL) 수정 클릭시
function goPopSegInfoUpdateSql() {
	if ($("#popSegInfoFormMail select[name='status']").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#popSegInfoFormMail select[name='status']").focus();
		return;
	}
	if ($("#popSegInfoFormMail input[name='segNm']").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		return;
	}
	if ($("#popSegInfoFormMail textarea[name='query']").val() == "") {
		alert("QUERY 필수입력 항목입니다.");
		$("#popSegInfoFormMail textarea[name='query']").focus();
		return;
	}
	//수신자 그룹 체크 항목
	var targetService = "SMS";
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
	if ($.byteString($("#popSegInfoFormMail input[name='segNm']").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail input[name='segNm']").focus();
		$("#popSegInfoFormMail input[name='segNm']").select();
		return;
	}
	if ($.byteString($("#popSegInfoFormMail textarea[name='segDesc']").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#popSegInfoFormMail textarea[name='segDesc']").focus();
		$("#popSegInfoFormMail textarea[name='segDesc']").select();
		return;
	}

	// 수정 처리
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/ems/seg/segUpdate.json?" + param, function(data) {
		if (data.result == "Success") {
			alert("수정되었습니다.");
			goPopSegList();
		} else if (data.result == "Fail") {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}
