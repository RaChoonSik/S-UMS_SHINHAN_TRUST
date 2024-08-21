<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.03.21
	*	설명 : SMS발송 발송 이력	출력(팝업내용)
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
	<form id="popSmsSendHistForm" name="popSmsSendHistForm" method="post">
		<input type="hidden" name="page" value="${searchVO.page}">
		<input type="hidden" id="popHistMsgid" name="msgid" value="${searchVO.msgid}">
		<input type="hidden" id="popHistKeygen" name="keygen" value="${searchVO.keygen}">
		<fieldset>
			<!-- 목록// -->
			<div class="graybox">
				<div class="title-area">
					<h3 class="h3-title">목록</h3>
					<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
					<!-- 버튼// -->
					<div class="btn-wrap">
						<button type="button" class="btn fullgreen" onclick="fn.popupClose('#popup_sms_send_hist');">닫기</button>
					</div>
					<!-- //버튼 -->
				</div>
				<div class="grid-area">
					<table class="grid">
						<caption>발송이력정보 정보</caption>
						<colgroup>
							<col style="width:10%;">
							<col style="width:auto%;">
							<col style="width:25%;">
							<col style="width:25%;">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">발송구분</th>
								<th scope="col">문자명</th>
								<th scope="col">발송일</th>
								<th scope="col">발송결과</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${fn:length(smsSendHist) > 0}">
								<!-- 데이터가 있을 경우// -->
								<c:forEach items="${smsSendHist}" var="smsSend" >
									 <tr>
										<td style="color: navy;font-weight:bold" >본발송  (<c:out value="${smsSend.msgid}"/>)</td>
										<td style="color: navy;font-weight:bold"><c:out value="${smsSend.smsName}"/></td>
										<td style="color: navy;font-weight:bold">
											<fmt:parseDate var="sendDate" value="${smsSend.sendDate}" pattern="yyyyMMddHHmm"/>
											<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
											<c:out value='${sendDt}'/>
										</td>
										<td style="color: navy;font-weight:bold"><c:out value="${smsSend.statusNm}"/></td>
									</tr>
								</c:forEach>
								<!-- //데이터가 있을 경우 -->
							</c:if>
							<c:if test="${empty smsSendHist}">
								<!-- 데이터가 없을 경우// -->
								<tr>
									<td colspan="4" class="no_data">등록된 내용이 없습니다.</td>
								</tr>
								<!-- //데이터가 없을 경우 -->
							</c:if>
						</tbody>
 
					</table>
				</div>
			</div>
			<!-- //목록 -->
		
			<!-- 페이징// -->
			<div class="paging">
				${pageUtil.pageHtml}
			</div>
			<!-- //페이징 -->

		</fieldset>
	</form>
</div>

