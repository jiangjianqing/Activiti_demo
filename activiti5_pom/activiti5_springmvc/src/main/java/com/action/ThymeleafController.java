package com.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import common.spring.AvoidDuplicateSubmission;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

	public ThymeleafController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value={"" , "/list"},method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public String getAll(Model model,HttpServletRequest request,HttpServletResponse response){
		model.addAttribute("name", "中文测试  Dear");
		//特别注意：这里返回的是tiles.xml中定义的definition.name
        return "first-template";
    }
	//
	//注意:thymeleaf目前还无法与tiles3整合,可以使用layout-dialet实现同样的功能
	//https://ultraq.github.io/thymeleaf-layout-dialect/Installation.html
	
	//https://github.com/thymeleaf/thymeleaf-extras-springsecurity

}
