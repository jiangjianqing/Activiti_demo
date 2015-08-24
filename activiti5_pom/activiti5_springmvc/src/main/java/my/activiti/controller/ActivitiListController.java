package my.activiti.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/workflow")
public class ActivitiListController {
	
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(value = "/process-list")
	public ModelAndView processList(){
		ModelAndView mav=new ModelAndView("workflow/process-list");
		List<ProcessDefinition> processDefinitionList=repositoryService.createProcessDefinitionQuery().list();
		mav.addObject("processDefinitionList", processDefinitionList);
		return mav;
	}
	
	@RequestMapping(value = "/redeploy/all")
	public String redeployAll() throws Exception{
		return "redirect:/workflow/process-list";
	}
}
