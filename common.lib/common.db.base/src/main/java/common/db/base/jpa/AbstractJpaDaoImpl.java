package common.db.base.jpa;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageInfo;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.internal.BaseJpaDaoImpl;
import common.db.base.jpa.internal.PaginationJpaDaoImpl;


/**
 * 后续所有的JPA DAO 类都需要从这里继承
 * @author jjq
 *
 * @param <T>
 */

public abstract class AbstractJpaDaoImpl<T,K> implements AbstractJpaDao<T,K> {
	
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

	public int insert(T t) {
		int ret=-1;
		try {
			baseDao.create(t);
			ret=1;
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public int updateByPrimaryKey(T t){
		int ret=-1;
		try {
			baseDao.merge(t);
			ret = 1;
		}catch(DaoException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public T selectByPrimaryKey(K key) {
		try {
			return baseDao.findByKey(key);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int deleteByPrimaryKey(K key) {
		int ret=-1;
		try {
			baseDao.removeByKey(key);
			ret = 1;
		}catch(DaoException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<T> getList(){
		try {
			return baseDao.listAll();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public PageInfo<T> getPageList(int currPage) {
		try {
			return paginationDao.queryForPaginationList(currPage , getEntityClazz());
		} catch (OutOfPageRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public PageInfo<T> getPageList(int currPage, int pageSize){
		try {
			return paginationDao.queryForPaginationList(currPage,pageSize , getEntityClazz());
		} catch (OutOfPageRangeException | DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
