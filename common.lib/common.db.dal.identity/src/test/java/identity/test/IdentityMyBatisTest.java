package identity.test;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import common.db.base.test.AbstractMyBatisTestCase;
import common.db.model.identity.Role;
import common.db.model.identity.RoleTypeEnum;
import common.db.model.identity.User;
import common.db.repository.mybatis.identity.RoleDao;
import common.db.repository.mybatis.identity.UserDao;

public class IdentityMyBatisTest extends AbstractMyBatisTestCase {

	
//  private ApplicationContext ac = null;  
	
    @Resource  
    private RoleDao roleDao;  
  
//  @Before  
//  public void before() {  
//      ac = new ClassPathXmlApplicationContext("applicationContext.xml");  
//      userService = (IUserService) ac.getBean("userService");  
//  }  
  
    @Test  
    public void test1() {  
    	Role role=new Role();
    	role.setName("Mybatistest");
    	role.setType(RoleTypeEnum.ADMIN);
    	roleDao.insert(role);
        //User user= new User();
        //user.setId(new Long(123));
        //UserDao.insert(user);  
        // System.out.println(user.getUserName());  
        // logger.info("值："+user.getUserName());  
        //logger.info(JSON.toJSONString(user));  
    }  
}
