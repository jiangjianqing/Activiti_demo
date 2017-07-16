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
import common.web.exception.BindingResultErrorException;
import common.web.model.AuthenticationUser;
import common.web.utils.AbstractHelperClass;
import common.web.utils.SystemIntegrationHelper;

@ControllerAdvice //(basePackages = "com.github") //basePackages用于指定对哪些包里的Controller起作用。
public class IntegrationControllerAdvice extends AbstractHelperClass {

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
    
}

