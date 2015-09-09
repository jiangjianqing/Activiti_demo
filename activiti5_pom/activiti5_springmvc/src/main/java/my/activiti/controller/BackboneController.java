package my.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import my.activiti.bean.MyProcessDefinition;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Scope(value="session")
@Controller
@RequestMapping(value="backbone")
public class BackboneController {
	//-------------返回页面地址区域
	private String type="type";
	@RequestMapping(value="group.do")
	public ModelAndView groupIndex(){
		ModelAndView mav=new ModelAndView("backbone-test");
		mav.addObject(type, "group");
		return mav;
	}
	
	@RequestMapping(value="activiti_explorer.do")
	public ModelAndView activitiExplorerIndex(){
		ModelAndView mav=new ModelAndView("backbone-test");
		mav.addObject(type, "ActivitiExplorer");
		return mav;
	}
	
	//--------------------DI区域---------
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected IdentityService identityService;
	
	//-----------------------------------以下为restful功能区域----------------------
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
	
	/*
	@RequestMapping(value="group/{groupId}",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value=HttpStatus.OK,reason="op is successed")
	public String addGroup(@RequestParam String id,@RequestParam String name,@RequestParam String type){
		Group newGroup=identityService.newGroup(id);
		newGroup.setName(name);
		newGroup.setType(type);
		identityService.saveGroup(newGroup);
		JSONObject tmpJSONObj = new JSONObject();
		tmpJSONObj.put("status", "ok");
		return tmpJSONObj.toString();
	}*/
	

	/**
	 * 新增、修改Group，根据banckbone和activiti5的特点集成为一个方法,新增是POST，修改为PUT
	 * @param group
	 * @return
	 */
	@RequestMapping(value="group/{groupId}",method={RequestMethod.POST,RequestMethod.PUT},produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(value=HttpStatus.OK)
	public String modifyGroup(@RequestBody org.activiti.engine.impl.persistence.entity.GroupEntity  group){
		Group old=identityService.createGroupQuery().groupId(group.getId()).singleResult();
		if(old!=null){//20150903:这里不能直接用传递过来的group更新，会导致乐观锁异常ActivitiOptimisticLockingException
			old.setName(group.getName());
			old.setType(group.getType());
			identityService.saveGroup(old);
		}else{
			identityService.saveGroup(group);
		}
		
		System.out.println(String.format("id=%s,name=%s,type=%s",group.getId(),group.getName(),group.getType()));
		JSONObject tmpJSONObj = new JSONObject();
		tmpJSONObj.put("status", "ok");
		return tmpJSONObj.toString();
	}
	
	//----------------------------以下为process区域-------------
	@RequestMapping(value = {"process"},method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<MyProcessDefinition> getProcessList(){
		
		//org.springframework.http.converter.HttpMessageNotWritableException: 
		//Could not write content: Direct self-reference leading to cycle (through reference chain: java.util.ArrayList[0]->org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity["processDefinition"]); nested exception is com.fasterxml.jackson.databind.JsonMappingException: Direct self-reference leading to cycle (through reference chain: java.util.ArrayList[0]->org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity["processDefinition"])
		//at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.writeInternal(AbstractJackson2HttpMessageConverter.java:238)
		//20150909,直接返回repositoryService.createProcessDefinitionQuery().list();会出现上述错误
		List<ProcessDefinition> pd_list=repositoryService.createProcessDefinitionQuery().list();
		List<MyProcessDefinition> my_pd_list=new ArrayList<MyProcessDefinition>();
		for(ProcessDefinition pd:pd_list){
			MyProcessDefinition my_pd=new MyProcessDefinition();
			my_pd.setId(pd.getId());
			my_pd.setName(pd.getName());
			my_pd.setCategory(pd.getCategory());
			my_pd.setDeploymentId(pd.getDeploymentId());
			my_pd.setKey(pd.getKey());
			my_pd.setDiagramResourceName(pd.getDiagramResourceName());
			my_pd.setDescription(pd.getDescription());
			my_pd.setResourceName(pd.getResourceName());
			my_pd.setVersion(pd.getVersion());
			my_pd_list.add(my_pd);
		}
		return my_pd_list;
	}
	
	@RequestMapping(value="process/{deploymentId}",method={RequestMethod.DELETE})
	@ResponseStatus(value=HttpStatus.OK)
	public void deleteDeployment(@PathVariable String deploymentId){
		//注意：这里删除的是一个部署，用spring统一部署的时候会使得N个流程定义都使用一个部署id，导致全部被删除
		repositoryService.deleteDeployment(deploymentId, true);
	}
}
