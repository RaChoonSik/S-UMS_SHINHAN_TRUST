/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.23
 * 설명 : 메일발송결재 데이터 처리
 */
package kr.co.sict.ums.ems.apr.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.ems.apr.vo.SecuApprovalLineVO;
import kr.co.sict.ums.ems.cam.vo.AttachVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;

@Repository
public class SecuApprovalLineDAO implements SecuApprovalLineMapper {
	@Autowired
	private SqlSession sqlSessionEms;

	@Override
	public List<SecuApprovalLineVO> getApprovalLineList(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getApprovalLineList(approvalLineVO);
	}

	@Override
	public List<SecuApprovalLineVO> getApprovalLineEmsList(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getApprovalLineEmsList(approvalLineVO);
	}
	
	@Override
	public List<SecuApprovalLineVO> getApprovalLineRnsList(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getApprovalLineRnsList(approvalLineVO);
	}
	
	
	@Override
	public SecuApprovalLineVO getMailInfo(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getMailInfo(approvalLineVO);
	}

	@Override
	public List<AttachVO> getAttachList(int taskNo) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getAttachList(taskNo);
	}

	@Override
	public List<SecuApprovalLineVO> getMailApprLineList(int taskNo) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getMailApprLineList(taskNo);
	}

	@Override
	public int updateMailAprStep(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).updateMailAprStep(approvalLineVO);
	}

	@Override
	public int updateMailAprStepNext(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).updateMailAprStepNext(approvalLineVO);
	}

	@Override
	public int updateTaskStatusAdmit(TaskVO taskVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).updateTaskStatusAdmit(taskVO);
	}

	@Override
	public int updateSubTaskStatusAdmit(TaskVO taskVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).updateSubTaskStatusAdmit(taskVO);
	}

	@Override
	public int getApprCount(String userId) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getApprCount(userId);
	}

	@Override
	public String getRejectNm(String rejectCd) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).getRejectNm(rejectCd);
	}

	@Override
	public List<SecuApprovalLineVO> nextMailAprStep(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).nextMailAprStep(approvalLineVO);
	}
	
	@Override
	public List<SecuApprovalLineVO> nowMailAprStep(SecuApprovalLineVO approvalLineVO) throws Exception {
		return sqlSessionEms.getMapper(SecuApprovalLineMapper.class).nowMailAprStep(approvalLineVO);
	}
	
}
