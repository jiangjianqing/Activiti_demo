package common.db.base.jpa.internal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.db.base.DbUtil;
import common.db.base.exception.DaoException;

public abstract class ExtendJpaDaoImpl {
	protected final Logger logger = LoggerFactory
			.getLogger(getClass());

    private EntityManager em;

    public void SetEntityManager(EntityManager em){
    	this.em=em;
    }

    public final EntityManager getEntityManager(){
    	return em;
    }

    public final <T> List<T>  queryForList(Class<T> clazz) throws DaoException{
        return this.queryForList(clazz, null, null);
    }

    public final int executeUpdate(String updateHQL,Object queryParams) throws DaoException{
    	try{
    		Query query = getEntityManager().createQuery(updateHQL);
            JpaUtil.setQueryParams(query, queryParams);
            return query.executeUpdate();
    	}
    	catch(EntityExistsException ex) 
        {

    		ex.printStackTrace();
            throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        } 
        catch(IllegalArgumentException ex)
        {

        	ex.printStackTrace();
            throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        } 
        catch(TransactionRequiredException ex) 
        {

        	ex.printStackTrace();
            throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }
    	catch(Exception ex){
    		throw new DaoException("unknow DaoException"+ex.getClass().getSimpleName(),ex.getMessage());
    	}

    }

    public final int executeUpdate(String updateHQL) throws DaoException{
            return this.executeUpdate(updateHQL, null);
    }

    public final <T> List <T> queryForList(Class<T> clazz,String[] fields,String condition,Object queryParams,LinkedHashMap<String,String> orderBy) throws DaoException{
        String queryHQL=getQueryString(clazz,fields,condition,JpaUtil.buildOrderby(orderBy));
    	return this.queryForList(queryHQL, queryParams);
    }

    public final <T> List <T> queryForList(Class<T> clazz,LinkedHashMap<String,String> conditions,Object queryParams) throws DaoException{
            return this.queryForList(clazz,null, conditions, queryParams, null);
    }

    public final <T> List <T> queryForList(Class<T> clazz,String[] fields,LinkedHashMap<String,String> conditions,Object queryParams,LinkedHashMap<String,String>orderBy) throws DaoException{
    	String queryHQL = getSimpleQueryString(clazz,fields,conditions,orderBy);
        return queryForList(queryHQL,queryParams);
    }

    public final <T> List <T> queryForList(String queryHQL) throws DaoException{
        return this.queryForList(queryHQL, null);
    }

    public final <T> List<T> queryForList(Class<T>clazz,LinkedHashMap<String,String>orderBy) throws DaoException{
        return this.queryForList(clazz,null, "", null, orderBy);
    }
    
    public final <T> List<T> queryForList(Query query,Object queryParams) throws DaoException{
        return this.queryForList(query, queryParams, -1, -1);
    }

    public final <T> List <T>  queryForList(String queryHQL,Object queryParams) throws DaoException{
            return this.queryForList(queryHQL, queryParams, -1, -1);
    }

    public final int queryForInt(String queryIntHQL) throws DaoException{
        return this.queryForInt(queryIntHQL, null);
    }

    public final int queryForInt(String queryIntHQL,Object queryParams) throws DaoException{
    	try{
	        Query query = getEntityManager().createQuery(queryIntHQL);
	        JpaUtil.setQueryParams(query, queryParams);
	        int result = Integer.parseInt(query.getSingleResult().toString());
	        return result;
    	}
    	catch(Exception ex){
    		throw new DaoException("unknow DaoException"+ex.getClass().getSimpleName(),ex.getMessage());
    	}
    }

    public final int queryCount(String queryForListHQL,Object queryParams) throws DaoException {

        StringBuilder countHQLBuilder = new StringBuilder(" select count(*) ");
        try {
            countHQLBuilder.append(queryForListHQL.substring(queryForListHQL.toLowerCase().indexOf("from")));
        } catch (RuntimeException ex) {
        	System.out.println("queryCount:HQL 语句存在问题： "+queryForListHQL);
        	ex.printStackTrace();
        	throw  ex;

        }
        return queryForInt(countHQLBuilder.toString(), queryParams);
    }

    public final <T> int queryCount(Class<T> clazz,String condition,Object queryParams) throws DaoException {
    	condition=DbUtil.toSqlWhere(condition);
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(");
        sqlBuilder.append(JpaUtil.getPkField(clazz)).append(") FROM ")
        	.append(JpaUtil.getEntityName(clazz)).append(" ").append(JpaUtil.getDefaultAlias()).append(" ")
        	.append(condition);

        return queryForInt(sqlBuilder.toString(), queryParams);
    }

    public final List queryForList(String queryHQL,Object queryParams,int firstResult,int maxResult) throws DaoException{
    	try{
            Query query = getEntityManager().createQuery(queryHQL);
            JpaUtil.setQueryParams(query,queryParams);
            if(firstResult>=0)
                query.setFirstResult(firstResult);
            if(maxResult>0)
                query.setMaxResults(maxResult);
            return query.getResultList();

    	}
    	catch(Exception ex){
    		throw new DaoException("unknow DaoException:"+ex.getClass().getSimpleName(),ex.getMessage());
    	}
    }
    
    public final List queryForList(Query query,Object queryParams,int firstResult,int maxResult) throws DaoException{
    	try{
            JpaUtil.setQueryParams(query,queryParams);
            if(firstResult>=0)
                query.setFirstResult(firstResult);
            if(maxResult>0)
                query.setMaxResults(maxResult);
            return query.getResultList();

    	}
    	catch(Exception ex){
    		throw new DaoException("unknow DaoException:"+ex.getClass().getSimpleName(),ex.getMessage());
    	}
    }

    public final <T> String getQueryString(Class<T> clazz,String[] fields,String condition,String orderBy){
    	condition=DbUtil.toSqlWhere(condition);
        StringBuilder queryBuilder = new StringBuilder(JpaUtil.buildSelect(clazz, fields, null));
        queryBuilder.append(" ").append(condition).append(" ").append(orderBy);
        return queryBuilder.toString();
    }

    public final<T> String getSimpleQueryString(Class<T> clazz,String[] fields,LinkedHashMap<String,String>conditions,LinkedHashMap<String,String> orderBy){
    	String strCondition=getSimpleCondtitons(conditions);
        String strOrderBy=JpaUtil.buildOrderby(orderBy);
    	return getQueryString(clazz,fields,strCondition,strOrderBy);       	
    }

    protected final String getSimpleCondtitons(LinkedHashMap<String,String> conditions){
            if(conditions==null||conditions.size()==0){
                return " ";
            }
            StringBuilder conditionsBuilder = new StringBuilder("  where 1=1  ");
            Set<String>conditonFields  = conditions.keySet();
            for(String conditionField : conditonFields){
            	conditionsBuilder.append(" and "+JpaUtil.getDefaultAlias()+".");
                conditionsBuilder.append(conditionField);
                conditionsBuilder.append(" ");
                conditionsBuilder.append(conditions.get(conditionField));
                conditionsBuilder.append("  ?  ");
            }
        return conditionsBuilder.toString();
    }

}

