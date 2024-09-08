<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.21
	*	설명 : 대상자보기(미리보기) 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
function goSearch() {
	$("#divSegInfo").empty();
	$("#segInfoForm input[name='page']").val("1");
	
    var url = "";
    if($("#segInfoForm input[name='createTy']").val() == "003") {
    	url = "<c:url value='/ems/seg/segFileMemberListP.ums'/>";
    } else {
    	url = "<c:url value='/ems/seg/segDbMemberListP.ums'/>";
	}
    
    var param = $("#segInfoForm").serialize();
	$.ajax({
		type : "GET",
		url : url + "?" + param,
		dataType : "html",
		//async: false,
		success : function(pageHtml){
			$("#divSegInfo").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

function goPageNum(page) {
	$("#divSegInfo").empty();
	$("#segInfoForm input[name='page']").val(page);
	
    var url = "";
    if($("#segInfoForm input[name='createTy']").val() == "003") {
    	url = "<c:url value='/ems/seg/segFileMemberListP.ums'/>";
    } else {
    	url = "<c:url value='/ems/seg/segDbMemberListP.ums'/>";
	}
    
    var param = $("#segInfoForm").serialize();
	$.ajax({
		type : "GET",
		url : url + "?" + param,
		dataType : "html",
		//async: false,
		success : function(pageHtml){
			$("#divSegInfo").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}
</script>

<form id="segInfoForm" name="segInfoForm">
<input type="hidden" name="page" value="${segmentVO.page}"/>
<input type="hidden" name="segNo" value="<c:out value='${segmentVO.segNo}'/>">
<input type="hidden" name="separatorChar" value="<c:out value='${segmentVO.separatorChar}'/>">
<input type="hidden" name="segFlPath" value="<c:out value='${segmentVO.segFlPath}'/>">
<input type="hidden" name="createTy" value="<c:out value='${segmentVO.createTy}'/>">
<input type="hidden" name="dbConnNo" value="<c:out value='${segmentVO.dbConnNo}'/>">
<input type="hidden" name="query" value="<c:out value='${segmentVO.query}'/>">
<input type="hidden" name="selectSql" value="<c:out value='${segmentVO.selectSql}'/>">
<input type="hidden" name="fromSql" value="<c:out value='${segmentVO.fromSql}'/>">
<input type="hidden" name="whereSql" value="<c:out value='${segmentVO.whereSql}'/>">
<input type="hidden" name="orderbySql" value="<c:out value='${segmentVO.orderbySql}'/>">
<input type="hidden" name="mergeKey" value="<c:out value='${segmentVO.mergeKey}'/>">

<table class="table_line_outline" style="width:100%;background-color:#ffffff;">
    <tr height="20">
        <td align=left width='120' class="td_title"><spring:message code='SEGTBLTL002'/></td><!-- 발송대상그룹명 -->
        <td height="23" width='480' class="td_body"><c:out value="${segmentVO.segNm}"/></td>
    </tr>
    <tr height="20">
        <td align=left class="td_title"><spring:message code='SEGTBLTL004'/></td><!-- 질의문 -->
        <td class="td_body">
        	<c:choose>
        		<c:when test="${'003' eq segmentVO.createTy}">
	        		<c:out value="${uploadPath}/${segmentVO.segFlPath}"/>
        		</c:when>
        		<c:when test="${'002' eq segmentVO.createTy }">
        			<c:out value="${segmentVO.query}"/>
        		</c:when>
        		<c:otherwise>
        			<c:set var="sql" value="SELECT ${segmentVO.selectSql}"/>
        			<c:set var="sql" value="${sql} FROM ${segmentVO.fromSql}"/>
        			<c:if test="${not empty segmentVO.whereSql && '' ne segmentVO.whereSql}">
        				<c:set var="sql" value="${sql} WHERE ${segmentVO.whereSql}"/>
        			</c:if>
        			<c:if test="${not empty segmentVO.orderbySql && '' ne segmentVO.orderbySql}">
        				<c:set var="sql" value="${sql} ORDER BY ${segmentVO.orderbySql}"/>
        			</c:if>
        		</c:otherwise>
        	</c:choose>
        	<c:out value="${sql}"/>
        </td>
    </tr>
    <tr height="20">
        <td align=left class="td_title"><spring:message code='SEGTBLTL006'/></td><!-- 테스트결과 -->
        <td height="23" class="td_body"><c:out value="${segmentVO.totCnt}"/></td>
    </tr>
    <tr height="20">
        <td align=left class="td_title"><spring:message code='SEGTBLTL030'/></td><!-- 회원 정보 -->
        <td height="23" class="td_body">
        	<c:choose>
	        	<c:when test="${'003' eq segmentVO.createTy || '002' eq segmentVO.createTy}">
		            <Font color='red'>*</FONT> <spring:message code='SEGJSALT015'/><!-- File Group/직접SQL입력은 전체검색만 가능합니다. -->
		            &nbsp;&nbsp;
		            <input type="button" class="btn_style" value="<spring:message code='COMBTN002'/>" onClick="goSearch()"><!-- 검색 -->
	        	</c:when>
	        	<c:otherwise>
	        		<select name="search" class="select">
	        			<c:forTokens items="${segmentVO.mergeKey}" delims="," var="mergeKey" varStatus="keyStatus">
	        				<c:forTokens items="${segmentVO.mergeCol}" delims="," var="mergeCol" varStatus="colStatus">
	        					<c:if test="${keyStatus.index == colStatus.index}">
	        						<option value="<c:out value='${mergeCol}'/>"><c:out value='${mergeKey}'/></option>
	        					</c:if>
	        				</c:forTokens>
	        			</c:forTokens>
	        		</select>
		            &nbsp;&nbsp;
		            <input type="text" name="value" value="" style="width:100;">
		            &nbsp;&nbsp;
		            <input type="button" class="btn_style" value="<spring:message code='COMBTN002'/>" onClick="goSearch()"><!-- 검색 -->
	        	</c:otherwise>
	        </c:choose>
        </td>
    </tr>
    </table>
    <table style="width:100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td height='10'></td></tr>
    <tr>
        <td>
            <!-- 회원 리스트 시작 부분 -->
            <div id="divSegInfo" style="width:100%;height:360px;">
            </div>
            <!-- 회원 리스트 끝 부분 -->
        </td>
    </tr>
    <tr>
        <td align='right' height='30'><input type="button" class="btn_style" value="<spring:message code='COMBTN001'/>" onClick="window.close()"></td><!-- 닫기 -->
    </tr>
    </table>
    </form>

</body>
</html>
