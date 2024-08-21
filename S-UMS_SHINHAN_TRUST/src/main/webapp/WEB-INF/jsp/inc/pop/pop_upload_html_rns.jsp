<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : HTML 업로드 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_html" class="poplayer pophtmlenroll">
	<div class="inner small">
		<header>
			<h2>HTML 등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<form id="fileUpload" name="fileUpload" method="post" enctype="multipart/form-data">
					<input type="hidden" name="folder" value="html/<c:out value='${NEO_USER_ID}'/>"/>
					<fieldset>
						<legend>HTML 등록</legend>
	
						<div class="fileupload">
							<div class="inputfile" style="width:316px;">
								<input type="hidden" id="rFileName" name="rFileName">
								<input type="file" id="fileUrl" name="fileUrl" class="hide" onchange="fn.fileCheck(this)">
								<input type="text" id="vFileName" name="vFileName" class="upload-name" placeholder="HTML 파일첨부" readonly>
								<span class="filesize"></span>
							</div>
							<label for="fileUrl" class="btn fullpurple" style="width:68px;">첨부</label>
							<button type="button" class="btn fullblack hide" style="width:68px;" onclick="fn.fileReset(this);">삭제</button>
						</div>
						<div class="btn-wrap">
							<button type="button" class="btn big fullpurple" onclick="goHtmlUpload('<c:out value='${NEO_USER_ID}'/>');">등록</button>
							<button type="button" class="btn big" onclick="fn.fileReset(this);fn.popupClose('#popup_html');">취소</button>
						</div>
	
					</fieldset>
				</form>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.fileReset(this);fn.popupClose('#popup_html');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
