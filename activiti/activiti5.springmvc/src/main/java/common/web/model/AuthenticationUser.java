package common.web.model;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.*;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.*;

/*
 * org.activiti.engine.identity.User 中的id为字符串，与common.db.model.identity.entity.User冲突
 */

public class AuthenticationUser extends common.db.model.identity.entity.User implements org.springframework.security.core.userdetails.UserDetails {
	
	private Collection<? extends GrantedAuthority> authorities;
	
	//---------------spring-security-userdetail 内容------------
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return super.getUserName();
	}
	
}
