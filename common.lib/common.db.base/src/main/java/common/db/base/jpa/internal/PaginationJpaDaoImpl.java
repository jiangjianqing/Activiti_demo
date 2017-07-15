package common.db.base.jpa.internal;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.db.base.DbUtil;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.page.PageObject;

public class PaginationJpaDaoImpl extends ExtendJpaDaoImpl {

    public final <T>PageObject<T> queryForPaginationList(int currentPage, int pageSize, String queryForListHQL,Object queryParams) throws OutOfPageRangeException, DaoException {
        int dataCount = queryCount(queryForListHQL, queryParams);
        PageObject<T> pageObject = new PageObject<T>(dataCount, currentPage,pageSize);
        pageObject.setPageList(queryForList(queryForListHQL, queryParams, pageObject.getStartPoint(),pageObject.getPageSize()));
        return pageObject;
    }

    public final <T>PageObject<T> queryForPaginationList(int currentPage, int pageSize, String queryForListHQL) throws OutOfPageRangeException, DaoException{
        return this.queryForPaginationList(currentPage, pageSize, queryForListHQL,null);
    }

    public final<T>PageObject<T> queryForPaginationList(int currentPage,int pageSize,Class<T> clazz,LinkedHashMap<String,String>conditions,Object queryParams,LinkedHashMap<String,String> orderBy) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(currentPage,clazz,null,conditions,queryParams,null);
    }

    public final<T>PageObject<T>queryForPaginationList(int currentPage,int pageSize,Class<T> clazz,LinkedHashMap<String,String>conditions,Object queryParams) throws OutOfPageRangeException, DaoException{
            return queryForPaginationList(currentPage, pageSize,clazz,conditions,queryParams,null);
    }

    public final<T>PageObject<T> queryForPaginationList(int currentPage,int pageSize,Class<T> clazz) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(currentPage, pageSize,clazz,null,null);
    }

    public final<T>PageObject<T> queryForPaginationList(int currentPage,Class<T> clazz) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(currentPage,clazz,null,null,null);
    }

    public final<T>PageObject<T>queryForPaginationList(int currentPage,Class<T> clazz,String[] fields,LinkedHashMap<String,String>conditions,Object queryParams) throws OutOfPageRangeException, DaoException{
            return queryForPaginationList(currentPage,clazz,fields,conditions,queryParams,null);
    }

    protected final <T>PageObject<T> queryForPaginationList(int currentPage,String queryForListHQL,Object queryParams) throws OutOfPageRangeException, DaoException {
    	int dataCount = queryCount(queryForListHQL, queryParams);
        PageObject<T> pageObject = new PageObject<T>(dataCount, currentPage);
        pageObject.setPageList(queryForList(queryForListHQL, queryParams, pageObject.getStartPoint(),pageObject.getPageSize()));
        return pageObject;
    }

    public final <T>PageObject<T> queryForPaginationList(int currentPage, String queryForListHQL) throws OutOfPageRangeException, DaoException{
        return this.queryForPaginationList(currentPage,queryForListHQL,null);
    }

    public final<T>PageObject<T> queryForPaginationList(int currentPage,Class<T> clazz,String[] fields,LinkedHashMap<String,String>conditions,Object queryParams,LinkedHashMap<String,String> orderBy) throws OutOfPageRangeException, DaoException{
    	String queryHQL = getSimpleQueryString(clazz,fields,conditions,orderBy);   
        return this.queryForPaginationList(currentPage, queryHQL, queryParams);
    }

}

