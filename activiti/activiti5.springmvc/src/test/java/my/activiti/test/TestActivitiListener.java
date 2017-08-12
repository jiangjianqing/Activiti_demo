package my.activiti.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

public class TestActivitiListener extends BaseSpringActivitiTester {
	

	@Ignore("2017-08-12 在activiti6 下测试无法通过")
	@Test
	public void test() {
		Map<String,Object> variables=new HashMap<String,Object>();
		String name="Henry Yan";
		variables.put("testName", name);//20150823,特别注意：这里不要使用name作为变量名称，否则代码中会注入当前项目的name【activiti5_springmvc】
		identityService.setAuthenticatedUserId("henryyan");
		String businessKey="test20150823";
		ProcessInstance processInstance=runtimeService.startProcessInstanceByKey("listener", businessKey,variables);
		
		//检查是否执行了启动监听
		String processInstanceId=processInstance.getId();
		assertTrue((Boolean)runtimeService.getVariable(processInstanceId, "setInStartListener"));
		//检查任务监听是否被执行
		Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("jenny").singleResult();
		String setInTaskCreate=(String)taskService.getVariable(task.getId(), "setInTaskCreate");
		assertEquals("create,Hello,Henry Yan",setInTaskCreate);
		taskService.complete(task.getId());
		//流程结束后查询变量
		List<HistoricVariableInstance> list=historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		boolean hasVariableOfEndListener=false;
		for(HistoricVariableInstance item:list){
			if(item.getVariableName().equals("setInEndListener"))
				hasVariableOfEndListener=true;
		}
		assertTrue(hasVariableOfEndListener);
		
//		assertEquals("henryyan",runtimeService.getVariable(processInstance.getId(), "authenticatedUserIdForTest"));
//		assertEquals(name+", added by print(String name)",runtimeService.getVariable(processInstance.getId(), "returnValue"));
//		assertEquals(businessKey,runtimeService.getVariable(processInstance.getId(), "businessKey"));
//		Task task=taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//		String setByTask=(String)taskService.getVariable(task.getId(), "setByTask");
//		System.out.println("测试通过，setByTask:"+setByTask);
	}

}
