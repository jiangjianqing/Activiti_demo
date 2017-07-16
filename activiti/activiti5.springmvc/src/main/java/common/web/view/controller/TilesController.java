package common.web.view.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import common.web.model.AuthenticationUser;

@Controller
@RequestMapping("/tiles")
public class TilesController {

	public TilesController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value={"" , "/list"},method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public String getAll(Model model,HttpServletRequest request,HttpServletResponse response){
		model.addAttribute("name", "Dear");
		//特别注意：这里返回的是tiles.xml中定义的definition.name
        return "myFirstTilesView";
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
		    if (entry.getValue() instanceof AuthenticationUser){
		    	AuthenticationUser tmp=(AuthenticationUser)entry.getValue();
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
	public AuthenticationUser showUserJson(ModelMap modMap
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
		AuthenticationUser user = new AuthenticationUser();
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
