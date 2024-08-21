<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.03.2
	*	설명 : 카카오 템플릿 머지 매핑 리스트
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<table class="grid">
	<caption>그리드 정보</caption>
	<colgroup>
		<col style="width: 10%;">
		<col style="width: auto;">
		<col style="width: 36%;">
	</colgroup>
	<thead>
		<tr>
			<th scope="col">NO</th>
			<th scope="col">알림톡 템플릿 항목</th>
			<th scope="col" style="opacity: 1;">함수 입력</th>
		</tr>
	</thead>
	<tbody id="mergyItems">
		<!-- 데이터가 있을 경우// -->
		<c:if test="${fn:length(kakaoTemplateMergeList) > 0}">
			<c:forEach items="${kakaoTemplateMergeList}" var="kakaoTemplateMerge" varStatus="kakaoTemplateMergeStatus">
				<tr>
					<td style="display:none;"><input type="text" value="<c:out value='${templateMerge.kakaoCol}'/>"></td>
					<td>${kakaoTemplateMergeStatus.index + 1}</td>
					
					<td><c:out value='${kakaoTemplateMerge.kakaoCol}'/></td>
					<td>
						<div class="select">
							<select name="merge" title="옵션선택" onchange="setMerge();">
								<option value="">선택</option>
								<c:if test="${fn:length(segMergeList) > 0}">
									<c:forEach items="${segMergeList}" var="segMergeItem">
										<option value="<c:out value='${kakaoTemplateMerge.kakaoCol}'/>|<c:out value='${segMergeItem}'/>"<c:if test="${segMergeItem eq kakaoTemplateMerge.kakaoMergeCol}"> selected</c:if>><c:out value='${segMergeItem}'/></option>
									</c:forEach>
								</c:if>
							</select>
						</div>
					</td>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>
</table>