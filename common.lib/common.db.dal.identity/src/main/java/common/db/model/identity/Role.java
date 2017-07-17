package common.db.model.identity;

import java.io.Serializable;
import javax.persistence.*;

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

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="NAME",unique=true,nullable=false,length=100)
	private String name;
	
	@Column(name="TYPE")
	private String type;

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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
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