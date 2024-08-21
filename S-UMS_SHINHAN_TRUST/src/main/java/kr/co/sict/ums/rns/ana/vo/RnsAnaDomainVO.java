/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 도메인별통계 VO
 */
package kr.co.sict.ums.rns.ana.vo;

import kr.co.sict.ums.com.vo.CommonVO;
 
public class RnsAnaDomainVO extends CommonVO {
	
	private String domainname;	//도메인 이름
	private int total;						//총발송건수
	private int success;					//성공수
	private int fail;						//실패수
	private int syntax;						//문법오류
	private int dns;						//DNS에러
	private int transact;					//트랜잭션에러
	private int receiver;					//수신자오류
	private int network;					//네트워크
	private int service;					//서비스장애

	private String displaySuccess;					//표기되는 성공수(%)
	private String displayFail;						//표기되는 실패수(%)
	private String displaySyntax;					//표기되는 문법오류수(%)
	private String displayDns;						//표기되는 DNS에러수(%)
	private String displayTransact;					//표기되는 트랜잭션에러수(%)
	private String displayReceiver;					//표기되는 수신자오류수(%)
	private String displayNetwork;					//표기되는 네트워크수(%)
	private String displayService;					//표기되는 서비스장애수(%)

	//검색조건
	private String searchStdDt;			// 검색시작일
	private String searchEndDt;			// 검색종료일
	private String searchDomainName;	// 검색 도메인
	public String getDomainname() {
		return domainname;
	}
	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFail() {
		return fail;
	}
	public void setFail(int fail) {
		this.fail = fail;
	}
	public int getSyntax() {
		return syntax;
	}
	public void setSyntax(int syntax) {
		this.syntax = syntax;
	}
	public int getDns() {
		return dns;
	}
	public void setDns(int dns) {
		this.dns = dns;
	}
	public int getTransact() {
		return transact;
	}
	public void setTransact(int transact) {
		this.transact = transact;
	}
	public int getReceiver() {
		return receiver;
	}
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
	public int getNetwork() {
		return network;
	}
	public void setNetwork(int network) {
		this.network = network;
	}
	public int getService() {
		return service;
	}
	public void setService(int service) {
		this.service = service;
	}
	public String getDisplaySuccess() {
		return displaySuccess;
	}
	public void setDisplaySuccess(String displaySuccess) {
		this.displaySuccess = displaySuccess;
	}
	public String getDisplayFail() {
		return displayFail;
	}
	public void setDisplayFail(String displayFail) {
		this.displayFail = displayFail;
	}
	public String getDisplaySyntax() {
		return displaySyntax;
	}
	public void setDisplaySyntax(String displaySyntax) {
		this.displaySyntax = displaySyntax;
	}
	public String getDisplayDns() {
		return displayDns;
	}
	public void setDisplayDns(String displayDns) {
		this.displayDns = displayDns;
	}
	public String getDisplayTransact() {
		return displayTransact;
	}
	public void setDisplayTransact(String displayTransact) {
		this.displayTransact = displayTransact;
	}
	public String getDisplayReceiver() {
		return displayReceiver;
	}
	public void setDisplayReceiver(String displayReceiver) {
		this.displayReceiver = displayReceiver;
	}
	public String getDisplayNetwork() {
		return displayNetwork;
	}
	public void setDisplayNetwork(String displayNetwork) {
		this.displayNetwork = displayNetwork;
	}
	public String getDisplayService() {
		return displayService;
	}
	public void setDisplayService(String displayService) {
		this.displayService = displayService;
	}
	public String getSearchStdDt() {
		return searchStdDt;
	}
	public void setSearchStdDt(String searchStdDt) {
		this.searchStdDt = searchStdDt;
	}
	public String getSearchEndDt() {
		return searchEndDt;
	}
	public void setSearchEndDt(String searchEndDt) {
		this.searchEndDt = searchEndDt;
	}
	public String getSearchDomainName() {
		return searchDomainName;
	}
	public void setSearchDomainName(String searchDomainName) {
		this.searchDomainName = searchDomainName;
	}
	
 
}
