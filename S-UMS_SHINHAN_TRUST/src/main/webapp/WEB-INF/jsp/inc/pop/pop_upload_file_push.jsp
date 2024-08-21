<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.27
	*	설명 : PUSH 첨부파일등록 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_file" class="poplayer pophtmlenroll">
	<div class="inner small">
		<header>
			<h2>파일 등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<form id="attachUpload" name="attachUpload" method="post" enctype="multipart/form-data">
					<input type="hidden" name="folder" value="push"/>
					<fieldset>
						<legend>파일 등록</legend>
	
						<div class="fileupload">
							<div class="inputfile" style="width:316px;">
								<input type="file" id="fileUrl2" name="fileUrl2" class="hide" onchange="fn.fileCheck(this)">
								<input type="text" id="upTempAttachPath" name="upTempAttachPath" class="upload-name" placeholder="파일첨부" readonly>
								<span class="filesize"></span>
							</div>
							<label for="fileUrl2" class="btn fullorange" style="width:68px;" id="popFileAttachReady">첨부</label>
							<button type="button" class="btn fullblack hide" style="width:68px;" id="popFileDelete" onclick="fn.fileReset(this);">삭제</button>
						</div>
						<div class="btn-wrap">
							<button type="button" class="btn big fullorange" onclick="attachFileUpload();">등록</button>
							<button type="button" class="btn big" onclick="fn.popupClose('#popup_file');">취소</button>
						</div>
	
					</fieldset>
				</form>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_file');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
