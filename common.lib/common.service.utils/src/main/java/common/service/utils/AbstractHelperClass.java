package common.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 为其他类提供基础功能服务，建议其他业务类都从该类继承
 */
public abstract class AbstractHelperClass {
	
	/*特别注意：如果要生成一个静态Helper类，则需要手动加入如下代码才能获得logger的支持
	 * 因为static类是无法获得this的
	 * 
	
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
			
			
	*/
	
	/*
	 * 处理多国语言
	 *
	 *example:
	 *messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found")
	 */
	protected final static MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	/*
	 * 重要：调试日志记录，所有从本类继承的Controller都可以直接使用
	 */
	protected final Logger logger = LoggerFactory
			.getLogger(getClass());
	
	/*
	 * 重要：调试日志记录，所有从本类继承的Controller都可以直接使用
	 * 
	 * 通过匿名类的方法可以动态获取当前类的类名
	 */
	/*
	protected final static Logger logger = LoggerFactory
			.getLogger(new Object() {
				//静态方法中获取当前类名
				public String getClassName() {
					String className = this.getClass().getName();
					return className.substring(0, className.lastIndexOf('$'));
				}
			}.getClassName()
		);
	*/
	
	/**
	 * 获取当前Conotrller的RequestMapping中第一个value值（url），目前主要用于redirect
	 * @return
	 * @throws Exception
	 */
	protected String getDefaultRequestMappingUrl() throws Exception {
		RequestMapping mapping = this.getClass().getAnnotation(RequestMapping.class);
		if(mapping == null) {
			throw new Exception(this.getClass().getName()+"没有增加RequestMapping 注解.");
		}
		if (mapping.value().length==0) {
			throw new Exception(this.getClass().getName()+" RequestMapping 注解中尚未加入value");
		}
		return mapping.value()[0];
	}
}
