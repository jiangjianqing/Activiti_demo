package common.security;

import javax.annotation.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import common.db.model.identity.User;

/**
 * 如果要对当前的用户登陆信息做更多验证，只需要在这里增加内容，比如回答问题等等
 * @author jjq
 *
 */
public class SimpleAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Resource
	private UserDetailsService userDetailsService;
	
	@Resource
	private PasswordEncoderAssist passwordEncoderAssist;

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public PasswordEncoderAssist getPasswordEncoderAssist() {
		return passwordEncoderAssist;
	}

	public void setPasswordEncoderAssist(PasswordEncoderAssist passwordEncoderAssist) {
		this.passwordEncoderAssist = passwordEncoderAssist;
	}

	/**
	 * 能够对除用户名密码以外的登录信息做验证的方法
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		/*
		CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;
		
		String poem = LoginQuestion.getQuestions().get(token.getQuestionId());

		if (!poem.split("/")[1].equals(token.getAnswer())) {
			throw new BadAnswerException("the answer is wrong!");
		}
		*/
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		UserDetails user=userDetailsService.loadUserByUsername(username);
		
		boolean validPassword=passwordEncoderAssist.matches(authentication.getCredentials().toString(), (User)user);
		if (!validPassword){
			throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "CustomAuthenticationProvider Bad credentials"));
		}	
		return user;

	}

}

