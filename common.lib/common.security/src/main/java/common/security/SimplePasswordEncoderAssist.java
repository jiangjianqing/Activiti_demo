package common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import common.db.model.identity.User;

public class SimplePasswordEncoderAssist implements PasswordEncoderAssist {
	
	private PasswordEncoder passwordEncoder;


	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

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
	 * 获取原始密码，为了加强密码的防破解，使用了salt
	 * @param password
	 * @param user
	 * @return
	 */
	private String getRawPassword(String password,User user){
		return password + getSalt(user);
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
	 * 对用户密码进行编码
	 * @param password
	 * @param user
	 * @return
	 */
	public String encodePassword(String password,User user){
		return passwordEncoder.encode(getRawPassword(password,user));
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	/**
	 * 判断输入的明文密码和User中存储的加密密码是否匹配
	 * @param password
	 * @param user
	 * @return
	 */
	@Override
	public boolean matches(String password, User user) {
		return passwordEncoder.matches(getRawPassword(password,(User)user), user.getPassword());
	}

}
