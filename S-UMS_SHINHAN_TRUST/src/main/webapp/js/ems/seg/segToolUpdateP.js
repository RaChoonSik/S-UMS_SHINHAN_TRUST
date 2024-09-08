/**********************************************************
*	작성자 : 김준희
*	작성일시 : 2021.08.18
*	설명 : 수신자그룹 수정(추출도구) JavaScript
**********************************************************/

//*************************************************************
// isLine에 들어가는 순서
// tblInfo   = 0. tblNo|1. tblNm|2. tblAlias
// colInfo   = 3. colNo|4. colNm|5. colDataTy|6. colAlias
// valueInfo = 7. valueNo|8. valueNm|9. valueAlias
// operInfo  = 10. operNo|11. operNm|12. operAlias
// sort      = 13. sortNm|14. sortAlias(NULL일 경우 none|none가 들어감)
// whereRel  = 15. 관계식(AND 또는 OR)
//*************************************************************

$(document).ready(function() {
	getMetaFrameContent();
});

// 조건부분 초기화
var condRowId = 1;
function goLoad(dbSrcWhere) {
	var srcWhere = dbSrcWhere;

	var isLine = new Array(16);
	var allLine = new Array();     // isLine에 표현될 값
	var lastIsLine = "";                // 콤보박스에 표현될 값

	var pos = -1;
	var tmp = "";
	var i_ps = 0;

	pos = srcWhere.indexOf("##");

	while(pos != -1) {
		tmp = srcWhere.substring(0, pos);
		srcWhere = srcWhere.substring(pos+2);

		allLine[i_ps++] = tmp;
		pos = srcWhere.indexOf("##");
	}
	lastIsLine = srcWhere;  // 콤보박스에 표현될 값

	var condHTML = "";

	for(i =0; i < allLine.length; i++) {
		temp = allLine[i];

		ii = temp.indexOf("|");
		isLine[0] = temp.substring(0, ii);

		for(kk = 1; kk <= 14; kk++) {
			temp = temp.substring(ii+1);
			ii = temp.indexOf("|");
			isLine[kk] = temp.substring(0, ii);
		}

		isLine[15] = temp.substring(ii+1);

		// tblInfo = 0. p_tbl_no|1. p_tbl_nm|2. p_tbl_alias
		// colInfo = 3. p_col_no|4. p_col_nm|5. p_col_data_ty|6. p_col_alias
		// valueInfo = 7. p_value_no|8. p_value_nm|9. p_value_alias
		// operInfo = 10. p_oper_no|11. p_oper_nm|12. p_oper_alias
		// sort = 13. sort_nm|14. sort_alias(NULL일 경우 none|noen가 들어감)
		// whereRel = 15. 관계식(AND 또는 OR)

		if(isLine[14] == 'none') temp_sort = "&nbsp;";
		else temp_sort = isLine[14];

		condHTML += "<tr> ";
		condHTML += "  <td><label for='condRowId_" + condRowId + "'><input type='checkbox' id='condRowId_" + condRowId + "' name='isLine' value='"+allLine[i]+"' onclick='goIsLineClick();' checked><span></span></label></td>";
		condHTML += "  <td>"+ isLine[2] +"</td>";
		condHTML += "  <td>"+ isLine[6] +"</td>";
		condHTML += "  <td>"+ isLine[9] +"</td>";
		condHTML += "  <td>"+ isLine[12] +"</td>";
		condHTML += "  <td>"+ temp_sort +"</td>";
		condHTML += "  <td>"+ isLine[15] +"</td>";
		condHTML += "</tr>";
		condRowId++;
	}

	$("#divConditional").append(condHTML);

	// 콤보박스로 조건절을 선택하는 부분
	temp = lastIsLine;

	if(temp != "") {
		ii = temp.indexOf("|");
		isLine[0] = temp.substring(0, ii);

		for(kk = 1; kk <= 13; kk++) {
			temp = temp.substring(ii+1);
			ii = temp.indexOf("|");
			isLine[kk] = temp.substring(0, ii);
		}
		isLine[14] = temp.substring(ii+1);

		// 콤보박스를 표현해 주는 부분
		$("#tblInfo").val( isLine[0]+"|"+isLine[1]+"|"+isLine[2] );
		$("#tblNo").val( isLine[0] );
		$("#tblNm").val( isLine[1] );
		$("#tblAlias").val( isLine[3] );

		$("#colInfo").val( isLine[3]+"|"+isLine[4]+"|"+isLine[5]+"|"+isLine[6] );
		$("#colNo").val( isLine[3] );
		$("#colNm").val( isLine[4] );
		$("#colDataTy").val( isLine[5] );
		$("#colAlias").val( isLine[6] );

		if($("#valueInfo").attr("type") == 'text')
			$("#valueInfo").val( isLine[8] );
		else
			$("#valueInfo").val( isLine[7]+"|"+isLine[8]+"|"+isLine[9] );

		$("#valueNo").val( isLine[7] );
		$("#valueNm").val( isLine[8] );
		$("#valueAlias").val( isLine[9] );

		$("#operInfo").val( isLine[10]+"|"+isLine[11]+"|"+isLine[12] );
		$("#operNo").val( isLine[10] );
		$("#operNm").val( isLine[11] );
		$("#operAlias").val( isLine[12] );

		if(isLine[13] != 'none') $("#sort").val( isLine[13]+"|"+isLine[14] );
	}
}

//메타 테이블 컨텐츠 생성
function getMetaFrameContent() {
	var dbConnNo = $("#dbConnNo").val();
	var mergeCol = $("#mergeCol").val();
	$.ajax({
		type : "GET",
		url : "./segMetaFrameP.ums?dbConnNo=" + dbConnNo + "&mergeCol=" + mergeCol,
		dataType : "html",
		//async: false,
		success : function(pageHtml){
			$("#divMetaTableInfo").html(pageHtml);
		},
		error : function(){
			alert("Error!!");
		}
	});
}

//사용자그룹 선택시 사용자 목록 설정
function getUserList(deptNo) {
	$.getJSON("../../com/getUserList.json?deptNo=" + deptNo, function(data) {
		$("#userId").children("option:not(:first)").remove();
		$.each(data.userList, function(idx,item){
			var option = new Option(item.userNm,item.userId);
			$("#userId").append(option);
		});
	});
}

//메타 테이블 체크박스 클릭
function goColumnClick() {
	var selectSql = "";
	var fromSql = "";
	var mergeKey = "";
	var mergeCol = "";

	// 테이블 체크 박스 초기화
	$("input[name='metaTblNm']").each(function(idx, item) {
		$(item).prop("checked", false);
	});

	// Select절 구하는 부분
	var cnt = 0;
	$("input[name='metaColInfo']").each(function(x, column) {
		if($(column).is(":checked") == true) {
			if(cnt != 0) {
				selectSql += ", ";
				mergeKey  += ",";
				mergeCol  += ",";
			}
			selectSql += $(column).val();
			mergeKey  += $(column).val().substring($(column).val().indexOf(" AS ") + 4);
			mergeCol  += $(column).val().substring(0, $(column).val().indexOf(" AS "));
			cnt++;

			$("input[name='metaTblNm']").each(function(y,table) {
				if( $(table).val() == $(column).val().substring(0, $(column).val().indexOf(".")) ) {
					$(table).prop("checked", true);
				}
			});
		}
	});

	getFromSql();
	getWhereSql();
	$("#mergeKey").val(mergeKey);
	$("#mergeCol").val(mergeCol);
	$("#selectSql").val(selectSql);
}

//조회조건 테이블 선택시
function goTblSelect() {
	var tempStr = $("#tblInfo").val();
	if(tempStr == "") {
		$("#tblNo").val("");
		$("#tblNm").val("");
		$("#tblAlias").val("");
	} else {
		$("#tblNo").val( tempStr.substring(0, tempStr.indexOf("|")) );
		$("#tblNm").val( tempStr.substring(tempStr.indexOf("|")+1, tempStr.lastIndexOf("|")) );
		$("#tblAlias").val( tempStr.substring(tempStr.lastIndexOf("|")+1) );
	}

	var tmp = $("#tblInfo").val();
	var tblNo = tmp.substring(0,tmp.indexOf("|"));
	$.getJSON("./getMetaColumnList.json?tblNo=" + tblNo, function(data) {
		$("#colInfo").children("option:not(:first)").remove();
		$.each(data.metaColumnList, function(idx,item){
			var option = new Option(item.colAlias,item.colNo + "|" + item.colNm + "|" + item.colDataTyJdbc + "|" + item.colAlias);
			$("#colInfo").append(option);
		});
	});
}

//조회조건 컬럼 선택시
function goColSelect() {
	var tempStr = $("#colInfo").val();
	if(tempStr == "") {
		$("#colNo").val("");
		$("#colNm").val("");
		$("#colDataTy").val("");
		$("#colAlias").val("");
	} else {
		$("#colNo").val( tempStr.substring(0, tempStr.indexOf("|")) );
		tempStr = tempStr.substring(tempStr.indexOf("|")+1);
		$("#colNm").val( tempStr.substring(0, tempStr.indexOf("|")) );
		$("#colDataTy").val( tempStr.substring(tempStr.indexOf("|")+1, tempStr.lastIndexOf("|")) );
		$("#colAlias").val( tempStr.substring(tempStr.lastIndexOf("|")+1) );
	}

	var tmp = $("#colInfo").val();
	var colNo = tmp.substring(0,tmp.indexOf("|"));

	// 추출값(관계값) 설정
	$.getJSON("../../sys/dbc/metavalList.json?colNo=" + colNo, function(data) {
		if(data.metaValueList.length > 0) {
			$("#valueInfoDisplay").empty();

			var valueHtml = "";
			valueHtml += "<select id='valueInfo' name='valueInfo' onchange='goValueSelect();' title='추출값 선택'>";
			valueHtml += "<option value=''>선택</option>";
			valueHtml += "</select>";

			$("#valueInfoDisplay").html(valueHtml);

			$("#valueInfo").children("option:not(:first)").remove();
			$.each(data.metaValueList, function(idx,item){
				var option = new Option(item.valueAlias,item.valueNo + "|" + item.valueNm + "|" + item.valueAlias);
				$("#valueInfo").append(option);
			});
		} else {
			$("#valueInfoDisplay").empty();
			var valueHtml = "<input type='text' id='valueInfo' name='valueInfo' onkeyup='goValueSelect()'>";
			$("#valueInfoDisplay").html(valueHtml);
		}
	});

	// 조건식(관계식) 설정
	$.getJSON("./metaOperList.json?colNo=" + colNo, function(data) {
		$("#operInfo").children("option:not(:first)").remove();
		$.each(data.metaOperatorList, function(idx,item){
			var option = new Option(item.operAlias,item.operNo + "|" + item.operNm + "|" + item.operAlias);
			$("#operInfo").append(option);
		});
	});
}

//조회조건 추출값 선택시
function goValueSelect() {
	var tempStr = $("#valueInfo").val();
	if(tempStr == "") {
		$("#valueNo").val("");
		$("#valueNm").val("");
		$("#valueAlias").val("");
	} else if(tempStr.indexOf("|") == -1) {
		$("#valueNo").val("0");
		$("#valueNm").val( tempStr );
		$("#valueAlias").val( tempStr );
	} else {
		$("#valueNo").val( tempStr.substring(0, tempStr.indexOf("|")) );
		$("#valueNm").val( tempStr.substring(tempStr.indexOf("|")+1, tempStr.lastIndexOf("|")) );
		$("#valueAlias").val( tempStr.substring(tempStr.lastIndexOf("|")+1) );
	}
}

// 조회조건 조건식 선택시
function goOperSelect() {
	var tempStr = $("#operInfo").val();
	if(tempStr == "") {
		$("#operNo").val("");
		$("#operNm").val("");
		$("#operAlias").val("");
	} else {
		$("#operNo").val( tempStr.substring(0, tempStr.indexOf("|")) );
		$("#operNm").val( tempStr.substring(tempStr.indexOf("|")+1, tempStr.lastIndexOf("|")) );
		$("#operAlias").val( tempStr.substring(tempStr.lastIndexOf("|")+1) );
	}

	if($("#valueNm").val() == "") {
		alert("추출값을 선택해 주세요.");
		$("#operInfo option:eq(0)").prop("selected",true);
		return;
	}
}

//조회조건 정렬 선택하였을 경우
function goSortSelect() {
	if($("#tblInfo").val() == "") {
		alert("테이블을 선택해 주세요.");
		$("#sort option:eq(0)").prop("selected",true);
		return;
	}

	if($("#colInfo").val() == "") {
		alert("컬럼을 선택해 주세요.");
		$("#sort option:eq(0)").prop("selected",true);
		return;
	}

	if($("#valueInfo").val() == "") {
		alert("추출값을 선택해 주세요.");
		$("#sort option:eq(0)").prop("selected",true);
		return;
	}

	if($("#operInfo").val() == "") {
		alert("조건식을 선택해 주세요.");
		$("#sort option:eq(0)").prop("selected",true);
		return;
	}
}

//조회조건 등록 클릭시
function fRelSelect() {
	var temp = "";
	var sort = "";
	var colAlias = "";
	var oldValue = "";	
	var ii;

	if($("#tblInfo").val() == "") {
		alert("테이블을 선택해 주세요.");
		$("#sort option:eq(0)").prop("selected",true);
		return;
	}

	if($("#colInfo").val() == "") {
		alert("컬럼을 선택해 주세요.");
		$("#sort option:eq(0)").prop("selected",true);
		return;
	}

	if($("#valueInfo").val() == "") {
		alert("추출값을 선택해 주세요.");
		$("#sort option").eq(0).prop("selected",true);
		return;
	}

	if($("#operInfo").val() == "") {
		alert("조건식을 선택해 주세요.");
		$("#sort option").eq(0).prop("selected",true);
		return;
	}

	colAlias = $("#colAlias").val();

	temp = $("#sort").val().substring($("#sort").val().indexOf("|")+1);
	if(temp != "") sort = temp;

	oldValue = $("#valueInfo").val();
	if($("#valueInfo").val().indexOf("|") == -1) {
		$("#valueInfo").val( $("#valueInfo").val()+"|"+$("#valueInfo").val()+"|"+$("#valueInfo").val() );
	}
	
	var tmpSort = "";
	if($("#sort").val() == "") tmpSort = "none|none";
	else tmpSort = $("#sort").val();	
	var addValue =  $("#tblInfo").val() +"|"+ $("#colInfo").val() +"|"+ $("#valueNo").val() +"|"+ $("#valueNm").val() +"|"+ $("#valueAlias").val() +"|"+ $("#operInfo").val() +"|"+ tmpSort +"|"+ $("#whereRel").val();

	var condHTML = "";
	//var isLine = new Array(16);	
	var frm = document.segInfoForm;
	for(i=frm.length-1; i >= 0; i--) {
		if(frm.elements[i].name == "isLine") {
			temp = frm.elements[i].value;
			if(temp == addValue){
				$("#valueInfo").val(oldValue)
				alert("이미 등록된 추출 조건입니다");
				return;
			}
			/*
			if(frm.elements[i].checked) {
				temp = frm.elements[i].value;
				ii = temp.indexOf("|");
				isLine[0] = temp.substring(0, ii);

				for(kk = 1; kk <= 14; kk++) {
					temp = temp.substring(ii+1);
					ii = temp.indexOf("|");
					isLine[kk] = temp.substring(0, ii);
				}
				if(isLine[14] == "none") isLine[14] = "&nbsp;";
				isLine[15] = temp.substring(ii+1);

				// isLine[2] : 테이블 알리아스
				// isLine[6] : 컬럼 알리아스
				// isLine[9] : Value 알리아스
				// isLine[12] : Operator 알리아스
				// isLine[14] : Sort 알리아스(none일경우 ""로 변경)
				// isLine[15] : 관계(And/Or)

				condHTML += "<tr> ";
				condHTML += "  <td><label for='condRowId_" + condRowId + "'><input type='checkbox' id='condRowId_" + condRowId + "' name='isLine' value='"+frm.elements[i].value+"' onclick='goIsLineClick();' checked><span></span></label></td>";
				
				condHTML += "  <td>"+ isLine[2] +"</td>";
				condHTML += "  <td>"+ isLine[6] +"</td>";
				condHTML += "  <td>"+ isLine[9] +"</td>";
				condHTML += "  <td>"+ isLine[12] +"</td>";
				condHTML += "  <td>"+ isLine[14] +"</td>";
				condHTML += "  <td>"+ isLine[15] +"</td>";
				condHTML += "</tr>";
				
				chekExist = frm.elements[i].value;
				condRowId++;
			}
			*/
		}
	}

	condHTML += "        <tr> ";
	condHTML += "            <td><label for='condRowId_" + condRowId + "'><input type='checkbox' id='condRowId_" + condRowId + "' name='isLine' value='"+ $("#tblInfo").val() +"|"+ $("#colInfo").val() +"|"+ $("#valueNo").val() +"|"+ $("#valueNm").val() +"|"+ $("#valueAlias").val() +"|"+ $("#operInfo").val() +"|"+ tmpSort +"|"+ $("#whereRel").val() +"' onclick='goIsLineClick();' checked><span></span></label></td>";
	condHTML += "            <td>"+ frm.tblAlias.value +"</td>";
	condHTML += "            <td>"+ frm.colAlias.value +"</td>";
	condHTML += "            <td>"+ frm.valueAlias.value +"</td>";
	condHTML += "            <td>"+ frm.operAlias.value +"</td>";
	condHTML += "            <td>"+ sort +"</td>";
	condHTML += "            <td>"+ frm.whereRel.value +"</td>";
	condHTML += "        </tr>";
	
	condRowId++;
	
	$("#divConditional").append(condHTML);

	// 테이블, 컬럼, value, operator, 정렬, 관계식등을 초기화함.
	$("#tblInfo option").eq(0).prop("selected",true);
	$("#colInfo").children("option:not(:first)").remove();
	$("#valueInfoDisplay").empty();
	var valueHtml = "<input type='text' id='valueInfo' name='valueInfo' onkeyup='goValueSelect()' style='width:120px;'>";
	$("#valueInfoDisplay").html(valueHtml);
	$("#operInfo").children("option:not(:first)").remove();
	$("#sort option").eq(0).prop("selected",true);
	$("#whereRel option").eq(0).prop("selected",true);

	$("#tblNo").val("");
	$("#tblNm").val("");
	$("#tblAlias").val("");
	$("#colNo").val("");
	$("#colNm").val("");
	$("#colDataTy").val("");
	$("#colAlias").val("");
	$("#valueNo").val("");
	$("#valueNm").val("");
	$("#valueAlias").val("");
	$("#operNo").val("");
	$("#operNm").val("");
	$("#operAlias").val("");

	getFromSql();
	getWhereSql();
	getOrderbySql();
}

//조건식을 선택하였을 경우
function goIsLineClick() {
	getOrderbySql();
	getWhereSql();
	getFromSql();
}

//From절을 얻어온다..........
function getFromSql() {
	var arrayFromSql = new Array();    // from절에 들어갈 모든 테이블명을 넣어 놓는다... 나중에 하니씩 비교를 하여 중복되는 것을 삭제함.
	var j = 0;
	// 이건 Select 절에 필요한 부분에서 From 절 뽑아옴.
	$("input[name='metaTblNm']").each(function(idx,item){
		if($(item).is(":checked") == true) {
			arrayFromSql[j] = $(item).val();
			j++;
		}
	});

	// 이건 Where절에 필요한 부분에서 From 절 뽑아옴.
	$("input[name='isLine']").each(function(idx,item){
		if($(item).is(":checked") == true) {
			var tempIsLine = $(item).val().substring($(item).val().indexOf("|")+1);
			arrayFromSql[j] = tempIsLine.substring(0, tempIsLine.indexOf("|"));
			j++;
		}
	});

	// 마지막으로 Where절을 하고 있을때(operator와 정렬까지 선택하였을 경우)
	if($("#tblNm").val() != "") {
		arrayFromSql[j] = $("#tblNm").val();
		j++;
	}

	var tblCheck = false;                   // arrayFromSql 과 arrayTemp를 비교하여 false일 경우에만 fromSql 에 추가함.
	var arrayTemp = new Array();            // arrayFromSql 과 비교하기 위해 임시로 저장함.
	var fromSql = "";                       // 실질적으로 from절에 들어갈 값

	if(j > 0) {
		arrayTemp[0] = arrayFromSql[0];     // arrayTemp를 초기화함.
		fromSql = arrayTemp[0];

		j = 1;
		// 중복 테이블 명을 제거하는 부분
		for(i = 1; i < arrayFromSql.length; i++) {
			tblCheck = false;
			for(iii=0; iii < arrayTemp.length; iii++) {
				if(arrayTemp[iii] == arrayFromSql[i]) tblCheck = true;
			}

			if(!tblCheck) {
				arrayTemp[j] = arrayFromSql[i];
				fromSql += ", " + arrayTemp[j];
				j++;
			}
		}
	}
	$("#fromSql").val(fromSql);
}

//Where절을 얻어온다..........
function getWhereSql() {
	var isLine = new Array(16);
	var temp = "";
	var ii;

	var whereSql = "";

	var tblNm = "";
	var colNm = "";
	var colDataTy = "";   // 데이타 타입
	var colRel = "";        //조건식
	var valueNm = "";

	var srcWhere = "";      // 조건절의 모든 부분을 여기에 저장함.

	var pos = -1;

	$("input[name='isLine']").each(function(idx,item){
		if($(item).is(":checked") == true) {
			temp = $(item).val();
			srcWhere += temp + "##";

			ii = temp.indexOf("|");
			isLine[0] = temp.substring(0, ii);

			for(kk = 1;kk<=14;kk++) {
				temp = temp.substring(ii+1);
				ii = temp.indexOf("|");
				isLine[kk] = temp.substring(0, ii);
			}
			isLine[15] = temp.substring(ii+1);

			// 조건식을 만든다.
			if(isLine[11].toUpperCase() == "PLIKE" || isLine[11].toUpperCase() == "SLIKE") whereSql += isLine[1] + "." + isLine[4] + " LIKE ";
			else whereSql += isLine[1] + "." + isLine[4] + " " + isLine[11] + " ";

			// tblInfo   = 0. tblNo|1. tblNm|2. tblAlias
			// colInfo   = 3. colNo|4. colNm|5. colDataTy|6. colAlias
			// valueInfo = 7. valueNo|8. valueNm|9. valueAlias
			// operInfo  = 10. operNo|11. operNm|12. operAlias
			// sort      = 13. sortNm|14. sortAlias(NULL일 경우 none|none가 들어감)
			// whereRel  = 15. 관계식(AND 또는 OR)
			// =, <>, >, < ,  >=, <=, != 등은 그대로
			// in 일 경우 괄호를 삽입
			// like일 경우 %를 양쪽으로 삽입
			if(!checkDBStrType(colDataTy)) {    // ums.common.js에 존제
				if(isLine[11].toUpperCase() == "IN" || isLine[11].toUpperCase() == "NOT IN") {
					whereSql += "(" +isLine[8] + ") ";
				} else if(isLine[11].toUpperCase() == "LIKE" || isLine[11].toUpperCase() == "NOT LIKE") {
					whereSql += "%" + isLine[8] + "% ";
				} else if(isLine[11].toUpperCase() == "PLIKE") {
					whereSql += "" + isLine[8] + "% ";
				} else if(isLine[11].toUpperCase() == "SLIKE") {
					whereSql += "%" + isLine[8] + " ";
				} else {
					whereSql += isLine[8] + " ";
				}
			} else {
				if(isLine[11].toUpperCase() == "IN" || isLine[11].toUpperCase() == "NOT IN") {
					temp = isLine[8];
					valueNm = "";

					pos = temp.indexOf(",");
					while(pos != -1) {
						valueNm += "'" + temp.substring(0, pos) + "', ";
						temp = trim(temp.substring(pos+1));
						pos = temp.indexOf(",");
					}
					value += "'" + trim(temp) + "' ";

					whereSql += "(" +valueNm + ") ";
				} else if(isLine[11].toUpperCase() == "LIKE" || isLine[11].toUpperCase() == "NOT LIKE") {
					whereSql += "'%" + isLine[8] + "%' ";
				} else if(isLine[11].toUpperCase() == "PLIKE") {
					whereSql += "'" + isLine[8] + "%' ";
				} else if(isLine[11].toUpperCase() == "SLIKE") {
					whereSql += "'%" + isLine[8] + "' ";
				} else {
					whereSql += "'" + isLine[8] + "' ";
				}
			}
			whereSql += isLine[15] + " ";
		}
	});

	// 마지막에 붙는 관계식(AND/OR)를 삭제함.. (조건식 및 정렬을 선택하였을 경우에는 삭제하지 않음)
	if($("#tblNm").val() == "") {
		whereSql = whereSql.substring(0, whereSql.lastIndexOf(" "));
		whereSql = whereSql.substring(0, whereSql.lastIndexOf(" "));
	}

	// 마지막으로 Where절을 하고 있을때(operator와 정렬까지 선택하였을 경우)
	if($("#tblNm").val() != "") {
		tblNm = $("#tblNm").val();
		colNm = $("#colNm").val();
		colDataTy = $("#colDataTy").val();
		colRel = $("#operNm").val();
		valueNm = $("#valueNm").val();

		// isLine에 들어가지 않은 조건의 선택문을 srcWhere 저장해 놓음.
		// 추후, 수정에서 사용됨.
		// 수정할때, isLine부분과 조건을 선택하는 부분을 그대로 재현하기 위하여 정보를 DB에 저장하기 위함.

		var tmpSort = "";

		if($("#sort").val() == "") tmpSort = "none|none";
		else tmpSort = $("#sort").val();

		srcWhere += $("#tblInfo").val() +"|"+ $("#colInfo").val() +"|"+ $("#valueNo").val() +"|"+ $("#valueNm").val() +"|"+ $("#valueAlias").val() +"|"+ $("#operInfo").val() +"|"+ tmpSort;

		if(colRel.toUpperCase() == "PLIKE" || colRel.toUpperCase() == "SLIKE") whereSql += tblNm + "." + colNm + " LIKE ";
		else whereSql += tblNm + "." + colNm + " " + colRel + " ";

		// =, <>, >, < ,  >=, <=, != 등은 그대로
		// in 일 경우 괄호를 삽입
		// like일 경우 %를 양쪽으로 삽입
		if(!checkDBStrType(colDataTy)) {
			if(colRel.toUpperCase() == "IN" || colRel.toUpperCase() == "NOT IN") {
				whereSql += "(" +valueNm + ") ";
			} else if(colRel.toUpperCase() == "LIKE" || colRel.toUpperCase() == "NOT LIKE") {
				whereSql += "%" + valueNm + "% ";
			} else if(colRel.toUpperCase() == "PLIKE") {
				whereSql += "" + valueNm + "% ";
			} else if(colRel.toUpperCase() == "SLIKE") {
				whereSql += "%" + valueNm + " ";
			} else {
				whereSql += valueNm + " ";
			}
		} else {
			if(colRel.toUpperCase() == "IN" || colRel.toUpperCase() == "NOT IN") {
				var pos = -1;
				temp = valueNm;
				valueNm = "";

				pos = temp.indexOf(",");
				while(pos != -1) {
					valueNm += "'" + temp.substring(0, pos) + "', ";
					temp = trim(temp.substring(pos+1));
					pos = temp.indexOf(",");
				}
				value += "'" + trim(temp) + "' ";

				where_ += "(" +valueNm + ") ";
			} else if(colRel.toUpperCase() == "LIKE" || colRel.toUpperCase() == "NOT LIKE") {
				whereSql += "'%" + valueNm + "%' ";
			} else if(colRel.toUpperCase() == "PLIKE") {
				whereSql += "'" + valueNm + "%' ";
			} else if(colRel.toUpperCase() == "SLIKE") {
				whereSql += "'%" + valueNm + "' ";
			} else {
				whereSql += "'" + valueNm + "' ";
			}
		}
	}

	var joinTbl = getJoinTbl();

	if(joinTbl.length != 0 && whereSql.length != 0) whereSql = whereSql + " AND " + joinTbl;
	else if(joinTbl.length != 0 && whereSql.length == 0) whereSql = joinTbl;

	//$("#srcWhere").val( srcWhere );
	$("#whereSql").val( whereSql );
}

//order by 절
function getOrderbySql() {
	var isLine = new Array(16);
	var temp = "";
	var ii;

	var sortNm = "none";

	var orderbySql = "";

	var srcWhere = "";      // 조건절의 모든 부분을 여기에 저장함.

	var j = 0;
	$("input[name='isLine']").each(function(idx,item){
		if($(item).is(":checked") == true) {
			temp = $(item).val();

			srcWhere += temp + "##";

			ii = temp.indexOf("|");
			isLine[0] = temp.substring(0, ii);

			for(kk = 1; kk <= 14; kk++) {
				temp = temp.substring(ii+1);
				ii = temp.indexOf("|");
				isLine[kk] = temp.substring(0, ii);
			}

			if(isLine[13] != "none") {
				if(j != 0) orderbySql += ", ";
				orderbySql += isLine[1] + "." + isLine[4] + " " + isLine[13];

				if(sortNm != "none" && sortNm != isLine[13]) {
					if(sortNm == "none") sortNm = "";
					$("#orderbySql").val( orderbySql );
					$("#sort option").eq(0).prop("selected",true);
					return false;
				}
				sortNm = isLine[13];
				j++;
			}

		}

	});

	if($("#sort").val() != "") {
		if(sortNm != "none" && sortNm != $("#sort").val().substring(0, $("#sort").val().indexOf("|")) ) {
			if(sortNm == "none") sortNm = "";
			$("#orderbySql").val( orderbySql );
			$("#sort option").eq(0).prop("selected",true);
			return false;
		}

		if(j > 0) orderbySql += ", ";
		else sortNm = $("#sort").val().substring(0, $("#sort").val().indexOf("|"));

		orderbySql += $("#tblNm").val() + "." + $("#colNm").val();

		var tmpSort = "";

		if($("#sort").val() == "") tmpSort = "none|none";
		else tmpSort = $("#sort").val();

		srcWhere += $("#tblInfo").val() +"|"+ $("#colInfo").val() +"|"+ $("#valueNo").val() +"|"+ $("#valueNm").val() +"|"+ $("#valueAlias").val() +"|"+ $("#operInfo").val() +"|"+ tmpSort;
	}


	if(sortNm == "none") sortNm = "";
	//$("#srcWhere").val( srcWhere );
	if(orderbySql != "") $("#orderbySql").val( orderbySql );
	else $("#orderbySql").val("");
}


//대상수 구하기
function goSegCnt() {
	var obj = document.segform;

	if($("#dbConnNo").val().length == 0) {
		alert("Connection 을 선택해 주세요.");
		return;
	}

	if($("#selectSql").val().length == 0 || $("#fromSql").val().length == 0) {
		alert("쿼리문을 잘못 입력하셨습니다.");
		return;
	}

	var param = $("#segInfoForm").serialize();
	$.getJSON("./segCount.json?" + param, function(data) {
		$("#totCnt").val(data.totCnt);
		$("#txtTotCnt").html(data.totCnt + "명");
	});
}

// 수정버튼 클릭 시
function goSegToolUpdate() {
	if(status == "002") {
		alert("삭제된 발송대상그룹입니다!!\n삭제된 발송대상그룹은 수정을 할 수 없습니다!!");
		return;
	}
	if($("#dbConnNo").val() == "") {
		alert("Connection은 필수입력 항목입니다.");
		$("#dbConnNo").focus();
		return;
	}
	if(typeof $("#deptNo").val() != "undefined") {
		if($("#deptNo").val() == "0"){
			alert("사용자그룹은 필수입력 항목입니다.");
			$("#deptNo").focus();
			return;	
		}		
		if($("#deptNo").val() != "0" && $("#userId").val() == "") {
			alert("사용자는 필수입력 항목입니다.");
			$("#userId").focus();
			return;
		}
	}
	if($("#segNm").val() == "") {
		alert("수신자그룹명은 필수입력 항목입니다.");
		$("#segNm").focus();
		return;
	}
	if($("#selectSql").val() == "" || $("#fromSql").val() == "") {
		alert("질의문이 올바르지 않습니다.");
		return;
	}
	
	// 입력값 Byte 체크
	if($.byteString($("#segNm").val()) > 100) {
		alert("수신자그룹명은 100byte를 넘을 수 없습니다.");
		$("#segNm").focus();
		$("#segNm").select();
		return true;
	}
	if($.byteString($("#segDesc").val()) > 200) {
		alert("수신자그룹설명은 200byte를 넘을 수 없습니다.");
		$("#segDesc").focus();
		$("#segDesc").select();
		return true;
	}

	var srcWhere = "";
	var frm = document.segInfoForm;
	for(i=frm.length-1; i >= 0; i--) {
		if(frm.elements[i].name == "isLine") {
			if(frm.elements[i].checked) {
				temp = frm.elements[i].value;
				srcWhere += temp + "##";
			}
		}
	}
	$("#srcWhere").val(srcWhere);
	
	console.log($("#srcWhere").val());
	if($("#totCnt").val() == "0"){
		if(actionPreview == false) {
			var a = confirm("대상자수 추출을 하지 않았습니다.\n계속 실행을 하겠습니까?");
			if ( a ) {
				var param = $("#segInfoForm").serialize();
				$.getJSON("./segUpdate.json?" + param, function(data) {
					if(data.result == "Success") {
						alert("수정되었습니다.");
						$("#searchForm").attr("action","./segMainP.ums").submit();
					} else if(data.result == "Fail") {
						alert("수정 처리중 오류가 발생하였습니다.");
					}
				});
			} else return;
		} else {
			var param = $("#segInfoForm").serialize();
			$.getJSON("./segUpdate.json?" + param, function(data) {
				if(data.result == "Success") {
					alert("수정되었습니다.");
					$("#searchForm").attr("action","./segMainP.ums").submit();
				} else if(data.result == "Fail") {
					alert("수정 처리중 오류가 발생하였습니다.");
				}
			});
		}
	} else {
		var param = $("#segInfoForm").serialize();
		$.getJSON("./segUpdate.json?" + param, function(data) {
			if(data.result == "Success") {
				alert("수정되었습니다.");
				$("#searchForm").attr("action","./segMainP.ums").submit();
			} else if(data.result == "Fail") {
				alert("수정 처리중 오류가 발생하였습니다.");
			}
		});
	}
}

// 미리보기 클릭시
function goSegInfo() {
	if($("#dbConnNo").val().length == 0) {
		alert("Connection 을 선택해 주세요.");
		return;
	}
	if($("#selectSql").val().length == 0 || $("#fromSql").val().length == 0) {
		alert("쿼리문을 잘못 입력하셨습니다.");
		return;
	}
	
	// 대상수 구하기
	goSegCnt();
	
	$("#previewSegNm").html( $("#segNm").val() );
	var sql = "SELECT " + $("#selectSql").val() + " FROM " + $("#fromSql").val();
	if($("#whereSql").val() != "") {
		sql += " WHERE " + $("#whereSql").val();
	}
	if($("#orderbySql").val() != "") {
		sql += " ORDER BY " + $("#orderbySql").val();
	}
	$("#previewSql").html( sql );
	
	var mergeKey = $("#mergeKey").val().split(",");
	var mergeCol = $("#mergeCol").val().split(",");
	for(var i=0;i<mergeKey.length;i++) {
		var option = new Option(mergeKey[i], mergeCol[i]);
		$("#previewSearch").append(option);
	}	
	
	if(checkSearchReason) {
		goPageNumSeg("1");
	}
	fn.popupOpen('#popup_preview_seg');
}

// 미리보기 페이징(ums.common.js 변수&함수 포함)
var actionPreview = false;
function goPageNumSeg(pageNum) {
	$("#page").val( pageNum );
	var param = $("#segInfoForm").serialize();
	$.ajax({
		type : "GET",
		url : "./segDbMemberListP.ums?" + param + "&checkSearchReason=" + checkSearchReason + "&searchReasonCd=" + $("#searchReasonCd").val() + "&contentPath=" + window.location.pathname,
		dataType : "html",
		success : function(pageHtml){
			checkSearchReason = true;
			actionPreview = true;
			$("#previewMemberList").html(pageHtml);
		},
		error : function(){
			actionPreview = false;
			alert("Error!!");
		}
	});
}

// 사용중지 클릭시
function goDisable() {
	$("#status").val("001");
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수신자그룹이 사용중지 되었습니다.");
			$("#btnDisable").hide();
			$("#btnEnable").removeClass("hide");
			$("#btnEnable").show();
		} else if(data.result == "Fail") {
			alert("사용중지 처리중 오류가 발생하였습니다.");
		}
	});
}

// 복구 클릭시
function goEnable() {
	$("#status").val("000");
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수신자그룹이 복구 되었습니다.");
			$("#btnDisable").removeClass("hide");
			$("#btnDisable").show();
			$("#btnEnable").hide();
		} else if(data.result == "Fail") {
			alert("복구 처리중 오류가 발생하였습니다.");
		}
	});
}

// 삭제 클릭시
function goDelete() {
	$("#status").val("002");
	var param = $("#segInfoForm").serialize();
	$.getJSON("./segDelete.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("수신자그룹이 삭제 되었습니다.");
			
			$("#searchForm").attr("target","").attr("action","./segMainP.ums").submit();
		} else if(data.result == "Fail") {
			alert("삭제 처리중 오류가 발생하였습니다.");
		}
	});
}

// 목록 버튼 클릭시
function goSegList() {
	$("#searchForm").attr("target","").attr("action","./segMainP.ums").submit();
}
