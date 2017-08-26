package workflow.activiti.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import common.web.controller.AbstractViewController;

@Controller
@RequestMapping("workflow/task")
public class TaskManageController extends AbstractViewController {

	@RequestMapping(value="")
	public ModelAndView getTaskList() throws Exception {
		ModelAndView mav = new ModelAndView(getDefaultRequestMappingUrl());
		return mav;
	}
}
