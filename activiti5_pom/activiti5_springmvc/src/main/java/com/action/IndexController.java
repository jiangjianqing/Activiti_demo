package com.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import common.spring.AvoidDuplicateSubmission;

@Controller
@RequestMapping("/")
public class IndexController {

	public IndexController() {
		// TODO Auto-generated constructor stub
	}
	
	private UserDetails getUserDetails(){
		UserDetails ret = null;
		// check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			ret = (UserDetails) auth.getPrincipal();
			
		}
		//model.addObject("username", userDetail.getUsername());
		return ret;
	}
	
	private boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		boolean isAuthenticated = !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
		return isAuthenticated;
	}
	
	@RequestMapping(value="/",method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public ModelAndView getAll(HttpServletRequest request,HttpServletResponse response 
    		, RedirectAttributes redirectAttributes){
		ModelAndView model = new ModelAndView("index");

        return model;
    }

}
