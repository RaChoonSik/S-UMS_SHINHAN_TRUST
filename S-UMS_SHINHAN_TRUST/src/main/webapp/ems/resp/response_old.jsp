<%------------------------------------------------------------------
 *
 * 프로그램명	: response.jsp
 * Version		: 1.0
 * 작성일		: 2004/11/11
 * 작성자		: 오범석
 * 수정일		:
 * 수정자		:
 * 설명			: 수신 응답 로그 jsp
 *
 * 프로젝트명	: MPv3
 *
--------------------------------------------------------------------%>
<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="java.io.*,java.util.*,java.sql.*,java.text.*,java.rmi.RemoteException,com.mp.util.*,com.mp.exception.*, java.net.*" %>
<jsp:useBean id="util" class="com.mp.util.InitApp" scope="application" />
<jsp:useBean id="resp" class="com.mp.util.RespLog" scope="application" />
<%
	String MNAME = "/resp/response.jsp";

	String qry_str = request.getQueryString();
	//System.out.println("수신정보 : " + qry_str);
	
	qry_str = qry_str.replaceAll("&&","|");
	//System.out.println("변환(&&>|) : " + qry_str);

	qry_str = util.getDate(Code.TM_YMDHM) + "|" + qry_str;
	System.out.println("수신확인 : " + qry_str);
	System.out.println("");
	
	resp.resp_logwriter(qry_str);
%>
OK
