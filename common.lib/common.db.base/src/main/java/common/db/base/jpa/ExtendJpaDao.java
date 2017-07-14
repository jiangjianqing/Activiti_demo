package common.db.base.jpa;

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

public abstract class ExtendJpaDao {
	private static final Logger log = LoggerFactory.getLogger(ExtendJpaDao.class);

    private EntityManager em;

    public void SetEntityManager(EntityManager em){
    	this.em=em;
    }
    
    public final EntityManager getEntityManager(){
    	return em;
    }

    /**
     * 根据实体Class 查询改实体所有的记录， 实体必须使用符合JPA规范的注解进行配置
     * 由于是查询所有记录，输出效率考虑 请慎重使用
     * @param clazz 需要查询的实体类
     * @throws DaoException 
     */
    public final <T> List<T>  queryForList(Class<T> clazz) throws DaoException{
        return this.queryForList(clazz, null, null);
    }
    /**
     * 更新实体,实体必须使用符合JPA规范的注解进行配置
     * @param HQL 语言  <code>update User o set o.userName = ?  where o.id = ?</code>
     * @param 预处理参数 <code>new Object{"张三",1} ; </code>
     * @throws DaoException 
     */
    public final int executeUpdate(String updateHQL,Object queryParams) throws DaoException{
    	try{
    		Query query = getEntityManager().createQuery(updateHQL);
            JpaUtil.setQueryParams(query, queryParams);
            return query.executeUpdate();
    	}
    	catch(EntityExistsException ex) 
        {
        	//getEntityManager().getTransaction().rollback();
    		ex.printStackTrace();
            throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        } 
        catch(IllegalArgumentException ex)
        {
        	//getEntityManager().getTransaction().rollback();
        	ex.printStackTrace();
            throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        } 
        catch(TransactionRequiredException ex) 
        {
        	//getEntityManager().getTransaction().rollback();
        	ex.printStackTrace();
            throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }
    	catch(Exception ex){
    		throw new DaoException("unknow DaoException"+ex.getClass().getSimpleName(),ex.getMessage());
    	}
    	
    }
    /**
     * 更新实体,实体必须使用符合JPA规范的注解进行配置
     * @param HQL 语言  <code>update User o set o.userName = ?  where o.id = ?</code>
     * <code>delete  User o  where o.id = ?</code>
     * @throws DaoException 
     * 	
     */
    public final int executeUpdate(String updateHQL) throws DaoException{
            return this.executeUpdate(updateHQL, null);
    }
    
    /**
     * 根据实体Class,条件，参数  查询记录， 实体必须使用符合JPA规范的注解进行配置
     * @param clazz
     * @param fields
     * @param condition
     * @param queryParams
     * @param orderBy
     * @return
     * @throws DaoException 
     */
    public final <T> List <T> queryForList(Class<T> clazz,String[] fields,String condition,Object queryParams,LinkedHashMap<String,String> orderBy) throws DaoException{
        String queryHQL=getQueryString(clazz,fields,condition,JpaUtil.buildOrderby(orderBy));
    	return this.queryForList(queryHQL, queryParams);
    }
    /**
     * 根据实体Class,条件，参数  查询记录， 实体必须使用符合JPA规范的注解进行配置
     * @param clazz            需要查询的实体类
     * @param conditions       put("userName","like")
     * @param queryParams   new Object[]{"%zhang%"}
     * @throws DaoException 
     */
    public final <T> List <T> queryForList(Class<T> clazz,LinkedHashMap<String,String> conditions,Object queryParams) throws DaoException{
            return this.queryForList(clazz,null, conditions, queryParams, null);
    }
    /**
     * 根据实体Class,条件，参数  查询记录，可以对相应的记录进行排序 实体必须使用符合JPA规范的注解进行配置
     * @param clazz            需要查询的实体类
     * @param conditions       put("userName","like")
     * @param parameterArray   new Object[]{"%zhang%"}
     * @param order            put("id","asc")
     * @throws DaoException 
     */
    public final <T> List <T> queryForList(Class<T> clazz,String[] fields,LinkedHashMap<String,String> conditions,Object queryParams,LinkedHashMap<String,String>orderBy) throws DaoException{
    	String queryHQL = getSimpleQueryString(clazz,fields,conditions,orderBy);
        return queryForList(queryHQL,queryParams);
    }
    /**
     * 使用HQL 进行实体的查询 实体必须使用符合JPA规范的注解进行配置
     * @param queryHQL  select o from User o  where o.id = 36
     * @throws DaoException 
     */
    public final <T> List <T> queryForList(String queryHQL) throws DaoException{
        return this.queryForList(queryHQL, null);
    }
    /**
     * 根据实体Class 查询改实体所有的记录，可以对相应的记录进行排序 实体必须使用符合JPA规范的注解进行配置
     * 由于是查询所有记录，输出效率考虑 请慎重使用
     * @param clazz            需要查询的实体类
     * @param order            put("id","asc")
     * @throws DaoException 
     */
    public final <T> List<T> queryForList(Class<T>clazz,LinkedHashMap<String,String>orderBy) throws DaoException{
        return this.queryForList(clazz,null, "", null, orderBy);
    }
    /**
     * 使用HQL 进行实体的查询 实体必须使用符合JPA规范的注解进行配置
     * @param queryHQL  select o from User o  where o.id =?
     * @param parameterArray new Object[]{36}
     * @throws DaoException 
     */
    public final <T> List <T>  queryForList(String queryHQL,Object queryParams) throws DaoException{
            return this.queryForList(queryHQL, queryParams, -1, -1);
    }
    /**
     * 使用HQL查询整数(任何可以返回整数的HQL语句) 实体必须使用符合JPA规范的注解进行配置
     * @param queryHQL  select o.id from User o  where o.id =36
     * @throws DaoException 
     */
    public final int queryForInt(String queryIntHQL) throws DaoException{
        return this.queryForInt(queryIntHQL, null);
    }
    /**
     * 使用HQL查询整数(任何可以返回整数的HQL语句) 实体必须使用符合JPA规范的注解进行配置
     * @param queryHQL  select o.id from User o  where o.id =36
     * @param parameterArray new Object[]{36}
     * @throws DaoException 
     */
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
    
    /**
     * 查询数据总量
     * 20160128：这里还要优化查询性能
     * @param queryForListHQL
     * @param preparedParameterArray
     * @return
     * @throws DaoException 
     */
    public final int queryCount(String queryForListHQL,Object queryParams) throws DaoException {
         
        StringBuilder countHQLBuilder = new StringBuilder(" select count(*) ");
        try {
            countHQLBuilder.append(queryForListHQL.substring(queryForListHQL.toLowerCase().indexOf("from")));
        } catch (RuntimeException ex) {
        	System.out.println("queryCount:HQL 语句存在问题： "+queryForListHQL);
        	ex.printStackTrace();
        	throw  ex;
        	//org.springframework.jdbc.BadSqlGrammarException这里不需要
            //throw new BadSqlGrammarException("SQL  :  ", queryForListHQL, null);
        }
        return queryForInt(countHQLBuilder.toString(), queryParams);
    }
    /**
     * 查询数据总量
     * @param clazz
     * @param condition
     * @param queryParams
     * @return
     * @throws DaoException 
     */
    public final <T> int queryCount(Class<T> clazz,String condition,Object queryParams) throws DaoException {
    	condition=DbUtil.toSqlWhere(condition);
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(");
        sqlBuilder.append(JpaUtil.getPkField(clazz)).append(") FROM ")
        	.append(JpaUtil.getEntityName(clazz)).append(" ").append(JpaUtil.getDefaultAlias()).append(" ")
        	.append(condition);

        return queryForInt(sqlBuilder.toString(), queryParams);
    }

    /**
     * 支持分页查询
     * @param queryHQL
     * @param queryParams
     * @param firstResult
     * @param maxResult
     * @return
     * @throws DaoException 
     */
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
    
    /**
     * 获取查询字符串
     * @param clazz
     * @param fields
     * @param condition
     * @param orderBy
     * @return
     */
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
    
    /**
     * 支持最简单的条件语句生成（and）
     * @param conditions
     * @return
     */
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
