package my.activiti.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import common.db.model.identity.Role;
import common.db.model.identity.RoleTypeEnum;
import common.db.repository.identity.RoleDao;
import common.db.repository.identity.UserDAO;
import common.service.utils.SpringContextHolder;

public class IdentifyServiceTest extends BaseSpringActivitiTester {
	
	@Before
	public void setUp() throws Exception {
		//identityService=activitiRule.getIdentityService();
		if (identityService==null){
			fail("identityService没有初始化");
		}
		
		if (taskService==null){
			fail("taskService没有初始化");
		}
	}
	
	@Test
	public void testUser() throws Exception {
		common.db.model.identity.User user = new common.db.model.identity.User();
		user.setUserName("jjq");
		user.setPassword("1234567");
		user.setFirstName("蒋");
		user.setLastName("建清");
		user.setEmail("cz_jjq@qq.com");
		UserDAO userDAO=SpringContextHolder.getBean(UserDAO.class);
		userDAO.insert(user);
		/*
		User user=identityService.newUser("jjq");
		//...
		identityService.saveUser(user);
		*/
		
		User userInDb=identityService.createUserQuery().userId("jjq").singleResult();
		assertNotNull(userInDb);
		//h2 的view 无法删除数据 
		/*
		identityService.deleteUser("jjq");
		System.out.println(String.format("新增用户信息，first name=%s，last name=%s",userInDb.getFirstName(),userInDb.getLastName()));
		userInDb=identityService.createUserQuery().userId("jjq").singleResult();
		assertNull(userInDb);*/
		System.out.println("User测试成功");
	}

	@Test
	public void testGroup() throws Exception{
		RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
		Role role = new Role();
		role.setName("deptLeader");
		role.setType(RoleTypeEnum.USER);
		roleDao.insert(role);
		/*
		Group group=identityService.newGroup("deptLeader");
		group.setName("部门领导");
		group.setType("assignment");//1、assignment   2、security-role
		identityService.saveGroup(group);
		*/
		
		List<Group> groupList=identityService.createGroupQuery().groupId("deptLeader").list();
		assertEquals(1,groupList.size());
		System.out.println(String.format("新增Group信息，Group name=%s",groupList.get(0).getName()));
		//h2 的view 无法删除数据 
		/*identityService.deleteGroup("deptLeader");
		groupList=identityService.createGroupQuery().groupId("deptLeader").list();
		assertEquals(0,groupList.size());
		*/
		System.out.println("Group测试成功");
	}
}
