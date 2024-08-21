/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 데이터 처리==>계정관리 데이터 처리
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.dao ==> kr.co.sict.ums.sys.acc.dao
 *                계정관리외의 함수제거
 */
package kr.co.sict.ums.sys.acc.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sys.acc.vo.DeptVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.ums.sys.acc.vo.OrganizationVO;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;
import kr.co.sict.ums.sys.acc.vo.UserProgVO;
import kr.co.sict.ums.sys.acc.vo.UserOrgVO;

@Repository
public class AccountDAO implements AccountMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<DeptVO> getDeptList(DeptVO deptVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getDeptList(deptVO);
	}

	@Override
	public DeptVO getDeptInfo(DeptVO deptVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getDeptInfo(deptVO);
	}

	@Override
	public int insertDeptInfo(DeptVO deptVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).insertDeptInfo(deptVO);
	}

	@Override
	public int updateDeptInfo(DeptVO deptVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).updateDeptInfo(deptVO);
	}

	@Override
	public int deleteDeptInfo(DeptVO deptVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).deleteDeptInfo(deptVO);
	}
	
	@Override
	public OrganizationVO getOrgInfo(String orgCd) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgInfo(orgCd);
	}

	@Override
	public List<OrganizationVO> getOrgList(OrganizationVO orgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgList(orgVO);
	} 

	@Override
	public List<OrganizationVO> getOrgInfoList(OrganizationVO orgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgInfoList(orgVO);
	}	
	
	@Override
	public String getOrgListTreeParent(String orgCd) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgListTreeParent(orgCd);
	}	
	
	@Override
	public List<OrganizationVO> getOrgChildList(String upOrgCd) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgChildList(upOrgCd);
	} 
	
	@Override
	public OrganizationVO getOrgInfoView(String orgCd) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgInfoView(orgCd);
	}  
	
	@Override
	public List<OrganizationVO> getOrgListView(OrganizationVO orgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgListView(orgVO);
	}  
	
	@Override
	public List<OrganizationVO> getOrgChildListView(String upOrgCd) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgChildListView(upOrgCd);
	} 
	  
	@Override
	public String getOrgCodeMax(String lvlVal) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgCodeMax(lvlVal);		
	}
	
	@Override
	public int getOrgUserCount(OrganizationVO orgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgUserCount(orgVO);		
	}
	
	@Override
	public int insertOragnizationInfo(OrganizationVO orgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).insertOragnizationInfo(orgVO);		
	}

	@Override
	public int updateOrganizationInfo(OrganizationVO orgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).updateOrganizationInfo(orgVO);
	}
	
	@Override
	public int deleteOrganizationInfo(OrganizationVO orgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).deleteOrganizationInfo(orgVO);
	} 
	
	@Override
	public List<UserVO> getUserList(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getUserList(userVO);
	}

	@Override
	public List<UserVO> getOrgUserList(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getOrgUserList(userVO);
	}
	
	@Override
	public List<UserVO> getUserListSimple(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getUserListSimple(userVO);
	}
	
	@Override
	public UserVO getUserInfo(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getUserInfo(userVO);
	}
	
	@Override
	public UserVO getUserInfoDetail(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).getUserInfoDetail(userVO);
	}
	
	@Override public List<UserVO> getUserServiceList(UserVO userVO) throws Exception { 
	return sqlSessionEms.getMapper(AccountMapper.class).getUserServiceList(userVO); 
	}
	
	@Override public List<ServiceVO> getServiceList(ServiceVO serviceVO) throws Exception { 
	return sqlSessionEms.getMapper(AccountMapper.class).getServiceList(serviceVO); 
	}
	 	
	@Override
	public List<UserVO> userIdCheck(String userId) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).userIdCheck(userId);
	}

	@Override
	public int insertUserInfo(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).insertUserInfo(userVO);
	} 

	@Override
	public int updateUserInfo(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).updateUserInfo(userVO);
	}
	
	@Override
	public int deleteUserInfo(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).deleteUserInfo(userVO);
	}
	
	@Override
	public int insertUserProgInfo(UserProgVO userProgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).insertUserProgInfo(userProgVO);
	}
	
	@Override
	public int deleteUserProgInfo(UserProgVO userProgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).deleteUserProgInfo(userProgVO);
	}
	
	@Override
	public int insertUserOrgInfo(UserOrgVO userOrgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).insertUserOrgInfo(userOrgVO);
	}
	
	@Override
	public int updateUserOrgInfo(UserOrgVO userOrgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).updateUserOrgInfo(userOrgVO);
	}	
 
	@Override
	public int deleteUserOrgInfo(UserOrgVO userOrgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).deleteUserOrgInfo(userOrgVO);
	}
	
	@Override
	public int updateServiceLink(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).updateServiceLink(userVO);
	}
	
	@Override
	public int deleteServiceLink(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).deleteServiceLink(userVO);
	}
	
	@Override
	public int checkUserPwd(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).checkUserPwd(userVO);
	}	
	
	@Override
	public int updateUserPwd(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).updateUserPwd(userVO);
	}
		
	@Override
	public int deleteUserSysMenu(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).deleteUserSysMenu(userVO);
	}

	@Override
	public int insertUserDefaultMenu(UserProgVO userProgVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).insertUserDefaultMenu(userProgVO);
	}
	
	@Override
	public int initUserPwd(UserVO userVO) throws Exception {
		return sqlSessionEms.getMapper(AccountMapper.class).initUserPwd(userVO);
	}	
}
