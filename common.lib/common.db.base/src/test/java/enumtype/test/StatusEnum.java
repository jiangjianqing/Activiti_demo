package enumtype.test;

import java.util.HashMap;
import java.util.Map;

import common.db.base.AbstractEnum;

/**
 * 状态枚举的范例，这里算是写了一个标准用法
 * 
 * @author jjq
 *
 */
public enum StatusEnum implements AbstractEnum<String> {
	// 这里的("xxx")其实就是下面构造函数的参数传入 StatusEnum(String code)
	// 再配合toString() ,最终实现了类似C++中的enum赋值效果

	PREPARE("PREP", "准备阶段"), INPROGRESS("INPR", "处理阶段"), FINISH("FNSH", "已完成");

	// 重要：code必须要么设置=null，要么设置为unique string，即使空字符串也可以

	private StatusEnum(String code, String description) {
			this.code = code;
			this.description = description;
		}

	private String code;
	private String description;

	// 取出当前Enum编码
	@Override
	public String getCode() {
		if (code != null) {
			return code;
		} else {
			return super.toString();
		}
	}

	@Override
	public String getDescription() {
		return description;
	}

	// 由于静态类无法覆盖Object的方法，子类需要添加如下方法
	@Override
	public String toString() {
		return getCode();
	}

	// 子类必须实现代码解析的静态方法
	public static StatusEnum parseCode(String code) {
		for (StatusEnum s : StatusEnum.values()) {
			if (s.getCode().equalsIgnoreCase(code))
				return s;
		}
		return null;
	}

	// 子类必须实现取列表的静态方法
	public static Map<String, String> getCodeAndDescriptions() {
		Map<String, String> ret = new HashMap<String, String>();
		for (StatusEnum s : StatusEnum.values()) {
			ret.put(s.getCode(), s.getDescription());
		}

		return ret;
	}
}
