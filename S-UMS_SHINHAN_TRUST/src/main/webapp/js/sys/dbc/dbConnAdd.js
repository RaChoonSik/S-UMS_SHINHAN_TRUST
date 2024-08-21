
//DB DRIVER, URL 설정 EVENT 구현
function setDB(dbTy) {
	var driver = "";
	var url = "";
	if(dbTy == "000") {			//ORACLE
		driver = "oracle.jdbc.driver.OracleDriver";
		url = "jdbc:oracle:thin:@[IP]:[PORT]:[SID]";
	} else if(dbTy == "001") {	//MSSQL
		driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		url = "jdbc:microsoft:sqlserver://[IP]:[PORT];databaseName=[Database Name]";
	} else if(dbTy == "002") {	//INFORMIX
		driver = "com.informix.jdbc.IfxDriver";
		url = "jdbc:informix-sqli://[IP]:[PORT]/[DB명]:informixserver=[SERVER명]";
	} else if(dbTy == "003") {	//SYBASE
		driver = "com.sybase.jdbc2.jdbc.SybDriver";
		url = "jdbc:sybase:Tds:[IP]:[PORT]";
	} else if(dbTy == "004") {	//MYSQL
		//driver = "com.mysql.jdbc.Driver";
		driver = "com.mysql.cj.jdbc.Driver";
		url = "jdbc:mysql://[IP]/[DB명]";
	} else if(dbTy == "005") {	//UNISQL
		driver = "unisql.jdbc.driver.UniSQLDriver";
		url = "jdbc:unisql:[IP]:[PORT]:[Database Name]:::";
	} else if(dbTy == "006") {	//INGRES
		driver = "ca.edbc.jdbc.EdbcDriver";
		url = "jdbc:edhc://[IP]:IM7/[Node Name]";
	} else if(dbTy == "007") {	//DB2
		driver = "COM.ibm.db2.jdbc.net.DB2Driver";
		url = "jdbc:db2://[IP]:[PORT]/[DB NAME]";
	} else if(dbTy == "008") {	//POSTGRES
		driver = "org.postgresql.Driver";
		url = "jdbc:postgresql://[IP]:[PORT]/[Database Name]";
	} else if(dbTy == "009") {	//CUBRID
		driver = "cubrid.jdbc.driver.CUBRIDDriver";
		url = "jdbc:cubrid:[IP]:30000:[Database Name]:::?charset=EUC-KR";
	}

	$("#dbDriver").val(driver);
	$("#dbUrl").val(url);
}

// 연결 테스트
function testDbConn() {
	var errflag = false;
	var errstr = "";

	if($("#dbDriver").val() == "") {
		errflag = true;
		errstr += " [DRIVER] ";
	}

	if($("#dbUrl").val() == "") {
		errflag = true;
		errstr += " [URL] ";
	}

	if($("#loginId").val() == "") {
		errflag = true;
		errstr += " [DB ID] ";
	}

	if($("#loginPwd").val() == "") {
		errflag = true;
		errstr += " [DB PASSWORD] ";
	}

	if(errflag) {
		alert("<spring:message code='COMJSALT016'/>\n" + errstr); //다음 정보를 확인하세요.
		return;
	}
	
	var param = $("#dbConnInfoForm").serialize();
	$.getJSON("/sys/dbc/dbConnTest.json?" + param, function(data) {
		if(data.result == "Success") {
			alert("연결테스트에 성공하였습니다");	//연결 성공
		} else if(data.result == "Fail") {
			alert("연결테스트에 실패하였습니다" + data.errMsg);	//연결 실패
		}
	});
}

// 초기화 버튼 클릭시
function goReset() {
	$("#dbConnInfoForm")[0].reset(); 
} 

// 등록 클릭시
function goAdd() {
 
	if(checkForm()) {
		return;
	}
		
	var param = $("#dbConnInfoForm").serialize();
	$.getJSON("/sys/dbc/dbConnAdd.json?" + param, function(data) {
		if(data.result == "Success") {
			document.location.href= "./dbConnListP.ums";
			$("#dbConnInfoForm").attr("target","").attr("action","./dbConnListP.ums").submit();
		} else {
			alert("등록에 실패하였습니다");
		}
	});
}

// 입력 폼 검사
function checkForm() {
	var errstr = "";
	var errflag = false;
	if($("#dbConnNm").val() == "") {
		errstr += "[CONNECTION명]"; 
		errflag = true;
	}
	
	if(!$('#dbTy > option:selected').val()) {
		errstr += "[DB종류]";
		errflag = true;
	}

	if($("#dbDriver").val() == "") {
		errstr += "[DB DRIVER]"; 
		errflag = true;
	}
 
	if($("#dbUrl").val() == "") {
		errstr += "[URL]"; 
		errflag = true;
	}
	
	if(!$('#dbCharSet > option:selected').val()) {
		errstr += "[문자타입]";
		errflag = true;
	}
 
	
	if(!$('#status > option:selected').val()) {
		errstr += "[상태]";
		errflag = true;
	}
 
 	if($("#loginId").val() == "") {
		errstr += "[DB ID]"; 
		errflag = true;
	}
	
	if($("#loginPwd").val() == "") {
		errstr += "[DB PASSWORD]"; 
		errflag = true;
	}	

	if(errflag) {
		alert("다음 정보를 확인하세요.\n" + errstr);
	}
	
	if($.byteString($("#dbConnDesc").val()) > 380 ) {
		alert("설명은 400byte를 넘을 수 없습니다.");
		$("#dbConnDesc").focus();
		$("#dbConnDesc").select();
		errflag = true;
	} 
		
	return errflag;
}

//  취소 클릭시(리스트로 이동)
function goCancel() {
	$("#dbConnInfoForm").attr("target","").attr("action","./dbConnListP.ums").submit();
}
