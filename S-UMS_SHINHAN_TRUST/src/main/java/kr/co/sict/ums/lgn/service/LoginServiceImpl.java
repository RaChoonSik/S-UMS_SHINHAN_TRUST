/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 로그인 서비스 구현
 */
package kr.co.sict.ums.lgn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.lgn.dao.LoginDAO;
import kr.co.sict.ums.lgn.vo.LoginHistVO;
import kr.co.sict.ums.lgn.vo.LoginVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.acc.vo.UserProgVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDAO loginDAO;

    @Override
    public UserVO isValidUser(LoginVO loginVO) throws Exception {
        
        UserVO initPwdInfo = loginDAO.getInitPwdInfo(loginVO);
        if(initPwdInfo !=null && "Y".equals(initPwdInfo.getPwInitYn())) {
            loginVO.setpPwInitYn("Y");
        }
        return loginDAO.isValidUser(loginVO);
    }

    @Override
    public UserVO isSSOUser(LoginVO loginVO) throws Exception {
        return loginDAO.isSSOUser(loginVO);
    } 
    
    @Override
    public List<UserProgVO> getUserProgList(String userId) throws Exception {
        return loginDAO.getUserProgList(userId);
    } 

    @Override
    public List<SysMenuVO> getUserMenuList(String userId) throws Exception {
        return loginDAO.getUserMenuList(userId);
    }
    
    @Override
    public UserVO procRimanUserInsert(UserVO userVO) throws Exception {
        return loginDAO.procRimanUserInsert(userVO);
    }
}
