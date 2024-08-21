<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.30
	*	설명 : 결재 반려사유코드 등록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_reject_reason" class="poplayer popcheckreturn">
	<div class="inner small">
		<header>
			<h2>반려 사유등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="select">
					<select id="rejectCd" name="rejectCd" title="반려사유 선택">
						<option value="">선택</option>
						<c:if test="${fn:length(rejectList) > 0}">
							<c:forEach items="${rejectList}" var="reject">
								<option value="<c:out value='${reject.cd}'/>"><c:out value='${reject.cdNm}'/></option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<p class="list-star">반려 사유가 등록되지 않았습니다.</p>
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="goApprStepReject();">등록</button>
					<button type="button" class="btn big" onclick="fn.popupClose('#popup_reject_reason');">취소</button>
				</div>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_reject_reason');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>