package common.web.controller.advice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import common.service.utils.AbstractHelperClass;
import common.web.service.SystemIntegrator;

@ControllerAdvice //(basePackages = "com.github") //basePackages用于指定对哪些包里的Controller起作用。
public class IntegrationControllerAdvice extends AbstractHelperClass {

	//20170817 加入required=false ,当SystemIntegrator没有注册时不会报错
	@Autowired(required=false) 
	private SystemIntegrator systemIntegrator;
	
	//用于自动绑定前台请求参数到Model中。
    @InitBinder  
    public void initBinder(WebDataBinder binder) {  
    	
    	// 此处仅演示忽略request中的参数id
   		//binder.setDisallowedFields("id");
   	    //用到的场景非常少,目前没有测试过
   	    //System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器"); 
   	    	
   	    //重要：本类用于解决：Failed to convert value of type 'java.lang.String' to required type 'java.util.Date'; 
   	    //发生这一错误的主要原因是Controller类中需要接收的是Date类型，但是在页面端传过来的是String类型，最终导致了这个错误。
    	//特别注意:在各个Controller中如果需要单独定制时间格式，单独复制该方法到该Controller中即可
    	
    	
    	//极其重要：如果在每个Controller中如果时间字段处理规则不同，可以使用下形式:
    	//binder.registerCustomEditor(Date.class,#{fieldName}, new CustomDateEditor(dateFormat, true));  
    	//参考范例 http://www.cnblogs.com/AloneSword/p/3998943.html    说说Spring中的WebDataBinder

    	logger.debug("**in testing,binder.registerCustomEditor 用来支持springmvc 中 string转Date,一种类型转换只能注册一次");
    	/*
   		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
   		dateFormat.setLenient(true);   
   		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
           
   		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");   
   		dateFormat.setLenient(true);   
   		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  */
    	
   		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
   		dateFormat.setLenient(true);
   		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); 
           
           
    }
    
    /**
     * 利用ModelAtribute的特性完成SystemIntegration whenRequest的动作，这样性能比较高
     * 原因是利用RequestListener时出现了4次调用，影响性能,可能是使用了模板后的后遗症
     */
  	@ModelAttribute  
    public void onRequest() {  
  		if (systemIntegrator!=null){
  			systemIntegrator.onServletRequest();
  		}
  		

    }
    
}

