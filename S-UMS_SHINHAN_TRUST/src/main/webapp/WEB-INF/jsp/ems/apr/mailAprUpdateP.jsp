<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.30
	*	설명 : 메일발송결재 화면 출력
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<script type="text/javascript" src="<c:url value='/js/ems/apr/mailAprUpdateP.js'/>"></script>

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
					<h2>
						<c:if test="${'Y' eq searchVO.topNotiYn}">
							<c:out value="${NEO_USER_NM}"/> 발송결재
						</c:if>
						<c:if test="${'Y' ne searchVO.topNotiYn}">
							메일발송결재
						</c:if>
					</h2>
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
						<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
						<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
						<input type="hidden" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>">
						<input type="hidden" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
						<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
						<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
						<input type="hidden" name="searchWorkStatus" value="<c:out value='${searchVO.searchWorkStatus}'/>">
						<input type="hidden" name="searchAprDeptNo" value="<c:out value='${searchVO.searchAprDeptNo}'/>">
						<input type="hidden" name="searchAprUserId" value="<c:out value='${searchVO.searchAprUserId}'/>">
						<input type="hidden" name="topNotiYn" value="<c:out value='${searchVO.topNotiYn}'/>">						
					</form>
					<form id="mailAprForm" name="mailAprForm" method="post">
						<input type="hidden" id="taskNo" name="taskNo" value="<c:out value='${mailInfo.taskNo}'/>">
						<input type="hidden" id="subTaskNo" name="subTaskNo" value="<c:out value='${mailInfo.subTaskNo}'/>">
						<input type="hidden" id="taskNm" name="taskNm" value="<c:out value='${mailInfo.taskNm}'/>">
						<input type="hidden" id="mailTitle" name="mailTitle" value="<c:out value='${mailInfo.mailTitle}'/>">
						
						<input type="hidden" id="apprStep" name="apprStep" value="0">
						<input type="hidden" id="apprUserId" name="apprUserId" value="">
						<input type="hidden" id="regId" name="regId" value="<c:out value='${mailInfo.regId}'/>">
						<input type="hidden" id="tmpUserId" name="tmpUserId" value="<c:out value='${mailInfo.userId}'/>">
						
						<input type="hidden" id="rsltCd" name="rsltCd" value="">
						<input type="hidden" id="totalCount" name="totalCount" value="0">
						<input type="hidden" name="mailTypeNm" value="대용량">
						<fieldset>
							<legend>조건 및 메일 내용</legend>

							<!-- 결재// -->
							<div class="approvebox" id="divMailApprStepList">
							<%@ include file="/WEB-INF/jsp/ems/apr/mailAprStepListP.jsp" %>
							</div>
							<!-- //결재 -->
							
							<!-- 결재등록팝업// -->
							<%@ include file="/WEB-INF/jsp/inc/pop/pop_confirm_reason.jsp" %>
							<!-- //결재등록팝업 -->
							
							<!-- 결재반려사유등록팝업// -->
							<%@ include file="/WEB-INF/jsp/inc/pop/pop_reject_reason.jsp" %>
							<!-- //결재반려사유등록팝업 -->
						
							<!-- 조건// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">조건</h3>
								</div>
	
								<div class="table-area">
									<table>
										<caption></caption>
										<colgroup>
											<col style="width:120px">
											<col style="width:calc(100% - 120px)">
											<col style="width:120px">
											<col style="width:auto">
										</colgroup> 
										<tbody>
											<tr>
												<th>예약일시</th>
												<td>
													<fmt:parseDate var="sendDate" value="${mailInfo.sendDt}" pattern="yyyyMMddHHmm"/>
													<fmt:formatDate var="sendDt" value="${sendDate}" pattern="yyyy년 MM월 dd일 HH시 mm분"/>
													<c:out value="${sendDt}"/>
												</td>
												<%--정기메일만 표시 --%>
												<c:if test="${'001' eq mailInfo.sendRepeat}">
													<th>정기발송 주기</th>
													<td>
														<c:out value="${mailInfo.sendTermLoop}${mailInfo.sendTermLoopTyNm}"/>&nbsp;
														<fmt:parseDate var="sendTermEndDate" value="${mailInfo.sendTermEndDt}" pattern="yyyyMMddHHmm"/>
														<fmt:formatDate var="sendTermEndDt" value="${sendTermEndDate}" pattern="yyyy.MM.dd"/>
														<c:out value="${sendTermEndDt}"/> 종료
													</td>
												</c:if>
											</tr>
											<tr>
												<th>캠페인명</th>
												<td><c:out value="${mailInfo.campNm}"/></td>
												<th>캠페인목적</th>
												<td><c:out value="${mailInfo.campTyNm}"/></td>
											</tr>
											<tr>
												<%--
												<th>수신자그룹</th>
												<td>
													<p class="inline-txt"><c:out value="${mailInfo.segNm}"/></p>
													<button type="button" class="btn fullblue" style="margin-left:20px;" onclick="goSegInfoMail('<c:out value='${mailInfo.segNo}'/>');">미리보기</button>
												</td>
												--%>
<%-- 												<th>보안메일</th>
												<td><c:if test="${'Y' eq mailInfo.webAgentAttachYn}">해당</c:if></td> --%>
												<th>발송자 이메일</th>
												<td><crypto:decrypt colNm="MAIL_FROM_EM" data="${mailInfo.mailFromEm}"/></td>
												<th>사용자그룹</th>
												<td><c:out value="${mailInfo.deptNm}"/></td>
											</tr>
											<tr>
												<th>사용자명</th>
												<td><c:out value="${mailInfo.userNm}"/></td>
												<th>발송자명</th>
												<td><c:out value="${mailInfo.mailFromNm}"/></td>
											</tr>
<%--											<tr>
												<th>발송자 이메일</th>
												<td><crypto:decrypt colNm="MAIL_FROM_EM" data="${mailInfo.mailFromEm}"/></td>
 											<th>옵션</th>
												<td>
													<p class="inline-txt"><c:if test="${'Y' eq mailInfo.respYn}">수신확인(예)</c:if></p>
													<p class="inline-txt"><c:if test="${'Y' ne mailInfo.respYn}">수신확인(아니오)</c:if></p>
													<p class="inline-txt">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</p>
													<p class="inline-txt">
														<c:if test="${not empty mailInfo.mailMktNm}">마케팅 동의여부 : <c:out value="${mailInfo.mailMktNm}"/></c:if>
														<c:if test="${empty mailInfo.mailMktNm}">마케팅 동의여부 : 해당없음</c:if>
													</p>
													
													<label for="chk_02"><input type="checkbox" id="chk_02" disabled<c:if test="${'Y' eq mailInfo.respYn}"> checked</c:if>><span>수신확인</span></label>
													<label for="chk_03"><input type="checkbox" id="chk_03" disabled<c:if test="${not empty mailInfo.mailMktNm}"> checked</c:if>><span>마케팅 동의여부 : <c:if test="${empty mailInfo.mailMktNm}">해당없음</c:if>
													<c:if test="${not empty mailInfo.mailMktNm}"><c:out value="${mailInfo.mailMktNm}"/></c:if></span></label>
												</td> 
											</tr>--%>
											<tr>
												<th>메일명</th>
												<td colspan="3"><c:out value="${mailInfo.taskNm}"/></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!-- //조건 -->
	
							<!-- 메일 내용// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">메일 내용</h3>
	
									<!-- 버튼// -->
									<div class="btn-wrap">
										<c:if test="${'002' ne mailInfo.status && ('000' eq mailInfo.workStatus || '201' eq mailInfo.workStatus || '202' eq mailInfo.workStatus)}">
											<button type="button" class="btn" onclick="popTestSend();">테스트발송</button>
										</c:if>
									</div>
									<!-- //버튼 -->
								</div>
								
								<div class="list-area">
									<ul>
										<li class="col-full">
											<label>메일 제목</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value="${mailInfo.mailTitle}"/></p>
											</div>
										</li>
										<li class="col-full">
											<!-- 미리보기 영역// -->
											<div class="previewbox">${mailContent}</div>
											<!-- //미리보기 영역 -->
										</li>
										<li class="col-full">
											<label class="vt">첨부 파일</label>
											<div class="list-item">

												<c:if test="${fn:length(attachList) > 0}">
													<ul class="filelist">
														<c:forEach items="${attachList}" var="attach">
															<li>
																<a href="javascript:goFileDown('<c:out value="${attach.attNm}"/>','<c:out value='${attach.attFlPath}'/>');" class="link"><c:out value="${attach.attNm}"/></a>
																<em><script type="text/javascript">getFileSizeDisplay(<c:out value="${attach.attFlSize}"/>);</script></em>
															</li>
														</c:forEach>
													</ul>
												</c:if>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //메일 내용 -->
	
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

	<iframe id="iFrmMail" name="iFrmMail" style="width:0px;height:0px;"></iframe>
	
	<!-- 수신자그룹미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_mail.jsp" %>
	<!-- //수신자그룹미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->
	
	<!-- 결재반려사유내용팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_reject_display.jsp" %>
	<!-- //결재반려사유내용팝업 -->
	
	<!-- 테스트메일발송팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_sendtest_user.jsp" %>
	<!-- //테스트메일발송팝업 -->
	
</body>
</html>
