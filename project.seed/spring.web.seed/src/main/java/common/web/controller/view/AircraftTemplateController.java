package common.web.controller.view;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import common.security.AuthenticationUser;
import common.web.controller.AbstractViewController;
import common.web.model.sidebar.SimpleSidebarGroup;
import common.web.model.sidebar.SimpleSidebarItem;

@Controller
@RequestMapping("/aircraft")
public class AircraftTemplateController extends AbstractViewController{

	private List<SimpleSidebarGroup> groups;
	
	public AircraftTemplateController() {
		// TODO Auto-generated constructor stub
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
  		group.setName("dashboard");
  		group.setTitle("Dashboard");
  		group.setUniqueGroupClass("dashboard-menu");
  		group.setGraphClasses("fa fa-fw fa-dashboard");
  		group.setItems(new ArrayList<SimpleSidebarItem>());
  		SimpleSidebarItem item=new SimpleSidebarItem();
  		item.setName("module");
  		item.setTitle("Module");
  		item.setHref("#");
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
  		item.setHref("#");
  		group.getItems().add(item);
  		
  		list.add(group);
  		
  		groups=list;
  		return list;

    }
	
	@RequestMapping(value={"" , "/list"},method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public String getAll(Model model,HttpServletRequest request,HttpServletResponse response){
		model.addAttribute("name", "Dear");
		//特别注意：这里返回的是tiles.xml中定义的definition.name
        return "aircraft-template-example";
    }

}
