<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko">
<head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.oreilly.servlet.MultipartRequest,com.oreilly.servlet.multipart.DefaultFileRenamePolicy,java.util.*,java.io.*,java.text.*" %>
<%@ page import="java.sql.*" %>
<%
	request.setCharacterEncoding("utf-8");
	String realFolder = "";
	String filename1 = "";
	int maxSize = 1024*1024*5;
	String encType = "utf-8";
	String savefile = "img";
	ServletContext scontext = pageContext.getServletContext();
	realFolder = scontext.getRealPath(savefile)+"/upload";
 
	try {
		MultipartRequest multi=new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());

		Enumeration<?> files = multi.getFileNames();
		String file1 = (String)files.nextElement();
		filename1 = multi.getFilesystemName(file1);
	} catch(Exception e) {
		e.printStackTrace();
	}

	String now = new SimpleDateFormat("yyyyMMddHmsS").format(new java.util.Date());  //현재시간
	int i = -1;
	i = filename1.lastIndexOf("."); // 파일 확장자 위치
	String realFileName = now + filename1.substring(i, filename1.length());  //현재시간과 확장자 합치기

	File oldFile = new File(realFolder +"/"+ filename1);
	File newFile = new File(realFolder +"/"+ realFileName); 

	oldFile.renameTo(newFile); // 파일명 변경
 
	String fullpath = realFolder + "/" + filename1;
%>
<script>
	 window.opener.parent.pasteHTML('<%=realFileName%>');   
	 window.close();
</script>
