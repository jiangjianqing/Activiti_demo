package org.security.toturial;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class MyAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object obj, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		
		if( configAttributes == null ) {  
            return ;  
        }  
		//所请求的资源拥有的权限(一个资源对多个权限)
        Iterator<ConfigAttribute> ite = configAttributes.iterator();  
          
        while( ite.hasNext()){  
            ConfigAttribute ca = ite.next();  
            //访问所请求资源所需要的权限
            String needPermission = ((SecurityConfig)ca).getAttribute(); 
            System.out.println("needPermission is " + needPermission); 
            //用户所拥有的权限authentication  
            for( GrantedAuthority ga: authentication.getAuthorities()){  
                if(needPermission.trim().equals(ga.getAuthority().trim())){  
                    return;  
                }  
            }  
        }  
        throw new AccessDeniedException("无权限！");  

	}

	@Override
	public boolean supports(ConfigAttribute arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
