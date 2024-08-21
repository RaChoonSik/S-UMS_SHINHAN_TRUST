/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 정보 VO
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : sys.vo -->sys.acc.vo 
 */
package kr.co.sict.ums.sys.acc.vo;

import kr.co.sict.ums.com.vo.CommonVO;

public class UserVO extends CommonVO {

    private String userId;        // 아이디
    private String userPwd;        // 비밀번호
    private String userEm;        // 이메일
    private String userTel;        // 연락처
    private String userDesc;    // 사용자설명
    private String userNm;        // 사용자이름
    private int deptNo;            // 부서번호
    private String deptNm;        // 부서명
    private String status;        // 상태(000:정상, 001:사용중지, 002:삭제)
    private String statusNm;    // 상태명
    private String returnEm;    // Return이메일
    private String replyToEm;    // 회신이메일
    private String mailFromEm;    // 발송자이메일
    private String mailFromNm;    // 발송자명
    private String charset;        // 문자셋
    private String charsetNm;    // 문자셋명
    private String tzCd;        // 타임존
    private String tzNm;        // 타임존명
    private String tzTerm;        // 시간차
    private String uilang;        // UI언어권
    private String uilangNm;    // UI언어권명
    
    private String orgCd;        // 조직코드
    private String orgKorNm;    // 조직한글명
    private String orgEngNm;    // 조직영문명
    private String positionGb;    // 직급
    private String positionNm;    // 직급명
    private String jobGb;        // 직책코드
    private String jobNm;        // 직책명
    private String serviceGb;    // 사용가능서비스코드
    private String serviceNm;     // 사용가능서비스명
    private String pwInitYn;    // 사용자비밀번호 초기화 여부
    
    private String regId;        // 등록자
    private String regDt;        // 등록일자
    private String upId;        // 수정자
    private String upDt;        // 수정일자
    private String font;        // 폰트
    private String regNm;        // 등록자이름
    private String upNm;        // 수정자이름
    
    // 프로그램을 위해 추가
    private int selDeptNo;        // 콤보박스 부서번호
    private String progId;        // 프로그램ID
    private String userStatus;    // 사용자상태 
    
    // 검색
    private String searchUserId;    // 사용자ID 검색
    private String searchUserNm;    // 사용자명 검색
    private String searchStatus;    // 상태 검색
    private String searchDeptNo;    // 그룹
    private String searchServiceGb;    // 서비스권한
    private String searchOrgKorNm;    // 부서명
    private String searchOrgCd;        // 부서코드 
    private String userIds;            // 사용자 여러개
    private String arrSearchOrgCds[];    // 부서 여러개
    //서비스 구분용 
    private String useEMS;
    private String useRNS;
    private String useSMS;
    private String usePUSH;
    private String useSYS;
    
    //팝업 사용자 정보 수정 용 
    private int popDeptNo;
    private String  popUserNm;
    private String  popUserTel;
    private String  popUserEm;
    private String  popMailFromNm;
    private String  popMailFromEm;
    private String  popReplyToEm;
    private String  popReturnEm;
    private String  popCharset;
    private String  popTzCd;
    private String  popUilang;
    private String  popTzTerm;
    
    //인증 프로시져 OUT 변수 
    private int prslt;
    private String  rtnMsg;
    
    //비밀번호 변경 
    private String curPwd;        // 현재 비밀번호
    
    //링크 서비스
    private int    linkService;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserEm() {
        return userEm;
    }

    public void setUserEm(String userEm) {
        this.userEm = userEm;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public int getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(int deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptNm() {
        return deptNm;
    }

    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusNm() {
        return statusNm;
    }

    public void setStatusNm(String statusNm) {
        this.statusNm = statusNm;
    }

    public String getReturnEm() {
        return returnEm;
    }

    public void setReturnEm(String returnEm) {
        this.returnEm = returnEm;
    }

    public String getReplyToEm() {
        return replyToEm;
    }

    public void setReplyToEm(String replyToEm) {
        this.replyToEm = replyToEm;
    }

    public String getMailFromEm() {
        return mailFromEm;
    }

    public void setMailFromEm(String mailFromEm) {
        this.mailFromEm = mailFromEm;
    }

    public String getMailFromNm() {
        return mailFromNm;
    }

    public void setMailFromNm(String mailFromNm) {
        this.mailFromNm = mailFromNm;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharsetNm() {
        return charsetNm;
    }

    public void setCharsetNm(String charsetNm) {
        this.charsetNm = charsetNm;
    }

    public String getTzCd() {
        return tzCd;
    }

    public void setTzCd(String tzCd) {
        this.tzCd = tzCd;
    }

    public String getTzNm() {
        return tzNm;
    }

    public void setTzNm(String tzNm) {
        this.tzNm = tzNm;
    }

    public String getTzTerm() {
        return tzTerm;
    }

    public void setTzTerm(String tzTerm) {
        this.tzTerm = tzTerm;
    }

    public String getUilang() {
        return uilang;
    }

    public void setUilang(String uilang) {
        this.uilang = uilang;
    }

    public String getUilangNm() {
        return uilangNm;
    }

    public void setUilangNm(String uilangNm) {
        this.uilangNm = uilangNm;
    }

    public String getOrgCd() {
        return orgCd;
    }

    public void setOrgCd(String orgCd) {
        this.orgCd = orgCd;
    }

    public String getOrgKorNm() {
        return orgKorNm;
    }

    public void setOrgKorNm(String orgKorNm) {
        this.orgKorNm = orgKorNm;
    }

    public String getOrgEngNm() {
        return orgEngNm;
    }

    public void setOrgEngNm(String orgEngNm) {
        this.orgEngNm = orgEngNm;
    }

    public String getPositionGb() {
        return positionGb;
    }

    public void setPositionGb(String positionGb) {
        this.positionGb = positionGb;
    }

    public String getPositionNm() {
        return positionNm;
    }

    public void setPositionNm(String positionNm) {
        this.positionNm = positionNm;
    }

    public String getJobGb() {
        return jobGb;
    }

    public void setJobGb(String jobGb) {
        this.jobGb = jobGb;
    }

    public String getJobNm() {
        return jobNm;
    }

    public void setJobNm(String jobNm) {
        this.jobNm = jobNm;
    }

    public String getServiceGb() {
        return serviceGb;
    }

    public void setServiceGb(String serviceGb) {
        this.serviceGb = serviceGb;
    }

    public String getServiceNm() {
        return serviceNm;
    }

    public void setServiceNm(String serviceNm) {
        this.serviceNm = serviceNm;
    }

    public String getPwInitYn() {
        return pwInitYn;
    }

    public void setPwInitYn(String pwInitYn) {
        this.pwInitYn = pwInitYn;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public String getUpDt() {
        return upDt;
    }

    public void setUpDt(String upDt) {
        this.upDt = upDt;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getRegNm() {
        return regNm;
    }

    public void setRegNm(String regNm) {
        this.regNm = regNm;
    }

    public String getUpNm() {
        return upNm;
    }

    public void setUpNm(String upNm) {
        this.upNm = upNm;
    }

    public int getSelDeptNo() {
        return selDeptNo;
    }

    public void setSelDeptNo(int selDeptNo) {
        this.selDeptNo = selDeptNo;
    }

    public String getProgId() {
        return progId;
    }

    public void setProgId(String progId) {
        this.progId = progId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getSearchUserId() {
        return searchUserId;
    }

    public void setSearchUserId(String searchUserId) {
        this.searchUserId = searchUserId;
    }

    public String getSearchUserNm() {
        return searchUserNm;
    }

    public void setSearchUserNm(String searchUserNm) {
        this.searchUserNm = searchUserNm;
    }

    public String getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(String searchStatus) {
        this.searchStatus = searchStatus;
    }

    public String getSearchDeptNo() {
        return searchDeptNo;
    }

    public void setSearchDeptNo(String searchDeptNo) {
        this.searchDeptNo = searchDeptNo;
    }

    public String getSearchServiceGb() {
        return searchServiceGb;
    }

    public void setSearchServiceGb(String searchServiceGb) {
        this.searchServiceGb = searchServiceGb;
    }

    public String getSearchOrgKorNm() {
        return searchOrgKorNm;
    }

    public void setSearchOrgKorNm(String searchOrgKorNm) {
        this.searchOrgKorNm = searchOrgKorNm;
    }

    public String getSearchOrgCd() {
        return searchOrgCd;
    }

    public void setSearchOrgCd(String searchOrgCd) {
        this.searchOrgCd = searchOrgCd;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String[] getArrSearchOrgCds() {
        return arrSearchOrgCds;
    }

    public void setArrSearchOrgCds(String[] arrSearchOrgCds) {
        this.arrSearchOrgCds = arrSearchOrgCds;
    }

    public String getUseEMS() {
        return useEMS;
    }

    public void setUseEMS(String useEMS) {
        this.useEMS = useEMS;
    }

    public String getUseRNS() {
        return useRNS;
    }

    public void setUseRNS(String useRNS) {
        this.useRNS = useRNS;
    }

    public String getUseSMS() {
        return useSMS;
    }

    public void setUseSMS(String useSMS) {
        this.useSMS = useSMS;
    }

    public String getUsePUSH() {
        return usePUSH;
    }

    public void setUsePUSH(String usePUSH) {
        this.usePUSH = usePUSH;
    }

    public String getUseSYS() {
        return useSYS;
    }

    public void setUseSYS(String useSYS) {
        this.useSYS = useSYS;
    }

    public int getPopDeptNo() {
        return popDeptNo;
    }

    public void setPopDeptNo(int popDeptNo) {
        this.popDeptNo = popDeptNo;
    }

    public String getPopUserNm() {
        return popUserNm;
    }

    public void setPopUserNm(String popUserNm) {
        this.popUserNm = popUserNm;
    }

    public String getPopUserTel() {
        return popUserTel;
    }

    public void setPopUserTel(String popUserTel) {
        this.popUserTel = popUserTel;
    }

    public String getPopUserEm() {
        return popUserEm;
    }

    public void setPopUserEm(String popUserEm) {
        this.popUserEm = popUserEm;
    }

    public String getPopMailFromNm() {
        return popMailFromNm;
    }

    public void setPopMailFromNm(String popMailFromNm) {
        this.popMailFromNm = popMailFromNm;
    }

    public String getPopMailFromEm() {
        return popMailFromEm;
    }

    public void setPopMailFromEm(String popMailFromEm) {
        this.popMailFromEm = popMailFromEm;
    }

    public String getPopReplyToEm() {
        return popReplyToEm;
    }

    public void setPopReplyToEm(String popReplyToEm) {
        this.popReplyToEm = popReplyToEm;
    }

    public String getPopReturnEm() {
        return popReturnEm;
    }

    public void setPopReturnEm(String popReturnEm) {
        this.popReturnEm = popReturnEm;
    }

    public String getPopCharset() {
        return popCharset;
    }

    public void setPopCharset(String popCharset) {
        this.popCharset = popCharset;
    }

    public String getPopTzCd() {
        return popTzCd;
    }

    public void setPopTzCd(String popTzCd) {
        this.popTzCd = popTzCd;
    }

    public String getPopUilang() {
        return popUilang;
    }

    public void setPopUilang(String popUilang) {
        this.popUilang = popUilang;
    }

    public String getPopTzTerm() {
        return popTzTerm;
    }

    public void setPopTzTerm(String popTzTerm) {
        this.popTzTerm = popTzTerm;
    }

    public String getCurPwd() {
        return curPwd;
    }

    public void setCurPwd(String curPwd) {
        this.curPwd = curPwd;
    }

    public int getLinkService() {
        return linkService;
    }

    public void setLinkService(int linkService) {
        this.linkService = linkService;
    }

    public int getPrslt() {
        return prslt;
    }

    public void setPrslt(int prslt) {
        this.prslt = prslt;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }
}
