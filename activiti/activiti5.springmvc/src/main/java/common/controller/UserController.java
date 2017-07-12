package common.controller;

import java.io.PrintWriter;
import java.util.*;

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

import common.model.User;

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
@SessionAttributes(types = {User.class,/*String.class*/},value={"currentUser","session.message"})//将符合types或者vlaue的ModelMap对象 存入Session
//放到Session属性列表中，以便这个属性可以跨请求访问
public class UserController {
	// private Map<String, Info> model = Collections.synchronizedMap(new
	// HashMap<String, Info>());

	@RequestMapping(value="/",method=RequestMethod.POST)
	public String addUser(@Valid User User, BindingResult result /*其他参数必须在result后面*/){
		if(result.hasErrors()) { //验证失败 
			System.out.println("验证User出现错误");
			System.out.println(result.toString());
            return "error";  
        }  
        return "show";  //验证成功
	}
	
	
	@RequestMapping(value = "/{id}", produces = "application/json;charset=UTF-8")  
    public @ResponseBody  
    String say(@PathVariable(value = "id") String msg) {  
        return "{\"userid\":\"you say:'" + msg + "'\"}";  
    }  

	// 返回void和ModelMap的时候，逻辑视图名由请求处理方法对应的 URL 确定，如以下的方法：对应springmvc/modelMap.jsp这个视图
	//在默认情况下，ModelMap 中的属性作用域是 request 级别，相当于HttpServletRequest中的request.setAttribute() 一样, 在 JSP 视图页面中通过 request.getAttribute(“attribute name”) 
	//当本次请求结束后，ModelMap 中的属性将销毁。如果希望在多个请求中共享 ModelMap 中的属性，必须将其属性转存到 session 中，结合@SessionAttributes使用即可
	@RequestMapping("/springmvc/modelMap")
	public ModelMap modelMap(ModelMap modMap,HttpServletRequest request) {
		//这里会显示所有ModelMap中的对象，重点是SessionAttributes影响的部分，这样可以和直接操作Session完全相同
		System.out.println(String.format("modMap的长度=%d", modMap.size()));
		for (Map.Entry<String, Object> entry : modMap.entrySet()) {  
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
		    if (entry.getValue() instanceof User){
		    	User tmp=(User)entry.getValue();
		    	System.out.println(String.format("user.name=%s", tmp.getUserName()));
		    }
		}  
		
		List<String> names = new ArrayList<String>();
		names.add("Rick");
		names.add("Austin");
		modMap.put("names", names);
		modMap.put("session.message", "hello guys");
		modMap.put("comment", "hello guys");
		
		return modMap;

	}

	@RequestMapping(value = "showuserjson", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public User showUserJson(ModelMap modMap
			,@ModelAttribute("session.message") String msg //使用 ModelAttribute读取参数，默认为requestScope，如果该参数不存在则会报错
			,HttpSession session) {
		//如果是第一次连接，则session.isNew()==true
		System.out.println(String.format("session.isNew=%s",session.isNew()));
		System.out.println(String.format("session.message=%s", msg));
		Enumeration<String> sessionArg=session.getAttributeNames();
		while(sessionArg.hasMoreElements()){
			String argName=sessionArg.nextElement();
			System.out.println(String.format("name=%s,value=%s",argName, session.getAttribute(argName)));
		}
		//logger.debug("使用ResponseBody时记录日志");
		User user = new User();
		user.setUserName("中文测试789");
		modMap.put("user", user);//这个User会存入session中
		return user;
	}
	
	//@ResponseBody可以直接返回结果，
	//而ResponseEntity 可以定义返回的HttpHeaders和HttpStatus，
	@RequestMapping("/entity/status")  
    public ResponseEntity<String> responseEntityStatusCode() {  
        return new ResponseEntity<String>("The String ResponseBody with custom status code (403 Forbidden)",  
                HttpStatus.FORBIDDEN);  
    }  
  
    @RequestMapping("/entity/headers")  
    public ResponseEntity<String> responseEntityCustomHeaders() {  
        HttpHeaders headers = new HttpHeaders();  
        headers.setContentType(MediaType.TEXT_PLAIN);  
        headers.set("abcde", "测试header");
        return new ResponseEntity<String>("The String ResponseBody with custom header Content-Type=text/plain",  
                headers, HttpStatus.OK);  
    }  
    
    //利用 spring mvc ResponseEntity 做文件下载
    //headers="Content-Type=text/plain"  :要求Request Headers里的Content-Type为"text/plain"才能执行该方法，被consumes="text/plain"
    //headers="Accept=text/plain"		：指定的格式为Response Headers的Content-Type的格式，被 produces = "application/json;charset=UTF-8"代替
    @RequestMapping(value = "/cmpSts/{cmpId}", method = RequestMethod.GET,consumes="text/plain"/*headers="Content-Type=text/plain"*/
    			,produces={MediaType.APPLICATION_OCTET_STREAM_VALUE, "application/json"})
	public ResponseEntity<byte[]> cmpSts(@PathVariable int cmpId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Locale local=request.getLocale();
		String[] file = new String[]{"测试文件.txt","中文,english"};
		byte[] bs = file[1].getBytes("UTF-8");
		HttpHeaders headers = new HttpHeaders();  
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
	    headers.setContentDispositionFormData("attachment", new String(file[0].getBytes("UTF-8"), "ISO8859-1"));  //解决文件名中文乱码问题
		return new ResponseEntity<byte[]>(bs, headers, HttpStatus.CREATED);
	}

}
