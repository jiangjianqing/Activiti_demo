package com.focusight.platform3.controller;

import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;
//import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.focusight.platform3.views.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
	// private Map<String, Info> model = Collections.synchronizedMap(new
	// HashMap<String, Info>());
	private static final Logger log = LoggerFactory
			.getLogger(UserController.class);

	@RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String hello() {
		log.info("使用JSONObject时记录日志");
		JSONObject tmpJSONObj = null;
		tmpJSONObj = new JSONObject();
		tmpJSONObj.put("status", "ok");
		tmpJSONObj.put("errorMessage", "中文错误消息");
		return tmpJSONObj.toString();
	}
	
	@RequestMapping(value = "/say/{msg}", produces = "application/json;charset=UTF-8")  
    public @ResponseBody  
    String say(@PathVariable(value = "msg") String msg) {  
        return "{\"msg\":\"you say:'" + msg + "'\"}";  
    }  

	@RequestMapping("/modelAndView")
	public ModelAndView modelAndView() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("show");
		mav.addObject("message", "modelAndView 方法被调用");
		return mav;
	}

	@RequestMapping("/springmvc/modelMap")
	public ModelMap modelMap(ModelMap modMap) {
		List<String> names = new ArrayList<String>();
		names.add("Rick");
		names.add("Austin");
		modMap.put("names", names);
		modMap.put("message", "hello guys");
		modMap.put("comment", "hello guys");
		return modMap;

	}

	@RequestMapping(value = "showuserjson", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public User showUserJson() {
		log.debug("使用ResponseBody时记录日志");
		User user = new User();
		user.setName("中文测试789");
		return user;
	}

	@RequestMapping(value = "/showuser2.json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public User showUserJson2() {
		log.debug("使用ResponseBody时记录日志");
		User user = new User();
		user.setName("中文测试123");
		return user;
	}

}
