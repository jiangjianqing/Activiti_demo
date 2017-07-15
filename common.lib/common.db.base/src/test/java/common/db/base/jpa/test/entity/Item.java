package common.db.base.jpa.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import common.db.base.jpa.AbstractEntityBean;

@Entity
@Table(name="item")
public class Item extends AbstractEntityBean {

	private int id;
	private String name;
	
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "id", unique = true, nullable = false)  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Column(unique = false, length=32)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public Object grabPrimaryKey() {
		return id;
	}

}
