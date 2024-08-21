/**
 * 작성자 : 김준희
 * 작성일시 : 2021.08.26
 * 설명 : 사용자메뉴 권한 매퍼 
 */
package kr.co.sict.ums.sys.aut.dao;

import java.util.List;

import kr.co.sict.ums.sys.aut.vo.MenuUserMappVO;  

public interface AuthMapper {
	/**
	 * 사용자메뉴 권한  목록 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAuthList(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 권한  정보 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public MenuUserMappVO getMenuUserMappInfo(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 권한  정보 등록
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public int insertUserAuth(MenuUserMappVO menuUserMappVO) throws Exception;

	/**
	 * 사용자메뉴 권한  정보 삭제
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public int deleteUserAuth(MenuUserMappVO menuUserMappVO) throws Exception;

	/**
	 * 메뉴 사용 권한  정보 삭제
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuUserAuth(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 권한  목록 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAuthInfo(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자메뉴 하위 메뉴 갯수 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAuthChildCount(MenuUserMappVO menuUserMappVO) throws Exception;
	 
	/**
	 * 사용자 권한 부여가능한 메뉴 조회
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	 * 사용자 메뉴 목록 조회 - 메뉴기준-
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getUserAccessMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception;

	/**
	 * 메뉴별 사용자 목록 조회 - 메뉴기준
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getMenuUserList(MenuUserMappVO menuUserMappVO) throws Exception;	
	
	/**
	 * 메뉴별 사용자 목록 조회 - 사용자만
	 * @param menuUserMappVO
	 * @return
	 * @throws Exception
	 */
	public List<MenuUserMappVO> getMenuUserSimple(MenuUserMappVO menuUserMappVO) throws Exception;
	
	/**
	* 공통설정을 제외한 메뉴 조회
 	* @param menuUserMappVO
 	* @return
 	* @throws Exception
 	*/
	public List<MenuUserMappVO> getMenuList(MenuUserMappVO menuUserMappVO) throws Exception;					
	
}
