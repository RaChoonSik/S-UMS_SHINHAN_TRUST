<%--
	/**********************************************************
	*	작성자 : 김준희
	*	작성일시 : 2021.08.31
	*	설명 : 조직 목록 
	**********************************************************/
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.jsp" %>

<!-- 목록// -->
<div class="graybox">
	<div class="title-area">
		<h3 class="h3-title">목록</h3>
		<span class="total">Total: <em>48</em></span>

		<!-- 버튼// -->
		<div class="btn-wrap">
			<button type="button" class="btn fullred plus">신규등록</button>
			<button type="button" class="btn">삭제</button>
		</div>
		<!-- //버튼 -->
	</div>

	<!-- gridtoggle-area// -->
	<div class="gridtoggle-area">
		<div class="grid">
			<ul class="gridhead">
				<li class="col checkbox">
					<label><input type="checkbox" id="orgAllChk" name="orgAllChk" onclick='selectAll(this)'><span></span></label>
				</li>
				<li class="col departmentcode">부서코드</li>
				<li class="col departmentname">부서명</li>
				<li class="col registrant">등록자</li>
				<li class="col enrolldate">등록일</li>
			</ul>

			<ul class="gridbody">
				<li>
					<ul id="orgList" class="toggle-unfold">
						<!-- D001// -->
						<li class="depth1">
							<ul class="col-box">
								<li>
									<span class="col checkbox">
										<label><input type="checkbox"><span></span></label>
									</span>
									<span class="col departmentcode">
										<button type="button" class="btn-toggle">D001</button>
									</span>
									<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
									<span class="col registrant">SYSTEM</span>
									<span class="col enrolldate">2021.07.26</span>
								</li>
							</ul>

							<!-- 2depth// -->
							<ul class="depth2">
								<!-- D002// -->
								<li>
									<ul class="col-box">
										<li>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">D002</span>
											<span class="col departmentname">
												<a href="javascript:;" class="bold">비서팀</a>
											</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>
								</li>
								<!-- //D002 -->

								<li>
									<ul class="col-box">
										<li>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">D002</span>
											<span class="col departmentname">
												<a href="javascript:;" class="bold">비서팀</a>
											</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>
								</li>
							</ul>
							<!-- //2depth -->
						</li>
						<!-- //D001 -->

						<!-- A001// -->
						<li class="depth1">
							<ul class="col-box">
								<li>
									<span class="col checkbox">
										<label><input type="checkbox"><span></span></label>
									</span>
									<span class="col departmentcode">
										<button type="button" class="btn-toggle">A001</button>
									</span>
									<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
									<span class="col registrant">SYSTEM</span>
									<span class="col enrolldate">2021.07.26</span>
								</li>
							</ul>

							<!-- 2depth// -->
							<ul class="depth2">
								<!-- A002// -->
								<li>
									<ul class="col-box">
										<li>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<button type="button" class="btn-toggle">A002</button>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>

									<!-- 3depth// -->
									<ul class="depth3">
										<li>
											<ul>
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<button type="button" class="btn-toggle">A003</button>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
											<ul class="depth4">
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<span class="last">A004</span>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
										</li>
										<li class="col-box">
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<span class="last">A003</span>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>
									<!-- //3depth -->
								</li>
								<!-- //A002 -->

							</ul>
							<!-- //2depth -->
						</li>
						<!-- //A001 -->
						<!-- A001// -->
						<li class="depth1">
							<ul class="col-box">
								<li>
									<span class="col checkbox">
										<label><input type="checkbox"><span></span></label>
									</span>
									<span class="col departmentcode">
										<button type="button" class="btn-toggle">A001</button>
									</span>
									<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
									<span class="col registrant">SYSTEM</span>
									<span class="col enrolldate">2021.07.26</span>
								</li>
							</ul>

							<!-- 2depth// -->
							<ul class="depth2">
								<!-- A002// -->
								<li>
									<ul class="col-box">
										<li>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<button type="button" class="btn-toggle">A002</button>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>

									<!-- 3depth// -->
									<ul class="depth3">
										<li>
											<ul>
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<button type="button" class="btn-toggle">A003</button>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
											<ul class="depth4">
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<span class="last">A004</span>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
										</li>
										<li class="col-box">
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<span class="last">A003</span>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>
									<!-- //3depth -->
								</li>
								<!-- //A002 -->

							</ul>
							<!-- //2depth -->
						</li>
						<!-- //A001 -->
						<!-- A001// -->
						<li class="depth1">
							<ul class="col-box">
								<li>
									<span class="col checkbox">
										<label><input type="checkbox"><span></span></label>
									</span>
									<span class="col departmentcode">
										<button type="button" class="btn-toggle">A001</button>
									</span>
									<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
									<span class="col registrant">SYSTEM</span>
									<span class="col enrolldate">2021.07.26</span>
								</li>
							</ul>

							<!-- 2depth// -->
							<ul class="depth2">
								<!-- A002// -->
								<li>
									<ul class="col-box">
										<li>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<button type="button" class="btn-toggle">A002</button>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>

									<!-- 3depth// -->
									<ul class="depth3">
										<li>
											<ul>
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<button type="button" class="btn-toggle">A003</button>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
											<ul class="depth4">
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<span class="last">A004</span>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
										</li>
										<li class="col-box">
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<span class="last">A003</span>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>
									<!-- //3depth -->
								</li>
								<!-- //A002 -->

							</ul>
							<!-- //2depth -->
						</li>
						<!-- //A001 -->
						<!-- A001// -->
						<li class="depth1">
							<ul class="col-box">
								<li>
									<span class="col checkbox">
										<label><input type="checkbox"><span></span></label>
									</span>
									<span class="col departmentcode">
										<button type="button" class="btn-toggle">A001</button>
									</span>
									<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
									<span class="col registrant">SYSTEM</span>
									<span class="col enrolldate">2021.07.26</span>
								</li>
							</ul>

							<!-- 2depth// -->
							<ul class="depth2">
								<!-- A002// -->
								<li>
									<ul class="col-box">
										<li>
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<button type="button" class="btn-toggle">A002</button>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>

									<!-- 3depth// -->
									<ul class="depth3">
										<li>
											<ul>
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<button type="button" class="btn-toggle">A003</button>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
											<ul class="depth4">
												<li class="col-box">
													<span class="col checkbox">
														<label><input type="checkbox"><span></span></label>
													</span>
													<span class="col departmentcode">
														<span class="last">A004</span>
													</span>
													<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
													<span class="col registrant">SYSTEM</span>
													<span class="col enrolldate">2021.07.26</span>
												</li>
											</ul>
										</li>
										<li class="col-box">
											<span class="col checkbox">
												<label><input type="checkbox"><span></span></label>
											</span>
											<span class="col departmentcode">
												<span class="last">A003</span>
											</span>
											<span class="col departmentname">
										<a href="javascript:;" class="bold">경영팀</a>
									</span>
											<span class="col registrant">SYSTEM</span>
											<span class="col enrolldate">2021.07.26</span>
										</li>
									</ul>
									<!-- //3depth -->
								</li>
								<!-- //A002 -->

							</ul>
							<!-- //2depth -->
						</li>
						<!-- //A001 -->
					</ul>
				</li>
			</ul>
		</div>
	</div>
	<!-- //gridtoggle-area -->
</div>
<!-- //목록 -->

<%-- <!-- 페이징// -->
<div class="paging">
	${pageUtil.pageHtml}
</div> --%>
<!-- //페이징 -->
