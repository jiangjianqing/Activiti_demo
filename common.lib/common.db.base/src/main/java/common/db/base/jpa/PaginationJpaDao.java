package common.db.base.jpa;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.db.base.DbUtil;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.page.PageObject;

public class PaginationJpaDao extends ExtendJpaDao {
	private static final Logger log = LoggerFactory.getLogger(PaginationJpaDao.class);
	/**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录，并且排序  实体必须使用符合JPA规范的注解进行配置
     * @param currentPage      当前页码
     * @param pageSize         单页记录数
     * @param preparedParameterArray new Object[]{13}
     * @param queryForListQHL
     * </br>HQL语句 <code>select o from User o where o.id >?
     * </br>或者from User o where o.id >?</code>
	 * @throws OutOfPageRangeException 
	 * @throws DaoException 
     * 
     */
    public final <T>PageObject<T> queryForPaginationList(int currentPage, int pageSize, String queryForListHQL,Object queryParams) throws OutOfPageRangeException, DaoException {
        int dataCount = queryCount(queryForListHQL, queryParams);
        PageObject<T> pageObject = new PageObject<T>(dataCount, currentPage,pageSize);
        pageObject.setPageList(queryForList(queryForListHQL, queryParams, pageObject.getStartPoint(),pageObject.getPageSize()));
        return pageObject;
    }
    /**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录，并且排序  实体必须使用符合JPA规范的注解进行配置
     * @param currentPage      当前页码
     * @param pageSize         单页记录数
     * @param queryForListQHL 
     * </br>HQL语句 <code>select o from User o where o.id > 10
     * </br>或者from User o where o.id >10</code>
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final <T>PageObject<T> queryForPaginationList(int currentPage, int pageSize, String queryForListHQL) throws OutOfPageRangeException, DaoException{
        return this.queryForPaginationList(currentPage, pageSize, queryForListHQL,null);
    }
    /**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录，并且排序  实体必须使用符合JPA规范的注解进行配置
     * @param currentPage      当前页码
     * @param pageSize         单页记录数
     * @param clazz            需要查询的实体类
     * @param conditions       put("userName","like")
     * @param parameterArray   new Object[]{"%zhang%"}
     * @param orderBy          put("id","asc")
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final<T>PageObject<T> queryForPaginationList(int currentPage,int pageSize,Class<T> clazz,LinkedHashMap<String,String>conditions,Object queryParams,LinkedHashMap<String,String> orderBy) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(currentPage,clazz,null,conditions,queryParams,null);
    }
    /**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录  实体必须使用符合JPA规范的注解进行配置
     * @param currentPage      当前页码
     * @param pageSize         单页记录数
     * @param clazz            需要查询的实体类
     * @param conditions       put("userName","like")
     * @param parameterArray   new Object[]{"%zhang%"}
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final<T>PageObject<T>queryForPaginationList(int currentPage,int pageSize,Class<T> clazz,LinkedHashMap<String,String>conditions,Object queryParams) throws OutOfPageRangeException, DaoException{
            return queryForPaginationList(currentPage, pageSize,clazz,conditions,queryParams,null);
    }
 
    /**
     * 根据实体 当前页码，单页记录数 Class 分页查询记录 实体必须使用符合JPA规范的注解进行配置
     * @param currentPage      当前页码
     * @param pageSize         单页记录数
     * @param clazz            需要查询的实体类
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final<T>PageObject<T> queryForPaginationList(int currentPage,int pageSize,Class<T> clazz) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(currentPage, pageSize,clazz,null,null);
    }
     
     
    /**
     * 根据实体 当前页码，单页记录数 Class 分页查询记录 实体必须使用符合JPA规范的注解进行配置 默认单页数据记录数为10
     * @param currentPage      当前页码
     * @param clazz            需要查询的实体类
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final<T>PageObject<T> queryForPaginationList(int currentPage,Class<T> clazz) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(currentPage,clazz,null,null,null);
    }
     
    /**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录  实体必须使用符合JPA规范的注解进行配置  默认单页数据记录数为10
     * @param currentPage      当前页码
     * @param clazz            需要查询的实体类
     * @param conditions       put("userName","like")
     * @param parameterArray   new Object[]{"%zhang%"}
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final<T>PageObject<T>queryForPaginationList(int currentPage,Class<T> clazz,String[] fields,LinkedHashMap<String,String>conditions,Object queryParams) throws OutOfPageRangeException, DaoException{
            return queryForPaginationList(currentPage,clazz,fields,conditions,queryParams,null);
    }
    /**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录，并且排序  实体必须使用符合JPA规范的注解进行配置 默认单页数据记录数为10
     * @param currentPage      当前页码
     * @param pageSize         单页记录数
     * @param preparedParameterArray new Object[]{13}
     * @param queryForListQHL
     * </br>HQL语句 <code>select o from User o where o.id >?
     * </br>或者from User o where o.id >?</code>
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     * 
     */
    protected final <T>PageObject<T> queryForPaginationList(int currentPage,String queryForListHQL,Object queryParams) throws OutOfPageRangeException, DaoException {
    	int dataCount = queryCount(queryForListHQL, queryParams);
        PageObject<T> pageObject = new PageObject<T>(dataCount, currentPage);
        pageObject.setPageList(queryForList(queryForListHQL, queryParams, pageObject.getStartPoint(),pageObject.getPageSize()));
        return pageObject;
    }
     
    /**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录，并且排序  实体必须使用符合JPA规范的注解进行配置  默认单页数据记录数为10
     * @param currentPage      当前页码
     * @param pageSize         单页记录数
     * @param queryForListQHL 
     * </br>HQL语句 <code>select o from User o where o.id > 10
     * </br>或者from User o where o.id >10</code>
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final <T>PageObject<T> queryForPaginationList(int currentPage, String queryForListHQL) throws OutOfPageRangeException, DaoException{
        return this.queryForPaginationList(currentPage,queryForListHQL,null);
    }
    /**
     * 根据实体 当前页码，单页记录数 Class,条件，参数  分页查询记录，并且排序  实体必须使用符合JPA规范的注解进行配置 默认单页数据记录数为10
     * @param currentPage      当前页码
     * @param clazz            需要查询的实体类
     * @param conditions       put("userName","like")
     * @param parameterArray   new Object[]{"%zhang%"}
     * @param orderBy          put("id","asc")
     * @throws OutOfPageRangeException 
     * @throws DaoException 
     */
    public final<T>PageObject<T> queryForPaginationList(int currentPage,Class<T> clazz,String[] fields,LinkedHashMap<String,String>conditions,Object queryParams,LinkedHashMap<String,String> orderBy) throws OutOfPageRangeException, DaoException{
    	String queryHQL = getSimpleQueryString(clazz,fields,conditions,orderBy);   
        return this.queryForPaginationList(currentPage, queryHQL, queryParams);
    }
    
}
