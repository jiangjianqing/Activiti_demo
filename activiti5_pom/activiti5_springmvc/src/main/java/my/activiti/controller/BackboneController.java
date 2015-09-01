package my.activiti.controller;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="backbone")
public class BackboneController {
	private String type="type";
	@RequestMapping(value="group.do")
	public ModelAndView groupIndex(){
		ModelAndView mav=new ModelAndView("backbone-test");
		mav.addObject(type, "group");
		return mav;
	}
	
	@Autowired
	protected IdentityService identityService;
	
	/**
	 * 
	 * 获取group.json
	 * @return
	 */
	@RequestMapping(value = {"group"},method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Group> getGroupList(){
		List<Group> list=identityService.createGroupQuery().list();
		return list;
	}
}
