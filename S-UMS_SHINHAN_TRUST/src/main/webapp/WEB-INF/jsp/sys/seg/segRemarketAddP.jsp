<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.18
	*	설명 : 수신자그룹 등록(리타게팅) 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/seg/segRemarketAddP.js'/>"></script>

<body>
	<c:set var="ID"/>
	<c:set var="NAME"/>
	<c:set var="EMAIL"/>
	<c:if test="${fn:length(mergeKeyList) > 0}">
		<c:forEach items="${mergeKeyList}" var="mergeKey" varStatus="status">
			<c:choose>
				<c:when test="${status.index == 0}">
					<c:set var="EMAIL" value="${mergeKey.cdNm}"/>
				</c:when>
				<c:when test="${status.index == 1}">
					<c:set var="NAME" value="${mergeKey.cdNm}"/>
				</c:when>
				<c:when test="${status.index == 2}">
					<c:set var="ID" value="${mergeKey.cdNm}"/>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</c:if>
	<div id="wrap" class="sys">

		<!-- lnb// -->
		<div id="lnb">
			<!-- LEFT MENU -->
			<%@ include file="/WEB-INF/jsp/inc/menu_sys.jsp" %>
			<!-- LEFT MENU -->
		</div>
		<!-- //lnb -->

		<!-- content// -->
		<div id="content">

			<!-- cont-head// -->
			<section class="cont-head">
				<div class="title">
					<h2>수신자그룹 신규등록</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->

			<!-- cont-body// -->
			<section class="cont-body">

				<form id="segInfoForm" name="segInfoForm" method="post">
					<input type="hidden" id="page" name="page" value="1">
					<input type="hidden" id="dbConnNo" name="dbConnNo" value="0">
					<input type="hidden" id="segNo" name="segNo" value="0">
					<input type="hidden" id="mergeKey" name="mergeKey" value="<c:out value='${ID}'/>,<c:out value='${EMAIL}'/>,<c:out value='${NAME}'/>">
					<input type="hidden" id="createTy" name="createTy"  value='004'>
					<input type="hidden" id="emsuseYn" name="emsuseYn" value="Y">
					<input type="hidden" id="taskNo" name="taskNo" value="0">
					<input type="hidden" id="subTaskNo" name="subTaskNo" value="0">
					<input type="hidden" id="orderbySql" name="orderbySql" value="">
					<input type="hidden" id="segFlPath" name="segFlPath" value="">
					<input type="hidden" id="mergeCol" name="mergeCol" value='NEO_SENDLOG.CUST_ID,NEO_SENDLOG.CUST_EM,NEO_SENDLOG.CUST_NM'>
					<input type="hidden" id="srcWhere" name="srcWhere"  readonly>
					<fieldset>
						<legend>수신자그룹 신규등록</legend>

						<!-- tab// -->
						<div class="tab">
							<div class="tab-menu col4">
								<a href="javascript:goCreateTy('003');">파일연동</a>
								<a href="javascript:goCreateTy('000');">추출조건</a>
								<a href="javascript:goCreateTy('002');">직접SQL</a>
								<a href="javascript:goCreateTy('004');" class="active">리타게팅</a>
							</div>
							<div class="tab-cont">

								<!-- tab-cont : 리타게팅// -->
								<div class="active">
									<!-- 조회// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">조건</h3>
										</div>
										
										<div class="list-area">
											<ul>
												<li>
													<label class="required">리타게팅</label>
													<div class="list-item">
														<div class="select">
															<select id="gubun" name="gubun" title="리타게팅 선택">
																<option value="1" selected>메일</option>
															</select>
														</div>
													</div>
												</li>
												<li>
													<label class="required">메일명</label>
													<div class="list-item">
														<input type="text" id="taskNm" name="taskNm" placeholder="메일명을 선택해주세요." style="width:calc(100% - 52px);" onclick="openInfo('1');" readonly>
														<button type="button" class="btn fullred" onclick="openInfo('1');">검색</button>
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
																<select id="userId" name="userId" title="사용자 선택">
																	<option value="">선택</option>
																</select>
															</div>
														</div>
													</li>
												</c:if>
												<li class="col-full">
													<label class="required">수신자그룹</label>
													<div class="list-item">
														<input type="text" id="segNm" name="segNm" placeholder="수신자그룹 명칭을 입력해주세요.">
													</div>
												</li>
												<li  class="col-full" style="margin-top:15px;">
													<label>설명</label>
													<div class="list-item">
														<textarea id="segDesc" name="segDesc" placeholder="수신자그룹 설명을 입력해주세요."></textarea>
													</div>
												</li>
											</ul>
			
										</div>
									</div>
									<!-- //조회 -->
			
									<!-- 추출조건 선택// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">추출조건 선택</h3>
										</div>
										
										<div class="list-area">
											<ul>
												<li class="col-full">
													<label class="required">1차 조건</label>
													<div class="list-item">
														<!-- 라디오버튼 -->
														<label for="rdo_01"><input type="radio" id="rdo_01" name="firstWhere" value="001" onclick="goSelect();"><span>성공</span></label>
														<label for="rdo_02"><input type="radio" id="rdo_02" name="firstWhere" value="002" onclick="goSelect();"><span>오픈</span></label>
														<label for="rdo_03"><input type="radio" id="rdo_03" name="firstWhere" value="003" onclick="goSelect();"><span>실패</span></label>
													</div>
												</li>
												<li  class="col-full" style="margin-top:10px;">
													<label class="required">2차 조건</label>
													<div class="list-item">
														<!-- 체크 박스 -->
														<label for="chk_01"><input type="checkbox" id="chk_01" name="secondWhere" value="001" onclick="fFail();"><span>생성에러</span></label>
														<label for="chk_02"><input type="checkbox" id="chk_02" name="secondWhere" value="002" onclick="fFail();"><span>이메일문법오류</span></label>
														<label for="chk_03"><input type="checkbox" id="chk_03" name="secondWhere" value="003" onclick="fFail();"><span>도메인없음</span></label>
														<label for="chk_04"><input type="checkbox" id="chk_04" name="secondWhere" value="004" onclick="fFail();"><span>S/W네트워크에러</span></label>
														<label for="chk_05"><input type="checkbox" id="chk_05" name="secondWhere" value="005" onclick="fFail();"><span>H/W네트워크에러</span></label>
														<label for="chk_06"><input type="checkbox" id="chk_06" name="secondWhere" value="006" onclick="fFail();"><span>트랜잭션에러</span></label>
														<label for="chk_07"><input type="checkbox" id="chk_07" name="secondWhere" value="007" onclick="fFail();"><span>스팸차단</span></label>
														<label for="chk_08"><input type="checkbox" id="chk_08" name="secondWhere" value="008" onclick="fFail();"><span>메일박스부족</span></label>
														<label for="chk_09"><input type="checkbox" id="chk_09" name="secondWhere" value="009" onclick="fFail();"><span>계정없음</span></label>
													</div>
												</li>
			
												<li class="col-full">
													<label>SELECT</label>
													<div class="list-item">
														<textarea id="selectSql" name="selectSql" readonly>NEO_SENDLOG.CUST_ID AS <c:out value="${ID}"/>, NEO_SENDLOG.CUST_EM AS <c:out value="${EMAIL}"/>, NEO_SENDLOG.CUST_NM AS <c:out value="${NAME}"/></textarea>
													</div>
												</li>
												<li class="col-full">
													<label>FROM</label>
													<div class="list-item">
														<textarea id="fromSql" name="fromSql" readonly></textarea>
													</div>
												</li>
												<li class="col-full">
													<label>WHERE</label>
													<div class="list-item">
														<textarea id="whereSql" name="whereSql" readonly></textarea>
													</div>
												</li>
												<li class="col-full">
													<label onclick="goSegCnt();">수신그룹<br>미리보기</label>
													<div class="list-item">
														<p class="inline-txt color-gray" id="txtTotCnt">0명</p>
														<input type="hidden" id="totCnt" name="totCnt" value="0"/>
														<button type="button" class="btn fullred" onclick="goSegInfo();">미리보기</button>
													</div>
												</li>
											</ul>
			
										</div>
									</div>
									<!-- 추출조건 선택// -->

									<!-- btn-wrap// -->
									<div class="btn-wrap">
										<button type="button" class="btn big fullred" onclick="goRemarketAdd();">등록</button>
										<button type="button" class="btn big" onclick="goCancel();">취소</button>
									</div>
									<!-- //btn-wrap -->
								
								</div>
								<!-- //tab-cont : 리타게팅 -->
								
							</div>
						</div>
						<!-- //tab -->
						
							
					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<!-- 메일검색팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_mail_search_sys.jsp" %>
	<!-- //메일검색팝업팝업 -->
	
	<!-- 미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_sys.jsp" %>
	<!-- //미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason_sys.jsp" %>
	<!-- //조회사유팝업 -->
	
</body>
</html>
