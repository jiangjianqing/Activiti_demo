package common.web.service;

/**
 * 用于识别用于与其他系统集成的类
 * @author jjq
 *
 */
public interface SystemIntegrator extends SessionEvent {
	
	/**
	 * 每次收到用户请求是需要执行的集成操作
	 * 比如activiti5设置当前授权用户id：IdentityService().setAuthenticatedUserId(...)
	 */
	void onServletRequest();
	
	
}
