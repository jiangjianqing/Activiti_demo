package common.web.controller.rest;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.model.identity.Role;
import common.db.repository.identity.RoleDao;
import common.service.utils.BindingResultHelper;
import common.web.controller.AbstractRestController;
import common.web.model.WrappedResponseBody;

@Controller	
@RequestMapping("/rest/identity/role")
//放到Session属性列表中，以便这个属性可以跨请求访问
public class RoleController extends AbstractRestController {
	
	@Resource
	private RoleDao roleDao;
	// private Map<String, Info> model = Collections.synchronizedMap(new
	// HashMap<String, Info>());
	
	
	
	
	//----------------------以下内容为6个方法：一个调试方法、一个分页方法和4个标准的crud方法，标配-----------------------------------
	/**
	 * 调试方法，用于观察所有数据
	 * @return
	 * @throws DaoException
	 */
	@RequestMapping(value="all",method=RequestMethod.GET)
	@ResponseBody
	public WrappedResponseBody getAll() throws OutOfPageRangeException, DaoException{
		return new WrappedResponseBody(roleDao.getList());
	}
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	@ResponseBody
	public WrappedResponseBody getPageList(@RequestParam(defaultValue="1" , name = "${naming.requestParam.page}") int page) throws OutOfPageRangeException, DaoException{
		return new WrappedResponseBody(roleDao.getPageList(page));
	}	
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.POST)
	@ResponseBody
	public WrappedResponseBody create(@Valid @RequestBody Role role , BindingResult result) throws DaoException{
		BindingResultHelper.checkValidateResult(result);
		roleDao.insert(role);
		return new WrappedResponseBody(role); 
	}
	
	@RequestMapping(value={"/{id}"},method={RequestMethod.GET})
	@ResponseBody
	public WrappedResponseBody findById(@PathVariable Long id) throws DaoException, EntityNotFoundException{
		Role role=roleDao.selectByPrimaryKey(id);
		if(role == null){
			throw new EntityNotFoundException(id.toString());
		}
		return new WrappedResponseBody(role);
	}
	
	@RequestMapping(value={"/{id}"},method={RequestMethod.PUT})
	@ResponseBody
	public WrappedResponseBody update(@PathVariable Long id,@Valid @RequestBody Role role , BindingResult result) throws DaoException{
		BindingResultHelper.checkValidateResult(result);
		role.setId(id);
		return new WrappedResponseBody(roleDao.updateByPrimaryKey(role));
	}
	
	
	@RequestMapping(value={"/{id}"},method={RequestMethod.DELETE})
	@ResponseBody
	public WrappedResponseBody  delete(@PathVariable Long id) throws DaoException{
		roleDao.deleteByPrimaryKey(id);
		return new WrappedResponseBody("");
	}
	

}
