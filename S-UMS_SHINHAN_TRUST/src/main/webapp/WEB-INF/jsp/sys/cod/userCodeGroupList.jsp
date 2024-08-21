<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 코드 목록 조회
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
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>					
					<th scope="col">
						<label for="userCodeGrouupAllChk"><input type="checkbox" id="userCodeGrouupAllChk" name="userCodeGrouupAllChk" onclick='selectAll(this)'><span></span></label>
					</th>
					<th scope="col">분류코드</th>
					<th scope="col">분류명</th>
					<th scope="col">상위코드</th>
					<th scope="col">설명</th>
					<th scope="col">시스템</th>
					<th scope="col">사용여부</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(userCodeGroupList) > 0}">
					<c:forEach items="${userCodeGroupList}" var="userCodeGroup" varStatus="userCodeGroupStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - userCodeGroupStatus.index}"/></td>
							<td align="center">
								<c:if test="${userCodeGroup.sysYn eq 'Y'}">
									<label for="checkbox_<c:out value='${userCodeGroupStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${userCodeGroupStatus.count}'/>" name="delCdGrp" value="<c:out value='${userCodeGroup.cdGrp}'/>" disabled="disabled"><span></span></label>
								</c:if>
								<c:if test="${userCodeGroup.sysYn eq 'N'}">
									<label for="checkbox_<c:out value='${userCodeGroupStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${userCodeGroupStatus.count}'/>" name="delCdGrp" value="<c:out value='${userCodeGroup.cdGrp}'/>"><span></span></label>
								</c:if>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${userCodeGroup.cdGrp}'/>');" class="bold"><c:out value='${userCodeGroup.cdGrp}'/></a>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${userCodeGroup.cdGrp}'/>');" class="bold"><c:out value='${userCodeGroup.cdGrpNm}'/></a>
							</td>
							 <c:choose>					
								<c:when test="${empty userCodeGroup.upCdGrp}">
									<td><c:out value="${userCodeGroup.upCdGrpNm}"/></td>
								</c:when>
								<c:otherwise>
									<td><c:out value="${userCodeGroup.upCdGrpNm}"/>(<c:out value="${userCodeGroup.upCdGrp}"/>)</td>
								</c:otherwise>
							</c:choose>
							<td><c:out value="${userCodeGroup.cdGrpDtl}"/></td>
							<td><c:out value="${userCodeGroup.sysYn}"/></td>
							<td><c:out value="${userCodeGroup.useYn}"/></td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty userCodeGroupList}">
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
<!-- //페이징 -->
