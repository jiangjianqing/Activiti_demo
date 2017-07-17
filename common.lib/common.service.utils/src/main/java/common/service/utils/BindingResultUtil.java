package common.service.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import common.service.exception.BindingResultErrorException;

public class BindingResultUtil {
	private static final Logger log = LoggerFactory.getLogger(BindingResultUtil.class);

	public static void checkValidateResult(BindingResult result) throws BindingResultErrorException{
		checkValidateResult(result,false);
	}	

	public static void checkValidateResult(BindingResult result,boolean isDebug) throws BindingResultErrorException{
		if(result.hasErrors()){
			List<ObjectError> list = result.getAllErrors();
			String jsonString=JsonUtil.toJson(list);
			if(isDebug){
				for(ObjectError objectError:list){
		            log.warn(objectError.getDefaultMessage());
		            log.warn(objectError.toString());
		        }
			}
			throw new BindingResultErrorException(jsonString);

        }
	}	

}

