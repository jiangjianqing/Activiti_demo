package common.service.aop.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用于Aspect标识的注解，必须用在方法上
 * 例如
 * 	@SystemControllerLog(description = "删除用户") 
 * @author jjq
 *
 */
@Retention(RUNTIME)
@Target({ METHOD, PARAMETER })
@Documented
public @interface SystemControllerLog {
	String description()  default "";    
}
