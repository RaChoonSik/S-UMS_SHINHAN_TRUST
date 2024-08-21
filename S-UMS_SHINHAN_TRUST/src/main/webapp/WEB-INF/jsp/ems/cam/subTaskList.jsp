<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.31
	*	설명 : 정기메일 차수별 발송목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
		
		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullblue" onclick="goMain();">뒤로가기</button>
		</div>
		<!-- //버튼 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:13%;">
				<col style="width:13%;">
				<col style="width:5%;">
				<col style="width:auto;">
				<col style="width:15%;">
				<col style="width:14%;">
				<col style="width:8%;">
				<col style="width:6%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">발송일시</th>
					<th scope="col">발송종료일시</th>
					<th scope="col">소요시간</th>
					<th scope="col">메일명</th>
					<th scope="col">캠페인명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">발송상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty mailList}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${mailList}" var="mail" varStatus="mailStatus">
						<tr>
							<td>${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailStatus.index}</td>
							<td>
								<fmt:parseDate var="sendDate" value="${mail.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${sendDt}"/>
							</td>
							<td>
								<fmt:parseDate var="endDate" value="${mail.endDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="endDt" value="${endDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${endDt}"/>
							</td>
							<td>
								<c:if test="${not empty mail.endDt}">
									<fmt:parseNumber var="sendDtNum" value="${sendDate.time/(1000*60)}"/>
									<fmt:parseNumber var="endDtNum" value="${endDate.time/(1000*60)}"/>
									<fmt:formatNumber var="dateDiff" type="number" value="${endDtNum - sendDtNum + 1}"/>
									<c:out value="${dateDiff}"/>분 
								</c:if>
							</td>
							<td>
								<c:choose>
									<c:when test="${'204' eq mail.workStatus || '001' eq mail.workStatus}">
	 									<a href="javascript:goUpdateDate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','N');" class="bold">[<c:out value='${mail.subTaskNo}'/>차] <c:out value="${mail.taskNm}"/></a>
									</c:when>
									<c:when test="${'201' eq mail.workStatus}">
										<a href="javascript:goUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="bold">[<c:out value='${mail.subTaskNo}'/>차] <c:out value="${mail.taskNm}"/></a>
									</c:when>
									<c:when test="${'202' eq mail.workStatus}">
										<c:choose>
											<c:when test="${'N' eq mail.approvalProcAppYn}">
												<a href="javascript:goUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="bold">[<c:out value='${mail.subTaskNo}'/>차] <c:out value="${mail.taskNm}"/></a>
											</c:when>
											<c:otherwise>
												<a href="javascript:goUpdateDate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','Y');" class="bold">[<c:out value='${mail.subTaskNo}'/>차] <c:out value="${mail.taskNm}"/></a>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<a href="javascript:goUpdateDate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','Y');" class="bold">[<c:out value='${mail.subTaskNo}'/>차] <c:out value="${mail.taskNm}"/></a>
									</c:otherwise>
								</c:choose>
							</td>
							<%-- <td><a href="javascript:goUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="bold">[<c:out value='${mail.subTaskNo}'/>차] <c:out value="${mail.taskNm}"/></a></td> --%>
							<td><c:out value="${mail.campNm}"/></td>
							<td><c:out value="${mail.segNm}"/></td>
							<td><c:out value="${mail.userNm}"/></td>
							<td>
								<c:choose>
									<%-- 발송대기 (최초: 결재상신 ==>변경: 결재완료--%>
									<c:when test="${'204' eq mail.workStatus}"> 
											<c:out value="${mail.workStatusNm}"/> 
									</c:when>
									<%-- 발송승인/발송중/발송완료/결재진행/결재반려 --%>
									<c:when test="${'001' eq mail.workStatus || '002' eq mail.workStatus || '003' eq mail.workStatus || '202' eq mail.workStatus || '203' eq mail.workStatus}">
										<c:out value="${mail.workStatusNm}"/>
									</c:when>
									<%-- 결재대기 --%>
									<c:when test="${'201' eq mail.workStatus}">
										<c:out value="${mail.workStatusNm}"/>
									</c:when>
									<%-- 발송실패 --%>
									<c:otherwise>
										<a href="javascript:goFail('<c:out value='${mail.workStatusDtl}'/>');" class="medium"><c:out value="${mail.workStatusNm}"/></a>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty mailList}">
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