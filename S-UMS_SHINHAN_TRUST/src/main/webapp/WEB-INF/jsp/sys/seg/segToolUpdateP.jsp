<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.20
	*	설명 : 발송대상(세그먼트) 추출도구이용 수정 화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/seg/segToolUpdateP.js'/>"></script>
<script type="text/javascript">
//조인
function getJoinTbl() {
    var temp = "";
    var pos = -1;

    var returnValue = "";
    var joinRelValue = new Array();
    var joinTblList = new Array();
    var joinRel = new Array();
    var n = 0;

    var fromTblList = new Array();      // From절에서 가져오 테이블 리스트
    
<c:if test="${fn:length(metaJoinList) > 0}">
	<c:set var="cnt" value="${0}"/>
	<c:forEach items="${metaJoinList}" var="metaJoin">
		joinTblList[<c:out value='${cnt}'/>] = "<c:out value='${metaJoin.mstTblNm}'/>,<c:out value='${metaJoin.forTblNm}'/>";
		joinRel[<c:out value='${cnt}'/>] = "<c:out value='${metaJoin.mstTblNm}'/>.<c:out value='${metaJoin.mstColNm}'/> = <c:out value='${metaJoin.forTblNm}'/>.<c:out value='${metaJoin.forColNm}'/>";
		<c:set var="cnt" value="${cnt+1}"/>
		joinTblList[<c:out value='${cnt}'/>] = "<c:out value='${metaJoin.forTblNm}'/>,<c:out value='${metaJoin.mstTblNm}'/>";
		joinRel[<c:out value='${cnt}'/>] = "<c:out value='${metaJoin.mstTblNm}'/>.<c:out value='${metaJoin.mstColNm}'/> = <c:out value='${metaJoin.forTblNm}'/>.<c:out value='${metaJoin.forColNm}'/>";
		<c:set var="cnt" value="${cnt+2}"/>
	</c:forEach>
</c:if>

    // [시작] From 절의 테이블을 fromTblList 배열에 넣어 놓는다.
    temp = $("#fromSql").val().toUpperCase();
    pos = temp.indexOf(",");
    while(pos != -1) {
        fromTblList[n] =  trim(temp.substring(0, pos));
        temp = temp.substring(pos+1);
        pos = temp.indexOf(",");
        n++;
    }
    fromTblList[n] = trim(temp);
    // [끝] From 절의 테이블을 fromTblList 배열에 넣어 놓는다.

    n = 0;
    // formTblList을 두개씩 나두어 조합을 하여 모든 경우의 수를 만들어 joinTblList와 비교를 한다.
    // formTblList는 A,B와 B,A의 경우를 같은 걸로 보고 A,B만 비교한다.
    for(ni = 0; ni < joinTblList.length; ni++) {
        for(fromTblList_i = 0; fromTblList_i < fromTblList.length; fromTblList_i++) {
            for(fromTblList_j = fromTblList_i + 1; fromTblList_j < fromTblList.length; fromTblList_j++) {
                if(joinTblList[ni] == (fromTblList[fromTblList_i] + "," + fromTblList[fromTblList_j])) {
                    joinRelValue[n] = joinRel[ni];
                    n++;
                }
            }
        }
    }

    for(rn = 0; rn < joinRelValue.length; rn++) {
        if(rn == joinRelValue.length - 1) returnValue += joinRelValue[rn];
        else returnValue += joinRelValue[rn] + " AND ";
    }
    return returnValue;
}
</script>

<body onload="goLoad('<c:out value='${srcWhere}'/>')">
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
					<h2>수신자그룹 정보수정(추출조건)</h2>
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
					<input type="hidden" name="dbConnNo" value="<c:out value='${searchVO.dbConnNo}'/>">
					<input type="hidden" name="tblNo"/>
					<input type="hidden" name="colNo"/>
					<input type="hidden" name="status"/>
				</form>
				<form id="segInfoForm" name="segInfoForm" method="post">
					<input type="hidden" id="page"      name="page" value="1">
					<input type="hidden" id="dbConnNo"  name="dbConnNo" value="<c:out value='${segmentInfo.dbConnNo}'/>">
					<input type="hidden" id="segNo"     name="segNo" value="<c:out value='${segmentInfo.segNo}'/>">
					<input type="hidden" id="status"    name="status" value="<c:out value='${segmentInfo.status}'/>">
					<input type="hidden" id="mergeKey"  name="mergeKey" value="<c:out value='${segmentInfo.mergeKey}'/>">
					<input type="hidden" id="mergeCol"  name="mergeCol" value="<c:out value='${segmentInfo.mergeCol}'/>">
					<input type="hidden" id="srcWhere" name="srcWhere" value="<c:out value='${segmentInfo.srcWhere}'/>">
					<input type="hidden" id="createTy"  name="createTy" value="<c:out value='${segmentInfo.createTy}'/>">
					<input type="hidden" id="dataInfo"  name="dataInfo" >
					<input type="hidden" id="targetService"  name="targetService" >
					
					
					<!-- 커리 조건 선택에 필요한 값들 -->
					<input type="hidden" id="tblNo"     name="tblNo" style="width:0;" readonly>
					<input type="hidden" id="tblNm"     name="tblNm" style="width:0;" readonly>
					<input type="hidden" id="tblAlias"  name="tblAlias" style="width:0;" readonly>
					
					<input type="hidden" id="colNo"     name="colNo" style="width:0;" readonly>
					<input type="hidden" id="colNm"     name="colNm" style="width:0;" readonly>
					<input type="hidden" id="colDataTy" name="colDataTy" style="width:0;" readonly>
					<input type="hidden" id="colAlias"  name="colAlias" style="width:0;" readonly>
					
					<input type="hidden" id="operNo"    name="operNo" style="width:0;" readonly>
					<input type="hidden" id="operNm"    name="operNm" style="width:0;" readonly>
					<input type="hidden" id="operAlias" name="operAlias" style="width:0;" readonly>
					
					<input type="hidden" id="valueNo"    name="valueNo" style="width:0;" readonly>
					<input type="hidden" id="valueNm"    name="valueNm" style="width:0;" readonly>
					<input type="hidden" id="valueAlias" name="valueAlias" style="width:0;" readonly>
					
					<fieldset>
						<legend>조건 및 추출조건 등록, 미리보기</legend>

						<!-- 조건// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">조건</h3>
								<span class="required">*필수입력 항목</span>
							</div>
							
							<div class="list-area">
								<ul>
									<li>
										<label class="required">Connection</label>
										<div class="list-item">
											<div class="select">
												<select id="dbConnNo" name='dbConnNo' onchange='goReload()' title="Connection 선택">
													<c:if test="${fn:length(dbConnList) > 0}">
														<c:forEach items="${dbConnList}" var="dbConn">
															<option value="<c:out value='${dbConn.dbConnNo}'/>"<c:if test="${dbConn.dbConnNo == segmentInfo.dbConnNo}"> selected</c:if>><c:out value='${dbConn.dbConnNm}'/></option>
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
																<option value="<c:out value='${dept.deptNo}'/>"<c:if test="${segmentInfo.deptNo == dept.deptNo}"> selected</c:if>><c:out value='${dept.deptNm}'/></option>
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
																	<option value="<c:out value='${user.userId}'/>"<c:if test="${segmentInfo.userId eq user.userId}"> selected</c:if>><c:out value='${user.userNm}'/></option>
																</c:forEach>
															</c:if>
													</select>
												</div>
											</div>
										</li>
									</c:if>
									<%-- 디자인 깨짐 방지 --%>
									<c:if test="${'N' eq NEO_ADMIN_YN}">
										<li>
											<label></label>
											<div class="list-item">
											</div>
										</li>
									</c:if>
									<li>
										<label class="required">서비스유형</label>
										<div class="list-item">
											<label><input type="checkbox" id="emsuseYn" name="emsuseYn" value="Y"<c:if test="${'Y' eq segmentInfo.emsuseYn}"> checked</c:if>><span>EMS</span></label>
											<label><input type="checkbox" id="smsuseYn" name="smsuseYn" value="Y"<c:if test="${'Y' eq segmentInfo.smsuseYn}"> checked</c:if>><span>SMS</span></label>
											<label><input type="checkbox" id="pushuseYn" name="pushuseYn" value="Y"<c:if test="${'Y' eq segmentInfo.pushuseYn}"> checked</c:if>><span>PUSH</span></label>
										</div>
									</li>
									<li class="col-full">
										<label class="required">수신자그룹</label>
										<div class="list-item">
											<input type="text" id="segNm" name="segNm" value="<c:out value='${segmentInfo.segNm}'/>" placeholder="수신자그룹 명칭을 입력해주세요.">
										</div>
									</li>
									<li class="col-full">
										<label>설명</label>
										<div class="list-item">
											<textarea id="segDesc" name="segDesc" placeholder="수신자그룹 설명을 입력해주세요."><c:out value='${segmentInfo.segNm}'/></textarea>
										</div>
									</li>
								</ul>

							</div>
						</div>
						<!-- //조건 -->

						<!-- connection 테이블// -->
						<div class="groupbox col4" id="divMetaTableInfo"></div>
						<!-- //connection 테이블 -->

						<!-- 추출조건 등록// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">추출조건 등록</h3>
							</div>
							
							<div class="list-area col3">
								<ul>
									<li>
										<label>테이블</label>
										<div class="list-item">
											<div class="select">
												<select id="tblInfo" name="tblInfo" onchange="goTblSelect();" title="테이블 선택">
													<option value="">선택</option>
													<c:if test="${fn:length(metaTableList) > 0}">
														<c:forEach items="${metaTableList}" var="metaTable">
															<option value="<c:out value='${metaTable.tblNo}|${metaTable.tblNm}|${metaTable.tblAlias}'/>"><c:out value='${metaTable.tblAlias}'/></option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>칼럼</label>
										<div class="list-item">
											<div class="select">
							             		<select id="colInfo" name="colInfo" onchange="goColSelect();" title="칼럼 선택">
							             			<option value="">선택</option>
							             		</select>
											</div>
										</div>
									</li>
									<li>
										<label>추출값</label>
										<div class="list-item">
											<div class="select" id="valueInfoDisplay">
												<input type="text" id="valueInfo" name="valueInfo" onkeyup="goValueSelect();">
											</div>
										</div>
									</li>
									<li>
										<label>조건식</label>
										<div class="list-item">
											<div class="select">
												<select id="operInfo" name="operInfo" onchange="goOperSelect()" title="조건식 선택">
													<option value="">선택</option>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>정렬</label>
										<div class="list-item">
											<div class="select">
												<select id="sort" name="sort" onchange="goSortSelect()" title="정렬 선택">
													<option value="">선택</option>
													<option value="ASC|올림">올림</option>
													<option value="DESC|내림">내림</option>
												</select>
											</div>
										</div>
									</li>
									<li>
										<label>관계식</label>
										<div class="list-item">
											<div class="select">
												<select id="whereRel" name="whereRel" title="관계식 선택">
													<option value="">선택</option>
													<option value="AND">AND</option>
													<option value="OR">OR</option>
												</select>
											</div>
										</div>
									</li>
								</ul>

								<div class="btn-wrap">
									<button type="button" class="btn big fullred" onclick="fRelSelect();">등록</button>
								</div>
							</div>
						</div>
						<!-- //추출조건 등록 -->

						<!-- 추출조건 미리보기// -->
						<div class="graybox">
							<div class="title-area">
								<h3 class="h3-title">추출조건 미리보기</h3>
								
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
								<table class="grid type-black">
									<caption>그리드 정보</caption>
									<colgroup>
										<col style="width:7%;">
										<col style="width:auto;">
										<col style="width:15%;">
										<col style="width:15%;">
										<col style="width:15%;">
										<col style="width:15%;">
										<col style="width:15%;">
									</colgroup>
									<thead>
										<tr>
											<th scope="col"></th>
											<th scope="col">테이블</th>
											<th scope="col">칼럼</th>
											<th scope="col">추출값</th>
											<th scope="col">조건식</th>
											<th scope="col">정렬</th>
											<th scope="col">관계식</th>
										</tr>
									</thead>
									<tbody id="divConditional">
								</table>

								<ul>
									<li class="col-full">
										<label>SELECT</label>
										<div class="list-item">
											<textarea id="selectSql" name="selectSql" readonly><c:out value="${segmentInfo.selectSql}"/></textarea>
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
										<label>ORDER BY</label>
										<div class="list-item">
											<textarea id="orderbySql" name="orderbySql" readonly><c:out value="${segmentInfo.orderbySql}"/></textarea>
										</div>
									</li>
									<li class="col-full">
										<label onclick="goSegCnt();">수신그룹<br>미리보기</label>
										<div class="list-item">
											<p class="inline-txt color-gray" id="txtTotCnt"><c:out value='${segmentInfo.totCnt}'/>명</p>
											<input type="hidden" id="totCnt" name="totCnt" value="<c:out value='${segmentInfo.totCnt}'/>"/>
											<button type="button" class="btn fullred" onclick="goSegInfo();">미리보기</button>
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
						<!-- //추출조건 미리보기 -->

						<!-- btn-wrap// -->
						<div class="btn-wrap">
							<button type="button" class="btn big fullred" onclick="goSegToolUpdate();">수정</button>
							<button type="button" class="btn big" onclick="goSegList();">취소</button>
						</div>
						<!-- //btn-wrap -->
						
						<!-- 미리보기 팝업// -->
						<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg.jsp" %>
						<!-- //미리보기 팝업 -->
					</fieldset>
				</form>

			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>

	<!-- 미리보기 팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_preview_seg_sys.jsp" %>
	<!-- //미리보기 팝업 -->
	
	<!-- 조회사유팝업// -->
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_search_reason_sys.jsp" %>
	<!-- //조회사유팝업 -->
	
</body>
</html>
