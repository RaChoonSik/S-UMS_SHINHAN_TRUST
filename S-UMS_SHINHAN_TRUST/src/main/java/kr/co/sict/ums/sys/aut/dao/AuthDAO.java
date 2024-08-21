/**
 * 작성자 : 김준희
 * 작성일시 : 2021.08.26
 * 설명 : 사용자메뉴 권한 관리 처리
 */
package kr.co.sict.ums.sys.aut.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sys.aut.vo.MenuUserMappVO;

@Repository
public class AuthDAO implements AuthMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<MenuUserMappVO> getUserAuthList(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAuthList(menuUserMappVO);
	}

	@Override
	public MenuUserMappVO getMenuUserMappInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuUserMappInfo(menuUserMappVO);
	}

	@Override
	public int insertUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).insertUserAuth(menuUserMappVO);
	}

	@Override
	public int deleteUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).deleteUserAuth(menuUserMappVO);
	}  

	@Override
	public int deleteMenuUserAuth(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).deleteMenuUserAuth(menuUserMappVO);
	}  
	
	@Override
	public List<MenuUserMappVO> getUserAuthInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAuthInfo(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getUserAuthChildCount(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAuthChildCount(menuUserMappVO);
	}

	@Override
	public List<MenuUserMappVO> getUserMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserMenuInfo(menuUserMappVO);
	}	
	
	@Override
	public List<MenuUserMappVO> getUserAccessMenuInfo(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getUserAccessMenuInfo(menuUserMappVO);
	}

	
	@Override
	public List<MenuUserMappVO> getMenuUserList(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuUserList(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getMenuUserSimple(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuUserSimple(menuUserMappVO);
	}
	
	@Override
	public List<MenuUserMappVO> getMenuList(MenuUserMappVO menuUserMappVO) throws Exception {
		return sqlSessionEms.getMapper(AuthMapper.class).getMenuList(menuUserMappVO);
	}	
	
}
