package common.db.base;

public interface  AbstractEnum<T> {

	/**
	 * 返回code信息
	 * @return
	 */
	public T getCode();
	
	/**
	 * 返回Enum注释信息
	 * @return
	 */
	public String getDescription();
	
	//20170720：非常重要 在接口中无法定义子类必须实现的静态类 ,以下都为标准写法
	/*
	 * 
	ADMIN(null , "管理员"),USER(null ,"普通用户");
	
	//重要：code必须要么设置=null，要么设置为unique string，即使空字符串也可以
	
	private RoleTypeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	private String code;
	private String description;
	
	//取出当前Enum编码
	@Override
	public String getCode() {
		if(code!=null){
			return code;
		}else{
			return super.toString();
		}
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	//由于静态类无法覆盖Object的方法，子类需要添加如下方法
	@Override
	public String toString(){
		return getCode();
	}
	
	//子类必须实现代码解析的静态方法
	public static RoleTypeEnum parseCode(String code) {
		for (RoleTypeEnum s : RoleTypeEnum.values()) {
			if (s.getCode().equalsIgnoreCase(code))
				return s;
		}
		return null;
	}
	
	//子类必须实现取列表的静态方法
	public static Map<String, String> getCodeAndDescriptions(){
		Map<String , String> ret = new HashMap<String, String>();
		for (RoleTypeEnum s : RoleTypeEnum.values()){
			ret.put(s.getCode(), s.getDescription());
		}
		
		return ret;
	}
	
	*/
}
