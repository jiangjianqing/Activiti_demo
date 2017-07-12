package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

	//重要：使用@Value注入配置文件中的内容
	@Value("${security.loginProcessingUrl}")  
	private String loginProcessingUrl;
	
	@Value("${security.mainPage}")  
	private String mainPage;

	public AuthController() {
		// TODO Auto-generated constructor stub
	}
	
	private boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		boolean isAuthenticated = !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
		return isAuthenticated;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public ModelAndView getAll(HttpServletRequest request,HttpServletResponse response 
    		, RedirectAttributes redirectAttributes){
		ModelAndView model = new ModelAndView();
		if (isAuthenticated()) {
			request.setAttribute("message", "进入login界面时发现已经登录，直接跳转进main页面");
			redirectAttributes.addFlashAttribute("other_message", "进入login界面时发现已经登录，直接跳转进main页面");
			model.setViewName("redirect:"+this.mainPage);
		} else {
			model.setViewName("login");
			model.addObject("loginUrl", this.loginProcessingUrl);
		}	
        return model;
    }
	//教程：
	//http://www.cnblogs.com/nuoyiamy/p/5591559.html
	
	//注意:thymeleaf目前还无法与tiles3整合,可以使用layout-dialet实现同样的功能
	//https://ultraq.github.io/thymeleaf-layout-dialect/Installation.html
	
	//https://github.com/thymeleaf/thymeleaf-extras-springsecurity

}
