/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 시스템 데이터 처리==>시스템 로그 데이터 처리
 * 수정자 : 김준희
 * 수정일시 : 2021.08.10
 * 수정내역 : 패키지명수정 및 sys관련 분리에 의한 소스 변경  kr.co.sict.ums.sys.dao ==> kr.co.sict.ums.sys.log.dao
 *                계정관리외의 함수제거
 */
package kr.co.sict.ums.sys.log.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sict.ums.sys.log.vo.ActionLogVO;
import kr.co.sict.ums.sys.log.vo.LoginHistVO;

@Repository
public class SystemLogDAO implements SystemLogMapper {
	@Autowired
	private SqlSession sqlSessionEms;
	
	@Override
	public List<LoginHistVO> getLoginHistList(LoginHistVO loginHistVO) throws Exception {
		return sqlSessionEms.getMapper(SystemLogMapper.class).getLoginHistList(loginHistVO);
	}
	
	@Override
	public List<ActionLogVO> getActionLogList(ActionLogVO actionLogVO) throws Exception {
		return sqlSessionEms.getMapper(SystemLogMapper.class).getActionLogList(actionLogVO);
	}	

	@Override
	public int insertActionLog(ActionLogVO actionLogVO) throws Exception {
		return sqlSessionEms.getMapper(SystemLogMapper.class).insertActionLog(actionLogVO);
	}
}
