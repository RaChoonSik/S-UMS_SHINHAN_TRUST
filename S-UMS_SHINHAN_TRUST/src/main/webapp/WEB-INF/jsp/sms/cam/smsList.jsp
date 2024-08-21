<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.10.19
	*	설명 : 문자발송 목록 조회
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
			<button type="button" class="btn fullgreen plus" onclick="goSmsAdd();">신규등록</button>
			<button type="button" class="btn" onclick="goSmsCopy();">복사</button>
			<button type="button" class="btn" onclick="goSmsDelete();">삭제</button>
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
					<th scope="col">문자명</th>
					<th scope="col">전송유형</th>
					<th scope="col">캠페인명</th>
					<th scope="col">수신자그룹</th>
					<th scope="col">사용자명</th>
					<th scope="col">상태</th>
					<th scope="col">발송상태</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty smsList}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${smsList}" var="sms" varStatus="smsStatus">
						<tr>
							<td>${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - smsStatus.index}</td>
							<td>
								<c:choose>
									<c:when test="${'000' ne sms.smsStatus}">
										<label><input type="checkbox" name="selKeygens" value="<c:out value='${sms.msgid}'/>" onclick="return goDeleteClick();" disabled><span></span></label>
									</c:when>
									<c:otherwise>
										<label><input type="checkbox" name="selKeygens" onclick="goKeygen('<c:out value='${smsStatus.index}'/>')" value="<c:out value='${sms.msgid}'/>:<c:out value='${sms.keygen}'/>"><span></span></label>
									</c:otherwise>
								</c:choose> 
 								<input type="checkbox" name="selStatus" value="<c:out value='${sms.status}'/>" style="display:none;">
 								<input type="checkbox" name="selSmsStatus" value="<c:out value='${sms.smsStatus}'/>" style="display:none;">
							</td>
							<td>
								<fmt:parseDate var="sendDate" value="${sms.sendDate}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/> 
								<c:out value="${sendDt}"/>
							</td>
							<td>
								<a href="javascript:goUpdate('<c:out value='${sms.msgid}'/>','<c:out value='${sms.keygen}'/>');" class="bold"><c:out value="${sms.taskNm}"/></a>
							</td>
							<td><c:out value="${sms.gubunNm}"/></td>
							<td><c:out value="${sms.campNm}"/></td>
							<td><a href="javascript:goSegInfoMail('<c:out value='${sms.segNo}'/>');" class="regular"><c:out value="${sms.segNm}"/></a></td>
							<td><c:out value="${sms.userNm}"/></td>
							<td><c:out value="${sms.smsStatusNm}"/></td>
							<td>
								<c:choose>
									<%-- 발송대기 --%>
									<c:when test="${'000' eq sms.smsStatus}">
										<c:if test="${'000' eq sms.status}">
											<a href="javascript:goAdmit('<c:out value='${smsStatus.index}'/>');" class="medium"><c:out value="${sms.statusNm}"/></a>
										</c:if>
										<c:if test="${'000' ne sms.status}">
											<c:out value="${sms.statusNm}"/>
										</c:if>
									</c:when>
									<%-- 발송승인/발송중/발송완료/결재진행/결재반려 --%>
									<c:when test="${'000' ne sms.smsStatus}">
										<c:out value="${sms.statusNm}"/>
									</c:when>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty smsList}">
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
