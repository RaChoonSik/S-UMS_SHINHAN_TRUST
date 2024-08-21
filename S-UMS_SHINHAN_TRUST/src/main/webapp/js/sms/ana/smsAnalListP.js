/**********************************************************
*	작성자 : 이문용
*	작성일시 : 2022.03.16
*	설명 : 문자 발송목록 JavaScript
**********************************************************/
$(document).ready(function() {
	var timeGap = getTimeGap();
	$("#timeGap").text(timeGap);
});

//소요시간계산
function getTimeGap(){
	
	var time1 = new Date($("#startTime").text());
	console.log("startTime: " + time1 );
	var time2 = new Date($("#endTime").text());
	console.log("endTime: " + time2 );
	var timeGap = time2 - time1;
	
	var hours =  Math.floor((timeGap % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
	var min = Math.floor((timeGap % (1000 * 60 * 60)) / (1000 * 60));
	var sec = Math.floor((timeGap % (1000 * 60)) / 1000);

	if (hours   < 10) {hours   = "0" + hours;}
	if (min < 10) {min = "0" + min;}
	if (sec < 10) {sec = "0" + sec;}	

	return hours + ":" + min+ ":" + sec;
}

// 목록에서 메일명 클릭시 팝업창
function goSmsSendList(msgid, keygen, rsltCd) { 
	$("#popSmsSendResultSearchForm input[name='sendTelNo']").val("");
	$("#popSmsSendResultSearchForm input[name='rsltCd']").val(rsltCd);
	$("#popSmsSendResultSearchForm input[name='msgid']").val(msgid);
	$("#popSmsSendResultSearchForm input[name='keygen']").val(keygen);
	
	goSmsSendReultList();
	fn.popupOpen('#popup_smsSendResultList');
}


function goSmsPreview() {
	// 미리보기 관련 설정 초기화
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
	
	var	smsMessage = $("#smsMessage").val()
	
	previewHtml += smsMessage + '</p>';

	var messageByte = byteCheck(smsMessage);
	previewHtml += '<span class="byte">' + messageByte + '/2000byte</span>';
	previewHtml += '</div>';

	$("#divPopPhonePreview").empty();
	$("#divPopPhonePreview").append(previewHtml);
	fn.popupOpen('#popup_sms_preview');
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

//목록
function goList() {
	if ($("#searchCallGubun").val() == "C" ){
		$("#searchForm").attr("target","").attr("action","./smsSendListP.ums").submit();
	} else {
		$("#searchForm").attr("target","").attr("action","./smsListP.ums").submit();	
	}
	
}

