<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.24
	*	설명 : 단기메일발송 신규등록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/ems/cam/mailAddP.js'/>"></script>
<script type="text/javascript">var globalApprLineYn = "N";</script>

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
					<h2>단기메일발송 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="mailInfoForm" name="mailInfoForm" method="post">
					<input type="hidden" id="taskNo" name="taskNo" value="0">
					<input type="hidden" id="subTaskNo" name="subTaskNo" value="0">
					<!-- 신규메일 등록에 사용되는 값들 -->
					<c:set var="channel" value=""/>
					<c:set var="nmMerge" value=""/>
					<c:set var="idMerge" value=""/>
					<c:set var="encoding" value=""/>
					<c:if test="${fn:length(channelList) > 0}">
						<c:forEach items="${channelList}" var="chnl" varStatus="chnlStatus">
							<c:if test="${chnlStatus.index == 0}">
								<c:set var="channel" value="${chnl.cd}"/>
							</c:if>
						</c:forEach>
					</c:if>
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
					<c:if test="${fn:length(encodingList) > 0}">
						<c:forEach items="${encodingList}" var="enc" varStatus="encStatus">
							<c:if test="${encStatus.index == 0}">
								<c:set var="encoding" value="${enc.cd}"/>
							</c:if>
						</c:forEach>
					</c:if>
					<input type="hidden" id="composerValue" name="composerValue">
					<input type="hidden" id="channel"       name="channel"     value='<c:out value='${channel}'/>'>
					<input type="hidden" id="campNo"        name="campNo"      value="0">
					<input type="hidden" id="campTy"        name="campTy"      value="">
					<input type="hidden" id="nmMerge"       name="nmMerge"     value="$:<c:out value='${nmMerge}'/>:$">
					<input type="hidden" id="idMerge"       name="idMerge"     value="$:<c:out value='${idMerge}'/>:$">
					<input type="hidden" id="aliasFileNm"   name="aliasFileNm" value="">
					
					<!--  단기메일 화면숨김 고정값 -->
					<input type="hidden" id="contTy"     name="contTy"     value="000">
					<input type="hidden" id="isSendTerm" name="isSendTerm" value="N">
					<input type="hidden" id="linkYn"     name="linkYn"     value="N">
					<input type="hidden" id="respLog"    name="respLog"    value="31">
					
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
										<label class="required">예약일시</label>
										<div class="list-item">
											<div class="datepickertimebox">
												<div class="datepicker">
													<label>
														<input type="text" id="sendYmd" name="sendYmd" readonly>
													</label>
												</div>
												<div class="select">
													<jsp:useBean id="currTime" class="java.util.Date"/>
													<fmt:formatDate var="currHour" value="${currTime}" pattern="HH"/>
													<fmt:formatDate var="currMin" value="${currTime}" pattern="mm"/>
													<select id="sendHour" name="sendHour" title="시간 선택">
														<c:forEach begin="0" end="23" var="hour">
															<option value="<c:out value='${hour}'/>"<c:if test="${hour == currHour}"> selected</c:if>><c:out value='${hour}'/>시</option>
														</c:forEach>
													</select>
												</div>
												<div class="select">
													<select id="sendMin" name="sendMin" title="분 선택">
														<c:forEach begin="0" end="59" var="min">
															<option value="<c:out value='${min}'/>"<c:if test="${min == currMin}"> selected</c:if>><c:out value='${min}'/>분</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</li>
									<li>
										<label class="required">캠페인명</label>
										<div class="list-item">
											<div class="select">
												<select id="campInfo" name="campInfo" onchange="goCamp();" title="캠페인명 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(campList) > 0}">
														<c:forEach items="${campList}" var="camp">
															<option value="<c:out value='${camp.campNo}|${camp.campTy}|${camp.campTyNm}'/>"><c:out value='${camp.campNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
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
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<div class="select" style="width:calc(100% - 75px);">
												<select id="segNoc" name="segNoc" onchange="goMerge();" title="수신자그룹 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(segList) > 0}">
														<c:forEach items="${segList}" var="seg">
															<option value="<c:out value='${seg.segNo}|${seg.mergeKey}'/>"><c:out value='${seg.segNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
											<button type="button" class="btn fullblue" onclick="goSegInfoMail('');">미리보기</button>
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
																<option value="<c:out value='${dept.deptNo}'/>"><c:out value='${dept.deptNm}'/></option>
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
													</select>
												</div>
											</div>
										</li>
									</c:if>
									<li>
										<label>발송자명</label>
										<div class="list-item">
											<p class="inline-txt line-height40" id="txtMailFromNm"><c:out value='${userInfo.mailFromNm}'/></p>
										</div>
									</li>
									<li>
										<label>옵션</label>
										<div class="list-item">
											<label for="respYn"><input type="checkbox" id="respYn" name="respYn" value="Y" checked><span>수신확인</span></label>
											<button type="button" class="btn fullblue" style="margin-right:10px;" onclick="popEnvSetting();">환경설정</button>
											<div class="marketingbox">
												<label for="tempMktGb"><input type="checkbox" id="tempMktGb" name="tempMktGb" value="Y" onclick="checkMktGb();"><span>마케팅 수신 동의유형</span></label>
												<div class="select">
													<select id="mailMktGb" name="mailMktGb" onchange="changeMktGb();" title="마케팅수신동의유형 선택">
														<option value="">선택</option>
														<c:if test="${fn:length(mktGbList) > 0}">
															<c:forEach items="${mktGbList}" var="mktGb">
																<option value="<c:out value='${mktGb.cd}'/>"><c:out value='${mktGb.cdNm}'/></option>
															</c:forEach>
														</c:if>
													</select>
												</div>
											</div>
										</div>
									</li>
									<li>
										<label>웹에이전트</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label" id="txtWebAgentUrl">URL(첨부파일) 형식이 지정되지 않았습니다.</p>
												<button type="button" class="btn fullblue" onclick="popWebAgent();">등록</button>
											</div>
										</div>
									</li>
									<li>
										<label>머지입력</label>
										<div class="list-item">
											<div class="select" style="width:calc(100% - 102px);">
												<select id="mergeKey" name="mergeKey" title="머지입력 선택">
												</select>
											</div>
											<button type="button" class="btn fullblue" onclick="goTitleMerge();">제목</button>
											<button type="button" class="btn fullblue" onclick="goContMerge();">본문</button>
										</div>
									</li>
									<li>
										<label>발송결재라인</label>
										<div class="list-item">
											<div class="filebox">
												<p class="label" id="txtApprovalLineYn">발송결재라인이 등록되지 않았습니다.</p>
												<button type="button" class="btn fullblue" onclick="popMailApproval();">설정</button>
											</div>
										</div>
									</li>
									<li>
										<label>발송결과 종별</label>
										<div class="list-item">
											<div class="select">
												<select id="sndTpeGb" name="sndTpeGb" title="발송결과종별 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(sndTpeList) > 0}">
														<c:forEach items="${sndTpeList}" var="sndTpe">
															<option value="<c:out value='${sndTpe.cd}'/>"><c:out value='${sndTpe.cdNm}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li class="col-full">
										<label class="required">메일명</label>
										<div class="list-item">
											<input type="text" id="taskNm" name="taskNm" placeholder="메일명을 입력해주세요.">
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
									<button type="button" class="btn" onclick="fn.popupOpen('#popup_html');">HTML등록</button>
								</div>
								<!-- //버튼 -->
							</div>
							
							<div class="list-area">
								<ul>
									<li class="col-full">
										<label class="required">메일 제목</label>
										<div class="list-item">
											<input type="text" id="mailTitle" name="mailTitle" placeholder="메일 제목을 입력해주세요.">
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
											<ul class="filelist" id="mailAttachFileList"></ul>
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
							<button type="button" class="btn big fullblue" onclick="goMailAdd();">등록</button>
							<button type="button" class="btn big" onclick="goMailCancel();">취소</button>
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
	
	<!-- HTML등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_html.jsp" %>
	<!-- //HTML등록 팝업 -->
	
	<!-- 파일등록 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_upload_file.jsp" %>
	<!-- //파일등록 팝업 -->
	
</body>
</html>
