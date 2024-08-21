<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.30
	*	설명 : 결재 반려사유 내용 표시
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_confirm_display" class="poplayer popreturnreason">
	<div class="inner">
		<header>
			<h2>결재 승인 의견</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">

					<div class="table-area">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width:110px">
								<col style="width:auto">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">승인일자</th>
									<td id="txtConfirmRejectDt"></td>
								</tr>
								<tr>
									<th scope="row">결제유형</th>
									<td id="txtConfirmRejectView"></td>
								</tr>								
								<tr>
									<th scope="row">승인의견</th>
									<td>
										<textarea readonly id="txtConfirmRejectDesc"></textarea>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<!-- 버튼// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="fn.popupClose('#popup_confirm_display');">확인</button>
				</div>
				<!-- //버튼 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_confirm_display');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>