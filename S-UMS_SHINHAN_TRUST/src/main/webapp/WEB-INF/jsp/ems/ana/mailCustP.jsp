<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.04
	*	설명 : 통계분석 고객별 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 조회// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">조회</h3>
	</div>
	
	<div class="list-area">
		<ul>
			<li>
				<label>ID</label>
				<div class="list-item">
					<input type="text" id="searchCustId" name="searchCustId" value="<c:out value='${sendLogVO.searchCustId}'/>" placeholder="ID를 입력해주세요.">
				</div>
			</li>
			<li>
				<label>이메일</label>
				<div class="list-item">
					<input type="text" id="searchCustEm" name="searchCustEm" value="<c:out value='${sendLogVO.searchCustEm}'/>" placeholder="이메일을 입력해주세요.">
				</div>
			</li>
			<li>
				<label>고객명</label>
				<div class="list-item">
					<input type="text" id="searchCustNm" name="searchCustNm" value="<c:out value='${sendLogVO.searchCustNm}'/>" placeholder="고객명을 입력해주세요.">
				</div>
			</li>
			<li>
				<label>검색유형</label>
				<div class="list-item">
					<div class="select">
						<select id="searchKind" name="searchKind" title="검색유형 선택">
							<option value="000"<c:if test="${'000' eq sendLogVO.searchKind}"> selected</c:if>>선택</option>
							<option value="001"<c:if test="${'001' eq sendLogVO.searchKind}"> selected</c:if>>발송성공</option>
							<option value="002"<c:if test="${'002' eq sendLogVO.searchKind}"> selected</c:if>>발송실패</option>
							<option value="003"<c:if test="${'003' eq sendLogVO.searchKind}"> selected</c:if>>수신</option>
							<option value="004"<c:if test="${'004' eq sendLogVO.searchKind}"> selected</c:if>>미수신</option>
							<option value="005"<c:if test="${'005' eq sendLogVO.searchKind}"> selected</c:if>>수신거부</option>
							<option value="006"<c:if test="${'006' eq sendLogVO.searchKind}"> selected</c:if>>수신허용</option>
						</select>
					</div>
				</div>
			</li>
		</ul>
	</div>
</div>
<!-- //조회 -->

<!-- btn-wrap// -->
<div class="btn-wrap">
	<button type="button" class="btn big fullblue" onclick="goCustSearch();">검색</button>
	<button type="button" class="btn big" onclick="goInit();">초기화</button>
</div>
<!-- //btn-wrap -->

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
				<col style="width:9%;">
				<col style="width:auto;">
				<col style="width:7%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:9%;">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">ID</th>
					<th scope="col">이메일</th>
					<th scope="col">고객명</th>
					<th scope="col">발송여부</th>
					<th scope="col">발송시간</th>
					<th scope="col">수신여부</th>
					<th scope="col">수신거부</th>
					<th scope="col">수신시간</th>
					<th scope="col">Message</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(custList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${custList}" var="cust" varStatus="custStatus">
						<tr>
							<td>${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - custStatus.index}</td>
							<td><c:out value='${cust.custId}'/></td>
							<td><crypto:decrypt colNm="CUST_EM" data="${cust.custEm}"/></td>
							<td><c:out value='${cust.custNm}'/></td>
							<td>
								<c:choose>
									<c:when test="${'000' eq cust.sendRcode}">발송성공</c:when>
									<c:otherwise>발송실패</c:otherwise>
								</c:choose>
							</td>
							<td>
								<fmt:parseDate var="sendDate" value="${cust.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy-MM-dd HH:mm"/>
								<c:out value='${sendDt}'/>
							</td>
							<td>
								<c:choose>
									<c:when test="${empty cust.openDt}">미확인</c:when>
									<c:otherwise>수신확인</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq cust.deniedType}">수신거부</c:when>
									<c:otherwise>수신허용</c:otherwise>
								</c:choose>
							</td>
							<td>
								<fmt:parseDate var="openDate" value="${cust.openDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="openDt" value="${openDate}" pattern="yyyy-MM-dd HH:mm"/>
								<c:out value='${openDt}'/>
							</td>
							<td><c:out value='${cust.sendMsg}'/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty custList}">
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

<!-- btn-wrap// -->
<div class="btn-wrap">
	<button type="button" class="btn big fullblue" onclick="goExcelDown('Cust');">엑셀 다운로드</button>
	<button type="button" class="btn big" onclick="goList();">목록</button>
</div>
<!-- //btn-wrap -->
