package com.focusight.platform3.views.model;

import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class User {
	@NotEmpty(message="姓名不能为空")
	@Length(min=8, max=30, message="姓名（ ${validatedValue} ） 长度必须大于 {min} 小于 {max}")
	private String name;
	@NotEmpty(message="密码不能为空")
	@Length(min=5, max=10, message="密码 （ ${validatedValue} ） 长度必须大于 {min} 小于 {max}")  //这里使用了el表达式，需要在pom中引用javax.el
	private String password;  
	@NotNull(message="生日不能为空")
	private String birthday;
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
