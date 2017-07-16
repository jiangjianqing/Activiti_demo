package common.web.view.controller;

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
import common.web.exception.BindingResultErrorException;
import common.web.model.AuthenticationUser;
import common.web.utils.AbstractHelperClass;
import common.web.utils.SystemIntegrationHelper;

@ControllerAdvice //(basePackages = "com.github") //basePackages用于指定对哪些包里的Controller起作用。
public class WebControllerAdvice extends AbstractHelperClass {

	//用于自动绑定前台请求参数到Model中。
    @InitBinder  
    public void initBinder(WebDataBinder binder) {  
    	
    	//// 此处仅演示忽略request中的参数id
    	//binder.setDisallowedFields("id");
    	//用到的场景非常少,目前没有测试过
    	//System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器"); 
    }
    
    /**
     * 利用ModelAtribute的特性完成SystemIntegration whenRequest的动作，这样性能比较高
     * 原因是利用RequestListener时出现了4次调用，影响性能,可能是使用了模板后的后遗症
     */
  	@ModelAttribute  
    public void onRequest() {  
          SystemIntegrationHelper.whenServletRequest();

    }
    
  //添加返回的对象到ModelMap中，value用来指定对象名称（没有value则会使用返回对象的小写名称，如下面返回User，则变量名=user），用到的场景非常少
  	@ModelAttribute(value="controllerAdviceUser")  
    public AuthenticationUser newUser() {  
          System.out.println("=======@ControllerAdvice.@ModelAttribute=====应用到所有@RequestMapping注解方法，在其执行之前把返回值放入Model");  
          AuthenticationUser user= new AuthenticationUser();
          user.setUserName("这是ControllerAdviceTest添加的User");
          return user;
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
    @ResponseBody  //重要：如果不使用@ResponseBody标注，返回一个逻辑视图名  
	public String processEntityNotFoundException(NativeWebRequest request, EntityNotFoundException ex){
		//System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出UnauthenticatedException异常时执行");  
    	//return "viewName"; 
    	
    	return ex.getMessage();
	}

    @ExceptionHandler(OutOfPageRangeException.class)  
    @ResponseStatus(value = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)  
    @ResponseBody
    public String processOutOfPageRangeException(OutOfPageRangeException ex){
    	return "page值异常";

    }

    @ExceptionHandler(BindingResultErrorException.class)  
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)  
    @ResponseBody
    public String processValidateFailedException(BindingResultErrorException ex){
    	return ex.getMessage();
    }

    @ExceptionHandler(DaoException.class)  
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  
    @ResponseBody
    public String processDaoException(DaoException ex){

    	logger.error("encouter DaoException："+ex.getExceptionName());
    	logger.error(ex.getMessage());
    	return ex.getExceptionName()+":"+"DaoException异常";
    }

    @ExceptionHandler(NoFieldChangedException.class)  
    @ResponseStatus(value = HttpStatus.OK)  
    @ResponseBody
    public String processNoFieldChangedException(NoFieldChangedException ex){
    	return ex.getEntityName()+ex.getMessage();
    }

    public String buildResponseString(){

    	String ret="";
    	return ret;
    }
}

