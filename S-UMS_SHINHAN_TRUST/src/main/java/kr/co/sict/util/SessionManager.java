/**
 * 작성자 : 김준희
 * 작성일시 : 2022.11.14
 * 설명 : 다중접속 제어 위한 세션관리
 */
package kr.co.sict.util;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionManager {
	private static final Log LOG = LogFactory.getLog(SessionManager.class);
	
	private static SessionManager sessionManager = null;
	
	private static ConcurrentHashMap<String, HttpSession> loginUsers; 
	
	private synchronized static SessionManager getInstance() {
		if(sessionManager == null) {
			sessionManager = new SessionManager();
			loginUsers = new ConcurrentHashMap<String, HttpSession>();
		}
		
		return sessionManager;
	}
	
	private ConcurrentHashMap<String, HttpSession> getLoginUsers() {
		return loginUsers;
	}
	
	public static void addUser(HttpSession session) {
		SessionManager sm = getInstance();
		String userId = (String)session.getAttribute("NEO_USER_ID");
		if(userId != null) {
			LOG.debug("--LOGINUSER-- addUser userId : " + userId);
			if(sm.getLoginUsers().containsKey(userId)) {
				LOG.debug("--LOGINUSER-- blockExistSession userId : " + userId);
				HttpSession se = sm.getLoginUsers().get(userId);
				if(se != null) {
					getInstance().getLoginUsers().remove(userId); 
					se.setAttribute("NEO_BLOCKED", true);
					// 
					//session.invalidate();
					session.setAttribute("NEO_MULTILOGIN", true);
					session.setAttribute("NEO_BLOCKEDIP", se.getAttribute("NEO_CONNECTIP"));
				}
			}
			sm.getLoginUsers().put(userId, session);
		}
	}
	
	public static void removeSession(HttpSession session) {
		SessionManager sm = getInstance();
		for (String key : sm.getLoginUsers().keySet()) {
			HttpSession se = sm.getLoginUsers().get(key);
			if(se == session) {
				// 새로운 세션으로 덮어썼다면 없을수도..
				boolean ret = sm.getLoginUsers().remove(key, session);
				LOG.debug("--LOGINUSER-- removeSession userId : " + key + ", remove : " + ret);
				break;
			}
		}
	}
	
	
}
