package common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import common.db.model.identity.User;

public interface PasswordEncoderAssist extends PasswordEncoder {
	public boolean matches(String password,User user);
	public void encodeNewUserPassword(User user);
}
