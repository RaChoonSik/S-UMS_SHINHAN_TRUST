<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.30
	*	설명 : 결재 메시지 전달
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=EDGE">
<meta name="Author" content="sict">
<meta name="keywords" content="UMS">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>S-UMS</title>
<%
	int taskNo        = Integer.parseInt(request.getParameter("taskNo")==null?"0":request.getParameter("taskNo"));
	int apprStep      = Integer.parseInt(request.getParameter("apprStep")==null?"0":request.getParameter("apprStep"));
	int totalCount    = Integer.parseInt(request.getParameter("totalCount")==null?"0":request.getParameter("totalCount"));
	String apprUserId = request.getParameter("apprUserId");
	String rsltCd     = request.getParameter("rsltCd");
	String rejectCd   = request.getParameter("rejectCd");
	String taskNm     = request.getParameter("taskNm");
	String mailTitle  = request.getParameter("mailTitle");
	String apprRegId =  request.getParameter("regId");
	String mailTypeNm = request.getParameter("mailTypeNm");
	String actApprUserId = request.getParameter("actApprUserId");
	String alertMessage = "";
	
	//alertMessage += "Task No = " + taskNo + "\\n";
	//alertMessage += "Appr Step = " + apprStep + "\\n";
	//alertMessage += "Appr User Id = " + apprUserId + "\\n";
	//alertMessage += "Mail Name = " + taskNm + "\\n";
	//alertMessage += "Mail Title = " + mailTitle + "\\n";
	
	// 결재요청(상신)
	if("001".equals(rsltCd)) {
		alertMessage += "[" + mailTypeNm +"]결재 메시지 전달\\n\\n";
		alertMessage += "결재자ID : " + apprUserId + "\\n";
		alertMessage += "[이메일 발송 결재 요청] 메일제목 : " + mailTitle + "\\n";
	
	// 결재승인
	} else if("002".equals(rsltCd)) {
		// 요청자
		if(apprUserId == actApprUserId) {
			alertMessage += "[" + mailTypeNm +"]결재 메시지 전달\\n\\n";
			alertMessage += "결재등록자ID : " + apprRegId + "\\n";
			alertMessage += "[이메일 발송 결재완료] 메일제목 : " + mailTitle + "\\n";
		
		// 전결자
		} else {
			alertMessage += "[" + mailTypeNm +"]결재 메시지 전달\\n\\n";
			alertMessage += "결재자ID : " + apprUserId + "\\n";
			alertMessage += "[이메일 발송 전결 결재] 메일제목 : " + mailTitle + "\\n";
		}
	
	// 결재반려
	} else if("003".equals(rsltCd)) {
		// 요청자
		if(apprUserId == actApprUserId) {
			alertMessage += "[" + mailTypeNm +"]결재 메시지 전달\\n\\n";
			alertMessage += "결재등록자ID : " + apprRegId + "\\n";
			alertMessage += "[이메일 발송 결재 반려] 메일제목 : " + mailTitle + "\\n";
		
		// 전결자
		} else {
			alertMessage += "[" + mailTypeNm +"]결재 메시지 전달\\n\\n";
			alertMessage += "결재자ID : " + apprUserId + "\\n";
			alertMessage += "[이메일 발송 전결 반려] 메일제목 : " + mailTitle + "\\n";
		}
	}
	
	
	/*********************************/
	/*  알림 발송 처리는 프로젝트 내부 처리  */
	/*********************************/
%>
<script type="text/javascript">
alert("<%=alertMessage%>");
</script>

<body></body>
</html>
