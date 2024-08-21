<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.01
	*	설명 : RNS 자동메일 서비스목록 조회
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
			<button type="button" class="btn fullpurple plus" onclick="goServiceAdd();">신규등록</button>
			<button type="button" class="btn" onclick="goApprCancel();">결재취소</button>
			<button type="button" class="btn" onclick="popRnsTestSend();">테스트발송</button>
			<button type="button" class="btn" onclick="goCopy();">복사</button>
			<button type="button" class="btn" onclick="goDisable();">사용중지</button>
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
				<col style="width:8%;">
				<col style="width:auto;">
				<col style="width:12%;">
				<col style="width:12%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<col style="width:10%;">
				<!-- <col style="width:8%;"> -->
				<!-- <col style="width:8%;"> -->
				<col style="width:12%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label><input type="checkbox" name="isAll" onclick="goAll();"><span></span></label>
					</th>
					<th scope="col">서비스아이디(캠페인번호)</th>
					<th scope="col">서비스명</th>
					<th scope="col">사용자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">콘텐츠타입</th>
					<th scope="col">상태</th>
					<th scope="col">등록일시</th>
					<!-- <th scope="col">결재상태</th> -->
					<!-- <th scope="col">보안결재</th> -->
					<th scope="col">템플릿코드</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(serviceList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${serviceList}" var="service" varStatus="serviceStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - serviceStatus.index}"/></td>
							<td>
								<c:choose>
									<c:when test="${'002' eq service.status}">
										<label><input type="checkbox" name="tids" value="<c:out value='${service.tid}'/>" onclick="return goDeleteClick();" disabled><span></span></label>
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="tids" value="<c:out value='${service.tid}'/>" onclick="goTid('<c:out value='${serviceStatus.index}'/>')"><span></span></label>
									</c:otherwise>
								</c:choose>
								<input type="checkbox" name="status" value="<c:out value="${service.status}"/>" style="display:none;">
								<input type="checkbox" name="workStatus" value="<c:out value='${service.workStatus}'/>" style="display:none;">
								<input type="checkbox" name="apprProcYn" value="<c:out value='${service.approvalProcYn}'/>" style="display:none;">
							</td>
							<td><c:out value='${service.tid}'/></td>
							<td><a href="javascript:goUpdate('<c:out value='${service.tid}'/>');" class="bold"><c:out value="${service.tnm}"/></a></td>
							<td><c:out value="${service.deptNm}"/></td>
							<td><c:out value="${service.userNm}"/></td>
							<td><c:out value="${service.contentsTypNm}"/></td>
							<td><c:out value="${service.statusNm}"/></td>
							<td>
								<fmt:parseDate var="regDate" value="${service.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${regDt}"/>
							</td>
							 
<!-- 							<td> -->
<%-- 								<c:choose> --%>
<%-- 									발송대기 --%>
<%-- 									<c:when test="${'000' eq service.workStatus}"> --%>
<%-- 										<c:if test="${'000' eq service.status}"> --%>
<%-- 											<a href="javascript:goRnsAdmit('<c:out value='${service.tid}'/>');" class="medium"><c:out value="${service.workStatusNm}"/></a> --%>
<%-- 										</c:if> --%>
<%-- 										<c:if test="${'000' ne service.status}"> --%>
<%-- 											<c:out value="${service.workStatusNm}"/> --%>
<%-- 										</c:if> --%>
<%-- 									</c:when> --%>
<%-- 									결재 완료 --%>
<%-- 									<c:when test="${'204' eq service.workStatus}"> --%>
<%-- 										<c:out value="${service.workStatusNm}"/> --%>
<%-- 									</c:when> --%>
<%-- 									발송승인/발송중/발송완료/결재진행/결재반려 --%>
<%-- 									<c:when test="${'001' eq service.workStatus || '002' eq service.workStatus || '003' eq service.workStatus || '202' eq service.workStatus || '203' eq service.workStatus}"> --%>
<%-- 										<c:out value="${service.workStatusNm}"/> --%>
<%-- 									</c:when> --%>
<%-- 									결재대기 --%>
<%-- 									<c:when test="${'201' eq service.workStatus}"> --%>
<%-- 										<c:if test="${'000' eq service.status}"> --%>
<%-- 											<a href="javascript:popSubmitApproval('<c:out value='${service.tid}'/>','<c:out value='${service.userId}'/>');" class="medium"><c:out value="${service.workStatusNm}"/></a> --%>
<%-- 										</c:if> --%>
<%-- 										<c:if test="${'000' ne service.status}"> --%>
<%-- 											<c:out value="${service.workStatusNm}"/> --%>
<%-- 										</c:if> --%>
<%-- 									</c:when> --%>
<%-- 									발송실패 --%>
<%-- 									<c:otherwise> --%>
<%-- 										<a href="javascript:goFail('<c:out value='${service.workStatusDtl}'/>');" class="medium"><c:out value="${service.workStatusNm}"/></a> --%>
<%-- 										<c:out value="${service.workStatusNm}"/> --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<!-- 							</td> -->
							 
							<%-- <td><c:if test="${'Y' eq service.approvalLineYn}">해당</c:if></td> --%>
							<td><c:out value="${service.eaiCampNo}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>
				<c:if test="${empty serviceList}">
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
