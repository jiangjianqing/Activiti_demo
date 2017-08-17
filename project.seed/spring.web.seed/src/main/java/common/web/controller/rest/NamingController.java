package common.web.controller.rest;

import java.io.Serializable;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.model.identity.RoleTypeEnum;
import common.service.aop.annotation.SystemControllerLog;
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
