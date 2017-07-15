package common.web.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



import common.db.base.exception.DaoException;
import common.db.base.exception.NoFieldChangedException;
import common.db.base.exception.OutOfPageRangeException;
import common.web.exception.BindingResultErrorException;
import common.web.utils.AbstractHelperClass;

@ControllerAdvice
public class WebControllerAdvice extends AbstractHelperClass {

    @InitBinder  
    public void initBinder(WebDataBinder binder) {  

    }

    /**
     * 使用jpa中的异常对象进行数据反馈
     * @param ex
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)  
    @ResponseStatus(value = HttpStatus.NOT_FOUND)  
    @ResponseBody
	public String processEntityNotFoundException(EntityNotFoundException ex){
		return ex.getMessage();
	}

    @ExceptionHandler(OutOfPageRangeException.class)  
    @ResponseStatus(value = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)  
    @ResponseBody
    public String processOutOfPageRangeException(OutOfPageRangeException ex){
    	return "page值异常";

    }

    @ExceptionHandler(BindingResultErrorException.class)  
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)  
    @ResponseBody
    public String processValidateFailedException(BindingResultErrorException ex){
    	return ex.getMessage();
    }

    @ExceptionHandler(DaoException.class)  
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)  
    @ResponseBody
    public String processDaoException(DaoException ex){

    	logger.error("encouter DaoException："+ex.getExceptionName());
    	logger.error(ex.getMessage());
    	return ex.getExceptionName()+":"+"DaoException异常";
    }

    @ExceptionHandler(NoFieldChangedException.class)  
    @ResponseStatus(value = HttpStatus.OK)  
    @ResponseBody
    public String processNoFieldChangedException(NoFieldChangedException ex){
    	return ex.getEntityName()+ex.getMessage();
    }

    public String buildResponseString(){

    	String ret="";
    	return ret;
    }
}

