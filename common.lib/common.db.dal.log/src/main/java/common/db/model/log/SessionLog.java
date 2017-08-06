package common.db.model.log;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  @AssertTrue //用于Boolean字段，该字段只能为true  
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
@Table(name="COMMON_LOG_SESSION")
@NamedQueries({
    //@NamedQuery(name="findAll",query="SELECT u FROM User u"),
    //@NamedQuery(name="findUserWithId",query="SELECT u FROM User u WHERE u.id = ?1"),
    //@NamedQuery(name="User.findByName",query="SELECT u FROM User u WHERE u.userName = :name")
}) 
//重要：不要在父类与子类同时使用JsonIgnoreProperties，会导致父类的设定失效
//@JsonIgnoreProperties(value={"sysRoles"/*,"password","salt"*/})
public class SessionLog implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@PrePersist
	void populateDBFields(){}
	
	@PreUpdate
    void preUpdate() {
		//createTime=null;//确保create不会被更新
	}
	
	@PreRemove
	void preRemove() { }
	
	@PreDestroy
	void preDestroy(){}
	
	@PostLoad
	public void populateTransientFields(){
		/*
		if (this.roles == null){
			this.roles = new ArrayList<Role>();
		}
		for (Role role : this.roles) {
			roleIds.add(role.getId());
		}*/
	}
	/**
	 *代码自动生成字符串ID的方式 
	@Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2") //这个是hibernate的注解/生成32位UUID
    protected String id;
	 */

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;


	private Long userId;
	
	private String sessionId;
	
	private String hostName;
	
	private String ipAddr;
	
	private Date createTime;
	
	private Date loginTime;
	
	private Date logoutTime;
	
	//是否超时导致会话无效
	private Boolean SessionNonTimeouted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public Boolean getSessionNonTimeouted() {
		return SessionNonTimeouted;
	}

	public void setSessionNonTimeouted(Boolean sessionNonTimeouted) {
		SessionNonTimeouted = sessionNonTimeouted;
	}

}