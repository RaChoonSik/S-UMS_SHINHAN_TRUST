<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.18
	*	설명 : DB연결 목록 조회
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
			<button type="button" class="btn fullred plus" onclick="goAdd();">신규등록</button>
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
		<!-- //총 건 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:auto;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:0%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label for="dbConnAllChk"><input type="checkbox" id="dbConnAllChk" name="dbConnAllChk" onclick='selectAll(this)'><span></span></label>
					</th>
					<th scope="col">CONNECTION명</th>
					<th scope="col">DB종류</th>
					<th scope="col">상태</th>
					<th scope="col">권한관리</th>
					<th scope="col">메타정보관리</th>
					<th scope="col">조인정보관리</th>
					<th scope="col">등록자</th>
					<th scope="col">등록일</th>
					<th scope="col" class="hide">DB연결번호</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(dbConnList) > 0}">
					<c:forEach items="${dbConnList}" var="dbConn" varStatus="dbConnStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - dbConnStatus.index}"/></td>
							<td align="center">
								<label for="checkbox_<c:out value='${dbConnStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${dbConnStatus.count}'/>" name="dbConnNo" value="<c:out value='${dbConn.dbConnNo}'/>"><span></span></label>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${dbConn.dbConnNo}'/>')" class="bold"><c:out value='${dbConn.dbConnNm}'/></a>
							</td>
							<td><c:out value="${dbConn.dbTyNm}"/></td>
							<td><c:out value="${dbConn.statusNm}"/></td>
							<td><button type="button" class="btn" value="<c:out value='${dbConn.dbConnNo}'/>" onClick="setGrant(<c:out value='${dbConn.dbConnNo}'/>)">설정</button></td>
							<td><button type="button" class="btn" value="<c:out value='${dbConn.dbConnNo}'/>" onClick="setMeta(<c:out value='${dbConn.dbConnNo}'/>)">설정</button></td>
							<td><button type="button" class="btn" value="<c:out value='${dbConn.dbConnNo}'/>" onClick="setJoin(<c:out value='${dbConn.dbConnNo}'/>)">설정</button></td>
							<td><c:out value="${dbConn.regNm}"/></td>
							<td><c:out value="${dbConn.regDt}"/></td>
							<td class="hide"><c:out value='${dbConn.dbConnNo}'/></td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty dbConnList}">
					<tr>
						<td colspan="8" class="no_data">등록된 내용이 없습니다.</td>
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
