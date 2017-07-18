package common.web.model;

import java.io.Serializable;

/**
 * 本类用于对返回前端的JSON数据进行格式化处理
 * @author jjq
 *
 */
public class WrappedResponseBody implements Serializable{
	private Boolean hasError = false;
	private String message = "";
	private Object data;
	
	public WrappedResponseBody(Exception ex , Object data){
		this.init(ex , data);
	}
	
	public WrappedResponseBody(Exception ex ){
		this.init(ex , null);
	}
	
	public WrappedResponseBody(Object data){
		this.init(null , data);
	}
	
	private void init(Exception ex , Object data){
		hasError = (ex != null);
		if (hasError){
			//对exception 进行处理
		}
		this.data = data;
	}

	public Boolean getHasError() {
		return hasError;
	}

	public void setHasError(Boolean hasError) {
		this.hasError = hasError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}
