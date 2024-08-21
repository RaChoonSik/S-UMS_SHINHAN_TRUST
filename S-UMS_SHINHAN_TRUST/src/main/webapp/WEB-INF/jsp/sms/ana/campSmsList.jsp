<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.10.26
	*	설명 : 캠페인별 발송현황
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
					<col style="width:15;">
					<col style="width:10;">
					<col style="width:10%;">
					<col style="width:5%;">
					<col style="width:auto;">
					<col style="width:9%;">
					<col style="width:9%;">
					<col style="width:9%;">
					<col style="width:9%;">
				</colgroup>
				<thead>
					<tr>
					<th scope="col">NO</th>
					<th scope="col">템플릿명</th>
					<th scope="col">템플릿코드</th>
					<th scope="col">최종<br>발송일자</th>
					<th scope="col">건수</th>
					<th scope="col">발송문자명</th>
					<th scope="col">사용자명</th>
					<th scope="col">문자등록일자</th>
					<th scope="col">템플릿등록일자</th>
					<th scope="col">발송상태</th> 
					</tr>
				</thead>
				<tbody>
					<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(campSmsList) > 0}">
					<c:forEach items="${campSmsList}" var="campSms" varStatus="status">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - status.index}"/></td>
							<td><a href="javascript:;" onclick="goCampSmsList('<c:out value="${campSms.tempCd}"/>','<c:out value="${campSms.tempNm}"/>');" class="bold"><c:out value="${campSms.tempNm}"/></a></td>
							<c:if test="${campSms.tempCd !='999999'}">
							<td><c:out value="${campSms.tempCd}"/></td>
							</c:if>
							<c:if test="${campSms.tempCd =='999999'}">
							<td></td>
							</c:if>	
							<td>
								<fmt:parseDate var="sendDate" value="${campSms.sendDate}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy-MM-dd HH:mm"/>
								<c:out value='${sendDt}'/>
							</td>
							<td><c:out value="${campSms.smsCnt}"/></td>
							<td><c:out value="${campSms.smsName}"/></td>
							<td><c:out value="${campSms.userNm}"/></td>
							<td>
								<fmt:parseDate var="smsRegDate" value="${campSms.smsRegDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="smsRegDt" value="${smsRegDate}" pattern="yyyy-MM-dd"/>
								<c:out value='${smsRegDt}'/>
							</td>
							<td>
								<fmt:parseDate var="campRegDate" value="${campSms.tempRegDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="campRegDt" value="${campRegDate}" pattern="yyyy-MM-dd"/>
								<c:out value='${campRegDt}'/>
							</td>
							<td><c:out value="${campSms.statusNm}"/></td>
						</tr>
					</c:forEach>
				</c:if>

				<c:if test="${empty campSmsList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="10" class="no_data">등록된 내용이 없습니다.</td>
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