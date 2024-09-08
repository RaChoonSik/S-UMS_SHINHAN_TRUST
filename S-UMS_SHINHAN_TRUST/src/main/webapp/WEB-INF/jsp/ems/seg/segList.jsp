<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.16
	*	설명 : 수신자그룹 목록 조회
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
			<button type="button" class="btn" onclick="goDisable();">사용중지</button>
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
		<!-- //총 건 -->
	</div>

	<div class="grid-area">
		<table class="grid layout-fixed">
			<caption>수신자그룹 목록</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:7%;">
				<col style="width:auto;">
				<col style="width:10%;">
				<col style="width:15%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:15%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label for="checkboxAll"><input type="checkbox" id="checkboxAll" name="isAll" onclick="goSelectAll();"><span></span></label>
					</th>
					<th scope="col">수신자그룹명</th>
					<th scope="col">수신자 인원</th>
					<th scope="col">질의문</th>
					<th scope="col">유형</th>
					<th scope="col">사용자명</th>
					<th scope="col">상태</th>
					<th scope="col">등록일시</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(segmentList) > 0}">
					<c:forEach items="${segmentList}" var="segment" varStatus="segStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - segStatus.index}"/></td>
							<td>
								<c:choose>
									<c:when test="${segment.status eq '002'}">
										<label for="checkbox${segStatus.count}"><input type="checkbox" id="checkbox${segStatus.count}" name="segNo" onclick="return goDeleteClick();"><span></span></label>
									</c:when>
									<c:otherwise>
										<label for="checkbox${segStatus.count}"><input type="checkbox" id="checkbox${segStatus.count}" name="segNos" value="<c:out value='${segment.segNo}'/>"/><span></span></label>
									</c:otherwise>
								</c:choose>
							</td>
							<td><a href="javascript:goUpdate('<c:out value='${segment.segNo}'/>','<c:out value='${segment.createTy}'/>','<c:out value='${segment.segFlPath}'/>');" class="bold"><c:out value="${segment.segNm}"/></a></td>
							<td><a href="javascript:goSegInfoMail('<c:out value='${segment.segNo}'/>');" class="medium"><c:out value="${segment.totCnt}"/></a></td>
							<td>
								<span class="ellipsis">
								<c:choose>
									<c:when test="${segment.createTy eq '003'}">
										<c:out value='${uploadPath}/${segment.segFlPath}'/>
									</c:when>
									<c:when test="${segment.createTy eq '002'}">
										<c:out value='${segment.query}'/>
									</c:when>
									<c:otherwise>
										<c:set var="query" value="SELECT ${segment.selectSql}"/>
										<c:set var="query" value="${query} FROM ${segment.fromSql}"/>
										<c:set var="query" value="${query} WHERE ${segment.whereSql}"/>
										<c:set var="query" value="${query} ORDER BY ${segment.orderbySql}"/>
										<c:out value='${segment.query}'/>
									</c:otherwise>
								</c:choose>
								</span>
							</td>
							<td><c:out value='${segment.createTyNm}'/></td>
							<td><c:out value='${segment.userNm}'/></td>
							<td><c:out value='${segment.statusNm}'/></td>
							<td>
								<fmt:parseDate var="regDate" value="${segment.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value="${regDt}"/>
							</td>
						</tr>
					</c:forEach>
				</c:if>
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty segmentList}">
					<tr>
						<td colspan="9" class="no_data">등록된 내용이 없습니다.</td>
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
