<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.09.01
	*	설명 : 자동메일 서비스 신규등록 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_rns.jsp" %>
<%@ taglib prefix="crypto" uri="/WEB-INF/tlds/crypto.tld" %>

<script type="text/javascript" src="<c:url value='/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/rns/svc/serviceAddP.js'/>"></script>

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
					<h2>자동메일 서비스 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="serviceInfoForm" name="serviceInfoForm" method="post">
					<input type="hidden" id="serviceContent" name="serviceContent">
					<input type="hidden" name="useYn" value="Y">
					<!--  실시간 이메일 화면숨김 고정값 -->
					<input type="hidden" id="linkYn"     	name="linkYn"     value="N">
					<input type="hidden" id="respLog"    	name="respLog"    value="31">
					<input type="hidden" id="approvalYn"    name="approvalYn"    value="N">
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
													<!-- <option value="">선택</option> -->
													<c:if test="${fn:length(typeList) > 0}">
														<c:forEach items="${typeList}" var="type">
															<option value="<c:out value='${type.cd}'/>"<c:if test="${'000' eq type.cd}"> selected</c:if>><c:out value='${type.cdNm}'/></option>
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
										<label class="required">발송자명</label>
										<div class="list-item">
											<input type="text" name="sname" value="<c:out value='${userInfo.mailFromNm}'/>">
											<%-- <p class="inline-txt line-height40" id="txtSname"><c:out value='${userInfo.mailFromNm}'/></p> --%>
										</div>
									</li>
									<li>
										<label class="required">발송자 이메일</label>
										<div class="list-item">
											<input type="text" name="smail" value="<crypto:decrypt colNm='MAIL_FROM_EM' data='${userInfo.mailFromEm}'/>">
											<%-- <p class="inline-txt line-height40" id="txtSmail"><crypto:decrypt colNm="MAIL_FROM_EM" data="${userInfo.mailFromEm}"/></p> --%>
										</div>
									</li>
									<li>
										<label>보안메일</label>
										<div class="list-item">
											<div class="filebox" style="width:100%;">
												<p class="label" id="txtWebAgentUrl" style="width:230px;">형식이 지정되지 않았습니다.</p>
												<button type="button" class="btn fullpurple" onclick="popWebAgent();">등록</button>
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
												<p class="label bg-gray" style="width:calc(100% - 75px)" id="txtApprovalLineYn">발송결재라인이 등록되지 않았습니다.</p>
												<button type="button" class="btn fullpurple" onclick="popMailApproval();">결재등록</button>
											</div>
										</div>
									</li>
									<li>
										<label>템플릿코드</label>
										<div class="list-item">
											<div class="filebox" style="width:100%;">
												<input type="text" id="eaiCampNo" name="eaiCampNo" maxlength="40" placeholder="템플릿코드를 입력해주세요." style="width:89%;">
												<button type="button" class="btn fullpurple" id="chkEaiCampNo" onclick="checkEaiCampNo();" value="N">중복검사</button>
											</div>
										</div>
									</li>
									<li class="col-full">
										<label class="required">서비스명</label>
										<div class="list-item">
											<input type="text" id="tnm" name="tnm" placeholder="서비스명을 입력해주세요.">
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
									<li>
										<label class="required">메일 제목</label>
										<div class="list-item">
											<input type="text" id="emailSubject" name="emailSubject" placeholder="메일 제목을 입력해주세요.">
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
											<ul class="filelist" id="mailAttachFileList"></ul>
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
							<button type="button" class="btn big fullpurple" onclick="goServiceAdd();">등록</button>
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

	<!-- 웹에이전트미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_webagent.jsp" %>
	<!-- //웹에이전트미리보기 팝업 -->
	
</body>
</html>
