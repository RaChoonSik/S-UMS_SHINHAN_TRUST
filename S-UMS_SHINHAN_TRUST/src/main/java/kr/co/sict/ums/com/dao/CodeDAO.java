/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 코드 데이터 처리
 */
package kr.co.sict.ums.com.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.com.vo.CodeGroupVO;
import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.push.cam.vo.PushCampaignVO;
import kr.co.sict.ums.sms.cam.vo.SmsCampaignVO;

@Repository
public class CodeDAO implements CodeMapper {
	@Autowired
	SqlSession sqlSessionEms;

	@Override
	public List<CodeVO> getCodeList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCodeList(codeVO);
	}
	
	@Override
	public CodeVO getCodeInfo(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCodeInfo(codeVO);
	}
	
	@Override
	public CodeGroupVO getCodeGrpInfo(CodeGroupVO codeGroupVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCodeGrpInfo(codeGroupVO);
	}

	@Override
	public List<CodeVO> getCodeGrpList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCodeGrpList(codeVO);
	}
	
	@Override
	public List<CodeVO> getCodeListByUpCd(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCodeListByUpCd(codeVO);
	}
	
	@Override
	public List<CodeVO> getTimezoneList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getTimezoneList(codeVO);
	}

	@Override
	public List<CodeVO> getDeptList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getDeptList(codeVO);
	}
	
	@Override
	public List<CodeVO> getUserList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getUserList(codeVO);
	}
	
	@Override
	public List<CodeVO> getAuthGroupList() throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getAuthGroupList();
	}

	@Override
	public List<CodeVO> getProgramList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getProgramList(codeVO);
	}

	@Override
	public List<CampaignVO> getCampaignList(CampaignVO campaignVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCampaignList(campaignVO);
	}

	@Override
	public List<PushCampaignVO> getCampaignPushList(PushCampaignVO pushCampaignVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCampaignPushList(pushCampaignVO);
	}

	@Override
	public List<SmsCampaignVO> getCampaignSmsList(SmsCampaignVO smsCampaignVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getCampaignSmsList(smsCampaignVO);
	}

	@Override
	public List<CodeVO> getWorkStatusList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getWorkStatusList(codeVO);
	}

	@Override
	public List<CodeVO> getRcodeList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getRcodeList(codeVO);
	}

	@Override
	public List<CodeVO> getKakaoTemplateList(CodeVO codeVO) throws Exception {
		return sqlSessionEms.getMapper(CodeMapper.class).getKakaoTemplateList(codeVO);
	}
}
