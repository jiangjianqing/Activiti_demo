package common.db.base;


import java.util.List;

import javax.persistence.EntityManager;

import com.github.pagehelper.PageInfo;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;

/**
 * 所有Dao的基类，要考虑兼容JPA和mybatis
 * @author jjq
 *
 * @param <T>
 */
public interface AbstractDao<T,K> {
	public static int DEFAULT_PAGE_SIZE=10;
	void create(T t) throws DaoException;
	T update(T t) throws DaoException;
	T findByKey(K key) throws DaoException;
	boolean deleteByKey(K key) throws DaoException;
	List<T> getList() throws DaoException;
	
	PageInfo<T> getPageList(int currPage) throws OutOfPageRangeException, DaoException;
	PageInfo<T> getPageList(int currPage,int pageSize) throws OutOfPageRangeException, DaoException;
}
