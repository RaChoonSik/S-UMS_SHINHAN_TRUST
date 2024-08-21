<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.19
	*	설명 : 파일 업로드 처리 후 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<c:choose>
	<c:when test="${'Success' eq result}">
		<c:choose>
			<c:when test="${'select' eq uploadVO.inputType}">
	            <script type="text/javascript">
	                window.opener.appendOption(window.opener.document.<c:out value="${uploadVO.formName}"/>, "<c:out value='${oldFileName}'/>", "<c:out value='${newFileName}'/>", "<c:out value='${uploadVO.rFileName}'/>", "<c:out value='${uploadVO.vFileName}'/>");
	                alert("<spring:message code='COMJSALT008'/>");		// 등록 성공
	                self.close();
	            </script>
			</c:when>
			<c:otherwise>
	            <script type="text/javascript">
	            	$("#<c:out value='${uploadVO.rFileName}'/>", opener.document).val( "<c:out value='${newFileName}'/>" );
	            	$("#<c:out value='${uploadVO.vFileName}'/>", opener.document).val( "<c:out value='${oldFileName}'/>" );
	                //window.opener.document.<c:out value="${uploadVO.formName}"/>.<c:out value="${uploadVO.rFileName}"/>.value = "<c:out value='${newFileName}'/>";
	                //window.opener.document.<c:out value="${uploadVO.formName}"/>.<c:out value="${uploadVO.vFileName}"/>.value = "<c:out value='${oldFileName}'/>";
	                alert("<spring:message code='COMJSALT008'/>");		// 등록 성공
	                self.close();
	            </script>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
        <script type="text/javascript">
            alert("<spring:message code='COMJSALT009'/>");		// 등록 실패
            window.history.go(-1);
        </script>
	</c:otherwise>
</c:choose>

</body>
</html>
