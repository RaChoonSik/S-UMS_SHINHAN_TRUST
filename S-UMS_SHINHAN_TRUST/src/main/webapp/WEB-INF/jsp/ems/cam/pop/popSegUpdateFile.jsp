<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.10.15
	*	설명 : 메일작성 내부 수신자그룹 수정(파일)
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
	<form id="popSegSearchForm" name="popSegSearchForm" method="post">
		<input type="hidden" name="page" value="${searchVO.page}">
		<input type="hidden" name="searchStartDt" value="${searchVO.searchStartDt}">
		<input type="hidden" name="searchEndDt" value="${searchVO.searchEndDt}">
		<input type="hidden" name="searchSegNm" value="${searchVO.searchSegNm}">
		<input type="hidden" name="searchCreateTy" value="${searchVO.searchCreateTy}">
		<input type="hidden" name="searchEmsuseYn" value="${searchVO.searchEmsuseYn}">
		<input type="hidden" name="searchSmsuseYn" value="${searchVO.searchSmsuseYn}">
		<input type="hidden" name="searchPushuseYn" value="${searchVO.searchPushuseYn}">
	</form>
	<form id="popSegInfoFormMail" name="popSegInfoFormMail" method="post">
		<input type="hidden" name="segNo" value="<c:out value='${segInfo.segNo}'/>">
		<input type="hidden" name="deptNo" value="<c:out value='${segInfo.deptNo}'/>">
		<input type="hidden" name="userId" value="<c:out value='${segInfo.userId}'/>">
		<input type="hidden" id="segFlPath" name="segFlPath" value="<c:out value='${segInfo.segFlPath}'/>">
		<input type="hidden" id="mergeKey" name="mergeKey" value="<c:out value='${segInfo.mergeKey}'/>">
		<input type="hidden" id="mergeCol" name="mergeCol" value="<c:out value='${segInfo.mergeCol}'/>">
		<input type="hidden" name="createTy" value="<c:out value='${segInfo.createTy}'/>">
		<input type="hidden" id="dataInfo" name="dataInfo">
		<input type="hidden" id="headerInfo" name="headerInfo">
		<input type="hidden" name="emsuseYn" value="Y">
		<input type="hidden" name="downType" value="002">
		<fieldset>
			<legend>수신자그룹 선택</legend>
			<h3 class="pop-title">수신자그룹 정보수정(파일등록)</h3>

			<!-- 조건// -->
			<div class="graybox">
				<div class="title-area">
					<h3 class="h3-title">조건</h3>

					<!-- 버튼// -->
					<div class="btn-wrap">
						<span class="required">*필수입력 항목</span>
					</div>
					<!-- //버튼 -->
				</div>
				
				<div class="list-area type-short">
					<ul>
						<li>
							<label class="required">파일등록</label>
							<div class="list-item">
								<input type="text" name="tempFlPath" value="<c:out value='${fn:substring(segInfo.segFlPath,fn:indexOf(segInfo.segFlPath,"-")+1,fn:length(segInfo.segFlPath))}'/>" placeholder="파일 등록" style="width:calc(100% - 128px);" readonly>
								<button type="button" class="btn fullblue" onclick="fn.popupOpen('#popup_file_seg');">등록</button>
								<button type="button" class="btn" onclick="goSegDownload();">다운로드</button>
							</div>
						</li>
						<li>
							<label class="required">구분자</label>
							<div class="list-item">
								<input type="text" name="separatorChar" value="<c:out value='${segInfo.separatorChar}'/>" placeholder="구분자를 입력해주세요." style="width:calc(100% - 52px);">
								<button type="button" class="btn fullblue" onclick="fncSep('<c:out value='${NEO_USER_ID}'/>');">등록</button>
							</div>
						</li>
						<li>
							<label class="required">상태</label>
							<div class="list-item">
								<div class="select">
									<select name="status" title="상태 선택">
										<option value="">선택</option>
										<c:if test="${fn:length(statusList) > 0}">
											<c:forEach items="${statusList}" var="status">
												<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq segInfo.status}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
											</c:forEach>
										</c:if>
									</select>
								</div>
							</div>
						</li>
						<li class="col-full">
							<label class="required">수신자<br>그룹</label>
							<div class="list-item">
								<input type="text" name="segNm" value="<c:out value='${segInfo.segNm}'/>" placeholder="수신자그룹 명칭을 입력해주세요.">
							</div>
						</li>
						<li class="col-full">
							<label>설명</label>
							<div class="list-item">
								<textarea name="segDesc" placeholder="수신자그룹 설명을 입력해주세요."><c:out value="${segInfo.segDesc}"/></textarea>
							</div>
						</li>
						<li class="col-full">
							<label>수신그룹<br> 인원</label>
							<div class="list-item">
								<input type="hidden" id="totCnt" name="totCnt" value="<c:out value='${segInfo.totCnt}'/>"/>
								<p class="inline-txt color-gray line-height40"><c:out value='${segInfo.totCnt}'/>명</p>
							</div>
						</li>
						<li>
							<label>등록자</label>
							<div class="list-item">
								<p class="inline-txt"><c:out value="${segInfo.regNm}"/></p>
							</div>
						</li>
						<li>
							<label>수정자</label>
							<div class="list-item">
								<p class="inline-txt"><c:out value="${segInfo.upNm}"/></p>
							</div>
						</li>
						<li>
							<label>등록일시</label>
							<div class="list-item">
								<fmt:parseDate var="regDt" value="${segInfo.regDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
								<p class="inline-txt"><c:out value="${regDt}"/></p>
							</div>
						</li>
						<li>
							<label>수정일시</label>
							<div class="list-item">
								<fmt:parseDate var="upDt" value="${segInfo.upDt}" pattern="yyyyMMddHHmmss"/>
								<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
								<p class="inline-txt"><c:out value="${upDt}"/></p>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<!-- //조건 -->
			
			<!-- btn-wrap// -->
			<div class="btn-wrap">
				<button type="button" class="btn big fullblue" onclick="goPopSegInfoUpdateFile('<c:out value='${NEO_USER_ID}'/>');">수정</button>
				<button type="button" class="btn big" onclick="goPopSegList();">목록</button>
			</div>
			<!-- //btn-wrap -->
		</fieldset>
	</form>
</div>

