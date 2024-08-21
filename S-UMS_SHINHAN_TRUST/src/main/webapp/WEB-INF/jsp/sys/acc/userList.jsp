<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : 사용자 목록 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<!-- //총 건 -->
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>

		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullred plus" onclick="goAdd();">신규등록</button>
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:8%;">
				<col style="width:15%;">
				<col style="width:8%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label for="userAllChk"><input type="checkbox" id="userAllChk" name="userAllChk" onclick='selectAll(this)'><span></span></label>
					</th>
					<th scope="col">사용자ID</th>
					<th scope="col">사용자명</th>
					<th scope="col">사용자상태</th>
					<th scope="col">사용자그룹</th>
					<th scope="col">서비스권한</th>
					<th scope="col">부서명</th>
					<th scope="col">직급/직책</th>
					<th scope="col">등록자</th>
					<th scope="col">등록일</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(userList) > 0}">
					<c:forEach items="${userList}" var="user" varStatus="userStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - userStatus.index}"/></td>
							<td align="center">
								<label for="checkbox_<c:out value='${userStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${userStatus.count}'/>" name="delUserId" value="<c:out value='${user.userId}'/>"><span></span></label>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${user.userId}'/>')" class="bold"><c:out value='${user.userId}'/></a>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${user.userId}'/>')" class="bold"><c:out value='${user.userNm}'/></a>
							</td>
							<td><c:out value="${user.statusNm}"/></td>
							<td><c:out value="${user.deptNm}"/></td>
							<td><c:out value="${user.serviceNm}"/></td>
							<td><c:out value="${user.orgKorNm}"/></td>
							<td><c:out value="${user.positionNm}"/>/<c:out value="${user.jobNm}"/></td>
							<td><c:out value="${user.regNm}"/></td>
							<td><c:out value="${user.regDt}"/></td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty userList}">
					<tr>
						<td colspan="11" class="no_data">등록된 내용이 없습니다.</td>
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
