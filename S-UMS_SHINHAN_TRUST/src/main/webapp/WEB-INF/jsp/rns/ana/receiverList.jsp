<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.22
	*	설명 : 수신자별 분석 목록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>
		<div class="btn-wrap">
			<button type="button" class="btn fullpurple" onclick="goExcel();">엑셀다운로드</button>
		</div>
	</div>

	<div class="grid-area pb20">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:12%;">
				<col style="width:15%;">
				<col style="width:9%;">
				<col style="width:15%;">
				<col style="width:10%;">
				<col style="width:6%;">
				<col style="width:6%;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
				<col style="display:none;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">ID</th>
					<th scope="col">이메일</th>
					<th scope="col">서비스구분</th>
					<th scope="col">발송일시</th>
					<th scope="col">발송결과</th>
					<th scope="col">오픈일</th>
					<th scope="col">메일내용</th>
					<th scope="col">mid</th>
					<th scope="col">subid</th>
					<th scope="col" style="display:none;">메일제목</th>
					<th scope="col" style="display:none;">발송자명</th>
					<th scope="col" style="display:none;">발송자이메일주소</th>
					<th scope="col" style="display:none;">등록일시</th>
					<th scope="col" style="display:none;">발송템플릿</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${fn:length(receiverDataList) > 0}">
					<!-- 데이터가 있을 경우// -->
					<c:forEach items="${receiverDataList}" var="receiverData" varStatus="receiverDataStatus">
						<tr>
							<%-- <td><c:out value="${pageUtil.totalRow - (pageUtil.currPage-1)*pageUtil.pageRow - receiverDataStatus.index}"/></td> --%>
							<td><c:out value="${receiverData.rid}"/></td>
							<%-- <td><c:out value="${receiverData.rmail}"/></td> --%>							
							<td><crypto:decrypt colNm="RMAIL" data="${receiverData.rmail}"/></td>
							<td><c:out value="${receiverData.tnm}"/></td>
							<td><c:out value="${receiverData.sdate}"/></td>
							<td><c:out value="${receiverData.rcode}"/></td>
							<td><c:out value="${receiverData.rsdate}"/></td>
							<td><button type="button" class="btn" onclick="goMailDetil('<c:out value="${receiverData.mid}"/>', '<c:out value="${receiverData.deptNm}"/>', '<c:out value="${receiverData.attchCnt}"/>', this);" >보기</button></td>
							<td><c:out value="${receiverData.mid}"/></td>
							<td><c:out value="${receiverData.subid}"/></td>							
							<td style="display:none;"><c:out value="${receiverData.subject}"/></td>
							<td style="display:none;"><c:out value="${receiverData.sname}"/></td>
							<td style="display:none;"><crypto:decrypt colNm="SMAIL" data="${receiverData.smail}"/></td>
							<%-- <td style="display:none;"><c:out value="${receiverData.smail}"/></td> --%>
							<td style="display:none;"><c:out value="${receiverData.cdate}"/></td>
							<td style="display:none;"><c:out value="${receiverData.contents}"/></td>
							
						</tr>
					</c:forEach>
					<!-- //데이터가 있을 경우 -->
				</c:if>

				<c:if test="${empty receiverDataList}">
					<!-- 데이터가 없을 경우// -->
					<tr>
						<td colspan="8" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
					<!-- //데이터가 없을 경우 -->
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<!-- //목록 -->

<!-- 페이징// -->
<div class="paging">
	${pageUtil.pageHtml}
</div>
<!-- //페이징 -->