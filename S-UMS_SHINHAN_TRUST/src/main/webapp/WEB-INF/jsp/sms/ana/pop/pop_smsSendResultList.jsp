<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.03.25
	*	설명 : 발송건수 성공 실패 검색
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_smsSendResultList" class="poplayer popsendsuccess">
	<div class="inner">
		<header>
			<h2>발송건수 분석</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<!-- 조회// -->
				<div class="graybox">
				<form id="popSmsSendResultSearchForm" name="popSmsSendResultSearchForm" method="post">
					<input type="hidden" id="popRsltCd"    name="rsltCd">
					<input type="hidden" id="popSmsSendYn" name="smsSendYn">
					<input type="hidden" id="popMsgid"     name="msgid">
					<input type="hidden" id="popKeygen"    name="keygen">
					<input type="hidden"                   name="gubun">
					
					<div class="title-area">
						<h3 class="h3-title">조회</h3>
						<span class="required">*검색하고자 하는 연락처의 전체 번호를 입력해야 검색됩니다</span>
					</div>
					
					<div class="list-area">
						<ul>
							<li class="col-full">
								<label>연락처</label>
								<div class="list-item">
									<input type="text" name="phone" placeholder="연락처를 입력해주세요." style="width:470px;">
									<button type="button" class="btn fullgreen mgl10" style="width:68px;" onclick="goSmsSendReultList()">검색</button>
								</div>
							</li>
						</ul>
					</div>
				</form>
				</div>
				<!-- //조회 -->
				<div id="divSmsSendResultList" style="margin-top:30px;"></div>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_smsSendResultList');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<!-- //팝업 -->
