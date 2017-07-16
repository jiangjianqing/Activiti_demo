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
import common.web.exception.PasswardInvalidException;
import common.web.utils.AbstractHelperClass;

/**
 * 用户的密码验证、权限生成都有本类完成
 * @author jjq
 *
 */
public class CustomUserDetailsService extends AbstractHelperClass implements UserDetailsService {

	private PasswordEncoder passwordEncoder;

	@Resource
	private UserDaoImpl userDao;
	
	/**
	 * 对新添加的用户的密码进行编码，修改密码也要使用这里
	 * @param user
	 */
	public void encodeNewUserPassword(User user){
		//特别重要：在这里设置了每个User的Salt
		user.setSalt(user.getUserName());
		user.setPassword(encodePassword(user.getPassword(),user));
	}
	/**
	 * 获取用户的Salt
	 * @param user
	 * @return
	 */
	private String getSalt(User user){
		return user.getSalt();
	}

	/**
	 * 获取原始密码，为了加强密码的防破解，使用了salt
	 * @param password
	 * @param user
	 * @return
	 */
	public String getRawPassword(String password,User user){
		return password + getSalt(user);
	}
	
	/**
	 * 对用户密码进行编码
	 * @param password
	 * @param user
	 * @return
	 */
	public String encodePassword(String password,User user){
		return passwordEncoder.encode(getRawPassword(password,user));
	}
	
	/**
	 * 判断输入的明文密码和User中存储的加密密码是否匹配
	 * @param password
	 * @param user
	 * @return
	 */
	public boolean isPasswordMatch(String password,UserDetails user){
		return passwordEncoder.matches(getRawPassword(password,(User)user), user.getPassword());
	}

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

		User user=userDao.findByName(username);
		if(user==null)
        	throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"));    

		if(null!=password){

			if(isPasswordMatch(password,(UserDetails)user) == false)
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

    public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}

