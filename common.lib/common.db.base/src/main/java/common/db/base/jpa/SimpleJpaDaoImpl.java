package common.db.base.jpa.sample;

import java.util.List;

import javax.persistence.EntityManager;

import common.db.base.exception.DaoException;
import common.db.base.exception.NoFieldChangedException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.BaseDao;
import common.db.base.jpa.PaginationJpaDao;
import common.db.base.page.PageObject;

public abstract class SimpleJpaDaoImpl<T> implements SimpleJpaDao<T> {

	protected BaseDao<T> baseDao;
	protected PaginationJpaDao paginationDao = new PaginationJpaDao();

	public void setEntityManager(EntityManager em){
		baseDao.setEntityManager(em);
		paginationDao.SetEntityManager(em);
	}

	public void create(T t) throws DaoException {
		baseDao.create(t);
	}
	
	public T update(T t) throws DaoException, NoFieldChangedException{
		return baseDao.merge(t);
	}

	public T findByKey(Object key) throws DaoException {
		return baseDao.findByKey(key);
	}

	public boolean removeByKey(Object key) throws DaoException {
		return baseDao.removeByKey(key);
	}
	
	public List<T> listAll() throws DaoException{
		return baseDao.listAll();
	}

	public PageObject<T> getList(int currPage,Class<T> clazz) throws OutOfPageRangeException, DaoException {
		return paginationDao.queryForPaginationList(currPage,clazz);
	}

	public PageObject<T> getList(int currPage, int pageSize,Class<T> clazz) throws OutOfPageRangeException, DaoException{
		return paginationDao.queryForPaginationList(currPage,pageSize,clazz);
	}

}
