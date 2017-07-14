package common.db.base.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import common.db.base.exception.DaoException;

public interface BaseDao<T> {
	void setEntityManager(EntityManager em);
	EntityManager getEntityManager();
	List<T> listAll() throws DaoException;
	//T findById(Class<T> c,Object id);
	T findByKey(Object key) throws DaoException;
	//20160419添加，便于在当前BaseDao中快速查询指定类型的对象
	Object findByKey(Class<?> clazz,Object key) throws DaoException;
	void create(T object) throws DaoException;
	T update(T object) throws DaoException;
	boolean removeByKey(Object key) throws DaoException;
	int removeAll() throws DaoException;
 
    /**
     * 可确定为新建实体；返回处于托管状态的实例
     * @throws DaoException 
     */
    public T persist(T t) throws DaoException;
 
    public void persist(Collection<T> ts) throws DaoException;
 
    /**
     * 若数据库中已有此记录，置为托管状态
     * @throws DaoException 
     */
    public T merge(T t) throws DaoException;
 
    /**
     * 若数据库中已有此记录，置为托管状态
     * @throws DaoException 
     */
    public Collection<T> merge(Collection<T> t) throws DaoException;
 
    /**
     * 删除
     * @throws DaoException 
     */
    public boolean remove(T t) throws DaoException;
 
    /**
     * 批量删除 传入集合
     * @throws DaoException 
     */
    public void remove(Collection<T> ts) throws DaoException;
 
    /**
     * 删除指定id、指定class的实例
     * @throws DaoException 
     */
    public void remove(Class<T> clazz, Object id) throws DaoException;
 
    /**
     * 删除指定id、指定class的实例
     * @throws DaoException 
     */
    public void remove(Class<T> clazz, Collection<Object> ids) throws DaoException;
 
    /**
     * 清除一级缓存
     * @throws DaoException 
     */
    public void clear() throws DaoException;
 
    /**
     * 将对象置为游离状态
     * @throws DaoException 
     */
    public void detach(T t) throws DaoException;
 
    /**
     * 将对象置为游离状态
     * @throws DaoException 
     */
    public void detach(Collection<T> ts) throws DaoException;
 
    /**
     * 检查实例是否处于托管状态
     * @throws DaoException 
     */
    public boolean isManaged(T t) throws DaoException;
 
    /**
     * 同步jpa容器和数据库
     * @throws DaoException 
     */
    public void flush() throws DaoException;
}
