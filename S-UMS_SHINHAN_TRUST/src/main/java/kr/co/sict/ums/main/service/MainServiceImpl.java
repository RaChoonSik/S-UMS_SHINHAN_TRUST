/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 메인화면 서비스 구현
 */
package kr.co.sict.ums.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.main.dao.MainDAO;
import kr.co.sict.ums.main.vo.MenuVO;
import kr.co.sict.ums.sys.acc.vo.ServiceVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;

@Service
public class MainServiceImpl implements MainService {
	@Autowired
	private MainDAO mainDAO;

	@Override
	public List<MenuVO> getTopMenuList(String uilang)  throws Exception {
		return mainDAO.getTopMenuList(uilang);
	}

	@Override
	public SysMenuVO getMenuBasicInfo(String menuId) throws Exception {
		return mainDAO.getMenuBasicInfo(menuId);
	}

	@Override
	public SysMenuVO getServiceUserMenu(SysMenuVO sysMenuVO) throws Exception {
		return mainDAO.getServiceUserMenu(sysMenuVO);
	}

	@Override
	public SysMenuVO getUserMenuAuth(SysMenuVO sysMenuVO) throws Exception {
		return mainDAO.getUserMenuAuth(sysMenuVO);
	}
 
	@Override
	public List<ServiceVO> getUserService(String userId) throws Exception {
		return mainDAO.getUserService(userId);
	}

	@Override
	public SysMenuVO getSourcePathMenu(String sourcePath) throws Exception {
		return mainDAO.getSourcePathMenu(sourcePath);
	}
	
}
