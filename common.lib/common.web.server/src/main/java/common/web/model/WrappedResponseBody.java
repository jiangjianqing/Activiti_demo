package common.web.model;

import java.io.Serializable;

/**
 * 本类用于对返回前端的JSON数据进行格式化处理
 * @author jjq
 *
 */
public class WrappedResponseBody implements Serializable{
/*
 * 这里考虑部分兼容FSA标准：
	一个Action要符合 FSA(Flux Standard Action) 规范，需要满足如下条件：
    1、是一个纯文本对象
    2、只具备 type 、payload、error 和 meta 中的一个或者多个属性。type 字段不可缺省，其它字段可缺省
    3、若 Action 报错，error 字段不可缺省，切必须为 true

	*/
	
	/**
	 * 返回的是否为错误信息
	 */
	private Boolean error = false;
	/**
	 * 待补充
	 */
	private String message = "";
	/**
	 * 返回的数据体
	 */
	private Object payload;
	
	public WrappedResponseBody(Exception ex , Object data){
		this.init(ex , data);
	}
	
	public WrappedResponseBody(Exception ex ){
		this.init(ex , null);
	}
	
	public WrappedResponseBody(Object payload){
		this.init(null , payload);
	}
	
	private void init(Exception ex , Object payload){
		error = (ex != null);
		if (error){
			//对exception 进行处理
		}
		this.payload = payload;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getPayload() {
		return payload;
	}

	public void setData(Object payload) {
		this.payload = payload;
	}
	
	
}
