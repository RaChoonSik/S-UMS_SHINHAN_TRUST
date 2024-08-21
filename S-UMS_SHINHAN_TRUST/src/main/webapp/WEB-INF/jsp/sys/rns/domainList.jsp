<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.08
	*	설명 : 도메인 목록 
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
		<!-- //버튼 -->

	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:15%;">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label for="domainAllChk"><input type="checkbox" id="domainAllChk" name="domainAllChk" onclick='selectAll(this)'><span></span></label>
					</th>
					<th scope="col">도메인명</th>
					<th scope="col">등록자</th>
					<th scope="col">등록일</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(domainList) > 0}">
					<c:forEach items="${domainList}" var="domain" varStatus="domainStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - domainStatus.index}"/></td>
							<td align="center">
								<label for="checkbox_<c:out value='${domainStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${domainStatus.count}'/>" name="delDomainId" value="<c:out value='${domain.domainId}'/>"><span></span></label>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${domain.domainId}'/>')" class="bold"><c:out value='${domain.domainName}'/></a>
							</td>
							<td><c:out value="${domain.regNm}"/></td>
							<td><c:out value="${domain.regDt}"/></td>							
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty domainList}">
					<tr>
						<td colspan="5" class="no_data">등록된 내용이 없습니다.</td>
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
