<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.26
	*	설명 : 부서 검색 팝업화면
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_org_search" class="poplayer popsearchgroup">
	<div class="inner" style="max-width:454px;">
		<header>
			<h2>조직도</h2>
		</header>
		<div class="popcont layertoggle">
			<div class="cont">
				<!--그룹목록// -->
				<div class="adminauthority">
					<div class="graybox">
						<div class="content-area">
							<h4>부서목록</h4>
							<input type="hidden" id="selUpOrgCd" name="selUpOrgCd" value="">
							<input type="hidden" id="selUpOrgNm" name="selUpOrgNm" value="">
							<input type="hidden" id="selOrgCd" name="selOrgCd" value="">
							<input type="hidden" id="selOrgNm" name="selOrgNm" value="">
							<ul class="toggle">  
								<li>
									<button type="button" class="btn-toggle" onclick="getOrgList('0','최상위부서');">조직도</button>
								</li> 
							</ul>
							<hr style="border:1px color= silver;" width="100%"></hr>
							<ul class="toggle"> 
								<c:if test="${fn:length(orgList) > 0}">
									<c:forEach items="${orgList}" var="org">
										<li>  
											<button type="button" class="btn-toggle" onclick="getOrgList('<c:out value='${org.orgCd}'/>','<c:out value='${org.orgNm}'/>');"><c:out value="${org.orgNm}"/></button>
											<c:if test="${org.childCnt > 0}">
												<ul class="depth<c:out value='${org.lvlVal+1}'/>" id="<c:out value='${org.orgCd}'/>"></ul>
											</c:if>
										</li>
									</c:forEach>
								</c:if>
							</ul>
						</div>
					</div>
				</div>
				<!-- //그룹목록 --> 
				<!-- 버튼// -->
				<div class="btn-wrap">
					<button type="button" class="btn big fullred" onclick="popOrgSelect();">확인</button>
					<button type="button" class="btn big" onclick="fn.popupClose('#popup_org_search');">취소</button>
				</div>
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_org_search');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background"></span>	
</div>
