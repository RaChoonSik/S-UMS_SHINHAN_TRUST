/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 서비스 구현==>사용자코드관리 서비스 구현
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.service ==> kr.co.sict.ums.sys.cod.service
 *               로그인히스토리 외의 함수 제거  나중에 각종 로그 정보 추가해야함 
 * 수정자 : 김준희
 * 수정일시 : 2021.08.11
 * 수정내역 : 코드 그룹 및 코드 관리 변경 개발           
 */
package kr.co.sict.ums.sys.cod.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.sys.cod.dao.UserCodeDAO;
import kr.co.sict.ums.sys.cod.vo.UserCodeVO;
import kr.co.sict.ums.sys.cod.vo.SenderKeyVO;
import kr.co.sict.ums.sys.cod.vo.UserCodeGroupVO;

@Service
public class UserCodeServiceImpl implements UserCodeService {
	@Autowired
	private UserCodeDAO userCodeDAO;
	
	@Override
	public List<UserCodeGroupVO> getUserCodeGroupList(UserCodeGroupVO userCodeGroupVO) throws Exception {
		return userCodeDAO.getUserCodeGroupList(userCodeGroupVO);
	}
	
	@Override
	public UserCodeGroupVO getUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception {
		return userCodeDAO.getUserCodeGroupInfo(userCodeGroupVO);
	}
	 
	@Override
	public int insertUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception {
		int result = 0;
		String[] uilang = userCodeGroupVO.getUilang().split(",");
		String[] cdGrpNm = userCodeGroupVO.getCdGrpNm().split(",");
		String[] cdGrpDtl = userCodeGroupVO.getCdGrpDtl().split(",");
		
		//분류코드 번호 자동 발번 
		String insCdGrp = userCodeDAO.getCodeGroup(userCodeGroupVO);
		userCodeGroupVO.setCdGrp(insCdGrp);
		for(int i=0;i<uilang.length;i++) {
			UserCodeGroupVO codeGruop = new UserCodeGroupVO();
			codeGruop.setUilang(uilang[i]);
			codeGruop.setCdGrp(userCodeGroupVO.getCdGrp());			
			codeGruop.setCdGrpNm(cdGrpNm[i].trim());
			codeGruop.setCdGrpDtl(cdGrpDtl[i].trim());
			codeGruop.setUpCdGrp(userCodeGroupVO.getUpCdGrp());
			codeGruop.setUseYn(userCodeGroupVO.getUseYn());
			codeGruop.setSysYn(userCodeGroupVO.getSysYn());			
			codeGruop.setRegId(userCodeGroupVO.getRegId());	
			codeGruop.setRegDt(userCodeGroupVO.getRegDt());				
			//동일한 코드 또는 이름 있는지 확인. 존재시 오류 
			if (userCodeDAO.getCodeGroupCountByCodeGroup(codeGruop) > 0 ) {
				return -111; 
			}
			else if (userCodeDAO.getCodeGroupCountByCodeGroupNm(codeGruop) > 0 ) {
				return -112; 
			}
			else {
				result += userCodeDAO.insertUserCodeGroupInfo(codeGruop);
			}
		}
		return result;
	}

	@Override
	public int updateUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception {
		return userCodeDAO.updateUserCodeGroupInfo(userCodeGroupVO);
	}

	@Override
	public int deleteUserCodeGroupInfo(UserCodeGroupVO userCodeGroupVO) throws Exception {		
		/*
		 * if (userCodeDAO.getCodeCountUnderCodeGroup(userCodeGroupVO) > 0 ) { return
		 * -444; } else { return userCodeDAO.deleteUserCodeGroupInfo(userCodeGroupVO); }
		 */
		return userCodeDAO.deleteUserCodeGroupInfo(userCodeGroupVO);
	}
	
	@Override
	public List<UserCodeVO> getUserCodeList(UserCodeVO userCodeVO) throws Exception {
		return userCodeDAO.getUserCodeList(userCodeVO);
	}
	
	@Override
	public UserCodeVO getUserCodeInfo(UserCodeVO userCodeVO) throws Exception {
		return userCodeDAO.getUserCodeInfo(userCodeVO);
	}
	
	@Override
	public String getUserCodeGroupUpCdGrp(UserCodeVO userCodeGroupVO) throws Exception {
		return userCodeDAO.getUserCodeGroupUpCdGrp(userCodeGroupVO);
	}
	
	@Override
	public String insertUserCodeInfo(UserCodeVO userCodeVO) throws Exception {
		String result = "";
		
		String[] uilang = userCodeVO.getUilang().split(",");
		String[] cdNm = userCodeVO.getCdNm().split(",");
		String[] cdDtl = userCodeVO.getCdDtl().split(",");
		
		//분류코드 번호 자동 발번 
		String insCd = String.format("%03d",userCodeDAO.getCode(userCodeVO));
 
		userCodeVO.setCd(insCd);
		
		for(int i=0;i<uilang.length;i++) {
			UserCodeVO code = new UserCodeVO();
			code.setUilang(uilang[i]);
			code.setCdGrp(userCodeVO.getCdGrp());
			code.setCd(userCodeVO.getCd());
			code.setCdNm(cdNm[i].trim());
			code.setCdDtl(cdDtl[i].trim());
			code.setUseYn(userCodeVO.getUseYn());
			code.setUpCd(userCodeVO.getUpCd());
			code.setRegId(userCodeVO.getRegId());
			code.setRegDt(userCodeVO.getRegDt());
			
			int sortNo = userCodeDAO.getCodeMaxSortNo(code);
			code.setSortNo(sortNo);
			
			//동일한 코드 또는 이름 있는지 확인. 존재시 오류 
			if (userCodeDAO.getCodeCountByCode(code) > 0 ) {
				return "exists code !!!"; 
			}
			else if (userCodeDAO.getCodeCountByCodeNm(code) > 0 ) {
				return "exists code Name !!!"; 
			}
			else {
				if(userCodeDAO.insertUserCodeInfo(code) < 0) {
					return "insert error !!!";
				}else {
					result = insCd;
				}
			}
		}
		return result;
	}

	@Override
	public int updateUserCodeInfo(UserCodeVO userCodeVO) throws Exception {
		return userCodeDAO.updateUserCodeInfo(userCodeVO);		
	}
 
	@Override
	public int updateUserCodeSortNo(UserCodeVO userCodeVO) throws Exception {
		return userCodeDAO.updateUserCodeSortNo(userCodeVO);		
	}
	
	@Override
	public int deleteUserCodeInfo(UserCodeVO userCodeVO) throws Exception {
		return userCodeDAO.deleteUserCodeInfo(userCodeVO);
	}
	
	/**************************** 발송키 관리 ******************************/
	
	@Override
	public List<SenderKeyVO> getSenderKeyList(SenderKeyVO senderKeyVO) throws Exception {
		return userCodeDAO.getSenderKeyList(senderKeyVO);
	}
	
	@Override
	public SenderKeyVO getSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception {
		return userCodeDAO.getSenderKeyInfo(senderKeyVO);
	}
	
	@Override
	public int getSenderKeyExist(SenderKeyVO senderKeyVO) throws Exception {
		return userCodeDAO.getSenderKeyExist(senderKeyVO);
	}
	
	@Override
	public int insertSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception {
		int result = -1;
		if(userCodeDAO.insertSenderKeyInfo(senderKeyVO) > 0 ) {
			result = userCodeDAO.getSenderKeyId();
		}
		
		return result;
	}

	@Override
	public int updateSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception {
		return userCodeDAO.updateSenderKeyInfo(senderKeyVO);	
	}
	
	@Override
	public int deleteSenderKeyInfo(SenderKeyVO senderKeyVO) throws Exception {
		return userCodeDAO.deleteSenderKeyInfo(senderKeyVO);
	}
}
