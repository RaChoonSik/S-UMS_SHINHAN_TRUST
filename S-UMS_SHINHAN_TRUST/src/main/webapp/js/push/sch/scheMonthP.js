$(document).ready(function(){

	/* month picker */
	var today = getTodayType();
	var options = {
		pattern: 'yyyy.mm',
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	};
	$("#monthPicker").monthpicker(options);
	
	//초기값
	function getTodayType(){
		var date = new Date();
		return date.getFullYear() + "." + ("0"+(date.getMonth()+1)).slice(-2);
	}
	$('#monthPicker').val(today);
	
	//달력버튼
	$('.btn-calendar').on('click', function(){
		$('#monthPicker').focus();
	});
 
	//달력버튼
	$('#monthPicker').on('change', function(){
		goSearch();
	});
 
  
	goSearch();
}); 

function goNext(){
	var curMonth = $('#monthPicker').val();
	var curDate = toDate(curMonth);	 
	curDate.setMonth(curDate.getMonth() + 1); 
	$('#monthPicker').val(curDate.getFullYear() + "." + ("0"+(curDate.getMonth()+1)).slice(-2));
	goSearch();
}

function goPrev()
{
	var curMonth = $('#monthPicker').val();
	var curDate = toDate(curMonth);	 
	curDate.setMonth(curDate.getMonth() - 1); 
	$('#monthPicker').val(curDate.getFullYear() + "." + ("0"+(curDate.getMonth()+1)).slice(-2));
	goSearch();
}
  
function goSearch(){ 
 	var param = $('#monthPicker').val(); 
	
	setDisplayWeekRow(param);
	$.getJSON("./scheMonthList.json?searchStartDt=" + param, function(data) { 
		const td_list = document.querySelectorAll('#month td');
		const td_list_length = td_list.length;
		for(var i = 0; i < td_list_length; i++)  {
    		 $(td_list[i]).empty(); 
  		}
		$.each(data.pushScheduleList, function(idx,item){ 			
			var listId= "day" + item.dayIndex ;
			var listTag = document.getElementById(listId);
			var compare = "Y"; //오늘여부 비교 
 			var tdHtml = "" ;
		
			tdHtml = '<span>' + item.displayYmd +'</span>';
			
			if ( item.totCnt > 0  ) {
				tdHtml +='<a href="javascript:goView(\'' + item.sendDt + '\');">'
				if (item.today == compare){
					tdHtml += '<div class="mark"><span class="today">Today</span></div>';
				}
				tdHtml += '<ul class="calendar-state">';
				if (item.schCnt > 0 ) {
					tdHtml +='<li class="wait">' + item.schDesc + '</li>';
				} 
				if (item.ingCnt > 0 ) {
					tdHtml +='<li class="ing">' + item.ingDesc + '</li>';
				} 
				if (item.sucCnt > 0  ||  item.failCnt > 0 ) {
					tdHtml +='<li class="end">'  + item.endDesc + '(' + item.sucDesc + '/' + item.failDesc + ')</li>';
				}
				tdHtml +='</ul></a>';
			}
			tdHtml += '</td>' 			
			$(tdHtml).appendTo($(listTag));
		});	
	}); 
}

function setDisplayWeekRow(dateStr) {
	var year  = Number(dateStr.substring(0, 4));
	var month = Number(dateStr.substring(5, 7));

	var nowDate =new Date(year, month-1, 1);
 
	var lastDate =new Date(year, month, 0).getDate();
	var monthSWeek = nowDate.getDay();
 
	var weekSeq = parseInt((parseInt(lastDate) + monthSWeek - 1)/7) + 1;
 
	if (weekSeq <= 4 )
	{	
		$('#week5').attr('style', "display:none;");
		$('#week6').attr('style', "display:none;");  
	} else if (weekSeq == 5 ) {
		$('#week5').attr('style', "display:'';"); 
		$('#week6').attr('style', "display:none;");
	} else{
		$('#week6').attr('style', "display:'';"); 
		$('#week6').attr('style', "display:'';"); 
	}
	
	return weekSeq;
}
 
// 일별 조회 팝업 
function goView(sendDt) {	
	$("#sendDt").val(sendDt);
	var startDate = sendDt;
	var endDate = sendDt;  
	var param = "searchStartDt=" + startDate + "&searchEndDt="  + endDate ;
	
	$.getJSON("./scheMonthList.json?" + param, function(data) {
		$.each(data.pushScheduleList, function(idx,item){ 
			$("#displayYmdFull").text(item.displayYmdFull);
			$("#schDesc").text(item.schDesc);
			$("#ingDesc").text(item.ingDesc);
			$("#endDesc").text(item.endDesc + '(' + item.sucDesc + '/' + item.failDesc + ')');
		});
	});
	
	$.getJSON("./scheDayList.json?" + param, function(dayData) {
		var scheInfo = ""; 
		$.each(dayData.pushScheduleList, function(idx,item){
			  
			if (item.workStatus  == "000" || item.workStatus  == "001") { 
				scheInfo += '<div class="wait"><span class="time"></span><p><a href="javascript:goUpdate(\'' + item.pushmessageId + '\');">' + item.pushName + '</a>';
				scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.workStatusNm + '</em></span></p></div>' ;
			} else if (item.workStatus  == "002") {
				scheInfo += '<div class="ing"><span class="time"></span><p><a href="javascript:goUpdate(\'' + item.pushmessageId + '\');">' + item.pushName + '</a>';
				scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.workStatusNm  + '</em></span></p></div>' ;
			} else if (item.workStatus  == "003") {
				scheInfo += '<div class="end"><span class="time"></span><p><a href="javascript:goUpdate(\'' + item.pushmessageId + '\');">' + item.pushName + '</a>';
				scheInfo += '<span>' + item.scheduleDesc + '<em>' + item.workStatusNm  + '</em></span></p></div>' ;
			} else {
				scheInfo += '<div class="end"><span class="time"></span><p><a href="javascript:goUpdate(\'' + item.pushmessageId + '\');">' + item.pushName + '</a>';
				scheInfo += '<span>' + item.scheduleDesc + '<em class="color-red">' + item.workStatusNm  + '</em></span></p></div>' ;
			}
		});	 
		$("#popPushDayList").html(scheInfo );
	});
	 
	fn.popupOpen("#popup_push_day_calendar");
}
   
function goDay(gubun)
{	
	var sendDt =$("#sendDt").val();
	if (sendDt.length < 8) {
		alert("기준할 일자가 없습니다 확인해주세요!")
		return;
	}
	var yyyyMMdd = String(sendDt);
	var sYear = yyyyMMdd.substring(0,4);
	var sMonth = yyyyMMdd.substring(4,6);
	var sDate = yyyyMMdd.substring(6,8);

	var date = new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
	console.log("goday 1  :" + date )
	if (gubun == "M") {
		date.setDate(date.getDate() - 1 );	
	} else {
		date.setDate(date.getDate() + 1);
	}
	
	var sYear = String(date.getFullYear());
	var sMonth = String(date.getMonth() + 1);
	var sDate = String(date.getDate());
	console.log("sYear  :" + sYear + "/ sMonth  :" + sMonth  + " / sDate  :" + sDate );
	sMonth = sMonth > 9 ? sMonth : "0" + sMonth;
	sDate  = sDate > 9 ? sDate : "0" + sDate;
	var targetDt =  sYear + sMonth + sDate;	

	goView(targetDt);
}
// PUSH 정보 수정 화면 이동
function goUpdate(pushmessageId){	
 	$("#pushmessageId").val(pushmessageId);

	$.getJSON("./scheduleGrant.json?searchGrantMenuId=" + "M4001000", function(data) {
		if(data.result == "Success") {
			$("#searchForm").attr("target","").attr("action","/push/cam/pushUpdateP.ums").submit();
		} else if(data.result == "Fail") {
			alert("PUSH발송 메뉴 접근 권한이 없습니다"); 
		}
	});
}

function toDate(str_date){
	var yyyyMM = String(str_date); 
	var sYear = yyyyMM.substring(0,4);
	var sMonth = yyyyMM.substring(5,7);
	var sDate ='01'; 
	return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
}

function toString(date_str){
	var sYear = date_str.getFullYear();
	var sMonth = date_str.getMonth() + 1;
	sMonth = sMonth > 9 ? sMonth : "0" + sMonth;
	return sYear + '.' +  sMonth;
}