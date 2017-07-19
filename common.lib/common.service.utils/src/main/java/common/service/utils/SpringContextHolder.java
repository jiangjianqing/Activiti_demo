package common.service.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.
 * 
 * 使用范例:
 * ApplicationContext cx=SpringContextHolder.getApplicationContext();
 * org.springframework.jdbc.core.JdbcTemplate jdbcTemplate=SpringContextHolder.getBean("jdbcTemplate");
 * 
 * 使用WebApplicationContextUtils也可以实现一样的效果
 * ConfigService configService = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(ConfigService.class);
 * 
 */
public class SpringContextHolder implements ApplicationContextAware {
	
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
		
	private static ApplicationContext applicationContext;
	
	static {
		try {
			//通过servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
			//已经将WebApplicationContext的实例放入ServletContext 中了。
			
			//重要：20170719 这里直接直接使用spring提供的contextLoader来获取applicationContext,所以系统中可以不初始化本类了
			applicationContext = ContextLoader.getCurrentWebApplicationContext();
		} catch (Exception ex) {
			logger.error("无法静态初始化WebApplicationContext:"+ex.getMessage());
		}
	}

	/**
	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext; // NOSONAR
	}

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * 通过代码创建类实例，同时注入spring内容
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static AutowireCapableBeanFactory getAutowireCapableBeanFactory() {
		checkApplicationContext();
		return applicationContext.getAutowireCapableBeanFactory();
	}

	/**
	 * 清除applicationContext静态变量.
	 */
	public static void cleanApplicationContext() {
		applicationContext = null;
	}

	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException(
					"applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}
}
