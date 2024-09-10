<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.27
	*	설명 : 정기메일 등록현황 목록 조회
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
			<button type="button" class="btn fullblue plus" onclick="goMailAdd();">신규등록</button>
<!-- 			<button type="button" class="btn" onclick="goApprCancel();">결재취소</button> -->
			<button type="button" class="btn" onclick="popTestSend();">테스트발송</button>
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
				<col style="width:10%;">
				<col style="width:15%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:10%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:8%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">
						<label><input type="checkbox" name="isAll" onclick="goAll();"><span></span></label>
					</th>
					<th scope="col">예약일시</th>
					<th scope="col">메일명</th>
					<th scope="col">메일유형</th>
					<th scope="col">발송메일</th>
					<th scope="col">캠페인명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">상태</th>
					<th scope="col">발송상태</th>
					<th scope="col">보안결재</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty mailList}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${mailList}" var="mail" varStatus="mailStatus">
						<tr>
							<td>${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - mailStatus.index}</td>
							<td>
								<c:choose>
									<c:when test="${'002' eq mail.status}">
										<label><input type="checkbox" name="taskNos" value="<c:out value='${mail.taskNo}'/>" onclick="return goDeleteClick();" disabled><span></span></label>
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="taskNos" value="<c:out value='${mail.taskNo}'/>" onclick="goTaskNo('<c:out value='${mailStatus.index}'/>')"><span></span></label>
									</c:otherwise>
								</c:choose>
								<input type="checkbox" name="subTaskNos" value="<c:out value='${mail.subTaskNo}'/>" style="display:none;">
								<input type="checkbox" name="workStatus" value="<c:out value='${mail.workStatus}'/>" style="display:none;">
								<input type="checkbox" name="apprProcYn" value="<c:out value='${mail.approvalProcYn}'/>" style="display:none;">
								<input type="checkbox" name="segCreateTy" value="<c:out value='${mail.segCreateTy}'/>" style="display:none;">
							</td>
							<td>
								<fmt:parseDate var="sendDate" value="${mail.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${sendDt}"/>
							</td>
							<td>
								<c:choose>
									<c:when test="${'000' eq mail.workStatus}">
	 									<a href="javascript:goUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="bold"><c:out value="${mail.taskNm}"/></a>
									</c:when>
									<c:when test="${'204' eq mail.workStatus || '001' eq mail.workStatus}">
	 									<a href="javascript:goUpdateDate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','N');" class="bold"><c:out value="${mail.taskNm}"/></a>
									</c:when>
									<c:when test="${'201' eq mail.workStatus}">
										<a href="javascript:goUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="bold"><c:out value="${mail.taskNm}"/></a>
									</c:when>
									<c:when test="${'202' eq mail.workStatus}">
										<c:choose>
											<c:when test="${'N' eq mail.approvalProcAppYn}">
												<a href="javascript:goUpdate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="bold"><c:out value="${mail.taskNm}"/></a>
											</c:when>
											<c:otherwise>
												<a href="javascript:goUpdateDate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','Y');" class="bold"><c:out value="${mail.taskNm}"/></a>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<a href="javascript:goUpdateDate('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','Y');" class="bold"><c:out value="${mail.taskNm}"/></a>
									</c:otherwise>
								</c:choose>
							</td>
							<td><c:out value='${mail.sendRepeatNm}'/></td>
							<td>
								<c:if test="${'001' eq mail.sendRepeat}">
									<a href="javascript:goSubTaskList('<c:out value='${mail.taskNo}'/>');" class="regular"><c:out value='${mail.mailCnt}'/>건</a>
								</c:if>
								<c:if test="${'000' eq mail.sendRepeat}">
									<c:out value='${mail.mailCnt}'/>건
								</c:if>
							</td>
							<td><c:out value="${mail.campNm}"/></td>
							<td><a href="javascript:goSegInfoMail('<c:out value='${mail.segNo}'/>');" class="regular"><c:out value="${mail.segNm}"/></a></td>
							<td><c:out value="${mail.userNm}"/></td>
							<td><c:out value="${mail.statusNm}"/></td>
							<td>
								<c:choose>
									<%-- 발송대기 --%>
									<c:when test="${'000' eq mail.workStatus}">
										<c:if test="${'000' eq mail.status}">
											<a href="javascript:goAdmit('<c:out value='${mailStatus.index}'/>');" class="medium"><c:out value="${mail.workStatusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne mail.status}">
											<c:out value="${mail.workStatusNm}"/>
										</c:if>
									</c:when>
									<%-- 발송승인/발송중/발송완료/결재진행/결재반려 --%>
									<c:when test="${'001' eq mail.workStatus || '002' eq mail.workStatus || '003' eq mail.workStatus || '202' eq mail.workStatus || '203' eq mail.workStatus}">
										<c:out value="${mail.workStatusNm}"/>
									</c:when>
									<%-- 결재대기 ==> 결재상신 --%>
									<c:when test="${'201' eq mail.workStatus}">
										<c:if test="${'000' eq mail.status}">
											<a href="javascript:popSubmitApproval('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>','<c:out value='${mail.userId}'/>');" class="medium"><c:out value="${mail.workStatusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne mail.status}">
											<c:out value="${mail.workStatusNm}"/>
										</c:if>
									</c:when>
									<%-- 결재진행 : 결재진행(002), 결재반려(203)   --%>
									<c:when test="${'202' eq mail.workStatus || '203' eq mail.workStatus}">
										<c:if test="${'000' eq mail.status}">
											<a href="javascript:popApprovalState('<c:out value='${mail.taskNo}'/>','<c:out value='${mail.subTaskNo}'/>');" class="medium"><c:out value="${mail.workStatusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne mail.status}">
											<c:out value="${mail.workStatusNm}"/>
										</c:if>
									</c:when>
									<%-- 발송승인(001), 발송중(002), 발송완료(003) --%>
									<c:when test="${'001' eq mail.workStatus || '002' eq mail.workStatus || '003' eq mail.workStatus }">
										<c:out value="${mail.workStatusNm}"/>
									</c:when>
									<%-- 결재완료(204)  ==> 발송승인 --%>
									<c:when test="${'204' eq mail.workStatus}">
										<c:if test="${'000' eq mail.status}">
											<a href="javascript:goAdmit('<c:out value='${mailStatus.index}'/>');" class="medium"><c:out value="${mail.workStatusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne mail.status}">
											<c:out value="${mail.workStatusNm}"/>
										</c:if>
									</c:when>
									<%-- 발송실패 --%>
									<c:otherwise>
										<a href="javascript:goFail('<c:out value='${mail.workStatusDtl}'/>');" class="medium"><c:out value="${mail.workStatusNm}"/></a>
									</c:otherwise>
								</c:choose>
							</td>
							<td><c:if test="${'Y' eq mail.approvalLineYn}">해당</c:if></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty mailList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="12" class="no_data">등록된 내용이 없습니다.</td>
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