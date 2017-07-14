package common.db.base.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import common.db.base.validator.ForbiddenValidator;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })  
@Retention(RetentionPolicy.RUNTIME)  
//指定验证器  
@Constraint(validatedBy = ForbiddenValidator.class)  
@Documented  
public @interface Forbidden {
	//默认错误消息  
    String message() default "{forbidden.word}";  
  
    //分组  
    Class<?>[] groups() default { };  
  
    //负载  
    Class<? extends Payload>[] payload() default { };  
  
    //指定多个时使用  
    @Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })  
    @Retention(RetentionPolicy.RUNTIME)  
    @Documented  
    @interface List {  
        Forbidden[] value();  
    }  
}
