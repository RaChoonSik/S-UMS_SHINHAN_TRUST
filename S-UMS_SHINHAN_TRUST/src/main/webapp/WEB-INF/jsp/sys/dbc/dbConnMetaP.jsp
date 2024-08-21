<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.06
	*	설명 : 데이터베이스 메타 정보 관리
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/header_sys.jsp" %>

<script type="text/javascript" src="<c:url value='/js/sys/dbc/dbConnMeta.js'/>"></script>

<body>
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
					<h2>데이터베이스 메타정보관리</h2>
				</div>
				
				<!-- 공통 표시부// -->
				<%@ include file="/WEB-INF/jsp/inc/top.jsp" %>
				<!-- //공통 표시부 -->
				
			</section>
			<!-- //cont-head -->


			<!-- cont-body// -->
			<section class="cont-body">

				<div class="connectdb-wrap">
					<form id="dbConnInfoForm" name="dbConnInfoForm" method="post">
						<input type="hidden" id="dbConnNo" name="dbConnNo" value="<c:out value='${dbConnInfo.dbConnNo}'/>">
						<input type="hidden" id="tblNo" name="tblNo" value="0">
						<input type="hidden" id="tblNm" name="tblNm" value="">
						<input type="hidden" id="colNo" name="colNo" value="0">
						<input type="hidden" id="colNm" name="colNm" value="">
						<input type="hidden" id="colAlias" name="colAlias" value="">
						<input type="hidden" id="colDataTy" name="colDataTy" value="">
						<input type="hidden" id="colDesc" name="colDesc" value="">
						<input type="hidden" id="colHiddenYn" name="colHiddenYn" value="">
						<input type="hidden" id="colEncrDecrYn" name="colEncrDecrYn" value=""> 
						
						<fieldset>
							<legend>데이터베이스 정보, 메타정보관리 및 내용</legend> 
							<!-- 데이터베이스 정보// -->
							<div class="graybox">
								<div class="title-area">
									<h3 class="h3-title">데이터베이스 정보</h3> 
								</div>
								
								<div class="list-area">
									<ul>
										<li>
											<label>Connection</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${dbConnInfo.dbConnNm}'/></p>
											</div>
										</li>
										<li>
											<label>DB종류</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${dbConnInfo.statusNm}'/></p>
											</div>
										</li>
										<li class="col-full">
											<label>설명</label>
											<div class="list-item">
												<p class="inline-txt"><c:out value='${dbConnInfo.dbConnDesc}'/></p>
											</div>
										</li>
									</ul>
								</div>
							</div>
							<!-- //데이터베이스 정보 -->
							
								<!-- 메타정보관리// -->
							<div class="graybox editpw active">

								<div class="title-area">
									<h3 class="h3-title">메타정보관리</h3>
									<button type="button"><span class="hidden">여닫기버튼</span></button>
								</div>
								<!-- 메타테이블 및 메타정보관리// -->
								<div class="table-wrap clear">
									<!-- 메타테이블// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">메타테이블</h3>
										</div>
			
										<div class="grid-area grid-btn">
											<table class="grid">
												<caption>그리드 정보</caption>
												<tbody>
													<c:if test="${fn:length(metaTableList) > 0}">
														<c:forEach items="${metaTableList}" var="meta" varStatus="metaStatus">
															<tr> 
																<td onclick="getMetaTableInfo('<c:out value='${meta.tblNm}'/>');">
																	<c:out value='${meta.tblNm}'/>
																	<c:if test="${fn:length(meta.tblAlias) > 0 }"> [<c:out value='${meta.tblAlias}' />]</c:if>
																</td>
																<!-- <td style="visibility:hidden"></td> -->
															</tr>
														</c:forEach>
													</c:if> 
												</tbody>
											</table>
										</div>
									</div>
									<!-- //메타테이블 -->
	
									<!-- 메타정보관리// -->
									<div class="graybox">
										<div class="title-area">
											<h3 class="h3-title">메타정보관리</h3>
	
											<!-- 버튼// -->
											<div class="btn-wrap" style="margin-top:0;">
											
												<!-- 등록된 별칭값이 없을 경우 미노출// -->
												<button type="button" class="btn" id="metaInfoDel" onclick="goDelete()" >삭제</button>
												<!-- 등록된 별칭값이 없을 경우 미노출// -->
	
												<button type="button" class="btn" id="metaInfoReWrite" onclick="goReset()" >재입력</button>
											</div>
											<!-- //버튼 -->
										</div>
										
										<!-- 메타테이블 목록(tr) 클릭시 값 노출// -->
										<div class="list-area">
											<ul>
												<li class="col-full">
													<label>테이블명</label>
													<div class="list-item">
														<p class="inline-txt" id="viewTblNm"></p>
													</div>
												</li>
												<li class="col-full">
													<label>별칭</label>
													<div class="list-item">
														<input type="text" placeholder="등록data" id="tblAlias" name="tblAlias">
													</div>
												</li>
												<li class="col-full">
													<label>설명</label>
													<div class="list-item">
														<input type="text" placeholder="등록data" id="tblDesc" name="tblDesc">
													</div>
												</li>
											</ul>
										</div>
										<!-- //메타테이블 목록(tr) 클릭시 값 노출 -->
	
										<!-- 버튼// -->
										<div class="btn-wrap" style="margin-top:0;">
	
											<button type="button" class="btn big fullred" id="metaInfoUpd" onclick="goUpdate()" value="">수정</button>
	
											<!-- 등록된 별칭값이 없을 경우 노출// -->
											<!-- <button type="button" class="btn big fullred">등록</button> -->
											<!-- 등록된 별칭값이 없을 경우 노출// -->
	
											<button type="button" class="btn big" onclick="goCancel()">목록</button>
										</div>
										<!-- //버튼 -->
									</div>
									<!-- //메타정보관리 -->
									
								</div>
								<!-- //메타테이블 및 메타정보관리 -->
							</div>
							<!-- //메타정보관리 -->
							
							<!-- 내용// -->
							<div class="graybox" style="visibility:hidden" id="divMetaColumnist">
							 
							</div>
							<!-- //내용 -->
								
						</fieldset>
					</form>
				</div> 
				
			</section>
			<!-- //cont-body -->
			
		</div>
		<!-- // content -->
	</div>
	<%@ include file="/WEB-INF/jsp/inc/pop/pop_meta_extract.jsp" %>	
</body>
</html>
