package workflow.activiti.web.controller.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import common.db.model.identity.User;
import common.web.controller.AbstractViewController;
import common.web.utils.SessionHelper;
import workflow.activiti.utils.ActivitiUtil;

@Controller
@RequestMapping("workflow/task")
public class TaskManageController extends AbstractViewController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;

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
}
