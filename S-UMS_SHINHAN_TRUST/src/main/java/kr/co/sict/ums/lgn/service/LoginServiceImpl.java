/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 사용자 로그인 서비스 구현
 */
package kr.co.sict.ums.lgn.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.lgn.dao.LoginDAO;
import kr.co.sict.ums.lgn.vo.LoginVO;
import kr.co.sict.ums.sms.cam.vo.SmsVO;
import kr.co.sict.ums.sys.acc.vo.SysMenuVO;
import kr.co.sict.ums.sys.acc.vo.UserProgVO;
import kr.co.sict.ums.sys.acc.vo.UserVO;
import kr.co.sict.util.PropertiesUtil;

@Service
public class LoginServiceImpl implements LoginService {
	private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private LoginDAO loginDAO;
    
    @Autowired
	private PropertiesUtil properties;

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

	@Override
	public int isValidTwoFactor(LoginVO loginVO) throws Exception {
		int result = loginDAO.isValidTwoFactor(loginVO);
		if (result == 1) {
			// 인증코드가 맞으면 neo_user에 마지막 인증코드를 업데이트 함.
			// lgn/lgn.ums 접속시 체크
			
			loginDAO.updateNeoUserTwoFactorCode(loginVO);
			
		}
		
		return result;
	}

	@Override
	public int sendSms(UserVO userVO) throws Exception {
		LoginVO loginVO = new LoginVO();
		loginVO.setpUserId(userVO.getUserId());
		loginVO.setTwoFactorCode(randomCodeGenerator());
		loginDAO.updateTwoFactor(loginVO);
		
		//userVO.getUserTel()			//전화번호
		//loginVO.getTwoFactorCode();	//코드
		String msg = "인증번호는 ["+loginVO.getTwoFactorCode()+"] 입니다.";
		
		logger.debug(msg);
		
		String sendPhone = properties.getProperty("SMS.SEND_PHONE_NUMBER");
		
		UUID uuid = UUID.randomUUID();
		String cmid = uuid.toString().replaceAll("-", "");
		
		SmsVO smsVO = new SmsVO();
		smsVO.setMsgType(0);
		smsVO.setCmid(cmid);
		smsVO.setDestPhone(userVO.getUserTel());
		smsVO.setSendPhone(sendPhone);
		smsVO.setMsgBody(msg);
		
		return loginDAO.insertSmsSend(smsVO);
	}
	
	/**
	 * 10000 ~ 99999 범위의 숫자 생성
	 * @return
	 */
	private String randomCodeGenerator() {
		Random random = new Random();
		int randomCode = 10000 + random.nextInt(90000);
		return String.valueOf(randomCode);
	}

	@Override
	public int checkAuthUser(LoginVO loginVO) {
		return loginDAO.checkAuthUser(loginVO);
	}
}
