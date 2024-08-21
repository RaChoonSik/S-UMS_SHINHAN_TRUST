<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.03
	*	설명 : 메타 칼럼 리스트 목록
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">내용</h3>
	</div>

	<div class="grid-area">
		<table class="grid">
			<caption>그리드 정보</caption>
			<colgroup>
				<col style="width:10%;">
				<col style="width:15%;">
				<col style="width:10%;">
				<col style="width:auto;">
				<col style="width:15%;">
				<col style="width:12%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">컬럼명</th>
					<th scope="col">별칭</th>
					<th scope="col">타입</th>
					<th scope="col">설명</th>
					<th scope="col">처리</th>
					<th scope="col">옵션</th> 
				</tr>
			</thead>
			<tbody id="metaColumnList">
				<c:if test="${fn:length(metaColumnList) > 0}">
					<c:forEach items="${metaColumnList}" var="metaColumn" varStatus="metaColumnStatus">
						<tr>
							<td style="display:none;"><c:out value="${metaColumn.colNo}"/></td>
							<td><c:out value="${metaColumn.colNm}"/></td>
							<td><input type="text" value="<c:out value='${metaColumn.colAlias}'/>"></td>
							<td><c:out value="${metaColumn.colDataTy}"/></td>
							<td>
								<textarea><c:out value="${metaColumn.colDesc}"/></textarea>
							</td> 
						 	<td>
							 	<c:if test="${ metaColumn.colNo!= 0}">
									<button type="button" class="btn" value="<c:out value='${metaColumn.colNo}'/>" onclick="goUpdateColumn(this);">수정</button>
									<button type="button" class="btn" value="<c:out value='${metaColumn.colNo}'/>" onclick="goExtractColumn(this);">추출조건</button>
									<button type="button" class="btn" value="<c:out value='${metaColumn.colNo}'/>" onclick="goDeleteColumn(this);">삭제</button>
									<button type="button" class="btn" style="display:none;" value="0" onclick="goUpdateColumn(this);">등록</button>	
							 	</c:if>
							 	<c:if test="${metaColumn.colNo == 0}">
							 		<button type="button" class="btn" style="display:none;" value="<c:out value='${metaColumn.colNo}'/>" onclick="goUpdateColumn(this);">수정</button>
									<button type="button" class="btn" style="display:none;" value="<c:out value='${metaColumn.colNo}'/>" onclick="goExtractColumn(this);">추출조건</button>
									<button type="button" class="btn" style="display:none;" value="<c:out value='${metaColumn.colNo}'/>" onclick="goDeleteColumn(this);">삭제</button>
									<button type="button" class="btn" value="0" onclick="goUpdateColumn(this);">등록</button>	
							 	</c:if>	
							</td>
							<td class="tal">
								<label>
									<c:choose>
							    	<c:when test="${'Y' eq metaColumn.colHiddenYn}">
							        <input type="checkbox" value="true" checked>
									</c:when>
									<c:otherwise>
							        <input type="checkbox" value="false">
									</c:otherwise>    
							 		</c:choose>
							 		<span>숨김</span>
							 	</label>
								<label>
									<c:choose>
									<c:when test="${'Y' eq metaColumn.colEncrDecrYn}">
									<input type="checkbox" value="true" checked>
									</c:when>
									<c:otherwise>
									<input type="checkbox" value="false">
									</c:otherwise>    
									</c:choose>
									<span>보안처리</span>
							 	</label> 
							</td>
						</tr>
						
					</c:forEach>
				</c:if> 
				<!-- 데이터가 없을 경우// -->
				<c:if test="${empty metaColumnList}">
					<tr>
						<td colspan="6" class="no_data">등록된 내용이 없습니다.</td>
					</tr>
				</c:if>
				<!-- //데이터가 없을 경우 -->
			</tbody>
			
			
		</table>
	</div>
</div>
