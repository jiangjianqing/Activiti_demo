package my.activiti.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import my.activiti.bean.MyBean;

public class TestSpringActivitiTemplate extends BaseSpringActivitiTester  {

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Map<String,Object> variables=new HashMap<String,Object>();
		String name="Henry yan";
		variables.put("name", name);
		identityService.setAuthenticatedUserId("henryyan");
		String businessKey="9999";
		ProcessInstance processInstance=runtimeService.startProcessInstanceByKey("myProcess", businessKey,variables);
		assertEquals("henryyan",runtimeService.getVariable(processInstance.getId(), "authenticatedUserIdForTest"));
		assertEquals(name+", added by print(String name)",runtimeService.getVariable(processInstance.getId(), "returnValue"));
		assertEquals(businessKey,runtimeService.getVariable(processInstance.getId(), "businessKey"));
		Task task=taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		String setByTask=(String)taskService.getVariable(task.getId(), "setByTask");
		System.out.println("测试通过，setByTask:"+setByTask);
	}

}
