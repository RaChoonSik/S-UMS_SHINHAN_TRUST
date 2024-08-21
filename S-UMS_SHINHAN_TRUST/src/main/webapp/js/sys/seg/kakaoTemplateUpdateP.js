/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2022.03.20
*	설명 : 카카오 알림톡 템플릿 수정
**********************************************************/
$(document).ready(function() {
	
	getApiTemplateInfo();
	
	$("#tempContent").on("keyup", function() {
		getApiTemplateInfo();
	});
});

function getApiTemplateInfo() {
	var apiTempCd= $("#apiTemplateList").val();
	var orgTempCd= $("#orgApiTempCd").val();
	
	if (apiTempCd == "") {
		$("#apiMergeItem").val("");
		$("#segNo").val("0");
		setMergeFunc();
	} else {
		var param = "/sys/seg/getSmsTemplateInfo.json?tempCd=" + apiTempCd;
		$.getJSON(param, function(data) {
			if(data.result == "Success") {
				$("#apiMergeItem").val(data.mergeItem);
				$("#segNo").val(data.segNo);
				setMergeFunc();
				if (apiTempCd == orgTempCd) {
					setTemplateMergeMappFunc();
				}
			}
		});
	}
}

// 수정 클릭시
function goKakaoTemplateUpdate() {
	if(checkForm() == true) return;
	
	if($('#chkWeblinkYn').is(':checked')){
		$("#kakaoTemplateInfoForm input[name='weblinkYn']").val("Y");
	} else{
		$("#kakaoTemplateInfoForm input[name='weblinkYn']").val("N");
	}
	
	var size = document.getElementsByName("merge").length;
	var valueArray = new Array();
	var apiMergeCols = "";
	if (size > 0 ) {
		for(var i = 0; i < size; i++){
			if(document.getElementsByName("merge")[i].value != ""){
				var apiMerge = document.getElementsByName("merge")[i].value;
				valueArray.push(apiMerge);
			}
		}
		
		if(valueArray.length > 0) {
			for (var i = 0 ; i < valueArray.length ; i ++){
				apiMergeCols += valueArray[i] +",";
			}
		}
	}
	
	$("#kakaoTemplateInfoForm input[name='apiMergeCols']").val(apiMergeCols);
	$("#kakaoTemplateInfoForm").attr("target","iFrmKakaoTemplate").attr("action","./kakaoTemplateUpdate.ums").submit();
}

// 목록 클릭시(리스트로 이동)
function goKakaoTemplateListP() {
	$("#kakaoTemplateInfoForm").attr("target","").attr("action","./kakaoTemplateListP.ums").submit();
}

// 입력 폼 검사
function checkForm() {
	var tempContent = $("#tempContent").val();
	var tempNm = $("#tempNm").val();
	var result = false;

	if($.trim($("#deptNo").val()) == "0") {
		alert("사용자그룹은 필수입력 항목입니다.");
		$("#deptNo").focus();
		return true;
	}
	
	if($.trim($("#userId").val()) == "") {
		alert("사용자는 필수입력 항목입니다.");
		$("#userId").focus();
		return true;
	}
	
	if($.trim($("#status").val()) == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#status").focus();
		return true;
	}
	 
	if($.trim(tempNm) == "") {
		alert("템플릿명은 필수입력 항목입니다.");
		$("#tempNm").focus();
		return true;
	}
	
	if($.byteString(tempNm) > 128) {
		alert("템플릿명은 128byte를 넘을 수 없습니다.");
		$("#tempNm").focus();
		return true;
	}
	
	if($.trim(tempContent) == "") {
		alert("템플릿 내용은 필수입력 항목입니다.");
		$("#tempContent").focus();
		return true;
	}
	
	if( checkMergeFunc() == true) {
		$("#tempContent").focus();
		return true;
	}
	
	return result;
}

//머지 함수 및 글자수 체크
function checkMergeFunc() {
	var pattern = /\n/g; 
	var regExpFull = /[^#{\}]+(?=})/g;
	var regExpPre = /#{/g;
	let msgRegFullResult =  $("#tempContent").val().match(regExpFull);
	let msgRegPreResult =   $("#tempContent").val().match(regExpPre);
	var mergeItems = "";
	
	if ((msgRegFullResult != null) && (msgRegPreResult != null) ){
		if (msgRegFullResult.length == msgRegPreResult.length ){
			for(var i=0; i < msgRegFullResult.length; i++) {
				var mergeItem = msgRegFullResult[i];
				if(mergeItem.match(pattern) != null) {
					alert("함수 템플릿은 개행이 불가합니다.");
					$("#tempContent").focus();
					return true;
				}
			}
		} else {
			alert("함수 포멧이 맞지 않습니다.");
			$("#tempContent").focus();
			return true;
		}
	} 
	
	var msgRegResult = removeDuplicatesArray(msgRegFullResult);
	
	for(var i=0; i < msgRegResult.length; i++) {
		var mergeItem = msgRegResult[i];
		mergeItems += "#{" + mergeItem + "},";
	}
	$("#mergyItem").val(mergeItems);
	return false;
}

//머지 함수 및 글자수 설정 
function setMergeFunc() {
	$("#mergeItems").empty();
	
	var pattern = /\n/g; 
	var regExpFull = /[^#{\}]+(?=})/g;
	var regExpPre = /#{/g;
	let msgRegFullResult =  $("#tempContent").val().match(regExpFull);
	let msgRegPreResult =   $("#tempContent").val().match(regExpPre);
	var templateMergeItem = "";
	let mergeResult = new Array();
	
	if ((msgRegFullResult != null) && (msgRegPreResult != null) ){
		if (msgRegFullResult.length == msgRegPreResult.length ){
			for(var i=0; i < msgRegFullResult.length; i++) {
				var mergeItem = msgRegFullResult[i];
				if(mergeItem.match(pattern) == null) {
					mergeResult.push(mergeItem);
				}
			}
			if (mergeResult.length > 0) {
				var msgRegResultUnique = removeDuplicatesArray(mergeResult);
				
				if(msgRegResultUnique.length > 0) {
					var htmlContent = ""
					for(var i=0; i < msgRegResultUnique.length; i++) {
						var j = i + 1;
						htmlContent += "<tr>";
						htmlContent += "<td>" + j + "</td>";
						htmlContent += "<td>" + msgRegResultUnique[i]+ "</td>";
						htmlContent += "<td><div class='select'>";
						htmlContent += "<select name='merge' title='옵션선택' onchange='setMerge();'>";
						htmlContent += "<option value=''>선택</option>";
						if ( $("#apiMergeItem").val() != "" ){
							var apiTemplateMergeItem =  $("#apiMergeItem").val().split(',');
							for(var k=0; k < apiTemplateMergeItem.length; k++) {
								templateMergeItem =  apiTemplateMergeItem[k].replace("$:", "").replace(":$", "");
								htmlContent += "<option value=" + "#{" +  msgRegResultUnique[i] + "}" + "|" + apiTemplateMergeItem[k] + ">" + templateMergeItem + "</option>";
								//htmlContent += "<option value=" + apiTemplateMergeItem[k] + "|" + "#{" +  msgRegResultUnique[i] + "}>" + templateMergeItem + "</option>";
							}
						}
						htmlContent += "</select></div></td></tr>";
					}
					$("#mergeItems").append(htmlContent);
					
					for(var i=0; i < msgRegResultUnique.length; i++) {
						var mergeItem = msgRegResultUnique[i];
						mergeItems += "#{" + mergeItem + "},";
					}
					$("#mergyItemDesc").val(mergeItems);
				} 
			}
		}
	}
}

//머지 함수 및 글자수 설정 
function setTemplateMergeMappFunc() {
	
	var templateMergeCol = $('#apiTemplateMergeList').find('option').map(function() {
	      return $(this).val();
	}).get()
	
	var mergeSize = document.getElementsByName("merge").length;
	if( templateMergeCol.length > 0 &&  mergeSize > 0 ){
		for (var i = 0 ; i < templateMergeCol.length ; i ++){
			var templateMergeString = templateMergeCol[i];
			for(var j = 0; j < mergeSize; j++){
				const mergeCol = document.getElementsByName("merge")[j]
				const mergeLen = mergeCol.options.length;
				for (let m=0; m<mergeLen; m++){
					if(mergeCol.options[m].value == templateMergeString){
						mergeCol.options[m].selected = true;
					}
				}
			}
		}
	}
}
// 머지함수 체인지시에 Api템플릿내용 변동처리
function setMerge(){
	
	var valueArray = new Array();
	
	var size = document.getElementsByName("merge").length;
	for(var i = 0; i < size; i++){
		if(document.getElementsByName("merge")[i].value != ""){
			var apiKakaoMerge = document.getElementsByName("merge")[i].value;
			valueArray.push(apiKakaoMerge);
		}
	}
	
	var apiTemplateMessage = $("#tempContent").val();

	if(valueArray.length > 0) {
		for (var i = 0 ; i < valueArray.length ; i ++){
			var valueArrayString = valueArray[i];
			var apiKakaoTemplateItem =  valueArrayString.substring(0, valueArrayString.indexOf("|"));
			var mergeItem =valueArrayString.substring(valueArrayString.indexOf("|") + 1);
			apiTemplateMessage =replaceKakaoMergeMessage (apiTemplateMessage, apiKakaoTemplateItem, mergeItem);
		}
		$("#tempMappContent").val(apiTemplateMessage);
	}
}

function replaceKakaoMergeMessage(target, kakaoTemplateItem, mergeItem){
	var result = "";

	result = target.replaceAll(kakaoTemplateItem,mergeItem );
	return result;
}

function checkByte(obj, maxByte) {
	var vowelRegex = /\#\{.+?\}/g;
	var strValue = $.trim(obj.value).replace(/(\r\n|\n|\r)/gm, "");
	var result = strValue.match(vowelRegex);
	
	if(result){
		for (var i = 0; i < result.length; i++) {
			strValue = strValue.replace(result[i],"");
		}
	}
	
	var strLen = strValue.length;
	var totalByte = 0;
	var len = 0;
	var oneChar = ""; 
	
	for (var i = 0; i < strLen; i++) {
		oneChar = strValue.charAt(i);
		if (escape(oneChar).length > 4) {
			totalByte += 2;
		} else {
			totalByte++;
		}
		
		// 입력한 문자 길이보다 넘치면 잘라내기 위해 저장
		if (totalByte <= maxByte) {
			len = i + 1;
		}
	}
	// 넘어가는 글자는 자른다.
	if (totalByte > maxByte) {
		alert(maxByte + "바이트를 초과 입력 할 수 없습니다.");
		obj.value  = strValue.substr(0, len);
	}
}

function removeDuplicatesArray(arr) {
	var tempArr = [];
	if(!isNull(arr)) {
		for (var i = 0; i < arr.length; i++) {
			if (tempArr.length == 0) {
				tempArr.push(arr[i]);
			} else {
				var duplicatesFlag = true;
				for (var j = 0; j < tempArr.length; j++) {
					if (tempArr[j] == arr[i]) {
						duplicatesFlag = false;
						break;
					}
				}
				if (duplicatesFlag) {
					tempArr.push(arr[i]);
				}
			}
		}
	}
	return tempArr;
}

function isNull(v) {
	return (v === undefined || v === null) ? true : false;
}