package common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.json.JSONObject;

import common.service.utils.AbstractHelperClass;

//@Aspect
public class ApiServiceAspect extends AbstractHelperClass {
	
	private final String POINT_CUT = "execution(* common.web.controller.rest.*.*(..))"; 
	
	
	private void anyMethod(){};
	
	public ApiServiceAspect(){
		logger.warn("ApiServiceAspect created----------------------"); 
	}
	
	/**
	 * 前置通知 ： 在目标方法被调用之前执行
	 * @param joinPoint
	 */
	//@Before(value=POINT_CUT)
	public void before(JoinPoint joinPoint){
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		StringBuilder log = new StringBuilder();
		log.append("before: ")
			.append(className)
			.append("@")
			.append(methodName)
			.append(" , params: ");
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			log.append(JSONObject.valueToString(arg) + ", ");
		}
		System.out.println(log.toString());
		logger.warn(log.toString()); 
	}
	
	/**
	 * 后置通知 ： 在目标方法被调用之后执行
	 * @param joinPoint
	 * @param returnObj
	 */
	public void afterReturning(JoinPoint joinPoint , Object returnObj){
		
	}
	
	/**
	 * 异常通知 ： 在捕捉到目标方法抛出异常时执行
	 * @param joinPoint
	 * @param e
	 */
	public void afterThrowing(JoinPoint joinPoint, Throwable e){
		
	}
	
	/**
	 * 最终通知 ： 不管目标方法调用成功与否，均在调用完毕之后执行
	 * @param joinPoint
	 */
	public void after(JoinPoint joinPoint) {
		
	}
	
	/**
	 * 环绕通知 ： 在目标方法被调用前后均可进行操作
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
    public Object around(ProceedingJoinPoint pjp) throws Throwable {  
        // 调用核心逻辑  
        Object retVal = pjp.proceed();  
        return retVal;  
    }
}
