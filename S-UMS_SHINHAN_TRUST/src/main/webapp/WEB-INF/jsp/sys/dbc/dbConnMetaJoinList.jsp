<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : DB CONNECTION JOIN 리스트 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<table class="grid">
	<caption>그리드 정보</caption>
	<colgroup>
	<col style="width:10%;">
	<col style="width:10%;">
	<col style="width:20%;">
	<col style="width:20%;">
	<col style="width:15%;">
	</colgroup>
	<thead>
		<tr>
			<th scope="col">마스터키정보</th>
			<th scope="col">포린키정보</th>
			<th scope="col">조인유형</th>
			<th scope="col">관계유형</th>
			<th scope="col">처리</th>
		</tr>
	</thead>
	<tbody id="joinList">
		<!-- 데이터가 있을 경우// -->
		<c:if test="${fn:length(metaJoinList) > 0}">
			<c:forEach items="${metaJoinList}" var="join" varStatus="joinStatus">
			<tr>
				<td style="display:none;"><input type="text" placeholder="value값" value="<c:out value='${join.joinNo}'/>"></td>
				<td><c:out value='[T]${join.mstTblNm}'/><br><c:out value='[C]${join.mstColNm}'/></td>
				<td><c:out value='[T]${join.forTblNm}'/><br><c:out value='[C]${join.forColNm}'/></td>
				<td>
					<div class="select">
						<select name="joinTy" title="옵션선택">
							<option value="">선택</option>
							<c:if test="${fn:length(joinTyList) > 0}">
								<c:forEach items="${joinTyList}" var="joinTy">
									<option value="<c:out value='${joinTy.cd}'/>"<c:if test="${joinTy.cd eq join.joinTy}"> selected</c:if>><c:out value='${joinTy.cdNm}'/></option>
								</c:forEach>
							</c:if>
						</select>
					</div>
				</td>
				<td>
					<div class="select">
						<select name="relTy" title="옵션선택">
							<option value="">선택</option>
							<c:if test="${fn:length(relTyList) > 0}">
								<c:forEach items="${relTyList}" var="relTy">
									<option value="<c:out value='${relTy.cd}'/>"<c:if test="${relTy.cd eq join.relTy}"> selected</c:if>><c:out value='${relTy.cdNm}'/></option>
								</c:forEach>
							</c:if>
						</select>
					</div>
				</td>	
				<td>
					<div class="btn-wrap">
						<button type="button" class="btn" onclick="goUpdate(this,'<c:out value='${join.joinNo}'/>')">수정</button>
						<button type="button" class="btn" onclick="goDelete(this,'<c:out value='${join.joinNo}'/>')">삭제</button>
					</div>
				</td>
			</tr>
			</c:forEach>
		</c:if>
	  
	</tbody>
</table>