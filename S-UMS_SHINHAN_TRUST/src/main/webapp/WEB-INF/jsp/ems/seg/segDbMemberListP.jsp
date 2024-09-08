<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.21
	*	설명 : 대상자보기(미리보기)에서 DB 멤버 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 목록// -->

<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${totalCount}"/></em></span>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<!-- 데이터가 있을 경우// -->
			<c:if test="${totalCount > 0}">
				<thead>
					<tr>
						<c:forTokens items="${segmentVO.mergeKey}" delims="," var="mergeKey" varStatus="keyStatus">
							<th scope="col"><c:out value="${mergeKey}"/></th>
						</c:forTokens>
					</tr>
				</thead>
				<tbody>
					<!-- 데이터가 있을 경우// -->
					<c:if test="${fn:length(memberList) > 0}">
						<c:forEach items="${memberList}" var="member" varStatus="memberStatus">
							<tr>
								<c:forTokens items="${segmentVO.mergeKey}" delims="," var="mergeKey">
									<c:forEach items="${member}" var="mem">
										<c:if test="${mem.key eq mergeKey}">
											<td><crypto:decrypt colNm="${mem.key}" data="${mem.value}"/></td>
										</c:if>
									</c:forEach>
								</c:forTokens>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</c:if>
			<!-- //데이터가 있을 경우 -->
	
			<!-- 데이터가 없을 경우// -->
			<c:if test="${totalCount == 0}">
				<tr>
					<td colspan="6" class="no_data">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<!-- //데이터가 없을 경우 -->
		</table>
	</div>
</div>
<!-- //목록 -->

<!-- 페이징// -->
<div class="paging">
	${pageUtil.pageHtml}
</div>
<!-- //페이징 -->

