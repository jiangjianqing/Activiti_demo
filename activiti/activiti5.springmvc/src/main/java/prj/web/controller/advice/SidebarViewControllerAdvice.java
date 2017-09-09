package prj.web.controller.advice;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import common.service.utils.AbstractHelperClass;
import common.web.controller.AbstractViewController;
import common.web.model.sidebar.SimpleSidebarGroup;
import common.web.model.sidebar.SimpleSidebarItem;

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
public class SidebarViewControllerAdvice extends AbstractHelperClass {
	private List<SimpleSidebarGroup> groups;

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
  	@ModelAttribute("sidebarGroups")
    public List<SimpleSidebarGroup> addSidebarGroups() {  
  		if (groups!=null){
  			return groups;
  		}
  		logger.warn("sidebarGroups added!!!");
  		List<SimpleSidebarGroup> list = new ArrayList<SimpleSidebarGroup>();
  		SimpleSidebarGroup group=new SimpleSidebarGroup();
  		group.setName("workflow");
  		group.setTitle("Workflow");
  		group.setUniqueGroupClass("dashboard-menu");
  		group.setGraphClasses("fa fa-fw fa-dashboard");
  		group.setItems(new ArrayList<SimpleSidebarItem>());
  		SimpleSidebarItem item=new SimpleSidebarItem();
  		item.setName("process");
  		item.setTitle("Process");
  		item.setHref("/workflow/process");
  		group.getItems().add(item);
  		
  		item=new SimpleSidebarItem();
  		item.setName("historic-process");
  		item.setTitle("HistoricProcess");
  		item.setHref("/workflow/process/finished");
  		group.getItems().add(item);
  		
  		item=new SimpleSidebarItem();
  		item.setName("task");
  		item.setTitle("Task");
  		item.setHref("/workflow/task");
  		group.getItems().add(item);
  		
  		item=new SimpleSidebarItem();
  		item.setName("execution");
  		item.setTitle("Execution");
  		item.setHref("/workflow/execution");
  		group.getItems().add(item);
  		
  		list.add(group);
  		
  		//---加入identity组
  		group=new SimpleSidebarGroup();
  		group.setName("identity");
  		group.setTitle("Identity");
  		group.setUniqueGroupClass("debug-menu");
  		group.setGraphClasses("fa fa-fw fa-legal");
  		group.setItems(new ArrayList<SimpleSidebarItem>());
  		item=new SimpleSidebarItem();
  		item.setName("user");
  		item.setTitle("User");
  		item.setHref("/identity/user");
  		group.getItems().add(item);
  		item=new SimpleSidebarItem();
  		item.setName("role");
  		item.setTitle("Role");
  		item.setHref("/identity/role");
  		group.getItems().add(item);
  		
  		list.add(group);
  		
  		//---加入测试组
  		group=new SimpleSidebarGroup();
  		group.setName("debug");
  		group.setTitle("Debug");
  		group.setUniqueGroupClass("debug-menu");
  		group.setGraphClasses("fa fa-fw fa-legal");
  		group.setItems(new ArrayList<SimpleSidebarItem>());
  		item=new SimpleSidebarItem();
  		item.setName("develop");
  		item.setTitle("Develop");
  		item.setHref("/develop");
  		group.getItems().add(item);
  		
  		list.add(group);
  		
  		groups=list;
  		return list;

    }

}

