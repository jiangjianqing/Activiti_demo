package common.db.base.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 基础实体Bean，包含系统表中公共的字段 <br>
 * 重写 toString() clone() equals() hashCode()
 * 
 * @author ztxs
 *
 */
@JsonIgnoreProperties(value={"selected","attributeNames","tableName"})
@MappedSuperclass
public abstract class AbstractEntityBean implements Serializable {

	private static final long serialVersionUID = 1962905939086138888L;
	private transient EntityBeanUtil eoutil;
	
	protected transient boolean selected;

	@Transient
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	// property which not persist to database
	@Transient
	protected EntityBeanUtil getBeanUtility() {
		if (eoutil == null) {
			eoutil = new EntityBeanUtil(this);
		}
		return eoutil;
	}

	@Override
	public String toString() {
		return getBeanUtility().beanToString();
	}

	@Override
	public boolean equals(Object obj) {
		return getBeanUtility().equalsBean(obj);
	}

	@Override
	public int hashCode() {
		return getBeanUtility().hashCodeBean();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Object obj = null;
		try {
			obj = getBeanUtility().cloneBean();
		} catch (Exception e) {
			throw new CloneNotSupportedException(e.getMessage());
		}

		return obj;
	}

	/**
	 * 得到所有可持久化字段的名称
	 * 
	 * @return 名称列表
	 */
	@Transient
	public String[] getAttributeNames() {
		return getBeanUtility().getAttributeNames();
	}

	/**
	 * 得到某个字段的值
	 * 
	 * @param attributeName
	 *            字段名
	 * @return 值
	 */
	@Transient
	public Object getAttributeValue(String attributeName) {
		return getBeanUtility().getAttributeValue(attributeName);
	}

	/**
	 * 设置某个字段的值
	 * 
	 * @param attributeName
	 *            字段名
	 * @param value
	 *            值
	 */
	@Transient
	public void setAttributeValue(String attributeName, Object value) {
		getBeanUtility().setAttributeValue(attributeName, value);
	}

	@SuppressWarnings("static-access")
	@Transient
	public String getEnumDescription(String enumAttributeName) {
		Object value = getAttributeValue(enumAttributeName);

		return getBeanUtility().getEnumDescription(value);
	}

	/**
	 * 获得实体对应的表名
	 */
	@Transient
	public String getTableName() {
		return getBeanUtility().getTableName();
	}

	/**
	 * 比较此对象与另一个对象的差别，并返回值不同的字段的名称。
	 * 
	 * @param antherBean
	 *            将要比较的对象
	 * @return 值不同的字段名
	 */
	@Transient
	public List<String> getDifferentFields(AbstractEntityBean anotherBean) {
		return getBeanUtility().getDifferentFields(anotherBean);
	}

	/**
	 * 获取主键值
	 * 
	 * @return 主键值
	 */
	@Transient
	public abstract Object grabPrimaryKey();

	/**
	 * 比较主键值是否相同
	 */
	@Transient
	public boolean equalsPK(Object obj) {
		if (obj == null)// 对象为空不比较
			return false;
		// 类型不同不必进行比较
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		// 不是BaseEO，不必比较
		if (!(obj instanceof AbstractEntityBean)) {
			return false;
		}

		AbstractEntityBean eo = (AbstractEntityBean) obj;

		if (grabPrimaryKey() != null && eo.grabPrimaryKey() != null) {
			if (grabPrimaryKey().equals(eo.grabPrimaryKey()))
				return true;
			return false;
		} else {
			return false;
		}

	}

	/**
	 * 拷贝另一个eo对象中的字段值到当前对象中
	 * 
	 * @param fromEO
	 *            从哪里拷贝
	 * @param copyAttributes
	 *            拷贝哪些字段
	 */
	public void copyAttributeValue(AbstractEntityBean fromEO, String[] copyAttributes) {
		if (copyAttributes == null)
			return;

		for (String attr : copyAttributes) {
			this.setAttributeValue(attr, fromEO.getAttributeValue(attr));
		}
	}

	/**
	 * 加载所有延迟加载字段
	 */
	public void loadLazyAttributes() {
		getBeanUtility().loadLazyField();
	}

}
