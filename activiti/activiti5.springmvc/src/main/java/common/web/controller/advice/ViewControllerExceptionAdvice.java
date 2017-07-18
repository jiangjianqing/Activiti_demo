package common.web.controller.advice;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import common.db.base.exception.DaoException;
import common.db.base.exception.NoFieldChangedException;
import common.db.base.exception.OutOfPageRangeException;
import common.security.AuthenticationUser;
import common.service.exception.BindingResultErrorException;
import common.service.utils.AbstractHelperClass;
import common.web.controller.AbstractRestController;
import common.web.controller.AbstractViewController;

/**
 * 处理所有视图controller异常
 * 注意：
 * 	以下@ExceptionHandler方法处理都禁止标注@ResponseBody  //重要：如果不使用@ResponseBody标注，将返回一个逻辑视图名  
 * @author jjq
 *
 *basePackages用于指定对哪些包里的Controller起作用,无法使用通配符
 *所以使用assignableTypes来代替（也可以使用annotations 实现同样功能）
 */
@ControllerAdvice(assignableTypes = {AbstractViewController.class}) 
public class ViewControllerExceptionAdvice extends AbstractHelperClass {

	//用于自动绑定前台请求参数到Model中。
    @InitBinder  
    public void initBinder(WebDataBinder binder) {  
    	
    	//// 此处仅演示忽略request中的参数id
    	//binder.setDisallowedFields("id");
    	//用到的场景非常少,目前没有测试过
    	//System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器"); 
    }
    
    /**
     * 使用jpa中的异常对象进行数据反馈
     * @param ex
     * @return
     * 
     * //可以把异常处理器应用到所有控制器，而不是@Controller注解的单个控制器【这个非常重要】
     */
    @ExceptionHandler(EntityNotFoundException.class)  
    @ResponseStatus(value = HttpStatus.NOT_FOUND)  
	public String processEntityNotFoundException(NativeWebRequest request, EntityNotFoundException ex){
		//System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出UnauthenticatedException异常时执行");  
    	//return "viewName"; 
    	
    	return "error500";
	}

}

