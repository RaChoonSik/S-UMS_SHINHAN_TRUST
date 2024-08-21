<%--
	/**********************************************************
	*	작성자 : 김상진
	*	작성일시 : 2021.08.19
	*	설명 : 발송결재라인 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_mail_approval" class="poplayer popsendenroll">
	<input type="hidden" id="curUserId" value='${NEO_USER_ID}'>
	<div class="inner large">
		<header>
			<h2>발송결재라인 등록</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="clear">
					<!-- 조직도// -->
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">조직도</h3>
						</div>
						
						<div class="content-area" style="height:430px;overflow:auto;">
							<h4>그룹목록</h4>
							<ul class="toggle">
								<c:if test="${fn:length(orgList) > 0}">
									<c:forEach items="${orgList}" var="org">
										<li>
											<button type="button" class="btn-toggle" onclick="getOrgUserList('<c:out value='${org.orgCd}'/>','<c:out value='${org.childCnt}'/>');"><c:out value="${org.orgNm}"/></button>
											<c:if test="${org.childCnt > 0}">
												<ul class="depth<c:out value='${org.lvlVal+1}'/>" id="<c:out value='${org.orgCd}'/>"></ul>
											</c:if>
										</li>
									</c:forEach>
								</c:if>
							</ul>
						</div>
					</div>
					<!-- //조직도 -->

					<!-- 사용자// -->
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">사용자</h3>
						</div>
						
						<div class="content-area" style="height:430px;overflow:auto;">
							<div class="item">
								<input type="text" id="searchUserNm" name="searchUserNm" placeholder="사용자명 검색">
								<button type="button" class="btn fullblue" onclick="popSearchUser();">검색</button>
							</div>

							<ul class="user" id="popUserList">
							</ul>
						</div>
					</div>
					<!-- //사용자 -->

					<!-- btn-area// -->
					<div class="btn-area">
						<button type="button" class="btn-add" onclick="popApprovalLineAdd();"><span class="hide">추가</span></button>
						<button type="button" class="btn-remove" onclick="popApprovalLineRemove();"><span class="hide">삭제</span></button>
					</div>
					<!-- //btn-area -->

					<!-- 결재라인 정보// -->
					<div class="graybox fr">
						<div class="title-area">
							<h3 class="h3-title">결재라인 정보</h3>
						</div>
						
						<div class="content-area">
							<h4>요청자</h4>

							<div class="requester">
								<span><c:out value="${NEO_USER_NM}"/></span>
								<span><c:out value="${NEO_ORG_NM}"/></span>
								<span><c:out value="${NEO_JOB_NM}"/></span>
							</div>

							<h4>결재라인
								<button type="button" class="btn-reset" onclick="popApprovalReset();"><span class="hide">초기화</span></button>
							</h4>

							<div class="grid-area" style="height:265px;">
								<table class="grid">
									<caption>그리드 정보</caption>
									<colgroup>
										<col style="width:35%;">
										<col style="width:35%;">
										<col style="width:45%;">
										<col style="width:20%;">
									</colgroup>
									<thead>
										<tr>
											<th scope="col">사용자명</th>
											<th scope="col">조직명</th>
											<th scope="col">직책</th>
										</tr>
									</thead>
									<tbody id="apprUserList" class="sortable">
									<c:if test="${not empty apprLineList}">
										<c:forEach items="${apprLineList}" var="apprLine">
											<tr ondblclick="deleteApprovalLineDblClick(this);">
												<td><input type="hidden" name="apprUserId" value="<c:out value='${apprLine.apprUserId}|${apprLine.orgCd}|${apprLine.positionGb}|${apprLine.jobGb}'/>"><c:out value="${apprLine.apprUserNm}"/></td>
												<td style="display:none;"><input type="hidden" name="apprUserNm" value="<c:out value='${apprLine.apprUserNm} / ${apprLine.orgNm} / ${apprLine.jobNm}'/>"></td>
												<td><c:out value="${apprLine.orgNm}"/></td>
												<td><c:out value="${apprLine.jobNm}"/></td>
											</tr>
										</c:forEach>
									</c:if>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<!-- //결재라인 정보 -->
				</div>

				<!-- btn-wrap// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullblue" onclick="popApprovalLineSave();">설정</button>
					<!-- <button type="button" class="btn big" onclick="popApprovalLineCancel();">취소</button> -->
				</div>
				<!-- //btn-wrap -->

			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_mail_approval');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>
</div>
<input type="hidden" id="hiddenApprLineList" name="hiddenApprLineList" value=""/>
