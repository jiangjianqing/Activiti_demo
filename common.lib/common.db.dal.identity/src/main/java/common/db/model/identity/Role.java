package common.db.model.identity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the sys_roles database table.
 * 
 */
@Entity
@Table(name="COMMON_ID_ROLE")
//@NamedQuery(name="SysRole.findAll", query="SELECT s FROM SysRole s")
public class Role implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@PostLoad
	public void populateTransientFields(){
		//为前端填充角色类型描述信息
		if (type!=null){
			setTypeDescription(type.getDescription());
		}
	}

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="NAME",unique=true,nullable=false,length=100)
	private String name;
	
	@Column(name="TYPE")
	@Enumerated(EnumType.STRING)
	private RoleTypeEnum type;
	
	@Transient
	private String typeDescription;

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String roleTypeDescription) {
		this.typeDescription = roleTypeDescription;
	}

	//bi-directional many-to-many association to SysUser
	@ManyToMany(mappedBy="roles",fetch=FetchType.EAGER)
	private List<User> users;

	public Role() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleTypeEnum getType() {
		return this.type;
	}

	public void setType(RoleTypeEnum type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}