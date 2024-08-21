<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.02
	*	설명 : 자동메일 서비스 정보수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_rns.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/rns/svc/serviceUpdateP.js'/>"></script>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function(){
		setServiceContent("<c:out value='${serviceInfo.contentsTyp}'/>","<c:out value='${serviceInfo.contentsPath}'/>");
	},1000);
});
rnsStatus = "<c:out value='${serviceInfo.status}'/>";
rnsWorkStatus = "<c:out value='${serviceInfo.workStatus}'/>";
rnsApprLineYn = "<c:out value='${serviceInfo.approvalLineYn}'/>";
rnsApprProcYn = "<c:out value='${serviceInfo.approvalProcYn}'/>";
</script>

<body>
	<div id="wrap" class="rns">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_rns.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>자동메일 서비스 정보수정</h2>
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
					<input type="hidden" name="searchTnm" value="<c:out value='${searchVO.searchTnm}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" name="searchContentsTyp" value="<c:out value='${searchVO.searchContentsTyp}'/>">
					<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
					<input type="hidden" name="tid" value="<c:out value='${searchVO.tid}'/>">
					<input type='hidden' id="tids" name='tids' value="<c:out value='${searchVO.tid}'/>">
					<input type="hidden" id="status" name="status">
				</form>
				
				<form id="serviceInfoForm" name="serviceInfoForm" method="post">
					<input type="hidden" id="tid" name="tid" value="<c:out value='${serviceInfo.tid}'/>">
					<input type="hidden" id="serviceContent" name="serviceContent">
					<input type="hidden" name="useYn" value="<c:out value='${serviceInfo.useYn}'/>">
					<input type="hidden" name="contentsPath" value="<c:out value='${serviceInfo.contentsPath}'/>">
					<input type="hidden" id="approvalYn" name="approvalYn" value="$:<c:out value='${serviceInfo.approvalLineYn}'/>:$">
					
					<fieldset>
						<legend>조건 및 메일 내용</legend>

						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조건</h3>
								<span class="required">*필수입력 항목</span>
							</div>
							
							<div class="list-area">
								<ul>
									<li>
										<label>템플릿명</label>
										<div class="list-item">
											<div class="select">
												<select id="tempFlPath" name="tempFlPath" onchange="goTemplate();" title="템플릿명 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(tempList) > 0}">
														<c:forEach items="${tempList}" var="temp">
															<option value="<c:out value='${temp.tempFlPath}'/>"><c:out value='${temp.tempNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label class="required">콘텐츠타입</label>
										<div class="list-item">
											<div class="select">
												<select id="contentsTyp" name="contentsTyp" title="콘텐츠타입 선택" disabled>
													<c:if test="${fn:length(typeList) > 0}">
														<c:forEach items="${typeList}" var="type">
															<option value="<c:out value='${type.cd}'/>"<c:if test="${type.cd eq serviceInfo.contentsTyp}"> selected</c:if>><c:out value='${type.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
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
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == serviceInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
																<option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId eq serviceInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</li>
									</c:if>
									<li>
										<label class="required">발송자명</label>
										<div class="list-item">
											<input type="hidden" name="sid" value="<c:out value='${serviceInfo.sid}'/>">
											<input type="text" name="sname" value="<c:out value='${serviceInfo.sname}'/>">
											<%-- <p class="inline-txt line-height40"><c:out value='${serviceInfo.sname}'/></p> --%>
										</div>
									</li>
									<li>
										<label class="required">발송자 이메일</label>
										<div class="list-item">
											<input type="text" name="smail" value="<crypto:decrypt colNm="SMAIL" data="${serviceInfo.smail}"/>">
											<%-- <p class="inline-txt line-height40"><crypto:decrypt colNm="SMAIL" data="${serviceInfo.smail}"/></p> --%>
										</div>
									</li>
									<li>
										<label>보안메일</label>
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
												<button type="button" class="btn fullpurple" onclick="popWebAgent();">수정</button>
												<div class="select" style="display:none;">
													<select id="webAgentAttachYn" name="webAgentAttachYn" onchange="changeAttachYn();" title="보안첨부여부 선택">
														<option value="Y">첨부파일</option>
														<option value="N">본문삽입</option>
													</select>
												</div>
												<div class="select"style="width:100px;">
													<select id="secuAttTyp" name="secuAttTyp" title="문서유형 선택">
														<option value="NONE">NONE</option>
														<option value="PDF">PDF</option>
														<option value="EXCEL">EXCEL</option>
													</select>
												</div>
												<button type="button" class="btn fullpurple" onclick="popWebAgentPreview();">미리보기</button>
											</div>
										</div>
									</li> 
									<li>
										<label>발송결재라인</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label bg-gray"<c:if test="${'201' eq serviceInfo.workStatus}"> style="width:calc(100% - 102px);"</c:if> id="txtApprovalLineYn">
													<c:if test="${'Y' eq serviceInfo.approvalLineYn}">
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
													<c:if test="${'N' eq serviceInfo.approvalLineYn}">
														발송결재라인이 등록되지 않았습니다.
													</c:if>
												</p>
												<button type="button" class="btn fullpurple" onclick="popMailApproval();">수정</button>
												<c:if test="${'201' eq serviceInfo.workStatus}"> 
													<button type="button" class="btn" onclick="goRnsSubmitApproval();">상신</button>
												</c:if>
											</div>
										</div>
									</li> 
									<li>
										<label>템플릿코드</label>
										<div class="list-item">
											<div class="filebox" style="width:100%;">
												<input type="text" id="eaiCampNo" name="eaiCampNo" value="<c:out value='${serviceInfo.eaiCampNo}'/>" maxlength="40" placeholder="템플릿코드를 입력해주세요." style="width:89%;">
												<button type="button" class="btn fullpurple" id="chkEaiCampNo" onclick="checkEaiCampNo();" value="N">중복검사</button>
											</div>
										</div>
									</li>
									<li class="col-full">
										<label class="required">서비스명</label>
										<div class="list-item">
											<input type="text" id="tnm" name="tnm" value="<c:out value='${serviceInfo.tnm}'/>" placeholder="서비스명을 입력해주세요.">
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
									<c:if test="${'000' eq serviceInfo.status && '202' eq serviceInfo.workStatus && 'N' eq serviceInfo.approvalProcYn}">
										<button type="button" class="btn" onclick="goApprCancel();">결재취소</button>
									</c:if>
									<%-- 정상 또는 사용중인 항목에 대해서 결재 대기 또는 결재 진행만 테스트발송 가능 --%>
									<%--
									<c:if test="${'002' ne serviceInfo.status && ('201' eq serviceInfo.workStatus || '202' eq serviceInfo.workStatus)}">
										<button type="button" class="btn" onclick="popRnsTestSend();">테스트발송</button>
									</c:if>
									--%>
									<button type="button" class="btn" onclick="popRnsTestSend();">테스트발송</button>
									<button type="button" class="btn" onclick="fn.popupOpen('#popup_html');">HTML등록</button>
									<%--삭제가 아닌것중에 상태가 발송대기,결재대기,발송승인 상태에서 사용중지/복구/삭제 가능 --%>
									<c:if test="${'002' ne serviceInfo.status && '202' ne serviceInfo.workStatus}">
										<button type="button" class="btn<c:if test="${'001' eq serviceInfo.status}"> hide</c:if>" id="btnDisable" onclick="goDisable();">사용중지</button>
										<button type="button" class="btn<c:if test="${'000' eq serviceInfo.status}"> hide</c:if>" id="btnEnable" onclick="goEnable();">복구</button>
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
											<input type="text" id="emailSubject" name="emailSubject" value="<c:out value='${serviceInfo.emailSubject}'/>" placeholder="메일 제목을 입력해주세요.">
										</div>
									</li>
									<li>
										<label>함수입력</label>
										<div class="list-item">
											<div class="select" style="width:calc(100% - 102px);">
												<select id="mergeKey" name="mergeKey" title="함수입력 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(mergeList) > 0}">
														<c:forEach items="${mergeList}" var="merge">
															<option value="<c:out value='$:${merge.cdNm}:$'/>"><c:out value='${merge.cdDtl}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
											<button type="button" class="btn fullpurple" onclick="goTitleMerge();">제목</button>
											<button type="button" class="btn fullpurple" onclick="goContMerge();">본문</button>
										</div>
									</li>
									<li class="col-full">
										<!-- 에디터 영역// -->
										<div class="editbox">
											<textarea name="ir1" id="ir1" rows="10" cols="100" style="text-align: center; width: 100%; height: 400px; display: none; border: none;" ></textarea>
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
												//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
												//body_loaded();
											},
											fCreator: "createSEditor2"
											});
								
											function pasteHTML(obj) {
												var sHTML = "<img src='<c:url value='/img/upload'/>/"+obj+"'>";
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
								<ul>
									<li>
										<label>등록자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${serviceInfo.regNm}"/></p>
										</div>
									</li>
									<li>
										<label>수정자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${serviceInfo.upNm}"/></p>
										</div>
									</li>
									<li>
										<label>등록일시</label>
										<div class="list-item">
											<p class="inline-txt">
												<fmt:parseDate var="regDate" value="${serviceInfo.regDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="regDt" value="${regDate}" pattern="yyyy.MM.dd HH:mm"/>
												<c:out value="${regDt}"/>
											</p>
										</div>
									</li>
									<li>
										<label>수정일시</label>
										<div class="list-item">
											<p class="inline-txt">
												<fmt:parseDate var="upDate" value="${serviceInfo.upDt}" pattern="yyyyMMddHHmmss"/>
												<fmt:formatDate var="upDt" value="${upDate}" pattern="yyyy.MM.dd HH:mm"/>
												<c:out value="${upDt}"/>
											</p>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- //메일 내용 -->
						
						<!-- 웹에이전트팝업// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_web_agent_rns.jsp" %>
						<!-- //웹에이전트팝업 -->

						<!-- 발송결재라인정보팝업// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_approval.jsp" %>
						<!-- //발송결재라인정보팝업 -->
						
						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullpurple" onclick="goServiceUpdate('<c:out value='${serviceInfo.status}'/>');">수정</button>
							<button type="button" class="btn big" onclick="goServiceCancel();">취소</button>
						</div>
						<!-- //btn-wrap -->
							
					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	
	<iframe id="iFrmService" name="iFrmService" style="width:0px;height:0px;"></iframe>
	
	<!-- HTML등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_html_rns.jsp" %>
	<!-- //HTML등록 팝업 -->
	
	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_file_rns.jsp" %>
	<!-- //파일등록 팝업 -->
	
	<!-- RNS테스트메일발송팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_sendtest_user_rns.jsp" %>
	<!-- //테스트메일발송팝업 -->
	
	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp" %>
	<!-- //웹에이전트미리보기 팝업 -->
	
	<!-- 발송결재라인정보팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_rns_approval_info.jsp" %>
	<!-- //발송결재라인정보팝업 -->	
</body>
</html>
