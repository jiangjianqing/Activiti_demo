package common.security;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import common.db.base.exception.DaoException;
import common.db.model.identity.Role;
import common.db.model.identity.User;
import common.db.repository.jpa.identity.impl.UserDaoImpl;
import common.web.exception.PasswardInvalidException;
import common.web.model.AuthenticationUser;
import common.web.utils.AbstractHelperClass;

public class CustomUserDetailsService extends AbstractHelperClass implements UserDetailsService {

	private PasswordEncoder passwordEncoder;

	@Resource
	private UserDaoImpl userDao;

	public void encodeNewUserPassword(User user){
		user.setSalt(user.getUserName());
		user.setPassword(encodePassword(user.getPassword(),user.getSalt()));
	}

	public String encodePassword(String password,String salt){
		return passwordEncoder.encodePassword(password, salt);
	}

	public UserDetails loadUserByUsername(String username,String password) throws UsernameNotFoundException, DaoException, PasswardInvalidException, IllegalAccessException, InvocationTargetException {

		User user=userDao.findByName(username);
		if(user==null)
        	throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"));    

		if(null!=password){
			String pwd=encodePassword(password,user.getSalt());
			if(!pwd.equals(user.getPassword()))
				throw new PasswardInvalidException();
		}

        return createUserDetails(username, user, getAuthorities(user.getRoles())); 
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.debug("UserDetailsServiceImpl 收到认证请求，username="+username);
		try {
			return loadUserByUsername(username,null);			
		}catch(UsernameNotFoundException notFound){
			throw notFound;
		}
		catch (Exception ex) {
			throw new AuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	private UserDetails createUserDetails(String username, User userFromUserQuery,
            Collection<GrantedAuthority> combinedAuthorities) throws IllegalAccessException, InvocationTargetException {
        String returnUsername = userFromUserQuery.getUserName();
        User user=userFromUserQuery;

        boolean enabled = user.isEnabled();  
        boolean accountNonExpired = user.isAccountNonExpired();  
        boolean credentialsNonExpired = user.isCredentialsNonExpired();  
        boolean accountNonLocked = user.isAccountNonLocked();   

        AuthenticationUser uerDetail = new AuthenticationUser();
        BeanUtils.copyProperties(uerDetail, user);
        return uerDetail;  
    }

    private Collection<GrantedAuthority> getAuthorities(List<Role> roles) {  
    	Integer access=1;
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);  

        logger.debug("Grant ROLE_USER to this user");  
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));  

        if (access.compareTo(1) == 0) {  
            logger.debug("Grant ROLE_ADMIN to this user");  
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  
        }  

        return authList;  
    }  

    public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}

