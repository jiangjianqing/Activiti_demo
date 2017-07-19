package enumtype.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class EnumTest {

	@Test
	public void test() {
		System.out.println("testEnumValue>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		StatusEnum status = StatusEnum.PREPARE;
		
		System.out.println("利用toString()将code与name进行转化 ： StatusEnum.PREPARE => "+status);

	}
	
	@Test
	public void testParseCode() {	
		System.out.println("testParseCode>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		StatusEnum status = StatusEnum.parseCode("123");
		assertTrue(status == null);
		status = StatusEnum.parseCode("PREP");
		System.out.println("读取description ：StatusEnum.parseCode('PREP') => "+status.getDescription());
		assertTrue(status == StatusEnum.PREPARE);
	}

}
