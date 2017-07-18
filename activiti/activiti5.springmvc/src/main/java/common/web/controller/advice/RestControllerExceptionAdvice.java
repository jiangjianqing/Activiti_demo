package common.web.controller.advice;

import javax.persistence.EntityNotFoundException;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import common.service.utils.AbstractHelperClass;
import common.web.controller.AbstractRestController;
import common.web.model.WrappedResponseBody;

/**
 * 处理所有的Restful接口异常
 * 注意：
 * 	以下@ExceptionHandler方法处理都必须标注@ResponseBody  //重要：如果不使用@ResponseBody标注，将返回一个逻辑视图名 
 * 没有使用@RestControllerAdvice,是为了与spring3.x保持兼容 
 * @author jjq
 *
 *basePackages用于指定对哪些包里的Controller起作用,无法使用通配符
 *所以使用assignableTypes来代替（也可以使用annotations 实现同样功能）
 *
 */
@ControllerAdvice(assignableTypes = {AbstractRestController.class})
public class RestControllerExceptionAdvice extends AbstractHelperClass {

	/**
	 * 用于自动绑定前台请求参数到Model中。
	 * @param binder
	 */
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
    //@ResponseStatus(value = HttpStatus.NOT_FOUND)  
    @ResponseBody  //重要：如果不使用@ResponseBody标注，返回一个逻辑视图名  
	public WrappedResponseBody processEntityNotFoundException(NativeWebRequest request, EntityNotFoundException ex){
    	return new WrappedResponseBody(ex);
	}
    
    /**
     * 重要利用处理优先级机制，其他@ExceptionHandler不处理的异常将由这里处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)  
    @ResponseBody  //重要：如果不使用@ResponseBody标注，返回一个逻辑视图名  
	public WrappedResponseBody processEntityNotFoundException(NativeWebRequest request, Exception ex){
    	logger.debug("processed by  @ExceptionHandler(Exception.class)  ");
    	return new WrappedResponseBody(ex);
	}

    /*
    @ExceptionHandler(OutOfPageRangeException.class)  
    @ResponseStatus(value = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)  
    @ResponseBody
    public String processOutOfPageRangeException(OutOfPageRangeException ex){
    	return "page值异常";

    }

    @ExceptionHandler(BindingResultErrorException.class)  
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)  
    @ResponseBody
    public WrappedResponseBody processValidateFailedException(BindingResultErrorException ex){
    	return new WrappedResponseBody(ex);
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
    */
}

