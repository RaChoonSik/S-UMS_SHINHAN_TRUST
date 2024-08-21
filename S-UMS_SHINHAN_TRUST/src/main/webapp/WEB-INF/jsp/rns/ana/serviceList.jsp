<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 월별통계 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">서비스별 발송결과</h3>
		<span></span>
		<div class="btn-wrap">
			<button type="button" class="btn fullpurple" onclick="goExcel();">엑셀다운로드</button>
		</div>
	</div>

	<div class="grid-area pb20">
		<table id="tblServiceData" class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:auto;">
				<col style="width:10%;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:15%;">
				
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">서비스ID</th>
					<th scope="col">서비스항목</th>
					<th scope="col">발송건수</th>
					<th scope="col">성공수(%)</th>
					<th scope="col">실패수(%)</th>
					<th scope="col">응답수(%)</th>
					
					<th scope="col" style="display:none;">성공수</th> 
					<th scope="col" style="display:none;">실패수</th>
					<th scope="col" style="display:none;">응답수</th>
					
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(serviceDataList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${serviceDataList}" var="serviceData">
						<tr>
							<td><c:out value="${serviceData.tid}"/></td>
							<td><c:out value="${serviceData.tnm}"/></td>
							<td><c:out value="${serviceData.send}"/></td>
							<td><c:out value="${serviceData.displaySuccess}"/></td>
							<td><c:out value="${serviceData.displayFailed}"/></td>
							<td><c:out value="${serviceData.displayReply}"/></td>
							
							<td style="display:none;"><c:out value="${serviceData.success}"/></td>
							<td style="display:none;"><c:out value="${serviceData.failed}"/></td>
							<td style="display:none;"><c:out value="${serviceData.reply}"/></td>
							 
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty serviceDataList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="9" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
			<tfoot>
				<tr>
					<td>&nbsp;</td>
					<td>합계</td>
					<td id="tot_send">0</td>
					<td id="tot_success">0</td>
					<td id="tot_failed">0</td>
					<td id="tot_reply">0</td>
				</tr>
			</tfoot>			
		</table>
	</div>
</div>
<!-- //목록 -->
  