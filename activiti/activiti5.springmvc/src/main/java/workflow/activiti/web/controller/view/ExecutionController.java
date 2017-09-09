package workflow.activiti.web.controller.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.NativeExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import common.db.model.identity.User;
import common.web.controller.AbstractViewController;
import common.web.utils.SessionHelper;

@Controller
@RequestMapping("workflow/execution")
public class ExecutionController extends AbstractViewController{
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	private List<Execution> getExecutionByUserId(String userId){
		String sql = "select RES.* from ACT_RU_EXECUTION RES left join " + 
				"	ACT_HI_TASKINST ART on ART.PROC_INST_ID_=RES.PROC_INST_ID_ " + 
				"    where ART.ASSIGNEE_ = '"+userId+"' and ACT_ID_ is not null " + 
				"    and IS_ACTIVE_= true " + 
				"    order by START_TIME_ desc;";
		NativeExecutionQuery nativeExecutionQuery = runtimeService.createNativeExecutionQuery();
		return nativeExecutionQuery.sql(sql).list();
		//listPage可以分页
 	}
	
	private void definitionCache(Map<String,ProcessDefinition> definitionMap,String processDefinitionId) {
		definitionMap.put(processDefinitionId, repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult());
	}
	
	@RequestMapping(value="")
	public ModelAndView getList() {
		User user=(User)SessionHelper.getAuthenticatedUser();
		List<Execution> executionList = getExecutionByUserId(user.getUserName());
		//查询流程定义对象
		Map<String,ProcessDefinition> definitionMap = new HashMap<String,ProcessDefinition>();
		//任务的英文-中文对照
		Map<String, Task> taskMap = new HashMap<String,Task>();
		//每个Execution的当前活动ID,可能为多个
		Map<String,List<String>> currentActivityMap = new HashMap<String,List<String>>();
		//设置每个Execution对象的当前活动节点
		for(Execution execution : executionList) {
			ExecutionEntity executionEntity = (ExecutionEntity)execution;
			String processInstanceId = executionEntity.getProcessInstanceId();
			String processDefinitionId = executionEntity.getProcessDefinitionId();
			//缓存ProcessDefinition对象到Map集合
			definitionCache(definitionMap,processDefinitionId);
			//查询当前流程的所有处于活动状态的活动ID，如果流程中存在并行任务且此时处于活动状态的
			//Activity有多个，则查询出来的结果是一个集合
			List<String> activeActivityIds = runtimeService.getActiveActivityIds(execution.getId());
			//经测试，execution.id与processInstanceId相同
			currentActivityMap.put(execution.getId(), activeActivityIds);
			for(String activityId : activeActivityIds) {
				//查询处于活动状态的任务
				Task task = taskService.createTaskQuery().taskDefinitionKey(activityId)
						.executionId(execution.getId()).singleResult();
				if(task == null) {//处理调用服务
					ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
							.superProcessInstanceId(processInstanceId).singleResult();
					task = taskService.createTaskQuery()//查询调用活动的当前节点
							.processInstanceId(processInstance.getProcessInstanceId()).singleResult();
					definitionCache(definitionMap,processInstance.getProcessDefinitionId());
				}
				taskMap.put(activityId, task);
			}
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("taskMap",taskMap);
		mav.addObject("definitionMap", definitionMap);
		mav.addObject("currentActivityMap", currentActivityMap);
		//重要：在view中根据活动ID获取Task对象
		return mav;
	}
}
