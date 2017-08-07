package common.web.controller.rest;

import java.io.PrintWriter;
import java.io.Serializable;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
//import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import common.aop.annotation.SystemControllerLog;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.page.PageObject;
import common.db.model.identity.Role;
import common.db.model.identity.RoleTypeEnum;
import common.db.model.identity.User;
import common.db.repository.jpa.identity.RoleDao;
import common.db.repository.jpa.identity.UserDAO;
import common.db.repository.jpa.identity.impl.RoleDaoImpl;
import common.db.repository.jpa.identity.impl.UserDaoImpl;
import common.security.AuthenticationUser;
import common.security.SimpleUserDetailsService;
import common.service.utils.SpringContextHolder;
import common.web.controller.AbstractRestController;
import common.web.model.RequestParamNaming;
import common.web.model.WrappedResponseBody;

/**
 * 用于提供server和client共用的命名
 * @author jjq
 *
 */
@Controller	
@RequestMapping("/rest/common/naming")
//放到Session属性列表中，以便这个属性可以跨请求访问
public class NamingController  extends AbstractRestController {
	
	private AllNamings allNamings;
	
	public NamingController() {
		RequestParamNaming requestParamNaming = new RequestParamNaming();
		SpringContextHolder.getAutowireCapableBeanFactory().autowireBean(requestParamNaming);
		
		allNamings = new AllNamings();
		allNamings.setRequestParamNaming(requestParamNaming);
	}
	
	@SystemControllerLog(description="查询系统命名参数")
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	@ResponseBody
	public WrappedResponseBody getAll() throws OutOfPageRangeException, DaoException{
		
		return new WrappedResponseBody(allNamings);
	}

}

class AllNamings implements Serializable{
	private RequestParamNaming requestParamNaming ;
	
	private Map<String , String> roleTypes = RoleTypeEnum.getCodeAndDescriptions();
	
	public Map<String, String> getRoleTypes() {
		return roleTypes;
	}	

	public RequestParamNaming getRequestParamNaming() {
		return requestParamNaming;
	}

	public void setRequestParamNaming(RequestParamNaming requestParamNaming) {
		this.requestParamNaming = requestParamNaming;
	}
	
}
