<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.27
	*	설명 : PUSH 발송 목록 조회
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
			<button type="button" class="btn fullorange plus" onclick="goPushAdd();">신규등록</button>
			<button type="button" class="btn" onclick="goPushCopy();">복사</button>
			<button type="button" class="btn" onclick="goPushDelete();">삭제</button>
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
				<col style="width:auto;">
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
 					<th scope="col">
						<label><input type="checkbox" name="isAll" onclick="goAll();"><span></span></label>
					</th> 
					<th scope="col">예약일시</th>
					<th scope="col">PUSH명</th>
					<th scope="col">발송유형</th>
					<th scope="col">캠페인명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">상태</th>
					<th scope="col">발송상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty pushList}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${pushList}" var="push" varStatus="pushStatus">
						<tr>
							<td>${pageUtil.totalRow - (pageUtil.currPage - 1)*pageUtil.pageRow - pushStatus.index}</td>
							<td>
								<c:choose>
									<c:when test="${'000' ne push.status}">
										<label><input type="checkbox" name="pushmessageIds" value="<c:out value='${push.pushmessageId}'/>" onclick="return goDeleteClick();" disabled><span></span></label>
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="pushmessageIds" onclick="goPushmessageId('<c:out value='${pushStatus.index}'/>')" value="<c:out value='${push.pushmessageId}'/>"><span></span></label>
									</c:otherwise>
								</c:choose> 
 								<input type="checkbox" name="selStatus" value="<c:out value='${push.status}'/>" style="display:none;">
 								<input type="checkbox" name="selWorkStatus" value="<c:out value='${push.workStatus}'/>" style="display:none;">
							</td> 
							<td>
								<fmt:parseDate var="sendDate" value="${push.sendDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${sendDt}"/>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${push.pushmessageId}'/>');" class="bold"><c:out value="${push.pushName}"/></a>
							</td>
							<td><c:out value="${push.pushGubunNm}"/></td>
							<td><c:out value="${push.campNm}"/></td>
							<td><a href="javascript:goSegInfoMail('<c:out value='${push.segNo}'/>');" class="regular"><c:out value="${push.segNm}"/></a></td>
							<td><c:out value="${push.userNm}"/></td>
							<td><c:out value="${push.statusNm}"/></td>
							<td>
								<c:choose>
									<%-- 발송대기 --%>
									<c:when test="${'000' ne push.status}">
										<c:out value="${push.workStatusNm}"/>
									</c:when>
									<c:when test="${'000' eq push.status}">
										<c:if test="${'000' eq push.workStatus}">
											<a href="javascript:goAdmit('<c:out value='${pushStatus.index}'/>');" class="medium"><c:out value="${push.workStatusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne push.workStatus}">
											<c:out value="${push.workStatusNm}"/>
										</c:if>
									</c:when>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty pushList}">
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
