<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.28
	*	설명 : 정기메일발송 정보수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/ems/cam/taskUpdateP.js'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function(){
		setMailContent("<c:out value='${mailInfo.contFlPath}'/>");
	},1000);
}); 

mailStatus = "<c:out value='${mailInfo.status}'/>";
mailWorkStatus = "<c:out value='${mailInfo.workStatus}'/>";
mailApprLineYn = "<c:out value='${mailInfo.approvalLineYn}'/>";
mailApprProcYn = "<c:out value='${mailInfo.approvalProcYn}'/>";
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
					<input type="hidden" name="page" value="<c:out value='${searchVO.page}'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="campNo" value="<c:out value='${searchVO.campNo}'/>">
					<input type="hidden" name="searchTaskNm" value="<c:out value='${searchVO.searchTaskNm}'/>">
					<input type="hidden" name="searchCampNo" value="<c:out value='${searchVO.searchCampNo}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
					<input type="hidden" name="searchWorkStatus" value="<c:out value='${searchVO.searchWorkStatus}'/>">
					<input type="hidden" name="searchSendRepeat"    value="<c:out value='${searchVO.searchSendRepeat}'/>">
					<input type="hidden" name="taskNo" value="<c:out value='${searchVO.taskNo}'/>">
					<input type="hidden" name="subTaskNo" value="<c:out value='${searchVO.subTaskNo}'/>">
					
					<input type='hidden' id="taskNos" name='taskNos' value="<c:out value='${searchVO.taskNo}'/>">
					<input type='hidden' id="subTaskNos" name='subTaskNos' value="<c:out value='${searchVO.subTaskNo}'/>">
					<input type='hidden' id="sendTestTaskNo" name='sendTestTaskNo' value="0">
					<input type='hidden' id="sendTestSubTaskNo" name='sendTestSubTaskNo' value="0">
					<input type="hidden" id="status" name="status">
				</form>

				<form id="mailInfoForm" name="mailInfoForm" method="post">
					<c:set var="nmMerge" value=""/>
					<c:set var="idMerge" value=""/>
					<c:if test="${fn:length(mergeList) > 0}">
						<c:forEach items="${mergeList}" var="merge" varStatus="mergeStatus">
							<c:if test="${mergeStatus.index == 1}">
								<c:set var="nmMerge" value="${merge.cdNm}"/>
							</c:if>
							<c:if test="${mergeStatus.index == 2}">
								<c:set var="idMerge" value="${merge.cdNm}"/>
							</c:if>
						</c:forEach>
					</c:if>
					<input type="hidden" id="composerValue"   name="composerValue">
					<input type="hidden" id="taskNo"          name="taskNo"          value='<c:out value='${mailInfo.taskNo}'/>'>
					<input type="hidden" id="subTaskNo"       name="subTaskNo"       value='<c:out value='${mailInfo.subTaskNo}'/>'>
					<input type="hidden" id="contFlPath"      name="contFlPath"      value='<c:out value='${mailInfo.contFlPath}'/>'>
					<input type="hidden" id="channel"         name="channel"         value='<c:out value='${mailInfo.channel}'/>'>
					<input type="hidden" id="campNo"          name="campNo"          value="<c:out value='${mailInfo.campNo}'/>">
					<input type="hidden" id="campTy"          name="campTy"          value="<c:out value='${mailInfo.campTy}'/>">
					<input type="hidden" id="nmMerge"         name="nmMerge"         value="$:<c:out value='${nmMerge}'/>:$">
					<input type="hidden" id="idMerge"         name="idMerge"         value="$:<c:out value='${idMerge}'/>:$">
					<input type="hidden" id="txtMailFromNm" value="<c:out value='${mailInfo.mailFromNm}'/>">
					<input type="hidden" id="txtSendRepeat" value="<c:out value='${mailInfo.sendRepeat}'/>">
					<input type="hidden" id="segCreateTy"                            value="<c:out value='${mailInfo.segCreateTy}'/>">
					<!--  정기메일 화면숨김 고정값 -->
					<input type="hidden" id="contTy"     name="contTy"     value="000">
					<input type="hidden" id="linkYn"     name="linkYn"     value="N">
					<input type="hidden" id="respLog"    name="respLog"    value="31">
					<input type="hidden" id="approvalYn" name="approvalYn" value="$:<c:out value='${mailInfo.approvalLineYn}'/>:$">
					<input type="hidden" id="respYn"     name="respYn"     value="Y">
					<fieldset>
						<legend>조건 및 메일 내용</legend>

						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
							<h3 class="h3-title" style="margin-top:0;">조건</h3>
 							<!--	<h3 class="h3-title" style="margin-top:0;">
									<button type="button" class="btn fullblue" style="margin-right: 8px;">EAI연계</button>
									<span class="required">*EAI연계 등록 후 진행하여 주십시오.</span>
							</h3> -->
								<!-- 버튼// -->
								<div class="btn-wrap">
									<span class="required" style="margin-right:0;">*필수입력 항목</span>
									<button type="button" class="btn" onclick="popEnvSetting();">환경설정</button>
								</div>
								<!-- //버튼 -->
							</div>
							
							<div class="list-area">
								<ul>
									<li>
										<label class="required">메일유형</label>
										<div class="list-item">
											<label for="rdo_01" name="rdo_01"><input type="radio" id="rdo_01" value="N" name="isSendTerm" <c:if test="${'000' eq mailInfo.sendRepeat}"> checked</c:if>><span>단기메일</span></label>
											<label for="rdo_02" name="rdo_02"><input type="radio" id="rdo_02" value="Y" name="isSendTerm" <c:if test="${'001' eq mailInfo.sendRepeat}"> checked</c:if>><span>정기메일</span></label>
										</div>
									</li>
									<li>
										<label class="required">캠페인명</label>
										<div class="list-item">
											<div class="filebox">
												<c:set var="campNm" value=""/>
												<c:forEach items="${campList}" var="camp">
													<c:if test="${camp.campNo == mailInfo.campNo}">
														<c:set var="campNm" value="${camp.campNm}"/>
													</c:if>
												</c:forEach>
												<p class="label bg-gray" id="txtCampNm"><c:out value="${campNm}"/></p>
												<button type="button" class="btn btn-search" onclick="popCampSelect();"><span class="hidden">선택</span></button>
											</div>
										</div>
									</li>
									<li style="margin-top:15px;margin-right:6%;">
										<label class="required">예약일시</label>
										<div class="list-item">
											<!-- datepickerrange// -->
											<div class="datepickerrange fromDate" style="width:38%;">
												<fmt:parseDate var="sendDate" value="${mailInfo.sendDt}" pattern="yyyyMMddHHmm"/>
												<fmt:formatDate var="sendYmd" value="${sendDate}" pattern="yyyy.MM.dd"/>
												<fmt:formatDate var="sendHour" value="${sendDate}" pattern="HH"/>
												<fmt:formatDate var="sendMin" value="${sendDate}" pattern="mm"/>
												<script type="text/javascript">var sendYmd = "<c:out value='${sendYmd}'/>";</script>
												<label>
													<input type="text" id="sendYmd" name="sendYmd" value="<c:out value='${sendYmd}'/>">
												</label>
											</div>
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
													<c:forEach begin="0" end="59" var="min">
														<option value="<c:out value='${min}'/>"<c:if test="${min == sendMin}"> selected</c:if>><c:out value='${min}'/>분</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</li>
									<!-- 정기메일의 경우 노출// -->
									<li style="margin-right:0;">
										<label class="required">정기발송 주기</label>
										<div class="list-item">
											<div class="multibox">
												<input type="text" id="sendTermLoop" name="sendTermLoop"  value="<c:out value="${ empty mailInfo.sendTermLoop ? '7' : mailInfo.sendTermLoop}"/>" style="width:15%;" class="attr_disabled">
												<div class="select" style="display:inline-block;width:20%;">
													<select id="sendTermLoopTy" name="sendTermLoopTy" title="정기발송주기 선택"  class="attr_disabled">
														<c:if test="${fn:length(periodList) > 0}">
															<c:forEach items="${periodList}" var="period">
																<option value="<c:out value='${period.cd}'/>"<c:if test="${mailInfo.sendTermLoopTy eq period.cd}"> selected</c:if>><c:out value='${period.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
												<div class="datepickerrange toDate" style="width:38%;margin-right:1%;">
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
										</div>
									</li>
									<!-- //정기메일의 경우 노출 -->
								</ul>
								<ul>
									<%-- 관리자의 경우 전체 요청부서를 전시하고 그 외의 경우에는 숨김 --%>
									<c:if test="${'Y' eq NEO_ADMIN_YN}">
										<li>
											<label class="required">사용자그룹</label>
											<div class="list-item">
												<div class="select">
													<select id="deptNo" name="deptNo" onchange="getUserList(this.value);" title="사용자그룹 선택">
														<option value="0">선택</option>
														<c:if test="${fn:length(deptList) > 0}">
															<c:forEach items="${deptList}" var="dept">
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == mailInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
										<li>
											<label class="required">사용자명</label>
											<div class="list-item">
												<div class="select">
													<select id="userId" name="userId" title="사용자명 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(userList) > 0}">
															<c:forEach items="${userList}" var="user">
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq mailInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
									</c:if>
<%-- 									<li>
										<label>발송결과 종별</label>
										<div class="list-item">
											<div class="select">
												<select id="sndTpeGb" name="sndTpeGb" title="발송결과종별 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(sndTpeList) > 0}">
														<c:forEach items="${sndTpeList}" var="sndTpe">
															<option value="<c:out value='${sndTpe.cd}'/>"<c:if test="${sndTpe.cd eq mailInfo.sndTpeGb}"> selected</c:if>><c:out value='${sndTpe.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li> --%>
									<li>
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<div class="filebox">
												<c:set var="segNm" value=""/>
												<c:set var="segNoc" value=""/>
												<c:forEach items="${segList}" var="seg">
													<c:if test="${seg.segNo == mailInfo.segNo}">
														<c:set var="segNm" value="${seg.segNm}"/>
														<c:set var="segNoc" value="${seg.segNo}|${seg.mergeKey}"/>
													</c:if>
												</c:forEach>
												<p class="label bg-gray" style="width:calc(100% - 108px);" id="txtSegNm"><c:out value="${segNm}"/></p>
												<input type="hidden" id="segNoc" name="segNoc" value="<c:out value="${segNoc}"/>">
												<button type="button" class="btn btn-search" onclick="popSegSelect();"><span class="hidden">선택</span></button>
												<button type="button" class="btn fullblue" onclick="goSegInfoMail('');">미리보기</button>
											</div>
										</div>
									</li>
									<li>
										<label>템플릿명</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray" id="txtTempNm">선택된 템플릿이 없습니다.</p>
												<button type="button" class="btn btn-search" onclick="popTempSelect();"><span class="hidden">선택</span></button>
											</div>
										</div>
									</li>
									<li>
										<label>발송결재라인</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray"<c:if test="${'201' eq mailInfo.workStatus}"> style="width:calc(100% - 102px);"</c:if> id="txtApprovalLineYn">
													<c:if test="${'Y' eq mailInfo.approvalLineYn}">
														<c:if test="${fn:length(apprLineList) > 0}">
															<c:set var="arrpLength" value="${fn:length(apprLineList) - 1}"/>
															<c:forEach items="${apprLineList}" var="appr">
																<c:if test="${appr.apprStep == 1}">
																	<c:if test="${arrpLength > 0 }">
																		<c:out value='${appr.apprUserNm}'/> / <c:out value='${appr.orgNm}'/> / <c:out value='${appr.jobNm}'/> 외 <c:out value='${arrpLength}'/>명
																	</c:if>
																	<c:if test="${arrpLength == 0 }">
																		<c:out value='${appr.apprUserNm}'/> / <c:out value='${appr.orgNm}'/> / <c:out value='${appr.jobNm}'/>
																	</c:if> 
																</c:if>
															</c:forEach>
														</c:if>
													</c:if>
													<c:if test="${'N' eq mailInfo.approvalLineYn}">
														발송결재라인이 등록되지 않았습니다.
													</c:if>
												</p>
												<button type="button" class="btn fullblue" onclick="popMailApproval();">수정</button>
												<c:if test="${'201' eq mailInfo.workStatus}">
													<button type="button" class="btn" onclick="goSubmitApproval();">상신</button>
												</c:if> 
											</div>
										</div>
									</li>
									<li>
										<label>웹에이전트</label>
										<div class="list-item">
											<div class="filebox" style="width:100%;">
												<p class="label" id="txtWebAgentUrl" style="width:230px;">
													<c:if test="${not empty webAgent}">
														<c:if test="${'Y' eq webAgent.secuAttYn}">첨부파일로 지정되었습니다.</c:if>
														<c:if test="${'N' eq webAgent.secuAttYn}">본문삽입으로 지정되었습니다.</c:if>
													</c:if>
													<c:if test="${empty webAgent}">
														형식이 지정되지 않았습니다.
													</c:if>
												</p>
												<button type="button" class="btn fullblue" onclick="popWebAgent();">수정</button>
												<div class="select" style="display:none;">
													<input type="hidden" id="webAgentAttachYn" name="webAgentAttachYn" value="N">
													<!-- 
													<select id="webAgentAttachYn" name="webAgentAttachYn" onchange="changeAttachYn();" title="웹에이전트첨부 선택">
														<option value="Y"<c:if test="${'Y' eq webAgent.secuAttYn}"> selected</c:if>>첨부파일</option>
														<option value="N"<c:if test="${'N' eq webAgent.secuAttYn}"> selected</c:if>>본문삽입</option>
													</select>
												 	-->
												</div>
												<div class="select"style="width:100px;">
													<select id="secuAttTyp" name="secuAttTyp" onchange="changeAttachYn();" title="문서유형 선택">
														<option value="NONE"<c:if test="${'NONE' eq webAgent.secuAttTyp}"> selected</c:if>>NONE</option>
														<option value="PDF"<c:if test="${'PDF' eq webAgent.secuAttTyp}"> selected</c:if>>PDF</option>
														<option value="EXCEL"<c:if test="${'EXCEL' eq webAgent.secuAttTyp}"> selected</c:if>>EXCEL</option>
													</select>
												</div>
												<button type="button" class="btn fullblue" onclick="popWebAgentPreview();">미리보기</button>
											</div>
										</div>
									</li>
<%-- 										<li>
											<label>발송자명</label>
											<div class="list-item">
												<p class="inline-txt line-height40" id="txtMailFromNm"><c:out value='${mailInfo.mailFromNm}'/></p>
											</div>
										</li> --%>

<%-- 									<li>
										<label>옵션</label>
										<div class="list-item">
											<label for="respYn"><input type="checkbox" id="respYn" name="respYn" value="Y"<c:if test="${'Y' eq mailInfo.respYn}"> checked</c:if>><span>수신확인</span></label>
											<div class="marketingbox">
												<label for="tempMktGb"><input type="checkbox" id="tempMktGb" name="tempMktGb" value="Y" onclick="checkMktGb();"<c:if test="${not empty mailInfo.mailMktGb}"> checked</c:if>><span>마케팅 동의여부</span></label>
												<div class="select">
													<select id="mailMktGb" name="mailMktGb" onchange="changeMktGb();" title="마케팅동의여부 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(mktGbList) > 0}">
															<c:forEach items="${mktGbList}" var="mktGb">
																<option value="<c:out value='${mktGb.cd}'/>"<c:if test="${mktGb.cd eq mailInfo.mailMktGb}"> selected</c:if>><c:out value='${mktGb.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</div>
									</li> --%>

									<li class="col-full">
										<label class="required">메일명</label>
										<div class="list-item">
											<input type="text" id="taskNm" name="taskNm" value="<c:out value='${mailInfo.taskNm}'/>" placeholder="메일명을 입력하세요.">
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
									<%-- <c:if test="${'N' eq mailInfo.approvalLineYn && '000' eq mailInfo.status && '000' eq mailInfo.workStatus}"> --%>
									<c:if test="${'Y' eq mailInfo.approvalLineYn && '000' eq mailInfo.status && '204' eq mailInfo.workStatus}">
										<button type="button" class="btn fullblue" onclick="goAdmit();">발송승인</button>
									</c:if>
									<c:if test="${'000' eq mailInfo.status && '202' eq mailInfo.workStatus && 'N' eq mailInfo.approvalProcYn}">
										<button type="button" class="btn" onclick="goApprCancel();">결재취소</button>
									</c:if>
									<%-- 발송대기,결재대기,결재진행 상태에서 테스트발송 가능 --%>
									<c:if test="${'002' ne mailInfo.status && ('000' eq mailInfo.workStatus || '001' eq mailInfo.workStatus || '201' eq mailInfo.workStatus || '202' eq mailInfo.workStatus ||'204' eq mailInfo.workStatus)}">
										<button type="button" class="btn" onclick="popTestSend();">테스트발송</button>
									</c:if>
									<button type="button" class="btn" onclick="popUploadHtml('mail');">HTML등록</button>
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
									<li>
										<label class="required">메일 제목</label>
										<div class="list-item">
											<input type="text" id="mailTitle" name="mailTitle" value="<c:out value='${mailInfo.mailTitle}'/>" placeholder="메일 제목을 입력하세요.">
										</div>
									</li>
									<li>
										<label>함수입력</label>
										<div class="list-item">
											<div class="select" style="width:calc(100% - 102px);">
												<select id="mergeKeyMail" name="mergeKeyMail" title="함수입력 선택">
												</select>
											</div>
											<button type="button" class="btn fullblue" onclick="goTitleMerge();">제목</button>
											<button type="button" class="btn fullblue" onclick="goContMerge();">본문</button>
										</div>
									</li>
									<li class="col-full">
										<!-- 에디터 영역// -->
										<div class="editbox">
											<textarea name="ir1" id="ir1" rows="10" cols="100" style="text-align:center; width:100%; height:412px; display:none; border:none;"></textarea>
											<script type="text/javascript">
											var oEditors = [];
								
											// 추가 글꼴 목록
											//var aAdditionalFontSet = [["MS UI Gothic", "MS UI Gothic"], ["Comic Sans MS", "Comic Sans MS"],["TEST","TEST"]];
								
											nhn.husky.EZCreator.createInIFrame({
												oAppRef: oEditors,
												elPlaceHolder: "ir1",
												sSkinURI: "<c:url value='/smarteditor/SmartEditor2Skin.html'/>",
												htParams : {
													bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
													bUseVerticalResizer : true,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
													bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
													//aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
													fOnBeforeUnload : function(){
														//alert("완료!");
													}
												}, //boolean
												fOnAppLoad : function(){
													//예제 코드
													//oEditors.getById["ir1"].exec("SET_CONTENTS", [""]);    //스마트에디터 초기화
													//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
													//oEditors.getById["ir1"].exec("PASTE_HTML", ["${compVal}"]);
												},
												fCreator: "createSEditor2"
											});
								
											function pasteHTML(obj) {
												var sHTML = "<img src='<c:out value='${DEFAULT_DOMAIN}'/>/img/upload/"+obj+"'>";
												oEditors.getById["ir1"].exec("PASTE_HTML", [sHTML]);
											}
								
											function showHTML() {
												var sHTML = oEditors.getById["ir1"].getIR();
												alert(sHTML);
											}
								
											function submitContents(elClickedObj) {
												oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);	// 에디터의 내용이 textarea에 적용됩니다.
								
												// 에디터의 내용에 대한 값 검증은 이곳에서 document.getElementById("ir1").value를 이용해서 처리하면 됩니다.
												try {
													elClickedObj.form.submit();
												} catch(e) {}
											}
									
											function setDefaultFont() {
												var sDefaultFont = '궁서';
												var nFontSize = 24;
												oEditors.getById["ir1"].setDefaultFont(sDefaultFont, nFontSize);
											}
											</script>
										</div>
										<!-- //에디터 영역 -->
									</li>
									<li class="col-full">
										<label class="vt">파일첨부</label>
										<div class="list-item">
											<button type="button" class="btn" onclick="fn.popupOpen('#popup_file');">파일선택</button>
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
															<em><script type="text/javascript">getFileSizeDisplay(<c:out value='${attach.attFlSize}'/>);</script></em>
															<button type="button" class="btn-del" onclick="deleteAttachFile(this)"><span class="hide">삭제</span></button>
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
							<button type="button" class="btn big fullblue" onclick="goUpdate();">수정</button>
							<button type="button" class="btn big" onclick="goCancel();">취소</button>
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
	
	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_file.jsp" %>
	<!-- //파일등록 팝업 -->
	
	<!-- 테스트메일발송팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_sendtest_user.jsp" %>
	<!-- //테스트메일발송팝업 -->
	
	<!-- 캠페인선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/ems/cam/pop/pop_campaign.jsp" %>
	<!-- //캠페인선택 팝업 -->
	
	<!-- 템플릿선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/ems/cam/pop/pop_template.jsp" %>
	<!-- //템플릿선택 팝업 -->
	
	<!-- 수신자그룹선택 팝업// -->
	<%@ include file="/WEB-INF/jsp/ems/cam/pop/pop_segment.jsp" %>
	<!-- //수신자그룹선택 팝업 -->
	
	<!-- HTML등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_html.jsp" %>
	<!-- //HTML등록 팝업 -->
	
	<!-- 파일등록(수신자그룹등록) 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_seg.jsp" %>
	<!-- //파일등록(수신자그룹등록) 팝업 -->
	
	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp" %>
	<!-- //웹에이전트미리보기 팝업 -->
	
</body>
</html>