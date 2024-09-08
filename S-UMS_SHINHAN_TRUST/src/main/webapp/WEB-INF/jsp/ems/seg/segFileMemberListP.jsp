<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.18
	*	설명 : 파일에서 대상자 추출
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>
<script type="text/javascript">
$(document).ready(function(){
	if(parent) {
		var totCount = "<c:out value='${totCount}'/>";
		$("#totCnt").val(totCount);
		$("#txtTotCnt").html(totCount + "명");
		
		var mergeKey = "<c:out value='${mergeKey}'/>";
		$("#mergeKey").val(mergeKey);
		$("#mergeCol").val(mergeKey);
	} else {
		var totCount = "<c:out value='${totCount}'/>";
		$("#totCnt",parent.document).val(totCount);
		$("#txtTotCnt",parent.document).html(totCount + "명");
		
		var mergeKey = "<c:out value='${mergeKey}'/>";
		$("#mergeKey",parent.document).val(mergeKey);
		$("#mergeCol",parent.document).val(mergeKey);
	}
});
</script>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${totCount}"/></em></span>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<!-- 데이터가 있을 경우// -->
			<c:if test="${'Success' eq result }">
				<thead>
					<tr>
						<c:forEach items="${memAlias}" var="memTitle">
							<th scope="col"><c:out value="${memTitle}"/></th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${memList}" var="member">
						<tr>
							<c:forEach items="${memAlias}" var="memTitle">
								<c:forEach items="${member}" var="mem">
									<c:if test="${memTitle eq mem.key}">
										<td><crypto:decrypt colNm="${mem.key}" data="${mem.value}"/></td>
									</c:if>
								</c:forEach>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</c:if>
			<!-- //데이터가 있을 경우 -->

			<!-- 데이터가 없을 경우// -->
			<c:if test="${'Fail' eq result}">
				<tr>
					<td colspan="6" class="no_data">
						<font color='red'>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						* Error!!<BR><BR>
						<c:set var="LINE"/>
						<c:set var="Error"/>
						<c:forEach items="${memList}" var="member">
							<c:forEach items="${member}" var="mem">
								<c:forEach items="${mem.key}" var="item">
									<c:if test="${'LINE' eq item}">
										<c:set var="LINE" value="${mem.value}"/>
									</c:if>
									<c:if test="${'Error' eq item}">
										<c:set var="Error" value="${mem.value}"/>
									</c:if>
								</c:forEach>
							</c:forEach>
						</c:forEach>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						* Line : <c:out value="${LINE}"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						MSG : <c:out value="${Error}"/>
						</font>
					</td>
				</tr>
			</c:if>
			<!-- //데이터가 없을 경우 -->
		</table>
	</div>
</div>
<!-- //목록 -->

<!-- 페이징// -->
<div class="paging">
	${pageUtil.pageHtml}
</div>
<!-- //페이징 -->
