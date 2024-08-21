/**
 * 작성자 : 김준희
 * 작성일시 : 2022.11.14
 * 설명 : 다중접속 제어 위한 세션관리
 */
package kr.co.sict.util;

import java.util.concurrent.ConcurrentHashMap;

import java.util.Hashtable;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;


/*
리스너 등록방법
 1. @WebListener 어노테이션 사용
 2. web-app xml설정 추가
	<?xml version="1.0" encoding="UTF-8"?>
	<web-app ...>
	     <listener>
	          <listener-class>com.srctree.session.MySessionManager</listener-class>
	     </listener>
	</web-app>
*/
@WebListener
public class SessionManager implements HttpSessionListener {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	public static int SESSION_TIME = 30*60;//30분
	 
	private static SessionManager sessionManager;
	
	public static SessionManager instance() {
		if (sessionManager == null) {
			synchronized (SessionManager.class) {
				if (sessionManager == null) {
					sessionManager = new SessionManager();
				}
			}
		}
		return sessionManager;
	}
  
	private Hashtable<String, HttpSession> useridHash;//유저 아이디 : 세션
 
	public SessionManager() {
		useridHash = new Hashtable<String, HttpSession>();
	}
 
	public void sessionCreated(HttpSessionEvent se)  {
		addSession(se.getSession());
		logger.debug("Created SessionID = " + se.getSession().getId().toString());		
	}
 
	public void sessionDestroyed(HttpSessionEvent se)  {
		removeSession(se.getSession());
		logger.debug("Destroyed SessionID = " + se.getSession().getId().toString()); 
	}
 
	//세션 추가
	void addSession(HttpSession session) {
	}
 
	//세션 삭제
	void removeSession(HttpSession session) {
		//useridHash 체크 후 제거
		String idKey = (String)session.getAttribute("idKey");
		if(idKey != null && useridHash.containsKey(idKey)) {
			useridHash.remove(idKey);
			logger.debug("Remove SessionID = " + idKey + "[count: " + useridHash.size() +"]" );
		}
	}
 
	//촏 개수 리턴
	int getCount() {
		return useridHash.size();
	}
 
	//중복로그인 처리
	public void loginProcess(String userid, HttpSession session) {
		//해당 유저아이디의 세션이 이미 존재한다면 이전 세션은 종료처리한다.
		try {
			if(useridHash.containsKey(userid)) {
				HttpSession prvSession = useridHash.get(userid);
				String idKey = (String)prvSession.getAttribute("idKey");
				//현재 정상적으로 로그인이 된 상태라면
				if(idKey != null && idKey.length() > 0) {				
					prvSession.invalidate();
					logger.debug("loginProcess() : 기존세션 종료처리 " );
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		session.setMaxInactiveInterval(SESSION_TIME);
		session.setAttribute("idKey", userid);
 
		useridHash.put(userid, session);
		logger.debug("loginProcess() = " + userid + "[getId: " +  session.getId()+"]"  + "[count: " +  useridHash.size()+"]");
	}
}
