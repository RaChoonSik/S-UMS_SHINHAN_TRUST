<%--
	/**********************************************************
	*	작성자 : 김재환
	*	작성일시 : 2021.12.27
	*	설명 : 좌측메뉴(PUSH)
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<script type="text/javascript">
function runMenu(pMenuId, menuId, menuUrl) {
	/*
	$("#pMenuId").val(pMenuId);
	$("#menuId").val(menuId);
	$("#runMenuForm").attr("target","").attr("action",menuUrl).submit();
	*/
	document.location.href = menuUrl;
}
</script>

<form id="runMenuForm" name="runMenuForm" method="post">
<input type="hidden" id="pMenuId" name="pMenuId"/>
<input type="hidden" id="menuId" name="menuId"/>
</form>

<span class="logo-ums"><a href="/service.ums"><img src="/img//push/logo_ums.png" alt="UMS"></a></span>
<h1>S-PUSH</h1>

<div class="btn-mail">
	<button type="button" class="btn fullorange" onclick="javascript:goNewPush();">신규발송</button>
</div>

<!-- 메뉴// 좌측 ID 별 -->
<nav>
	<ul>
		<c:if test="${fn:length(NEO_MENU_LIST) > 0}">
			<c:forEach items="${NEO_MENU_LIST}" var="lvl1">
				<c:if test="${lvl1.menulvlVal == 1 && lvl1.serviceGb == 40}">
					<li<c:if test="${lvl1.menuId eq NEO_P_MENU_ID}"> class="active"</c:if>>
						<c:set var="pMenuClass" value=""/>
						<c:choose>
							<c:when test="${'M4001000' eq lvl1.menuId}"><c:set var="pMenuClass" value="item01"/></c:when>
							<c:when test="${'M4002000' eq lvl1.menuId}"><c:set var="pMenuClass" value="item02"/></c:when>
							<c:when test="${'M4003000' eq lvl1.menuId}"><c:set var="pMenuClass" value="item03"/></c:when>
							<c:when test="${'M4004000' eq lvl1.menuId}"><c:set var="pMenuClass" value="item04"/></c:when>
						</c:choose>
						<a href="#" class="depth1"><span class="<c:out value='${pMenuClass}'/>"><c:out value='${lvl1.menuNm}'/></span></a>
						<c:set var="hasLvl2" value="${false}"/>
						<c:forEach items="${NEO_MENU_LIST}" var="lvl2Check">
							<c:if test="${lvl1.menuId eq lvl2Check.parentmenuId}"><c:set var="hasLvl2" value="${true}"/></c:if>
						</c:forEach>
						<c:if test="${hasLvl2}">
							<div class="inner-menu">
								<ul>
									<c:forEach items="${NEO_MENU_LIST}" var="lvl2">
										<c:if test="${lvl2.menulvlVal == 2 && lvl1.menuId eq lvl2.parentmenuId}">
											<li<c:if test="${lvl2.menuId eq NEO_MENU_ID}"> class="active"</c:if>><a href="javascript:runMenu('<c:out value='${lvl1.menuId}'/>','<c:out value='${lvl2.menuId}'/>','<c:url value='${lvl2.sourcePath}'/>');"><c:out value='${lvl2.menuNm}'/></a></li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</c:if>
					</li>
				</c:if>
			</c:forEach>
		</c:if>
	</ul>
</nav>
<!-- //메뉴 -->
