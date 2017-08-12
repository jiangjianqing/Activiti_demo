package my.activiti.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/spring-*.xml")
@Transactional
//@TransactionConfiguration(transactionManager = "jdbcTransactionManager", defaultRollback = false)
public abstract class BaseSpringActivitiTester {
	protected final Logger logger = LoggerFactory
			.getLogger(getClass());
	
	@Autowired
	protected IdentityService identityService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected FormService formService;
	@Autowired
	protected ProcessEngine processEngine;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Boolean isCreated = false;
	
	@Before
	public void before() throws DataAccessException, IOException{
		if (!isCreated){
			isCreated = true;
			System.out.println("--------准备创建 集成 activiti5 所需的视图-----------");
			System.out.println(jdbcTemplate.toString());
			//System.out.println(loadUpgradeSql());
			try{
				jdbcTemplate.batchUpdate(loadUpgradeSql());
			}catch (Exception e) {
				logger.error("集成 activiti5 时发生错误:"+e);
			}
			
			
		}

	}
	
	private String loadUpgradeSql() throws IOException{
		try(InputStream in = this.getClass().getResourceAsStream("/sql_upgrade/integrate_activiti5_identity.sql");){
			try(ByteArrayOutputStream bos = new ByteArrayOutputStream( in.available())){
			
				int buf_size = 1024;
				byte[] buffer = new byte[buf_size];
				int len = 0;
				while (-1 != (len = in.read(buffer, 0, buf_size))) {
					bos.write(buffer, 0, len);
				}
				return bos.toString();
			}
		}

	}
	
	
}
