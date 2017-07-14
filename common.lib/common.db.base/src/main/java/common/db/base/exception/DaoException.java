package common.db.base.exception;

public class DaoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String exceptionName;
	private String msg;

	public DaoException(String exceptionName,String msg){
		super(msg);
		this.exceptionName=exceptionName;
		this.msg=msg;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
