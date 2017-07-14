package common.db.base.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import common.db.base.validator.annotation.Forbidden;

/**
 * 屏蔽关键词验证
 * @author cz_jjq
 *
 */
public class ForbiddenValidator implements ConstraintValidator<Forbidden, String> {

	private String[] forbiddenWords = {"admin"};  
	
	@Override
	public void initialize(Forbidden arg0) {
		//初始化，得到注解数据  

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext arg1) {
		if(value==null || value.isEmpty()) {  
            return true;  
        }  
  
        for(String word : forbiddenWords) { 
            if(value.contains(word)) {  
                return false;//验证失败  
            }  
        }  
        return true;  
	}

}
