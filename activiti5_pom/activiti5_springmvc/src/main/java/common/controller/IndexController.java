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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		ModelAndView model = new ModelAndView("index");
		model.addObject("logoutUrl", this.logoutUrl);
        return model;
    }

}
