package common.web.utils;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import common.security.AuthenticationUser;
import common.service.utils.AbstractHelperClass;

public class SessionHelper extends AbstractHelperClass {
	
	//由AbstractHelperClass提供的静态类方法支持函数，必须放在子类中
	protected final static String getStaticClassName(){
				return new Object() {
					//静态方法中获取当前类名
					public String getClassName() {
						String className = this.getClass().getName();
						return className.substring(0, className.lastIndexOf('$'));
					}
				}.getClassName();
			}
			
	
	protected final static Logger logger = LoggerFactory
					.getLogger(getStaticClassName());
	//------------------static 方法模板定义结束---------------------	
	
	
	private final static String springSecurityContextName = "SPRING_SECURITY_CONTEXT";
	
	public final static String SESSION_LOG_ID = "SESSION_LOG_ID";
	

	
	public static String getAuthenticationAttributeName(){
		return springSecurityContextName;
	}
	
	public static Object getSpringSecurityContext(HttpSession session){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		return session.getAttribute(springSecurityContextName);
	}
	
	/*
	public boolean isLogined(){
		return getSpringSecurityContext()!=null;
	}
	*/
	
	public static boolean isAuthenticated(){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext==null){
			return false;
		}
		Authentication authentication = securityContext.getAuthentication();
		if (authentication==null){
			return false;
		}
		boolean isAuthenticated = !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
		return isAuthenticated;
	}
	
	public static AuthenticationUser getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken || authentication==null){
			logger.error("用户尚未登陆，无法执行getAuthenticatedUser");
			return null;
		}
		
		AuthenticationUser user = null;
		Object principal = authentication.getPrincipal();
		if (principal instanceof AuthenticationUser){
			user = (AuthenticationUser)principal;
		}else if (principal instanceof UserDetails) {
			logger.warn("当前注册用户并不是AuthenticationUser实例，准备执行UserDetail转换");
			user = new AuthenticationUser();  
			user.cloneUserDetails((UserDetails)principal);
		}else{
			
			System.out.println(principal.getClass().getName());
			logger.error("发现未知的AuthenticationUser类型，需要处理");
		}	
		return user;
	}
	
	public static Long getSessionLogId(HttpSession session){
		return (Long) session.getAttribute(SESSION_LOG_ID);
	}
}
