package identity.test;

import static org.junit.Assert.*;

import org.junit.Test;

import common.db.model.identity.RoleTypeEnum;

public class EnumTest {

	@Test
	public void testRoleTypeEnum() {
		RoleTypeEnum roleTypeEnum =RoleTypeEnum.ADMIN;
		
		System.out.println(roleTypeEnum);
	}

}
