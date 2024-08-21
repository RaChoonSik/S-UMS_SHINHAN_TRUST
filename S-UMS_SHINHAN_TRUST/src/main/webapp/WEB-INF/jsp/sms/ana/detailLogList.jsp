<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.16
	*	설명 : SMS 상세로그 목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

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
				<col style="width:5%;"><!-- 2021.12.30 col 추가 및 width 수정 -->
				<col style="width:7%;">
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:auto;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:5%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">메세지 유형</th>
					<th scope="col">발송유형</th>
					<th scope="col">발송일시</th>
					<th scope="col">메세지 제목</th>
					<th scope="col">고객ID</th>
					<th scope="col">고객명</th>
					<th scope="col">고객전화번호</th>
					<th scope="col">발송자 ID</th>
					<th scope="col">발송자명</th>
					<th scope="col">발송결과</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(sendLogList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${sendLogList}" var="sendLog" varStatus="logStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - logStatus.index}"/></td>
							<td><c:out value="${sendLog.gubunNm}"/></td>
							<td><c:out value="${sendLog.sendGubunNm}"/></td>
							<td>
								<fmt:parseDate var="sendDt" value="${sendLog.sendDate}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="sendDate" value="${sendDt}" pattern="yyyy-MM-dd HH:mm"/>
								<c:out value='${sendDate}'/>
							</td>
							<td><a href="javascript:;" onclick="goSmsDetail('<c:out value="${sendLog.msgid}"/>', '<c:out value="${sendLog.keygen}"/>', '<c:out value="${sendLog.campNm}"/>', '<c:out value="${sendLog.attachFileList}"/>', '<c:out value="${sendLog.cmid}"/>', this);" class="bold"><c:out value="${sendLog.subject}"/></a></td>
							<td><c:out value="${sendLog.id}"/></td>
							<td><c:out value="${sendLog.name}"/></td>
							<td><crypto:decrypt colNm= "PHONE" data="${sendLog.phone}"/></td>
							<td><c:out value="${sendLog.exeUserId}"/></td>
							<td><c:out value="${sendLog.exeUserNm}"/></td>
							<td>
								<c:choose>
									<c:when test="${'0' eq sendLog.rsltCd}">
										<a href="javascript:;" onclick="goPopRcodeDesc('<c:out value="${sendLog.rname}"/>');" >실패</a>
									</c:when>
									<c:otherwise>
										성공
									</c:otherwise>
								</c:choose>
							</td>
							<td>
							<input type="hidden" id="smsMessage" name= "smsMessage" value=""/>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty sendLogList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="18" class="no_data">등록된 내용이 없습니다.</td>
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

