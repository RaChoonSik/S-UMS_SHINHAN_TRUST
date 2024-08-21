/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 로그인 데이터 처리
 */
package kr.co.sict.ums.lgn.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.lgn.vo.LoginHistVO;
import kr.co.sict.ums.lgn.vo.LoginVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.acc.vo.UserProgVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;

@Repository
public class LoginDAO implements LoginMapper {
    @Autowired
    SqlSession sqlSessionEms;

    @Override
    public UserVO isValidUser(LoginVO loginVO) throws Exception {
        return sqlSessionEms.getMapper(LoginMapper.class).isValidUser(loginVO);
    } 

    @Override
    public List<UserProgVO> getUserProgList(String userId) throws Exception {
        return sqlSessionEms.getMapper(LoginMapper.class).getUserProgList(userId);        
    }
 
    @Override
    public List<SysMenuVO> getUserMenuList(String userId) throws Exception {
        return sqlSessionEms.getMapper(LoginMapper.class).getUserMenuList(userId);
    }
    
    @Override
    public UserVO isSSOUser(LoginVO loginVO) throws Exception {
        return sqlSessionEms.getMapper(LoginMapper.class).isSSOUser(loginVO);
    }
    
    @Override
    public UserVO getInitPwdInfo(LoginVO loginVO) throws Exception {
        return sqlSessionEms.getMapper(LoginMapper.class).getInitPwdInfo(loginVO);
    }
    
    @Override
    public UserVO procRimanUserInsert(UserVO userVO) throws Exception {
        return sqlSessionEms.getMapper(LoginMapper.class).procRimanUserInsert(userVO);
    }
}
