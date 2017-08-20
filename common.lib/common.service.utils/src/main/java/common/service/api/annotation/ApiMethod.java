package common.service.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethod {

	/**
	 * <p>方法描述:接口名称</p>
	 * @return String
	 */
	String value();
	
	/**
	 * <p>方法描述:分组</p>
	 * @return String
	 */
	String group() default "";
	
	/**
	 * <p>方法描述:排序</p>
	 * @return int
	 */
	int order () default 0;
	
	/**
	 * <p>方法描述:参数列表</p>
	 * @return InterfaceParam[]
	 */
	ApiMethodParam[] params() default {};
}