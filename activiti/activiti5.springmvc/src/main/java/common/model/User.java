package common.model;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.*;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.*;

public class User extends common.db.model.identity.entity.User implements org.activiti.engine.identity.User ,org.springframework.security.core.userdetails.UserDetails {
	
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
		return null;
	}
	
	
	public String getId() {
		return super.getId().toString();
	}
	@Override
	public void setId(String id) {
		
		
	}
	@Override
	public boolean isPictureSet() {
		
		return false;
	}
	
}
