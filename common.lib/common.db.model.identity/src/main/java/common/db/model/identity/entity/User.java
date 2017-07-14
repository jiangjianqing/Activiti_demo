package common.db.model.identity.entity;

import java.io.Serializable;
import java.lang.reflect.Array;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NotFound;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.db.base.jpa.BaseEntityBean;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the sys_users database table.
 * 

@Table(name = "user_roles", catalog = "test", 
uniqueConstraints = @UniqueConstraint(
  columnNames = { "role", "username" }))
  //上述为组合字段唯一限制的范例
 */
@Entity
@Table(name="sys_users")
//@NamedQuery(name="SysUser.findAll", query="SELECT s FROM SysUser s")
//重要：不要在父类与子类同时使用JsonIgnoreProperties，会导致父类的设定失效
//@JsonIgnoreProperties(value={"sysRoles"/*,"password","salt"*/})
public class User extends BaseEntityBean {
	private static final long serialVersionUID = 1L;
	
	//自定义生成clob类型字段的sql语句。
	//@column(name=" contact_name ",columndefinition="clob not null")
	//private String name;
	
	//指定字段“monthly_income”月收入的类型为double型，精度为12位，小数点位数为2位
	//@column(name="monthly_income",precision=12, scale=2)
	//private bigdecimal monthlyincome;
	
	/*insertable 和updatable两个变量注解属性，分别表示字段在插入和修改是，对象属性的可用性，
	 * 例如创建人的insertable = true ,updatable = false, 
	 * 当执行insert语句时将写入创建人，修改时将不能更改创建人，
	 * 关于默认值，可以在@PrePersist和@PreUpdate来预置默认值。 
	*/	
	
	@PrePersist
	void prePersist(){}
	
	@PreUpdate
    void preUpdate() { }
	
	@PreRemove
	void preRemove() { }
	
	@PreDestroy
	void preDestroy(){}
	
	@PostLoad
	public void postLoad(){
		if(this.sysRoles!=null){
			for (Role role : this.sysRoles) {
				roles.add(role.getId());
			}
		}
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="password",nullable=false,length=255)
	private String password;

	private String salt;

	@NotNull(message="{user.name.null}")
	@Size(min=6,max=30,message="{user.name.length.illegal}")
	@Column(name="username",unique=true,nullable=false,length=60)
	private String username;
	
	//下面几项为spring-security需要的内容，默认值=true
	private boolean enabled=true;  
    private boolean accountNonExpired=true;  
    private boolean credentialsNonExpired=true;  
    private boolean accountNonLocked=true;   
    
    @Column(length=8192)
    private String extendInfo="";

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

	//bi-directional many-to-many association to SysRole
	@JsonBackReference
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="sys_users_roles"
		, joinColumns={
			@JoinColumn(name="user_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="role_id")
			}
		)
	private List<Role> sysRoles;
	
	@Transient
	private List<Integer> roles=new ArrayList<Integer>();

	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoleIds(List<Integer> roles) {
		this.roles = roles;
	}

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUsername() {
		return this.username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Role> getSysRoles() {
		return this.sysRoles;
	}

	public void setSysRoles(List<Role> sysRoles) {
		this.sysRoles = sysRoles;
	}

	@Override
	public Object grabPrimaryKey() {
		return id;
	}

}