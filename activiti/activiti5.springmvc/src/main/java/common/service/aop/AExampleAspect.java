package common.aop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;

import common.service.utils.AbstractHelperClass;

@Aspect
public class AExampleAspect extends AbstractHelperClass {
	
	/* 重要：可以使用@annotation语法来标记需要aop的方法
	//Service层切点    
    @Pointcut("@annotation(com.annotation.SystemServiceLog)")    
     public  void serviceAspect() {    
    }    
    
    //Controller层切点    
    @Pointcut("@annotation(com.annotation.SystemControllerLog)")    
     public  void controllerAspect() {    
    }   
    */
	
	private final String POINT_CUT = "anyMethod()"; 
	
	
	@Pointcut("execution(* common.web.controller.rest.*.*(..))")  
	private void anyMethod(){};
	
	
	
	/**
	 * 前置通知 ： 在目标方法被调用之前执行
	 * @param joinPoint
	 * 
	 * 
	 * 还有另一种写法
	 * @Before("anyMethod() && args(object)")  
    	public void doBefore(Object object) {  
        	//System.out.println("前置通知" + object);  
    	}  
	 */
	@Before(value=POINT_CUT)
	public void before(JoinPoint joinPoint){
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		StringBuilder log = new StringBuilder();
		log.append("before: ")
			.append(className)
			.append("@")
			.append(methodName);
		//获取输入参数
		Object[] args = joinPoint.getArgs();
		if (args!=null && args.length>0){
			log.append(" , params: ");
			for (Object arg : args) {
				log.append(JSONObject.valueToString(arg) + ", ");
			}
		}else{
			log.append(" , no params: ");
		}
		logger.warn(log.toString()); 
	}
	
	/**
	 * 后置通知 ： 在目标方法被调用之后执行
	 * @param joinPoint
	 * @param returnObj
	 */
	@AfterReturning(value = POINT_CUT, returning = "returnObj") 
	public void afterReturning(JoinPoint joinPoint , Object returnObj){

		logger.warn("afterReturning="+returnObj.toString());
	}
	
	/**
	 * 异常通知 ： 在捕捉到目标方法抛出异常时执行
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(value = POINT_CUT, throwing = "e") 
	public void afterThrowing(JoinPoint joinPoint, Throwable e){
		
		logger.error("-------------------afterThrowing.handler.start-------------------");  
		logger.error("afterThrowing: "+e.getMessage(), e); 
        /*
		if(!des.equals("")){  
            logger.error("方法描述：" + des);  
        }  */
        //logger.error(getMethodNameAndArgs(joinPoint));  
        //log.error("ConstantUtil.getTrace(e): " + getTrace(e));  
  
        logger.error("异常名称：" + e.getClass().toString());  
        logger.error("-------------------afterThrowing.handler.end------------------");
		// 在这里判断异常，根据不同的异常返回错误。  
        //if (e.getClass().equals(DataAccessException.class)) {  
	}
	
	/**
	 * 最终通知 ： 不管目标方法调用成功与否，均在调用完毕之后执行
	 * @param joinPoint
	 */
	@After(value = POINT_CUT)
	public void after(JoinPoint joinPoint) {
		
	}
	
	/**
	 * 环绕通知 ： 在目标方法被调用前后均可进行操作
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around(value = POINT_CUT) 
    public Object around(ProceedingJoinPoint pjp) throws Throwable {  
        // 调用核心逻辑  
        //Object retVal = pjp.proceed();  
        //return retVal;  
        
        Long begin = System.currentTimeMillis();
        StringBuilder log = new StringBuilder("around: ");
        Object result = null;
        try {
        	result = pjp.proceed();
        } catch (Exception e) {
        	logger.error(log + e.getMessage(), e);
        }
        Long end = System.currentTimeMillis();
        log.append(" 执行时间: ")
        	.append(end-begin)
        	.append("ms");
        logger.warn(log.toString());
        
     // 调用方法名称  
        String methodName = pjp.getSignature().getName();  
      //获取进入的类名  
        String className = pjp.getSignature().getDeclaringTypeName();  
        className = className.substring(className.lastIndexOf(".") + 1).trim(); 
        
        //如果是日志类服务就不用处理  
        if(className.equals("SysLogServiceImpl")
        		||className.equals("TotalMsgServiceImpl")
        		||className.equals("TotalUserServiceImpl")
        		||className.equals("MessageServiceImpl")){  
            return result;  
        } 
        //如果是添加/删除/修改等操作
        if(Pattern.matches("(add|update|delete)[\\S]*",  
                methodName)) {  
        	//用户管理  
            if(className.equals("UserServiceImpl")){    
            	/*
            	if(user!=null){  
                    logMsg="用户名："+user.getCname()+"-在-"+"操作用户";  
                }else{  
                    logMsg="";  
                } */ 
            }
        }
        return result;  
    }
	
	/** 
     * 将异常信息输出到log文件 
     * @param t 
     * @return 
     */  
    public static String getTrace(Throwable t) {          
        StringWriter stringWriter= new StringWriter();          
        PrintWriter writer= new PrintWriter(stringWriter);          
        t.printStackTrace(writer);          
        StringBuffer buffer= stringWriter.getBuffer();         
        return buffer.toString();      
    } 
}
