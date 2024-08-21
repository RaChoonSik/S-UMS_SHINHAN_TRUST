<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.21
	*	설명 : 초기화된 비밀번호 사용자 비밀번호 변경
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

 
	<div id="popup_user_editpassword" class="poplayer popeditpw"><!-- id값 수정 가능 -->
		<input type="hidden" id="needChange" value="${needChange}">
		<input type="hidden" id="needUserId" value="${needUserId}">
		<div class="inner small">
			<header>
				<h2>비밀번호 변경</h2>
			</header>
			<form id="popUserInitPwdForm" name="popUserInitPwdForm" method="post">
				<input type="hidden" id="popUserEditPwdUserId" name="userId" value="">
				<input type="hidden" id="popUserEditPwdPassword" name="userPwd" value="">
				<input type="hidden" id="popUserEditPwdPasswordInit" name="pwInitYn" value="">
				<div class="popcont">
					<div class="cont">
						<!-- 비밀번호 변경// -->
						<ul>
							<li>
								<label>신규 비밀번호</label>
								<div>
									<div class="pw-area">
										<input type="password" id="popUserEditPwd" placeholder="비밀번호를 입력해주세요.">
										<!-- <button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button> -->
									</div>
									<p class="inline-txt color-red">*영문자, 숫자, 특수문자를 포함한 8자리 이상 설정</p>
								</div>
							</li>
							<li>
								<label>비밀번호 확인</label>
								<div>
									<div class="pw-area">
										<input type="password" id="popUserEditPwdChk" placeholder="비밀번호를 한번 더 입력해주세요.">
										<!-- <button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button> -->
									</div>
									<p class="inline-txt color-red" id="popUserEditPasswordMessage"></p> 
								</div>
							</li>
						</ul>
						<!-- //비밀번호 변경 -->
		
						<!-- 버튼// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="popSaveInitPasswordChange()">저장</button>
						</div>
						<!-- //버튼 -->
					</div>
				</div>
			</form>
			<button type="button" class="btn_popclose" onclick="popCloseUserEditPassword();"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background"></span>
	</div>