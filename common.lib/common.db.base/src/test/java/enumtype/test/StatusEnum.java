package enumtype.test;

import common.db.base.StringEnum;

/**
 * 状态枚举的范例，这里算是写了一个标准用法
 * @author jjq
 *
 */
public enum StatusEnum implements StringEnum{
	//这里的("xxx")其实就是下面构造函数的参数传入 StatusEnum(String code)
	//再配合toString() ,最终实现了类似C++中的enum赋值效果
	
	PREPARE("PREP" , "准备阶段"), INPROGRESS("INPR" , "处理阶段"), FINISH("FNSH" , "已完成");
	
	private String code;
	private String description;

	private StatusEnum(String code , String description) {
		this.code = code; 
		this.description = description;
	}

	/**
	 * 重要：将code作为返回值，可以实现c++中对enum赋值的效果
	 */
	@Override
	public String toString() {
		return code;
	}

	/**
	 * 判断传入的code是否是枚举中的值
	 * @param code
	 * @return
	 */
	public static StatusEnum parseCode(String code) {
		for (StatusEnum s : StatusEnum.values()) {
			if (s.code.equalsIgnoreCase(code))
				return s;
		}
		return null;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}
}
