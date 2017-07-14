package common.db.base.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import common.db.base.validator.annotation.Min_Sample;

public class MinValidator implements ConstraintValidator<Min_Sample, Integer> {

	private int minValue;  
	
	private String moneyReg = "^\\d+(\\.\\d{1,2})?$";//表示金额的正则表达式  
    private Pattern moneyPattern = Pattern.compile(moneyReg);  
	
	@Override
	public void initialize(Min_Sample arg0) {
		//把Min限制类型的属性value赋值给当前ConstraintValidator的成员变量minValue  
	    minValue = arg0.value();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext arg1) {
		//在这里我们就可以通过当前ConstraintValidator的成员变量minValue访问到当前限制类型Min的value属性了  
	    return value >= minValue;  	     
	}
	
	public boolean isValid_1(Double value, ConstraintValidatorContext arg1) {  
	       // TODO Auto-generated method stub  
	       if (value == null)  
	           return true;  
	       return moneyPattern.matcher(value.toString()).matches();  
	    }  

}
