package common.service.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethodParam {
	/**
	 * <p>方法描述:参数名称</p>
	 * @return String
	 */
	String name();
	
	/**
	 * <p>方法描述:接口说明</p>
	 * @return String
	 */
	String desc();
	
	/**
	 * <p>方法描述:测试参数值</p>
	 * @return String
	 */
	String testValue() default "";
}
