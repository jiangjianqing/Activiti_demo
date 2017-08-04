package common.aop;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import common.aop.annotation.SystemControllerLog;
import common.aop.annotation.SystemServiceLog;
import common.service.utils.AbstractHelperClass;

@Aspect
public class SystemControllerLogAspect extends AbstractHelperClass {

	private final String POINT_CUT = "systemControllerLogAspect()";

	@Pointcut("@annotation(common.aop.annotation.SystemControllerLog)")
	private void systemControllerLogAspect() {
	};

	@Before(value = POINT_CUT)
	public void before(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
        HttpSession session = request.getSession();    
        //读取session中的用户    
        //User user = (User) session.getAttribute(WebConstants.CURRENT_USER);    
        //请求的IP    
        String ip = request.getRemoteAddr();    
         try {    
            //*========控制台输出=========*//    
            System.out.println("=====前置通知开始=====");    
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
            System.out.println("方法描述:" + AspectUtils.getControllerMethodDescription(joinPoint));    
            //System.out.println("请求人:" + user.getName());    
            System.out.println("请求IP:" + ip);    
            //*========数据库日志=========*// 
            /*
            Log log = SpringContextHolder.getBean("logxx");    
            log.setDescription(getControllerMethodDescription(joinPoint));    
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
            log.setType("0");    
            log.setRequestIp(ip);    
            log.setExceptionCode( null);    
            log.setExceptionDetail( null);    
            log.setParams( null);    
            log.setCreateBy(user);    
            log.setCreateDate(DateUtil.getCurrentDate());    
            //保存数据库    
            logService.add(log);    */
            System.out.println("=====前置通知结束=====");    
        }  catch (Exception e) {    
            //记录本地异常日志    
            logger.error("==前置通知异常==");    
            logger.error("异常信息:{}", e.getMessage());    
        }
	}

	/**
	 * 异常通知 ： 在捕捉到目标方法抛出异常时执行
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(value = POINT_CUT, throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Throwable e) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();    
        //读取session中的用户    
        //User user = (User) session.getAttribute(WebConstants.CURRENT_USER);    
        //获取请求ip    
        String ip = request.getRemoteAddr();    
        //获取用户请求方法的参数并序列化为JSON格式字符串    
        String params = "";    
        /*
        if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {    
             for ( int i = 0; i < joinPoint.getArgs().length; i++) {    
                params += JSONUtil.toJsonString(joinPoint.getArgs()[i]) + ";";    
            }    
        }   */ 
         try {    
              /*========控制台输出=========*/    
            System.out.println("=====异常通知开始=====");    
            System.out.println("异常代码:" + e.getClass().getName());    
            System.out.println("异常信息:" + e.getMessage());    
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
            System.out.println("方法描述:" + AspectUtils.getControllerMethodDescription(joinPoint));    
            //System.out.println("请求人:" + user.getName());    
            System.out.println("请求IP:" + ip);    
            System.out.println("请求参数:" + params);    
               /*==========数据库日志=========*/
            /*
            Log log = SpringContextHolder.getBean("logxx");    
            log.setDescription(getServiceMthodDescription(joinPoint));    
            log.setExceptionCode(e.getClass().getName());    
            log.setType("1");    
            log.setExceptionDetail(e.getMessage());    
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
            log.setParams(params);    
            log.setCreateBy(user);    
            log.setCreateDate(DateUtil.getCurrentDate());    
            log.setRequestIp(ip);    
            //保存数据库    
            logService.add(log);    
            */
            System.out.println("=====异常通知结束=====");    
        }  catch (Exception ex) {    
            //记录本地异常日志    
            logger.error("==异常通知异常==");    
            logger.error("异常信息:{}", ex.getMessage());    
        }    
         /*==========记录本地异常日志==========*/    
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);    
    
	}


}
