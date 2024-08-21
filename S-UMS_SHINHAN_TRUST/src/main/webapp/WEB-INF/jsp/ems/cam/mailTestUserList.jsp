<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.23
	*	설명 : 테스트발송 사용자 목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">수신대상자 조회</h3>

		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn" onclick="goTestUserDelete();">삭제</button>
		</div>
		<!-- //버튼 -->
	</div>
	
	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:10%;">
				<col style="width:25%;">
				<col style="width:auto;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">
						<label><input type="checkbox" name="isUserAll" onclick="goUserAll();"><span></span></label>
					</th>
					<th scope="col">수신자</th>
					<th scope="col">이메일</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(testUserList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${testUserList}" var="testUser" varStatus="userStatus">
						<tr>
							<td><label><input type="checkbox" name="testUserNos" value="<c:out value="${testUser.testUserNo}"/>" onclick="checkTestUserEm(${userStatus.index})"><span></span></label></td>
							<td><c:out value="${testUser.testUserNm}"/></td>
							<td><crypto:decrypt colNm="TEST_USER_EM" data="${testUser.testUserEm}"/><input type="checkbox" name="testUserEm" value="<crypto:decrypt colNm="TEST_USER_EM" data="${testUser.testUserEm}"/>" style="width:0px;"></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>
				<c:if test="${empty testUserList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="3" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
		</table>
	</div>
</div>
