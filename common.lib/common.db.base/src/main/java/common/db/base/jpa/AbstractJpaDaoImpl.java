package common.db.base.jpa;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.db.base.exception.DaoException;
import common.db.base.exception.NoFieldChangedException;
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
	
	protected final Logger logger = LoggerFactory
			.getLogger(getClass());

	protected BaseJpaDaoImpl<T> baseDao = new BaseJpaDaoImpl<T>();
	protected PaginationJpaDaoImpl paginationDao = new PaginationJpaDaoImpl();
	
	protected Class<T> entityClass;
	
	protected final Class<T> getEntityClass(){
		return entityClass;
	}

	@PersistenceContext	/*20170715重要 ： 通过注解注入EntityManager*/  
	public final void setEntityManager(EntityManager em){
		//取得T的类型变量
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		entityClass=(Class<T>) type.getActualTypeArguments()[0];
		
		baseDao.setEntityManager(em , entityClass);
		paginationDao.SetEntityManager(em);
	}

	public final void create(T t) throws DaoException {
		baseDao.create(t);
	}
	
	public final T update(T t) throws DaoException, NoFieldChangedException{
		return baseDao.merge(t);
	}
	
	public final boolean delete(T t) throws DaoException, NoFieldChangedException{
		return baseDao.remove(t);
	}

	public final T findByKey(Object key) throws DaoException {
		return baseDao.findByKey(key);
	}

	public final boolean deleteByKey(Object key) throws DaoException {
		return baseDao.removeByKey(key);
	}
	
	public final List<T> getAll() throws DaoException{
		return baseDao.listAll();
	}

	public final PageObject<T> getList(int currPage) throws OutOfPageRangeException, DaoException {
		return paginationDao.queryForPaginationList(currPage , getEntityClass());
	}

	public final PageObject<T> getList(int currPage, int pageSize) throws OutOfPageRangeException, DaoException{
		return paginationDao.queryForPaginationList(currPage,pageSize , getEntityClass());
	}

}
