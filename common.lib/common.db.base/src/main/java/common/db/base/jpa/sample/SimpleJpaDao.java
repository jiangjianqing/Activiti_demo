package common.db.base.jpa.sample;


import java.util.List;

import javax.persistence.EntityManager;

import common.db.base.exception.DaoException;
import common.db.base.exception.NoFieldChangedException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.page.PageObject;

public interface SimpleJpaDao<T> {
	void setEntityManager(EntityManager em);
	void create(T t) throws DaoException;
	T update(T t) throws DaoException,NoFieldChangedException;
	T findByKey(Object key) throws DaoException;
	boolean removeByKey(Object key) throws DaoException;
	List<T> listAll() throws DaoException;
	
	PageObject<T> getList(int currPage) throws OutOfPageRangeException, DaoException;
	PageObject<T> getList(int currPage,int pageSize) throws OutOfPageRangeException, DaoException;
}
