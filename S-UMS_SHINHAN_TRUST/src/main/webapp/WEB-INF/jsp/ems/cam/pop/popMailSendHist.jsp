<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.21
	*	설명 : 업무별 메일 발송 이력	출력(팝업내용)
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
	<form id="popMailSendHistForm" name="popMailSendHistForm" method="post">
		<input type="hidden" name="page" value="${searchVO.page}">
		<input type="hidden" id="popHistTaskNo" name="taskNo" value="${searchVO.taskNo}">
		<input type="hidden" id="popHistSubTaskNo" name="subTaskNo" value="${searchVO.subTaskNo}">
		<fieldset>
			<!-- 목록// -->
			<div class="graybox">
				<div class="title-area">
					<h3 class="h3-title">목록</h3>
					<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
					<!-- 버튼// -->
					<div class="btn-wrap">
						<button type="button" class="btn fullblue" onclick="fn.popupClose('#popup_mail_send_hist');">닫기</button>
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
								<th scope="col">메일명</th>
								<th scope="col">발송일</th>
								<th scope="col">발송결과</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${fn:length(mailSendHist) > 0}">
								<!-- 데이터가 있을 경우// -->
								<c:forEach items="${mailSendHist}" var="mailSend" >
									<c:choose>
										<c:when test="${0 eq mailSend.rtyTaskNo}">
											 <tr>
												<td style="color: navy;font-weight:bold" >본발송  (<c:out value="${mailSend.taskNo}"/>)</td>
												<td style="color: navy;font-weight:bold"><c:out value="${mailSend.taskNm}"/></td>
												<td style="color: navy;font-weight:bold">
													<fmt:parseDate var="sendDate" value="${mailSend.sendDt}" pattern="yyyyMMddHHmm"/>
													<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
													<c:out value='${sendDt}'/>
												</td>
												<td style="color: navy;font-weight:bold"><c:out value="${mailSend.workStatusDtl}"/></td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td>재발송  (<c:out value="${mailSend.taskNo}"/>)</td>
												<td><c:out value="${mailSend.taskNm}"/></td>
												<td>
													<fmt:parseDate var="sendDate" value="${mailSend.sendDt}" pattern="yyyyMMddHHmm"/>
													<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
													<c:out value='${sendDt}'/>
												</td>
												<td><c:out value="${mailSend.workStatusDtl}"/></td>
											<tr>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								<!-- //데이터가 있을 경우 -->
							</c:if>
							<c:if test="${empty mailSendHist}">
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

