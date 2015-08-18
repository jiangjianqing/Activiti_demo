package com.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import common.spring.AvoidDuplicateSubmission;

@Controller
//@RequestMapping("/test/*")
public class EmpAction {

	public EmpAction() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="add.do",params="p=getAll",method=RequestMethod.GET/*, method = RequestMethod.POST*/)
	@AvoidDuplicateSubmission(needRemoveToken = true)
    public String getAll(HttpServletRequest request,HttpServletResponse response){
        //List list = empService.getAllEmp();
        //request.setAttribute("list", list);
		
		ApplicationContext cx=common.spring.SpringContextHolder.getApplicationContext();
		org.springframework.jdbc.core.JdbcTemplate jdbcTemplate=common.spring.SpringContextHolder.getBean("jdbcTemplate");
        return "show";
    }
	
	@RequestMapping(value="add.ajax",params="p=getAll",method=RequestMethod.GET)
	@AvoidDuplicateSubmission( needSaveToken = true)
    public String getAllTest(HttpServletRequest request,HttpServletResponse response){
        //List list = empService.getAllEmp();
        //request.setAttribute("list", list);
		ApplicationContext cx=common.spring.SpringContextHolder.getApplicationContext();
		org.springframework.jdbc.core.JdbcTemplate jdbcTemplate=common.spring.SpringContextHolder.getBean("jdbcTemplate");
        return "showajax";
    }

}
