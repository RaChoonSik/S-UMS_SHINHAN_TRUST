<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.07.25
	*	설명 : 연계서비스지정 수정화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_ems.jsp" %>

<script type="text/javascript" src="<c:url value='/js/ems/seg/segRemarketUpdateP.js'/>"></script>

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
					<h2>수신자그룹 정보수정(리타게팅)</h2>
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
					<input type="hidden" name="searchSegNm" value="<c:out value='${searchVO.searchSegNm}'/>">
					<input type="hidden" name="searchDeptNo" value="<c:out value='${searchVO.searchDeptNo}'/>">
					<input type="hidden" name="searchUserId" value="<c:out value='${searchVO.searchUserId}'/>">
					<input type="hidden" name="searchCreateTy" value="<c:out value='${searchVO.searchCreateTy}'/>">
					<input type="hidden" name="searchStatus" value="<c:out value='${searchVO.searchStatus}'/>">
					<input type="hidden" name="searchStartDt" value="<c:out value='${searchVO.searchStartDt}'/>">
					<input type="hidden" name="searchEndDt" value="<c:out value='${searchVO.searchEndDt}'/>">
					<input type="hidden" name="searchEmsuseYn" value="<c:out value='${searchVO.searchEmsuseYn}'/>">
					<input type="hidden" name="searchSmsuseYn" value="<c:out value='${searchVO.searchSmsuseYn}'/>">
					<input type="hidden" name="searchPushuseYn" value="<c:out value='${searchVO.searchPushuseYn}'/>">
				</form>
				<form id="segInfoForm" name="segInfoForm" method="post">
					<input type="hidden" id="page"       name="page" value="1">
					<input type="hidden" id="status"     name="status">
					<input type="hidden" id="dbConnNo"   name="dbConnNo" value="0">
					<input type="hidden" id="segNo"      name="segNo"  value="<c:out value='${segmentInfo.segNo}'/>">
					<input type="hidden" id="mergeKey"   name="mergeKey" value="<c:out value='${segmentInfo.mergeKey}'/>">
					<input type="hidden" id="mergeCol"   name="mergeCol" value="<c:out value='${segmentInfo.mergeCol}'/>">
					<input type="hidden" id="createTy"   name="createTy"  value="<c:out value='${segmentInfo.createTy}'/>">
					<input type="hidden" id="emsuseYn"   name="emsuseYn" value="<c:out value='${segmentInfo.emsuseYn}'/>">
					<input type="hidden" id="taskNo"     name="taskNo" value="<c:out value='${taskNo}'/>">
					<input type="hidden" id="subTaskNo"  name="subTaskNo" value="<c:out value='${subTaskNo}'/>">
					<input type="hidden" id="orderbySql" name="orderbySql"  value="<c:out value='${segmentInfo.orderbySql}'/>">
					<input type="hidden" id="segFlPath"  name="segFlPath" value="<c:out value='${segmentInfo.segFlPath}'/>">
					<input type="hidden" id="srcWhere"   name="srcWhere" value="<c:out value='${segmentInfo.srcWhere}'/>">
					<fieldset>
						<legend>조건 및 추출조건 선택</legend>

						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조건</h3>
								<span class="required">*필수입력 항목</span>
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
											<input type="text" id="taskNm" name="taskNm" value="<c:out value='${taskNm}'/>" readonly>
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
										                		<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${dept.deptNo == segmentInfo.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
										            	<c:if test="${fn:length(userList) > 0}">
										            		<c:forEach items="${userList}" var="user">
												                <option value="<c:out value='${user.userId}'/>"<c:if test="${user.userId == segmentInfo.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
															</c:forEach>
										            	</c:if>
										            </select>
												</div>
											</div>
										</li>
									</c:if>
									<li class="col-full">
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<input type="text" id="segNm" name="segNm" value="<c:out value='${segmentInfo.segNm}'/>" placeholder="수신자그룹 명칭을 입력해주세요.">
										</div>
									</li>
									<li class="col-full">
										<label>설명</label>
										<div class="list-item">
											<textarea id="segDesc" name="segDesc" placeholder="수신자그룹 설명을 입력해주세요."><c:out value='${segmentInfo.segDesc}'/></textarea>
										</div>
									</li>
								</ul>

							</div>
						</div>
						<!-- //조건 -->

						<!-- 추출조건 선택// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">추출조건 선택</h3>
								
								<!-- 버튼// -->
								<div class="btn-wrap">
									<c:if test="${'002' ne segmentInfo.status}">
										<button type="button" id="btnDisable" class="btn<c:if test="${'001' eq segmentInfo.status}"> hide</c:if>" onclick="goDisable();">사용중지</button>
										<button type="button" id="btnEnable" class="btn<c:if test="${'000' eq segmentInfo.status}"> hide</c:if>" onclick="goEnable();">복구</button>
										<button type="button" class="btn" onclick="goDelete();">삭제</button>
									</c:if>
								</div>
								<!-- //버튼 -->
							</div>
							
							<div class="list-area">
								<ul>
									<li class="col-full">
										<label class="required">1차 조건</label>
										<div class="list-item">
											<!-- 라디오버튼 -->
											<label for="rdo_01"><input type="radio" id="rdo_01" name="firstWhere" value="001" onclick="goSelect();"<c:if test="${'1|0' eq segmentInfo.segFlPath}"> checked</c:if>><span>성공</span></label>
											<label for="rdo_02"><input type="radio" id="rdo_02" name="firstWhere" value="002" onclick="goSelect();"<c:if test="${'2|0' eq segmentInfo.segFlPath}"> checked</c:if>><span>오픈</span></label>
											<label for="rdo_03"><input type="radio" id="rdo_03" name="firstWhere" value="003" onclick="goSelect();"<c:if test="${'3|'  eq fn:substring(segmentInfo.segFlPath,0,2)}"> checked</c:if>><span>실패</span></label>
										</div>
									</li>
									<li  class="col-full" style="margin-top:10px;">
										<label class="required">2차 조건</label>
										<div class="list-item">
											<c:set var="sw1" value=""/>
											<c:set var="sw2" value=""/>
											<c:set var="sw3" value=""/>
											<c:set var="sw4" value=""/>
											<c:set var="sw5" value=""/>
											<c:set var="sw6" value=""/>
											<c:set var="sw7" value=""/>
											<c:set var="sw8" value=""/>
											<c:set var="checkedArray" value="${fn:split(segmentInfo.segFlPath,'|')}"/>
											<c:forEach items="${checkedArray}" var="sw" varStatus="idx">
												<c:choose>
													<c:when test="${'001' eq sw}"><c:set var="sw1" value=" checked"/></c:when>
													<c:when test="${'002' eq sw}"><c:set var="sw2" value=" checked"/></c:when>
													<c:when test="${'003' eq sw}"><c:set var="sw3" value=" checked"/></c:when>
													<c:when test="${'004' eq sw}"><c:set var="sw4" value=" checked"/></c:when>
													<c:when test="${'005' eq sw}"><c:set var="sw5" value=" checked"/></c:when>
													<c:when test="${'006' eq sw}"><c:set var="sw6" value=" checked"/></c:when>
													<c:when test="${'007' eq sw}"><c:set var="sw7" value=" checked"/></c:when>
													<c:when test="${'008' eq sw}"><c:set var="sw8" value=" checked"/></c:when>
													<c:when test="${'009' eq sw}"><c:set var="sw9" value=" checked"/></c:when>
												</c:choose>
											</c:forEach>
											<!-- 체크 박스 -->
											<label for="chk_01"><input type="checkbox" id="chk_01" name="secondWhere" value="001" onclick="fFail();"<c:out value="${sw1}"/>><span>생성에러</span></label>
											<label for="chk_02"><input type="checkbox" id="chk_02" name="secondWhere" value="002" onclick="fFail();"<c:out value="${sw2}"/>><span>이메일문법오류</span></label>
											<label for="chk_03"><input type="checkbox" id="chk_03" name="secondWhere" value="003" onclick="fFail();"<c:out value="${sw3}"/>><span>도메인없음</span></label>
											<label for="chk_04"><input type="checkbox" id="chk_04" name="secondWhere" value="004" onclick="fFail();"<c:out value="${sw4}"/>><span>S/W네트워크에러</span></label>
											<label for="chk_05"><input type="checkbox" id="chk_05" name="secondWhere" value="005" onclick="fFail();"<c:out value="${sw5}"/>><span>H/W네트워크에러</span></label>
											<label for="chk_06"><input type="checkbox" id="chk_06" name="secondWhere" value="006" onclick="fFail();"<c:out value="${sw6}"/>><span>트랜잭션에러</span></label>
											<label for="chk_07"><input type="checkbox" id="chk_07" name="secondWhere" value="007" onclick="fFail();"<c:out value="${sw7}"/>><span>스팸차단</span></label>
											<label for="chk_08"><input type="checkbox" id="chk_08" name="secondWhere" value="008" onclick="fFail();"<c:out value="${sw8}"/>><span>메일박스부족</span></label>
											<label for="chk_09"><input type="checkbox" id="chk_09" name="secondWhere" value="009" onclick="fFail();"<c:out value="${sw9}"/>><span>계정없음</span></label>
										</div>
									</li>
									<li class="col-full">
										<label>SELECT</label>
										<div class="list-item">
											<textarea id="selectSql" name="selectSql" placeholder="NEO_SENDLOG.CUST_ID AS ID, NEO_SENDLOG.CUST_EM AS EMAIL, NEO_SENDLOG.CUST_NM AS NAME" readonly><c:out value="${segmentInfo.selectSql}"/></textarea>
										</div>
									</li>
									<li class="col-full">
										<label>FROM</label>
										<div class="list-item">
											<textarea id="fromSql" name="fromSql" readonly><c:out value="${segmentInfo.fromSql}"/></textarea>
										</div>
									</li>
									<li class="col-full">
										<label>WHERE</label>
										<div class="list-item">
											<textarea id="whereSql" name="whereSql" readonly><c:out value="${segmentInfo.whereSql}"/></textarea>
										</div>
									</li>
									<li class="col-full">
										<label onclick="goSegCnt();">수신그룹<br>미리보기</label>
										<div class="list-item">
											<p class="inline-txt color-gray"><c:out value='${segmentInfo.totCnt}'/>명</p>
											<input type="hidden" id="totCnt" name="totCnt" value="<c:out value='${segmentInfo.totCnt}'/>"/>
											<button type="button" class="btn fullblue" onclick="goSegInfo();">미리보기</button>
										</div>
									</li>
								</ul>

								<ul>
									<li>
										<label>등록자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${segmentInfo.regNm}"/></p>
										</div>
									</li>
									<li>
										<label>수정자</label>
										<div class="list-item">
											<p class="inline-txt"><c:out value="${segmentInfo.upNm}"/></p>
										</div>
									</li>
									<li>
										<label>등록일시</label>
										<div class="list-item">
											<fmt:parseDate var="regDt" value="${segmentInfo.regDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p class="inline-txt"><c:out value="${regDt}"/></p>
										</div>
									</li>
									<li>
										<label>수정일시</label>
										<div class="list-item">
											<fmt:parseDate var="upDt" value="${segmentInfo.upDt}" pattern="yyyyMMddHHmmss"/>
											<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
											<p class="inline-txt"><c:out value="${upDt}"/></p>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<!-- 추출조건 선택// -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullblue" onClick="goRemarketUpdate();">수정</button>
							<button type="button" class="btn big" onclick="goSegList();">취소</button>
						</div>
						<!-- //btn-wrap -->

					</fieldset>
				</form>
				
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<!-- 미리보기팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg.jsp" %>
	<!-- //미리보기팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason.jsp" %>
	<!-- //조회사유팝업 -->
	
</body>
</html>
