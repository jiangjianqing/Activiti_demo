package common.db.base.jpa;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.internal.BaseJpaDaoImpl;
import common.db.base.jpa.internal.PaginationJpaDaoImpl;
import common.db.base.page.PageObject;

/**
 * 后续所有的JPA DAO 类都需要从这里继承
 * @author jjq
 *
 * @param <T>
 */

public abstract class AbstractJpaDaoImpl<T> implements AbstractJpaDao<T> {
	
	protected BaseJpaDaoImpl<T> baseDao = new BaseJpaDaoImpl<T>();
	protected PaginationJpaDaoImpl paginationDao = new PaginationJpaDaoImpl();
	
	protected Class<T> entityClazz;
	
	protected Class<T> getEntityClazz(){
		return entityClazz;
	}
		
	/**
	 * 2017.07.15非常重要：
	 * 为了spring通过proxy的机制进行事务管理，这里的dao类方法都绝对不能使用final标注
	 */

	@PersistenceContext	/*20170715重要 ： 通过注解注入EntityManager*/  
	public void setEntityManager(EntityManager em){
		//取得T的类型变量
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityClazz=(Class<T>) type.getActualTypeArguments()[0];
		
		baseDao.setEntityManager(em , entityClazz);
		paginationDao.SetEntityManager(em);
	}

	public void create(T t) throws DaoException {
		baseDao.create(t);
	}
	
	public T update(T t) throws DaoException{
		return baseDao.merge(t);
	}
	
	public boolean delete(T t) throws DaoException{
		return baseDao.remove(t);
	}

	public T findByKey(Object key) throws DaoException {
		return baseDao.findByKey(key);
	}

	public boolean deleteByKey(Object key) throws DaoException {
		return baseDao.removeByKey(key);
	}
	
	public List<T> getList() throws DaoException{
		return baseDao.listAll();
	}

	public PageObject<T> getPageList(int currPage) throws OutOfPageRangeException, DaoException {
		return paginationDao.queryForPaginationList(currPage , getEntityClazz());
	}

	public PageObject<T> getPageList(int currPage, int pageSize) throws OutOfPageRangeException, DaoException{
		return paginationDao.queryForPaginationList(currPage,pageSize , getEntityClazz());
	}

}
