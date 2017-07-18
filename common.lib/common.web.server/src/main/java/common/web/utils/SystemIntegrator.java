package common.web.utils;

public interface SystemIntegrator {
	/**
	 * 当用户登录认证通过时进行的集成操作
	 */
	void onLogin();
	
	/**
	 * 当用户注销登陆后进行的集成操作
	 */
	void onLogoff();
	
	/**
	 * 每次收到用户请求是需要执行的集成操作
	 * 比如activiti5设置当前授权用户id：IdentityService().setAuthenticatedUserId(...)
	 */
	void onServletRequest();
	
	
}
