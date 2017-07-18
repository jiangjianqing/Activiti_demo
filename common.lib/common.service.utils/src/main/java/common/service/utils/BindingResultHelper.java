package common.service.utils;

import java.util.List;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import common.service.exception.BindingResultValidationException;

public class BindingResultHelper extends AbstractHelperClass{
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


	public static void checkValidateResult(BindingResult result) throws BindingResultValidationException{
		if(result.hasErrors()){
			List<ObjectError> list = result.getAllErrors();
			String jsonString=JsonUtil.toJson(list);
			for(ObjectError objectError:list){
	            logger.debug(objectError.getDefaultMessage());
	            logger.debug(objectError.toString());
	        }
			throw new BindingResultValidationException(jsonString);

        }
	}	

}

