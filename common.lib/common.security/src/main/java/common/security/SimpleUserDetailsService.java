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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import common.security.exception.PasswardInvalidException;
import common.service.utils.AbstractHelperClass;

/**
 * 用户的密码验证、权限生成都有本类完成
 * @author jjq
 *
 */
public class SimpleUserDetailsService extends AbstractHelperClass implements UserDetailsService {

	private PasswordEncoderAssist passwordEncoderAssist;

	public PasswordEncoderAssist getPasswordEncoderAssist() {
		return passwordEncoderAssist;
	}

	public void setPasswordEncoderAssist(PasswordEncoderAssist passwordEncoderAssist) {
		this.passwordEncoderAssist = passwordEncoderAssist;
	}

	@Resource
	private UserDaoImpl userDao;

	/**
	 * 判断用户名和密码是否正确
	 * @param username
	 * @param password
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws DaoException
	 * @throws PasswardInvalidException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserDetails loadUserByUsername(String username,String password) throws UsernameNotFoundException, DaoException, PasswardInvalidException, IllegalAccessException, InvocationTargetException {

		User user=userDao.findByUserName(username);
		if(user==null)
        	throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"));    

		if(null!=password){

			if(passwordEncoderAssist.matches(password,user) == false)
				throw new PasswardInvalidException();
		}

        return createUserDetails(user, getAuthorities(user.getRoles())); 
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

	/**
	 * 根据查询到的用户信息和组合好的权限，生成AuthenticationUser
	 * @param userFromUserQuery
	 * @param combinedAuthorities
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private UserDetails createUserDetails(User userFromUserQuery,
            Collection<GrantedAuthority> combinedAuthorities) throws IllegalAccessException, InvocationTargetException {
  
        AuthenticationUser uerDetail = new AuthenticationUser();
        BeanUtils.copyProperties(uerDetail, userFromUserQuery);
        uerDetail.setAuthorities(combinedAuthorities);
        return uerDetail;  
    }

	/**
	 * 根据当前用户的角色信息生成权限表
	 * @param roles
	 * @return
	 */
    private Collection<GrantedAuthority> getAuthorities(List<Role> roles) {  
    	//TODO:权限控制需要在这里补充角色与权限的关系
    	
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


}

