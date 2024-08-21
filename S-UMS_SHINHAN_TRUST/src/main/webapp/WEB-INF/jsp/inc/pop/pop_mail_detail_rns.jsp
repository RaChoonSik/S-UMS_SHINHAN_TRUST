<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.24
	*	설명 : 메일 상세 정보  
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<div id="popup_mail_detail_rns" class="poplayer popmaildetail">
	<div class="inner">
		<header>
			<h2>메일 상세 정보</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<!-- 조회// -->
				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">조회</h3>
					</div>
					
						<div class="table-area">
							<table>
								<caption>상세 정보</caption>
								<colgroup>
									<col style="width:105px;">
									<col style="width:auto;">
									<col style="width:105px;">
									<col style="width:225px;">
								</colgroup>
								<tbody>
									<tr>
										<th scope="row">발송자정보</th>
										<td id="popSnm"></td>
										<th scope="row">서비스항목</th>
										<td id="popTnm"></td>
									</tr>
									<tr>
										<th scope="row">메일제목</th>
										<td colspan="3" id="popSubject"></td>
									</tr>
									<tr>
										<th scope="row">등록일시</th>
										<td id="popCdate"> </td>
										<th scope="row">발송일시</th>
										<td id="popSdate"></td>
									</tr>
									<tr>
										<th scope="row">발송그룹</th>
										<td colspan="3" id="popDeptNm"></td>
									</tr>
									<tr>
										<th scope="row">발송템플릿</th>
										<!-- <td colspan="3" id="popContents"> -->
										<td colspan="3">
											<div id="popContents" class="poppreviewtemplate previewbox"></div>
										</td>
									</tr>
									<tr>
										<th scope="row">첨부파일</th>
										<td colspan="3" id="popAttchCnt">(파일 갯수 : 0개)</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				<!-- 조회// -->

				<form id="mailDetailForm" name="mailDetailForm" method="post">
					<input type="hidden" name="page" value="1">
					<input type="hidden" name="mid" value="0">
				</form>
				<!-- 목록&페이징// -->
				<div id="mailSendResult"></div>
				<!-- //목록&페이징 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_mail_detail_rns');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>

