package my.activiti.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import common.web.utils.SessionHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("/workflow")
public class ActivitiListController {
	//private static final Logger log = LoggerFactory
			//.getLogger(getClass());

	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	@RequestMapping(value = "/process-list")
	public ModelAndView getProcessList(){
		ModelAndView mav=new ModelAndView("workflow/process-list");
		List<ProcessDefinition> processDefinitionList=repositoryService.createProcessDefinitionQuery().list();
		mav.addObject("processDefinitionList", processDefinitionList);
		return mav;
	}
	
	/**
	 * 获取用户列表
	 * @return
	 */
	@RequestMapping(value={"user","user/list"},method=RequestMethod.GET)
	public ModelAndView getUserList(HttpSession session,ModelMap modelMap){
		List<User> list=identityService.createUserQuery().list();
		ModelAndView mav=new ModelAndView("identity/user-list");
		mav.addObject("userlist", list);
		
		SessionHelper sessionHelper = new SessionHelper(session);
		Object obj=sessionHelper.getAuthenticatedUser();
		if(obj!=null){
			User user=(User)obj;
			//读取直接分配给当前用户或已经签收的任务
			List<Task> doingTasks=taskService.createTaskQuery().taskAssignee(user.getId()).list();
			//等待签收的任务
			List<Task> wattingClaimTasks=taskService.createTaskQuery().taskCandidateUser(user.getId()).list();
			//合并两种任务
			List<Task> allTasks=new ArrayList<Task>();
			allTasks.addAll(doingTasks);
			allTasks.addAll(wattingClaimTasks);
			//activiti5.16以后提供的方法，一步就可以获取上述两种任务
			//List<Task> allTasks=taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();
			mav.addObject("taskList",allTasks);
			
			List<HistoricProcessInstance> hisProcessInstanceList=historyService.createHistoricProcessInstanceQuery()
					/*.finished().involvedUser(user.getId())*/.list();
			mav.addObject("historicProcessInstanceList",hisProcessInstanceList);
		}
		
		return mav;
	}

	@RequestMapping(value = "/delete-deployment")
	public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId){
		System.out.println("deleteProcessDefinition invoked,deploymentId="+deploymentId);
		//注意：这里删除的是一个部署，用spring统一部署的时候会使得N个流程定义都使用一个部署id，导致全部被删除
		repositoryService.deleteDeployment(deploymentId, true);

		return "redirect:/workflow/process-list";
	}

	@RequestMapping(value = "/read-resource")
	public void readResource(@RequestParam("pdid") String processDefinitionId,
							 @RequestParam("resourceName") String resourceName,HttpServletResponse response) throws Exception{
		ProcessDefinitionQuery pdq=repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd=pdq.processDefinitionId(processDefinitionId).singleResult();
		InputStream resourceAsStream=repositoryService.getResourceAsStream(pd.getDeploymentId(),resourceName);
		byte[] b=new byte[1024];
		int len=-1;
		while ((len=resourceAsStream.read(b,0,1024))!=-1){
			response.getOutputStream().write(b,0,len);
		}
	}

	/***
	 *  文件上传功能，该功能需要在bean配置中添加multipartResolver，并引入commons-fileupload包
	 *  否则会出现下面的错误
  		org.springframework.web.util.NestedServletException: 
  		Request processing failed; nested exception is java.lang.IllegalArgumentException: 
  		Expected MultipartHttpServletRequest: is a MultipartResolver configured?
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/deploy")
	public String deploy(@RequestParam(value="file") MultipartFile file){
		String filenName=file.getOriginalFilename();
		try{
			InputStream fileInputStream=file.getInputStream();

			String extension= FilenameUtils.getExtension(filenName);
			DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
			if(extension.equals("zip") || extension.equals("bar")){
				ZipInputStream zip=new ZipInputStream(fileInputStream);
				deploymentBuilder.addZipInputStream(zip);
			}else{
				deploymentBuilder.addInputStream(filenName,fileInputStream);
			}
			deploymentBuilder.deploy();
		}catch (Exception ex){
			//log.error("error on deploy process,because of file input stream");
		}
		return "redirect:/workflow/process-list";
	}
	
	@RequestMapping(value = "/redeploy/all")
	public String redeployAll() throws Exception{
		return "redirect:/workflow/process-list";
	}
	
	@RequestMapping(value = "/start-process/{processDefinitionId}")
	public ModelAndView readStartForm(@PathVariable("processDefinitionId") String processDefinitionId){
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();
		ModelAndView mav=new ModelAndView("workflow/start-process-form");
		// 根据是否有formkey属性判断使用哪个展示层
		// 判断是否有formkey属性
        if (hasStartFormKey) {
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            mav.addObject("startFormData", renderedStartForm);
        } else { // 动态表单字段
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            mav.addObject("startFormData", startFormData);
        }
        mav.addObject("processDefinition", processDefinition);
        mav.addObject("hasStartFormKey", hasStartFormKey);
		return mav;
	}
	
	@RequestMapping(value="/start-process-instance/{processDefinitionId}")
	public String startProcessInstance(@PathVariable String processDefinitionId,HttpSession session,HttpServletRequest request){
		String url="";
		SessionHelper sessionHelper = new SessionHelper(session);
		Object obj=sessionHelper.getAuthenticatedUser();
		if(obj!=null){
			User user=(User)obj;
			//identityService.setAuthenticatedUserId(user.getId());//登录时已经执行过，20150905测试代码：有时StartUserID=null，导致任务无法继续处理
			System.out.println("注意观察StartUserID=空的情况，会导致任务无法处理");
			StartFormData formData=formService.getStartFormData(processDefinitionId);
			List<FormProperty> formProperties=formData.getFormProperties();
			boolean hasFormKey=formData.getFormKey()!=null && formData.getFormKey().length()>0;
			Map<String,String> formValues=generateFormValueMap(hasFormKey,formProperties,request);
			formService.submitStartFormData(processDefinitionId, formValues);//生成提交数据
			
			url="redirect:/workflow/process-list";
		}else{
			System.out.println("当前没有登陆的用户");
			url="redirect:/identity/user";
		}
		return url;
	}
	
	/**
	 * claim一个任务，需要taskid和userid两个参数
	 * @param taskId
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/claim-task/{taskId}")
	public String claimTask(@PathVariable String taskId,HttpSession session){
		SessionHelper sessionHelper = new SessionHelper(session);
		Object obj=sessionHelper.getAuthenticatedUser();
		if(obj!=null){
			User user=(User)obj;
			taskService.claim(taskId, user.getId());
		}else{
			System.out.println("claimTask异常，当前没有登录用户");
		}
		String url="redirect:/identity/user";
		return url;
	}
	
	@RequestMapping(value="/do-task/{taskId}")
	public ModelAndView doTask(@PathVariable String taskId){
		ModelAndView mav=new ModelAndView("workflow/task-form");
		Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		boolean hasFormKey=taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
		mav.addObject("task",task);
		mav.addObject("hasFormKey",hasFormKey);
		if(task.getFormKey()!=null){
			Object renderFormData=formService.getRenderedTaskForm(taskId);		
			mav.addObject("taskFormData", renderFormData);
		}else{
			mav.addObject("taskFormData", taskFormData);
		}
		return mav;
	}
	
	@RequestMapping(value="/complete-task/{taskId}")
	public String completeTask(@PathVariable String taskId,HttpServletRequest request){
		TaskFormData taskFormData=formService.getTaskFormData(taskId);
		boolean hasFormKey=taskFormData.getFormKey()!=null && taskFormData.getFormKey().length()>0;
		List<FormProperty> formProperties=taskFormData.getFormProperties();
		Map<String,String> formValues=generateFormValueMap(hasFormKey,formProperties,request);//生成提交数据
		formService.submitTaskFormData(taskId, formValues);
		return "redirect:/identity/user";
	}
	
	/**
	 * 根据输入的参数生成提交数据，process和task通用
	 * @param hasFormKey
	 * @param formProperties
	 * @param request
	 * @return
	 */
	private Map<String,String> generateFormValueMap(boolean hasFormKey,List<FormProperty> formProperties,HttpServletRequest request){
		Map<String,String> formValues=new HashMap<String,String>();
		if(hasFormKey){//外置表单
			Map<String,String[]> parameterMap=request.getParameterMap();
			Set<Entry<String,String[]>> entrySet=parameterMap.entrySet();
			for(Entry<String,String[]> entry:entrySet){
				String key=entry.getKey();
				formValues.put(key, entry.getValue()[0]);
			}
		}else{//动态表单
			for(FormProperty prop:formProperties){
				if(prop.isWritable()){
					String value=request.getParameter(prop.getId());
					formValues.put(prop.getId(), value);
				}
			}
		}	
		return formValues;
	}
}
