<%--
	/**********************************************************
	*	작성자 : 이혜민
	*	작성일시 : 2022.06.16
	*	설명 : SMS 상세로그 목록
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
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:15%;">
				<col style="width:20%;">
				<col style="width:20%;">
				<col style="width:20%;">
				<col style="width:20%;">
<!-- 				<col style="width:auto;"> -->
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">메세지유형</th>
					<th scope="col">발송일시</th>
					<th scope="col">고객전화번호</th>
					<th scope="col">발송전화번호</th>
					<th scope="col">발송결과</th>
<!-- 					<th scope="col">발송결과내용</th> -->
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(sendLogList) > 0}">
					<c:choose>
						<c:when test="${'N' eq searchVO.searchIsInterface}">
							<c:forEach items="${sendLogList}" var="sendLog" varStatus="logStatus">
								<tr>
									<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - logStatus.index}"/>
										<input type="hidden"  name="keygenHddn" value='<c:out value="${sendLog.keygen}"/>'/>
										<input type="hidden"  name="callStatusNmHddn" value='<c:out value="${sendLog.callStatusNm}"/>'/>
										<input type="hidden"  name="msgBodyHddn" value='<c:out value="${sendLog.msgBody}"/>'/>
									</td>
									<td><c:out value="${sendLog.gubunNm}"/></td>
									<td>
										<fmt:parseDate var="sendDt" value="${sendLog.sendDate}" pattern="yyyyMMddHHmmss"/>
										<fmt:formatDate var="sendDate" value="${sendDt}" pattern="yyyy-MM-dd HH:mm"/>
										<c:out value='${sendDate}'/>
									</td>
									<td><c:out value="${sendLog.destPhone}"/></td>
									<td><c:out value="${sendLog.sendPhone}"/></td>
<%-- 									<td><a href="javascript:;" onclick="goSmsDetail('<c:out value="${sendLog.msgid}"/>', '<c:out value="${sendLog.keygen}"/>', '<c:out value="${sendLog.campNm}"/>', '<c:out value="${sendLog.attachFileList}"/>', '<c:out value="${sendLog.cmid}"/>', this);" class="bold"><c:out value="${sendLog.subject}"/></a></td> --%>
									<td>
										<a href="javascript:;" onclick="goPopRcodeDesc('<c:out value="${logStatus.index}"/>');" >
											<c:choose>
												<c:when test="${'1' eq sendLog.rsltCd}">
													성공
												</c:when>
												<c:otherwise>
													실패
												</c:otherwise>
											</c:choose>
										</a>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach items="${sendLogList}" var="sendLog" varStatus="logStatus">
								<tr>
									<td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - logStatus.index}"/>
										<input type="hidden"  name="keygenHddn" value='<c:out value="${sendLog.keygen}"/>'/>
										<input type="hidden"  name="callStatusNmHddn" value='<c:out value="${sendLog.callStatusNm}"/>'/>
										<input type="hidden"  name="msgBodyHddn" value='<c:out value="${sendLog.msgBody}"/>'/>
									</td>
									<td>
										<c:if test="${'0' eq sendLog.msgType}">SMS</c:if>
										<c:if test="${'5' eq sendLog.msgType}">MMS</c:if>
									</td>
									<td>
										<fmt:parseDate var="sendDt" value="${sendLog.requestTime}" pattern="yyyyMMddHHmmss"/>
										<fmt:formatDate var="sendDate" value="${sendDt}" pattern="yyyy-MM-dd HH:mm"/>
										<c:out value='${sendDate}'/>
									</td>
<%-- 									<td><crypto:decrypt colNm= "PHONE" data="${sendLog.destPhone}"/></td> --%>
									<td><c:out value="${sendLog.destPhone}"/></td>
									<td><c:out value="${sendLog.sendPhone}"/></td>
									<td>
										<a href="javascript:;" onclick="goPopRcodeDesc('<c:out value="${logStatus.index}"/>');" >
											<c:out value="${sendLog.statusNm}"/>
										</a>
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					
				</c:if>

				<c:if test="${empty sendLogList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="6" class="no_data">등록된 내용이 없습니다.</td>
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

