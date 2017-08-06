package common.db.base.test;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractJpaTestCase {
	protected static EntityManager em;
	
	/**
	 * 必须在子类的@BeforeClass中调用该方法,初始化JPA环境
	 * @param persistenceUnitName
	 */
	protected static void setPersistenceUnitName(String persistenceUnitName) {
		PersistenceProvider provider = new HibernatePersistenceProvider();
		//20170805 Persistence.createEntityManagerFactory(  this is deprecated
		EntityManagerFactory emf = provider.createEntityManagerFactory(persistenceUnitName,
				System.getProperties());
		em = emf.createEntityManager();

		// em.unwrap(java.sql.Connection.class);//报错Hibernate cannot unwrap
		// interface java.sql.Connection

		// 方式1
		// Connection cnn = em.unwrap(SessionImpl.class).connection();成功
		// 方式2
		//20170806  .getConnectionProvider方法在Hibernate5.2上已经移除
		//cnn = sessionFactory.getConnectionProvider().getConnection();
		Session session = (Session) em.getDelegate();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		
		Connection cnn = em.unwrap(SessionImpl.class).connection();;
		
		System.out.println("**************getConnection**********");
		System.out.println(cnn);
		
	};


	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		em.close();
	}

	@Before
	public void setUp() throws Exception {
		//在这里初始化Dao
		
		em.getTransaction().begin();
	}

	@After
	public void tearDown() throws Exception {
		em.getTransaction().commit();
	}
}
