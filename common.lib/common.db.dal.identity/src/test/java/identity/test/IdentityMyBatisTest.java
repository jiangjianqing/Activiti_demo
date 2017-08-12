package identity.test;

import static org.junit.Assert.*;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.service.spi.SessionFactoryServiceInitiator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import common.db.base.test.AbstractMyBatisTestCase;
import common.db.model.identity.Role;
import common.db.model.identity.RoleTypeEnum;
import common.db.repository.mybatis.identity.RoleDao;

public class IdentityMyBatisTest extends AbstractMyBatisTestCase {
	
	@Resource
	private RoleDao roleDao;
	//private SqlSessionFactory sessionFactory;
  
//  @Before  
//  public void before() {  
//      ac = new ClassPathXmlApplicationContext("applicationContext.xml");  
//      userService = (IUserService) ac.getBean("userService");  
//  }  
  
    @Test  
    public void test1() {  
    	
    	logger.warn("**注意：这里的测试会连接mysql并写入真实的数据，测试用例的写法还要完善!");
    	Role role=new Role();
    	role.setName("Mybatistest"+new Date().toString());
    	role.setType(RoleTypeEnum.ADMIN);
    	/*
    	try(SqlSession session=sessionFactory.openSession()){
			//session.getMapper获取的是一个代理对象
			//RoleDao roleDao=session.getMapper(RoleDao.class);

			//20170808 这里的事务处理不完善
			
			session.rollback();
			}*/
    	roleDao.insert(role);
    	//20170812 在map文件中加入就可以获得新ID <insert id="insert" keyProperty="id" useGeneratedKeys="true" 
    	logger.warn("new Role's id="+role.getId().toString());
    	
        //User user= new User();
        //user.setId(new Long(123));
        //UserDao.insert(user);  
        // System.out.println(user.getUserName());  
        // logger.info("值："+user.getUserName());  
        //logger.info(JSON.toJSONString(user));  
    }  
}
