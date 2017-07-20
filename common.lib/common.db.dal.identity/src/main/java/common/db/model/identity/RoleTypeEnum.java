package common.db.model.identity;

import java.util.HashMap;
import java.util.Map;

import common.db.base.AbstractEnum;

/**
 * 角色类型枚举对象
 * 
 * 展示了如何设定每个枚举的显示值
 * @author jjq
 *
 */
public enum RoleTypeEnum implements AbstractEnum<String>{
	ADMIN(null , "管理员"),USER(null ,"普通用户");
	
	//-------------------------以下基本都为标准写法，请勿修改-------------------------------
	
	//重要：code必须要么设置=null，要么设置为unique string，即使空字符串也可以
	
	private RoleTypeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	private String code;
	private String description;

	/**
	 * 标准的获取code写法，如果没有提供code，则用默认的super方式代替
	 */
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

	/**
	 * 利用toString 将原本默认的EnumName转为code输出
	 */
	@Override
	public String toString(){
		return getCode();
	}
	
	/**
	 * 判断传入的code是否是枚举中的值
	 * @param code
	 * @return
	 */
	public static RoleTypeEnum parseCode(String code) {
		for (RoleTypeEnum s : RoleTypeEnum.values()) {
			if (s.getCode().equalsIgnoreCase(code))
				return s;
		}
		return null;
	}
	
	public static Map<String, String> getCodeAndDescriptions(){
		Map<String , String> ret = new HashMap<String, String>();
		for (RoleTypeEnum s : RoleTypeEnum.values()){
			ret.put(s.getCode(), s.getDescription());
		}
		
		return ret;
	}
	
	/*  以下为不实用的范例
	ADMIN{
        @Override
        public String getDesc() {
            return "管理员";
        }           
    },USER{
        @Override
        public String getDesc() {
            return "普通用户";
        }           
    };

	public abstract String getDesc() ;
	*/
}
