<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.04
	*	설명 : 통계분석 고객별 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
// 검색 버튼 클릭시
function goSearch() {
	$("#searchForm").attr("action","<c:url value='/ems/ana/logListP.ums'/>").submit();
}

// 초기화 버튼 클릭시
function goReset() {
	$("#searchCustId").val("");
	$("#searchCustEm").val("");
	$("#searchCustNm").val("");
	$("#searchKind").val("000");
}

// Excel Download 클릭시
function goExcel() {
	$("#searchForm").attr("target","iFrmExcel").attr("action","<c:url value='/ems/ana/logExcelList.ums'/>").submit();
}


// 페이지 이동
function goPageNum(page) {
	$("#page").val(page);
	$("#searchForm").attr("action","<c:url value='/ems/ana/logListP.ums'/>").submit();
}
</script>

<form id="searchForm" name="searchForm" method="post">
<input type="hidden" id="page" name="page" value="<c:out value='${sendLogVO.page}'/>">
<input type="hidden" id="taskNo" name="taskNo" value="<c:out value='${sendLogVO.taskNo}'/>">
<input type="hidden" id="subTaskNo" name="subTaskNo" value="<c:out value='${sendLogVO.subTaskNo}'/>">

<table width="1000" border="0" cellspacing="1" cellpadding="0" class="table_line_outline">
	<tr>
		<td class="td_title" width="10%"><spring:message code='ANATBLLB003'/></td><!-- ID -->
		<td class="td_body" width="22%">
			<input type="text" style="border:1px solid #c0c0c0;" size="20" maxlength="20" id="searchCustId" name="searchCustId" value="<c:out value='${sendLogVO.searchCustId}'/>">
		</td>
		<td class="td_title" width="10%"><spring:message code='ANATBLLB004'/></td><!-- 이메일 -->
		<td class="td_body" width="22%">
			<input type="text" style="border:1px solid #c0c0c0;" size="20" maxlength="20" id="searchCustEm" name="searchCustEm" value="<c:out value='${sendLogVO.searchCustEm}'/>">
		</td>
		<td class="td_title" width="10%"><spring:message code='ANATBLLB005'/></td><!-- 고객명 -->
		<td class="td_body" width="22%">
			<input type="text"  style="border:1px solid #c0c0c0;" size="20" maxlength="20" id="searchCustNm" name="searchCustNm" value="<c:out value='${sendLogVO.searchCustNm}'/>">
		</td>
	</tr>
	<tr>
		<td class="td_title"><spring:message code='ANATBLLB002'/></td><!-- 검색유형 -->
		<td class="td_body" colspan=5>
			<select id="searchKind" name="searchKind">
				<option value="000"<c:if test="${'000' eq sendLogVO.searchKind}"> selected</c:if>>::: <spring:message code='ANATBLTL013'/> :::</option><!-- 검색유형 선택 -->
				<option value="001"<c:if test="${'001' eq sendLogVO.searchKind}"> selected</c:if>><spring:message code='ANATBLTL014'/></option><!-- 발송성공 -->
				<option value="002"<c:if test="${'002' eq sendLogVO.searchKind}"> selected</c:if>><spring:message code='ANATBLTL015'/></option><!-- 발송실패 -->
				<option value="003"<c:if test="${'003' eq sendLogVO.searchKind}"> selected</c:if>><spring:message code='ANATBLTL019'/></option><!-- 수신 -->
				<option value="004"<c:if test="${'004' eq sendLogVO.searchKind}"> selected</c:if>><spring:message code='ANATBLTL020'/></option><!-- 미수신 -->
				<option value="005"<c:if test="${'005' eq sendLogVO.searchKind}"> selected</c:if>>수신거부</option>
				<option value="006"<c:if test="${'006' eq sendLogVO.searchKind}"> selected</c:if>>수신허용</option>
			</select>
			&nbsp;&nbsp;&nbsp;
			<input type="button" value="<spring:message code='COMBTN002'/>" onClick="goSearch()" class="btn_typeC">&nbsp;<!-- 검색 -->
			<input type="button" value="<spring:message code='COMBTN003'/>" onClick="goReset()" class="btn_style">&nbsp;<!-- 초기화 -->
			<input type="button" value="Excel Download" onClick="goExcel()" class="btn_style">
		</td>
	</tr>
</table>
</form>

<table border="0" cellspacing="0" cellpadding="0" width="770">
	<tr height="20">
		<td align="right">&nbsp;</td>
	</tr>
</table>

<!-- 목록 -->
<table width="980"  height="200" border="1" cellspacing="0" cellpadding="1" class="table_line_outline">
	<tr>
		<td class="td_line" colspan="10"></td>
	</tr>
	<tr class="tr_head" align="center">
		<td width="10%"><spring:message code='ANATBLLB003'/></td>
		<td width="13%"><spring:message code='ANATBLLB004'/></td>
		<td width="10%"><spring:message code='ANATBLLB005'/></td>
		<td width="10%"><spring:message code='ANATBLLB006'/></td>
		<td width="13%"><spring:message code='ANATBLLB007'/></td>
		<td width="10%"><spring:message code='ANATBLLB008'/></td>
		<td width="10%">수신거부</td>
		<td width="13%"><spring:message code='ANATBLLB009'/></td>
		<td width="20%">Message</td>
	</tr>
	<c:if test="${fn:length(custList) > 0}">
		<c:forEach items="${custList}" var="cust">
			<tr class="tr_body">
				<td align="center"><c:out value='${cust.custId}'/></td>
				<td align="left"><c:out value='${cust.custEm}'/></td>
				<td align="center"><c:out value='${cust.custNm}'/></td>
				<td align="center">
					<c:choose>
						<c:when test="${'000' eq cust.sendRcode}">
							<spring:message code='ANATBLTL017'/><!-- 발송성공 -->
						</c:when>
						<c:otherwise>
							<spring:message code='ANATBLTL018'/><!-- 발송실패 -->
						</c:otherwise>
					</c:choose>
				</td>
				<td align="center">
					<c:out value='${cust.sendDt}'/>
				</td>
				<td align="center">
					<c:choose>
						<c:when test="${empty cust.openDt}">미확인</c:when>
						<c:otherwise>수신확인</c:otherwise>
					</c:choose>
				</td>
				<td align="center">
					<c:choose>
						<c:when test="${'Y' eq cust.deniedType}">수신거부</c:when>
						<c:otherwise>수신허용</c:otherwise>
					</c:choose>
				</td>
				<td align="center">
					<c:out value="${cust.openDt}"/>
				</td>
				<td align="center"><c:out value='${cust.sendMsg}'/></td>
			</tr>
		
		</c:forEach>
	</c:if>
	<c:set var="emptyCnt" value="${sendLogVO.rows - fn:length(custList)}"/>
	<c:if test="${emptyCnt > 0}">
		<c:forEach var="i" begin="1" end="${emptyCnt}">
			<tr class="tr_body">
				<td align="center">&nbsp;</td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>
				<td align="center"></td>
			</tr>
		</c:forEach>
	</c:if>
</table>

<table width="980" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="white" height="30">
		<td align="center" colspan="10">
			${pageUtil.pageHtml}
		</td>
	</tr>
</table>

<div style="width:0px;height:0px;display:none;">
<iframe id="iFrmExcel" name="iFrmExcel" style="width:0px;height:0px;"></iframe>
</div>

</body>
</html>
