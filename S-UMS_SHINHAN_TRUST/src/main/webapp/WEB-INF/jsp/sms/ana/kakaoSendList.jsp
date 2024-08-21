<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.10.26
	*	설명 : 통계분석 카카오알림 분석 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

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
					<col style="width:10%;">
					<col style="width:auto;">
					<!-- <col style="width:10%;"> -->
					<col style="width:10%;">
					<col style="width:10%;">
					<col style="width:6%;">
					<col style="width:6%;">
					<col style="width:6%;">
					<col style="width:7%;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col" rowspan="2">NO</th>
						<th scope="col" rowspan="2">발송일시</th>
						<th scope="col" rowspan="2">알림톡명</th>
						<!-- <th scope="col" rowspan="2">템플릿명</th> -->
						<th scope="col" rowspan="2">캠페인명</th>
						<th scope="col" rowspan="2">사용자그룹</th>
						<th scope="col" rowspan="2">유효성</th>
						<th scope="col" colspan="2" style="border-bottom:none;">알림톡</th>
						<th scope="col" colspan="2" style="border-bottom:none;">SMS</th>
						<th scope="col" rowspan="2">총 건수</th>
					</tr>
					<tr>
						<th scope="col">성공</th>
						<th scope="col">실패</th>
						<th scope="col">성공</th>
						<th scope="col">실패</th>
					</tr>
				</thead>
				<tbody>
					<!-- 데이터가 있을 경우// -->
				<c:if test="${fn:length(kakaoSendList) > 0}">
					<c:forEach items="${kakaoSendList}" var="kakao" varStatus="status">
						<tr>
 							<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - status.index}"/></td>
							<td>
								<fmt:parseDate var="sendDate" value="${kakao.sendDate}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy.MM.dd HH:mm"/>
								<c:out value="${sendDt}"/>
							</td>
 							<td><a href="javascript:goKakaoAnalList('<c:out value='${kakao.msgid}'/>','<c:out value='${kakao.keygen}'/>');" class="bold"><c:out value="${kakao.taskNm}"/></a></td>
							<td><c:out value='${kakao.tempNm}'/></td>
							<%-- <td><c:out value='${kakao.campNm}'/></td> --%>
							<td><c:out value='${kakao.deptNm}'/></td>
							<td><span><c:out value='${kakao.validYn}'/></span></td>
							<c:if test="${kakao.succCnt == 0}">
								<td><c:out value='${kakao.succCnt}'/></td>
							</c:if>
							<c:if test="${kakao.succCnt > 0}">
								<td><span onclick="goKakaoSendList('<c:out value='${kakao.msgid}'/>','<c:out value='${kakao.keygen}'/>','1','N')" style="cursor: pointer;"><c:out value='${kakao.succCnt}'/></span></td>
							</c:if>
							<c:if test="${kakao.failCnt == 0}">
								<td><c:out value='${kakao.failCnt}'/></td>
							</c:if>
							<c:if test="${kakao.failCnt > 0}">
								<td><span class="color-red" onclick="goKakaoSendList('<c:out value='${kakao.msgid}'/>','<c:out value='${kakao.keygen}'/>','0','N')" style="cursor: pointer;"><c:out value='${kakao.failCnt}'/></span></td>
							</c:if>
							<c:if test="${kakao.succSms == 0}">
								<td><c:out value='${kakao.succSms}'/></td>
							</c:if>
							<c:if test="${kakao.succSms> 0}">
								<td><span onclick="goKakaoSendList('<c:out value='${kakao.msgid}'/>','<c:out value='${kakao.keygen}'/>','1','Y')" style="cursor: pointer;"><c:out value='${kakao.succSms}'/></span></td>
							</c:if>
							<c:if test="${kakao.failSms == 0}">
								<td><c:out value='${kakao.failSms}'/></td>
							</c:if>
							<c:if test="${kakao.failSms > 0}">
								<td><span class="color-red" onclick="goKakaoSendList('<c:out value='${kakao.msgid}'/>','<c:out value='${kakao.keygen}'/>','0','Y')" style="cursor: pointer;"><c:out value='${kakao.failSms}'/></span></td>	
							</c:if>
							<td><c:out value='${kakao.sendTotCnt}'/></td>
						</tr>
					</c:forEach>
				</c:if>

				<c:if test="${empty kakaoSendList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="11" class="no_data">등록된 내용이 없습니다.</td>
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
<div class="btn-wrap" style="margin-top:60px;">
	<button type="button" class="btn big fullgreen" onclick="goList();">목록</button>
</div>