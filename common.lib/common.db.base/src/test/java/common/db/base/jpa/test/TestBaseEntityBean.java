package common.db.base.jpa.test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import common.db.base.jpa.test.entity.Item;
import common.db.base.jpa.test.entity.User;

/**
 * 当前尚未解决添加Boolean字段导致出错的问题
 * @author jjq
 *
 */
public class TestBaseEntityBean {
	
	@Test
	public void testUser() throws CloneNotSupportedException{
		User t1=new User();
		System.out.print(t1.getTableName());
		assertTrue(t1.getTableName().equals("user"));
		
		t1.setId(new Long(2));
		t1.setName("蒋建清");
		
		//复制后应该判定为相等
		User t2=(User)t1.clone();
		assertTrue(t1.equals(t2));
		assertTrue(t1.equalsPK(t2));
		
		//修改后应该判定为不相等
		t2.setAttributeValue("name", "wl");
		//t2.setName("wl");
		assertFalse(t1.equals(t2));
		assertTrue(t1.equalsPK(t2));
		
		//检查字符串
		assertTrue("wl".equals(t2.getAttributeValue("name")));
		
		assertTrue(t2.getDifferentFields(t1).size()==1);
		assertTrue(t2.getDifferentFields(t1).indexOf("name")==0);
		
		t2.setId(new Long(123));
		assertTrue(t2.getDifferentFields(t1).size()==2);
		assertTrue(t2.getDifferentFields(t1).indexOf("id")==1);
		assertFalse(t1.equalsPK(t2));
		
		
		System.out.println(t1);
		System.out.println(t1.hashCode());
		System.out.println(t2);
		System.out.println(t2.hashCode());
		t2.setId(t2.getId()+1);
		System.out.println(t2.hashCode());
		
		System.out.println("getDifferentField:");
		t2.copyAttributeValue(t1, new String[]{"id","name"});
		assertTrue(t2.equals(t1));
		//t2.setId(t1.getId());
		//t2.setName(t1.getName());
		System.out.println(t2.hashCode());
		assertTrue(t2.getDifferentFields(t1).size()==0);
		System.out.println("getAttributeNames:");
		for(int i=0;i<t2.getAttributeNames().length;i++){
			String attrName=t2.getAttributeNames()[i];
			System.out.println(attrName+":"+t2.getAttributeValue(attrName));
		}
		//fail("Not yet implemented");
	}
	
	@Ignore
	@Test
	public void testItem() throws CloneNotSupportedException {
		//20160129：尚未完成测试的功能有：
		//loadLazyAttributes,getEnumDescription
		Item t1=new Item();
		System.out.print(t1.getTableName());
		assertTrue(t1.getTableName().equals("item"));
		
		t1.setId(2);
		t1.setName("蒋建清");
		
		//复制后应该判定为相等
		Item t2=(Item)t1.clone();
		assertTrue(t1.equals(t2));
		assertTrue(t1.equalsPK(t2));
		
		//修改后应该判定为不相等
		t2.setAttributeValue("name", "wl");
		//t2.setName("wl");
		assertFalse(t1.equals(t2));
		assertTrue(t1.equalsPK(t2));
		
		//检查字符串
		assertTrue("wl".equals(t2.getAttributeValue("name")));
		
		assertTrue(t2.getDifferentFields(t1).size()==1);
		assertTrue(t2.getDifferentFields(t1).indexOf("name")==0);
		
		t2.setId(123);
		assertTrue(t2.getDifferentFields(t1).size()==2);
		assertTrue(t2.getDifferentFields(t1).indexOf("id")==1);
		assertFalse(t1.equalsPK(t2));
		
		
		System.out.println(t1);
		System.out.println(t1.hashCode());
		System.out.println(t2);
		System.out.println(t2.hashCode());
		t2.setId(t2.getId()+1);
		System.out.println(t2.hashCode());
		
		System.out.println("getDifferentField:");
		t2.copyAttributeValue(t1, new String[]{"id","name"});
		assertTrue(t2.equals(t1));
		//t2.setId(t1.getId());
		//t2.setName(t1.getName());
		System.out.println(t2.hashCode());
		assertTrue(t2.getDifferentFields(t1).size()==0);
		System.out.println("getAttributeNames:");
		for(int i=0;i<t2.getAttributeNames().length;i++){
			String attrName=t2.getAttributeNames()[i];
			System.out.println(attrName+":"+t2.getAttributeValue(attrName));
		}
		//fail("Not yet implemented");
	}

}
