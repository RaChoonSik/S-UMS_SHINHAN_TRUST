<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.17
	*	설명 : 캠페인별 메일발송목록 조회
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
			<button type="button" class="btn" onclick="goApprCancel();">결재취소</button>
			<button type="button" class="btn" onclick="popTestSend();">테스트발송</button>
			<button type="button" class="btn" onclick="goCopy();">복사</button>
			<button type="button" class="btn" onclick="goDisable();">사용중지</button>
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
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:10%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:5%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label for="checkbox_all"><input type="checkbox" name="isAll" id="checkbox_all" onclick='goAll()' style="border: 0"><span></span></label>
					</th>
					<th scope="col">발송일시</th>
					<th scope="col">메일명</th>
					<th scope="col">캠페인명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">메일유형</th>
					<th scope="col">상태</th>
					<th scope="col">빌송상태</th>
					<th scope="col">보안결제</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(campMailList) > 0}">
					<c:forEach items="${campMailList}" var="mail" varStatus="mailStatus">
						<!-- 데이터가 있을 경우// -->
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailStatus.index}"/></td>
							<td>
								<c:choose>
									<c:when test="${'002' eq mail.status}">
										<label><input type="checkbox" name="taskNos" value="<c:out value='${mail.taskNo}'/>" disabled><span></span></label>
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="taskNos" value="<c:out value='${mail.taskNo}'/>" onclick="goTaskNo('<c:out value='${mailStatus.index}'/>')"><span></span></label>
									</c:otherwise>
								</c:choose>
								<input type="checkbox" name="subTaskNos" value="<c:out value='${mail.subTaskNo}'/>" style="display:none;">
								<input type="checkbox" name="workStatus" value="<c:out value='${mail.workStatus}'/>" style="display:none;">
								<input type="checkbox" name="apprProcYn" value="<c:out value='${mail.approvalProcYn}'/>" style="display:none;">
							</td>
							<td>
								<fmt:parseDate var="sendDate" value="${mail.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy-MM-dd HH:mm"/> 
								<c:out value="${sendDt}"/>
							</td>
							<%-- <td><a href="javascript:goUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','<c:out value='${mail.sendRepeat}'/>');" class="bold"><c:out value='${mail.taskNm}'/></a></td> --%>
							<td><a href="javascript:goSubmit('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','<c:out value='${mail.workStatus}'/>','<c:out value='${mail.approvalProcAppYn}'/>');" class="bold"><c:out value='${mail.taskNm}'/></a></td>
							<td><c:out value="${mail.campNm}"/></td>
							<td><c:out value="${mail.segNm}"/></td>
							<td><c:out value="${mail.userNm}"/></td>
							<td><c:out value="${mail.sendRepeatNm}"/></td>
							<td><c:out value="${mail.statusNm}"/></td>
							<td><c:out value="${mail.workStatusNm}"/></td>
							<td><c:if test="${'Y' eq mail.approvalLineYn}">해당</c:if></td>
						</tr>
						<!-- //데이터가 있을 경우 -->
					</c:forEach>
				</c:if>

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty campMailList}">
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
<div class="btn-wrap" style="margin-top:60px;">
	<button type="button" class="btn fullblue big" onClick="goList()">목록</button>
</div>

