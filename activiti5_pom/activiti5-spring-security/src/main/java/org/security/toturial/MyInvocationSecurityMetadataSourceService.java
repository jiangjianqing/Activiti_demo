package org.security.toturial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.web.FilterInvocation;  
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * 加载资源与权限的对应关系
 * @author cz_jjq
 *
 */
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 返回所请求资源所需要的权限
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		/*
		String url = ((FilterInvocation) object).getRequestUrl();  
        int firstQuestionMarkIndex = url.indexOf("?");  
        if (firstQuestionMarkIndex != -1) {  
            url = url.substring(0, firstQuestionMarkIndex);  
        }  
        if (firstQuestionMarkIndex != -1) {  
            url = url.substring(0, firstQuestionMarkIndex);  
        }  
        System.out.println("url:"+url);  
        List<ConfigAttribute> result = new ArrayList<ConfigAttribute>();  
        ConfigAttribute attribute = new SecurityConfig("ROLE_BASE");  
        result.add(attribute);  
        try {  
            List<Permission> permList = permDao.getPermissionByUrl(url);  
            for (Permission permission : permList) {  
                ConfigAttribute conf = new SecurityConfig(permission.getPermName());  
                result.add(conf);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result; 
        */
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
