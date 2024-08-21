<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.21
	*	설명 : 업무별 메일 발송 이력
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_mail_send_hist" class="poplayer poptestsend">
	<div class="inner">
		<header>
			<h2>발송 이력</h2>
		</header>
		<div class="popcont" id="popMailSendHist">
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_mail_send_hist');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
