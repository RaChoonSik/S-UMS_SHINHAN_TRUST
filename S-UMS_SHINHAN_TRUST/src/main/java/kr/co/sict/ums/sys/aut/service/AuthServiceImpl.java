/**
 * 작성자 : 김준희
 * 작성일시 : 2021.07.07
 * 설명 : 사용자메뉴사용 권한
 */
package kr.co.sict.ums.sys.aut.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.aut.dao.AuthDAO;
import kr.co.sict.ums.sys.aut.vo.MenuUserMappVO; 

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private AuthDAO authDAO;

	@Override
	public List<MenuUserMappVO> getUserAuthList(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAuthList(menuUserMappVO);
	}

	@Override
	public MenuUserMappVO getMenuUserMappInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuUserMappInfo(menuUserMappVO);
	}

	@Override
	public int insertUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.insertUserAuth(menuUserMappVO);
	}
 
	@Override
	public int deleteUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return  authDAO.deleteUserAuth(menuUserMappVO);
	}
	 
	@Override
	public int deleteMenuUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return  authDAO.deleteMenuUserAuth(menuUserMappVO);
	}
	 
	
	@Override
	public List<MenuUserMappVO> getUserAuthInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAuthInfo(menuUserMappVO);
	}

	@Override
	public List<MenuUserMappVO> getUserAuthChildCount(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAuthChildCount(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getUserMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserMenuInfo(menuUserMappVO);
	}	
	
	@Override
	public List<MenuUserMappVO> getUserAccessMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getUserAccessMenuInfo(menuUserMappVO);
	}

	@Override
	public List<MenuUserMappVO> getMenuUserList(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuUserList(menuUserMappVO);
	}
	

	@Override
	public List<MenuUserMappVO> getMenuUserSimple(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuUserSimple(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getMenuList(MenuUserMappVO menuUserMappVO) throws Exception {
		return authDAO.getMenuList(menuUserMappVO);
	}
	
}
