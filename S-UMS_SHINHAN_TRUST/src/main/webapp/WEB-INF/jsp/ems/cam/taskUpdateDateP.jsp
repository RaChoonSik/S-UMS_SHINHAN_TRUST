<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.13
	*	설명 : 정기메일발송 일자 정보수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/ems/cam/taskUpdateDateP.js'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function(){
		setMailContent("<c:out value='${mailInfo.contFlPath}'/>");
	},500);
}); 
 
</script>
<body>
	<div id="wrap" class="ems mailsending">

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
					<h2>메일발송 정보수정</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">
				<form id="searchForm" name="searchForm" method="post">
					<input type="hidden" name="page"                value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="searchStartDt"       value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt"         value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="campNo"              value="<c:out value='${searchVO.campNo}'/>">
					<input type="hidden" name="searchTaskNm"        value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchCampNo"        value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" name="searchDeptNo"        value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId"        value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" name="searchStatus"        value="<c:out value='${searchVO.searchStatus}'/>">
					<input type="hidden" name="searchWorkStatus"    value="<c:out value='${searchVO.searchWorkStatus}'/>">
					<input type="hidden" name="searchSendRepeat"    value="<c:out value='${searchVO.searchSendRepeat}'/>">
					<input type="hidden" name="taskNo"              value="<c:out value='${searchVO.taskNo}'/>">
					<input type="hidden" name="subTaskNo"           value="<c:out value='${searchVO.subTaskNo}'/>">
					<input type="hidden" id="approvalProcAppYn"     value="<c:out value='${searchVO.approvalProcAppYn}'/>">
					
					<input type='hidden' id="taskNos"               name='taskNos'           value="<c:out value='${searchVO.taskNo}'/>">
					<input type='hidden' id="subTaskNos"            name='subTaskNos'        value="<c:out value='${searchVO.subTaskNo}'/>">
					<input type='hidden' id="sendTestTaskNo"        name='sendTestTaskNo'    value="0">
					<input type='hidden' id="sendTestSubTaskNo"     name='sendTestSubTaskNo' value="0">
					<input type="hidden" id="status"                name="status"> 
				</form>
				
				<form id="mailInfoForm" name="mailInfoForm" method="post">
					<input type="hidden" id="taskNo"          name="taskNo"         value='<c:out value='${mailInfo.taskNo}'/>'>
					<input type="hidden" id="subTaskNo"       name="subTaskNo"      value='<c:out value='${mailInfo.subTaskNo}'/>'>
					<input type="hidden" id="txtSendRepeat"                         value="<c:out value='${mailInfo.sendRepeat}'/>"> 
					<fieldset>
						<legend>조건 및 메일 내용</legend>
						
						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
								<!-- 버튼// -->
								<div class="btn-wrap">
									<span class="required" style="margin-right:0;">*수정 항목</span>
									<button type="button" class="btn" onclick="popEnvSetting();">환경설정</button>
								</div>
								<!-- //버튼 -->
							</div>
							
							<div class="list-area">
								<ul>
									<li class="col-full">
										<label>메일유형</label>
										<div class="list-item">
											<c:if test="${'000' eq mailInfo.sendRepeat}"> 
												<input type="hidden" id="isSendTerm" name="isSendTerm" value="N">
											</c:if>
											<c:if test="${'001' eq mailInfo.sendRepeat}">
												<input type="hidden" id="isSendTerm" name="isSendTerm" value="Y"> 
											</c:if>
											<p class="inline-txt">
											<c:if test="${'000' eq mailInfo.sendRepeat}">단기메일</c:if>
											<c:if test="${'001' eq mailInfo.sendRepeat}">정기메일</c:if>
											</p>
										</div>
									</li>
									<li style="margin-top:10px;margin-right:6%;">
										<label class="required">발송기간</label>
										<div class="list-item">
											<!-- datepickerrange// -->
											<div class="datepickerrange fromDate" style="width:38%;">
												<fmt:parseDate var="sendDate" value="${mailInfo.sendDt}" pattern="yyyyMMddHHmm"/>
												<fmt:formatDate var="sendYmd" value="${sendDate}" pattern="yyyy.MM.dd"/>
												<fmt:formatDate var="sendHourStr" value="${sendDate}" pattern="HH"/>
												<fmt:parseNumber var ="sendHour" value ="${sendHourStr}" pattern ="00"/>
												<!-- 시간 정보 안나오는것 때문에 추가함-->
												<fmt:formatDate var="sendMin" value="${sendDate}" pattern="mm"/>
												<script type="text/javascript">var sendYmd = "<c:out value='${sendYmd}'/>";</script>
												<label>
													<input type="text" id="sendYmd" name="sendYmd" value="<c:out value='${sendYmd}'/>">
												</label>
											</div>
											<span class="hyppen date"></span>
											<div class="datepickerrange toDate" style="width:42%;margin-right:1%;">
												<label>
													<c:if test="${not empty mailInfo.sendTermEndDt}">
														<fmt:parseDate var="sendTermEndDt" value="${mailInfo.sendTermEndDt}" pattern="yyyyMMddHHmm"/>
														<fmt:formatDate var="endDt" value="${sendTermEndDt}" pattern="yyyy.MM.dd"/>
														<script type="text/javascript">var sendTermEndDt = "<c:out value='${endDt}'/>";</script>
														<input type="text" id="sendTermEndDt" name="sendTermEndDt" value="<c:out value='${endDt}'/>" class="attr_disabled">
													</c:if>
													<c:if test="${empty mailInfo.sendTermEndDt}">
														<script type="text/javascript">var sendTermEndDt = "<c:out value='${sendYmd}'/>";</script>
														<input type="text" id="sendTermEndDt" name="sendTermEndDt" class="attr_disabled">
													</c:if>
												</label>
											</div>
											<!-- //datepickerrange -->
											<label for="noLimitedCheck"><input type="checkbox" id="noLimitedCheck" onclick="checkNoLImited();" class="attr_disabled">
												<span style="margin-right:0;">&nbsp;무기한</span>
											</label>
										</div>
									</li>
									<!-- 정기메일의 경우 노출// -->
									<li style="margin-top:10px;margin-right:0;">
										<label class="required">정기발송 주기</label>
										<div class="list-item">
											<div class="multibox">
												<input type="text" id="sendTermLoop" name="sendTermLoop"  value="<c:out value="${ empty mailInfo.sendTermLoop ? '7' : mailInfo.sendTermLoop}"/>" style="width:15%;" class="attr_disabled">
												<div class="select" style="display:inline-block;width:26%;">
													<select id="sendTermLoopTy" name="sendTermLoopTy" title="정기발송주기 선택"  class="attr_disabled">
														<c:if test="${fn:length(periodList) > 0}">
															<c:forEach items="${periodList}" var="period">
																<option value="<c:out value='${period.cd}'/>"<c:if test="${mailInfo.sendTermLoopTy eq period.cd}"> selected</c:if>><c:out value='${period.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
												<%-- <input type="text" id="sendYmd" name="sendYmd" value="<c:out value='${sendHour}'/>"> --%>
												<div class="select" style="width:25%;">
													<jsp:useBean id="currTime" class="java.util.Date"/>
													<fmt:formatDate var="currHour" value="${currTime}" pattern="HH"/>
													<fmt:formatDate var="currMin" value="${currTime}" pattern="mm"/>
													<select id="sendHour" name="sendHour" title="시간 선택">
														<c:forEach begin="0" end="23" var="hour">
															<option value="<c:out value='${hour}'/>"<c:if test="${hour == sendHour}"> selected</c:if>><c:out value='${hour}'/>시</option>
														</c:forEach>
													</select>
												</div>
												<div class="select" style="width:25%;">
													<select id="sendMin" name="sendMin" title="분 선택">
														<c:forEach begin="0" end="59" var="min" step="10">
															<option value="<c:out value='${min}'/>"<c:if test="${min == sendMin}"> selected</c:if>><c:out value='${min}'/>분</option>
														</c:forEach>
													</select>
												</div> 
											</div>
										</div>
									</li>
									<!-- //정기메일의 경우 노출 -->
								</ul>
								<ul style="margin-top:10px;">
									<li>
										<label>캠페인명</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray" style="width:calc(100% - 38px);"><c:out value="${mailInfo.campNm}"/></p>
											</div>
										</div>
									</li>
									<li>
										<label>수신자그룹</label>
										<div class="list-item">
											<div class="filebox">
												<c:set var="segNoc" value=""/>
												<c:forEach items="${segList}" var="seg">
													<c:if test="${seg.segNo == mailInfo.segNo}">
														<c:set var="segNm" value="${seg.segNm}"/>
														<c:set var="segNoc" value="${seg.segNo}|${seg.mergeKey}"/>
													</c:if>
												</c:forEach>
												<p class="label bg-gray" style="width:calc(100% - 128px);"><c:out value="${mailInfo.segNm}"/></p>
												<input type="hidden" id="segNoc" name="segNoc" value="<c:out value="${segNoc}"/>">
												<button type="button" class="btn fullblue" onclick="goSegInfoMail('');">미리보기</button>
											</div>
										</div>
									</li>
									<li>
										<label>웹에이전트</label>
										<div class="list-item">
											<div class="filebox" style="width:100%;">
												<p class="label bg-gray" style="width:calc(100% - 128px);">
													<c:if test="${not empty webAgent}">
														<c:if test="${'Y' eq webAgent.secuAttYn}">첨부파일로 지정되었습니다.
															(<c:out value="${webAgent.secuAttTyp}"/>)
														</c:if>
														<c:if test="${'N' eq webAgent.secuAttYn}">본문삽입으로 지정되었습니다.</c:if>
													</c:if>
													<c:if test="${empty webAgent}">
														형식이 지정되지 않았습니다.
													</c:if>
												</p>
												<button type="button" class="btn fullblue" onclick="popWebAgentPreview();">미리보기</button>
											</div>
										</div>
									</li>
									<li>
										<label>사용자그룹</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray"><c:out value='${mailInfo.deptNm}'/></p>
											</div>
										</div>
									</li>
									<li>
										<label>사용자명</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray"><c:out value='${mailInfo.userNm}'/></p>
											</div>
										</div>
									</li>
<%-- 									<li>
										<label>옵션</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray">
													수신확인<c:if test="${'Y' eq mailInfo.respYn}"> (예)</c:if><c:if test="${'N' eq mailInfo.respYn}"> (아니오)</c:if> 
													| 마케팅동의유형(<c:out value='${mailInfo.mailMktNm}'/>)
												</p>
											</div>
										</div>
									</li>  --%>
									<%--
									<li>
										<label>발송결재라인</label>
										<div class="list-item">
											<c:if test="${'N' eq mailInfo.approvalLineYn}">
												<p class="label bg-gray">발송결재라인이 등록되지 않았습니다.</p>
											</c:if>
											<c:if test="${'Y' eq mailInfo.approvalLineYn}">
												<a href="javascript:popApprovalState('<c:out value='${mailInfo.taskNo}'/>','<c:out value='${mailInfo.subTaskNo}'/>');">
													<c:if test="${fn:length(apprLineList) > 0}">
														<c:set var="arrpLength" value="${fn:length(apprLineList) - 1}"/>
														<c:forEach items="${apprLineList}" var="appr">
															<c:if test="${appr.apprStep == 1}">
																<c:if test="${arrpLength > 0 }">
																	<c:out value='${appr.apprUserNm}'/> / <c:out value='${appr.orgNm}'/> / <c:out value='${appr.jobNm}'/> 외 
																	<c:out value='${arrpLength}'/>명 
																</c:if>
																<c:if test="${arrpLength == 0 }">
																	<c:out value='${appr.apprUserNm}'/> / <c:out value='${appr.orgNm}'/> / <c:out value='${appr.jobNm}'/>  
																</c:if> 
															</c:if>
														</c:forEach>
													</c:if>
												</a>
											</c:if>
										</div>
									</li>--%>
									<li class="col-full">
										<label>메일명</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray">
													<c:out value='${mailInfo.taskNm}'/>
												</p>
											</div>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //조건 -->

						<!-- 메일 내용// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">메일 내용</h3>

								<!-- 버튼// -->
								<div class="btn-wrap">
									<%-- 발송대기,결재대기,결재진행 상태에서 테스트발송 가능 --%>
									<c:if test="${'002' ne mailInfo.status && ('000' eq mailInfo.workStatus || '001' eq mailInfo.workStatus || '201' eq mailInfo.workStatus || '202' eq mailInfo.workStatus ||'204' eq mailInfo.workStatus)}">
										<button type="button" class="btn" onclick="popTestSend();">테스트발송</button>
									</c:if>
									<%-- 발송대기,결재대기,발송승인 상태에서 사용중지/복구/삭제 가능 --%>
									<c:if test="${'002' ne mailInfo.status && ('000' eq mailInfo.workStatus || '001' eq mailInfo.workStatus || '003' eq mailInfo.workStatus || '201' eq mailInfo.workStatus)}">
										<button type="button" class="btn<c:if test="${'001' eq mailInfo.status}"> hide</c:if>" id="btnDisable" onclick="goDisable();">사용중지</button>
										<button type="button" class="btn<c:if test="${'000' eq mailInfo.status}"> hide</c:if>" id="btnEnable" onclick="goEnable();">복구</button>
										<button type="button" class="btn" onclick="goDelete();">삭제</button>
									</c:if>
									<c:if test="${'002' ne mailInfo.status}">
										<button type="button" class="btn" onclick="goCopy();">복사</button>
									</c:if>
								</div>
								<!-- //버튼 -->
							</div>
							
							<div class="list-area">
								<ul>
									<li class="col-full">
										<label>메일 제목</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray">
													<c:out value='${mailInfo.mailTitle}'/>
												</p>
											</div>
										</div>
									</li>
									<li class="col-full">
										<!-- 에디터 영역// -->
										<div id="mailContents" class="editbox"></div>
										<!-- //에디터 영역 -->
									</li>
									<li class="col-full">
										<label class="vt">파일첨부</label>
										<div class="list-item">
											<ul class="filelist" id="mailAttachFileList">
												<c:if test="${fn:length(attachList) > 0}">
													<c:set var="totalCount" value="${0}"/>
													<c:set var="totalSize" value="${0}"/>
													<c:forEach items="${attachList}" var="attach">
														<c:set var="totalCount" value="${totalCount + 1}"/>
														<c:set var="totalSize" value="${totalSize + attach.attFlSize}"/>
														<li>
															<input type="hidden" name="attachNm" value="<c:out value='${attach.attNm}'/>">
															<c:set var="attFlPath" value="${fn:substring(attach.attFlPath, fn:indexOf(attach.attFlPath,'/')+1, fn:length(attach.attFlPath))}"/>
															<input type="hidden" name="attachPath" value="<c:out value='${attFlPath}'/>">
															<input type="hidden" name="attachSize" value="<c:out value='${attach.attFlSize}'/>">
															<span><a href="javascript:goFileDown('<c:out value="${attach.attNm}"/>','<c:out value='${attach.attFlPath}'/>');"><c:out value='${attach.attNm}'/></a></span>
															<!-- <em><script type="text/javascript">getFileSizeDisplay(<c:out value='${attach.attFlSize}'/>);</script></em> -->
														</li>
													</c:forEach>
													<script type="text/javascript">
													totalFileCnt = <c:out value='${totalCount}'/>;
													totalFileByte = <c:out value='${totalSize}'/>;
													</script>
												</c:if>
											</ul>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //메일 내용 -->
						
						<!-- 환경설정팝업// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_env_setting.jsp" %>
						<!-- //환경설정팝업 -->
						
						<!-- 웹에이전트팝업// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_web_agent.jsp" %>
						<!-- //웹에이전트팝업 -->
						
						<!-- 발송결재라인팝업// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_approval.jsp" %>
						<!-- //발송결재라인팝업 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<c:if test="${'Y' eq searchVO.approvalProcAppYn}">
								<button type="button" class="btn big" onclick="goCancel();">닫기</button>	
							</c:if>
							<c:if test="${'Y' ne searchVO.approvalProcAppYn}">
								<button type="button" class="btn big fullblue" onclick="goUpdate();">수정</button>
								<button type="button" class="btn big" onclick="goCancel();">취소</button>	
							</c:if>
						</div>
						<!-- //btn-wrap -->
							
					</fieldset>
				</form>

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
	
	<!-- 테스트메일발송팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_sendtest_user.jsp" %>
	<!-- //테스트메일발송팝업 --> 
	
	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp" %>
	<!-- //웹에이전트미리보기 팝업 -->

	<!-- 발송결재상태팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_approval_state.jsp" %>
	<!-- //발송결재라인정보팝업 -->
	
	
	
</body>
</html>