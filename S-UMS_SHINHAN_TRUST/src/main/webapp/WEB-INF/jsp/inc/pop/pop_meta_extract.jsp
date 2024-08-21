<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.09.07
	*	설명 : 메타테이블 추출 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<div id="popup_meta_extract" class="poplayer popextractcondition">
	<form id="dbConnColValueInfoForm" name="dbConnColValueInfoForm" method="post">
	<div class="inner">
		<header>
			<h2>추출조건</h2>
		</header>
		<div class="popcont">
			<div class="cont">
			<!-- 등록 메타테이블명 data [등록 컬럼명 data] 추출조건// -->
				<div class="graybox">
					<div class="title-area">
						<h3 class="h3-title" id="metaTableTitle">등록 메타테이블명 data [등록 컬럼명 data] 추출조건</h3>
					</div>
					<div class="list-area"> 
							<input type="hidden" id="popTblNo" name="tblNo" value="0"> 
							<input type="hidden" id="popColNo" name="colNo" value="0"> 
							<input type="hidden" id="popColNos" name="colNos" value="0">
							<input type="hidden" id="valueNo" name="valueNo" value="0">
							<input type="hidden" id="valueNm" name="valueNm" value="">
							<input type="hidden" id="valueAlias" name="valueAlias" value="">
							<ul id="extractOperList"> 
							</ul>
					</div>
				</div> 
				<!-- //등록 메타테이블명 data [등록 컬럼명 data] 추출조건 -->

				<!-- 버튼// -->
				<div class="btn-wrap tar" style="margin-top:10px;">
					<button type="button" class="btn fullred" onclick="goUpdateOper()">수정</button>
					<button type="button" class="btn" onclick="goResetOper()">재입력</button>
				</div>
				<!-- //버튼 -->

				<!-- [등록 컬럼명 data] VALUE// -->
				<div class="graybox" id="divValueInfo">
					<div class="title-area">
						<h3 class="h3-title" id="metaColumnTitle">[등록 컬럼명 data] VALUE</h3>

						<!-- 버튼// -->
						<div class="btn-wrap">
							<button type="button" class="btn fullred plus" onclike="goValueReset();">신규등록</button>
						</div>
						<!-- //버튼 -->
					</div>
					
					<div class="grid-area" id="divExtractValueInfo"></div> 
				</div>
				<!-- //[등록 컬럼명 data] VALUE -->

				<!-- VALUE신규등록영역 :: 신규등록 버튼 클릭시 노출// -->
				<div class="valueenroll" style="margin-top:30px; display:none">

					<!-- VALUE신규등록// -->
					<div class="graybox">
						<div class="title-area">
							<h3 class="h3-title">VALUE 신규등록</h3>
						</div>
						
						<div class="list-area">
							<ul>
								<li>
									<label>VALUE</label>
									<div class="list-item">
										<input type="text" id="addValueNm" placeholder="value값을 입력하세요">
									</div>
								</li>
								<li>
									<label>별칭</label>
									<div class="list-item">
										<input type="text" id="addValueAlias" placeholder="별칭값을 입력하세요">
									</div>
								</li>
							</ul>
						</div>
					</div>
					<!-- //VALUE신규등록 -->

					<!-- 버튼// -->
					<div class="btn-wrap">
						<button type="button" class="btn big fullred" onclick="goAddValue()">등록</button>
						<button type="button" class="btn big"  onclick="goCancelValue()">취소</button>
					</div>
					<!-- //버튼 -->
				</div>
				<!-- //VALUE신규등록영역 :: 신규등록 버튼 클릭시 노출 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_meta_extract');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_meta_extract');"></span>
	</form>
</div> 