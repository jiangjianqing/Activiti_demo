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
@RequestMapping("/AdminLTE")
public class AdminLTETemplateController extends AbstractViewController{

	
	public AdminLTETemplateController() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	@RequestMapping(value={"" , "/list"},method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public String getAll(Model model,HttpServletRequest request,HttpServletResponse response){
		model.addAttribute("name", "Dear");
		//特别注意：这里返回的是tiles.xml中定义的definition.name
        return "adminlte-template-example";
    }

}
