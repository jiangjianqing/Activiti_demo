package common.web.controller.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import common.service.utils.AbstractController;

@Controller
@RequestMapping("/")
public class IndexController extends AbstractController {
	
	@Value("${security.logoutUrl}")
	private String logoutUrl;

	public IndexController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/",method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public ModelAndView getAll(HttpServletRequest request,HttpServletResponse response 
    		, RedirectAttributes redirectAttributes){
		//特别注意：这里返回的是WEB-INFO/templates中定义的.html文件名称
		ModelAndView model = new ModelAndView("index");
		model.addObject("logoutUrl", this.logoutUrl);
        return model;
    }
	
	//thymeleaf教程：
		//http://www.cnblogs.com/nuoyiamy/p/5591559.html
		
		//注意:thymeleaf目前还无法与tiles3整合,可以使用layout-dialet实现同样的功能
		//https://ultraq.github.io/thymeleaf-layout-dialect/Installation.html
		
		//http://blog.csdn.net/sun1021873926/article/details/61615219
		//http://blog.csdn.net/mygzs/article/details/52668099
		//https://github.com/thymeleaf/thymeleaf-extras-springsecurity

}
