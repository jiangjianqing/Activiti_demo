package my.activiti.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
@Transactional
@TransactionConfiguration(transactionManager = "jdbcTransactionManager", defaultRollback = true)
//@Deployment(resources="diagrams/Leave.bpmn")
public class TestDynamicForm {

	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;
	@Autowired
	private ProcessEngine processEngine;
	
	//@Test
	public void testApprove() {

		String currentUserId="henryyan";
		identityService.setAuthenticatedUserId(currentUserId);
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("leave").orderByDeploymentId().desc().list().get(0);
		System.out.println(String.format("DeploymentID=%s", processDefinition.getDeploymentId()));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca=Calendar.getInstance();
		String startDate=sdf.format(ca.getTime());
		ca.add(Calendar.DAY_OF_MONTH, 2);
		String endDate=sdf.format(ca.getTime());
		
		Map<String,String> variables=new HashMap<String,String>();
		variables.put("startDate", startDate);
		variables.put("endDate", endDate);
		variables.put("reason", "公休");
		
		ProcessInstance processInstance=formService.submitStartFormData(processDefinition.getId(), variables);
		assertNotNull(processInstance);
		
		//设置领导审批通过
		//注意createTaskQuery的查询方式，一定要先过滤processInstanceId或其他条件
		Task deptLeaderTask=taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).taskCandidateGroup("deptLeader").singleResult();
		variables=new HashMap<String,String>();
		variables.put("deptLeaderApproved", "true");//设置领导审批通过
		formService.submitTaskFormData(deptLeaderTask.getId(), variables);
		
		//hr审批通过
		Task hrTask=taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).taskCandidateGroup("hr").singleResult();
		variables=new HashMap<String,String>();
		variables.put("hrApproved", "true");
		formService.submitTaskFormData(hrTask.getId(), variables);
		//申请人消假
		Task reportBackTask=taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).taskAssignee(currentUserId).singleResult();
		variables=new HashMap<String,String>();
		variables.put("reportBackDate", sdf.format(ca.getTime()));
		formService.submitTaskFormData(reportBackTask.getId(), variables);
		
		HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().finished().singleResult();
		assertNotNull(historicProcessInstance);
		
		Map<String,Object> historyVariables=packageVariables(processInstance);
		assertEquals("ok",historyVariables.get("result"));
	}
	
	@Test
	public void testNotApprove() {

		String currentUserId="henryyan";
		identityService.setAuthenticatedUserId(currentUserId);
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("leave").orderByDeploymentId().desc().list().get(0);
		System.out.println(String.format("DeploymentID=%s", processDefinition.getDeploymentId()));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca=Calendar.getInstance();
		String startDate=sdf.format(ca.getTime());
		ca.add(Calendar.DAY_OF_MONTH, 2);
		String endDate=sdf.format(ca.getTime());
		
		Map<String,String> variables=new HashMap<String,String>();
		variables.put("startDate", startDate);
		variables.put("endDate", endDate);
		variables.put("reason", "公休");
		
		ProcessInstance processInstance=formService.submitStartFormData(processDefinition.getId(), variables);
		assertNotNull(processInstance);
		
		//设置领导审批通过
		//注意createTaskQuery的查询方式，一定要先过滤processInstanceId或其他条件
		Task deptLeaderTask=taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).taskCandidateGroup("deptLeader").singleResult();
		variables=new HashMap<String,String>();
		variables.put("deptLeaderApproved", "false");//设置领导审批通过
		formService.submitTaskFormData(deptLeaderTask.getId(), variables);
		
		//申请人取消申请
		Task reApplyTask=taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).taskAssignee(currentUserId).singleResult();
		variables=new HashMap<String,String>();
		//variables.put("startDate", startDate);
		//variables.put("endDate", endDate);
		//variables.put("reason", "公休");
		variables.put("reApply", "false");
		formService.submitTaskFormData(reApplyTask.getId(), variables);
		
		HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().finished().singleResult();
		assertNotNull(historicProcessInstance);
		
		Map<String,Object> historyVariables=packageVariables(processInstance);
		assertEquals("canceled",historyVariables.get("result"));
	}
	
	private Map<String,Object> packageVariables(ProcessInstance processInstance){
		Map<String,Object> historicVariables=new HashMap<String,Object>();
		List<HistoricDetail> list=historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();
		for(HistoricDetail historicDetail:list){
			if(historicDetail instanceof HistoricFormProperty){//表单中的字段
				HistoricFormProperty field=(HistoricFormProperty)historicDetail;
				historicVariables.put(field.getPropertyId(), field.getPropertyValue());
				System.out.println(String.format("form field: taskId=%s,%s=%s", 
						field.getTaskId(),field.getPropertyId(),field.getPropertyValue()));
			}else if(historicDetail instanceof HistoricVariableUpdate){
				HistoricVariableUpdate variable=(HistoricVariableUpdate)historicDetail;
				historicVariables.put(variable.getVariableName(), variable.getValue());
				System.out.println(String.format("variable: %s=%s", variable.getVariableName(),variable.getValue()));
			}
		}
		
		return historicVariables;
	}

}
