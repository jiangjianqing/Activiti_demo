package my.activiti.test;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/spring-*.xml")
@Transactional
//@TransactionConfiguration(transactionManager = "jdbcTransactionManager", defaultRollback = false)
public abstract class BaseSpringActivitiTester {
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
	
	@BeforeClass
	public static void BeforeClass(){
		
	}
}
