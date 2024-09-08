/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.16  
*	설명 : 상세로그 JavaScript
**********************************************************/

$(document).ready(function() {
	// 화면 로딩시 검색 실행
	goSearch("1");
	
	$(document).on("click", "input[name='isInterfaceRdo']", function(){
		$("#searchIsInterface").val(this.value);
	});
	
//	isInterfaceRdo
	
});

// 검색 버튼 클릭
function goSearch(pageNo) {
	if($("#searchStartDt").val() > $("#searchEndDt")) {
		alert("검색 시 시작일은 종료일보다 클 수 없습니다.");
		return;
	}
	
	var endDt = $("#searchEndDt").val().split('.');
	var startDt = $("#searchStartDt").val().split('.');
	var endDate = new Date(endDt[0], endDt[1], endDt[2]);
	var startDate = new Date(startDt[0], startDt[1], startDt[2]);
	
	var diff = endDate - startDate;
	var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
	if ( parseInt(diff/currDay) > 31 ){
		alert("검색 기간은 1개월을 넘길수 없습니다");
		return;
	}
	
//	var checked = $("#searchForm input[name='searchGubunNm']:checked").length > 0;
//	if(!checked){
//		alert("메세지 구분을 하나 이상 선택하세요.");
//		return;
//	}else{
//		goSmsTypeList();
//	}

	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "./detailLogList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divDetailLogList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

function goSmsDetail(msgid, keygen, campNm, attatchFileList, cmid, obj){

	var gubunNm  =""; 		//메세지 타입 구분명
	var sendGubunNm  =""; 		//메세지 타입 구분명
	var sendDate =""; 		//발송일시
	var subject  =""; 		//메세지명
	var id       =""; 		//고객ID
	var name     = ""; 		//고객명
	var phone    = ""; 		//고객전화번호
	var rslt     = "";		//결과
	
	var selTr = $(obj.parentNode.parentNode);
	var sell = $(selTr.children("td"));	
	
	for(var j= 0; j < sell.length; j ++){
		var idx = $(sell[j]).index();
		var val = $(sell[j]).text();
		
		if( idx == 1 ){ //메세지유형 
			gubunNm = val;
		}else if( idx == 2 ){ //실제발송유형
			sendGubunNm  = val;
		}else if( idx == 3 ){ //발송일시
			sendDate  = val;
		}else if( idx == 4 ){ //메세지 제목
			subject  = val;
		}else if( idx == 5 ){ //고객ID
			id  = val;
		}else if( idx == 6){ //고객명
			name  = val;
		}else if( idx == 7 ){ //고객전화번호
			phone  = val;
		}else if( idx == 8 ){ //발송자ID
			exeUserId  = val;
		}else if( idx == 9 ){ //발송자명
			exeUserNm = val;
		}else if( idx == 10 ){ //발송결과
			rslt  = val;
		}
	}
	$("#popMsgid").val(msgid);
	$("#popKeygen").val(keygen);
	$("#popCmid").val(cmid);
	$("#popAttachFileList").val(attatchFileList);
	$("#popSendDate").text(sendDate);
	$("#popCampNm").text(campNm);
	$("#popGubunNm").text(gubunNm);
	$("#popSendGubunNm").text(sendGubunNm);
	$("#popSmsTitle").text(subject);
	$("#popCustId").text(id);
	$("#popCustNm").text(name);
	$("#popCustPhone").text(phone);
	$("#popRsltCd").text(rslt);
	

	fn.popupOpen('#pop_detail_sms');
}

function goPopRcodeDesc(idx){
	
	if (idx == -1) return false;
	console.log(idx);
	var keygen = $("input[name='keygenHddn']").eq(idx).val();
	var callStatusNm = $("input[name='callStatusNmHddn']").eq(idx).val();
	var msgBody = $("input[name='msgBodyHddn']").eq(idx).val();
	
	$("#popKeygen").text(keygen);
	$("#popMsgBody").text(msgBody);
	$("#popCallStatusNm").text(callStatusNm);
	
	fn.popupOpen('#pop_detail_sms');
	
}
// 초기화 버튼 클릭
function goInit() {
	$("#searchForm")[0].reset();
	$("#searchForm input[name='searchCampNm']").val("");
	$("#txtCampNm").html("선택된 캠페인이 없습니다.");
} 

// 페이징
function goPageNum(pageNum) {
	goSearch(pageNum);
}
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
	$("#searchCampNm").val(campNm);
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

function goSmsTypeList(){

	var chkArr = $("input[name='searchGubunNm[]']");
	var gubunArr = [];
	
	for (var i= 0 ; i<chkArr.length; i++ ){
		if(chkArr[i].checked == true ){
			gubunArr.push(chkArr[i].value);
		}
	}
}

function popSmsPreview() {
	// 미리보기 관련 설정 초기화
	var previewHtml = "";
	var attatchLists = $('#popAttachFileList').val();
	var filePath = "";
	var filePath2 = "";
	var filePath3 = "";

	var addStartMessage = "(광고)";
	var addEndMessage = "무료수신거부 " + $("#legalCf").val();

	var imgDomainPath = document.location.origin + $('#imgUploadPath').val();

	var imgDomainPath = document.location.origin + $('#imgUploadPath').val();
	
	//  s  를 array 로 바꿔서 넣음 됨 
	if (attatchLists.length > 0){

		var attatchList = attatchLists.split(',');
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
	
	var	smsMessage = $("#smsMessage").val()
	
	previewHtml += smsMessage + '</p>';

	var messageByte = byteCheck(smsMessage);
	previewHtml += '<span class="byte">' + messageByte + '/2000byte</span>';
	previewHtml += '</div>';

	$("#divPopPhonePreview").empty();
	$("#divPopPhonePreview").append(previewHtml);
	fn.popupOpen('#popup_sms_preview');
}

function getSmsMsg(){
	var param = $("#smsDetailForm").serialize();
	
	$.getJSON("./getSmsMessage.json?" + param , function(data) {
		if(data.result == "Success") { 
			$("#smsMessage").val(data.smsMessage);
			popSmsPreview();
		} else if(data.result == "Fail") {
			alert("오류가 발생하였습니다.")
		}
	});
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