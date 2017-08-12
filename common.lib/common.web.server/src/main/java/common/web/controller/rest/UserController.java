package common.web.controller.rest;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.model.identity.User;
import common.db.repository.identity.UserDAO;
import common.security.AuthenticationUser;
import common.security.PasswordEncoderAssist;
import common.security.SimpleUserDetailsService;
import common.service.utils.BindingResultHelper;
import common.web.controller.AbstractRestController;
import common.web.model.WrappedResponseBody;

/**
 * 如类级别的映射为 @RequestMapping(value="/narrow", produces="text/html")，
 * 方法级别的为@RequestMapping(produces="application/xml")，
 * 此时方法级别的映射将覆盖类级别的，因此请求头headers=“Accept:application/xml”是成功的(produces)，而“text/html”将报406错误码，表示不支持的请求媒体类型。
 * 只有生产者/消费者 模式 是 覆盖，其他的使用方法是继承，如headers、params等都是继承。
 * 
 * 组合使用是“或”的关系
 * @RequestMapping(produces={"text/html", "application/json"}) ：将匹配“Accept:text/html”或“Accept:application/json”。
 * 
 * 问题
 * 消费的数据，如JSON数据、XML数据都是由我们读取请求的InputStream并根据需要自己转换为相应的模型数据；
 * 生产的数据，如JSON数据、XML数据都是由我们自己先把模型数据转换为json/xml等数据，然后输出响应流。
 * Spring提供了一组注解（@RequestBody、@ResponseBody）和一组转换类（HttpMessageConverter）来完成
 * 
 * @RequestMapping注解方法支持的返回值类型
 * ModelAndView对象，with the model implicitly enriched with command objects and the results of @ModelAttributes annotated reference data accessor methods.（恕我实在翻译不出 TAT）。
 * Model对象，with the view name implicitly determined through a RequestToViewNameTranslator and the model implicitly enriched with command objects and the results of @ModelAttribute annotated reference data accessor methods.
 * Map对象，for exposing a model, with the view name implicitly determined through a RequestToViewNameTranslator and the model implicitly enriched with command objects and the results of @ModelAttribute annotated reference data accessor methods.
 * View对象，with the model implicitly determined through command objects and @ModelAttribute annotated reference data accessor methods. The handler method may also programmatically enrich the model by declaring a Model argument (see above).
 * String，value that is interpreted as the logical view name, with the model implicitly determined through command objects and @ModelAttribute annotated reference data accessor methods. The handler method may also programmatically enrich the model by declaring a Model argument (see above).
 * void，if the method handles the response itself (by writing the response content directly, declaring an argument of type ServletResponse / HttpServletResponse for that purpose) or if the view name is supposed to be implicitly determined through a RequestToViewNameTranslator (not declaring a response argument in the handler method signature).
 * @ResponseBody，If the method is annotated with @ResponseBody, the return type is written to the response HTTP body. The return value will be converted to the declared method argument type using HttpMessageConverters. See Section 16.3.3.5, “Mapping the response body with the @ResponseBody annotation”.
 * HttpEntity或者ResponseEntity，HttpEntity<?>或者ResponseEntity<?>对象可以取到Servlet的response的HTTP头信息（headers）和内容（contents）。这个实体（entity body）可以通过使用HttpMessageConverter类被转成Response流。See Section 16.3.3.6, “Using HttpEntity<?>”.
 * Any other return type is considered to be a single model attribute to be exposed to the view, using the attribute name specified through @ModelAttribute at the method level (or the default attribute name based on the return type class name). The model is implicitly enriched with command objects and the results of @ModelAttribute annotated reference data accessor methods.

 * @author cz_jjq
 *
 *
 *重要：
 *
 *spring MVC Controller默认是单例的，单例的原因有二：
1、为了性能。
2、不需要多例。
 *
 *Controller最佳实践：
1、不要在controller中定义成员变量。
2、万一必须要定义一个非静态成员变量时候，则通过注解@Scope("prototype")，将其设置为多例模式。
 */

@Controller	
@RequestMapping("/rest/identity/user")
@SessionAttributes(types = {AuthenticationUser.class,/*String.class*/},value={"currentUser","session.message"})//将符合types或者vlaue的ModelMap对象 存入Session
//放到Session属性列表中，以便这个属性可以跨请求访问
public class UserController extends AbstractRestController{
	
	@Resource
	private PasswordEncoderAssist passwordEncoderAssist;
	
	@Resource
	private UserDAO userDao;
	// private Map<String, Info> model = Collections.synchronizedMap(new
	// HashMap<String, Info>());
	/**
	 * 调试方法，用于观察所有数据
	 * @return
	 * @throws DaoException
	 */
	@RequestMapping(value="all",method=RequestMethod.GET)
	@ResponseBody
	public List<User> getAll() throws DaoException{
		return userDao.getList();
	}
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	@ResponseBody
	public WrappedResponseBody getPageList(@RequestParam(defaultValue="1" , name = "${naming.requestParam.page}") int page) throws OutOfPageRangeException, DaoException{
		return new WrappedResponseBody(userDao.getPageList(page));
	}
	
	//public ResponseEntity<String> createMembership(@RequestParam String userId
	//		,@RequestParam(required=false) String[] groupIds){
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.POST)
	@ResponseBody
	public WrappedResponseBody create(@Valid @RequestBody User user, BindingResult result /*其他参数必须在result后面*/) throws DaoException{
		BindingResultHelper.checkValidateResult(result);
		passwordEncoderAssist.encodeNewUserPassword(user);//对密码进行加密处理
    	userDao.insert(user);
        return new WrappedResponseBody(user);  //验证成功
	}
	
	@RequestMapping(value={"/{id}"},method={RequestMethod.GET})
	@ResponseBody
	public WrappedResponseBody findById(@PathVariable Long id) throws DaoException, EntityNotFoundException{
		User user=userDao.selectByPrimaryKey(id);
		if(user==null){
			throw new EntityNotFoundException(id.toString());
		}
		return new WrappedResponseBody(user);
	}
	
	@RequestMapping(value={"/{id}"},method={RequestMethod.PUT})
	@ResponseBody
	public WrappedResponseBody update(@PathVariable Long id,@Valid @RequestBody User user,BindingResult result ) throws DaoException{
		BindingResultHelper.checkValidateResult(result);
		user.setId(id);
		return new WrappedResponseBody(userDao.updateByPrimaryKey(user));
	}
	
	@RequestMapping(value={"/{id}"},method={RequestMethod.DELETE})
	@ResponseBody
	public WrappedResponseBody  delete(@PathVariable Long id) throws DaoException{
		userDao.deleteByPrimaryKey(id);
		return new WrappedResponseBody("");
	}

}
