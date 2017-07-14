package common.db.base.jpa.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import common.db.base.jpa.BaseEntityBean;

@Entity
@Table(name="user")
public class User extends BaseEntityBean {

	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "id", unique = true, nullable = false)  
	private int id;
	
	@Column(unique = false, length=32)
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Object grabPrimaryKey() {
		// TODO Auto-generated method stub
		return id;
	}

}
