package workflow.activiti.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import common.db.model.identity.User;
import common.service.utils.SpringContextHolder;
import common.web.utils.SessionHelper;

public class ActivitiUtil {
	
	//由AbstractHelperClass提供的静态类方法支持函数，必须放在子类中
	protected final static String getStaticClassName(){
					return new Object() {
						//静态方法中获取当前类名
						public String getClassName() {
							String className = this.getClass().getName();
							return className.substring(0, className.lastIndexOf('$'));
						}
					}.getClassName();
				}
				
		
	protected final static Logger logger = LoggerFactory
						.getLogger(getStaticClassName());
		//------------------static 方法模板定义结束---------------------	
	
	private static String FP_PREFIX="bpmn_fp_";
	/**
	 * 根据输入的参数生成提交数据，process和task通用
	 * @param hasFormKey
	 * @param formProperties
	 * @param request
	 * @return
	 */
	private static Map<String,String> generateFormValueMap(boolean hasFormKey,List<FormProperty> formProperties,HttpServletRequest request){
		Map<String,String> formValues=new HashMap<String,String>();
		if(hasFormKey){//外置表单
			Map<String,String[]> parameterMap=request.getParameterMap();
			Set<Entry<String,String[]>> entrySet=parameterMap.entrySet();
			for(Entry<String,String[]> entry:entrySet){
				String key=entry.getKey();
				//formValues.put(key, entry.getValue()[0]);
				//2017.08.29 为确保没有过多的垃圾数据保存到activiti数据库，所以要求将需要保存的数据加上'fp_'前缀
				if(key.startsWith(FP_PREFIX)) {
					formValues.put(key.substring(FP_PREFIX.length()), entry.getValue()[0]);
				}else {
					//logger.debug();
				}
				
			}
		}else{//动态表单
			for(FormProperty prop:formProperties){
				if(prop.isWritable()){
					String propId = FP_PREFIX+prop.getId();
					//2017.08.29 为确保没有过多的垃圾数据保存到activiti数据库，所以要求将需要保存的数据加上'fp_'前缀
					String value=request.getParameter(propId);
					//String value=request.getParameter(prop.getId());
					formValues.put(prop.getId(), value);
				}
			}
		}	
		return formValues;
	}
	
	/**
	 * 流程启动步骤1：获取启动流程的Form参数
	 * @param processDefinitionId
	 * @return
	 */
	public static ModelAndView getProcessStartForm(String processDefinitionId){
		FormService formService = SpringContextHolder.getBean(FormService.class);
		RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();
		ModelAndView mav=new ModelAndView();
		// 根据是否有formkey属性判断使用哪个展示层
		// 判断是否有formkey属性
        if (hasStartFormKey) {
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            mav.addObject("formData", renderedStartForm);
        } else { // 动态表单字段
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            mav.addObject("formData", startFormData);
        }
        mav.addObject("processDefinition", processDefinition);
        mav.addObject("hasStartFormKey", hasStartFormKey);
        mav.addObject("FP_PREFIX", FP_PREFIX);
		return mav;
	}
	
	/**
	 * 流程启动步骤2：根据用户输入的form数据启动流程
	 * @param processDefinitionId
	 * @param request
	 */
	public static void  startProcessInstance(String processDefinitionId,HttpServletRequest request) {
		FormService formService = SpringContextHolder.getBean(FormService.class);
		IdentityService identityService = SpringContextHolder.getBean(IdentityService.class);
		
		User user=(User)SessionHelper.getAuthenticatedUser();
		identityService.setAuthenticatedUserId(user.getUserName());//登录时已经执行过，20150905测试代码：有时StartUserID=null，导致任务无法继续处理
		logger.debug("注意观察StartUserID=空的情况，会导致任务无法处理");
		StartFormData formData=formService.getStartFormData(processDefinitionId);
		List<FormProperty> formProperties=formData.getFormProperties();
		boolean hasFormKey=formData.getFormKey()!=null && formData.getFormKey().length()>0;
		Map<String,String> formValues=ActivitiUtil.generateFormValueMap(hasFormKey,formProperties,request);
		formService.submitStartFormData(processDefinitionId, formValues);//生成提交数据
	}
	
	/**
	 * Task步骤1：获取完成当前任务的Form
	 * @param taskId
	 * @return
	 */
	public static ModelAndView getTaskForm(String taskId){
		FormService formService = SpringContextHolder.getBean(FormService.class);
		TaskService taskService = SpringContextHolder.getBean(TaskService.class);
		ModelAndView mav=new ModelAndView();
		Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		boolean hasFormKey=taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
		mav.addObject("task",task);
		mav.addObject("hasFormKey",hasFormKey);
		if(task.getFormKey()!=null){
			Object renderFormData=formService.getRenderedTaskForm(taskId);		
			mav.addObject("formData", renderFormData);
		}else{
			mav.addObject("formData", taskFormData);
		}
		mav.addObject("FP_PREFIX", FP_PREFIX);
		return mav;
	}
	
	/**
	 * Task步骤2：根据用户输入的form数据完成Task
	 * @param taskId
	 * @param request
	 */
	public static void completeTask(String taskId,HttpServletRequest request) {
		FormService formService= SpringContextHolder.getBean(FormService.class);
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		boolean hasFormKey=taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
		List<FormProperty> formProperties=taskFormData.getFormProperties();
		Map<String,String> formValues=ActivitiUtil.generateFormValueMap(hasFormKey,formProperties,request);//生成提交数据
		formService.submitTaskFormData(taskId, formValues);
	}
}
