/**
 * 작성자 : 이혜민
 * 작성일시 : 2022.06.22
 * 설명 : SMS 템플릿 관리 DAO
 */
package kr.co.sict.ums.sys.seg.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sys.seg.vo.SmsTemplateVO;

@Repository
public class SmsTemplateDAO implements SmsTemplateMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<SmsTemplateVO> getSmsTemplateList(SmsTemplateVO smsTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).getSmsTemplateList(smsTemplateVO);
	}
	
	@Override
	public List<SmsTemplateVO> getSmsTemplateSimpleList() throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).getSmsTemplateSimpleList();
	}


	@Override
	public SmsTemplateVO getSmsTemplateInfo(String tempCd) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).getSmsTemplateInfo(tempCd);
	}

	@Override
	public int insertSmsTemplateInfo(SmsTemplateVO smsTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).insertSmsTemplateInfo(smsTemplateVO);
	}

	@Override
	public List<SmsTemplateVO> checkSmsTemplateCode(String tempCd) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).checkSmsTemplateCode(tempCd);
	}

	@Override
	public int updateSmsTemplateInfo(SmsTemplateVO smsTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).updateSmsTemplateInfo(smsTemplateVO);
	}

	@Override
	public int updateSmsTemplateStatus(SmsTemplateVO smsTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).updateSmsTemplateStatus(smsTemplateVO);
	}

	@Override
	public SmsTemplateVO getSmsTemplate(String tempCd) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).getSmsTemplate(tempCd);
	}
	
	@Override
	public List<SmsTemplateVO> getApiTemplateList(SmsTemplateVO smsTemplateVO) throws Exception {
		return sqlSessionEms.getMapper(SmsTemplateMapper.class).getApiTemplateList(smsTemplateVO);
	}
}
