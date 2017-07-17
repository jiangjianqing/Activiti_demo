package common.db.model.identity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Timestamp;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.db.base.jpa.AbstractEntityBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  @AssertTrue //用于boolean字段，该字段只能为true  
    @AssertFalse//该字段的值只能为false  
    @CreditCardNumber//对信用卡号进行一个大致的验证  
    @DecimalMax//只能小于或等于该值  
    @DecimalMin//只能大于或等于该值  
    @Digits(integer=2,fraction=20)//检查是否是一种数字的整数、分数,小数位数的数字。  
    @Email//检查是否是一个有效的email地址  
    @Future//检查该字段的日期是否是属于将来的日期  
    @Length(min=,max=)//检查所属的字段的长度是否在min和max之间,只能用于字符串  
    @Max//该字段的值只能小于或等于该值  
    @Min//该字段的值只能大于或等于该值  
    @NotNull//不能为null  
    @NotBlank//不能为空，检查时会将空格忽略  
    @NotEmpty//不能为空，这里的空是指空字符串  
    @Null//检查该字段为空  
    @Past//检查该字段的日期是在过去  
    @Size(min=, max=)//检查该字段的size是否在min和max之间，可以是字符串、数组、集合、Map等  
    @URL(protocol=,host,port)//检查是否是一个有效的URL，如果提供了protocol，host等，则该URL还需满足提供的条件  
    @Valid//该注解只要用于字段为一个包含其他对象的集合或map或数组的字段，或该字段直接为一个其他对象的引用，  
            //这样在检查当前对象的同时也会检查该字段所引用的对象  
 * @author cz_jjq
 *
 */
/**
 * The persistent class for the sys_users database table.
 * 

@Table(name = "user_roles", catalog = "test", 
uniqueConstraints = @UniqueConstraint(
  columnNames = { "role", "username" }))
  //上述为组合字段唯一限制的范例
 */
@Entity
@DynamicInsert //设置为true,表示insert对象的时候,生成动态的insert语句,如果这个字段的值是null就不会加入到insert语句当中.默认false。
@DynamicUpdate //设置为true,表示update对象的时候,生成动态的update语句,如果这个字段的值是null就不会被加入到update语句中,默认false。
@Table(name="COMMON_ID_USER")
@NamedQueries({
    //@NamedQuery(name="findAll",query="SELECT u FROM User u"),
    //@NamedQuery(name="findUserWithId",query="SELECT u FROM User u WHERE u.id = ?1"),
    @NamedQuery(name="User.findByName",query="SELECT u FROM User u WHERE u.userName = :name")
}) 
//重要：不要在父类与子类同时使用JsonIgnoreProperties，会导致父类的设定失效
//@JsonIgnoreProperties(value={"sysRoles"/*,"password","salt"*/})
public class User extends AbstractEntityBean {
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
    void preUpdate() {
		createTime=null;//确保create不会被更新
	}
	
	@PreRemove
	void preRemove() { }
	
	@PreDestroy
	void preDestroy(){}
	
	@PostLoad
	public void postLoad(){
		if (this.roles == null){
			this.roles = new ArrayList<Role>();
		}
		for (Role role : this.roles) {
			roleIds.add(role.getId());
		}
	}

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message="密码不能为空")
	@Length(min=5, max=10, message="密码 （ ${validatedValue} ） 长度必须大于 {min} 小于 {max}")  //这里使用了el表达式，需要在pom中引用javax.el
	@Column(name="PASSWORD",nullable=false,length=255)
	private String password;

	@Column(name="SALT")
	private String salt;

	@NotNull(message="{user.name.null}")
	@Size(min=6,max=30,message="{user.name.length.illegal}")
	@NotEmpty(message="姓名不能为空")
	@Length(min=8, max=30, message="姓名（ ${validatedValue} ） 长度必须大于 {min} 小于 {max}")
	@Column(name="USERNAME",unique=true,nullable=false,length=60)
	private String userName;
	
	//下面几项为spring-security需要的内容，默认值=true
	@Column(name="ENABLED")
	private boolean enabled;
	
	@Column(name="ACCOUNT_NON_EXPIRED")
    private boolean accountNonExpired;
    
    @Column(name="CREDENTIALS_NON_EXPIRED")
    private boolean credentialsNonExpired;  
    
    @Column(name="ACCOUNT_NON_LOCKED")
    private boolean accountNonLocked;   
    
    @Column(name="FIRST_NAME")
    private String firstName;
    
    @Column(name="LAST_NAME")
	private String lastName;
    
	@Column(name="EMAIL")
	private String email;
	
	@Temporal(TemporalType.TIMESTAMP)//利用@Temporal则可以获取自己想要的格式类型
	@Column(name="CREATE_TIME" , updatable=false //createTime不许更新
		,columnDefinition="TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
	)
	private Date createTime;
	
	/**
	 * 最后一次更新的时间，不包括对lastSuccessLoginTime的修改
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME" 
		//,columnDefinition="TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"//H2不支持 ON UPDATE CURRENT_TIMESTAMP
		,nullable=true
	)
	private Date updateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_SUCCESS_LOGIN_TIME"
			,nullable=true
	)
	private Date lastSuccessLoginTime;
    
	/*
    @Column(length=8192)
    private String extendInfo="";

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}
	*/

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//bi-directional many-to-many association to SysRole
	@JsonBackReference
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="COMMON_ID_USER_ROLE"
		, joinColumns={
			@JoinColumn(name="USER_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ROLE_ID")
			}
		)
	private List<Role> roles;
	
	@Transient
	private List<Long> roleIds=new ArrayList<Long>();

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public User() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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

	public String getUserName() {
		return this.userName;
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Role> getRoles() {
		if (this.roles == null){
			this.roles = new ArrayList<Role>();
		}
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Object grabPrimaryKey() {
		return id;
	}

}