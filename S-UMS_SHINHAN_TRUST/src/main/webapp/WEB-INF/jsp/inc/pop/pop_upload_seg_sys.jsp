<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.20
	*	설명 : 수신자그룹 파일등록 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_file_seg" class="poplayer pophtmlenroll">
	<div class="inner small"><!-- width 조정 필요시 style="max-width:숫자px"로 넣어주세요. -->
		<header>
			<h2>파일 등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<form id="fileUploadSeg" name="fileUploadSeg" method="post" enctype="multipart/form-data">
					<input type="hidden" name="folder" value="addressfile/<c:out value='${NEO_USER_ID}'/>"/>
					<fieldset>
						<legend>파일 등록</legend>
	
						<div class="fileupload">
							<div class="inputfile" style="width:316px;">
								<input type="hidden" id="upSegFlPath" name="upSegFlPath"/>
								<input type="file" id="fileUrlSeg" name="fileUrlSeg" class="hide" onchange="fn.fileCheck(this)">
								<input type="text" id="upTempFlPath" name="upTempFlPath" class="upload-name" placeholder="파일첨부" readonly>
								<span class="filesize"></span>
							</div>
							<label for="fileUrlSeg" class="btn fullred" style="width:68px;">첨부</label>
							<button type="button" class="btn fullblack hide" style="width:68px;" onclick="fn.fileReset(this);">삭제</button>
						</div>
						<div class="btn-wrap">
							<button type="button" class="btn big fullred" onclick="addressUpload();">등록</button>
							<button type="button" class="btn big" onclick="fn.fileReset(this);fn.popupClose('#popup_file_seg');">취소</button>
						</div>
	
					</fieldset>
				</form>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.fileReset(this);fn.popupClose('#popup_file_seg');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
