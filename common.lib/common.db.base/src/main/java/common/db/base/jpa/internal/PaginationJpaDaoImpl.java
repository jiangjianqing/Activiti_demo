package common.db.base.jpa.internal;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import common.db.base.DbUtil;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;

public class PaginationJpaDaoImpl extends ExtendJpaDaoImpl {

    public final <T>PageInfo<T> queryForPaginationList(int pageNum, int pageSize, String queryForListHQL,Object queryParams) throws OutOfPageRangeException, DaoException {
        int total = queryCount(queryForListHQL, queryParams);
        Page<T> page = new Page(pageNum,pageSize);
        page.setTotal(total);
        List list = queryForList(queryForListHQL, queryParams, page.getStartRow(),pageSize);
        page.addAll(list);
        
        PageInfo<T> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    public final <T>PageInfo<T> queryForPaginationList(int pageNum, int pageSize, String queryForListHQL) throws OutOfPageRangeException, DaoException{
        return this.queryForPaginationList(pageNum, pageSize, queryForListHQL,null);
    }

    public final<T>PageInfo<T> queryForPaginationList(int pageNum,int pageSize,Class<T> clazz,LinkedHashMap<String,String>conditions,Object queryParams,LinkedHashMap<String,String> orderBy) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(pageNum,clazz,null,conditions,queryParams,null);
    }

    public final<T>PageInfo<T>queryForPaginationList(int pageNum,int pageSize,Class<T> clazz,LinkedHashMap<String,String>conditions,Object queryParams) throws OutOfPageRangeException, DaoException{
            return queryForPaginationList(pageNum, pageSize,clazz,conditions,queryParams,null);
    }

    public final<T>PageInfo<T> queryForPaginationList(int pageNum,int pageSize,Class<T> clazz) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(pageNum, pageSize,clazz,null,null);
    }

    public final<T>PageInfo<T> queryForPaginationList(int pageNum,Class<T> clazz) throws OutOfPageRangeException, DaoException{
        return queryForPaginationList(pageNum,clazz,null,null,null);
    }

    public final<T>PageInfo<T>queryForPaginationList(int pageNum,Class<T> clazz,String[] fields,LinkedHashMap<String,String>conditions,Object queryParams) throws OutOfPageRangeException, DaoException{
            return queryForPaginationList(pageNum,clazz,fields,conditions,queryParams,null);
    }

    public final <T>PageInfo<T> queryForPaginationList(int pageNum, String queryForListHQL) throws OutOfPageRangeException, DaoException{
        return this.queryForPaginationList(pageNum,10,queryForListHQL,null);
    }

    public final<T>PageInfo<T> queryForPaginationList(int pageNum,Class<T> clazz,String[] fields,LinkedHashMap<String,String>conditions,Object queryParams,LinkedHashMap<String,String> orderBy) throws OutOfPageRangeException, DaoException{
    	String queryHQL = getSimpleQueryString(clazz,fields,conditions,orderBy);   
        return this.queryForPaginationList(pageNum,10, queryHQL, queryParams);
    }

}

