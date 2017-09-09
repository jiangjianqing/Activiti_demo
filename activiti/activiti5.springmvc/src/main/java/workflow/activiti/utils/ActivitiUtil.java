package workflow.activiti.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
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
			logger.debug("generateFormValueMap正在处理动态表单...");
			if (formProperties!=null) {
				for(FormProperty prop:formProperties){
					if(prop.isWritable()){
						String propId = FP_PREFIX+prop.getId();
						//2017.08.29 为确保没有过多的垃圾数据保存到activiti数据库，所以要求将需要保存的数据加上'fp_'前缀
						String value=request.getParameter(propId);
						//String value=request.getParameter(prop.getId());
						formValues.put(prop.getId(), value);
					}
				}	
			}else {
				logger.debug("formProperties == null，正在处理sub task");
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
		setAuthenticatedUserId();
		FormService formService = SpringContextHolder.getBean(FormService.class);
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
		HistoryService historyService = SpringContextHolder.getBean(HistoryService.class);
		ModelAndView mav=new ModelAndView();
		Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		List<HistoricTaskInstance> subTasks = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(taskId).list();
		mav.addObject("task",task);
		
		//特别注意：获取流程的审批意见时，可以获取整个processInstance的，也可以获取指定task的
		//获取task的审批意见 应用模式还没有想好
		logger.debug("审批意见的应用模式还没有想好，要注意");
		String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
		Map<String,String> taskNames = new HashMap<String,String>();
		for(HistoricTaskInstance historicTaskInstance : list) {
			taskNames.put(historicTaskInstance.getId(), historicTaskInstance.getName());
		}
		mav.addObject("taskNames", taskNames);//方便获取comment是在哪个task提交
		
		if (task.getParentTaskId()!=null) {
			HistoricTaskInstance parentTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).singleResult();
			mav.addObject("parentTask", parentTask);
			//subtask没有流程实例，所以其comment需要通过TASK类型获取
			mav.addObject("comments", ActivitiUtil.getComments(ActivitiDataType.TASK, taskId, null));
		}else {
			//普通task的comment可以直接通过PROCESSINSTANCE获取
			mav.addObject("comments", ActivitiUtil.getComments(ActivitiDataType.PROCESSINSTANCE, processInstanceId, null));
		}
		mav.addObject("subTasks",subTasks);

		boolean hasFormKey= false;
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		if (taskFormData != null) {//当taskFormData == null时，为sub task
			hasFormKey=taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
			if(task.getFormKey()!=null){
				Object renderFormData=formService.getRenderedTaskForm(taskId);		
				mav.addObject("formData", renderFormData);
			}else{
				mav.addObject("formData", taskFormData);
			}
		}
		mav.addObject("hasFormKey",hasFormKey);
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
		TaskService taskService = SpringContextHolder.getBean(TaskService.class);
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		
		if(taskFormData!=null) { //bpm中的任务使用formService.submitTaskFormData来完成任务
			boolean hasFormKey = taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
			List<FormProperty>  formProperties=taskFormData.getFormProperties();
			Map<String,String> formValues=ActivitiUtil.generateFormValueMap(hasFormKey,formProperties,request);//生成提交数据
			System.out.println(formValues);
			formService.submitTaskFormData(taskId, formValues);
		}else {//非常重要：当taskFormData == null时，为sub task,需要使用taskService.complete来完成任务
			taskService.complete(taskId, null);
		}
		
	}
	
	/**
	 * 添加activiti Comment（审批意见） ,审批意见是工作流引擎中一个不可或缺的模块
	 * 注意：意见总是和task相关，而人可以参与的任务只有用户任务，所以意见依附于用户任务
	 * @param taskId
	 * @param processInstanceId
	 * @param type
	 * @param message
	 */
	public static void addComment(String taskId,String processInstanceId,String type,String message) {
		setAuthenticatedUserId();
		TaskService taskService = SpringContextHolder.getBean(TaskService.class);
		if(processInstanceId==null || processInstanceId.trim().length()==0) {
			processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
		}
		if(type!=null && type.trim().length()>0) {
			taskService.addComment(taskId, processInstanceId, type, message);
		}else {
			taskService.addComment(taskId, processInstanceId, message);
		}
	
	}
	
	/**
	 * 获取指定类型的审批意见列表
	 * @param commentDataType
	 * @param id processInstanceId 或者 taskId
	 * @param type
	 * @return
	 */
	public static List<Comment> getComments(ActivitiDataType dataType,String id,String type){
		TaskService taskService = SpringContextHolder.getBean(TaskService.class);
		List<Comment> ret = null;
		switch(dataType) {
			case PROCESSINSTANCE:{
				if(type!=null && type.trim().length()>0) {
					ret = taskService.getProcessInstanceComments(id, type); 
				}else {
					ret = taskService.getProcessInstanceComments(id);
				}
				break;
			}
			case TASK:{
				if(type!=null && type.trim().length()>0) {
					ret = taskService.getTaskComments(id, type); 
				}else {
					ret = taskService.getTaskComments(id);
				}
				break;
			}
			default:{
				logger.error("ActivitiDataType 未处理，无法获取数据");
			}
				
		}
		
		return ret;
	}
	
	/**
	 * 获取附件清单
	 * @param dataType
	 * @param id
	 * @return
	 */
	public static List<Attachment> getAttachments(ActivitiDataType dataType,String id){
		TaskService taskService = SpringContextHolder.getBean(TaskService.class);
		List<Attachment> ret = null;
		switch(dataType) {
			case PROCESSINSTANCE:{
				ret = taskService.getProcessInstanceAttachments(id);
				break;
			}
			case TASK:{
				ret = taskService.getTaskAttachments(id);
				break;
			}
			default:{
				logger.error("ActivitiDataType 未处理，无法获取数据");
			}
		}
		
		return ret;
	}
	
	
	/**
	 * 设定当前操作activiti的用户,在需要修改activiti数据时一般都需要先进行本动作
	 * @return
	 */
	private static User setAuthenticatedUserId() {
		IdentityService identityService = SpringContextHolder.getBean(IdentityService.class);
		
		User user=(User)SessionHelper.getAuthenticatedUser();
		identityService.setAuthenticatedUserId(user.getUserName());//登录时已经执行过，20150905测试代码：有时StartUserID=null，导致任务无法继续处理
		logger.debug("注意观察StartUserID=空的情况，会导致任务无法处理");
		return user;
	}
}
