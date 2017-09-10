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
import workflow.activiti.utils.ActivitiUtil;

@Controller
@RequestMapping("workflow/execution")
public class ExecutionController extends AbstractViewController{
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	
	@RequestMapping(value="")
	public ModelAndView getList() {
		
		//重要：在view中根据活动ID获取Task对象
		return ActivitiUtil.getExecutionList();
	}
}
