<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.24
	*	설명 : 메일작성 환경설정 팝업
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_setting" class="poplayer popsetting">
	<div class="inner">
		<header>
			<h2>환경설정</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title">메일헤더 정보</h3>
					</div>
					
					<div class="table-area">
						<table>
							<caption></caption>
							<colgroup>
								<col style="width:105px">
								<col style="width:225px">
								<col style="width:105px">
								<col style="width:225px">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">발송자명</th>
									<td>
										<c:if test="${not empty userInfo}">
											<input type="text" id="sname" name="sname" value="<c:out value='${userInfo.mailFromNm}'/>" placeholder="발송자명을 입력하세요.">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<input type="text" id="sname" name="sname" value="<c:out value='${mailInfo.mailFromNm}'/>" placeholder="발송자명을 입력하세요.">
										</c:if>
									</td>
									<th scope="row">발송자 이메일</th>
									<td>
										<c:if test="${not empty userInfo}">
											<input type="text" id="smail" name="smail" value="<c:out value='${userInfo.mailFromEm}'/>" placeholder="발송자 이메일을 입력하세요.">
										</c:if>
										<c:if test="${not empty mailInfo}">
											<input type="text" id="smail" name="smail" value="<c:out value='${mailInfo.mailFromEm}'/>" placeholder="발송자 이메일을 입력하세요.">
										</c:if>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				
				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullpurple" onclick="popSettingSave();">등록</button>
					<button type="button" class="btn big" onclick="fn.popupClose('#popup_setting');">닫기</button>
				</div>
				<!-- //btn-wrap -->
				
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_setting');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
