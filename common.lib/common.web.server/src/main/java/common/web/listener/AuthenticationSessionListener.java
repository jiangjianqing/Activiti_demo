package common.web.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.aspectj.apache.bcel.classfile.Constant;

import common.service.utils.AbstractHelperClass;
import common.service.utils.SpringContextHolder;
import common.web.utils.SessionHelper;
import common.web.utils.SystemIntegrator;

/**
 * 记录用户的登录和注销
 * @author jjq
 *
 */
@WebListener
public class AuthenticationSessionListener extends AbstractHelperClass implements HttpSessionListener, HttpSessionAttributeListener {
    
	/**
	 * 特别注意：Listener由web-container创建,不能使用spring 注入bean ，需要SpringContextHolder的支持
	 */
	
	//private MemberLoginLogService memberLoginLogService;
    //private MemberInformationService memberInformationService;
    //private HttpServletRequest request;
    //private OperateLogService operateLogService;
    //public static final String SESSION_ATTRIBUTE_MEMBERLOGINRECORD = "memberLoginRecord";
    //创建固定大小的线程池
    ExecutorService executorService = Executors.newFixedThreadPool(20);
    @Override
    public void sessionCreated(HttpSessionEvent event) {
    	HttpSession session = event.getSession();
        // 国际化 语言环境,不一定在这里初始化,先注释
        // Locale locale = new Locale("zh", "CN");
        // event.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale);
        // event.getSession().setAttribute("lang","en-US");
        // 监听器不受SPRING 管理,所以使用此方式
        /*
    	if (ValidateUtil.isEmpty(this.memberLoginLogService)) {
            this.memberLoginLogService = (MemberLoginLogService) ApplicationContextHolder.getBean("memberLoginRecordService");
        }
        */
    	//logger.warn("创建 session");
        //logger.warn(session.toString());
        
        /*
        MemberLoginLogDto memberLoginLogDto = new MemberLoginLogDto(DateUtil.format(DateUtil.newDate(), DateUtil.C_TIME_PATTON_DEFAULT)).setAnonymous();
        int memberLoginRecordDtoId = memberLoginLogService.addMemberLoginLog(request, memberLoginLogDto);
        if(memberLoginRecordDtoId>0){
            memberLoginLogDto.setId(memberLoginRecordDtoId);
        }*/
        //session.setAttribute(WebSessionListener.SESSION_ATTRIBUTE_MEMBERLOGINRECORD,memberLoginLogDto);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        
        // 记录用户登出时间(以最后一次访问SESSION为准)
        HttpSession session = event.getSession();
        if(SessionHelper.isAuthenticated()==true){
        	SystemIntegrator systemIntegrator = SpringContextHolder.getBean(SystemIntegrator.class);
        	if (systemIntegrator != null){
        		systemIntegrator.onLogoff();
        	}
        	logger.debug("用户注销成功:"+SessionHelper.getAuthenticatedUser().getUsername());
        }
        /*
        if (!ValidateUtil.isEmpty(session.getAttribute(WebSessionListener.SPRING_SECURITY_CONTEXT))) {
            MemberLoginLogDto memberLoginRecordDto = (MemberLoginLogDto) session.getAttribute(WebSessionListener.SESSION_ATTRIBUTE_MEMBERLOGINRECORD);
            memberLoginRecordDto.setLogoutTime(DateUtil.format(new Date(session.getLastAccessedTime()), DateUtil.C_TIME_PATTON_DEFAULT));
            final MemberLoginLogDto finalMemberLoginRecordDto = memberLoginRecordDto;
            //匿名内部类 直接给一个线程发送数据到LOG系统,为了不阻塞程序
            executorService.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    memberLoginLogService.addMemberLoginLog(request, finalMemberLoginRecordDto);
                }
            }));
            
        } else {
            // 匿名会员 因为创建SESSION 已经新增了数据,如果没有登录,删除数据
            MemberLoginLogDto memberLoginRecordDto = (MemberLoginLogDto) session.getAttribute(WebSessionListener.SESSION_ATTRIBUTE_MEMBERLOGINRECORD);
            memberLoginRecordDto.setLogoutTime(DateUtil.format(new Date(session.getLastAccessedTime()), DateUtil.C_TIME_PATTON_DEFAULT));
            final MemberLoginLogDto finalMemberLoginRecordDto = memberLoginRecordDto;
            //匿名内部类 直接给一个线程发送数据到LOG系统,为了不阻塞程序
            executorService.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    memberLoginLogService.delMemberLoginLog(finalMemberLoginRecordDto);
                }
            }));
        }*/
        //因为系统销毁SESSION 可能不及时,所以手动清一下属性
        //session.removeAttribute(WebSessionListener.SESSION_ATTRIBUTE_MEMBERLOGINRECORD);
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        //记录用户实际登录时间非访问时间
        HttpSession session = event.getSession();
        
        if(event.getName() == SessionHelper.getAuthenticationAttributeName()
        		&& SessionHelper.isAuthenticated()){
        	SystemIntegrator systemIntegrator = SpringContextHolder.getBean(SystemIntegrator.class);
        	if (systemIntegrator != null){
        		systemIntegrator.onLogin();
        	}else{
        		logger.debug("没有找到SystemIntegrator接口的实现类，如果需要与其他系统集成，请提供该类");
        	}
        	
        	logger.debug("用户登录成功:"+SessionHelper.getAuthenticatedUser().getUsername());
        }
    	
        
        /*
        MemberLoginLogDto memberLoginRecordDto = (MemberLoginLogDto) session.getAttribute(WebSessionListener.SESSION_ATTRIBUTE_MEMBERLOGINRECORD);
        if (!ValidateUtil.isEmpty(memberLoginRecordDto)&&ValidateUtil.isEmpty(memberLoginRecordDto.getLoginTime())) {
            if(!ValidateUtil.isEmpty(session.getAttribute(WebSessionListener.SPRING_SECURITY_CONTEXT))){
                // 监听器不受SPRING 管理,所以使用此方式
                if (ValidateUtil.isEmpty(this.memberInformationService)) {
                    this.memberInformationService = (MemberInformationService) ApplicationContextHolder.getBean("memberInformationService");
                }
                UserDetails userDetails = (User) ((SecurityContext) session.getAttribute(WebSessionListener.SPRING_SECURITY_CONTEXT)).getAuthentication().getPrincipal();
                memberLoginRecordDto.setLoginTime(DateUtil.format(DateUtil.newDate(), DateUtil.C_TIME_PATTON_DEFAULT));
                MemberInformation memberInformation = this.memberInformationService.findMemberInformationByName(userDetails.getUsername());
                session.setAttribute(WebSessionListener.SESSION_ATTRIBUTE_MEMBERLOGINRECORD,setMemberLoginRecordMemberInformation(memberLoginRecordDto,memberInformation));
                if(ValidateUtil.isEmpty(this.operateLogService)){
                    this.operateLogService = (OperateLogService) ApplicationContextHolder.getBean("operateLogService");
                }
                OperateLogDto operateLogDto = new OperateLogDto(memberInformation.getMember_id(),OperateLogDto.ACTIVE_USER_LOGIN);
                operateLogService.addOperateLog(operateLogDto);
            }
        }
        */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
    	//在这里无法再次处理用户信息，改在sessionDestroyed中
    	/*if(event.getName() == sessionHelper.getSpringSecurityContextName()
        		&& sessionHelper.getSpringSecurityContext() == null){
        	logger.warn("用户登出");
        }*/
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
    }
    
    //设值
  /*
    private MemberLoginLogDto setMemberLoginRecordMemberInformation(MemberLoginLogDto memberLoginLogDto,MemberInformation memberInformation){
        memberLoginLogDto.setMemberId(memberInformation.getMember_id());
        memberLoginLogDto.setCreateByUserCode(memberInformation.getMember_id());
        memberLoginLogDto.setCreateByUserName(memberInformation.getMember_name());
        memberLoginLogDto.setLastModifyByUserCode(memberInformation.getMember_id());
        memberLoginLogDto.setLastModifyByUserName(memberInformation.getMember_name());
        return memberLoginLogDto;
    }
*/
      
}