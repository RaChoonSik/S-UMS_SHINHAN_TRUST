<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.31
	*	설명 : 테스트발송 결과 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_testsend_result" class="poplayer poptestsendinfo"><!-- id값 수정 가능 -->
	<div class="inner">
		<header>
			<h2>테스트메일 발송정보</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<!-- 조회// -->
				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">조회</h3>
					</div>
					
					<div class="table-area">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width:105px;">
								<col style="width:225px;">
								<col style="width:105px;">
								<col style="width:auto;">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">발송일시</th>
									<td id="txtSendDt"></td>
									<th scope="row">메일유형</th>
									<td id="txtSendRepeat"></td>
								</tr>
								<tr>
									<th scope="row">메일명</th>
									<td colspan="3" id="txtTaskNm"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<!-- 조회// -->

				<form id="testSendForm" name="testSendForm" method="post">
					<input type="hidden" name="page" value="1">
					<input type="hidden" name="taskNo" value="0">
					<input type="hidden" name="subTaskNo" value="0">
				</form>
				<!-- 목록&페이징// -->
				<div id="testResultList" style="margin-top:30px;"></div>
				<!-- //목록&페이징 --> 
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_testsend_result');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
