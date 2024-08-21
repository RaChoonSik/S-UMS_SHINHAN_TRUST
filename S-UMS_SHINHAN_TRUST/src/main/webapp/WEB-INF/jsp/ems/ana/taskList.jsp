<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.06
	*	설명 : 통계분석 정기메일 목록 조회
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
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:10%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:6%;">
				<col style="width:6%;">
				<col style="width:6%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label><input type="checkbox" name="isAll" onclick="goAll();"><span></span></label>
					</th>
					<th scope="col">구분</th>
					<th scope="col">캠페인명</th>
					<th scope="col">메일명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">등록일자</th>
					<th scope="col">발송일시</th>
					<th scope="col">발송상태</th>
					<th scope="col">상태</th>
					<th scope="col">차수별분석</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(taskList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${taskList}" var="mail" varStatus="mailStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailStatus.index}"/></td>
							<td>
								<label><input type="checkbox" name="taskNos" value="<c:out value='${mail.taskNo}'/>"><span></span></label>
								<input type="checkbox" name="subTaskNos" value="<c:out value='${mail.subTaskNo}'/>" style="display:none;">
							</td>
							<td><c:out value="${mail.sendRepeatNm}"/></td>
							<td><c:out value="${mail.campNm}"/></td>
							<td><a href="javascript:goTaskStat('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.sendRepeat}'/>');" class="bold"><c:out value="${mail.taskNm}"/></a></td>
							<td><c:out value='${mail.segNm}'/></td>
							<td><c:out value='${mail.userNm}'/></td>
							<td>
								<fmt:parseDate var="regDt" value="${mail.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="mailRegDt" value="${regDt}" pattern="yyyy.MM.dd"/>
								<c:out value='${mailRegDt}'/>
							</td>
							<td>
								<fmt:parseDate var="sendDt" value="${mail.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="mailSendDt" value="${sendDt}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value='${mailSendDt}'/>
							</td>
							<td><c:out value='${mail.workStatusNm}'/></td>
							<td><c:out value='${mail.statusNm}'/></td>
							<td>
								<c:if test="${mail.sendRepeat == '001'}">
									<button type="button" class="btn" onclick="goTaskStep('<c:out value='${mail.taskNo}'/>');">분석</button>
								</c:if>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>
				
				<c:if test="${empty taskList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="9" class="no_data">등록된 내용이 없습니다.</td>
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
