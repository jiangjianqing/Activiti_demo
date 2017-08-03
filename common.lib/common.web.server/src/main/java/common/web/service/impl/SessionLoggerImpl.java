package common.web.service;

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
import common.db.repository.jpa.log.SessionLogDao;
import common.service.utils.AbstractHelperClass;
import common.service.utils.SpringContextHolder;
import common.web.utils.SessionHelper;

public class SessionLogger extends AbstractHelperClass {

	// 由AbstractHelperClass提供的静态类方法支持函数，必须放在子类中
	protected final static String getStaticClassName() {
		return new Object() {
			// 静态方法中获取当前类名
			public String getClassName() {
				String className = this.getClass().getName();
				return className.substring(0, className.lastIndexOf('$'));
			}
		}.getClassName();
	}

	protected final static Logger logger = LoggerFactory.getLogger(getStaticClassName());
	// ------------------static 方法模板定义结束---------------------

	private static final String sessionAttrName = "sessionLogInfo";

	private static SessionLogDao getSessionLogDao() {
		return SpringContextHolder.getBean(SessionLogDao.class);
	}

	public static void login(HttpSession session) {
		SessionLogDao sessionLogDao = getSessionLogDao();
		if (sessionLogDao == null) {
			logger.error("没有发现sessionLogDao,无法记录会话日志");
			return;
		}
		SessionLog info = new SessionLog();
		info.setLoginTime(new Date());
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		info.setIpAddr(request.getRemoteAddr());
		info.setHostName(request.getRemoteHost());
		info.setCreateTime(new Date(session.getCreationTime()));
		info.setSessionId(session.getId());
		info.setUserId(SessionHelper.getAuthenticatedUser().getId());
		
		try {
			sessionLogDao.create(info);
			session.setAttribute(sessionAttrName, info);
			logger.warn(String.format("创建sessionlog,id=%d", info.getId()));
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void logout(HttpSession session) {
		SessionLogDao sessionLogDao = getSessionLogDao();
		if (sessionLogDao == null) {
			logger.error("没有发现sessionLogDao,无法记录会话日志");
			return;
		}

		SessionLog info = (SessionLog) session.getAttribute(sessionAttrName);
		try {
			info.setLogoutTime(new Date());
			sessionLogDao.update(info);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
