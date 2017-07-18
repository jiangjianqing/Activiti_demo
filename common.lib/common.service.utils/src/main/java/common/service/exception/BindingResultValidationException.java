package common.service.exception;

import javax.validation.ValidationException;

import org.springframework.validation.BindingResult;

public class BindingResultValidationException extends ValidationException {

	private static final long serialVersionUID = 1L;
	private String message="";

	public BindingResultValidationException(String message){
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

