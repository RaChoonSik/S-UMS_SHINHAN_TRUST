/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.04.01
*	설명 : PUSH알림발송 신규등록 JavaScript
**********************************************************/
$(document).ready(function() {
	$("#pushTitle").keyup(function() {
		setPushPreview();
	});
	
	$("#pushMessage").keyup(function() {
		setPushPreview();
	});
	
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

// 수신자그룹을 선택하였을 경우 머지부분 처리
function goMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	
	// 머지입력 초기화
	$("#mergeKeyPush").children().remove();
	
	var condHTML = "";
	var merge = $("#segNoc").val().substring($("#segNoc").val().indexOf("|")+1);
	var pos = merge.indexOf(",");
	while(pos != -1) {
		condHTML = "<option value='$:"+merge.substring(0,pos)+":$'>"+merge.substring(0,pos)+"</option>";
		$("#mergeKeyPush").append(condHTML);
		merge = merge.substring(pos+1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:"+merge+":$'>"+merge+"</option>";
	$("#mergeKeyPush").append(condHTML);
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

// 제목 클릭시(제목 내용 추가)
function goTitleMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	if($("#pushTitle").val() == "") {
		$("#pushTitle").val( $("#mergeKeyPush").val() );
	} else {
		var cursorPosition = $("#pushTitle")[0].selectionStart;
		$("#pushTitle").val( $("#pushTitle").val().substring(0,cursorPosition) + $("#mergeKeyPush").val() + $("#pushTitle").val().substring(cursorPosition) );
	}
	setPushPreview();
}

//본문 클릭시(본문 내용 추가)
function goContMerge() {
	if($("#segNoc").val() == "") {
		alert("수신자그룹을 선택하세요.");
		return;
	}
	
	addPushMessage($("#mergeKeyPush").val(), 2);
	setPushPreview();
}

function addPushMessage(addMessage, pos) {
	var element = document.getElementById("pushMessage");
	var strOriginal = element.value;
	var iStartPos = element.selectionStart;
	var iEndPos = element.selectionEnd;
	if(pos == 0 ) { //(광고) 클릭시 넣기 
		iStartPos = pos;
		iEndPos = pos;
	} else if (pos == 1) { //(광고) 클릭시 수신거부 내역 		
		iStartPos = element.value.length;
		iEndPos = iStartPos; 
	}
	var strFront = "";
	var strEnd = "";

	if(iStartPos == iEndPos) {
		strFront = strOriginal.substring(0, iStartPos);
		strEnd = strOriginal.substring(iStartPos, strOriginal.length);
	} else return;
		
	element.value = strFront + addMessage + strEnd;
}

// 파일첨부 팝업창에서 등록 클릭시
var totalFileCnt = 0;
var totalFileByte = 0;
function attachFileUpload() {
	if( totalFileCnt > 0 ) {
		alert("1개의 파일만 업로드 가능합니다. 등록된 파일 삭제 후 등록해주세요");
		return;
	}
	
	if($("#upTempAttachPath").val() == "") {
		alert("파일을 선택해주세요.");
		return;
	}
	
	// 첨부파일 용량 체크
	var fileByte = $("#fileUrl2")[0].files[0].size;
	if(totalFileByte + fileByte > 30720) {
		alert("첨부파일의 용량은 최대 30kb까지 입니다.");
		return;
	}
	
	var extCheck = "jpg,png,jpeg,bmp,gif";
	var fileName = $("#upTempAttachPath").val();
	var fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
		
	if(extCheck.toLowerCase().indexOf(fileExt.toLowerCase()) >= 0) {
		var frm = $("#attachUpload")[0];
		var frmData = new FormData(frm);
		$.ajax({
			type: "POST",
			enctype: 'multipart/form-data',
			url: '../../com/imgUploadPushPath.json',
			data: frmData,
			processData: false,
			contentType: false,
			success: function (result) {
				alert("파일 업로드가 완료되었습니다.");
				
				var s = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'],
					e = Math.floor(Math.log(fileByte) / Math.log(1024));
				var fileSizeTxt = (fileByte / Math.pow(1024, e)).toFixed(2) + " " + s[e];
				$("#fileSize").val(fileByte); 
				var attachFileHtml = "";
				attachFileHtml += '<li>';
				attachFileHtml += '<input type="hidden" name="attachNm" value="'+ result.oldFileName +'">';
				attachFileHtml += '<input type="hidden" name="attachPath" value="'+ result.newFileName +'">';
				attachFileHtml += '<span>'+ result.oldFileName +'</span>';
				/*attachFileHtml += '<em>'+ fileSizeTxt +'</em>';*/
				attachFileHtml += '<button type="button" class="btn-del" onclick="deleteAttachFile(this)"><span class="hide">삭제</span></button>';
				attachFileHtml += '</li>';
				
				totalFileCnt++;
				totalFileByte += fileByte;
				
				$("#pushAttachFileList").append(attachFileHtml);
				
				$("#popFileDelete").click();
				frm.reset();
				
				fn.fileReset('#popup_file');
				fn.popupClose('#popup_file');
				
				$('#btnAttachFile').attr('disabled','disabled');
				setPushPreview();
			},
			error: function (e) {
				alert("File Upload Error!!");
			}
		});
	} else {
		alert("등록 할 수 없는 파일 형식입니다. jpg,png,jpeg,bmp,gif만 가능합니다.");
	}
}

// 첨부파일 삭제 (업로드된 이후에 첨부파일 삭제)
function deleteAttachFile(obj) {
	totalFileCnt--;
	var attachSize = $(obj).closest("li").children("input[name='attachSize']").val();
	totalFileByte = totalFileByte - attachSize;
	$(obj).closest("li").remove();
	
	$('#btnAttachFile').removeAttr('disabled');
	setPushPreview();
}

function setPushPreview(){
	setPushGubun();
	setPushUI();

	var previewHtml = "";
	var attatchList = $('input[name="attachPath"]');
	var filePath ="";
	var imgLogUrl = document.location.origin + $("#imgAppLogo").val(); 
	
	var pushMessage = "<span class='push-desc'>" + $("#pushMessage").val() +"</span>";
	
	if ($("#chkLegalYn").prop("checked")) {
		pushMessage = "<strong class='push-tit'><span>(광고)</span>" + $("#pushTitle").val() + "</strong>" + pushMessage;
	} else {
		pushMessage = "<strong class='push-tit'>" + $("#pushTitle").val() + "</strong>" + pushMessage; 
	}
	previewHtml = pushMessage + "<span class='thumb'><img src='" + imgLogUrl + "'></span>"; 
	
	if(totalFileCnt > 0 ) { 
		var imgDomainPath = document.location.origin +  $('#imgUploadPushPath').val();
		filePath = imgDomainPath +  "/"+ attatchList[0].value; 
		previewHtml = "<span class='preview-img' id='previewImg'><img src=\'" + previewHtml +  "\' alt=''></span>";
	}
	
	if ($("#chkLegalYn").prop("checked")) {
		previewHtml +=  "<br/><span class='reject'>"+ $("#legalCf").val() +"</span>";
	}
	
	$("#contText").empty();
	$("#contText").append(previewHtml);
	 
	if(filePath.length > 0 ) {
		$("#previewImg").css({"background":"url(" + filePath + ")"});
	}
/*	$(".pushinfobox .push-area .preview .reject").each(function(index){
		var $obj = $(this).siblings(".push-desc"),
			wd = $(this).width() + 2;
		$obj.css({"width":"calc(100% - "+ wd +"px)"});
	})*/
}

function setPushGubun(){
	var pushMsgLen = $("#pushMessage").val().length;
	var lenDef = 30; 
	var lenMax = 140;
	
	if (pushMsgLen > lenMax){
		alert("문자 길이는 140자를 넘을수 없습니다");
		return;
	}
	
	if (pushMsgLen <= lenDef){
		$("#pushInfoTitle").html("단문 PUSH 발송정보");
		$("#pushGubun").val("000");
		lenMax = lenDef;
	} else {
		$("#pushInfoTitle").html("중문 PUSH 발송정보");
		$("#pushGubun").val("001");
	}
	
	if(totalFileCnt > 0) {
		$("#pushInfoTitle").html("이미지 PUSH 발송정보");
		$("#pushGubun").val("002");
	}
	
	$("#byteText").empty();
	$("#byteText").append(pushMsgLen + '/' + lenMax + ' 자');
}

function setPushUI(){
	var pushGubun = $("#pushGubun").val();
	
	if(pushGubun == "000"){
		document.getElementById('divPushinfobox').className = 'pushinfobox';
		document.getElementById('divPushPreview').className = 'preview active';
		$('#btnToggle').attr('style', "display:none;");
		
	} else if(pushGubun =="001"){
		document.getElementById('divPushinfobox').className = 'pushinfobox type-long';
		document.getElementById('divPushPreview').className = 'preview active';
		$('#btnToggle').attr('style', "display");
	} else {
		document.getElementById('divPushinfobox').className = 'pushinfobox type-img';
		document.getElementById('divPushPreview').className = 'preview active';
		$('#btnToggle').attr('style', "display");
	}
}

function goValidUrl(type){
	var url =""; 	
	if(type=="and"){
		url = $("#txtCallUri").val().trim();
	} else {
		url = $("#txtCallUriIos").val().trim();
	}
	
	if(url.length < 1) {
		alert("URL을 입력해주세요");
	}
	if(validUrl(url)){
		alert("이동경로가 유효합니다");
		if(type=="and"){
			url = $("#callUri").val(url);
		} else {
			url = $("#callUriIos").val(url);
		}
	}else {
		alert("URL이 올바르지 않습니다");
	}	
}

function validUrl(url){
	var regUrl = /(http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
	if(!regUrl.test(url)){
		return false;
	} else {
		return true;
	}
}

// 입력 폼 검사
function checkForm() {
	
	var errstr = "";
	var errflag = false;
	
	/*
	if( $("#txtCallUri").val().length > 1 || $("#txtCallUriIos").val().length > 1 ){
		if ($("#txtCallUri").val().length > 1 && $("#txtCallUriIos").val().length > 1){
			if($("#txtCallUri").val().trim() != $("#callUri").val().trim() ) {
				alert("Android항목의 등록버튼을 클릭하여 URL유효성을 확인해주세요");
				return true; 
			}
			if($("#txtCallUriIos").val().trim() != $("#callUriIos").val().trim() ) {
				alert("iOS항목의 등록버튼을 클릭하여 URL유효성을 확인해주세요");
				return true; 
			}
		} else{
			alert("이동경로 URL 등록하는 경우 Android 와 IOS URL을 모두 입력해주세요");
			return true; 
		}
	};
	*/
	
	if($("#campNo").val() == "0") {
		errstr += "[캠페인]";
		errflag = true;
	}
	if($("#segNoc").val() == "") {
		errstr += "[수신자 그룹 또는 수신번호]";
		errflag = true;
	}
	
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			errstr += "[사용자명]";
			errflag = true;
		}
	}
	
	if($("#pushName").val() == "") {
		errstr += "[PUSH명]";
		errflag = true;
	}
	
	if($("#pushTitle").val() == "") {
		errstr += "[PUSH제목]";
		errflag = true;
	}
	
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	return errflag;
}

function goPushAdd() {
	
	if(checkForm()) {
		return;
	}
	
	if($("#chkLegalYn").is(":checked")){
		$("#legalYn").val("Y");
	}else{
		$("#legalYn").val("N");
	}
	
	if($("#chkSmsYn").is(":checked")){
		$("#smsYn").val("Y");
	}else{
		$("#smsYn").val("N");
	}
	
	$("#pushInfoForm input[name='callUrlTyp']").val( $('input[name="urlType"]:checked').val());
	$("#pushInfoForm input[name='sendTyp']").val( $('input[name="chkSendCond"]:checked').val());
	$("#pushInfoForm").attr("target","iFrmMail").attr("action","./pushAdd.ums").submit();
}

function goPushCancel() {
	$("#searchForm").attr("target","").attr("action","./pushListP.ums").submit();
}

/***************************************************************************************************
* 2022.04.10 추가
* 팝업작업 처리
***************************************************************************************************/
// 캠페인명 선택 클릭시(캠페인선택 팝업)
function popCampSelect() {
	$("#popCampSearchForm input[name='searchStartDt']").val("");
	$("#popCampSearchForm input[name='searchEndDt']").val("");
	$("#popCampSearchForm input[name='searchCampNm']").val("");
	goPopCampSearch("1");
	fn.popupOpen("#popup_push_campaign");
}

// 팝업 캠페인 목록 검색
function goPopCampSearch(pageNum) {
	$("#popCampSearchForm input[name='page']").val(pageNum);
	var param = $("#popCampSearchForm").serialize();
	$.ajax({
		type : "GET",
		url : "/push/cam/pop/popCampList.ums?" + param,
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
		url : "/push/cam/pop/popCampAdd.ums?" + param,
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
		url : "/push/cam/pop/popCampUpdate.ums?" + param,
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
	
	fn.popupClose("#popup_push_campaign");
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
		url : "/push/cam/pop/popCampList.ums?" + param,
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

// 수신자그룹 선택 클릭시(수신자그룹선택 팝업)
function popSegSelect() {
	$("#fileUploadSeg")[0].reset();
	$(".fileupload .inputfile ~ button.btn").click();

	$("#popSegSearchForm input[name='searchStartDt']").val("");
	$("#popSegSearchForm input[name='searchEndDt']").val("");
	$("#popSegSearchForm input[name='searchSegNm']").val("");
	goPopSegSearch("1");
	fn.popupOpen("#popup_push_segment");
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
		segAddUrl = "/push/cam/pop/popSegAddFile.ums";
	} else {
		segAddUrl = "/push/cam/pop/popSegAddSql.ums";
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
		segUpdateUrl = "/push/cam/pop/popSegUpdateFile.ums";
	} else if(createTy == "002") {
		segUpdateUrl = "/push/cam/pop/popSegUpdateSql.ums";
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
	$("#mergeKeyPush").children().remove();
	
	var condHTML = "";
	var merge = segNoc.substring(segNoc.indexOf("|")+1);
	var pos = merge.indexOf(",");
	while(pos != -1) {
		condHTML = "<option value='$:"+merge.substring(0,pos)+":$'>"+merge.substring(0,pos)+"</option>";
		$("#mergeKeyPush").append(condHTML);
		merge = merge.substring(pos+1);
		pos = merge.indexOf(",");
	}
	condHTML = "<option value='$:"+merge+":$'>"+merge+"</option>";
	$("#mergeKeyPush").append(condHTML);
	
	fn.popupClose("#popup_push_segment");
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
	iFrmMail.location.href = "../../com/down.ums?downType=006";
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
		url : "/sys/seg/segFileMemberListP.ums?" + param,
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
	var targetService = "PUSH";
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
	$.getJSON("/sys/seg/segAdd.json?" + param, function(data) {
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
	
	var param = $("#popSegInfoFormMail").serialize();
	$.getJSON("/sys/seg/segDirectSQLTest.json?" + param, function(data) {
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
	var targetService = "PUSH";
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
	$.getJSON("/sys/seg/segAdd.json?" + param, function(data) {
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
	var targetService = "PUSH";
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
	$.getJSON("/sys/seg/segUpdate.json?" + param, function(data) {
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
	var targetService = "PUSH";
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
	$.getJSON("/push/seg/segUpdate.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수정되었습니다.");
			goPopSegList();
		} else if(data.result == "Fail") {
			alert("수정 처리중 오류가 발생하였습니다.");
		}
	});
}

function popUrlPreview() {
	var	url = $("#callUri").val().trim();
	var urlIos = $("#callUriIos").val().trim();

	if(url.length < 1 && urlIos.length < 1 ) {
		alert("URL을 입력해주세요");
	}
	
	if (url.length > 0 ){
		if(!validUrl(url)){
			alert("URL이 올바르지 않습니다");
			return;
		}
	} else{
		if (urlIos.length > 0 ){
			if(!validUrl(url)){
				alert("URL이 올바르지 않습니다");
				return; 
			}
			url = urlIos;
		}
	}

	var urlType = $('input[name="urlType"]:checked').val();
	var urlTypeNm ="" ;
	
	switch(urlType) {
		case "000" : urlTypeNm ="Weblink"; break;
		case "001" : urlTypeNm ="Applink"; break;
		case "002" : urlTypeNm ="Web Video"; break;
		default: urlTypeNm="없는 유형"; break;
	}
	
	$("#iFrmUrl").empty();
	$("#previewUrl").html(url);
	$("#previewUrlType").html(urlTypeNm);
	
	var param = $("#pushInfoForm").serialize();
	$.getJSON("./urlPreview.json?" + param, function(res) {
		if(res.result == 'Success') {
			document.getElementById("iFrmUrl").src= res.uri;
			fn.popupOpen('#popup_preview_url');
		} else {
			alert("미리보기 처리중 오류가 발생하였습니다.");
		}
	});
}