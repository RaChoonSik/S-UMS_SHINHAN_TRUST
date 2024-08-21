/**********************************************************
*	작성자 : 김재환
*	작성일시 : 2021.12.08
*	설명 : 카카오 알림톡 발송 신규등록 JavaScript
**********************************************************/
$(document).ready(function() {

	$(".datepickerrange.fromDate input").datepicker('setDate', new Date());
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
	
});

function getKakaoTemplateInfo() {
	$("#smsMessage").val("");
	$("#smsName").val("");
	
	var tempCd= $("#templateList").val();
	if (tempCd == "") {
		return ;
	}
	
	var param = "/sys/seg/getKakaoTemplateInfo.json?tempCd=" + tempCd;
	$.getJSON(param, function(data) {
		if(data.result == "Success") {
			$("#smsMessage").val(data.tempContent);
			$("#orgTempMessage").val(data.tempContent);
			$("#smsName").val(data.tempNm);
			$("#tempCd").val(data.tempCd);
			setValidate();
			param = $("#kakaoInfoForm").serialize(); 
			console.log(param);
			$.ajax({
				type : "GET",
				url : "./kakaoTemplateMerge.ums?" + param,
				dataType : "html",
				success : function(pageHtml){
					$("#divTemplateMergeList").html(pageHtml);
				},
				error : function(){
					alert("카카오 템플릿 머지항목 조회 실패!");
				}
			});
		}
	});
}

// 캠페인을 선택하였을 때 발생하는 이벤트
function goCamp() {
	if ($("#campInfo").val() == "") {
		$("#campNo").val("0");
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
			$("#kakaoInfoForm input[name='phoneNumber']").each(function(idx, item) {
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
	if (addPhoneNumberCount == 0 ) {
		$("#addPhoneNone").show();
	} else {
		$("#addPhoneNone").hide();
	}
}

//추가수신번호 새로고침 
function addPhoneNumberClear() {
	$('#addPhoneNumberList').empty();
	$('#addPhoneCount').val('0');
	addPhoneNumberCount = 0;
}

// 머지함수 체인지시에 템플릿내용 변동처리
function setMerge(){
	setValidate();
	
	var valueArray = new Array();
	
	var size = document.getElementsByName("merge").length;
	for(var i = 0; i < size; i++){
		if(document.getElementsByName("merge")[i].value != ""){
			var kakaoMerge = document.getElementsByName("merge")[i].value;
			valueArray.push(kakaoMerge);
		}
	}
	
	var smsMessage = $("#orgTempMessage").val();

	if(valueArray.length > 0) {
		for (var i = 0 ; i < valueArray.length ; i ++){
			var valueArrayString = valueArray[i];
			var kakaoTemplateItem = valueArrayString.substring(0, valueArrayString.indexOf("|"));
			//var mergeItem = valueArrayString.substring(valueArrayString.indexOf("$"));
			var mergeItem = "$:" + valueArrayString.substring(valueArrayString.indexOf("|") + 1) + ":$";
			smsMessage =replaceKakaoMergeMessage (smsMessage, kakaoTemplateItem, mergeItem);
		}
		$("#smsMessage").val(smsMessage);
	}
}

function replaceKakaoMergeMessage(target, kakaoTemplateItem, mergeItem){
	var result = "";
	/*
	var regex = new RegExp( kakaoTemplateItem, "g");
	result = target.replace(regex,mergeItem );
	*/
	result = target.replaceAll(kakaoTemplateItem,mergeItem );
	return result;
}

function replaceAll(str, searchStr, replaceStr) {
	return str.split(searchStr).join(replaceStr);
}

// 등록 클릭시 (카카오 알림톡 등록)
function goKakaoAdd() {
	
	if (checkedValid == false){
		alert("유효성 검사를 진행해주세요");
		return;
	}
	if (checkForm()) {
		return;
	}

	var smsPhones = "";

	$("#kakaoInfoForm input[name='phoneNumber']").each(function(idx, item) {
		smsPhones += $(item).val() + ",";
	});

	$("#kakaoInfoForm input[name='sendTyp']").val($('input[name="chkSendCond"]:checked').val());
	$("#kakaoInfoForm input[name='smsPhones']").val(smsPhones);

	var size = document.getElementsByName("merge").length;
	var valueArray = new Array();
	var kakaoMergeCols = "";
	if (size > 0 ) {
		for(var i = 0; i < size; i++){
			if(document.getElementsByName("merge")[i].value != ""){
				var kakaoMerge = document.getElementsByName("merge")[i].value;
				valueArray.push(kakaoMerge);
			}
		}
		
		if(valueArray.length > 0) {
			for (var i = 0 ; i < valueArray.length ; i ++){
				kakaoMergeCols += valueArray[i] +",";
			}
		}
	}
	
	$("#kakaoInfoForm input[name='kakaoMergeCols']").val(kakaoMergeCols);
	
	$("#kakaoInfoForm").attr("target", "iFrmMail").attr("action", "./kakaoAdd.ums").submit();
}

// 입력 폼 검사
function checkForm() {

	var errstr = "";
	var errflag = false;

	if ($("#campNo").val() == "0") {
		errstr += "[캠페인]";
		errflag = true;
	}
	
	if ($("#segNoc").val() == ""){
		errstr += "[수신자 그룹]";
		errflag = true;
	}
	
	var regExp = /[^#{\}]+(?=})/g;
	let msgRegResult =  $("#smsMessage").val().match(regExp);
	var templateLen = 0 ;
	templateLen = $("#templateLength").val();
	if (msgRegResult != null) {
		if ( templateLen == msgRegResult.length){
			errstr += "[입력된 템플릿 함수 항목이 없습니다]";
			errflag = true;
		}
	}
	
	if (typeof $("#deptNo").val() != "undefined") {
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

var checkedValid = false;
var resultValud = false; 
function checkValidate() {
	checkedValid = true;
	var size = document.getElementsByName("merge").length;
	var valid = true; ;
	for(var i = 0; i < size; i++){
		if(document.getElementsByName("merge")[i].value == "선택"){
			valid = false;
		}
	}
	
	if (valid == false){
		alert("템플릿과 등록된 내용이 유효하지 않습니다. 알람톡 등록시 SMS로 발송되어 집니다");
		document.getElementById('btnValid').className = 'btn btn-validity'; 
		$('#validMessage').attr('style', "display:'';"); 
		$('#validYn').val("N");
	} else {
		alert("템플릿과 등록된 내용이 유효합니다");
		document.getElementById('btnValid').className = 'btn fullgreen';
		$('#validMessage').attr('style', "display:none;");
		$('#validYn').val("Y");
	}
	resultValud = valid;
}

function setValidate() {
	checkedValid = false;
	document.getElementById('btnValid').className = 'btn btn-validity';
	$('#validMessage').attr('style', "display:none;");
}

function goKakaoCancel() {
	$("#searchForm").attr("target","").attr("action","./kakaoListP.ums").submit();
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
		url: "/sms/cam/pop/popCampList.ums?" + param,
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
		url: "/sms/cam/pop/popCampAdd.ums?" + param,
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
		url: "/sms/cam/pop/popCampUpdate.ums?" + param,
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
		url: "/sms/cam/pop/popCampList.ums?" + param,
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
		url: "/sms/cam/pop/popSegList.ums?" + param,
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
		segAddUrl = "/sms/cam/pop/popSegAddFile.ums";
	} else {
		segAddUrl = "/sms/cam/pop/popSegAddSql.ums";
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
		segUpdateUrl = "/sms/cam/pop/popSegUpdateFile.ums";
	} else if (createTy == "002") {
		segUpdateUrl = "/sms/cam/pop/popSegUpdateSql.ums";
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
	
	var segNo = $("#segNoc").val().substring(0, $("#segNoc").val().indexOf("|"));
	$("#segNo").val(segNo);
	
	getKakaoTemplateInfo();
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
		url: "/sys/seg/segFileMemberListP.ums?" + param,
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

	// 등록시 경로 추가  : addressfile/userId (2022.03.22)
	var tmp = $("#segFlPath").val().substring($("#segFlPath").val().lastIndexOf("/")+1);
	$("#segFlPath").val("addressfile/" + userId + "/" + tmp);
	
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/sys/seg/segAdd.json?" + param, function(data) {
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
	$.getJSON("/sys/seg/segDirectSQLTest.json?" + param, function(data) {
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
	$.getJSON("/sys/seg/segCount.json?" + param, function(data) {
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

	// 등록 처리
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/sys/seg/segAdd.json?" + param, function(data) {
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
		url: "/sms/cam/pop/popSegList.ums?" + param,
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

	// 수정시 경로 추가 : addressfile/userId (2022.03.22)
	var tmp = $("#segFlPath").val().substring($("#segFlPath").val().lastIndexOf("/")+1);
	$("#segFlPath").val("addressfile/" + userId + "/" + tmp);
	
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/sys/seg/segUpdate.json?" + param, function(data) {
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
	$.getJSON("/sys/seg/segUpdate.json?" + param, function(data) {
		if (data.result == "Success") {
			alert("수정되었습니다.");
			goPopSegList();
		} else if (data.result == "Fail") {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}
