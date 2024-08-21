<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2023.04.17
	*	설명 : 발송키 목록 조회
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>


<!-- 목록// -->
<div class="graybox no-paging">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em><c:out value="${pageUtil.totalRow}"/></em></span>		
		<!-- 버튼// -->
		<div class="btn-wrap">
			<!-- <button type="button" class="btn fullred" onclick="goSave();">저장</button> -->
			<!-- <button type="button" id="btnAddCode" class="btn fullred plus" onclick="goAdd();">신규등록</button> -->
			<button type="button" class="btn" onclick="goDelete();">삭제</button>
		</div>
		<!-- //총 건 -->
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:10%;">
				<col style="width:40%;">
				<col style="width:40%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr> 
					<th scope="col">
						<label for="senderKeyAllChk"><input type="checkbox" id="senderKeyAllChk" name="senderKeyAllChk" onclick='selectAll(this)'><span></span></label>
					</th>							
					<th scope="col">발송키아이디</th>
					<th scope="col">발송키</th>
					<th scope="col">사용여부</th>
				</tr>
			</thead>
			<tbody>
				<!-- 데이터가 있을 경우// -->
				<c:if test="${not empty senderKeyList}">
					<c:if test="${fn:length(senderKeyList) > 0}">
						<c:forEach items="${senderKeyList}" var="senderKey" varStatus="senderKeyStatus">
							<tr>
							 
								<td align="center">
									<label for="checkbox_<c:out value='${senderKeyStatus.count}'/>"><input type="checkbox" id="checkbox_<c:out value='${senderKeyStatus.count}'/>" name="delSenderKey" value="<c:out value='${senderKey.senderKeyId}'/>"><span></span></label>
								</td>									
								<td>
									<a href="javascript:getSenderKeyInfo('<c:out value='${senderKey.senderKeyId}'/>')" class="bold"><c:out value='${senderKey.senderKeyId}'/></a>
								</td>
								<td>
									<a href="javascript:getSenderKeyInfo('<c:out value='${senderKey.senderKeyId}'/>')" class="bold"><c:out value='${senderKey.senderKey}'/></a>
								</td>
								<td><c:out value="${senderKey.useYn}"/></td>
							</tr>
						</c:forEach>
					</c:if>
				</c:if>	
				<!-- //데이터가 있을 경우 -->

				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty senderKeyList}">
					<tr class="ui-state-disabled">
						<td colspan="4" class="no_data">등록된 내용이 없습니다.</td> 
					</tr>
				</c:if>
				<!-- //데이터가 없을 경우 -->
			</tbody>
		</table>
	</div>
<!-- //목록 -->
</div>
