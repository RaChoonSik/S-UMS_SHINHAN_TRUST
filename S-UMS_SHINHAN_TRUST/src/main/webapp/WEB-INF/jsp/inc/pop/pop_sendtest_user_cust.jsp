<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.23
	*	설명 : EAI 테스트발송 사용자목록 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_testsend_user_cust" class="poplayer poptestsend">
	<div class="inner">
		<header>
			<h2>테스트메일 발송 고객선택</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<!-- 발송고객정보// -->
				<div class="graybox">
					<form id="testCustForm" name="testCustForm" method="post">
						<input type="hidden" id="searchIfCampId" name="searchIfCampId" value="0">
						<input type="hidden" id="searchUserCustFirst" value="Y">
						
						<div class="title-area">
							<h3 class="h3-title">발송 고객 검색</h3>
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<span class="required" style="margin-right:0;">*필수입력 항목</span>
								<button type="button" class="btn fullblue" onClick="getTestCustList();">검색</button>
							</div>
							<!-- //btn-wrap -->
						</div>
						
						<div class="list-area">
						<ul>
							<li>
								<label>캠페인명</label>
								<div class="list-item">
									<p class="inline-txt" id="searchIfCampNm"></p>
								</div>
							</li>
							<li>
								<label class="required">이메일</label>
								<div class="list-item">
									<input type="text" placeholder="이메일을 입력해주세요." id="searchIfEmail" name="searchIfEmail">
								</div>
							</li>
							<li>
								<label>ID</label>
								<div class="list-item">
									<input type="text" placeholder="고객ID를 입력해주세요." id="searchIfId" name="searchIfId">
								</div>
							</li>
							<li>
								<label>고객명</label>
								<div class="list-item">
									<input type="text" placeholder="고객명을 입력해주세요." id="searchIfName" name="searchIfName">
								</div>
							</li>
						</ul>
						</div>
					</form>
				</div>
					
				<!-- //발송고객정보 -->
				<!-- 목록// -->
				<div id="divTestCustList" style="margin-top:30px;"></div>
				<!-- //목록 -->

			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_testsend_user_cust');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
