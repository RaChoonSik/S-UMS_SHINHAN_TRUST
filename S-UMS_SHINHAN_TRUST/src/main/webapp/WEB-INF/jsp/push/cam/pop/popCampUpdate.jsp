<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.04.05
	*	설명 : PUSH발송 내부 캠페인 수정
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
	<form id="popCampSearchForm" name="popCampSearchForm" method="post">
		<input type="hidden" name="page" value="${searchVO.page}">
		<input type="hidden" name="searchStartDt" value="${searchVO.searchStartDt}">
		<input type="hidden" name="searchEndDt" value="${searchVO.searchEndDt}">
		<input type="hidden" name="searchCampNm" value="${searchVO.searchCampNm}">
	</form>
	<form id="popCampInfoForm" name="popCampInfoForm" method="post">
		<input type="hidden" name="campNo" value="<c:out value='${campInfo.campNo}'/>">
		<input type="hidden" name="deptNo" value="<c:out value='${campInfo.deptNo}'/>">
		<input type="hidden" name="userId" value="<c:out value='${campInfo.userId}'/>">
		<fieldset>
			<legend>캠페인 정보수정</legend>
			<h3 class="pop-title">캠페인 정보수정</h3>

			<!-- 조건// -->
			<div class="graybox">
				<div class="title-area">
					<h3 class="h3-title">조건</h3>
					<span class="required">*필수입력 항목</span>
				</div>
				
				<div class="list-area">
					<ul>
						<li>
							<label class="required">캠페인 목적</label>
							<div class="list-item">
								<div class="select">
									<select name="campTy" title="캠페인목적 선택">
										<option value="">선택</option>
										<c:if test="${fn:length(campTyList) > 0}">
											<c:forEach items="${campTyList}" var="campTy">
												<option value="<c:out value='${campTy.cd}'/>"<c:if test="${campTy.cd eq campInfo.campTy}"> selected</c:if>><c:out value='${campTy.cdNm}'/></option>
											</c:forEach>
										</c:if>
									</select>
								</div>
							</div>
						</li>
						<li>
							<label>EAI 번호</label>
							<div class="list-item">
								<input type="text" name="eaiCampNo" value="<c:out value='${campInfo.eaiCampNo}'/>" maxlength="40" placeholder="EAI 번호를 입력해주세요.">
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
												<option value="<c:out value='${status.cd}'/>"<c:if test="${status.cd eq campInfo.status}"> selected</c:if>><c:out value='${status.cdNm}'/></option>
											</c:forEach>
										</c:if>
									</select>
								</div>
							</div>
						</li>
					</ul>

				</div>
			</div>
			<!-- //조건 -->

			<!-- 캠페인 내용// -->
			<div class="graybox">
				<div class="title-area">
					<h3 class="h3-title">캠페인 내용</h3>
					<span class="required">*필수입력 항목</span>
				</div>
				
				<div class="list-area">
					<ul>
						<li class="col-full">
							<label class="required">캠페인명</label>
							<div class="list-item">
								<input type="text" name="campNm" value="<c:out value='${pushInfo.campNm}'/>" placeholder="캠페인명을 입력해주세요.">
							</div>
						</li>
						<li class="col-full">
							<label>설명</label>
							<div class="list-item">
								<textarea name="campDesc" style="height:200px;" placeholder="캠페인 설명을 입력해주세요."><c:out value='${pushInfo.campDesc}'/></textarea>
							</div>
						</li>
					</ul>

					<ul>
						<li>
							<label>등록자</label>
							<div class="list-item">
								<p class="inline-txt"><c:out value="${pushInfo.regNm}"/></p>
							</div>
						</li>
						<li>
							<label>수정자</label>
							<div class="list-item">
								<p class="inline-txt"><c:out value="${pushInfo.upNm}"/></p>
							</div>
						</li>
						<li>
							<label>등록일시</label>
							<fmt:parseDate var="regDt" value="${pushInfo.regDt}" pattern="yyyyMMddHHmmss"/>
							<fmt:formatDate var="regDt" value="${regDt}" pattern="yyyy.MM.dd HH:mm"/>
							<div class="list-item">
								<p class="inline-txt"><c:out value="${regDt}"/></p>
							</div>
						</li>
						<li>
							<label>수정일시</label>
							<fmt:parseDate var="upDt" value="${pushInfo.upDt}" pattern="yyyyMMddHHmmss"/>
							<fmt:formatDate var="upDt" value="${upDt}" pattern="yyyy.MM.dd HH:mm"/>
							<div class="list-item">
								<p class="inline-txt"><c:out value="${upDt}"/></p>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<!-- //캠페인 내용 -->
			
			<!-- btn-wrap// -->
			<div class="btn-wrap">
				<button type="button" class="btn big fullorange" onclick="goPopCampInfoUpdate();">수정</button>
				<button type="button" class="btn big" onclick="goPopCampList();">목록</button>
			</div>
			<!-- //btn-wrap -->

		</fieldset>
	</form>
</div>
<script type="text/javascript">
/*
$("#popCampInfoForm input[name='eaiCampNo']").on("keyup", function() {
	$(this).val( $(this).val().replace(/[^A-Z0-9]/gi,"") );
	$(this).val( $(this).val().toUpperCase() );
});
*/
</script>
