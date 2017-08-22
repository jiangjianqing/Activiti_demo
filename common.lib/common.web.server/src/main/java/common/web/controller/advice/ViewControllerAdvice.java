package common.web.controller.advice;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import common.service.utils.AbstractHelperClass;
import common.web.service.SystemIntegrator;

@ControllerAdvice //(basePackages = "com.github") //basePackages用于指定对哪些包里的Controller起作用。
public class ViewControllerAdvice extends AbstractHelperClass {

	@Value("${security.logoutUrl}")
	private String logoutUrl;
	
	//用于自动绑定前台请求参数到Model中。
    @InitBinder  
    public void initBinder(WebDataBinder binder) {  
    	
    	//// 此处仅演示忽略request中的参数id
    	//binder.setDisallowedFields("id");
    	//用到的场景非常少,目前没有测试过
    	//System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器"); 
    }
    
   
    /**
     * 2017.08.22 增加所有ViewController都会使用的model变量
     * @param model
     */
  	@ModelAttribute  
    public void onRequest(Model model) {  
  		model.addAttribute("logoutUrl", this.logoutUrl);
    }
    
}

