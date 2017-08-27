package common.web.controller.view;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import common.db.base.exception.DaoException;
import common.db.model.identity.Role;
import common.db.model.identity.RoleTypeEnum;
import common.db.repository.identity.RoleDao;
import common.service.utils.BindingResultHelper;
import common.web.controller.AbstractViewController;
import common.web.model.WrappedResponseBody;

@Controller
@RequestMapping("identity/role")
public class RoleManageController extends AbstractViewController {
	@Resource
	private RoleDao roleDao;
	
	private Map<String , String> roleTypes = RoleTypeEnum.getCodeAndDescriptions();
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	public ModelAndView getList() throws Exception {
		ModelAndView mav = new ModelAndView(getDefaultRequestMappingUrl());
		mav.addObject("roleTypes", roleTypes);
		mav.addObject("roles", roleDao.getList());
		return mav;
	}
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.POST)
	public String create(@RequestParam(value="type")String type,@RequestParam(value="name")String name) throws Exception{
		Role role = new Role();
		role.setName(name);
		role.setType(RoleTypeEnum.parseCode(type));
		roleDao.insert(role);
		
		return "redirect:/"+getDefaultRequestMappingUrl(); 
	}
	
	@RequestMapping(value="delete/{roldId}")
	public String delete(@PathVariable("roldId") Long roleId) throws Exception{
		roleDao.deleteByPrimaryKey(roleId);
		
		return "redirect:/"+getDefaultRequestMappingUrl(); 
	}
}
