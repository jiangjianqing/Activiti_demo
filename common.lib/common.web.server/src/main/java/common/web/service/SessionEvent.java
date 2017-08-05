package common.web.service;

import javax.servlet.http.HttpSession;

public interface SessionEvent {
	/**
	 * Session创建，此时用户尚未登陆
	 */
	void onCreate(HttpSession session);
	/**
	 * 当用户登录认证通过时进行的集成操作
	 */
	void onLogin(HttpSession session);
	
	/**
	 * 当用户注销登陆后进行的集成操作
	 */
	void onLogout(HttpSession session);
}
