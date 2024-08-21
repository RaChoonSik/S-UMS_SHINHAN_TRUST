/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.08.17
*	설명 : 탬틀릿 등록 JavaScript
**********************************************************/

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

// 등록 버튼 클릭시
function goAdd() {
	if(checkForm()) return;
	
	//XssFilter 체크
	/*
	if (existXssFilter(oEditors.getById["ir1"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#tempVal").val( $("#ir1").val() );
	
	// 등록 처리
	var frm = $("#templateInfoForm")[0];
	var frmData = new FormData(frm);
	$.ajax({
		type: "POST",
		enctype: 'multipart/form-data',
		url: './tempAdd.json',
		data: frmData,
		processData: false,
		contentType: false,
		success: function (res) {
			if(res.result == "Success") {
				alert("등록 성공");
				document.location.href = "./tempListP.ums";
			} else {
				alert("등록 실패");
			}
		},
		error: function (e) {
			alert("등록 실패");
		}
	});
}

//입력폼 체크
function checkForm() {
	if($("#deptNo").val() == "0") {
		alert("사용자그룹은 필수입력 항목입니다.");
		$("#deptNo").focus();
		return true;
	}
	if($("#userId").val() == "") {
		alert("사용자는 필수입력 항목입니다.");
		$("#userId").focus();
		return true;
	}
	if($("#status").val() == "") {
		alert("상태는 필수입력 항목입니다.");
		$("#status").focus();
		return true;
	}
	if($("#tempNm").val() == "") {
		alert("템플릿명은 필수입력 항목입니다.");
		$("#tempNm").focus();
		return true;
	}
	
	// 입력값 Byte 체크
	if($.byteString($("#tempNm").val()) > 100) {
		alert("템플릿명은 100byte를 넘을 수 없습니다.");
		$("#tempNm").focus();
		$("#tempNm").select();
		return true;
	}
	if($.byteString($("#tempDesc").val()) > 200) {
		alert("템플릿 설명은 200byte를 넘을 수 없습니다.");
		$("#tempDesc").focus();
		$("#tempDesc").select();
		return true;
	}
	
	return false;
}

// 취소 클릭시
function goCancel() {
	document.location.href = "./tempListP.ums";
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
				$.getJSON("./tempFileView.json?tempFlPath=" + encodeURIComponent("html/" + userId + "/" + result.newFileName), function(res) {
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

// 미리보기 버튼 클릭시
function previewTemplate() {
	// 미리보기 템플릿명 설정
	$("#previewTitle").html( $("#tempNm").val() );
	
	//XssFilter 체크
	/*
	if (existXssFilter(oEditors.getById["ir1"])){
		alert("사용 할수 없는 스크립트가 포함되어 있습니다");
		return;
	}
	*/
	// 미리보기 내용 설정
	oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);
	$("#tempVal").val( $("#ir1").val() );
	$("#previewContent").html( $("#tempVal").val() );
	
	// 팝업 오픈
	fn.popupOpen('#popup_preview');
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