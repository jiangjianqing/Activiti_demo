package workflow.activiti.web.controller.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public String claimTask(@PathVariable String taskId,HttpSession session) throws Exception {
		User user=(User)SessionHelper.getAuthenticatedUser();
		taskService.claim(taskId, user.getUserName());
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
	@RequestMapping(value="/get-task-form/{taskId}")
	public ModelAndView getTaskForm(@PathVariable String taskId){
		ModelAndView mav=new ModelAndView("workflow/task-get-task-form");
		Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		boolean hasFormKey=taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
		mav.addObject("task",task);
		mav.addObject("hasFormKey",hasFormKey);
		if(task.getFormKey()!=null){
			Object renderFormData=formService.getRenderedTaskForm(taskId);		
			mav.addObject("formData", renderFormData);
		}else{
			mav.addObject("formData", taskFormData);
		}
		return mav;
	}
	
	@RequestMapping(value="complete-task/{taskId}")
	public String completeTask(@PathVariable String taskId,HttpServletRequest request) throws Exception{
		ActivitiUtil.completeTask(taskId, request);
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
}
