<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 도메인통계 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">도메인별 발송결과</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
		<div class="btn-wrap">
			<button type="button" class="btn fullpurple" onclick="goExcel();">엑셀다운로드</button>
		</div>
	</div>

	<div class="grid-area pb20">
		<table id="tblDomainData"  class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:5%;">
				<col style="width:auto;">
				<col style="width:12%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				<col style="width:9%;">
				
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">								
								
			</colgroup>
			<thead>
				<tr>
					<th scope="col">NO</th>
					<th scope="col">도메인명</th>
					<th scope="col">총발송건수</th>
					<th scope="col">성공수(%)</th>
					<th scope="col">실패수(%)</th>
					<th scope="col">문법오류(%)</th>
					<th scope="col">DNS에러(%)</th>
					<th scope="col">트랜잭션에러(%)</th>
					<th scope="col">수신자오류(%)</th>
					<th scope="col">네트워크(%)</th>
					<th scope="col">서비스장애(%)</th>
					
					<th scope="col" style="display:none;">총발송건수</th>
					<th scope="col" style="display:none;">성공수</th> 
					<th scope="col" style="display:none;">실패수</th>
					<th scope="col" style="display:none;">문법오류</th>
					<th scope="col" style="display:none;">DNS에러</th>
					<th scope="col" style="display:none;">트랜잭션에러</th> 
					<th scope="col" style="display:none;">수신자오류</th>
					<th scope="col" style="display:none;">네트워크</th>
					<th scope="col" style="display:none;">서비스장애</th>
					
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(domainDataList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${domainDataList}" var="domainData" varStatus="domainDataStatus">
						<tr>
							<td><c:out value="${domainDataStatus.index + 1}"/></td>
							
							<td><c:out value="${domainData.domainname}"/></td>
							<td><c:out value="${domainData.total}"/></td>
							<td><c:out value="${domainData.success}"/><br><c:out value="${domainData.displaySuccess}"/></td>
							<td><c:out value="${domainData.fail}"/><br><c:out value="${domainData.displayFail}"/></td>
							<td><c:out value="${domainData.syntax}"/><br><c:out value="${domainData.displaySyntax}"/></td>
							<td><c:out value="${domainData.dns}"/><br><c:out value="${domainData.displayDns}"/></td>
							<td><c:out value="${domainData.transact}"/><br><c:out value="${domainData.displayTransact}"/></td>
							<td><c:out value="${domainData.receiver}"/><br><c:out value="${domainData.displayReceiver}"/></td>
							<td><c:out value="${domainData.network}"/><br><c:out value="${domainData.displayNetwork}"/></td>
							<td><c:out value="${domainData.service}"/><br><c:out value="${domainData.displayService}"/></td>
							
							<td style="display:none;"><c:out value="${domainData.total}"/></td>
							<td style="display:none;"><c:out value="${domainData.success}"/></td>
							<td style="display:none;"><c:out value="${domainData.fail}"/></td>
							<td style="display:none;"><c:out value="${domainData.syntax}"/></td>
							<td style="display:none;"><c:out value="${domainData.dns}"/></td>
							<td style="display:none;"><c:out value="${domainData.transact}"/></td>
							<td style="display:none;"><c:out value="${domainData.receiver}"/></td>
							<td style="display:none;"><c:out value="${domainData.network}"/></td>
							<td style="display:none;"><c:out value="${domainData.service}"/></td>
														
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty domainDataList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="11" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="2">합계</td>	
					<td id="tot_total">0</td>
					<td id="tot_success">0</td>
					<td id="tot_fail">0</td>
					<td id="tot_syntax">0</td>
					<td id="tot_dns">0</td>
					<td id="tot_transact">0</td>
					<td id="tot_receiver">0</td>
					<td id="tot_network">0</td>
					<td id="tot_service">0</td>
				</tr>
			</tfoot>			
		</table>
	</div>
</div>
<!-- //목록 -->

<!-- 페이징// -->
<div class="paging">
	${pageUtil.pageHtml}
</div>
<!-- //페이징 -->