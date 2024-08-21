<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.01.14
	*	설명 :캠페인별 발송 - 메일별 발송목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>

		<div class="btn-wrap">
			<button type="button" class="btn fullblue" onclick="goJoin();">병합분석</button>
		</div>
		
		<!-- btn-wrap// -->
<!-- 		<div class="btn-wrap">
			<button type="button" class="btn big fullblue" onclick="excelDownload();">엑셀다운로드</button>
		</div> -->
		<!-- //btn-wrap -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:auto;">
				<col style="width:8%;">
				<col style="width:8%;">
				<col style="width:6%;">
				<col style="width:12%;">
				<col style="width:9%;">
				<col style="width:9%;">
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
					<th scope="col">메일명</th>
					<th scope="col">메일유형</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">발송자명</th>
					<th scope="col">발송주기</th>
					<th scope="col">등록일자</th>
					<th scope="col">발송일시</th>
					<th scope="col">발송상태</th>
					<th scope="col">발송구분</th>
					<th scope="col">상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(campMailEmsList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${campMailEmsList}" var="campMailEms" varStatus="campMailEmsStatus">
						<tr>
							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - campMailEmsStatus.index}"/></td>
							<td>
								<label><input type="checkbox" name="taskNos" value="<c:out value='${campMailEms.taskNo}'/>"><span></span></label>
								<input type="checkbox" name="subTaskNos" value="<c:out value='${campMailEms.subTaskNo}'/>" style="display:none;">
							</td>
							<c:choose>
								<c:when test="${'000' eq campMailEms.sendRepeat}">
									<td><a href="javascript:;" onclick="goCampMailAnalList('<c:out value="${campMailEms.taskNo}"/>', '<c:out value="${campMailEms.subTaskNo}"/>');" class="bold"><c:out value="${campMailEms.taskNm}"/></a></td>
								</c:when>
								<c:otherwise>
									<td><a href="javascript:;" onclick="goCampMailAnalList('<c:out value="${campMailEms.taskNo}"/>', '<c:out value="${campMailEms.subTaskNo}"/>');" class="bold">[<c:out value="${campMailEms.subTaskNo}"/>차]<c:out value="${campMailEms.taskNm}"/></a></td>
								</c:otherwise>
							</c:choose>
							<td><c:out value="${campMailEms.sendRepeatNm}"/></td>
							<td><c:out value="${campMailEms.segNm}"/></td>
							<td><c:out value="${campMailEms.mailFromNm}"/></td>
							<td>
								<c:choose>
									<c:when test="${'001' eq campMailEms.sendRepeat}">
										<fmt:parseDate var="sendTermStartDate" value="${campMailEms.sendTermStartDt}" pattern="yyyyMMddHHmm"/>
										<fmt:formatDate var="sendTermStartDt" value="${sendTermStartDate}" pattern="yyyy/MM/dd"/>
										<c:choose>
											<c:when test="${'999912312359' eq campMailEms.sendTermEndDt}">
												<c:out value='${sendTermStartDt}'/> ~ 무기한
												<br>주기 : <c:out value='${campMailEms.sendTermLoop}'/> (<c:out value='${campMailEms.sendTermLoopTyNm}'/>)
											</c:when>
											<c:otherwise>
												<fmt:parseDate var="sendTermEndDate" value="${campMailEms.sendTermEndDt}" pattern="yyyyMMddHHmm"/>
												<fmt:formatDate var="sendTermEndDt" value="${sendTermEndDate}" pattern="yyyy/MM/dd"/>
												<c:out value='${sendTermStartDt}'/> ~ <c:out value='${sendTermEndDt}'/>
												<br>주기 : <c:out value='${campMailEms.sendTermLoop}'/>(<c:out value='${campMailEms.sendTermLoopTyNm}'/>)
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<fmt:parseDate var="sendDate" value="${campMailEms.sendDt}" pattern="yyyyMMddHHmm"/>
										<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd hh:mm"/>
										<c:out value='${sendDt}'/>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<fmt:parseDate var="taskRegDate" value="${campMailEms.taskRegDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="taskRegDt" value="${taskRegDate}" pattern="yyyy.MM.dd"/>
								<c:out value='${taskRegDt}'/>
							</td>
							<td>
								<fmt:parseDate var="sendDate" value="${campMailEms.sendDt}" pattern="yyyyMMddHHmm"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value='${sendDt}'/>
							</td>
							<%-- <td><c:out value="${campMailEms.workStatusNm}"/></td> --%>
 							<td>
								<c:choose>
									<%-- 결재대기 --%>
									<c:when test="${'003' eq campMailEms.workStatus}">
										<c:out value="${campMailEms.workStatusNm}"/>
									</c:when>
									<%-- 발송실패 --%>
									<c:otherwise>
										<a href="javascript:goFail('<c:out value='${campMailEms.workStatusDtl}'/>');" class="medium"><c:out value="${campMailEms.workStatusNm}"/></a>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<a href="javascript:popMailHist('<c:out value='${campMailEms.taskNo}'/>', '<c:out value="${campMailEms.subTaskNo}"/>')">
								<c:choose>
									<c:when test="${0 eq campMailEms.rtyNo}">
										본발송
									</c:when>
									<c:otherwise>
										재발송
									</c:otherwise>
								</c:choose>
								</a>
							</td>
							<td><c:out value="${campMailEms.statusNm}"/></td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty campMailEmsList}">
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
<!-- btn-wrap// -->
<div class="btn-wrap" style="margin-top:60px;">
	<button type="button" class="btn fullblue big" onClick="goList()">목록</button>
</div>
