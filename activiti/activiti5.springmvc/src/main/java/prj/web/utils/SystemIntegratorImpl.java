package prj.web.utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.h2.util.New;
import org.mvel2.ast.NewObjectNode.NewObjectArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import common.security.AuthenticationUser;
import common.service.utils.AbstractHelperClass;
import common.service.utils.SpringContextHolder;
import common.web.service.SystemIntegrator;
import common.web.utils.*;

public class SystemIntegratorImpl  extends AbstractHelperClass 
		implements SystemIntegrator{

	//------------------static 方法模板定义结束---------------------	
	
	private static IdentityService getIdentityService(){
		return SpringContextHolder.getBean("identityService");
	};

	/**
	 * 与activiti集成，主要是设置当前线程上的注册用户，没错，就是当前线程：
	 * 在Activiti中,IdentityService中提供了SetAuthenticatedUserId方法用于将用户ID设置到当前的线程中,
	 * 最终调用ThreadLocal的set方法.具体的代码如下:
	 * identityService.setAuthenticatedUserId(userId);
	 */
	private void integrateActiviti(){
		
		if (SessionHelper.isAuthenticated()) {
			AuthenticationUser user = SessionHelper.getAuthenticatedUser();
			logger.debug("用户已经注册，执行activiti integrate 动作！");
			getIdentityService().setAuthenticatedUserId(user.getId().toString());	
		}	
	}
	
	public void onLogin(HttpSession session){
		integrateActiviti();
	}
	
	public void onLogout(HttpSession session){
		
	}
	
	public void onServletRequest(){
		integrateActiviti();
	}
}
