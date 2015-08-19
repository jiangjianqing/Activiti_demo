package org.security.toturial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.security.dao.UserDao;
import org.security.domain.DbUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {
	
	protected static Logger logger = Logger.getLogger(MyUserDetailsService.class);  
	
	private UserDao userDAO = new UserDao();  
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();  
        User user = new User();  
        try {  
            user = userDao.getUserByName(username);  
            List<String> authStr= userDao.loadUserAuthoritiesByName(username);  
            for (String authName : authStr) {  
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authName);  
                auths.add(authority);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return new User(user.getId(), user.getUserName(), user.getPassword(), user.getEmail(), user.getCreateDate(), user.getUserName(), auths, true, true, true); 
        */
		//return new User(username, "abc", true, true, true, true, null);
		UserDetails user = null;  
		  
        try {  
  
            // 搜索数据库以匹配用户登录名.  
            // 我们可以通过dao使用JDBC来访问数据库  
            DbUser dbUser = userDAO.getDatabase(username);  
  
            // Populate the Spring User object with details from the dbUser  
            // Here we just pass the username, password, and access level  
            // getAuthorities() will translate the access level to the correct  
            // role type  
  
            user = new User(dbUser.getUsername(), dbUser.getPassword()  
                    .toLowerCase(), true, true, true, true,  
                    getAuthorities(dbUser.getAccess()));  
  
        } catch (Exception e) {  
            logger.error("Error in retrieving user");  
            throw new UsernameNotFoundException("Error in retrieving user");  
        }  
  
        return user; 
	}
	
	/** 
     * 获得访问角色权限 
     *  
     * @param access 
     * @return 
     */  
    public Collection<GrantedAuthority> getAuthorities(Integer access) {  
  
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);  
  
        // 所有的用户默认拥有ROLE_USER权限  
        logger.debug("Grant ROLE_USER to this user");  
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));  
  
        // 如果参数access为1.则拥有ROLE_ADMIN权限  
        if (access.compareTo(1) == 0) {  
            logger.debug("Grant ROLE_ADMIN to this user");  
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  
        }  
  
        return authList;  
    }  

}
