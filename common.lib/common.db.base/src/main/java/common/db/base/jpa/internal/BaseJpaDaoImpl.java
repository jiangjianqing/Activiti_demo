package common.db.base.jpa.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import common.db.base.exception.DaoException;

public class BaseJpaDaoImpl<T> {

	/*
	 * JPA一些注意事项：
	 * 当在数据库中没有找到记录时，getReference()和find()是有区别的，find()方法会返回null，而getReference() 方法会抛出javax.persistence.EntityNotFoundException例外，另外getReference()方法不保证 entity Bean已被初始化。如果传递进getReference()或find()方法的参数不是实体Bean，都会引发 IllegalArgumentException例外
	 * 
	 * 在处理大量实体的时候，如果你不把已经处理过的实体从EntityManager中分离出来，将会消耗你大量的内存。调用EntityManager 的clear()方法后，所有正在被管理的实体将会从持久化内容中分离出来。有一点需要说明下，在事务没有提交前（事务默认在调用堆栈的最后提交，如：方 法的返回），如果调用clear()方法，之前对实体所作的任何改变将会掉失，所以建议你在调用clear()方法之前先调用flush()方法保存更 改。
	 * 
	 * 改变实体管理器的Flush模式 —— setFlushMode(),Flush模式有2种类型：AUTO and COMMIT。你可以改变他的值，如下：entityManager.setFlushMode(FlushModeType.COMMIT);
	FlushModeType.AUTO（缺省模式）：刷新在查询语句执行前(除了find()和getreference()查询)或事务提交时才发生，使用场合：在 大量更新数据的过程中没有任何查询语句(除了find()和getreference()查询)的执行。
	FlushModeType.COMMIT(性能好)：刷新只有在事务提交时才发生，使用场合：在大量更新数据的过程中存在查询语句(除了find()和 getreference()查询)的执行。
	JDBC 驱动跟数据库交互的次数。JDBC 性能最大的增进是减少JDBC 驱动与数据库之间的网络通讯。FlushModeType.COMMIT模式使更新只在一次的网络交互中完成，而FlushModeType.AUTO 模式可能需要多次交互（触发了多少次Flush 就产生了多少次网络交互）
	 * 
	 * update —— 分2种情况
情况1：当实体正在被容器管理时，你可以调用实体的set方法对数据进行修改，在容器决定flush时（这个由container自行判断），更新的数据 才会同步到数据库，而不是在调用了set方法对数据进行修改后马上同步到数据库。如果你希望修改后的数据马上同步到数据库，你可以调用EntityManager.flush()方法。 
情况2：（常用）在实体Bean已经脱离了EntityManager的管理时，你调用实体的set方法对数据进行修改是无法同步更改到数据库的。你必须调用 EntityManager.merge()方法。调用之后，在容器决定flush时（这个由container自行判断），更新的数据才会同步到数据 库。如果你希望修改后的数据马上同步到数据库，你可以调用EntityManager.flush()方法。
	 *
	 *	Person person = em.find(Person.class, 2);
		。。。
		if (em.contains(person)){
			//正在被持久化内容管理
		}else{
			//已经不受持久化内容管理
		}
	*如果此时person 对应的记录在数据库中已经发生了改变，可以通过refresh()方法得到最新数据。
		em.refresh (person);
		当然你再次调用find()或getReference()方法也可以得到 最新数据，但这种做法并不优雅。
	*	
	*（强烈不建议使用）获取持久化实现者的引用 —— getDelegate(),比如：HibernateEntityManager manager = (HibernateEntityManager)em.getDelegate();
	获得对Hibernate的引用后，可以直接面对Hibernate进行编码
	 */
	
	/*获取session、connection的方式
		//em.unwrap(java.sql.Connection.class);//会报错Hibernate cannot unwrap interface java.sql.Connection
		
		//有效方式1
		//Connection cnn = em.unwrap(SessionImpl.class).connection();成功
		//有效方式2
		Session session = (Session) em.getDelegate();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		Connection cnn=null;
		try {
			cnn = sessionFactory.getConnectionProvider().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("**************getConnection**********");
		System.out.println(cnn); 
	 
	 */
	
	protected EntityManager em;
	
	private Class<T> entityClazz;
	
	//2016-01-25屏蔽：该申明只有在直接继承时才有效
	//@SuppressWarnings("unchecked")
	//protected Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
	//2016-01-25将上述属性改为get方法，使其更为通用
	//20160126 重要:BaseDaoImpl中的getEntityClass需要该类被继承之后才能有效，所以这里进行了一次继承
	//原因为泛型擦拭法使得BaseDaoImpl无法获取自己的Generic Type类型，一定要在父类后面带上泛型，否则该方法就会无效
	@SuppressWarnings("unchecked")
	protected Class <T> getEntityClass (){
		return this.entityClazz;
			/*
		 Class<T> entityClass = null;
	      Type t = getClass().getGenericSuperclass();
	      if(t instanceof ParameterizedType){
	          Type[] p = ((ParameterizedType)t).getActualTypeArguments();
	         entityClass= (Class<T>)p[0];
	      }
	      return entityClass;
	      */
	}
	
	public void setEntityManager(EntityManager em) {
		this.em = em;
		//取得T的类型变量
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.entityClazz=(Class<T>) type.getActualTypeArguments()[0];
	}
	
	public void setEntityManager(EntityManager em , Class<T> clazz) {
		this.em = em;
		this.entityClazz = clazz;
	}
	
	public EntityManager getEntityManager(){
		return this.em;
	}

	public int removeAll() throws DaoException {
		try {
			Query query = em.createQuery("delete from "+getEntityClass().getName());
			return query.executeUpdate();
		} catch (Exception ex) {
			throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
		}
	}

	public List<T> listAll() throws DaoException {		
		try {
			Query query = em.createQuery(" from "+getEntityClass().getName());
			return query.getResultList();
		} catch (Exception ex) {
			throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
		}
	}
	
	public T findByKey(Object key) throws DaoException{
		try {
			System.out.println("findByKey.key=");
			System.out.println(key);
			return  (T) em.find(getEntityClass(), key);
		} catch (Exception ex) {
			throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
		}
	}

	public void create(T object) throws DaoException {
		try {
			em.persist(object);
		} catch (Exception ex) {
			throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
		}
	}

	public T update(T object) throws DaoException {
		try {
			return em.merge(object);
		} catch (Exception ex) {
			throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
		}
	}

	public boolean removeByKey(Object key) throws DaoException {
		T t=findByKey(key);
		if(t!=null){
			return remove(t);
		}else
			return false;
	}
	
	/**
     * 不需确定新建实体是否已经存在
	 * @throws DaoException 
     */
    public T merge(T t) throws DaoException {
        try {
            t = em.contains(t) ? t : em.merge(t);
            return t;
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }
    }
 
    /**
     * 可确定为新建实体
     * @throws DaoException 
     */
    public T persist(T t) throws DaoException {
        try {
        	em.persist(t);
            return t;
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }
    }
 
    /**
     * 可确定为新建实体 成功返回true 失败返回false
     * @throws DaoException 
     */
    public void persist(Collection<T> ts) throws DaoException {
    	for (T t : ts) {
        	persist(t);
        }
    }
 
    /**
     * 若数据库中已有此记录，置为托管状态
     * @throws DaoException 
     */
    public Collection<T> merge(Collection<T> ts) throws DaoException {
        Collection<T> collection = new HashSet<T>();
        for (T t : ts) {
            collection.add(merge(t));
        }
        return collection;
    }
 
    /**
     * 删除
     * @throws DaoException 
     */
    public boolean remove(T t) throws  DaoException {
        try {
            if (em.contains(t)) {
            	em.remove(t);
            } else {
                Object id = em.getEntityManagerFactory()
                        .getPersistenceUnitUtil().getIdentifier(t);
                em.remove(em.find(t.getClass(), id));
            }
            return true;
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }
    }
 
    /**
     * 批量删除 传入集合
     * @throws DaoException 
     */
    public void remove(Collection<T> ts) throws DaoException {
    	for (T t : ts) {
            remove(t);
        }
    }
 
    /**
     * 若数据库中存在，返回最新状态；否则，返回空
     *
     * @param clazz
     * @param t
     * @param id
     * @return
     * @throws DaoException 
     */
    protected T refresh(T t) throws  DaoException {
        Object id = em.getEntityManagerFactory()
                .getPersistenceUnitUtil().getIdentifier(t);
        try{
        	if (id==null) {
                return null;
            }
            if (em.contains(t)) {
            	em.refresh(t);
                return t;
            } else {
                return em.find(this.getEntityClass(), id);
            }
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }
        
    }
 
    /**
     * 清除一级缓存
     * @throws DaoException 
     */
    public void clear() throws DaoException {
    	try{
    		em.clear();
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }
    	
    }
 
    /**
     * 将对象置为游离状态
     * @throws DaoException 
     */
    public void detach(T t) throws DaoException {
    	try{
    		em.detach(t);
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }    	
    }
 
    /**
     * 将对象置为游离状态
     * @throws DaoException 
     */
    public void detach(Collection<T> ts) throws DaoException {
        for (T t : ts) {
            detach(t);
        }
    }
 
    /**
     * 判断实体是否处于托管状态
     * @throws DaoException 
     */
    public boolean isManaged(T t) throws DaoException {
    	try{
    		return em.contains(t);
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }   
        
    }
 
    /**
     * 同步jpa容器和数据库
     * @throws DaoException 
     */
    public void flush() throws DaoException {    	
    	try{
    		em.flush();
        } catch (Exception ex) {
        	throw new DaoException(ex.getClass().getSimpleName(),ex.getMessage());
        }  
    }

    /**
     * 支持命名查询
     * @param name
     * @return
     * @throws DaoException
     */
	public Query createNamedQuery(String name) throws DaoException {
		//放弃使用TypedQuery,因为使用Query更灵活
		return em.createNamedQuery(name/*, getEntityClass()*/);
	}

}
