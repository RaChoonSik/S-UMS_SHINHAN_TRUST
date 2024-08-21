/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 메인화면 서비스 인터페이스
 */
package kr.co.sict.ums.main.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.sict.ums.main.vo.MenuVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;

@Service
public interface MainService {
	/**
	 * 상단 메뉴 목록 조회
	 * @param uilang
	 * @return
	 * @throws Exception
	 */
	public List<MenuVO> getTopMenuList(String uilang) throws Exception;
	
	/**
	 * 기본 실행 경로 조회
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getMenuBasicInfo(String menuId) throws Exception;
	
	/**
	 * 각 서비스 기본 화면에서 첫번째 메뉴 조회
	 * @param sysMenuVO
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getServiceUserMenu(SysMenuVO sysMenuVO) throws Exception;
	
	/**
	 * 메뉴에 대한 사용자 권한 확인
	 * @param sysMenuVO
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getUserMenuAuth(SysMenuVO sysMenuVO) throws Exception;
	
	
	/**
	 * 서비스에 대한 사용자 권한 확인 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ServiceVO> getUserService(String userId) throws Exception;
	
	/**
	 * 시스템메뉴 정보에 등록된 경로인지 확인
	 * @param sourcePath
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getSourcePathMenu(String sourcePath) throws Exception;
	
}
