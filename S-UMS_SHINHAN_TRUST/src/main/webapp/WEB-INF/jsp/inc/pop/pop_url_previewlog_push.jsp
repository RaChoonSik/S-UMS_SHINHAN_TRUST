<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 팝업// -->
<div id="popup_previewlog" class="poplayer poppreviewlog"><!-- id값 수정 가능 -->
	<div class="inner">
		<header>
			<h2>이동경로 미리보기</h2>
		</header>
		<div class="popcont">
			<div class="cont">
				<div class="graybox">
					<div class="title-area">
						<h3>조회</h3>
					</div>
					<div class="table-area">
						<table>
							<caption>이동경로 미리보기</caption>
							<colgroup>
								<col style="width:110px">
								<col style="width:auto">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">경로유형</th>
									<td>weblink</td>
								</tr>
								<tr>
									<th scope="row">URL</th>
									<td>www.test.co.kr</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<!-- 미리보기 영역// -->
				<div class="previewbox">
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
					Lorem ipsum dolor sit, amet consectetur adipisicing elit. Voluptatum, a itaque optio debitis ut recusandae ipsum illum, ullam cumque vero sed nisi ea veritatis? Consequatur officia unde a perferendis impedit.
				</div>
				<!-- //미리보기 영역 -->
			</div>
		</div>
		<button type="button" class="btn_popclose" onclick="fn.popupClose('#popup_previewlog');"><span class="hidden">팝업닫기</span></button>
	</div>
	<span class="poplayer-background" onclick="fn.popupClose('#popup_previewlog');"></span>
</div>
