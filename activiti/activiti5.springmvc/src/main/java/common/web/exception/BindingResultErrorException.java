package common.web.exception;

import org.springframework.validation.BindingResult;

public class BindingResultErrorException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message="";

	public BindingResultErrorException(String message){
		this.message=message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

