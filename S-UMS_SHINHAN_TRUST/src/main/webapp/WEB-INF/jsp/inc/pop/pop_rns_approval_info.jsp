<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.10.19
	*	설명 :RNS 발송결재라인 등록정보 팝업
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_rns_approval_info" class="poplayer poppaymentinfo">
	<div class="inner small">
		<header>
			<h2>결재라인 정보</h2>
		</header>
		<div class="popcont" id="popRnsApprovalInfo">
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_rns_approval_info');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<style type="text/css">
	
</style>