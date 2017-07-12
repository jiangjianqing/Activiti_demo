package common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;

/*
 * 为其他类提供基础功能服务，建议其他业务类都从该类继承
 */
public abstract class AbstractHelperClass {
	/*
	 * 处理多国语言
	 *
	 *example:
	 *messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found")
	 */
	protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	/*
	 * 重要：调试日志记录，所有从本类继承的Controller都可以直接使用
	 */
	protected final Logger logger = LoggerFactory
			.getLogger(getClass());
}
