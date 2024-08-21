<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.26
	*	설명 : 부서 검색 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<script type="text/javascript" src="<c:url value='/js/aes.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/sha256.js'/>"></script>

<script type="text/javascript">
function popSavePassword(){
		
	if( popCheckPassword()) {  
		$("#popPasswordMessage").text("");
		var encCurPwd = CryptoJS.AES.encrypt($("#popCurPwd").val(), "!END#ERSUMS");
		var encUserPwd = CryptoJS.AES.encrypt($("#popUserPwd").val(), "!END#ERSUMS");
		
		$("#eCurPwd").val(encCurPwd);
		$("#eUserPwd").val(encUserPwd);
		
		var param = $("#popUserPwdForm").serialize();
		 
		$.ajax({
			type : "POST",
			url : "/sys/acc/userUpdatePasswordPop.json?" + param,
			dataType : "json",
			success : function(data){
				if(data.result == "Success") {
					alert("수정이 완료되었습니다"); 
					$("#popPasswordMessage").text("");
					$("#popCurPwd").val("");
					$("#popUserPwd").val("");
					$("#popUserPwdChk").val("");
					$("#popDivEditPw").css("display", "none"); 
				} else {
					alert("현재 비밀번호가 입력하신 비밀 번호와 맞지 않습니다 현재 비밀번호를 확인해주세요");
					
					$("#popCurPwd").focus();
					$("#popCurPwd").select();
				}
			},
			error: function (e) {
				alert("비밀번호 변경 처리에 오류가 발생했습니다");
			}
		});
	}
}

function popCheckPassword(){

	var pw = $("#popUserPwd").val();
	var chkPw = $("#popUserPwdChk").val()
	$("#popPasswordMessage").text("");
	
	if (pw != chkPw) {
		$("#popPasswordMessage").text("*비밀번호가 일치하지 않습니다"); 
		$("#popUserPwdChk").focus();
		$("#popUserPwdChk").select();
		return false;
	}
	
	var num = pw.search(/[0-9]/g);
	var eng = pw.search(/[a-z]/ig);
	var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	
	if(pw.length < 8 || pw.length > 20){
		$("#popPasswordMessage").text("*8자리 ~ 20자리 이내로 입력해주세요."); 
		$("#popUserPwd").focus();
		$("#popUserPwd").select();
		return false;
	}else if(pw.search(/\s/) != -1){	
		$("#popPasswordMessage").text("*비밀번호는 공백 없이 입력해주세요.");
		$("#popUserPwd").focus();
		$("#popUserPwd").select();
		
		return false;
	}else if( (num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0) ){		
		$("#popPasswordMessage").text("*영문,숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.");
		$("#popUserPwd").focus();
		$("#popUserPwd").select();
		
		return false;
	}else {
		return true;
	} 
}
</script>

<div id="popup_user_info" class="poplayer popedituserinfo"><!-- id값 수정 가능 -->
	<div class="inner">
		<header>
			<h2>개인 정보수정</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<form id="popUserPwdForm" name="popUserPwdForm" method="post">
					<input type="hidden" id="eCurPwd" name="curPwd" value="">
					<input type="hidden" id="eUserPwd"  name="userPwd" value="">
				</form>
				<form id="popUserInfoForm" name="popUserInfoForm" method="post">
					<input type="hidden" name="popUserId" id="popUserId" value="<c:out value='${NEO_USER_ID}'/>">
					<input type="hidden" name="popTzTerm" id="popTzTerm" value="">
					<input type="hidden" name="popDeptNo" id="popDeptNo" value="0">

					
					<!-- 기본정보// -->
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">기본정보</h3>
							<span class="required">*필수입력 항목</span>
						</div>
						
						<div class="list-area">
							<ul>
								<li>
									<label>ID</label>
									<div class="list-item">
										<p class="inline-txt line-height40"><c:out value='${NEO_USER_ID}'/></p>
									</div>
								</li>
								<li>
									<label class="required">사용자명</label>
									<div class="list-item">
										<input type="text" id="popUserNm" name="popUserNm" value="<c:out value='${NEO_USER_NM}'/>">
									</div>
								</li>
								<li>
									<label>부서</label>
									<div class="list-item">
										<p class="inline-txt line-height40"><c:out value='${NEO_ORG_NM}'/></p>
									</div>
								</li>
								<li>
									<label class="required">연락처</label>
									<div class="list-item">
										<input type="text" id="popUserTel" name="popUserTel" value="<crypto:decrypt colNm='USER_TEL' data='${NEO_USER_TEL}'/>">
									</div>
								</li>
								<li>
									<label class="required">이메일</label>
									<div class="list-item">
										<input type="text" id="popUserEm" name="popUserEm" value="<crypto:decrypt colNm='USER_EM' data='${NEO_USER_EM}'/>">
									</div>
								</li>
								<li>
									<label class="required">발송자명</label>
									<div class="list-item">
										<input type="text" id="popMailFromNm" name="popMailFromNm" value="<c:out value='${NEO_MAIL_FROM_NM}'/>">
									</div>
								</li>
								<li>
									<label class="required">발송자이메일</label>
									<div class="list-item">
										<input type="text" id="popMailFromEm" name="popMailFromEm" value="<crypto:decrypt colNm='MAIL_FROM_EM' data='${NEO_MAIL_FROM_EM}'/>">
										<%-- <input type="text" id="popMailFromEm" name="popMailFromEm" value="<crypto:decrypt colNm='MAIL_FROM_EM' data='${NEO_REPLY_TO_EM}'/>"> --%>
										
									</div>
								</li>
								<li>
									<label class="required">회신 이메일</label>
									<div class="list-item">
										<input type="text" id="popReplyToEm" name="popReplyToEm" value="<crypto:decrypt colNm='REPLY_TO_EM' data='${NEO_REPLY_TO_EM}'/>">
									</div>
								</li>
								<li>
									<label class="required">리턴 이메일</label>
									<div class="list-item">
										<input type="text" id="popReturnEm" name="popReturnEm" value="<crypto:decrypt colNm='RETURN_EM' data='${NEO_RETURN_EM}'/>">
									</div>
								</li>
								<li>
									<label class="required">메일문자셋</label>
									<div class="list-item">
										<div class="select">
											<select id="popCharset" name="popCharset" title="옵션 선택">
											<option value="0">선택</option>
											</select>
										</div>
									</div>
								</li>
								<li>
									<label class="required">타임존</label>
									<div class="list-item">
										<div class="select">
											<select id="popTzCd" name="popTzCd" title="옵션 선택">
												<option value="0">선택</option>
											</select>
										</div>
									</div>
								</li>
								<li>
									<label class="required">UI 언어권</label>
									<div class="list-item">
										<div class="select">
											<select  id="popUilang" name="popUilang" title="옵션 선택">
												<option value="0">선택</option>
											</select>
										</div>
									</div>
								</li>
							</ul>
						</div>
					</div>
					<!-- //기본정보 -->

					<!-- 비밀번호 변경// -->
					<div class="graybox editpw">
						<div class="title-area">
							<h3 class="h3-title">비밀번호 변경</h3>
							<button type="button"><span class="hidden">여닫기버튼</span></button>
						</div>
						
						<div class="list-area" id="popDivEditPw" style="display:none;">
							<ul>
								<li class="col-full">
									<label>현재 비밀번호</label>
									<div class="list-item">
										<div class="pw-area fl">
											<input type="password"  id="popCurPwd" name="popCurPwd" placeholder="비밀번호를 입력해주세요." style="width:248px;padding-right:38px;">
											<button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button>
										</div>
										<p class="inline-txt color-red">*영문자, 숫자, 특수문자를 포함한 8자리 이상 설정</p>
									</div>
								</li>
								<li class="col-full">
									<label>신규 비밀번호</label>
									<div class="list-item">
										<div class="pw-area fl">
											<input type="password"  id="popUserPwd" name="popUserPwd" placeholder="비밀번호를 입력해주세요." style="width:248px;padding-right:38px;">
											<button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button>
										</div>																			
										<p class="inline-txt color-red">*영문자, 숫자, 특수문자를 포함한 8자리 이상 설정</p>
									</div>
								</li>
								<li class="col-full">
									<label>비밀번호 확인</label>
									<div class="list-item">
										<div class="pw-area fl">
											<input type="password"  id="popUserPwdChk"  placeholder="비밀번호를 한번 더 입력해주세요." style="width:248px;padding-right:38px;">
											<button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button>
										</div>
										<button type="button" class="btn fullblue" onclick="popSavePassword()" style="margin-right:10px;">변경</button>
										<p class="inline-txt color-red" id="popPasswordMessage"></p>
										
									</div>
								</li>
							</ul>
						</div>
					</div>
					<!-- //비밀번호 변경 -->

					<!-- 버튼// -->
					<div class="btn-wrap">
						<button type="button" class="btn big fullblue" onclick="popUpdate();">저장</button>
					</div>
					<!-- //버튼 -->
				</form>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_user_info');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>