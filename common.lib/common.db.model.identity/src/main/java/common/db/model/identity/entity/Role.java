package common.db.model.identity.entity;

import java.io.Serializable;
import javax.persistence.*;

import common.db.base.jpa.BaseEntityBean;

import java.util.List;


/**
 * The persistent class for the sys_roles database table.
 * 
 */
@Entity
@Table(name="sys_roles")
//@NamedQuery(name="SysRole.findAll", query="SELECT s FROM SysRole s")
public class Role extends BaseEntityBean {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private byte available;

	private String description;

	@Column(name="role",unique=true,nullable=false,length=60)
	private String role;

	//bi-directional many-to-many association to SysUser
	@ManyToMany(mappedBy="sysRoles",fetch=FetchType.EAGER)
	private List<User> sysUsers;

	public Role() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getAvailable() {
		return this.available;
	}

	public void setAvailable(byte available) {
		this.available = available;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<User> getSysUsers() {
		return this.sysUsers;
	}

	public void setSysUsers(List<User> sysUsers) {
		this.sysUsers = sysUsers;
	}

	@Override
	public Object grabPrimaryKey() {
		return id;
	}

}