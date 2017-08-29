package workflow.activiti.web.controller.view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import common.db.model.identity.User;
import common.web.controller.AbstractViewController;
import common.web.utils.SessionHelper;
import workflow.activiti.utils.ActivitiUtil;

@Controller
@RequestMapping("workflow/process")
public class ProcessManageController extends AbstractViewController{
	
	@Autowired
	protected IdentityService identityService;
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;
	
	@RequestMapping(value="")
	public ModelAndView getList() throws Exception {
		ModelAndView mav = new ModelAndView(getDefaultRequestMappingUrl());
		List<ProcessDefinition> processDefinitionList=repositoryService.createProcessDefinitionQuery().list();
		mav.addObject("processDefinitionList", processDefinitionList);
		return mav;
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
	
	@RequestMapping(value = "/deploy",method=RequestMethod.POST)
	public String deploy(@RequestParam(value="file") CommonsMultipartFile[] uploadFiles ) throws Exception{
		for(CommonsMultipartFile file : uploadFiles) {
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
				logger.error("error on deploy process,because of file input stream");
			}
		}
		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
	@RequestMapping(value = "/delete-deployment")
	public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId) throws Exception{
		logger.debug("deleteProcessDefinition invoked,deploymentId="+deploymentId);
		
		//2017.08.26：一个流程定义不能直接删除，而需要通过流程定义的部署ID删除。
		//注意：这里删除的是一个部署，用spring统一部署的时候会使得N个流程定义都使用一个部署id，导致全部被删除
		repositoryService.deleteDeployment(deploymentId, true);

		return "redirect:/"+getDefaultRequestMappingUrl();
	}
	
	/**
	 * 获取ProcessDefinition的启动Form，是启动Process的第一步
	 * @param processDefinitionId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/get-start-form/{processDefinitionId}",method=RequestMethod.GET)
	public ModelAndView getStartForm(@PathVariable("processDefinitionId") String processDefinitionId) throws Exception{
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();
		ModelAndView mav=new ModelAndView("workflow/process-get-start-form");
		// 根据是否有formkey属性判断使用哪个展示层
		// 判断是否有formkey属性
        if (hasStartFormKey) {
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            mav.addObject("startFormData", renderedStartForm);
        } else { // 动态表单字段
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            mav.addObject("startFormData", startFormData);
            System.out.println("startFormData.size="+startFormData.getFormProperties().size());
        }
        mav.addObject("processDefinition", processDefinition);
        mav.addObject("hasStartFormKey", hasStartFormKey);
        System.out.println("hasStartFormKey="+hasStartFormKey);
        
		return mav;
	}
	
	@RequestMapping(value="/start-process-instance/{processDefinitionId}",method=RequestMethod.POST)
	public String startProcessInstance(@PathVariable String processDefinitionId,HttpSession session,HttpServletRequest request) throws Exception{
		String url="";

		if(SessionHelper.isAuthenticated()){
			User user=(User)SessionHelper.getAuthenticatedUser();
			identityService.setAuthenticatedUserId(user.getUserName());//登录时已经执行过，20150905测试代码：有时StartUserID=null，导致任务无法继续处理
			System.out.println("注意观察StartUserID=空的情况，会导致任务无法处理");
			StartFormData formData=formService.getStartFormData(processDefinitionId);
			List<FormProperty> formProperties=formData.getFormProperties();
			boolean hasFormKey=formData.getFormKey()!=null && formData.getFormKey().length()>0;
			Map<String,String> formValues=ActivitiUtil.generateFormValueMap(hasFormKey,formProperties,request);
			formService.submitStartFormData(processDefinitionId, formValues);//生成提交数据
			
			url="redirect:/"+getDefaultRequestMappingUrl();
		}else{
			System.out.println("当前没有登陆的用户");
			url="redirect:/identity/user";
		}
		return url;
	}
	
	
}
