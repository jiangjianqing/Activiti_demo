package common.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstractController {
	
	protected UserDetails getUserDetails(){
		UserDetails ret = null;
		// check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			ret = (UserDetails) auth.getPrincipal();
			
		}
		return ret;
	}
	
	protected boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		boolean isAuthenticated = !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
		return isAuthenticated;
	}
	
}
