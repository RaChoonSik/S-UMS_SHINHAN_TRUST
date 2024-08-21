<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.15
	*	설명 : RNS 발송결재 상태 팝업
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_rns_approval_state" class="poplayer popapproval">
	<div class="inner">
		<header>
			<h2>결재 상태</h2>
		</header>
		<div class="popcont" id="popRnsApprovalState">
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_rns_approval_state');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
 