<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.10
	*	설명 : 카카오 알림톡 템플릿 목록 조회
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
			<button type="button" class="btn fullblue plus" onclick="goAdd();">신규등록</button>
		</div>
		<!-- //총 건 -->
	</div>

	<div class="grid-area">
		<table class="grid layout-fixed">
			<caption>알림톡 템플릿 목록</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:auto;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:12%;">
				<col style="width:12%;">
				<col style="width:15%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">템플릿명</th>
					<th scope="col">템플릿 코드</th>
					<th scope="col">사용자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">상태</th>
					<th scope="col">등록일시</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(kakaoTemplateList) > 0}">
					<c:forEach items="${kakaoTemplateList}" var="kakaoTemplate" varStatus="segStatus">
						<tr>
 							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - segStatus.index}"/></td>
 							<td><a href="javascript:goUpdate('<c:out value='${kakaoTemplate.tempCd}'/>');" class="bold"><c:out value="${kakaoTemplate.tempNm}"/></a></td>
							<td><c:out value='${kakaoTemplate.tempCd}'/></td>
							<td><c:out value='${kakaoTemplate.deptNm}'/></td>
							<td><c:out value='${kakaoTemplate.userNm}'/></td>
							<td><c:out value='${kakaoTemplate.statusNm}'/></td>
							<td>
								<fmt:parseDate var="regDate" value="${kakaoTemplate.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value="${regDt}"/>
							</td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty kakaoTemplateList}">
					<tr>
						<td colspan="7" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
				</c:if>
				<!-- //데이터가 없을 경우 -->
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
