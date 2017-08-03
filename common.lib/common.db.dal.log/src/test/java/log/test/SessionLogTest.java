package log.test;

import static org.junit.Assert.*;

import java.io.Console;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import common.db.base.exception.DaoException;
import common.db.modal.log.SessionLog;
import common.db.repository.jpa.log.SessionLogDao;
import common.db.repository.jpa.log.impl.SessionLogDaoImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessionLogTest extends common.db.base.test.AbstractJpaTestCase {
	
	private static SessionLogDao sessionLogDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setPersistenceUnitName("log");
		sessionLogDao = new SessionLogDaoImpl();
		sessionLogDao.setEntityManager(em);
	}

	/*
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		super.tearDownAfterClass();
	}*/


	@Test
	public void test() throws DaoException {
		SessionLog data = new SessionLog();
		data.setCreateTime(new Date());
		data.setIpAddr("192.168.1.1");
		data.setHostName("pc-123");
		sessionLogDao.create(data);
		assertNotNull(data.getId());
		System.out.println(String.format("new SessionLog's id = %s",data.getId()));
	}

}
