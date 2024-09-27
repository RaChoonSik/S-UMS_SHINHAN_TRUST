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

 
	<div id="popup_user_two_factor" class="poplayer popTwoFactor"><!-- id값 수정 가능 -->
		<div class="inner small">
			<header>
				<h2>2단계 인증</h2>
			</header>
				<div class="popcont">
					<div class="cont">
						<!-- 비밀번호 변경// -->
						<ul>
							<li>
								<label class="hidden">회원정보에 등록된 전화번호 <span id="popUserTelTxt"></span>로 <br>인증번호가 발송되었습니다.</label>
								<label class="hidden" id="popTwoFactorTime"><span id="popTwoFactorTimerDisplay"></span><a href="javascript:popUserTwoFactorReSend();" class="bold">인증번호 재발송</a></label>
								<div>
									<div class="pw-area2">
										<input type="text" id="twoFactorCodeTxt" placeholder="인증번호를 입력해주세요.">
<!-- 										<button type="button" class="btn-pwshow"><span class="hidden">비밀번호 보이기버튼</span></button> -->
									</div>
									
								</div>
							</li>
						</ul>
						<!-- //비밀번호 변경 -->
		
						<!-- 버튼// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onclick="popUserTwoFactorConfirm()">확인</button>
						</div>
						<!-- //버튼 -->
					</div>
				</div>
			<button type="button" class="btn_popclose" onclick="popCloseUserTwoFactor();"><span class="hidden">팝업닫기</span></button>
		</div>
		<span class="poplayer-background"></span>
	</div>