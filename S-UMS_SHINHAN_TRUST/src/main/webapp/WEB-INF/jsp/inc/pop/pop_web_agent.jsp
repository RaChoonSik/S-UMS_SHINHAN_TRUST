<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.24
	*	설명 : 메일작성 환경설정 팝업
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_web_agent" class="poplayer popregisteragent"><!-- id값 수정 가능 -->
	<div class="inner small">
		<header>
			<h2>웹에이전트 등록</h2>
		</header>

		<div class="popcont">
			<div class="cont">
				<!-- 팝업내용 -->
				<p class="txt-black tac">
					Web Agent를 이용하여 웹페이지나 CGI, ASP, Servlet, JSP 등으로<br> 
  					부터 정보를 추출하여 Contents를 동적으로 생성합니다.
				</p>
				<c:if test="${empty webAgent}">
					<input type="text" id="webAgentUrl" name="webAgentUrl" placeholder="URL을 입력하세요.">
				</c:if>
				<c:if test="${not empty webAgent}">
					<input type="text" id="webAgentUrl" name="webAgentUrl" value="<c:out value="${webAgent.sourceUrl}"/>" placeholder="URL을 입력하세요.">
				</c:if>
				<div class="clear">
					<p class="list-star hide">URL이 입력되지 않았습니다.</p>
					<%--
					<c:if test="${empty webAgent}">
						<label><input type="checkbox" id="webAgentAttachYn" name="webAgentAttachYn" value="Y"><span>첨부파일로 지정</span></label>
					</c:if>
					<c:if test="${not empty webAgent}">
						<label><input type="checkbox" id="webAgentAttachYn" name="webAgentAttachYn" value="Y"<c:if test="${'Y' eq webAgent.secuAttYn}"> checked</c:if>><span>첨부파일로 지정</span></label>
					</c:if>
					--%>
				</div>

				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="popWebAgentSave();">등록</button>
					<button type="button" class="btn big" onclick="popWebAgentCancel();">취소</button>
				</div>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_web_agent');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>