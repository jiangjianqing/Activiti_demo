package common.web.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import common.db.base.exception.DaoException;
import common.db.model.log.SessionLog;
import common.db.repository.log.SessionLogDao;
import common.service.utils.AbstractHelperClass;
import common.service.utils.SpringContextHolder;
import common.web.service.SessionEvent;
import common.web.service.SessionLogger;
import common.web.utils.SessionHelper;

public class SessionLoggerImpl extends AbstractHelperClass implements SessionLogger {

	private final String sessionAttrName = "sessionLogInfo";
	
	private SessionLogDao sessionLogDao;


	public SessionLogDao getSessionLogDao() {
		return sessionLogDao;
	}

	public void setSessionLogDao(SessionLogDao sessionLogDao) {
		this.sessionLogDao = sessionLogDao;
	}

	@Override
	public void onLogin(HttpSession session) {
		if (sessionLogDao == null) {
			logger.error("没有发现sessionLogDao,无法记录会话日志");
			return;
		}
		//20170806 session创建时就记录的话，会导致注销的时候多记录一次的bug，暂时关闭
		/*
		SessionLog info = (SessionLog)session.getAttribute(sessionAttrName);
		if (info == null){
			info = createNewSessionLog(session);
			session.setAttribute(sessionAttrName, info);
		}*/
		SessionLog info = createNewSessionLog(session);
		info.setLoginTime(new Date());
		info.setUserId(SessionHelper.getAuthenticatedUser().getId());
		try {
			//sessionLogDao.update(info);
			sessionLogDao.insert(info);
			//注册SessionLogId，便于其他日志使用
			session.setAttribute(sessionAttrName, info);
			session.setAttribute(SessionHelper.SESSION_LOG_ID, info.getId());
			logger.warn(String.format("创建sessionlog,id=%d", info.getId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onLogout(HttpSession session) {
		if (sessionLogDao == null) {
			logger.error("没有发现sessionLogDao,无法记录会话日志");
			return;
		}

		SessionLog info = (SessionLog) session.getAttribute(sessionAttrName);
		try {
			info.setLogoutTime(new Date());
			sessionLogDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onCreate(HttpSession session) {
		//20170806 session创建时就记录的话，会导致注销的时候多记录一次的bug，暂时关闭
		/*
		SessionLog info = createNewSessionLog(session);
		try {
			sessionLogDao.create(info);
			session.setAttribute(sessionAttrName, info);
			//注册SessionLogId，便于其他日志使用
			session.setAttribute(SessionHelper.SESSION_LOG_ID, info.getId());
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	/**
	 * 根据HttpSession创建一个新的SessionLog对象
	 * @param session
	 * @return
	 */
	private SessionLog createNewSessionLog(HttpSession session){
		SessionLog info = new SessionLog();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		info.setIpAddr(request.getRemoteAddr());
		info.setHostName(request.getRemoteHost());
		info.setCreateTime(new Date(session.getCreationTime()));
		info.setSessionId(session.getId());
		return info;
	}

}
