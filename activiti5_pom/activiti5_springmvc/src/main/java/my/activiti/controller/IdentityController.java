package my.activiti.controller;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="identity")
public class IdentityController {

	@Autowired
	protected IdentityService identityService;
	
	/**
	 * 
	 * 获取group.json
	 * @return
	 */
	@RequestMapping(value = {"grouplist.json","group/list.json","group.json"}, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Group> getGroupList(){
		List<Group> list=identityService.createGroupQuery().list();
		return list;
	}
	
	/**
	 * 获得一个用户组的所有成员
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value="group/member/{groupId}",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> getMemberOfGroup(@PathVariable String groupId){
		return identityService.createUserQuery().memberOfGroup(groupId).list();
	}
	
	/**
	 * 获取用户组列表
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value={"group/list","group"},method=RequestMethod.GET)
	public String getGroup(ModelMap modelMap){
		List<Group> list=identityService.createGroupQuery().list();
		modelMap.put("grouplist", list);
		return "identity/group-list";
	}	
	
	/**
	 * 添加用户组
	 * @param id
	 * @param name
	 * @param type
	 * @return
	 */
	@RequestMapping(value="group/add",method=RequestMethod.POST)
	public String addGroup(@RequestParam String id,@RequestParam String name,@RequestParam String type){
		Group newGroup=identityService.newGroup(id);
		newGroup.setName(name);
		newGroup.setType(type);
		identityService.saveGroup(newGroup);
		return "redirect:/identity/group/list";
	}
	
	/**
	 * 删除用户组
	 * @param id
	 * @return
	 */
	@RequestMapping(value="group/del/{id}")
	public String delGroup(@PathVariable String id){
		Group group=identityService.createGroupQuery().groupId(id).singleResult();
		if(group!=null){
			identityService.deleteGroup(id);
		}
		return "redirect:/identity/group/list";
	}
	
	/**
	 * 向用户组添加用户
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	@RequestMapping(value="user/addgroup",method=RequestMethod.POST)
	public ResponseEntity<String> createMembership(@RequestParam String userId,@RequestParam(required=false) String[] groupIds){
		//将该用户原来的用户组信息清空
		List<Group> oldGroupList=identityService.createGroupQuery().groupMember(userId).list();
		for(Group grp:oldGroupList){
			identityService.deleteMembership(userId, grp.getId());
		}
		//添加新的用户组信息
		if(groupIds!=null){
			for(String groupid:groupIds){
				System.out.println(String.format("createMembership，userid=%s,groupid=%s", userId,groupid));
				identityService.createMembership(userId, groupid);
			}		
		}
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		//headers.setAcceptCharset(Charset.);
		//headers.setAccept({MediaType.TEXT_PLAIN_VALUE});
		headers.add("myHead", "test header");
		ResponseEntity<String> ret=new ResponseEntity<String>("成功",headers,HttpStatus.OK);
		//20150830 注意：这里返回的成功会显示乱码，尚未解决
		return ret;
	}
	
	/**
	 * 取得用户所在的用户组
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="user/groups/{userId}")
	@ResponseBody
	public List<Group> getUserGroups(@PathVariable String userId){
		List<Group> list=identityService.createGroupQuery().groupMember(userId).list();
		return list;
	}
	
	/**
	 * 获取用户列表
	 * @return
	 */
	@RequestMapping(value={"user","user/list"},method=RequestMethod.GET)
	public ModelAndView getUserList(){
		List<User> list=identityService.createUserQuery().list();
		ModelAndView mav=new ModelAndView("identity/user-list");
		mav.addObject("userlist", list);
		return mav;
	}
	
	/**
	 * 添加用户
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @return
	 */
	@RequestMapping(value="user/add",method=RequestMethod.POST)
	public String addUser(@RequestParam String id
				,@RequestParam String firstName
				,@RequestParam String lastName
				,@RequestParam String email
				,@RequestParam String password
			){
		User newUser=identityService.newUser(id);
		newUser.setEmail(email);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setPassword(password);
		identityService.saveUser(newUser);
		return "redirect:/identity/user/list";
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	@RequestMapping(value="user/del/{id}")
	public String removeUser(@PathVariable String id){
		identityService.deleteUser(id);
		return "redirect:/identity/user/list";
	}
}
