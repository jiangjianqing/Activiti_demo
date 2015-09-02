package my.activiti.controller;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="backbone")
public class BackboneController {
	private String type="type";
	@RequestMapping(value="group.do")
	public ModelAndView groupIndex(){
		ModelAndView mav=new ModelAndView("backbone-test");
		mav.addObject(type, "group");
		return mav;
	}
	
	@Autowired
	protected IdentityService identityService;
	
	/**
	 * 
	 * 获取group.json
	 * @return
	 */
	@RequestMapping(value = {"group"},method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Group> getGroupList(){
		List<Group> list=identityService.createGroupQuery().list();
		return list;
	}
	
	/**
	 * //注意：服务器端一定要返回json数据，否则永远都是触发fail，应该是parse出错导致,backbone默认请求的都是json数据
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value={"group/{groupId}"},method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value=HttpStatus.OK)
	public String delGroup(@PathVariable String groupId){
		identityService.deleteGroup(groupId);
		JSONObject tmpJSONObj = null;
		tmpJSONObj = new JSONObject();
		tmpJSONObj.put("status", "ok");
		tmpJSONObj.put("errorMessage", "中文错误消息");
		return tmpJSONObj.toString();
		//HttpHeaders headers=new HttpHeaders();
		//headers.setContentType(MediaType.TEXT_PLAIN);
		//return new ResponseEntity<String>("ok",headers,HttpStatus.OK);
	}
}