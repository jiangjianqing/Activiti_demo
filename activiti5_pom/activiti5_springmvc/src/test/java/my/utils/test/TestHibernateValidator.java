package my.utils.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.focusight.platform3.views.model.User;
import common.spring.ValidateUtil;

public class TestHibernateValidator {

	@Test
	public void test() {
		User user=new User();
		user.setName("测试员");
		user.setBirthday("1981-02-13");
		user.setPassword("123");
		String msg=ValidateUtil.validateModel(user);
		System.out.println("校验信息="+msg);
		assertNotEquals(0,msg.length());
	}

}
