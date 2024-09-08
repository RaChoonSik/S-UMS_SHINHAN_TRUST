<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.07
	*	설명 : 메인화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<script type="text/javascript">
$(document).ready(function() {
	// 조회기간 시작일 설정
	$("#searchStartDt").datepicker({
		//showOn:"button",
		minDate:"2021-01-01",
		maxDate:$("#searchEndDt").val(),
		onClose:function(selectedDate) {
			$("#searchEndDt").datepicker("option", "minDate", selectedDate);
		}
	});
	
	// 조회기간 종료일 설정
	$("#searchEndDt").datepicker({
		//showOn:"button",
		minDate:$("#searchStartDt").val(),
		onClose:function(selectedDate) {
			$("#searchStartDt").datepicker("option", "maxDate", selectedDate);
		}
	});
	
	loadMailList();
});

// 메일 목록 로딩
function loadMailList() {
	//divMailList
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "<c:url value='/ems/seg/segRemarketMailListP.ums'/>?" + param,
		dataType : "html",
		//async: false,
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

//사용자그룹 선택시 사용자 목록 설정
function getUserList(deptNo) {
	$.getJSON("<c:url value='/com/getUserList.json'/>?deptNo=" + deptNo, function(data) {
		$("#searchUserId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#searchUserId").append(option);
		});
	});
}

// 페이지 이동
function goPageNum(page) {
	$("#page").val(page);
	var param = $("#searchForm").serialize();
	$.ajax({
		type : "GET",
		url : "<c:url value='/ems/seg/segRemarketMailListP.ums'/>?" + param,
		dataType : "html",
		//async: false,
		success : function(pageHtml){
			$("#divMailList").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

//segRemarketMailListP에서 발생하는 이벤트 : 메일 선택시 발생
function goMailSelect(taskNo, subTaskNo, taskNm) {
	$(opener.document).find("#taskNo").val(taskNo);
	$(opener.document).find("#subTaskNo").val(subTaskNo);
	$(opener.document).find("#srcWhere").val(taskNo + "|" + subTaskNo + "|" + taskNm);
	$(opener.document).find("#taskNm").val(taskNm);

	window.close();
}
</script>

<div  class="popWrap">
<h1>발송 대상 그룹 보기</h1>
<div class="inWrap">
    <form id="searchForm" name="searchForm" method="post">
    <input type="hidden" id="page" name="page" value="1">
    <table border="1" cellspacing="0" cellpadding="0" class="table_line_outline" style="width:100%;">
    <tr height="20">
        <td width='70' class="td_title">부서</td>
        <td width='180' class="td_body">
	        <c:if test="${'Y' eq NEO_ADMIN_YN}">
	            <select name="searchDeptNo" onchange="getUserList(this.value);">
	                <option value='0'>::::: 부서 선택 :::::</option>
					<c:if test="${fn:length(deptList) > 0}">
						<c:forEach items="${deptList}" var="dept">
							<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
						</c:forEach>
					</c:if>
				</select>   	
	        </c:if>
	        <c:if test="${'N' eq NEO_ADMIN_YN}">
	        	<select id="searchDeptNo" name="searchDeptNo">
					<c:if test="${fn:length(deptList) > 0}">
						<c:forEach items="${deptList}" var="dept">
							<c:if test="${dept.deptNo == NEO_DEPT_NO}">
								<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
							</c:if>
						</c:forEach>
					</c:if>
	        	</select>
	        </c:if>
        </td>
        <td width='70' class="td_title">사용자</td>
        <td width='180' class="td_body">
            <select id="searchUserId" name="searchUserId">
                <option value=''>:: 사용자 선택 ::</option>
                	<c:if test="${fn:length(userList) > 0}">
                		<c:forEach items="${userList}" var="user">
                			<option value="<c:out value='${user.userId}'/>"><c:out value='${user.userNm}'/></option>
                		</c:forEach>
                	</c:if>
            </select>
        </td>
        <td width='70' class="td_title">등록일</td>
        <td width='220' class="td_body">
            <input type="text" id="searchStartDt" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>" style="width:100px;" readonly> ~
            <input type="text" id="searchEndDt" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>" style="width:100px;" readonly>
        </td>
    </tr>
    <tr height="20">
        <td class="td_title">메일명</td>
        <td class="td_body">
            <input type="text" name="searchTaskNm" size="20">
        </td>
        <td class="td_title">캠페인</td>
        <td colspan="3" class="td_body">
            <select name="searchCampNo">
                <option value='0'>::::: 캠페인 선택 :::::</option>
                <c:if test="${fn:length(campaignList) > 0}">
                	<c:forEach items="${campaignList}" var="campaign">
                		<option value="<c:out value='${campaign.campNo}'/>"><c:out value='${campaign.campNm}'/></option>
                	</c:forEach>
                </c:if>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="검색" class="btn_style" onClick="goSearch()">
            <input type="button" value="초기화" class="btn_style" onClick="goInit()">&nbsp;
        </td>
    </tr>
    </table>
    <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
    <tr><td height="10"></td></tr>
    <tr>
        <td>
        	<div id="divMailList" style="width:100%;height:360px;overflow:auto;"></div>
        </td>
    </tr>
    </table>
    </form>
    </div>
    
	    <div class="btnR">
	        <input type="button" class="btn_typeG" value="닫기" onClick="window.close()">
	    </div> 
    
    </div>


</body>
</html>
