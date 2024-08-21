<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.20
	*	설명 : 수신자그룹 조회사유등록 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_reason" class="poplayer popcheckreceiver">
	<div class="inner small">
		<header>
			<h2>수신자그룹 조회 사유등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="select">
					<select id="searchReasonCd" name="searchReasonCd" title="조회사유 선택">
						<option value="">선택</option>
						<c:if test="${fn:length(reasonList) > 0}">
							<c:forEach items="${reasonList}" var="reason">
								<option value="<c:out value='${reason.cd}'/>"><c:out value='${reason.cdNm}'/></option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<p class="list-star" id="txtSearchReason">조회 사유가 등록되지 않았습니다.</p>
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="goSearchReasonAdd();">등록</button>
					<button type="button" class="btn big" onclick="goSearchReasonCancel();">취소</button>
				</div>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_reason');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
