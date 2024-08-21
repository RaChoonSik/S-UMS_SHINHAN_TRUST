<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.19
	*	설명 : 파일 업로드 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
function closeWin() {
	window.close();
}

function uploadFile() {
	var extParam = "<c:out value='${upload.ext}'/>";
	var fileFormat = document.fileUpload.fileUrl.value;
	var lastFileName = fileFormat.substring(fileFormat.lastIndexOf("\\")+1);

	var ext = fileFormat.substring(fileFormat.lastIndexOf(".")+1); // 확장자 체크
	
	if((extParam != null && extParam != "null" && extParam != "") && extParam.toLowerCase().indexOf(ext.toLowerCase()) == -1) {
		alert(extParam+'<spring:message code="COMJSALT005"/>');		// 올바른 확장자가 아닙니다. 오로지 파일만 업로드할 수 있습니다.
		return;
	}
	
	if ( fileFormat == '' || fileFormat.length == 0) {
		alert('<spring:message code="COMJSALT006"/>');		// 업로드할 파일을 선택해 주세요.
		return;
	} else {
		for(i = 0; i < window.opener.document.<c:out value="${upload.formName}"/>.<c:out value="${upload.vFileName}"/>.length; i++) {
			if(window.opener.document.<c:out value="${upload.formName}"/>.<c:out value="${upload.vFileName}"/>.options[i].value == lastFileName) {
				alert("<spring:message code="COMJSALT007"/>");		// 같은 파일이 존재합니다.
				return;
			}
		}
	}
	
	document.fileUpload.action = "<c:url value='/com/upload.ums'/>";
	document.fileUpload.submit();
}


</script>

<form name="fileUpload" method="post" enctype="multipart/form-data">
<input type="hidden" name="folder" value="<c:out value='${upload.folder}'/>"/>
<input type="hidden" name="title" value="<c:out value='${upload.vFileName}'/>"/>
<input type="hidden" name="charset" value="<c:out value='${upload.charset}'/>"/>
<input type="hidden" name="formName" value="<c:out value='${upload.formName}'/>"/>
<input type="hidden" name="rFileName" value="<c:out value='${upload.rFileName}'/>"/>
<input type="hidden" name="vFileName" value="<c:out value='${upload.vFileName}'/>"/>
<input type="hidden" name="inputType" value="<c:out value='${upload.inputType}'/>"/>

<table width="630" border="0" cellspacing="1" cellpadding="0" class="">
  <tr>
    <td height="30" class="td_title"><b><center><spring:message code="CAMTBLTL030"/></b></td><!-- 파일첨부 -->
  </tr>
  <tr>
    <td class="td_body" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="10"></td>
        </tr>
        <tr>
          <td align="center">
            <table width="601" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                  <table width="100%" border="0" cellspacing="1" cellpadding="0" class="">
                    <tr>
                      <td bgcolor="E7E3D9" height="23" class="td_title"> FILE PATH</td>
                      <td height="23" bgcolor="F3F1EC"  class="td_body">
                        <input type="file" name="fileUrl" class="input" style="width:500;">
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td height="10"></td>
              </tr>
              <tr>
                <td align="center">
                    <input type="button" class="btn_style" value="<spring:message code="COMBTN011"/>" onClick="uploadFile()">&nbsp;<!-- 확인 -->
                    <input type="button" class="btn_style" value="<spring:message code="COMBTN001"/>" onClick="closeWin()"><!-- 닫기 -->
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="10"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</FORM>

</body>
</html>
