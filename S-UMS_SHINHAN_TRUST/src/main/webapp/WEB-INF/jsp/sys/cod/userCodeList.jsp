<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.05
	*	설명 : 코드 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

 

	<script>
	$(document).ready(function(){
		//드래그앤드롭
		$('#code_sortable').sortable(function() {
			items: 'tr:not(.ui-state-disabled)'
    	});
	});
	</script>

<!-- 목록// -->
<div class="graybox no-paging">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>		
		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullred" onclick="goSave();">저장</button>
			<!-- <button type="button" id="btnAddCode" class="btn fullred plus" onclick="goAdd();">신규등록</button> -->
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
		<!-- //총 건 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:3%;">
				<col style="width:5%;">
				<col style="width:5%;">
				<col style="width:20%;">
				<col style="width:20%;">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">순서</th>
					
					<th scope="col">NO</th>		
					<th scope="col">
						<label for="userCodeAllChk"><input type="checkbox" id="userCodeAllChk" name="userCodeAllChk" onclick='selectAll(this)'><span></span></label>
					</th>								
					<th scope="col">공통코드</th>
					<th scope="col">공통코드명</th>
					<th scope="col">상위코드</th>
					<th scope="col">사용여부</th>
				</tr>
			</thead>
			<tbody id="code_sortable" class="sortable">
				<!-- 데이터가 있을 경우// -->
				<c:if test="${not empty userCodeList}">
					<c:if test="${fn:length(userCodeList) > 0}">
						<c:forEach items="${userCodeList}" var="userCode" varStatus="userCodeStatus">
							<tr>
 								<td>
									<button type="button" class="list-btn btn-up"></button>
									<button type="button" class="list-btn btn-down"></button>
								</td>  
							
								<td><c:out value="${userCodeStatus.index + 1}"/></td>
								<td align="center">
									<c:if test="${userCode.sysYn eq 'Y'}">
										<label for="checkbox_<c:out value='${userCodeStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${userCodeStatus.count}'/>" name="delCd" value="<c:out value='${userCode.cd}'/>" disabled="disabled"><span></span></label>
									</c:if>
									<c:if test="${userCode.sysYn eq 'N'}">
										<label for="checkbox_<c:out value='${userCodeStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${userCodeStatus.count}'/>" name="delCd" value="<c:out value='${userCode.cd}'/>"><span></span></label>
									</c:if>
								</td>									
								<td>
									<a href="javascript:getUserCodeInfo('<c:out value='${userCode.cdGrp}'/>','<c:out value='${userCode.cd}'/>')" class="bold"><c:out value='${userCode.cd}'/></a>
								</td>
								<td>
									<a href="javascript:getUserCodeInfo('<c:out value='${userCode.cdGrp}'/>','<c:out value='${userCode.cd}'/>');" class="bold"><c:out value='${userCode.cdNm}'/></a>
								</td>
								<td><c:out value="${userCode.upCdNm}"/></td>
								<td><c:out value="${userCode.useYn}"/></td>
							</tr>
						</c:forEach>
					</c:if>
				</c:if>	
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty userCodeList}">
					<tr class="ui-state-disabled">
						<td colspan="7" class="no_data">등록된 내용이 없습니다.</td> 
					</tr>
				</c:if>
				<!-- //데이터가 없을 경우 -->
			</tbody>
		</table>
	</div>
<!-- //목록 -->
</div>
