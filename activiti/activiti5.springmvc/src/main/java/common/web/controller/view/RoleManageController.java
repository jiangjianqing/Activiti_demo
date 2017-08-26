package common.web.controller.view;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import common.db.repository.identity.RoleDao;
import common.web.controller.AbstractViewController;

@Controller
@RequestMapping("identity/role")
public class RoleManageController extends AbstractViewController {
	@Resource
	private RoleDao roleDao;
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	public ModelAndView getList() throws Exception {
		ModelAndView mav = new ModelAndView(getDefaultRequestMappingUrl());
		mav.addObject("roles", roleDao.getList());
		return mav;
	}
}
