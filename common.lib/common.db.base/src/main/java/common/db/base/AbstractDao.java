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
	int insert(T t);
	int updateByPrimaryKey(T t);
	T selectByPrimaryKey(K key);
	int deleteByPrimaryKey(K key);
	List<T> getList();
	
	PageInfo<T> getPageList(int currPage);
	PageInfo<T> getPageList(int currPage,int pageSize);
}
