/**********************************************************
*    작성자 : 이혜민
*    작성일시 : 2022.03.23
*    설명 : 통계분석 카카오통계분석 분석 목록 화면 JavaScript
**********************************************************/

$(document).ready(function() {
    goSearch("1");
});

// 검색 버튼 클릭시
function goSearch(pageNum) {
    if( $("#searchStartDt").val() > $("#searchEndDt").val() ) {
        alert("검색 시 시작일은 종료일보다 클 수 없습니다.");        // COMJSALT017
        return;
    }
    
    var endDt = $("#searchEndDt").val().split('.');
    var startDt = $("#searchStartDt").val().split('.');
    var endDate = new Date(endDt[0], endDt[1], endDt[2]);
    var startDate = new Date(startDt[0], startDt[1], startDt[2]);
    
    var diff = endDate - startDate;
    var currDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
    if ( parseInt(diff/currDay) > 365 ){
        alert("검색 기간은 1년을 넘길수 없습니다");
        return;
    }
    
    $("#searchForm input[name='page']").val(pageNum);
    var param = $("#searchForm").serialize();
    $.ajax({
        type : "GET",
        url : "./kakaoCampList.ums?" + param,
        dataType : "html",
        success : function(pageHtml){
            $("#divKakaoList").html(pageHtml);
        },
        error : function(){
            alert("List Data Error!!");
        }
    });
}

// 초기화 버튼 클릭시
function goInit() {
    $("#searchForm")[0].reset();
}

// 목록에서 캠페인명 클릭시
function goKakaoList(pCampNo) {
    $('#campNo').val(pCampNo);
    $("#searchForm").attr("target","").attr("action","./kakaoCampSendListP.ums").submit();
}

// 페이징
function goPageNum(pageNum) {
    goSearch(pageNum);
}
