<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.19
	*	설명 : 샘플 파일 다운로드 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
function fileDownload(downType) {
	$("#downType").val(downType);
	$("#fileDownForm").attr("target","");
    $("#fileDownForm").attr("action", "<c:url value='/com/down.ums'/>");
    $("#fileDownForm").submit();
}

function closeWin() {
	window.close();
}
</script>

<form id="fileDownForm" name="fileDownForm" method="post">
<input type="hidden" id="divVal" name="divVal" />
<input type="hidden" id="seqNo" name="seqNo" value="0"/>
<input type="hidden" id="downType" name="downType"/>
<div class="popWrap">
	<h1>샘플 다운로드</h1>
	<div class="inWrap fileD">
		<input type="button" class="btn_style" onclick="fileDownload('003');" value="메일샘플" />
	</div>
	<div class="btnR">
		<input type="button" class="btn_typeG" value="닫기" onClick="closeWin()">
	</div>
</div>
</form>
<iframe name="iFrmDown" width='0' height='0'></iframe>
</body>
</html>
