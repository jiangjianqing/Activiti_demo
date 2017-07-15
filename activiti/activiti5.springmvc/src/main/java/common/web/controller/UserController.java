package common.web.controller;

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
import common.db.model.identity.User;
import common.db.repository.jpa.identity.UserDAO;
import common.db.repository.jpa.identity.impl.UserDaoImpl;
import common.web.model.AuthenticationUser;

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
 */
@Scope("session")
@Controller
@RequestMapping("/rest/user")
@SessionAttributes(types = {AuthenticationUser.class,/*String.class*/},value={"currentUser","session.message"})//将符合types或者vlaue的ModelMap对象 存入Session
//放到Session属性列表中，以便这个属性可以跨请求访问
public class UserController {
	
	@Resource
	private UserDaoImpl userDao;
	// private Map<String, Info> model = Collections.synchronizedMap(new
	// HashMap<String, Info>());
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.GET)
	@ResponseBody
	public PageObject<User> getAll() throws OutOfPageRangeException, DaoException{
		return userDao.getList(1);
		//System.out.println(userDao);
		/*if(result.hasErrors()) { //验证失败 
			System.out.println("验证User出现错误");
			System.out.println(result.toString());
            return "error";  
        }  */
        //return "show";  //验证成功
	}
	
	//public ResponseEntity<String> createMembership(@RequestParam String userId
	//		,@RequestParam(required=false) String[] groupIds){
	
	@RequestMapping(value={"" , "/"},method=RequestMethod.POST)
	public String addUser(@Valid @RequestBody common.db.model.identity.User user, BindingResult result /*其他参数必须在result后面*/) throws DaoException{
		if(result.hasErrors()) { //验证失败 
			
			System.out.println("验证User出现错误");
			System.out.println(result.toString());
			throw new EntityNotFoundException("验证User出现错误");
            //return "error";  
        }else{
        	userDao.create(user);
        }
        return "show";  //验证成功
	}
	
	

}
