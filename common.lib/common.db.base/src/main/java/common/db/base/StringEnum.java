package common.db.base;

public interface StringEnum {
	// 实现类需要提供如下两个构造参数
	//String code;
	//String description = "";

	/**
	 * 返回code信息
	 * 
	 * @return
	 */
	public String getCode();

	/**
	* 返回Enum注释信息
	* @return
	*/
	public String getDescription();
			
}
