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
@RequestMapping("/tiles")
public class TilesController {

	public TilesController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET/*, method = RequestMethod.POST*/)
    public String getAll(HttpServletRequest request,HttpServletResponse response){
		//特别注意：这里返回的是tiles.xml中定义的definition.name
        return "myFirstTilesView";
    }

}
