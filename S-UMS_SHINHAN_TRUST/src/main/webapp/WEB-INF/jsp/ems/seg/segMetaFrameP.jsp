<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.20
	*	설명 : 연결된 테이블/컬럼 설정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<c:if test="${fn:length(metaTableList) > 0}">
	<c:forEach items="${metaTableList}" var="metaTable">
		<div class="box">
			<div class="title-area">
				<h4 class="h4-title">
    				<c:set var="tblChecked" value=""/>
    				<c:if test="${fn:length(mergeCol) > 0}">
    					<c:forEach items="${mergeCol}" var="col">
    						<c:set var="colTbl" value='${fn:substring(col, 0, fn:indexOf(col,"."))}'/>
    						<c:if test="${metaTable.tblNm eq colTbl}"><c:set var="tblChecked" value=" checked"/></c:if>
    					</c:forEach>
    				</c:if>
    				<input type="checkbox" name="metaTblNm" style="width:0px;" value="<c:out value="${metaTable.tblNm}"/>"<c:out value="${tblChecked}"/>>
    				<c:out value="${metaTable.tblAlias}"/>
				</h4>
			</div>
			
			<div class="list-area">
	    		<c:if test="${fn:length(metaColumnList) > 0}">
	    			<c:forEach items="${metaColumnList}" var="metaColumn">
	    				<c:if test="${metaTable.tblNo == metaColumn.tblNo}">
		    				<c:set var="colChecked" value=""/>
		    				<c:if test="${fn:length(mergeCol) > 0}">
		    					<c:forEach items="${mergeCol}" var="col">
		    						<c:if test="${col eq (metaTable.tblNm += '.' +=metaColumn.colNm)}"><c:set var="colChecked" value=" checked"/></c:if>
		    					</c:forEach>
		    				</c:if>
							<label for="checkbox_<c:out value="${metaTable.tblNm}_${metaColumn.colNm}"/>">
								<input type="checkbox" id="checkbox_<c:out value="${metaTable.tblNm}_${metaColumn.colNm}"/>" name="metaColInfo" value="<c:out value="${metaTable.tblNm}"/>.<c:out value="${metaColumn.colNm}"/> AS <c:out value="${metaColumn.colAlias}"/>" onclick='goColumnClick()'<c:out value="${colChecked}"/>>
								<span><c:out value="${metaColumn.colAlias}"/></span>
							</label>
						</c:if>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</c:forEach>
</c:if>
