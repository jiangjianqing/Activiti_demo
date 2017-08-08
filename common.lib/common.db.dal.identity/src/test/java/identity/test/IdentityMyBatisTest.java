package identity.test;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import common.db.model.identity.Role;
import common.db.model.identity.RoleTypeEnum;
import common.db.repository.mybatis.identity.RoleDao;


@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class IdentityMyBatisTest {

	protected final Logger logger = LoggerFactory
			.getLogger(getClass());
	
	@Resource
	private SqlSessionFactory sessionFactory;
  
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
    	try(SqlSession session=sessionFactory.openSession()){
			//session.getMapper获取的是一个代理对象
			RoleDao roleDao=session.getMapper(RoleDao.class);

			roleDao.insert(role);
			}
    	
        //User user= new User();
        //user.setId(new Long(123));
        //UserDao.insert(user);  
        // System.out.println(user.getUserName());  
        // logger.info("值："+user.getUserName());  
        //logger.info(JSON.toJSONString(user));  
    }  
}
