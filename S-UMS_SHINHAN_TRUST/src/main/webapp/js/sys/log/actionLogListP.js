$(document).ready(function() {	
	goSearch("1");
}); 

// 검색 버튼 클릭시
function goSearch(pageNo) {
  	
	$("#searchOrgCd").val($("#orgCd").val());
	$("#searchForm input[name='page']").val(pageNo);
	var param = $("#searchForm").serialize();	
	console.log(param);
	$.ajax({
		type : "GET",
		url : "./actionLogList.ums?" + param,
		dataType : "html",
		success : function(pageHtml){
			$("#divActionLogList").html(pageHtml);
		},
		error : function(){
			alert("List Data Error!!");
		}
	});
}  

function goExcel() {
	$("#searchForm").attr("target","iFrmExcel").attr("action","./actionLogExcelList.ums").submit();
}
// 검색 코드 분류 변경시*/ 
function change(){
	goSearch("1");
}

// 초기화 버튼 클릭시
function goReset() {
	$("#searchForm")[0].reset();
} 

function goPageNum(page) {
	goSearch(page);
}