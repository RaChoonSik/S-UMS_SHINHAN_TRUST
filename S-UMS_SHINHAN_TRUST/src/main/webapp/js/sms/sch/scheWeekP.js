 
$(document).ready(function(){

	/* weeek picker */
	var startDate;
	var endDate;
	
	$('#weekpicker').datepicker({
		showOtherMonths: true,
		selectOtherMonths: true,
		selectWeek:true,
		closeText: '닫기', // 닫기 버튼 텍스트 변경
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더중 월 표시를 위한 부분
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],	//한글 캘린더 중 월 표시를 위한 부분
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],	//한글 캘린더 요일 표시 부분
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],	//한글 요일 표시 부분
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],	// 한글 요일 표시 부분
		showMonthAfterYear: true,	// true : 년 월	false : 월 년 순으로 보여줌
		yearSuffix: '년',
		showButtonPanel: true,	// 오늘로 가는 버튼과 달력 닫기 버튼 보기 옵션

		onSelect: function(dateText, inst){ 
			var vvv  = $(this).val();
			var date = $(this).datepicker('getDate'); 
			startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay());
			endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay() + 6);

			var dateFormat = 'yy.mm.dd'
			startDate = $.datepicker.formatDate( dateFormat, startDate, inst.settings );
			endDate = $.datepicker.formatDate( dateFormat, endDate, inst.settings );		 
			$('#weekInfo').val(startDate + ' - ' + endDate);
			setTimeout("applyWeeklyHighlight()", 100);
			goSearch();
		},
		 
		beforeShow : function() {
			setTimeout("applyWeeklyHighlight()", 100);
		}
	}).datepicker('setDate',new Date());

	//초기값
	$('.ui-datepicker td.ui-datepicker-today a').click();
		
	//달력버튼
	$('.btn-calendar').on('click', function(){
		$('#weekpicker').show().focus().hide(); 
	});
	
	//달력버튼
	$('#weekpicker').on('change', function(){
		var date = $('#weekpicker').datepicker('getDate'); 
		startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay());
		endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay() + 6);

		var dateFormat = 'yy.mm.dd'
		startDate = $.datepicker.formatDate( dateFormat, startDate, inst.settings );
		endDate = $.datepicker.formatDate( dateFormat, endDate, inst.settings );		 
		$('#weekInfo').val(startDate + ' - ' + endDate);
				
		goSearch();
	});	
	
	$('#weekInfo').on('click', function(){
		$('#weekpicker').show().focus().hide(); 
	});	
		
});

//week hover
function applyWeeklyHighlight(){
	$('.ui-datepicker-calendar tr').each(function() {
		if ($(this).parent().get(0).tagName == 'TBODY') {
			$(this).mouseover(function() {
				$(this).find('a').css({
					'background' : '#eee',
				});
				$(this).css('background', '#eee');
			});
			
			$(this).mouseout(function() {
				$(this).css('background', '#ffffff');
				$(this).find('a').css('background', '');
			});
		}
	});
} 

function goPrev()
{
	var curDate = $('#weekpicker').datepicker('getDate');
	curDate.setDate(curDate.getDate() - 7 );
	$('#weekpicker').datepicker('setDate', curDate);
	
	var period = $('#weekInfo').val();
	var strArray = period.split(' - ');	
	var startDate  = strArray[0];
	
	startDate = dateAddDel(startDate, -7, 'd');
	endDate = dateAddDel(startDate, 6, 'd');
	$('#weekInfo').val(startDate + ' - ' + endDate);
	
	goSearch(); 
}

function goNext()
{
	var curDate = $('#weekpicker').datepicker('getDate');
	curDate.setDate(curDate.getDate() + 7 );
	 $('#weekpicker').datepicker('setDate', curDate);
	
	var period = $('#weekInfo').val();
	var strArray = period.split(' - ');	
	var startDate  = strArray[0];
	
	startDate = dateAddDel(startDate, 7, 'd');
	endDate = dateAddDel(startDate, 6, 'd');
	$('#weekInfo').val(startDate + ' - ' + endDate);
	
	goSearch();
}

function goSearch(){
	var period = $('#weekInfo').val(); 
	var startDate = period.substr(0, 10);
	var endDate = period.substr(13, 10);  
	var param = "searchStartDt=" + startDate + "&searchEndDt="  + endDate ; 
	console.log(param);
	$.getJSON("./scheWeekList.json?" + param, function(data) {
		$.each(data.smsScheduleList, function(idx,item){
			var dayId = "day" + item.weekDay ;
			var listId= "dayList" + item.weekDay ;
			var dayTag = document.getElementById(dayId);
			var listTag = document.getElementById(listId);
			var compare = "Y";
			
			if (item.statusCnt <= 1) {
				$(dayTag).empty();
				$(listTag).empty();
				if (item.today == compare){
					$(dayTag).append('<span class="today">Today</span>' + item.displayYmd);
				} else {
					$(dayTag).text(item.displayYmd);
				}
			}
			console.log("구분 " +  item.gubun); 
			if(item.gubun == "004"){
				if (item.status == "000" || item.status == "001") { 
					var scheInfo = '<div class="wait"><span class="time">[알림톡]</span><p><a href="javascript:goKakaoUpdate(\'' + item.msgid + '\',\'' + item.keygen +  '\');">' + item.taskNm + '</a>';
					scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.statusNm + '</em></span></p></div>' ;
					$(scheInfo).appendTo($(listTag));
				} else if (item.status  == "002") {  
					var scheInfo = '<div class="ing"><span class="time">[알림톡]</span><p><a href="javascript:goKakaoUpdate(\'' + item.msgid + '\',\'' + item.keygen +  '\');">' + item.taskNm + '</a>';
					scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.statusNm  + '</em></span></p></div>' ;
					$(scheInfo).appendTo(listTag);
				} else if (item.status  == "003") {  
					var scheInfo = '<div class="end"><span class="time">[알림톡]</span><p><a href="javascript:goKakaoUpdate(\'' + item.msgid + '\',\'' + item.keygen +  '\');">' + item.taskNm + '</a>';
					scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.statusNm  + '</em></span></p></div>' ;
					$(scheInfo).appendTo(listTag);
				}
			} else {
				if (item.status == "000") { 
					var scheInfo = '<div class="wait"><span class="time">[문자]</span><p><a href="javascript:goSmsUpdate(\'' + item.msgid + '\',\'' + item.keygen +  '\');">' + item.taskNm + '</a>';
					scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.statusNm + '</em></span></p></div>' ;
					$(scheInfo).appendTo($(listTag));
				} else if (item.status  == "001") {  
					var scheInfo = '<div class="ing"><span class="time">[문자]</span><p><a href="javascript:goSmsUpdate(\'' + item.msgid + '\',\'' + item.keygen +  '\');">' + item.taskNm + '</a>';
					scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.statusNm  + '</em></span></p></div>' ;
					$(scheInfo).appendTo(listTag);
				} else if (item.status  == "002") {  
					var scheInfo = '<div class="end"><span class="time">[문자]</span><p><a href="javascript:goSmsUpdate(\'' + item.msgid + '\',\'' + item.keygen +  '\');">' + item.taskNm + '</a>';
					scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.statusNm  + '</em></span></p></div>' ;
					$(scheInfo).appendTo(listTag);
				}
			}
		});	
	});
}

function goSmsUpdate(msgid, keygen){
	$("#msgid").val( msgid );
	$("#keygen").val( keygen );

	$.getJSON("./scheduleGrant.json?searchGrantMenuId=" + "M3002001", function(data) {
		if(data.result == "Success") {
			$("#searchForm").attr("target","").attr("action","/sms/cam/smsUpdateP.ums").submit();
		} else if(data.result == "Fail") {
			alert("문자발송 메뉴 접근 권한이 없습니다"); 
		}
	});
}

function goKakaoUpdate(msgid, keygen){	
	$("#msgid").val( msgid );
	$("#keygen").val( keygen );

	$.getJSON("./scheduleGrant.json?searchGrantMenuId=" + "M3002002", function(data) {
		if(data.result == "Success") {
			$("#searchForm").attr("target","").attr("action","/sms/cam/kakaoUpdateP.ums").submit();
		} else if(data.result == "Fail") {
			alert("카카오알림톡발송 메뉴 접근 권한이 없습니다"); 
		}
	});
}

function dateAddDel(sDate, nNum, type) {
	var yy = parseInt(sDate.substr(0, 4), 10);
	var mm = parseInt(sDate.substr(5, 2), 10);
	var dd = parseInt(sDate.substr(8), 10);
	
	if (type == "d") {
		d = new Date(yy, mm - 1, dd + nNum);
	} else if (type == "m") {
		d = new Date(yy, mm - 1, dd + (nNum * 31));
	} else if (type == "y") {
		d = new Date(yy + nNum, mm - 1, dd);
	}
	yy = d.getFullYear();
	mm = d.getMonth() + 1; 
	mm = (mm < 10) ? '0' + mm : mm;
	dd = d.getDate(); 
	dd = (dd < 10) ? '0' + dd : dd; 
	return yy + '.' +  mm  + '.' + dd;
} 

function toDate(str_date){
	var yyyyMM = String(str_date); 
	var sYear = yyyyMM.substring(0,4);
	var sMonth = yyyyMM.substring(5,7);
	var sDate = yyyyMM.substring(8,9); 
	return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
}

function toString(date_str){
	var sYear = date_str.getFullYear();
	var sMonth = date_str.getMonth() + 1;
	var sDay = date.getDate();//d
	month = month >= 10 ? month : '0' + month;	//month 두자리로 저장
	sDay = sDay >= 10 ? sDay : '0' + sDay;			//day 두자리로 저장
	sMonth = sMonth > 9 ? sMonth : "0" + sMonth;
	return  year + '.' + month + '.' + day;		//'-' 추가하여 yyyy-mm-dd 형태 생성 가능
}