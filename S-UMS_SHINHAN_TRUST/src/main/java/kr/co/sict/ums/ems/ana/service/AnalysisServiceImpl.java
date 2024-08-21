/**
 * 작성자 : 김상진
 * 작성일시 : 2021.08.02
 * 설명 : 통계분석 서비스 구현
 */
package kr.co.sict.ums.ems.ana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sict.ums.ems.ana.dao.AnalysisDAO;
import kr.co.sict.ums.ems.ana.vo.MailDomainVO;
import kr.co.sict.ums.ems.ana.vo.MailErrorVO;
import kr.co.sict.ums.ems.ana.vo.MailSummVO;
import kr.co.sict.ums.ems.ana.vo.PeriodSummVO;
import kr.co.sict.ums.ems.ana.vo.RespLogVO;
import kr.co.sict.ums.ems.ana.vo.SendLogVO;
import kr.co.sict.ums.ems.cam.vo.CampaignVO;
import kr.co.sict.ums.ems.cam.vo.TaskVO;

@Service
public class AnalysisServiceImpl implements AnalysisService {

	@Autowired
	private AnalysisDAO analysisDAO;
	
	@Override
	public List<TaskVO> getMailList(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailList(taskVO);
	}

	@Override
	public MailSummVO getMailSummResult(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailSummResult(taskVO);
	}

	@Override
	public List<MailErrorVO> getMailSummDetail(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailSummDetail(taskVO);
	}

	@Override
	public TaskVO getMailInfo(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailInfo(taskVO);
	}

	@Override
	public List<SendLogVO> getFailList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getFailList(sendLogVO);
	}

	@Override
	public List<MailErrorVO> getMailErrorList(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailErrorList(taskVO);
	}

	@Override
	public List<MailDomainVO> getMailDomainList(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailDomainList(taskVO);
	}

	@Override
	public List<SendLogVO> getMailSendHourList(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailSendHourList(taskVO);
	}

	@Override
	public SendLogVO getMailSendHourSum(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailSendHourSum(taskVO);
	}

	@Override
	public List<RespLogVO> getMailRespHourList(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailRespHourList(taskVO);
	}

	@Override
	public RespLogVO getMailRespHourSum(TaskVO taskVO) throws Exception {
		return analysisDAO.getMailRespHourSum(taskVO);
	}

	@Override
	public List<SendLogVO> getTaskCustLogList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getTaskCustLogList(sendLogVO);
	}
	
	@Override
	public List<SendLogVO> getCustLogList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getCustLogList(sendLogVO);
	}

	@Override
	public List<TaskVO> getJoinMailList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinMailList(sendLogVO);
	}

	@Override
	public RespLogVO getJoinSendResult(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinSendResult(sendLogVO);
	}
	
	@Override
	public List<MailErrorVO> getJoinErrorList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinErrorList(sendLogVO);
	}
	
	@Override
	public List<TaskVO> getTaskList(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskList(taskVO);
	}
	
	@Override
	public List<TaskVO> getTaskStepList(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskStepList(taskVO);
	}

	@Override
	public TaskVO getTaskInfo(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskInfo(taskVO);
	}

	@Override
	public MailSummVO getTaskSummResult(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskSummResult(taskVO);
	}

	@Override
	public List<MailErrorVO> getTaskSummDetail(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskSummDetail(taskVO);
	}

	@Override
	public List<MailErrorVO> getTaskErrorList(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskErrorList(taskVO);
	}

	@Override
	public List<MailDomainVO> getTaskDomainList(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskDomainList(taskVO);
	}

	@Override
	public List<SendLogVO> getTaskSendHourList(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskSendHourList(taskVO);
	}

	@Override
	public SendLogVO getTaskSendHourSum(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskSendHourSum(taskVO);
	}

	@Override
	public List<RespLogVO> getTaskRespHourList(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskRespHourList(taskVO);
	}

	@Override
	public RespLogVO getTaskRespHourSum(TaskVO taskVO) throws Exception {
		return analysisDAO.getTaskRespHourSum(taskVO);
	}

	@Override
	public CampaignVO getCampaignInfo(CampaignVO campaignVO) throws Exception {
		return analysisDAO.getCampaignInfo(campaignVO);
	}

	@Override
	public List<MailSummVO> getCampMailList(CampaignVO campaignVO) throws Exception {
		return analysisDAO.getCampMailList(campaignVO);
	}

	@Override
	public MailSummVO getCampMailTotal(CampaignVO campaignVO) throws Exception {
		return analysisDAO.getCampMailTotal(campaignVO);
	}

	@Override
	public List<TaskVO> getJoinCampList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinCampList(sendLogVO);
	}

	@Override
	public RespLogVO getJoinSendResultCamp(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinSendResultCamp(sendLogVO);
	}

	@Override
	public List<MailErrorVO> getJoinErrorListCamp(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinErrorListCamp(sendLogVO);
	}

	@Override
	public List<TaskVO> getJoinTaskList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinTaskList(sendLogVO);
	}

	@Override
	public RespLogVO getJoinSendResultTask(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinSendResultTask(sendLogVO);
	}

	@Override
	public List<MailErrorVO> getJoinErrorListTask(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getJoinErrorListTask(sendLogVO);
	}

	@Override
	public List<PeriodSummVO> getPeriodSummMonthList(PeriodSummVO periodSummVO) throws Exception {
		return analysisDAO.getPeriodSummMonthList(periodSummVO);
	}

	@Override
	public List<PeriodSummVO> getPeriodSummWeekList(PeriodSummVO periodSummVO) throws Exception {
		return analysisDAO.getPeriodSummWeekList(periodSummVO);
	}

	@Override
	public List<PeriodSummVO> getPeriodSummDateList(PeriodSummVO periodSummVO) throws Exception {
		return analysisDAO.getPeriodSummDateList(periodSummVO);
	}

	@Override
	public List<PeriodSummVO> getPeriodSummDomainList(PeriodSummVO periodSummVO) throws Exception {
		return analysisDAO.getPeriodSummDomainList(periodSummVO);
	}

	@Override
	public List<PeriodSummVO> getPeriodSummDeptList(PeriodSummVO periodSummVO) throws Exception {
		return analysisDAO.getPeriodSummDeptList(periodSummVO);
	}

	@Override
	public List<PeriodSummVO> getPeriodSummUserList(PeriodSummVO periodSummVO) throws Exception {
		return analysisDAO.getPeriodSummUserList(periodSummVO);
	}

	@Override
	public List<PeriodSummVO> getPeriodSummCampList(PeriodSummVO periodSummVO) throws Exception {
		return analysisDAO.getPeriodSummCampList(periodSummVO);
	}


	@Override
	public List<SendLogVO> getSendLogList(SendLogVO sendLogVO) throws Exception {
		if ("20".equals(sendLogVO.getSearchServiceGb())) {
			return analysisDAO.getSendLogListRns(sendLogVO);
		} else {
			return analysisDAO.getSendLogListEms(sendLogVO);
		}
	}
	
	@Override
	public List<SendLogVO> getSendLogListExcel(SendLogVO sendLogVO) throws Exception {
		if ("10".equals(sendLogVO.getSearchServiceGb())) {
			return analysisDAO.getSendLogListEmsExcel(sendLogVO);
		} else if ("20".equals(sendLogVO.getSearchServiceGb())) {
			return analysisDAO.getSendLogListRnsExcel(sendLogVO);
		} else {
			return analysisDAO.getSendLogListExcel(sendLogVO);
		}
	}
	
	@Override
	public List<SendLogVO> getCampMailSendList(SendLogVO sendLogVO) throws Exception {
		if ("20".equals(sendLogVO.getSearchServiceGb())) {
			return analysisDAO.getCampMailSendListRns(sendLogVO);
		} else {
			return analysisDAO.getCampMailSendListEms(sendLogVO);
		}
	}
	
	@Override
	public List<SendLogVO> getCampMailEmsList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getCampMailEmsList(sendLogVO);
	}
	
	@Override
	public SendLogVO getCampMailEms(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getCampMailEms(sendLogVO);
	}
		
	@Override
	public List<SendLogVO> getCampMailRnsList(SendLogVO sendLogVO) throws Exception {
		return analysisDAO.getCampMailRnsList(sendLogVO);
	}	

}
