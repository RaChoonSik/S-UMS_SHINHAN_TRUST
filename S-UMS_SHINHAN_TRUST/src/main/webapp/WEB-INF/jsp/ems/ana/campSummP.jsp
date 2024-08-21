<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.07
	*	설명 : 통계분석 캠페인 통계분석 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript">
//목록 클릭시
function goList() {
	$("#searchForm").attr("target","").attr("action","./campListP.ums").submit();
}
</script>

<body>
	<div id="wrap" class="ems">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_ems.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>캠페인 통계분석</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<div class="approvemail-wrap">
					<form id="searchForm" name="searchForm" method="post">
						<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
						<input type="hidden" name="searchCampNm" value="<c:out value='${searchVO.searchCampNm}'/>">
						<input type="hidden" name="searchCampTy" value="<c:out value='${searchVO.searchCampTy}'/>">
						<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
						<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
						<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
						<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
						<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
						
						<fieldset>
							<legend>캠페인 정의 및 메일 리스트</legend>
						
							<!-- 캠페인 정의// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">캠페인 정의</h3>
								</div>
	
								<div class="table-area">
									<table>
										<caption>캠페인 정의</caption>
										<colgroup>
											<col style="width:15%">
											<col style="width:35%">
											<col style="width:15%">
											<col style="width:35%">
										</colgroup> 
										<tbody>
											<tr>
												<th>등록일시</th>
												<td>
													<fmt:parseDate var="campRegDt" value="${campInfo.regDt}" pattern="yyyyMMddHHmmss"/>
													<fmt:formatDate var="regDt" value="${campRegDt}" pattern="yyyy-MM-dd HH:mm"/>
													<c:out value="${regDt}"/>
												</td>
												<th>사용자그룹</th>
												<td><c:out value="${campInfo.deptNm}"/></td>
											</tr>
											<tr>
												<th>사용자명</th>
												<td><c:out value="${campInfo.userNm}"/></td>
												<th>캠페인명</th>
												<td><c:out value="${campInfo.campNm}"/></td>
											</tr>
											<tr>
												<th>캠페인목적</th>
												<td colspan="3"><c:out value="${campInfo.campTy}"/></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!-- //캠페인 정의 -->
	
							<!-- 메일 리스트// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">메일 리스트</h3>
								</div>
								
								<div class="defination-area">
									<c:if test="${fn:length(mailList) > 0}">
										<c:forEach items="${mailList}" var="mail">
											<strong class="df-title">
												<span><c:out value='${mail.taskNo}'/></span>
												<span><c:out value='${mail.taskNm}'/></span>
											</strong>
											<dl class="df-list">
												<dt>발송수</dt>
												<dd>
													<fmt:formatNumber var="sendCntNum" type="number" value="${mail.sendCnt}" /><c:out value="${sendCntNum}"/>
												</dd>
												<dt>성공수</dt>
												<dd>
													<fmt:formatNumber var="succCntNum" type="number" value="${mail.succCnt}" /><c:out value="${succCntNum}"/>
												</dd>
												<dt>실패수</dt>
												<dd>
													<fmt:formatNumber var="failCntNum" type="number" value="${mail.sendCnt - mail.succCnt}" /><c:out value="${failCntNum}"/>
												</dd>
												<dt>오픈수</dt>
												<dd>
													<fmt:formatNumber var="openCntNum" type="number" value="${mail.openCnt}" /><c:out value="${openCntNum}"/>
												</dd>
												<dt>유효오픈수</dt>
												<dd>
													<fmt:formatNumber var="validCntNum" type="number" value="${mail.validCnt}" /><c:out value="${validCntNum}"/>
												</dd>
												<dt>수신거부</dt>
												<dd>
													<fmt:formatNumber var="blockCntNum" type="number" value="${mail.blockCnt}" /><c:out value="${blockCntNum}"/>
												</dd>
											</dl>
										</c:forEach>
									</c:if>
									<c:if test="${empty mailList}">
										<strong class="df-title">
											<span>&nbsp;</span>
											<span>&nbsp;</span>
										</strong>
										<dl class="df-list">
											<dt>발송수</dt>
											<dd>0</dd>
											<dt>성공수</dt>
											<dd>0</dd>
											<dt>실패수</dt>
											<dd>0</dd>
											<dt>오픈수</dt>
											<dd>0</dd>
											<dt>유효오픈수</dt>
											<dd>0</dd>
											<dt>수신거부</dt>
											<dd>0</dd>
										</dl>
									</c:if>
								</div>
							</div>
							<!-- //메일 리스트 -->
							
							<!-- btn-wrap// -->
							<div class="btn-wrap">
								<button type="button" class="btn big" onclick="goList();">목록</button>
							</div>
							<!-- //btn-wrap -->

						</fieldset>
					</form>
				</div>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

</body>
</html>
