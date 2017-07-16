package common.web.model;

import java.util.Collection;
import java.util.Date;
import java.util.zip.CRC32;

import javax.validation.Valid;

import org.hibernate.validator.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.*;

/*
 * org.activiti.engine.identity.User 中的id为字符串，与common.db.model.identity.entity.User冲突
 */

public class AuthenticationUser extends common.db.model.identity.User implements UserDetails {
	
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
	
	public void cloneUserDetails(UserDetails userDetails){
		this.setUserName(userDetails.getUsername());
		this.setPassword(userDetails.getPassword());
		this.setEnabled(userDetails.isEnabled());
		this.setAccountNonExpired(userDetails.isAccountNonExpired());
		this.setAccountNonLocked(userDetails.isAccountNonLocked());
		this.setCredentialsNonExpired(userDetails.isCredentialsNonExpired());
		this.setAuthorities(userDetails.getAuthorities());
		
		if(this.getId()==null){
			System.out.println("cloneUserDetail属于测试代码，传递过来的UserDetail并不携带UserID，用CRC32生成");
			CRC32 crc32 = new CRC32();
			crc32.update(userDetails.getUsername().getBytes());
			this.setId(crc32.getValue());
		}
	}
	
}
