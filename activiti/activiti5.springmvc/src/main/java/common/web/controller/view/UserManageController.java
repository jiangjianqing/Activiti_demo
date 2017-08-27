package common.web.controller.view;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import common.db.model.identity.User;
import common.db.repository.identity.RoleDao;
import common.db.repository.identity.UserDAO;
import common.security.PasswordEncoderAssist;
import common.web.controller.AbstractViewController;

@Controller
@RequestMapping("identity/user")
public class UserManageController extends AbstractViewController {
	@Resource
	private PasswordEncoderAssist passwordEncoderAssist;
	
	@Resource
	private UserDAO userDao;
	
	@Resource
	private RoleDao roleDao;
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	public ModelAndView getList() throws Exception
	{
		ModelAndView mav = new ModelAndView(getDefaultRequestMappingUrl());
		mav.addObject("users", userDao.getList());
		mav.addObject("roles", roleDao.getList());
		return mav;
	}
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.POST)
	public String create(@RequestParam(value="userName")String userName ,@RequestParam(value="roleId") Long[] roleIds) throws Exception {
		User user = new User();
		user.setUserName(userName);
		for (Long roleId:roleIds) {
			user.getRoles().add(roleDao.selectByPrimaryKey(roleId));
		}		
		user.setPassword("123456");//创建用户时由系统分配初始密码
		passwordEncoderAssist.encodeNewUserPassword(user);
		userDao.insert(user);
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
	@RequestMapping(value="delete/{userId}")
	public String delete(@PathVariable("userId") Long userId) throws Exception {
		userDao.deleteByPrimaryKey(userId);
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
}
