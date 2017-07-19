package common.db.base;

public interface NumberEnum {
	//实现类需要提供如下两个构造参数
	
	//Integer code = null;
	//String description = "";
	/**
	 * 返回code信息
	 * @return
	 */
	public Integer getCode();
	
	/**
	 * 返回Enum注释信息
	 * @return
	 */
	public String getDescription();
	
}
