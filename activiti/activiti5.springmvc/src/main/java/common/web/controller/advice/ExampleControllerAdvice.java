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
import common.web.exception.BindingResultErrorException;
import common.web.utils.AbstractHelperClass;
import common.web.utils.SystemIntegrationHelper;

@ControllerAdvice //(basePackages = "com.github") //basePackages用于指定对哪些包里的Controller起作用。
public class ExampleControllerAdvice extends AbstractHelperClass {

	//用于自动绑定前台请求参数到Model中。
    @InitBinder  
    public void initBinder(WebDataBinder binder) {  
    	
    	//// 此处仅演示忽略request中的参数id
    	//binder.setDisallowedFields("id");
    	//用到的场景非常少,目前没有测试过
    	//System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器"); 
    }
    
  //添加返回的对象到ModelMap中，value用来指定对象名称（没有value则会使用返回对象的小写名称，如下面返回User，则变量名=user），用到的场景非常少
  	@ModelAttribute(value="controllerAdviceUser")  
    public AuthenticationUser newUser() {  
          System.out.println("=======@ControllerAdvice.@ModelAttribute=====应用到所有@RequestMapping注解方法，在其执行之前把返回值放入Model");  
          AuthenticationUser user= new AuthenticationUser();
          user.setUserName("这是ControllerAdviceTest添加的User");
          return user;
      }  
}

