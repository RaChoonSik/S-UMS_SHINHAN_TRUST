/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 메인화면 데이터 처리
 */
package kr.co.sict.ums.main.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.main.vo.MenuVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;

@Repository
public class MainDAO implements MainMapper {
	@Autowired
	SqlSession sqlSessionEms;

	@Override
	public List<MenuVO> getTopMenuList(String uilang) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getTopMenuList(uilang);
	}

	@Override
	public SysMenuVO getMenuBasicInfo(String menuId) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getMenuBasicInfo(menuId);
	}

	@Override
	public SysMenuVO getServiceUserMenu(SysMenuVO sysMenuVO) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getServiceUserMenu(sysMenuVO);
	}

	@Override
	public SysMenuVO getUserMenuAuth(SysMenuVO sysMenuVO) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getUserMenuAuth(sysMenuVO);
	}

	@Override
	public List<ServiceVO> getUserService(String userId) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getUserService(userId);
	}

	@Override
	public SysMenuVO getSourcePathMenu(String sourcePath) throws Exception {
		return sqlSessionEms.getMapper(MainMapper.class).getSourcePathMenu(sourcePath);
	}
	
}
