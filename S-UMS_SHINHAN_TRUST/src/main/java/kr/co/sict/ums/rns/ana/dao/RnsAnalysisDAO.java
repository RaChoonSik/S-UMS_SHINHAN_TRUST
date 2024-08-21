/**
 * 작성자 : 김준희
 * 작성일시 : 2021.09.22
 * 설명 : RNS 통계 데이터 처리
 */
package kr.co.sict.ums.rns.ana.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.com.vo.CodeVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaMonthVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaServiceVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaDomainVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaMailSendResultVO;
import kr.co.sict.ums.rns.ana.vo.RnsAnaDetailLogVO;

@Repository
public class RnsAnalysisDAO implements RnsAnalysisMapper {
	@Autowired
	private SqlSession sqlSessionRns;

	@Override
	public List<RnsAnaMonthVO> getMonthList(RnsAnaMonthVO rnsAnaMonthVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getMonthList(rnsAnaMonthVO);
	}

	@Override
	public List<RnsAnaServiceVO> getServiceList(RnsAnaServiceVO rnsAnaServiceVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getServiceList(rnsAnaServiceVO);
	}

	@Override
	public List<RnsAnaDomainVO> getDomainList(RnsAnaDomainVO rnsAnaDomainVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getDomainList(rnsAnaDomainVO);
	}

	@Override
	public List<RnsAnaDetailLogVO> getReceiverList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getReceiverList(rnsAnaDetailLogVO);
	}

	@Override
	public List<RnsAnaDetailLogVO> getMailList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getMailList(rnsAnaDetailLogVO);
	}

	@Override
	public List<RnsAnaDetailLogVO> getDetailLogList(RnsAnaDetailLogVO rnsAnaDetailLogVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getDetailLogList(rnsAnaDetailLogVO);
	}
	

	@Override
	public List<RnsAnaMailSendResultVO> getMailSendResultList(RnsAnaMailSendResultVO rnsAnaMailSendResultVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getMailSendResultList(rnsAnaMailSendResultVO);
	}	
	
	@Override
	public List<CodeVO> getServiceCodeList() throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getServiceCodeList();
	}
	
	@Override
	public List<String> getSenderList() throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getSenderList();
	}

	@Override
	public List<CodeVO> getRcodeList(CodeVO codeVO) throws Exception {
		return sqlSessionRns.getMapper(RnsAnalysisMapper.class).getRcodeList(codeVO);
	}
	
 
}
