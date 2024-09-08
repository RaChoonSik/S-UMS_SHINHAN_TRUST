<%--
	/**********************************************************
	*	작성자 : 이문용
	*	작성일시 : 2022.03.30
	*	설명 : 통계분석 메세지 발송목록 분석 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
	</div>
		<div class="grid-area">
			<table class="grid">
				<caption>그리드 정보</caption>
				<colgroup>
					<col style="width:5%;">
					<col style="width:12%;">
					<col style="width:auto;">
					<col style="width:10%;">
					<col style="width:15%;">
					<col style="width:10%;">
					<col style="width:8%;">
					<col style="width:8%;">
					<col style="width:8%;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">NO</th>
						<th scope="col">발송일시</th>
						<th scope="col">메세지명</th>
						<th scope="col">전송유형</th>
						<th scope="col">캠페인명</th>
						<th scope="col">사용자그룹</th>
						<th scope="col">발송성공</th>
						<th scope="col">발송실패</th>
						<th scope="col">총 건수 </th>
					</tr>
				</thead>
				<tbody>
					<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(smsSendList) > 0}">
					<c:forEach items="${smsSendList}" var="smsSend" varStatus="status">
						<tr>
 							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - status.index}"/></td>
							<td>
								<fmt:parseDate var="sendDate" value="${smsSend.sendDate}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value="${sendDt}"/>
							</td>
<%--  							<td><a href="javascript:goSmsAnalList('<c:out value='${smsSend.msgid}'/>','<c:out value='${smsSend.keygen}'/>');" class="bold"><c:out value="${smsSend.taskNm}"/></a></td> --%>
 							<td><c:out value="${smsSend.taskNm}"/></td>
							<td><c:out value='${smsSend.gubunNm}'/></td>
							<td><c:out value='${smsSend.campNm}'/></td>
							<td><c:out value='${smsSend.deptNm}'/></td>
							<td><span onclick="goSmsSendList('<c:out value='${smsSend.msgid}'/>','<c:out value='${smsSend.keygen}'/>','1')" style="cursor: pointer;"><c:out value='${smsSend.succCnt}'/></span></td>
							<td><span class="color-red" onclick="goSmsSendList('<c:out value='${smsSend.msgid}'/>','<c:out value='${smsSend.keygen}'/>','0')" style="cursor: pointer;"><c:out value='${smsSend.failCnt}'/></span></td>
							<td><span onclick="goSmsSendList('<c:out value='${smsSend.msgid}'/>','<c:out value='${smsSend.keygen}'/>','')" style="cursor: pointer;"><c:out value='${smsSend.sendTotCnt}'/></span></td>
						</tr>
					</c:forEach>
				</c:if>

				<c:if test="${empty smsSendList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="9" class="no_data">등록된 내용이 없습니다.</td>
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