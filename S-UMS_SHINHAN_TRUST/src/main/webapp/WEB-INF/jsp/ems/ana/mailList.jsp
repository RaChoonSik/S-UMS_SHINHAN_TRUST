<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.04
	*	설명 : 단기메일 발송목록 조회
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
			<button type="button" class="btn fullblue" onclick="goJoin();">병합분석</button>
		</div>
		<!-- //버튼 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:10%;">
				<col style="width:13%;">
				<col style="width:auto;">
				<col style="width:13%;">
				<col style="width:13%;">
				<col style="width:13%;">
				<col style="width:13%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label><input type="checkbox" name="isAll" onclick="goAll();"><span></span></label>
					</th>
					<th scope="col">캠페인명</th>
					<th scope="col">메일명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">발송일시</th>
					<th scope="col">발송상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(mailList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${mailList}" var="mail" varStatus="mailStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailStatus.index}"/></td>
							<td>
								<label for="checkbox${mailStatus.count}"><input type="checkbox" id="checkbox${mailStatus.count}" name="taskNos" value="<c:out value='${mail.taskNo}'/>"><span></span></label>
								<input type="checkbox" name="subTaskNos" value="<c:out value='${mail.subTaskNo}'/>" style="display:none;">
							</td>
							<td><c:out value="${mail.campNm}"/></td>
							<td><a href="javascript:goMailStat('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="bold"><c:out value="${mail.taskNm}"/></a></td>
							<td><c:out value='${mail.segNm}'/></td>
							<td><c:out value='${mail.userNm}'/></td>
							<td>
								<fmt:parseDate var="sendDt" value="${mail.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="mailSendDt" value="${sendDt}" pattern="yyyy.MM.dd"/>
								<c:out value='${mailSendDt}'/>
							</td>
							<td><c:out value='${mail.statusNm}'/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty mailList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="8" class="no_data">등록된 내용이 없습니다.</td>
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
