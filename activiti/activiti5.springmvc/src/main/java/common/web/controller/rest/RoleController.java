package common.web.controller.rest;

import java.io.PrintWriter;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
//import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.page.PageObject;
import common.db.model.identity.Role;
import common.db.model.identity.User;
import common.db.repository.jpa.identity.RoleDao;
import common.db.repository.jpa.identity.UserDAO;
import common.db.repository.jpa.identity.impl.RoleDaoImpl;
import common.db.repository.jpa.identity.impl.UserDaoImpl;
import common.security.AuthenticationUser;
import common.security.CustomUserDetailsService;
import common.web.controller.AbstractRestController;

@Controller	
@RequestMapping("/rest/identity/role")
//放到Session属性列表中，以便这个属性可以跨请求访问
public class RoleController extends AbstractRestController {
	
	@Resource
	private RoleDao roleDao;
	// private Map<String, Info> model = Collections.synchronizedMap(new
	// HashMap<String, Info>());
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	@ResponseBody
	public PageObject<Role> getAll() throws OutOfPageRangeException, DaoException{
		return roleDao.getList(1);
	}

	
	

}
