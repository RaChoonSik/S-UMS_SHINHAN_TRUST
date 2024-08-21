/**********************************************************
*	작성자 : 김상진
*	작성일시 : 2021.07.06
*	설명 : 공통 자바스크립트
**********************************************************/


/********************************************************
 * 쿠키값 설정 (쿠키명, 쿠키값, 기간)
 ********************************************************/
function setCookie( name, value, expiredays ) {
    var todayDate = new Date();
    todayDate.setDate( todayDate.getDate() + expiredays );
    document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}

/********************************************************
 * 쿠키값 조회
 ********************************************************/
function getCookie(name) {
    var nameOfCookie = name + "=";
    var x = 0;
    while ( x <= document.cookie.length ) {
        var y = (x+nameOfCookie.length);
        if ( document.cookie.substring( x, y ) == nameOfCookie ) {
            if ( (endOfCookie=document.cookie.indexOf( ";", y )) == -1 )
                endOfCookie = document.cookie.length;
            return unescape( document.cookie.substring( y, endOfCookie ) );
        }
        x = document.cookie.indexOf( " ", x ) + 1;
        if ( x == 0 )
            break;
    }
    return "";
}

// JDBC TYPE 확인(문자형, 숫자형)
function checkDBStrType(jdbcType) {
	var yn = true;

	switch(jdbcType) {
		case 1:		//java.sql.Type.CHAR
			yn = true;
			break;
		case 2:		//java.sql.Type.NUMERIC
			yn = false;
			break;
		case 12:	//java.sql.Type.VARCHAR
			yn = true;
			break;
		case 91:	//java.sql.Type.DATE
			yn = true;
			break;
		default:
			yn = true;
	}
	return yn;
}

/**
 * 허용하지 않을 문자를 체크한다.
 */
function containsChars(inputValue,chars) {
	for (var inx = 0; inx < inputValue.length; inx++) {
		if (chars.indexOf(inputValue.charAt(inx)) != -1)
			return true;
	}
	return false;
}

/**
 * 입력값의 앞뒤 공백을 제거해준다.
 * @param		str     스트림할 값(object.value)
 * @return      str     앞뒤 공백이 제거된 입력 스트링
 */
function trim(str) {
	return endEnc(firstEnc(str));
}
function firstEnc(str) {
	var len = str.length;
	var i = 0;

	for(i = 0; str.charAt(i) == ' '; i++);
	str = str.substring(i, len);

	return str;
}
function endEnc(str) {
	var i = 0;
	var len = str.length;

	for(i = (len - 1); (str.charAt(i) == ' '); i--);
	str = str.substring(0, i + 1);

	return str;
}


/********************************************************
 * jQuery 관련
 ********************************************************/
(function($) {
	var hangulByteLenth = 3; //한글 한글자에 대한 byte 를 지정


	/********************************************************
	 * TextArea 텍스트 입력 byte 체크
	 ********************************************************/
	$.fn.checkbyte = function(d) {
		var e = {
			indicator : $("#indicator"),
			limit : 80,
			twice : false
		};
		if (d) {
			$.extend(e, d);
		};
		return this.each(function() {
			var c = $(this);
			c.bind("keyup", function(a) {
				$.check(c, e.indicator, parseInt(e.limit), e.twice);
			});
		});
	};

	/********************************************************
	 * 최대 입력 문자열 길이 초과시 문자열 자름
	 ********************************************************/
	$.limitString = function(a, b) {
		var d = new String(a);
		var e = 0;
		for ( var i = 0; i < a.length; i++) {
			var c = escape(a.charAt(i));
			if (c.length == 1)
				e++;
			else if (c.indexOf("%u") != -1)
				e += hangulByteLenth;
			else if (c.indexOf("%") != -1)
				e += c.length / 3;
			if (e > b) {
				d = d.substring(0, i);
				break;
			}
		}
		return d;
	};

	/********************************************************
	 * 문자열 byte 체크
	 ********************************************************/
	$.byteString = function(a) {
		var b = 0;
		for ( var i = 0; i < a.length; i++) {
			var c = escape(a.charAt(i));
			if (c.length == 1)
				b++;
			else if (c.indexOf("%u") != -1)
				b += hangulByteLenth;
			else if (c.indexOf("%") != -1)
				b += c.length / 3;
		}
		return b;
	};

	/********************************************************
	 * 텍스트 입력 byte 제한
	 ********************************************************/
	$.check = function(a, b, c, d) {
		var e = $.byteString(a.val());
		if (e > c) {
			alert("내용은 " + c + "byte를 넘을 수 없습니다. 초과된 부분은 자동으로 삭제됩니다.");
			a.val($.limitString(a.val(), c));
		}
		b.html($.byteString(a.val()));
	};

	
	/********************************************************
	 * input box 문자열 길이 체크
	 ********************************************************/
	$.checkLength = function(strId, strName, minLen, maxLen) {
		var text = $.trim($('#' + strId).val());
		var textLen = $.byteString(text);

		if(minLen > 0 && textLen == 0) {
			msg = "[" + strName + "] : 입력해주십시오";
			$.showMsgBox(msg, null, strId);
			return false;
		}else if(textLen < minLen || textLen > maxLen) {
			if(minLen == maxLen) {
				msg = "[" + strName + "] : " + minLen + "Byte를 입력해주십시오\r\n\r\n (주의: 한글 1자는 " + hangulByteLenth + "Byte로 계산함.)";
				$.showMsgBox(msg, null, strId);
				return false;
			} else {
				msg = "[" + strName + "] : " + minLen + " - " + maxLen + " Byte를  입력해주십시오. <br/>(주의: 한글 1자는 " + hangulByteLenth + "Byte로 계산함.)";
				$.showMsgBox(msg, null, strId);
				return false;
			}
		} else {
			return true;
		}
	};


	//배열의 중복 문자 제거..
	//배열명.unique(); <-- 사용법
	//var a = {1,2,3,4,5,1,2};
	//a.unique();
	//결과 [1,2,3,4,5]
	Array.prototype.unique = function() {
		var a = {};
		for(var i=0; i<this.length; i++)   {
			if(typeof a[this[i]] == "undefined")
				a[this[i]] = 1;
		}

		this.length = 0;
		for(var i in a)
			this[this.length] = i;

		return this;
	};

	// 주어진 패턴 공식에서 문자만 뽑아내서 갯수를 세고,
	// 각 A,B,C는 patChar[i]형태로 배열에 저장한다.
	//function extChar2(patt) {
	String.prototype.extChar = function() {
		var charReg;
		charReg = /\d/; // 삭제할 부분 숫자
		//charReg = /[a-z|A-Z]/; // 알파벳만 인정
		//charReg = /[a-z]/; // 알파벳 소문자만 인정
		charReg = /[A-Z]/; // 알파벳 대문자만 인정

		var pattern = this; // 패턴 공식 값 DB에서 읽어올 것
		var Char = "";
		var patChar = pattern.split("");

		for(var i=0; i<patChar.length; i++) {
			if(charReg.test(patChar[i])) {
				Char += patChar[i] + "/";
			}
		}

		return Char; //최종으로 뽑아낸 문자만 리턴해준다.
	};


	$.fn.numericOnly = function(param) {
			var allowList = "";
			if(param && param.allow && param.allow.length > 0 ){
				for( var i = 0 ;i < param.allow.length;i++ ){
					if(param.allow[i] == '.'){
						allowList+= "\\.";
					}
				}
			}

			return this.each(function() {
				var c = $(this);
				var regExp = new RegExp('[^0-9'+allowList+']','g');
				c.keyup(function(a) {
					if (this.value != this.value.replace(regExp, '')) {
					       this.value = this.value.replace(regExp, '');
					    }
				});
				c.change(function(a) {
					if (this.value != this.value.replace(regExp, '')) {
					       this.value = this.value.replace(regExp, '');
					    }
				});
			});
	};

})(jQuery);










var umsContext = "";

/********************************************************
 * 수신자그룹 미리보기 팝업창 공통함수
 ********************************************************/
// 메일관리 수신자그룹 미리보기
function goSegInfoMail(sNo) {
	console.log(sNo);
	
	if($("#segNoc").length > 0) {
		if($("#segNoc").val() == "") {
			alert("수신자그룹을 선택하세요.");
			return;
		}
	}
	console.log($("#segNoc").val());

	var segNo = "";
	if(sNo == "") {
		segNo = $("#segNoc").val().substring(0, $("#segNoc").val().indexOf("|"));
	} else {
		segNo = sNo;
	}
	
	// 미리보기 관련 설정 초기화
	checkSearchReason = false;
	$("#searchReasonCd").val("");
	var initHtml = "";
	initHtml += "<div class='graybox'>";
	initHtml += "<div class='title-area'>";
	initHtml += "<h3 class='h3-title'>목록</h3>";
	initHtml += "<span class='total'>Total: <em>0</em></span>";
	initHtml += "</div>";
	initHtml += "<div class='grid-area'>";
	initHtml += "<table class='grid'>";
	initHtml += "<caption>그리드 정보</caption>";
	initHtml += "<tr>";
	initHtml += "<td colspan='6' class='no_data'>조회 사유를 등록해 주세요.</td>";
	initHtml += "</tr>";
	initHtml += "</table>";
	initHtml += "</div>";
	initHtml += "</div>";
	$("#previewMemberList").html(initHtml);
	
	$.getJSON(umsContext + "/ems/cam/getSegInfo.json?segNo=" + segNo, function(res) {
		if(res.result == 'Success') {
			// 수신자그룹정보 팝업창 전달
			$("#popSegInfoForm input[name='segNo']").val( res.segmentVO.segNo );
			$("#popSegInfoForm input[name='separatorChar']").val( res.segmentVO.separatorChar );
			$("#popSegInfoForm input[name='segFlPath']").val( res.segmentVO.segFlPath );
			$("#popSegInfoForm input[name='createTy']").val( res.segmentVO.createTy );
			$("#popSegInfoForm input[name='dbConnNo']").val( res.segmentVO.dbConnNo );
			$("#popSegInfoForm input[name='query']").val( res.segmentVO.query );
			$("#popSegInfoForm input[name='selectSql']").val( res.segmentVO.selectSql );
			$("#popSegInfoForm input[name='fromSql']").val( res.segmentVO.fromSql );
			$("#popSegInfoForm input[name='whereSql']").val( res.segmentVO.whereSql );
			$("#popSegInfoForm input[name='orderbySql']").val( res.segmentVO.orderbySql );
			$("#popSegInfoForm input[name='mergeKey']").val( res.segmentVO.mergeKey );
			$("#popSegInfoForm input[name='mergeCol']").val( res.segmentVO.mergeCol );
			
			// 정보표시
			$("#previewSegNm").html( res.segmentVO.segNm );	// 수신자그룹명
			if(res.segmentVO.createTy == "002" || res.segmentVO.createTy == "003") {
				$("#popCreateTyFile").show();
				$("#popCreateTyTool").hide();
				if(res.segmentVO.createTy == "002")	{
					$("#previewSql").html( res.segmentVO.query );
				} else {
					$("#previewSql").html( res.segmentVO.segFlPath );
				}
			} else {
				$("#popCreateTyFile").hide();
				$("#popCreateTyTool").show();
				
				var sql = "SELECT " + res.segmentVO.selectSql + " FROM " + res.segmentVO.fromSql;
				if(res.segmentVO.whereSql != null && res.segmentVO.whereSql != "") {
					sql += " WHERE " + res.segmentVO.whereSql;
				}
				if(res.segmentVO.orderbySql != null && res.segmentVO.orderbySql != "") {
					sql += " ORDER BY " + res.segmentVO.orderbySql;
				}
				$("#previewSql").html( sql );
				
				// 회원정보 초기화 후 설정
				$("#mergeKey").children().remove();
				var mergeKey = res.segmentVO.mergeKey.split(",");
				var mergeCol = res.segmentVO.mergeCol.split(",");
				$("#previewSearch").empty();
				for(var i=0;i<mergeKey.length;i++) {
					var option = new Option(mergeKey[i], mergeCol[i]);
					$("#previewSearch").append(option);
				}
			}
			
			
		} else {
			alert("Data Error!!");
		}
	});
	fn.popupOpen('#popup_preview_seg');
}

// SMS관리 수신자그룹 미리보기
function goSegInfoSms(sNo) {
	console.log(sNo);
	
	if($("#segNoc").length > 0) {
		if($("#segNoc").val() == "") {
			alert("수신자그룹을 선택하세요.");
			return;
		}
	}
	console.log($("#segNoc").val());

	var segNo = "";
	if(sNo == "") {
		segNo = $("#segNoc").val().substring(0, $("#segNoc").val().indexOf("|"));
	} else {
		segNo = sNo;
	}
	
	// 미리보기 관련 설정 초기화
	checkSearchReason = false;
	$("#searchReasonCd").val("");
	var initHtml = "";
	initHtml += "<div class='graybox'>";
	initHtml += "<div class='title-area'>";
	initHtml += "<h3 class='h3-title'>목록</h3>";
	initHtml += "<span class='total'>Total: <em>0</em></span>";
	initHtml += "</div>";
	initHtml += "<div class='grid-area'>";
	initHtml += "<table class='grid'>";
	initHtml += "<caption>그리드 정보</caption>";
	initHtml += "<tr>";
	initHtml += "<td colspan='6' class='no_data'>조회 사유를 등록해 주세요.</td>";
	initHtml += "</tr>";
	initHtml += "</table>";
	initHtml += "</div>";
	initHtml += "</div>";
	$("#previewMemberList").html(initHtml);
	
	$.getJSON(umsContext + "/sms/cam/getSegInfo.json?segNo=" + segNo, function(res) {
		if(res.result == 'Success') {
			// 수신자그룹정보 팝업창 전달
			$("#popSegInfoForm input[name='segNo']").val( res.segmentVO.segNo );
			$("#popSegInfoForm input[name='separatorChar']").val( res.segmentVO.separatorChar );
			$("#popSegInfoForm input[name='segFlPath']").val( res.segmentVO.segFlPath );
			$("#popSegInfoForm input[name='createTy']").val( res.segmentVO.createTy );
			$("#popSegInfoForm input[name='dbConnNo']").val( res.segmentVO.dbConnNo );
			$("#popSegInfoForm input[name='query']").val( res.segmentVO.query );
			$("#popSegInfoForm input[name='selectSql']").val( res.segmentVO.selectSql );
			$("#popSegInfoForm input[name='fromSql']").val( res.segmentVO.fromSql );
			$("#popSegInfoForm input[name='whereSql']").val( res.segmentVO.whereSql );
			$("#popSegInfoForm input[name='orderbySql']").val( res.segmentVO.orderbySql );
			$("#popSegInfoForm input[name='mergeKey']").val( res.segmentVO.mergeKey );
			$("#popSegInfoForm input[name='mergeCol']").val( res.segmentVO.mergeCol );
			
			// 정보표시
			$("#previewSegNm").html( res.segmentVO.segNm );	// 수신자그룹명
			if(res.segmentVO.createTy == "002" || res.segmentVO.createTy == "003") {
				$("#popCreateTyFile").show();
				$("#popCreateTyTool").hide();
				if(res.segmentVO.createTy == "002")	{
					$("#previewSql").html( res.segmentVO.query );
				} else {
					$("#previewSql").html( res.segmentVO.segFlPath );
				}
			} else {
				$("#popCreateTyFile").hide();
				$("#popCreateTyTool").show();
				
				var sql = "SELECT " + res.segmentVO.selectSql + " FROM " + res.segmentVO.fromSql;
				if(res.segmentVO.whereSql != null && res.segmentVO.whereSql != "") {
					sql += " WHERE " + res.segmentVO.whereSql;
				}
				if(res.segmentVO.orderbySql != null && res.segmentVO.orderbySql != "") {
					sql += " ORDER BY " + res.segmentVO.orderbySql;
				}
				$("#previewSql").html( sql );
				
				// 회원정보 초기화 후 설정
				$("#mergeKey").children().remove();
				var mergeKey = res.segmentVO.mergeKey.split(",");
				var mergeCol = res.segmentVO.mergeCol.split(",");
				$("#previewSearch").empty();
				for(var i=0;i<mergeKey.length;i++) {
					var option = new Option(mergeKey[i], mergeCol[i]);
					$("#previewSearch").append(option);
				}
			}
			
			
		} else {
			alert("Data Error!!");
		}
	});
	fn.popupOpen('#popup_preview_seg');
}

// 미리보기 페이징
function goPageNumSeg(pageNum) {
	$("#popSegInfoForm input[name='page']").val(pageNum);
	var getMemberUrl = "";
	if($("#popSegInfoForm input[name='createTy']").val() == "003") {
		getMemberUrl = umsContext + "/sys/seg/segFileMemberListP.ums";
	} else {
		getMemberUrl = umsContext + "/sys/seg/segDbMemberListP.ums";
	}
	var param = $("#popSegInfoForm").serialize();
	$.ajax({
		type : "POST",
		url : getMemberUrl + "?" + param + "&checkSearchReason=" + checkSearchReason + "&searchReasonCd=" + $("#searchReasonCd").val() + "&contentPath=" + window.location.pathname,
		dataType : "html",
		success : function(pageHtml){
			checkSearchReason = true;
			$("#previewMemberList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 조회사유 등록여부 체크(목록 조회 함수 goPageNumSeg에서 값 변경함)
var checkSearchReason = false;

// 수신자그룹 조회 클릭시
function goSearchReason() {
	// 조회사유 등록 체크
	if(checkSearchReason) {
		goPageNumSeg("1");
	} else {
		fn.popupOpen('#popup_reason');
	}
}

// 조회사유 등록 클릭시
function goSearchReasonAdd() {
	if($("#searchReasonCd").val() == "") {
		alert("조회 사유를 선택해주세요.");
		return;
	} else {
		alert("조회사유가 등록되었습니다.");
		fn.popupClose('#popup_reason');
		
		goPageNumSeg("1");
	}
}

// 조회사유 취소 클릭시
function goSearchReasonCancel() {
	alert("조회 사유 등록이 취소되었습니다.");
	fn.popupClose('#popup_reason');
}






/********************************************************
 * 테스트발송 팝업창 공통함수
 ********************************************************/
// 테스트사용자 목록 조회
function getTestUserList() {
	var param = $("#testUserForm").serialize();
	$.ajax({
		type : "POST",
		url : umsContext + "/ems/cam/mailTestUserList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divTestUserList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}

// 테스트사용자 추가 클릭시
function goTestUserAdd() {
	if($("#testUserNm").val() == "") {
		alert("수신자를 입력해주세요.");
		$("#testUserNm").focus();
		return;
	}
	if($("#testUserEm").val() == "") {
		alert("이메일을 입력해주세요.");
		$("#testUserEm").focus();
		return;
	}
	
	// 등록 처리
	var param = $("#testUserForm").serialize();
	$.ajax({
		type: "POST",
		url: umsContext + "/ems/cam/mailTestUserAdd.json?" + param,
		dataType : "json",
		success: function (res) {
			if(res.result == "Success") {
				alert("등록되었습니다.");
				$("#testUserNm").val("");
				$("#testUserEm").val("");
				
				getTestUserList();
			} else {
				alert("등록 처리중 오류가 발생하였습니다.");
			}
		},
		error: function (e) {
			alert("등록 처리중 오류가 발생하였습니다.");
		}
	});
}

// 테스트사용자 목록 전체 선택
function goUserAll() {
	if($("#listForm input[name='isUserAll']").is(":checked")) {
		$("#listForm input[name='testUserNos']").prop("checked",true);
		$("#listForm input[name='testUserEm']").prop("checked",true);
	} else {
		$("#listForm input[name='testUserNos']").prop("checked",false);
		$("#listForm input[name='testUserEm']").prop("checked",false);
	}
}

// 테스트사용자 목록에서 체크박스 클릭시
function checkTestUserEm(idx) {
	if($("#listForm input[name='testUserNos']").eq(idx).is(":checked")) {
		$("#listForm input[name='testUserEm']").eq(idx).prop("checked",true);
	} else {
		$("#listForm input[name='testUserEm']").eq(idx).prop("checked",false);
	}
}

// 테스트사용자 삭제 클릭시
function goTestUserDelete() {
	var addCheck = false;
	$("#listForm input[name='testUserNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			addCheck = true;
		}
	});
	if(!addCheck) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}
	
	var param = $("#listForm").serialize();
	$.getJSON(umsContext + "/ems/cam/mailTestUserDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("삭제되었습니다.");
			// 목록 재조회;
			getTestUserList();
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	});
}

// 테스트발송 취소 클릭시
function goTestSendCancel() {
	alert("테스트 발송이 취소되었습니다.");
	fn.popupClose('#popup_testsend_user');
}

// 테스트발송 팝업창에서 테스트발송 클릭시
function goTestMailSend() {
	var addCheck = false;
	$("#listForm input[name='testUserNos']").each(function(idx,item){
		if($(item).is(":checked")) {
			addCheck = true;
		}
	});
	if(!addCheck) {
		alert("체크된 항목이 존재하지 않습니다.");
		return;
	}

	var a = confirm("테스트 메일을 발송하겠습니까?");
	if(a) {
		var param = $("#listForm").serialize();
		$.getJSON(umsContext + "/ems/cam/mailTestSend.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("테스트메일이 발송 되었습니다.");
				fn.popupClose('#popup_testsend_user');
			} else if(data.result == "Fail") {
				alert("테스트메일이 발송중 오류가 발생하였습니다.");
			}
		});
		
	} else {
		return;
	}
}




/********************************************************
 * top.jsp 메일결재알림
 ********************************************************/
function goMailApprLine(userId) {
	var apprUserId = "<input type='hidden' name='searchAprUserId' value='" + userId + "'>";
	var topNoti = "<input type='hidden' name='topNotiYn' value='Y'>";
	$("#pMenuId").val("M1003000");
	$("#menuId").val("M1003004");
	$("#runMenuForm").append(apprUserId);
	$("#runMenuForm").append(topNoti);
	$("#runMenuForm").attr("target","").attr("method","post").attr("action", umsContext + "/ems/apr/mailAprListP.ums").submit();
}

/********************************************************
 * top.jsp 사용자정보수정
 ********************************************************/
function goUserUpdate() {	
	 
	$.getJSON("/sys/acc/popUserInfo.json", function(data) {
		if (data.result == "Success"){
			var charset = data.userInfo.charset;
			var tzCd =  data.userInfo.tzCd;
			var uilang =  data.userInfo.uilang;
		
			if(data.charsetList.length > 0) {
				$("#popCharset").children("option:not(:first)").remove();
				$.each(data.charsetList, function(idx,item){
					var option = document.createElement('option');
					option.setAttribute('value', item.cd);
					option.innerText = item.cdNm;					
					if (item.cd == charset){
						option.setAttribute('selected', '');
					}
					$("#popCharset").append(option);
				});
			}
			if(data.tzCdList.length > 0) {
				$("#popTzCd").children("option:not(:first)").remove();
				$.each(data.tzCdList, function(idx,item){
					var option = document.createElement('option');
					option.setAttribute('value', item.cd);
					option.innerText = item.cdNm;
					if (item.cd == tzCd){
						option.setAttribute('selected', '');
						$("#popTzCd").val(item.cdNm);
					}
					$("#popTzCd").append(option);
				});
			}
			if(data.uilangList.length > 0) {
				$("#popUilang").children("option:not(:first)").remove();
				$.each(data.uilangList, function(idx,item){
					var option = document.createElement('option');
					option.setAttribute('value', item.cd);
					option.innerText = item.cdNm;
					if (item.cd == uilang){
						option.setAttribute('selected', '');
					}
					$("#popUilang").append(option);
				});
			}
			fn.popupOpen("#popup_user_info");
		} else {
			alert(data.resultMessage);
		}
	}); 
}

function popUpdate() {	

	if(popChckForm()) {
		return;
	}
	
	if(popChckData()) {
		return;
	}

	var tzTerm = document.querySelector("#popTzCd").innerText;;
	
	$("#popTzTerm").val(tzTerm);
	
	var param = $("#popUserInfoForm").serialize();
	 
	console.log(param);
	$.getJSON("/sys/acc/popUserUpdate.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수정이 완료되었습니다");
		 	fn.popupClose('#popup_user_info');
		} else {
			alert("수정에 실패하셨습니다");
		}
	}); 
} 

// 입력 폼 검사
function popChckForm() {

	var errstr = "";
	var errflag = false;
  
	if($("#popUserNm").val() == "") {
		errstr += "[사용자명]"; 
		errflag = true;
	} 
	
	if($("#popUserEm").val() == "") {
		errstr += "[사용자이메일주소]"; 
		errflag = true;
	}
	
	if($("#popReplyToEm").val() == "") {
		errstr += "[회신이메일주소]"; 
		errflag = true;
	}
	
	if($("#popMailFromEm").val() == "") {
		errstr += "[발송자이메일]"; 
		errflag = true;
	}

	if($("#popMailFromNm").val() == "") {
		errstr += "[발송자명]"; 
		errflag = true;
	}
 
	if($('#popCharset').val() == "0") {
		errstr += "[메일문자셋]";
		errflag = true;
	}
	
	if($('#popTzCd').val() == "0") {
		errstr += "[타임존]";
		errflag = true;
	}
	
	if($('#popUilang').val() == "0") {
		errstr += "[UI언어권]";
		errflag = true;
	} 
	
	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	 
	return errflag;
}

// 입력 폼 검사
function popChckData() {

	var errstr = "";
	var errflag = false;
  
	//글자수외에 Validation 체크 추가해야함..지금 말고..좀따..힘드렁 마이 힘드렁..겔겔겔.....
	if($.byteString($("#popUserNm").val()) > 38 ) {
		alert("사용자명은 40byte를 넘을 수 없습니다.");
		$("#popUserNm").focus();
		$("#popUserNm").select();
		errflag = true;
	}
	
	if (popCheckEmail()){
		errflag = true;
	}
	
	if (popCheckTelNo()){
		errflag = true;
	}		
 
	return errflag;
}

function popCheckEmail(){
	
	var email ="";
	var errstr = "";
	var errflag = false;	
	var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
	
	email = $("#popUserEm").val()
	if(!reg_email.test(email)) {
		errstr += "[이메일]";
		errflag = true; 
	} 
	
	email = $("#popMailFromEm").val()
	if(!reg_email.test(email)) {
		errstr += "[발송자이메일]";
		errflag = true; 
	} 
		 
	email = $("#popReplyToEm").val()
	if(!reg_email.test(email)) {
		errstr += "[회신이메일]";
		errflag = true; 
	}  
	
	email = $("#popReturnEm").val()
	if(!reg_email.test(email)) {
		errstr += "[RETURN이메일]";
		errflag = true; 
	}
	
	if(errflag) {
		alert("입력하신 이메일 주소는 유효하지 않습니다 이메일주소를 확인해주세요(예:gildong@sictglobal.com).\n" + errstr);
	}	
	 
	return errflag;
} 
 
function popCheckTelNo(){
	
	var telNo ="";
	var errstr = "";
	var errflag = false;	
	var reg_telNo =/^[0-9]{6,20}$/; 
	telNo = $("#popUserTel").val()
	if(!reg_telNo.test(telNo)) {
		errstr = "[연락처]";
		errflag = true; 
	} 
	 
	if(errflag) {
		alert("연락처는 6~20자리 이하 숫자만 가능합니다\n" + errstr);
	}	
	 
	return errflag;
}

function popCloseUserEditPassword(){		
	$("#needUserId").val("");
	//$("#pUserId").val("");
	$("#pUserPwd").val("");
	$("#popUserEditPwd").val("");
	$("#popUserEditPwdChk").val("");
	fn.popupClose('#popup_user_editpassword');
}

function popCheckInitPasswordChange(){

	var pw = $("#popUserEditPwd").val();
	var chkPw = $("#popUserEditPwdChk").val()
	$("#popUserEditPasswordMessage").text("");
	
	if (pw != chkPw) {
		$("#popUserEditPasswordMessage").text("*비밀번호가 일치하지 않습니다"); 
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	}
	
	var num = pw.search(/[0-9]/g);
	var eng = pw.search(/[a-z]/ig);
	var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	
	if(pw.length < 8 || pw.length > 20){
		$("#popUserEditPasswordMessage").text("*8자리 ~ 20자리 이내로 입력해주세요."); 
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		return false;
	}else if(pw.search(/\s/) != -1){	
		$("#popUserEditPasswordMessage").text("*비밀번호는 공백 없이 입력해주세요.");
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		
		return false;
	}else if( (num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0) ){		
		$("#popUserEditPasswordMessage").text("*영문,숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.");
		$("#popUserEditPwd").focus();
		$("#popUserEditPwd").select();
		
		return false;
	}else {
		return true;
	} 
}

/********************************************************
 * 신규발송 팝업에서 바로가기 클릭
 ********************************************************/
function goNewSms() { 
 
	$.getJSON("/sms/sch/scheduleGrant.json?searchGrantMenuId=" + "M3002001", function(data) {
		if(data.result == "Success") {
			$("#pMenuId").val("M3002000");
			$("#menuId").val("M3002001");
			$("#runMenuForm").attr("target","").attr("action","/sms/cam/smsAddP.ums").submit();
		} else if(data.result == "Fail") {
			alert("문자발송 메뉴 접근 권한이 없습니다"); 
		}
	}); 
 
}

/********************************************************
 * 신규발송 팝업에서 바로가기 클릭
 ********************************************************/
function goNewMail(sendRepeat) {
	// 단기메일 등록
	if(sendRepeat == "000") {
		$.getJSON("/ems/sch/scheduleGrant.json?searchSendRepeat=" + "M1002001", function(data) {
			if(data.result == "Success") {
				$("#pMenuId").val("M1002000");
				$("#menuId").val("M1002001");
				$("#runMenuForm").attr("target","").attr("action","/ems/cam/taskAddP.ums").submit();
			} else if(data.result == "Fail") {
				alert("메일발송 메뉴 접근 권한이 없습니다"); 
			}
		});
	// 정기메일 등록	
	} else if(sendRepeat == "001") {
		$.getJSON("/ems/sch/scheduleGrant.json?searchSendRepeat=" + "M1002002", function(data) {
			if(data.result == "Success") {
				$("#pMenuId").val("M1002000");
				$("#menuId").val("M1002002");
				$("#runMenuForm").attr("target","").attr("action","/ems/cam/taskAddP.ums").submit();
			} else if(data.result == "Fail") {
				alert("정기메일 메뉴 접근 권한이 없습니다"); 
			} 
		});	 
	}
}

/********************************************************
 * PUSH => 신규발송 팝업에서 바로가기 클릭
 ********************************************************/
function goNewPush() { 
 
	$.getJSON("/sms/sch/scheduleGrant.json?searchGrantMenuId=" + "M4002001", function(data) {
		if(data.result == "Success") {
			$("#pMenuId").val("M4002000");
			$("#menuId").val("M4002001");
			$("#runMenuForm").attr("target","").attr("action","/push/cam/pushAddP.ums").submit();
		} else if(data.result == "Fail") {
			alert("PUSH 발송 메뉴 접근 권한이 없습니다"); 
		}
	}); 
 
}

/********************************************************
 * 발송결재라인 팝업창 공통함수
 ********************************************************/
// 발송결재라인 등록 클릭시
var firstPopMailApprOpen = true;
function popMailApproval() {
	if(firstPopMailApprOpen) {
		firstPopMailApprOpen = false;
		$("#hiddenApprLineList").val($("#apprUserList").html());
		fn.popupOpen("#popup_mail_approval");
	} else {
		$("#apprUserList").html($("#hiddenApprLineList").val());
		fn.popupOpen("#popup_mail_approval");
	}
}

// 발송결재라인팝업 하위 조직목록 조회
function getOrgUserList(upOrgCd) {
	$.getJSON(umsContext + "/ems/cam/getOrgUserList.json?upOrgCd=" + upOrgCd, function(data) {
		// 조직 트리 설정
		var orgHtml = "";
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			if(item.childCnt > 0) {
				orgHtml += '<button type="button" class="btn-toggle" onclick="getOrgUserList(\'' + item.orgCd + '\',\'' + item.childCnt + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (item.lvlVal + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="getOrgUserList(\'' + item.orgCd + '\',\'' + item.childCnt + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
		
		// 사용자 목록 설정
		var userHtml = "";
		$.each(data.userList, function(idx,item){
			userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
			userHtml += '<button type="button" value="'+ item.userId +'|'+ item.userNm +'|'+ item.orgCd +'|'+ item.orgKorNm +'|'+ item.positionGb +'|'+ item.jobGb +'|'+ item.jobNm +'">';
			userHtml += '<strong>' + item.userNm + '</strong>';
			userHtml += '<span>' + item.orgKorNm + '</span>';
			userHtml += '<span>' + item.jobNm + '</span>';
			userHtml += '</button>';
			userHtml += '</li>';
		});
		$("#popUserList").html(userHtml);
	});
}

// 발송결재라인팝업 사용자명 검색
function popSearchUser() {
	if($("#searchUserNm").val() == "") {
		alert("사용자명을 입력하세요.");
		$("#searchUserNm").focus();
		return;
	}
	$.getJSON(umsContext + "/ems/cam/getUserListSearch.json?searchUserNm=" + encodeURIComponent($("#searchUserNm").val()), function(data) {
		// 사용자 목록 설정
		var userHtml = "";
		$.each(data.userList, function(idx,item){
			userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
			userHtml += '<button type="button" value="'+ item.userId +'|'+ item.userNm +'|'+ item.orgCd +'|'+ item.orgKorNm +'|'+ item.positionGb +'|'+ item.jobGb +'|'+ item.jobNm +'">';
			userHtml += '<strong>' + item.userNm + '</strong>';
			userHtml += '<span>' + item.orgKorNm + '</span>';
			userHtml += '<span>' + item.jobNm + '</span>';
			userHtml += '</button>';
			userHtml += '</li>';
		});
		$("#popUserList").html(userHtml);
		$("#searchUserNm").val("");
	});
}

// 발송결재라인팝업 결재라인추가(더블클릭)
function addApprovalLineDblClick(obj) {
	var userInfo = $(obj).children("button").val();
	var user = userInfo.split("|");
	var userId = user[0];
	var userNm = user[1];
	var orgCd = user[2];
	var orgNm = user[3];
	var posGb = user[4];
	var jobGb = user[5];
	var jobNm = user[6];
	
	var validUser = true;
	var checkapprUsrId = userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb;
	$("#apprUserList input[name='apprUserId']").each(function(idx,item){
		if($(item).val() == checkapprUsrId) {
			validUser = false;
		}
	});
	
	if(!validUser) {
		alert("이미 추가된 사용자입니다.");
		return;
	}
	
	var userSize = $("#apprUserList input[name='apprUserId']").length;
	if(userSize >= 4) {
		alert("4명까지만 추가할 수 있습니다.")
		return;
	}
	
	var userListHtml = "";
	userListHtml += '<tr ondblclick="deleteApprovalLineDblClick(this);">';
	userListHtml += '<td><input type="hidden" name="apprUserId" value="'+ userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb +'">' + userNm + '</td>';
	userListHtml += '<td style="display:none;"><input type="hidden" name="apprUserNm" value="'+ userNm +' / '+ orgNm +' / '+ jobNm +'"></td>';
	userListHtml += '<td>' + orgNm + '</td>';
	userListHtml += '<td>' + jobNm + '</td>';
	userListHtml += '</tr>';
	
	$("#apprUserList").append(userListHtml);
	$(obj).remove();
}

// 발송결재라인팝업 결재라인추가
function popApprovalLineAdd() {
	if($("#popUserList li.active").length) {
		var userInfo = $("#popUserList li.active button").val();
		
		var user = userInfo.split("|");
		var userId = user[0];
		var userNm = user[1];
		var orgCd = user[2];
		var orgNm = user[3];
		var posGb = user[4];
		var jobGb = user[5];
		var jobNm = user[6];
		
		var validUser = true;
		var checkapprUsrId = userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb;
		$("#apprUserList input[name='apprUserId']").each(function(idx,item){
			if($(item).val() == checkapprUsrId) {
				validUser = false;
			}
		});
		
		if(!validUser) {
			alert("이미 추가된 사용자입니다.");
			return;
		}
		
		var userSize = $("#apprUserList input[name='apprUserId']").length;
		if(userSize >= 4) {
			alert("4명까지만 추가할 수 있습니다.")
			return;
		}
		
		var userListHtml = "";
		userListHtml += '<tr ondblclick="deleteApprovalLineDblClick(this);">';
		userListHtml += '<td><input type="hidden" name="apprUserId" value="'+ userId +'|'+ orgCd +'|'+ posGb +'|'+ jobGb +'">' + userNm + '</td>';
		userListHtml += '<td style="display:none;"><input type="hidden" name="apprUserNm" value="'+ userNm +' / '+ orgNm +' / '+ jobNm +'"></td>';
		userListHtml += '<td>' + orgNm + '</td>';
		userListHtml += '<td>' + jobNm + '</td>';
		userListHtml += '</tr>';
		
		$("#apprUserList").append(userListHtml);
		$("#popUserList li.active").remove();
	} else {
		alert("사용자를 선택하세요.");
		return;
	}
}

// 발송결재라인팝업 결재라인제거
function popApprovalLineRemove() {
	if(!$("#apprUserList tr.selected").length) {
		alert("사용자를 선택하세요.");
		return;
	}
	
	var userId = "";
	var userNm = "";
	var orgCd  = "";
	var orgNm  = "";
	var posGb  = "";
	var jobGb  = "";
	var jobNm  = "";
	var curUserId = $("#curUserId").val();
	
	$("#apprUserList tr.selected td").each(function(idx,item){
		if(idx == 0) {
			var aprUserId = $(item).find("input").val();
			var userInfo = aprUserId.split("|");
			userId = userInfo[0];
			userNm = $(item).text();
			orgCd = userInfo[1];
			posGb = userInfo[2];
			jobGb = userInfo[3];
		} else if(idx == 2) {
			orgNm = $(item).text();
		} else if(idx == 3) {
			jobNm = $(item).text();
		}
	});
	
	var checkUserInfo = true;
	$("#popUserList li button").each(function(idx,item){
		if($(item).val() == userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm) {
			checkUserInfo = false;
		}
	});
	
	if(curUserId != "ADMIN" && curUserId == userId){
		checkUserInfo = false;
	}
	
	if(checkUserInfo) {
		var userHtml = "";
		userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
		userHtml += '<button type="button" value="'+ userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm +'">';
		userHtml += '<strong>' + userNm + '</strong>';
		userHtml += '<span>' + orgNm + '</span>';
		userHtml += '<span>' + jobNm + '</span>';
		userHtml += '</button>';
		userHtml += '</li>';
		$("#popUserList").append(userHtml);
	}

	$("#apprUserList tr.selected").remove();
}

function deleteApprovalLineDblClick(obj) {
	var userId = "";
	var userNm = "";
	var orgCd  = "";
	var orgNm  = "";
	var posGb  = "";
	var jobGb  = "";
	var jobNm  = "";
	var curUserId = $("#curUserId").val();
	$(obj).find("td").each(function(idx,item){
		if(idx == 0) {
			var aprUserId = $(item).find("input").val();
			var userInfo = aprUserId.split("|");
			userId = userInfo[0];
			userNm = $(item).text();
			orgCd = userInfo[1];
			posGb = userInfo[2];
			jobGb = userInfo[3];
		} else if(idx == 2) {
			orgNm = $(item).text();
		} else if(idx == 3) {
			jobNm = $(item).text();
		}
	});

	var checkUserInfo = true;
	$("#popUserList li button").each(function(idx,item){
		if($(item).val() == userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm) {
			checkUserInfo = false;
		}
	});
	
	if(curUserId != "ADMIN" && curUserId == userId){
		checkUserInfo = false;
	}
	
	if(checkUserInfo) {
		var userHtml = "";
		userHtml += '<li ondblclick="addApprovalLineDblClick(this);">';
		userHtml += '<button type="button" value="'+ userId +'|'+ userNm +'|'+ orgCd +'|'+ orgNm +'|'+ posGb +'|'+ jobGb +'|'+ jobNm +'">';
		userHtml += '<strong>' + userNm + '</strong>';
		userHtml += '<span>' + orgNm + '</span>';
		userHtml += '<span>' + jobNm + '</span>';
		userHtml += '</button>';
		userHtml += '</li>';
		$("#popUserList").append(userHtml);
	}
	
	$(obj).remove();
}

// 발송결재라인팝업 결재라인 초기화
function popApprovalReset() {
	$("#apprUserList").empty();
}

// 발송결재라인팝업 결재라인 등록
function popApprovalLineSave() {
	
	var curUserId = $("#curUserId").val();
	var checkUserInfo = true;
	var checkDupInfo = true;
	$("#apprUserList input[name='apprUserId']").each(function(idx,item){
		var aprUserId = $(item).val();
		var userInfo = aprUserId.split("|");
		var userId = userInfo[0];
		if(curUserId != "ADMIN" && curUserId == userId){
			checkUserInfo = false;
		}
	});
	
	if (checkUserInfo == false){
		alert("결재라인에 본인을 등록할수 없습니다.");
		return;
	}
	var approvlaLineDesc = ""; 
	var approvalYn ="N";
	
	if (checkUserInfo == false){
		alert("결재라인에 본인을 등록할수 없습니다.");
		return;
	}
	if($("#apprUserList input[name='apprUserId']").length == 0) {
		$("#txtApprovalLineYn").html("발송결재라인이 등록되지 않았습니다.");
		$("#approvalYn").val("N");
	} else {
		//2021.11.01 김준희 수정 
		//$("#txtApprovalLineYn").html("발송결재라인이 설정되었습니다.");
		$("#apprUserList input[name='apprUserNm']").each(function(idx,item){
			if (idx == 0) {
				if($("#apprUserList input[name='apprUserId']").length == 1) {
					approvlaLineDesc = $(item).val();
				} else {
					approvlaLineDesc = $(item).val() + " 외 " + ( $("#apprUserList input[name='apprUserId']").length -1) + "명";
					
				}
			} 
			$("#approvalYn").val("Y");
		});
		$("#txtApprovalLineYn").html(approvlaLineDesc);		
		
	}
	$("#hiddenApprLineList").val( $("#apprUserList").html() );
	
	alert("발송결재라인이 설정되었습니다.");
	fn.popupClose('#popup_mail_approval');
}

// 발송결재라인팝업 결재라인 등록취소
function popApprovalLineCancel() {
	popApprovalReset();
	alert("발송결재라인이 등록이 취소되었습니다.");
	$("#txtApprovalLineYn").html("발송결재라인이 등록되지 않았습니다.");
	fn.popupClose('#popup_mail_approval');
}

// 첨부파일 사이즈 표시
function getFileSizeDisplay(fSize) {
	var s = ['Byte', 'KB', 'MB', 'GB', 'TB', 'PB'],
		e = Math.floor(Math.log(fSize) / Math.log(1024));
	var fileSizeTxt = (fSize / Math.pow(1024, e)).toFixed(2) + " " + s[e];
	document.write(fileSizeTxt);
}

// 첨부파일 다운로드
function goFileDown(fileNm, filePath) {
	var param = "downType=009&attachNm=" + encodeURIComponent(fileNm) + "&attachPath=" + encodeURIComponent(filePath);
	iFrmMail.location.href = umsContext + "/com/down.ums?" + param;
}

// SMS, PUSH 이미지 첨부파일 다운로드
function goImgFileDown(downType, fileNm, filePath) {
	var param = "downType=" + downType + "&attachNm=" + encodeURIComponent(fileNm) + "&attachPath=" + encodeURIComponent(filePath);
	iFrmMail.location.href = umsContext + "/com/down.ums?" + param;
}

/********************************************************
 * 사용자그룹 => 사용자 Cascading
 ********************************************************/
// 사용자그룹 선택시 사용자 목록 조회(기본)
function getUserList(deptNo) {
	$.getJSON(umsContext + "/com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

// 사용자그룹 선택시 사용자 목록 조회(검색항목)
function getUserListSearch(deptNo) {
	$.getJSON(umsContext + "/com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}

/********************************************************
 * XSS 스크립트 처리 
 ********************************************************/
function checkXssFilter(editText) {
	var exist = false;
	var filterAry =new Array('javascript', 'script', 'iframe', 'document', 'vbscript', 'applet','embed', 'object',
	 'frame','grameset', 'layer', 'bgsound','alert', 'onblur', 'onchange','onclick'
	, 'ondblclick', 'onerror', 'onfocus','onload', 'onmouse', 'onscroll','onsubmit', 'onunload');
	
	editText = editText.toLowerCase();
	
	for(var i=0; i < filterAry.length; i++){
		if (editText.search(filterAry[i]) > -1){
			exist = true;
		}
	}
	
	var regExp =/^[o]n[A-Za-z]+=/;
	if( regExp.test(editText) ) {
		exist = true;
	}
		
	return exist;
}

function cleanXssFilter(editText) {
	var retStr = editText ;
 
	var filterAry =new Array(/javascript/g, /script/g, /iframe/g, /document/g, /vbscript/g,/applet/g,/embed/g,/object/g,
	 /frame/g,/grameset/g,/layer/g,/bgsound/g,/alert/g,/onblur/g,/onchange/g,/onclick/g,
	 /ondblclick/g,/onerror/g,/onfocus/g,/onload/g,/onmouse/g,/onscroll/g,/onsubmit/g,/onunload/g);
	
	var replaceAry =new Array('x-javascript', 'x-script', 'x-iframe', 'x-document', 'x-vbscript', 'x-applet','x-embed', 'x-object',
	 'x-frame','x-grameset', 'x-layer', 'x-bgsound','x-alert', 'x-onblur', 'x-onchange','x-onclick'
	, 'x-ondblclick', 'x-onerror', 'x-onfocus','x-onload', 'x-onmouse', 'x-onscroll','x-onsubmit', 'x-onunload');

	editText = editText.toLowerCase();
	
	for(var i=0; i < filterAry.length; i++){
		editText = editText.replace(filterAry[i], replaceAry[i]);
	}
	
	var regExp =/^[o]n[A-Za-z]+=/;
	editText = editText.replace(regExp, "xssfilter");
	
	retStr= editText; 
	return retStr; 
}
 

/********************************************************
 * 부서 조회 팝업창 공통함수  
 ********************************************************/
// 부서 조회 클릭시
function popOrgSearch() {
	fn.popupOpen("#popup_org_search");
}
  
// 부서 팝업 하위 조직목록 조회
function getOrgList(upOrgCd, upOrgNm) {
	
	$("#popup_org_search input[name='selUpOrgCd']").val(upOrgCd);
	$("#popup_org_search input[name='selUpOrgNm']").val(upOrgNm);
	$("#popup_org_search input[name='selOrgCd']").val("");
	$("#popup_org_search input[name='selOrgNm']").val("");
 
	$.getJSON("/sys/acc/getOrgList.json?upOrgCd=" + upOrgCd, function(data) {
		// 조직 트리 설정
		var orgHtml = "";		
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			
			if(item.childCnt > 0) { 

				orgHtml += '<button type="button" class="btn-toggle" onclick="getOrgList(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (parseInt(item.lvlVal) + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="orgSelect(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
		 
	});
}

// 부서 선택
function orgSelect(orgCd, orgNm) { 		
	$("#popup_org_search input[name='selOrgCd']").val(orgCd);
	$("#popup_org_search input[name='selOrgNm']").val(orgNm); 
}

// 부서 선택
function popOrgSelect() { 
 	var orgCd =$("#popup_org_search input[name='selOrgCd']").val();
	var orgNm =$("#popup_org_search input[name='selOrgNm']").val();
	 
	if (orgCd == ""){
		orgCd = $("#popup_org_search input[name='selUpOrgCd']").val();
	}
	
	if (orgNm == ""){
		orgNm = $("#popup_org_search input[name='selUpOrgNm']").val();
	}
	
	$("#orgKorNm").val(orgNm);
	$("#orgCd").val(orgCd);
	fn.popupClose('#popup_org_search');
}

/********************************************************
 * 부서 조회 팝업창 공통함수 (VIEW 사용 )
 ********************************************************/
// 부서 조회 클릭시
function popOrgSearchView() {
	fn.popupOpen("#popup_org_search_view");
}
  
// 부서 팝업 하위 조직목록 조회
function getOrgListView(upOrgCd, upOrgNm) {
	
	$("#popup_org_search_view input[name='selUpOrgCd']").val(upOrgCd);
	$("#popup_org_search_view input[name='selUpOrgNm']").val(upOrgNm);
	$("#popup_org_search_view input[name='selOrgCd']").val("");
	$("#popup_org_search_view input[name='selOrgNm']").val("");
 
	$.getJSON("/sys/acc/getOrgListView.json?upOrgCd=" + upOrgCd, function(data) {
		// 조직 트리 설정
		var orgHtml = "";
		$.each(data.orgList, function(idx,item){
			orgHtml += '<li>';
			if(item.childCnt > 0) { 

				orgHtml += '<button type="button" class="btn-toggle" onclick="getOrgListView(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
				orgHtml += '<ul class="depth' + (parseInt(item.lvlVal) + 1) + '" id="' + item.orgCd + '"></ul>';
			} else {
				orgHtml += '<button type="button" onclick="orgSelectView(\'' + item.orgCd + '\',\'' + item.orgNm + '\');">' + item.orgNm + '</button>';
			}
		});
		$("#" + upOrgCd).html(orgHtml);
		 
	});
}

// 부서 선택
function orgSelectView(orgCd, orgNm) { 	
	$("#popup_org_search_view input[name='selOrgCd']").val(orgCd);
	$("#popup_org_search_view input[name='selOrgNm']").val(orgNm); 
}

// 부서 선택
function popOrgSelectView() { 
 	var orgCd =$("#popup_org_search_view input[name='selOrgCd']").val();
	var orgNm =$("#popup_org_search_view input[name='selOrgNm']").val();
	 
	if (orgCd == ""){
		orgCd = $("#popup_org_search_view input[name='selUpOrgCd']").val();
	}
	
	if (orgNm == ""){
		orgNm = $("#popup_org_search_view input[name='selUpOrgNm']").val();
	} 
	
	$("#orgKorNm").val(orgNm);
	$("#orgCd").val(orgCd);
	fn.popupClose('#popup_org_search_view');
}

/********************************************************
 * 카카오 알림톡 미리보기 팝업창 공통함수
 ********************************************************/
// 카카오 알림톡 템플릿 미리보기
function goKakaoTemplatePreview() {

	// 미리보기 관련 설정 초기화
	var initHtml = "";
	var tempNm  = $("#tempNm").val(); // 템플릿명
	var tempContent  = $("#tempContent").val(); // 템플릿 내용
		
	initHtml += "<span class='name'>카카오 채널명</span>";
	initHtml += "<div class='messagebox'>";
	initHtml += "<span class='tit'>알림톡 도착</span>";
	initHtml += "<textarea class='message' readonly>" + tempContent + "</textarea>";
	initHtml += "</div>";
	
	$("#popupTemplateNm").html(tempNm);
	$("#previewContent").html(initHtml);
	 
	fn.popupOpen('#popup_kakao_previewtemplate');
}

function goKakaoPreview() {

	// 미리보기 관련 설정 초기화
	var initHtml = "";
	var taskNm  = $("#taskNm").val(); // 템플릿명
	var smsMessage  = $("#smsMessage").val(); // 템플릿 내용
		
	initHtml += "<span class='name'>카카오 채널명</span>";
	initHtml += "<div class='messagebox'>";
	initHtml += "<span class='tit'>알림톡 도착</span>";
	initHtml += "<textarea class='message' readonly>" + smsMessage + "</textarea>";
	initHtml += "</div>";
	
	$("#popupTemplateNm").html(taskNm);
	$("#previewContent").html(initHtml);
	 
	fn.popupOpen('#popup_kakao_previewtemplate');
}

function goKakaoApiTemplatePreview() {

	// 미리보기 관련 설정 초기화
	var initHtml = "";
	var tempNm  = $("#tempNm").val(); // 템플릿명
	var tempContent  = $("#tempMappContent").val(); // 템플릿 내용
		
	initHtml += "<span class='name'>카카오 채널명</span>";
	initHtml += "<div class='messagebox'>";
	initHtml += "<span class='tit'>알림톡 도착</span>";
	initHtml += "<textarea class='message' readonly>" + tempContent + "</textarea>";
	initHtml += "</div>";
	
	$("#popupTemplateNm").html(tempNm);
	$("#previewContent").html(initHtml);
	 
	fn.popupOpen('#popup_kakao_previewtemplate');
}

/********************************************************
 * SMS 및 카카오 성공 실패 상세내역  팝업창 공통함수
 ********************************************************/
function goSmsSendReultList() {

	var param = $("#popSmsSendResultSearchForm").serialize();
	$.ajax({
		type : "POST",
		url : umsContext + "/sms/ana/pop/popSmsSendResultList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divSmsSendResultList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}
/********************************************************
 * 수신자 그룹
 ********************************************************/
function checkSegmentFileInfo(headerInfoArr, dataInfo, targetService){
	var errStr = "";
	var	errStr = checkService(headerInfoArr,targetService);

	return errStr;
}

function checkService(headerInfoArr,targetService){
	var notExist  = new Array();
	
	var serviceInfo = new Array();
		serviceInfo.push("ID");
		serviceInfo.push("NAME");
	
	if(targetService.indexOf('EMS') > -1){
		serviceInfo.push("EMAIL");
	}
	if(targetService.indexOf('SMS') > -1){
		serviceInfo.push("PHONE");
	}
	if(targetService.indexOf('PUSH') > -1){
		serviceInfo.push("DEVICE_IDENTI_NO");
		serviceInfo.push("OS_GUBUN");
	}
	
	//errStr = serviceInfo.filter(x => !headerInfoArr.includes(x));
	
	for(var i = 0; i< serviceInfo.length; i++){
		if(headerInfoArr.indexOf(serviceInfo[i]) === -1){
			notExist.push(serviceInfo[i]);
		}
	}
	
	return notExist;
}
 
 