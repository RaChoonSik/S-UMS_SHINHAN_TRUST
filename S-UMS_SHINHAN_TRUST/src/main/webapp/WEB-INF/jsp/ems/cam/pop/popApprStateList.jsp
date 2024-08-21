<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2022.02.13
	*	설명 : 발송결재라인 정보 출력(팝업내용)
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div class="cont">
	<form>
		<fieldset>
			<legend>결재 상태</legend>
			<!-- 일반 결재// -->
			<h3 class="pop-title">일반 결재</h3>
			<div class="graybox">
				<div class="grid-area">
					<table class="grid">
						<caption>그리드 정보</caption>
						<colgroup>
							<col style="width:50%;">
							<col style="width:auto;">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">
									<c:if test="${0 ne firArrpLine.apprStep}">
										<strong>
											<span><c:out value="${firArrpLine.orgNm}"/></span><br><c:out value="${firArrpLine.apprUserNm}"/>
										</strong>
									</c:if>
								</th>
								<th scope="col">
									<c:if test="${0 ne secArrpLine.apprStep}">
										<strong>
											<span><c:out value="${secArrpLine.orgNm}"/></span><br><c:out value="${secArrpLine.apprUserNm}"/>
										</strong>
									</c:if>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									<c:if test="${0 ne firArrpLine.apprStep}">
										<div class="apprv-wrap">
											<c:choose>
												<c:when test="${'000' eq firArrpLine.rsltCd}">
													<textarea readonly></textarea>
													<c:if test="${not empty firArrpLine.upDt}">
														<fmt:parseDate var="firApprDate" value="${firArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="firApprDt" value="${firApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${firApprDt}"/></span>
													</c:if>
												</c:when>
												<c:when test="${'001' eq firArrpLine.rsltCd}">
													<strong>
														<c:out value="${firArrpLine.rsltNm}"/>
													</strong>
													<textarea readonly></textarea>
													<c:if test="${not empty firArrpLine.upDt}">
														<fmt:parseDate var="firApprDate" value="${firArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="firApprDt" value="${firApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${firApprDt}"/></span>
													</c:if>
												</c:when>
												<c:when test="${'002' eq firArrpLine.rsltCd}">
													<strong class="color-blue">
														<c:out value="${firArrpLine.rsltNm}"/>
														<!-- <span><c:if test="${'003' ne firArrpLine.rsltCd}">의견</c:if></span> --><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${firArrpLine.rejectDesc}"/>
													</textarea> --%>
													<c:if test="${not empty firArrpLine.upDt}">
														<fmt:parseDate var="firApprDate" value="${firArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="firApprDt" value="${firApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${firApprDt}"/></span>
													</c:if>
												</c:when>
												<c:when test="${'003' eq firArrpLine.rsltCd}">
													<strong class="color-red" ><!-- 반려의 경우에만 color-red 클래스를 추가합니다 -->
														<c:out value="${firArrpLine.rsltNm}"/>
														<span>반려사유:<c:out value="${firArrpLine.rejectNm}"/></span><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${firArrpLine.rejectDesc}"/>
													</textarea> --%>
													<c:if test="${not empty firArrpLine.upDt}">
														<fmt:parseDate var="firApprDate" value="${firArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="firApprDt" value="${firApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${firApprDt}"/></span>
													</c:if>
												</c:when>
											</c:choose>
										</div>
									</c:if>
									<c:if test="${0 eq firArrpLine.apprStep}">
										<div class="apprv-wrap">
											<textarea readonly></textarea>
										</div>
										
									</c:if>
								</td>
								<td>
									<c:if test="${0 ne secArrpLine.apprStep}">
										<div class="apprv-wrap">
											<c:choose>
												<c:when test="${'000' eq secArrpLine.rsltCd}">
													<textarea readonly></textarea>
													<c:if test="${not empty secArrpLine.upDt}">
														<fmt:parseDate var="secApprDate" value="${secArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="secApprDt" value="${secApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${secApprDt}"/></span>
													</c:if>
												</c:when>
												<c:when test="${'001' eq secArrpLine.rsltCd}">
													<strong>
														<c:out value="${secArrpLine.rsltNm}"/>
													</strong>
													<textarea readonly></textarea>
													<c:if test="${not empty secArrpLine.upDt}">
														<fmt:parseDate var="secApprDate" value="${secArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="secApprDt" value="${secApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${secApprDt}"/></span>
													</c:if>
												</c:when>
												<c:when test="${'002' eq secArrpLine.rsltCd}">
													<strong class="color-blue">
														<c:out value="${secArrpLine.rsltNm}"/>
														<!--<span><c:if test="${'003' ne forArrpLine.rsltCd}">의견</c:if></span>--><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${secArrpLine.rejectDesc}"/>
													</textarea> --%>
													<c:if test="${not empty secArrpLine.upDt}">
														<fmt:parseDate var="secApprDate" value="${secArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="secApprDt" value="${secApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${secApprDt}"/></span>
													</c:if>
												</c:when>
												<c:when test="${'003' eq secArrpLine.rsltCd}">
													<strong class="color-red" ><!-- 반려의 경우에만 color-red 클래스를 추가합니다 -->
														<c:out value="${secArrpLine.rsltNm}"/>
														<span>반려사유:<c:out value="${secArrpLine.rejectNm}"/></span><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${secArrpLine.rejectDesc}"/>
													</textarea> --%>
													<c:if test="${not empty secArrpLine.upDt}">
														<fmt:parseDate var="secApprDate" value="${secArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="secApprDt" value="${secApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${secApprDt}"/></span>
													</c:if>
												</c:when>
											</c:choose>
										</div>
									</c:if>
									<c:if test="${0 eq secArrpLine.apprStep}">
										<div class="apprv-wrap">
											<textarea readonly></textarea>
										</div>
									</c:if>
								</td>
							</tr>
						</tbody> 
					</table>
					<table class="grid">
						<caption>그리드 정보</caption>
						<colgroup>
							<col style="width:50%;">
							<col style="width:auto;">
						</colgroup> 
						<thead>
							<tr>
								<th scope="col">
									<c:if test="${0 ne thrArrpLine.apprStep}">
										<strong>
											<span><c:out value="${thrArrpLine.orgNm}"/></span><br><c:out value="${thrArrpLine.apprUserNm}"/>
										</strong>
									</c:if>
								</th>
								<th scope="col">
									<c:if test="${0 ne forArrpLine.apprStep}">
										<strong>
											<span><c:out value="${forArrpLine.orgNm}"/></span><br><c:out value="${forArrpLine.apprUserNm}"/>
										</strong>
									</c:if>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
 									<c:if test="${0 ne thrArrpLine.apprStep}">
										<c:choose>
											<c:when test="${'000' eq thrArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<textarea readonly></textarea>
													<c:if test="${not empty thrArrpLine.upDt}">
														<fmt:parseDate var="thrApprDate" value="${thrArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="thrApprDt" value="${thrApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${thrApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'001' eq thrArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<strong>
														<c:out value="${thrArrpLine.rsltNm}"/>
													</strong>
													<textarea readonly></textarea>
													<c:if test="${not empty thrArrpLine.upDt}">
														<fmt:parseDate var="thrApprDate" value="${thrArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="thrApprDt" value="${thrApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${thrApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'002' eq thrArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<strong class="color-blue">
														<c:out value="${thrArrpLine.rsltNm}"/>
														<!--<span><c:if test="${'003' ne forArrpLine.rsltCd}">의견</c:if></span>--><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${thrArrpLine.rejectDesc}"/>
													</textarea> --%>
													<c:if test="${not empty thrArrpLine.upDt}">
														<fmt:parseDate var="thrApprDate" value="${thrArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="thrApprDt" value="${thrApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${thrApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'003' eq thrArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<strong class="color-red" ><!-- 반려의 경우에만 color-red 클래스를 추가합니다 -->
														<c:out value="${thrArrpLine.rsltNm}"/>
														<span>반려사유:<c:out value="${thrArrpLine.rejectNm}"/></span><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${thrArrpLine.rejectDesc}"/>
													</textarea> --%>
													<c:if test="${not empty thrArrpLine.upDt}">
														<fmt:parseDate var="thrApprDate" value="${thrArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="thrApprDt" value="${thrApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${thrApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'009' eq forArrpLine.rsltCd}">
												<div class="apprv-wrap type-single">
													<strong class="color-blue"><c:out value="${thrArrpLine.rsltNm}"/></strong>
												</div>
											</c:when>
										</c:choose>
									</c:if>
									<c:if test="${0 eq thrArrpLine.apprStep}">
										<div class="apprv-wrap">
											<textarea readonly></textarea>
										</div>
									</c:if>
								</td>
								<td>
	 								<c:if test="${0 ne forArrpLine.apprStep}">
										<c:choose>
											<c:when test="${'000' eq forArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<textarea readonly></textarea>
													<c:if test="${not empty forArrpLine.upDt}">
														<fmt:parseDate var="forApprDate" value="${forArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="forApprDt" value="${forApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${forApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'001' eq forArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<strong>
														<c:out value="${forArrpLine.rsltNm}"/>
													</strong>
													<textarea readonly></textarea>
													<c:if test="${not empty forArrpLine.upDt}">
														<fmt:parseDate var="forApprDate" value="${forArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="forApprDt" value="${forApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${forApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'002' eq forArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<strong class="color-blue">
														<c:out value="${forArrpLine.rsltNm}"/>
														<!--<span><c:if test="${'003' ne forArrpLine.rsltCd}">의견</c:if></span>--><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${forArrpLine.rejectDesc}"/>
													</textarea> --%>
													
													<c:if test="${not empty forArrpLine.upDt}">
														<fmt:parseDate var="forApprDate" value="${forArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="forApprDt" value="${forApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${forApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'003' eq forArrpLine.rsltCd}">
												<div class="apprv-wrap">
													<strong class="color-red" ><!-- 반려의 경우에만 color-red 클래스를 추가합니다 -->
														<c:out value="${forArrpLine.rsltNm}"/>
														<span>반려사유:<c:out value="${forArrpLine.rejectNm}"/></span><!-- 사유가 있을 때 span태그로 지정합니다 -->
													</strong>
<%-- 													<textarea readonly><c:out value="${forArrpLine.rejectDesc}"/>
													</textarea> --%>
													<c:if test="${not empty forArrpLine.upDt}">
														<fmt:parseDate var="forApprDate" value="${forArrpLine.upDt}" pattern="yyyyMMddHHmmss"/>
														<fmt:formatDate var="forApprDt" value="${forApprDate}" pattern="yyyy.MM.dd"/>
														<span class="date"><c:out value="${forApprDt}"/></span>
													</c:if>
												</div>
											</c:when>
											<c:when test="${'009' eq forArrpLine.rsltCd}">
												<div class="apprv-wrap type-single">
													<strong class="color-blue"><c:out value="${forArrpLine.rsltNm}"/></strong>
												</div>
											</c:when>
										</c:choose>
									</c:if>
									<c:if test="${0 eq forArrpLine.apprStep}">
										<div class="apprv-wrap">
											<textarea readonly></textarea>
										</div>
									</c:if>
								</td>
							</tr>
						</tbody> 
					</table> 
				</div>
			</div>
			<!-- //일반 결재 -->
			<!-- btn-wrap// -->
			<div class="btn-wrap">
				<button type="button" class="btn big" onclick="fn.popupClose('#popup_mail_approval_state');">닫기</button>
			</div>
			<!-- //btn-wrap -->
		</fieldset>
	</form>
</div>
