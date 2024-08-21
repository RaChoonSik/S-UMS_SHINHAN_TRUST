/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 매퍼==>계정관리 매퍼
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.dao ==> kr.co.sict.ums.sys.acc.dao
 *                계정관리외의 항목제거
 */
package kr.co.sict.ums.sys.acc.dao;

import java.util.List;

import kr.co.sict.ums.sys.acc.vo.DeptVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.acc.vo.OrganizationVO;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;
import kr.co.sict.ums.sys.acc.vo.UserProgVO;
import kr.co.sict.ums.sys.acc.vo.UserOrgVO;

public interface AccountMapper {
	/**
	 * 부서 목록 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<DeptVO> getDeptList(DeptVO deptVO) throws Exception;
	
	/**
	 * 부서 정보 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public DeptVO getDeptInfo(DeptVO deptVO) throws Exception;
	
	/**
	 * 부서 정보 등록
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public int insertDeptInfo(DeptVO deptVO) throws Exception;
	
	/**
	 * 부서 정보 수정
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public int updateDeptInfo(DeptVO deptVO) throws Exception;
	
	/**
	 * 부서 정보 삭제
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public int deleteDeptInfo(DeptVO deptVO) throws Exception;
	
	/**
	 * 조직 정보 조회
	 * 
	 * @param orgVO
	 * @return
	 * @throws Exception
	 */
	public OrganizationVO getOrgInfo(String orgCd) throws Exception;
	
	/**
	 * 조직 목록 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<OrganizationVO> getOrgList(OrganizationVO orgVO) throws Exception;
	
	/**
	 * 조직 정보 상세 목록 조회
	 * 
	 * @param orgVO
	 * @return
	 * @throws Exception
	 */
	public List<OrganizationVO> getOrgInfoList(OrganizationVO orgVO) throws Exception;	
	
	/**
	 * 조직 트리 생성용 : 상위 코드 찾기
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public String getOrgListTreeParent(String orgCd) throws Exception;		
	
	/**
	 * 조직 하위레벨 목록 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<OrganizationVO> getOrgChildList(String upOrgCd) throws Exception;	
	 
	/**
	 * 팝업용 : VIEW 사용 조직 정보 조회
	 * 
	 * @param orgVO
	 * @return
	 * @throws Exception
	 */
	public OrganizationVO getOrgInfoView(String orgCd) throws Exception;	
	
	/**
	 * 팝업용 : VIEW 사용 조직 목록 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<OrganizationVO> getOrgListView(OrganizationVO orgVO) throws Exception;
	
	/**
	 * 팝업용 : VIEW 사용 조직 하위레벨 목록 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<OrganizationVO> getOrgChildListView(String upOrgCd) throws Exception;
	 

	/**
	 * 조직 코드 최대값
	 * 
	 * @param lvlVal
	 * @return
	 * @throws Exception
	 */ 
	public String getOrgCodeMax(String lvlVal) throws Exception;
	
	/**
	 * 조직 내 사용자 수 조회
	 * 
	 * @param orgVO
	 * @return
	 * @throws Exception
	 */ 
	public int getOrgUserCount(OrganizationVO orgVO) throws Exception;
	
	/**
	 * 조직 등록
	 * 
	 * @param orgVO
	 * @return
	 * @throws Exception
	 */ 
	public int insertOragnizationInfo(OrganizationVO orgVO) throws Exception;

	/**
	 * 조직정보 수정
	 * 
	 * @param orgVO
	 * @return
	 * @throws Exception
	 */ 
	public int updateOrganizationInfo(OrganizationVO orgVO) throws Exception;

	/**
	 * 조직 삭제
	 * 
	 * @param orgVO
	 * @return
	 * @throws Exception
	 */
	public int deleteOrganizationInfo(OrganizationVO orgVO) throws Exception;	
	
	/**
	 * 사용자 목록 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> getUserList(UserVO userVO) throws Exception;
	
	/**
	 * 조직내 사용자 목록 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> getOrgUserList(UserVO userVO) throws Exception;
	
	/**
	 * 사용자 목록 축약 버전 조회
	 * @param deptVO
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> getUserListSimple(UserVO userVO) throws Exception;
	
	/**
	 * 사용자 정보 조회
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public UserVO getUserInfo(UserVO userVO) throws Exception;
	
	/**
	 * 사용자 정보 상세 조회
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public UserVO getUserInfoDetail(UserVO userVO) throws Exception;	
	
	/**
	 * 사용자 서비스 조회
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> getUserServiceList(UserVO userVO) throws Exception; 
	
	/**
	 * 서비스 조회
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public List<ServiceVO> getServiceList(ServiceVO serviceVO) throws Exception; 
	
	/**
	 * 사용자 아이디를 체크한다. 중복 방지용
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> userIdCheck(String userId) throws Exception;
	
	/**
	 * 사용자 정보 등록
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int insertUserInfo(UserVO userVO) throws Exception;
 
	
	/**
	 * 사용자 정보 수정
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int updateUserInfo(UserVO userVO) throws Exception;
	
	/**
	 * 사용자 정보 삭제
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserInfo(UserVO userVO) throws Exception;
	
	/**
	 * 사용자 프로그램 정보 등록
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int insertUserProgInfo(UserProgVO userProgVO) throws Exception;	
	
	/**
	 * 사용자 프로그램 정보 삭제
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserProgInfo(UserProgVO userProgVO) throws Exception;	
	
	/**
	 * 사용자 조직 정보 등록
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int insertUserOrgInfo(UserOrgVO userOrgVO) throws Exception;	
	
	/**
	 * 사용자 조직 정보 수정
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int updateUserOrgInfo(UserOrgVO userOrgVO) throws Exception;	
	
	/**
	 * 사용자 조직 정보 삭제 
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserOrgInfo(UserOrgVO userOrgVO) throws Exception;	
	
	/**
	 * 사용자 조직 정보 삭제 
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int updateServiceLink(UserVO userVO) throws Exception;	
	
	/**
	 * 사용자 조직 정보 삭제 
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int deleteServiceLink(UserVO userVO) throws Exception;	
	
	/**
	 * 사용자 비밀번호 확인
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int checkUserPwd(UserVO userVO) throws Exception ;		
	
	/**
	 * 사용자 비밀번호 변경
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int updateUserPwd(UserVO userVO) throws Exception ;	
	
	/**
	 * 사용자 시스템 메뉴 사용 권한 삭제
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserSysMenu(UserVO userVO) throws Exception ;
	
	/**
	 * 사용자 서비스별 기본 메뉴 권한 부여
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int insertUserDefaultMenu(UserProgVO userProgVO) throws Exception ;		
	
	/**
	 * 사용자 비밀번호 초기화
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public int initUserPwd(UserVO userVO) throws Exception ;
}
