package workflow.activiti.web.controller.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import common.db.model.identity.User;
import common.web.controller.AbstractViewController;
import common.web.utils.SessionHelper;
import workflow.activiti.utils.ActivitiDataTypeEnum;
import workflow.activiti.utils.ActivitiUtil;

@Controller
@RequestMapping("workflow/task")
public class TaskManageController extends AbstractViewController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private HistoryService historyService;

	@RequestMapping(value= {"","/"})
	public ModelAndView getTaskList() throws Exception {
		ModelAndView mav = new ModelAndView(getDefaultRequestMappingUrl());
		return mav;
	}
	
	@RequestMapping(value="claim-task/{taskId}")
	public String claimTask(@PathVariable String taskId,@RequestParam("nextDo") String nextDo,HttpSession session) throws Exception {
		User user=(User)SessionHelper.getAuthenticatedUser();
		taskService.claim(taskId, user.getUserName());
		if(nextDo != null && nextDo.equals("handle")) {
			return "redirect:/"+getDefaultRequestMappingUrl()+"/get-task-form/"+taskId;
		}
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
	@RequestMapping(value="unclaim-task/{taskId}")
	public String unClaimTask(@PathVariable String taskId,HttpSession session) throws Exception {
		if (canUnclaim(taskId)) {
			User user=(User)SessionHelper.getAuthenticatedUser();
			taskService.claim(taskId, null);
		}else {
			logger.error("当前任务不符合反签收的条件！");
		}		
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
	@RequestMapping(value="/get-task-form/{taskId}")
	public ModelAndView getTaskForm(@PathVariable String taskId){
		ModelAndView mav=ActivitiUtil.getTaskForm(taskId);
		mav.setViewName("workflow/task-get-task-form");
		mav.addObject("canUnclaim", canUnclaim(taskId));
		return mav;
	}
	
	@RequestMapping(value="/comment/add/{taskId}",method=RequestMethod.POST)
	public ModelAndView addComment(@PathVariable String taskId
			,@RequestParam(value="processInstanceId",required=false,defaultValue="") String processInstanceId,@RequestParam("message") String message){
		ActivitiUtil.addComment(taskId, processInstanceId, null, message);
		ModelAndView mav = new ModelAndView("redirect:/"+getDefaultRequestMappingUrl()+"/get-task-form/"+taskId); 
		return mav;
	}
	
	/**
	 * 判断任务是否允许反签收
	 * @param taskId
	 * @return
	 */
	private Boolean canUnclaim(String taskId) {
		//反签收条件过滤
		List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
		for(IdentityLink identityLink:links) {
			logger.warn(identityLink.getType());
			//如果一个任务有相关的候选人、候选组就可以反签收
			if (identityLink.getType().equalsIgnoreCase(IdentityLinkType.CANDIDATE)) {
				//taskService.claim(taskId, null);
				return true;	
			}
		}
		return false;
	}
	
	@RequestMapping(value="complete-task/{taskId}")
	public String completeTask(@PathVariable String taskId,HttpServletRequest request) throws Exception{
		ActivitiUtil.completeTask(taskId, request);
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
//	@RequestMapping(value="subtask/add/{taskId}",method=RequestMethod.GET)
//	public ModelAndView getSubTaskForm(@PathVariable(value="taskId",required=false) String parentTaskId) {
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView getSubTaskForm(@RequestParam(value="parentTaskId",required=false) String parentTaskId) {
		ModelAndView mav = new ModelAndView("workflow/task-get-sub-task-form");
		//重要：parentTaskId！=null时为sub task，否则为manual task
		if(parentTaskId!=null) {
			Task parentTask = taskService.createTaskQuery().taskId(parentTaskId).singleResult();
			mav.addObject("parentTask", parentTask);
		}		
		return mav;
	}
	
	//@RequestMapping(value="subtask/add/{taskId}",method=RequestMethod.POST)
	//public ModelAndView addSubTask(@PathVariable("taskId") String parentTaskId
	@RequestMapping(value="add",method=RequestMethod.POST)
	public ModelAndView addSubTask(@RequestParam(value="parentTaskId",required=false) String parentTaskId
			,@RequestParam("taskName") String taskName,@RequestParam(value="description",required = false) String description) throws Exception {
		//ModelAndView mav=ActivitiUtil.getTaskForm(taskId);
		Task newTask = taskService.newTask();//非常重要：这里创建任务时可以指定ID，不由Activiti自动生成
		//重要：parentTaskId！=null时为sub task，否则为manual task
		if(parentTaskId!=null) {
			newTask.setParentTaskId(parentTaskId);
		}
		User user=(User)SessionHelper.getAuthenticatedUser();
		newTask.setOwner(user.getUserName());//设置拥有人
		newTask.setAssignee(user.getUserName());//设置办理人
		newTask.setName(taskName);//设置任务名称
		newTask.setDescription(description);
		newTask.setDueDate(new Date());
		//newTask.setFormKey(formKey);
		//newTask.setPriority(priority);
		//newTask.setTenantId(tenantId);
		taskService.saveTask(newTask);
		
		ModelAndView mav = new ModelAndView();
		if(parentTaskId!=null) {//如果有parentTaskId，则打开parentTask的form
			mav.setViewName("redirect:/"+getDefaultRequestMappingUrl()+"/get-task-form/"+parentTaskId);
		}else {//进入task 列表界面
			mav.setViewName("redirect:/"+getDefaultRequestMappingUrl());
		}

		return mav;
	}
	
	@RequestMapping(value="delete/{taskId}",method=RequestMethod.GET)
	public ModelAndView deleteSubTask(@PathVariable("taskId") String taskId, @RequestParam(value="parentTaskId",required=false) String parentTaskId) {
		taskService.deleteTask(taskId);
		ModelAndView mav = new ModelAndView();
		if(parentTaskId!=null) {//如果有parentTaskId，则打开parentTask的form
			mav.setViewName("redirect:/"+getDefaultRequestMappingUrl()+"/get-task-form/"+parentTaskId);
		}else {//进入task 列表界面
			mav.setViewName("redirect:/"+getDefaultRequestMappingUrl());
		}
		return mav;
	}
}
