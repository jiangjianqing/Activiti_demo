package my.activiti.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import common.model.User;


/**
 * 
 * @author cz_jjq
 *
 */
//@Scope("singleton")
@ControllerAdvice //(basePackages = "com.github") //basePackages用于指定对哪些包里的Controller起作用。
public class ControllerAdviceTest {
	//添加返回的对象到ModelMap中，value用来指定对象名称（没有value则会使用返回对象的小写名称，如下面返回User，则变量名=user），用到的场景非常少
	@ModelAttribute(value="controllerAdviceUser")  
    public User newUser() {  
        System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前把返回值放入Model");  
        User user= new User();
        user.setUserName("这是ControllerAdviceTest添加的User");
        return user;
    }  
  
	//用到的场景非常少,目前没有测试过
    @InitBinder  
    public void initBinder(WebDataBinder binder) {  
        //System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器");  
    }  
  
    //可以把异常处理器应用到所有控制器，而不是@Controller注解的单个控制器【这个非常重要】
    @ExceptionHandler(UnauthenticatedException.class)  
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)  
    public String processUnauthenticatedException(NativeWebRequest request, UnauthenticatedException e) {  
        System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出UnauthenticatedException异常时执行");  
        return "viewName"; //返回一个逻辑视图名  
    }  
}
