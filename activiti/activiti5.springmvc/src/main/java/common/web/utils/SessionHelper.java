package common.web.utils;

import java.io.Closeable;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import com.mysql.cj.api.Session;

public class SessionHelper implements AutoCloseable {
	private final String springSecurityContextName = "SPRING_SECURITY_CONTEXT";
	
	private HttpSession session;
	
	public SessionHelper(HttpSession session){
		this.session = session;
	}
	
	public String getSpringSecurityContextName(){
		return this.springSecurityContextName;
	}
	
	public Object getSpringSecurityContext(){
		return this.session.getAttribute(this.springSecurityContextName);
	}
	
	
	public Object getAuthenticatedUser(){
		return null;
	}

	public boolean isLogin(){
		return false;
	}

	@Override
	public void close() throws IOException {
		this.session=null;
	}
}
